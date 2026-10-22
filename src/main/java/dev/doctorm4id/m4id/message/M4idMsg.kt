package dev.doctorm4id.m4id.message

import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.bridge.M4idBridge
import kotlinx.serialization.Serializable
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerPlayer

/**
 * This represents a serializable message that can be sent to a client.
 */
@Serializable
abstract class M4idMsg : CustomPacketPayload {

    data class MsgContext(
        val player: ServerPlayer
    )

    open fun onClientReceived() {
        // Executes on client thread
    }

    open fun onServerReceived(ctx: MsgContext) {
        // Executes on server thread
    }

    fun sendToClient(player: ServerPlayer) {
        M4idBridge.sendMsgToClient(this, player)
    }

    fun sendToClients(players: Collection<ServerPlayer>) {
        for (player in players) {
            M4idBridge.sendMsgToClient(this, player)
        }
    }

    fun sendToServer() {
        M4idBridge.sendMsgToServer(this)
    }

    override fun type(): CustomPacketPayload.Type<out M4idMsg> {
        return M4id.Message.payloadMap[this::class] as CustomPacketPayload.Type<out M4idMsg>
    }
}