package dev.doctorm4id.m4id.message

import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.bridge.M4idBridge
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass

class M4idMessageApi internal constructor() {

    init {
        M4id.Logger.debug("M4id Message API Initialized.")
    }

    @PublishedApi
    internal val payloadMap = mutableMapOf<KClass<out M4idMsg>, CustomPacketPayload.Type<*>>()

    inline fun <reified C : M4idMsg> registerClientMessage(id: ResourceLocation, ser: KSerializer<C> = serializer<C>()) {
        val clazz = C::class
        val payloadId = CustomPacketPayload.Type<C>(id)
        payloadMap[clazz] = payloadId
        M4idBridge.registerClientMessage(ser, payloadId)
    }

    inline fun <reified S : M4idMsg> registerServerMessage(id: ResourceLocation, ser: KSerializer<S> = serializer<S>()) {
        val clazz = S::class
        val payloadId = CustomPacketPayload.Type<S>(id)
        payloadMap[clazz] = payloadId
        M4idBridge.registerServerMessage(ser, payloadId)
    }

}