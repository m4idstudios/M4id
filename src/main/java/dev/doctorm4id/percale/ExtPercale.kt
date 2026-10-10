package dev.doctorm4id.percale

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.*
import dev.doctorm4id.percale.encoder.PassEncoder
import dev.doctorm4id.percale.decoder.PassDecoder
import dev.doctorm4id.percale.reverse.toSerializer
import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.contextual
import kotlin.jvm.optionals.getOrNull

// ### Encoding ###

@PublishedApi
internal fun <T, U : Any> encodeWithDynamicOps(serializer: SerializationStrategy<U>, obj: U, ops: DynamicOps<T>, serialMod: SerializersModule): T? {
    val encoder = PassEncoder.pickEncoder(serializer.descriptor, ops, serialMod)
    encoder.encodeSerializableValue(serializer, obj)
    return encoder.getResult()
}

inline fun <T, reified U : Any> DynamicOps<T>.serialize(obj: U): T? {
    return encodeWithDynamicOps(serializer<U>(), obj, this, EmptySerializersModule())
}

fun <T, U : Any> DynamicOps<T>.serialize(
    obj: U,
    serializer: SerializationStrategy<U>,
    serialMod: SerializersModule = EmptySerializersModule()
): T? {
    return encodeWithDynamicOps(serializer, obj, this, serialMod)
}


// ### Decoding ###

@PublishedApi
internal fun <T, U : Any> decodeWithDynamicOps(serializer: DeserializationStrategy<U>, obj: T, ops: DynamicOps<T>, serialMod: SerializersModule): U {
    val decoder = PassDecoder.pickDecoder(serializer.descriptor, ops, obj, 0, serialMod)
    //println("Picked: ${decoder::class.simpleName}")
    return serializer.deserialize(decoder)
}

inline fun <T, reified U : Any> DynamicOps<in T>.deserialize(obj: T): U {
    return decodeWithDynamicOps(serializer<U>(), obj, this, EmptySerializersModule())
}

fun <T, U : Any> DynamicOps<T>.deserialize(
    obj: T,
    serializer: DeserializationStrategy<U>,
    serialMod: SerializersModule = EmptySerializersModule()
): U {
    return decodeWithDynamicOps(serializer, obj, this, serialMod)
}

// ### Codec

fun <U : Any> KSerializer<U>.toCodec(serialMod: SerializersModule = EmptySerializersModule()): Codec<U> {
    return object : Codec<U> {
        override fun <T : Any> encode(input: U, ops: DynamicOps<T>, prefix: T?): DataResult<T> {
            // DFU expects failures as a DataResult, not as exceptions
            val result = try {
                encodeWithDynamicOps(this@toCodec, input, ops, serialMod)
            } catch (e: Exception) {
                return DataResult.error<T> { "Failed to encode ${this@toCodec.descriptor.serialName}: ${e.message}" }
            } ?: return DataResult.error<T> { "Encoding ${this@toCodec.descriptor.serialName} produced no result" }

            if (prefix == null || prefix == ops.empty()) {
                return DataResult.success(result)
            }

            ops.getMap(result).result().getOrNull()?.let { mapEntries ->
                return ops.mergeToMap(prefix, mapEntries)
            }

            ops.getStream(result).result().getOrNull()?.toList()?.let { listEntries ->
                return ops.mergeToList(prefix, listEntries)
            }

            return ops.mergeToPrimitive(prefix, result)
        }

        override fun <T : Any?> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<U, T>> {
            return try {
                val result = decodeWithDynamicOps(this@toCodec, input, ops, serialMod)
                DataResult.success(Pair(result, input))
            } catch (e: Exception) {
                DataResult.error<Pair<U, T>> { "Failed to decode ${this@toCodec.descriptor.serialName}: ${e.message}" }
            }
        }
    }
}

inline fun <reified A : Any> SerializersModuleBuilder.contextualCodec(codec: Codec<A>) {
    contextual(codec.toSerializer())
}
