package dev.doctorm4id.m4id.bridge

import dev.doctorm4id.m4id.registration.M4idAutoRegistrar
import dev.doctorm4id.m4id.message.M4idMsg
import kotlinx.serialization.KSerializer
import net.minecraft.core.Registry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import java.nio.file.Path

interface M4idSharedApi {

    val platform: BridgePlatform

    fun isOnClient(): Boolean
    fun isOnServer(): Boolean

    fun <M : M4idMsg> registerClientMessage(
        serializer: KSerializer<M>,
        id: CustomPacketPayload.Type<M>
    ): Boolean

    fun <M : M4idMsg> sendMsgToClient(
        msg: M,
        player: ServerPlayer
    )

    fun <M : M4idMsg> registerServerMessage(
        serializer: KSerializer<M>,
        id: CustomPacketPayload.Type<M>
    ): Boolean

    fun <M : M4idMsg> sendMsgToServer(msg: M)

    fun <T> register(
        autoReg: M4idAutoRegistrar,
        reg: Registry<T>,
        thingId: String,
        obj: T
    ): T

    fun getConfigDir(): Path
}
