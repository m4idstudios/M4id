package dev.doctorm4id.m4id.platform.neoforge.bridge

import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.registration.M4idAutoRegistrar
import dev.doctorm4id.m4id.bridge.M4idSharedApi
import dev.doctorm4id.m4id.ext.register
import dev.doctorm4id.m4id.message.M4idMsg
import dev.doctorm4id.m4id.platform.bridge.BridgePlatform
import dev.doctorm4id.m4id.serial.toSimplePacketCodec
import kotlinx.serialization.KSerializer
import net.minecraft.core.Registry
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLPaths
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.ClientPayloadContext
import net.neoforged.neoforge.network.handling.IPayloadHandler
import net.neoforged.neoforge.network.handling.ServerPayloadContext
import net.neoforged.neoforge.network.registration.HandlerThread
import java.nio.file.Path

class M4idSharedApiForge : M4idSharedApi {

    companion object {
        private const val NETWORK_PROTOCOL_VERSION = "1"
    }

    init {
        M4id.Logger.debug("M4id Shared API (Forge) Initialized.")
    }

    override val platform: BridgePlatform
        get() = BridgePlatform.NEOFORGE

    // Event methods

    override fun isOnClient(): Boolean {
        return FMLEnvironment.dist == Dist.CLIENT
    }

    override fun isOnServer(): Boolean {
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER
    }

    // Messaging

    private val clientMsgMap = mutableListOf<ForgeMsgData<M4idMsg>>()
    private val serverMsgMap = mutableListOf<ForgeMsgData<M4idMsg>>()

    data class ForgeMsgData<M : M4idMsg>(val ser: KSerializer<M>, val type: CustomPacketPayload.Type<M>) {
        val streamCodec = ser.toSimplePacketCodec()
        val payloadHandler = IPayloadHandler<M> { p0, p1 ->
            p1.enqueueWork {
                when (p1) {
                    is ClientPayloadContext -> { p0.onClientReceived() }
                    is ServerPayloadContext -> { p0.onServerReceived(M4idMsg.MsgContext(p1.player())) }
                    else -> throw Exception("No valid payload context for this message serializer: $ser")
                }
            }.exceptionally { throwable ->
                throwable.printStackTrace()
                return@exceptionally null
            }
        }
    }

    // normally subscribeevent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(NETWORK_PROTOCOL_VERSION).executesOn(HandlerThread.NETWORK)
        for (serverMsg in serverMsgMap) {
            M4id.Logger.info("Registering ServerMsg: ${serverMsg.type.id}")
            registrar.playToServer(serverMsg.type, serverMsg.streamCodec, serverMsg.payloadHandler)
        }
        for (clientMsg in clientMsgMap) {
            M4id.Logger.info("Registering ClientMsg: ${clientMsg.type.id}")
            registrar.playToClient(clientMsg.type, clientMsg.streamCodec, clientMsg.payloadHandler)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <M : M4idMsg> registerClientMessage(
        serializer: KSerializer<M>,
        id: CustomPacketPayload.Type<M>
    ): Boolean {
        clientMsgMap.add(ForgeMsgData(serializer as KSerializer<M4idMsg>, id as CustomPacketPayload.Type<M4idMsg>))
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun <M : M4idMsg> registerServerMessage(
        serializer: KSerializer<M>,
        id: CustomPacketPayload.Type<M>
    ): Boolean {
        serverMsgMap.add(ForgeMsgData(serializer as KSerializer<M4idMsg>, id as CustomPacketPayload.Type<M4idMsg>))
        return true
    }

    override fun <M : M4idMsg> sendMsgToServer(msg: M) {
        PacketDistributor.sendToServer(msg)
    }

    override fun <M : M4idMsg> sendMsgToClient(msg: M, player: ServerPlayer) {
        PacketDistributor.sendToPlayer(player, msg)
    }

    override fun getConfigDir(): Path {
        return FMLPaths.CONFIGDIR.get()
    }

    override fun <T> register(autoReg: M4idAutoRegistrar, reg: Registry<T>, thingId: String, obj: T): T {
        reg.register(ResourceLocation.fromNamespaceAndPath(autoReg.getId(), thingId), obj)
        return obj
    }
}
