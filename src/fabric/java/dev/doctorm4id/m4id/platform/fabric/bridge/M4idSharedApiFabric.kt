package dev.doctorm4id.m4id.platform.fabric.bridge

//? fabric {

import dev.doctorm4id.m4id.registration.M4idAutoRegistrar
import dev.doctorm4id.m4id.registration.M4idRegistrar
import dev.doctorm4id.m4id.bridge.M4idSharedApi
import dev.doctorm4id.m4id.message.M4idMsg
import dev.doctorm4id.m4id.bridge.BridgePlatform
import dev.doctorm4id.m4id.serial.toSimplePacketCodec
import kotlinx.serialization.KSerializer
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.Registry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer
import java.nio.file.Path

class M4idSharedApiFabric : M4idSharedApi {

    override val platform: BridgePlatform
        get() = BridgePlatform.FABRIC

    // Messaging

    override fun isOnClient(): Boolean {
        return FabricLoader.getInstance().environmentType == EnvType.CLIENT
    }

    override fun isOnServer(): Boolean {
        return FabricLoader.getInstance().environmentType == EnvType.SERVER
    }

    override fun <M : M4idMsg> registerClientMessage(serializer: KSerializer<M>, id: CustomPacketPayload.Type<M>): Boolean {
        PayloadTypeRegistry.playS2C().register(id, serializer.toSimplePacketCodec())
        if (!isOnClient()) {
            return true
        }

        @Suppress("UNCHECKED_CAST")
        return clientMessageRegistrar?.invoke(id as CustomPacketPayload.Type<out M4idMsg>) ?: true
    }

    override fun <M : M4idMsg> registerServerMessage(serializer: KSerializer<M>, id: CustomPacketPayload.Type<M>): Boolean {
        PayloadTypeRegistry.playC2S().register(id, serializer.toSimplePacketCodec())
        return ServerPlayNetworking.registerGlobalReceiver(id) { payload, context ->
            (payload as M4idMsg).onServerReceived(M4idMsg.MsgContext(context.player()))
        }
    }

    override fun <M : M4idMsg> sendMsgToClient(msg: M, player: ServerPlayer) {
        ServerPlayNetworking.send(player, msg)
    }

    override fun <M : M4idMsg> sendMsgToServer(msg: M) {
        clientMessageSender?.invoke(msg)
    }

    // Registration

    override fun <T> register(autoReg: M4idAutoRegistrar, reg: Registry<T>, thingId: String, obj: T): T {
        return M4idRegistrar.register(autoReg, reg, thingId, lazyOf(obj)).value
    }

    override fun getConfigDir(): Path {
        return FabricLoader.getInstance().configDir
    }

    companion object {
        var clientMessageRegistrar: ((CustomPacketPayload.Type<out M4idMsg>) -> Boolean)? = null
        var clientMessageSender: ((M4idMsg) -> Unit)? = null
    }
}

//? }
