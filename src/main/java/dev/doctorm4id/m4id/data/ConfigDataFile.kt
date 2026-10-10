package dev.doctorm4id.m4id.data

import kotlinx.serialization.KSerializer
import java.io.File

class ConfigDataFile(src: File) : DataFile(src) {
    private var loaded = false

    init {
        M4idPersistence.configDataFiles.add(this)
    }

    override fun <T : Any> request(key: String, serializer: KSerializer<T>, default: T) {
        super.request(key, serializer, default)
        if (!loaded) {
            load()
            loaded = true
        }
    }
}