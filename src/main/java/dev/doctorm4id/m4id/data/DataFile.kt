package dev.doctorm4id.m4id.data

import dev.doctorm4id.m4id.M4id
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.serializer
import java.io.File
import java.nio.file.AtomicMoveNotSupportedException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.collections.iterator
import kotlin.properties.ReadWriteProperty

abstract class DataFile(val src: File) {

    internal data class DataRequest<T : Any>(val serializer: KSerializer<T>, val default: T) {
        val encoded: JsonElement
            get() {
                return M4id.Serial.Format.encodeToJsonElement(serializer, default)
            }

        fun encode(value: Any?): JsonElement {
            return M4id.Serial.Format.encodeToJsonElement(serializer, value as? T ?: default)
        }

        fun decode(data: JsonElement): T {
            return M4id.Serial.Format.decodeFromJsonElement(serializer, data)
        }
    }

    operator fun <T : Any> invoke(
        key: String,
        serializer: KSerializer<T>,
        default: () -> T
    ): ReadWriteProperty<Any, T> {
        return DataProperty(key, default, serializer, this)
    }

    inline fun <reified T : Any> of(
        key: String? = null,
        noinline default: () -> T
    ): ReadWriteProperty<Any, T> {
        return DataProperty(key, default, serializer(), this)
    }

    private val lock = Any()

    private val requests = mutableMapOf<String, DataRequest<*>>()

    private var results = mutableMapOf<String, JsonElement>()

    private val resultSerializer = MapSerializer(String.serializer(), JsonElement.serializer())

    private val loadedObjects = mutableMapOf<String, Any>()

    /**
     * True once the file has been read from disk. Keys requested after that point are filled in
     * straight away, since [load] won't run again for them.
     */
    private var fileRead = false

    fun hasRequested(key: String) = synchronized(lock) { key in requests }

    open fun <T : Any> request(key: String, serializer: KSerializer<T>, default: T) {
        synchronized(lock) {
            val request = DataRequest(serializer, default)
            requests[key] = request
            if (fileRead) {
                populate(key, request)
            }
        }
    }

    /** Fills [loadedObjects] for [key] from the stored value, or the default if there isn't a usable one. */
    private fun populate(key: String, request: DataRequest<*>) {
        val stored = results.getOrPut(key) { request.encoded }
        loadedObjects[key] = try {
            request.decode(stored)
        } catch (e: Exception) {
            M4id.Logger.error("Could not read '$key' from ${src.name}, using its default value instead", e)
            request.default
        }
    }

    open fun load() {
        synchronized(lock) {
            results = readResults()
            fileRead = true

            for ((reqId, reqData) in requests) {
                populate(reqId, reqData)
            }
        }
    }

    private fun readResults(): MutableMap<String, JsonElement> {
        val file = src
        file.absoluteFile.parentFile?.mkdirs()

        if (!file.exists()) {
            writeAtomically(M4id.Serial.Format.encodeToString(resultSerializer, emptyMap()))
            return mutableMapOf()
        }

        val contents = file.readText().ifBlank { "{}" }

        return try {
            M4id.Serial.Format.decodeFromString(resultSerializer, contents).toMutableMap()
        } catch (e: Exception) {
            // A hand-edited or damaged file shouldn't crash the game: keep a copy and fall back to defaults
            val backup = File(file.path + ".corrupt")
            M4id.Logger.error("Could not parse ${file.name}; keeping a copy as ${backup.name} and using defaults", e)
            runCatching { file.copyTo(backup, overwrite = true) }
            mutableMapOf()
        }
    }

    open fun save() {
        synchronized(lock) {
            // Nothing was ever read, so there's nothing to write (and the file mustn't be overwritten with an empty one)
            if (!fileRead) return

            // Start from what is already stored so keys nobody requested this session aren't dropped
            val out = results.toMutableMap()

            for ((objId, obj) in loadedObjects) {
                val data = requests[objId] ?: continue
                out[objId] = data.encode(obj)
            }

            results = out
            writeAtomically(M4id.Serial.Format.encodeToString(resultSerializer, out))
        }
    }

    /** Writes to a temp file next to [src] and moves it into place, so a crash can't leave a half-written file. */
    private fun writeAtomically(text: String) {
        val target = src.absoluteFile.toPath()
        target.parent?.let { Files.createDirectories(it) }
        val temp = target.resolveSibling(target.fileName.toString() + ".tmp")
        Files.writeString(temp, text)
        try {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
        } catch (e: AtomicMoveNotSupportedException) {
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING)
        }
    }

    // These are only called via the delegate properties, so casting is guaranteed to succeed

    @Suppress("UNCHECKED_CAST")
    internal open fun <R : Any> loadResult(key: String): R {
        synchronized(lock) {
            return loadedObjects[key] as? R
                ?: error("'$key' has not been loaded from ${src.name} yet")
        }
    }

    internal fun <R : Any> setResult(key: String, value: R) {
        synchronized(lock) {
            loadedObjects[key] = value
        }
    }

    companion object {
        fun forServer(file: File) = ServerDataFile(file)
        fun forClient(file: File) = ConfigDataFile(file)
    }
}
