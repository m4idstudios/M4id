package dev.doctorm4id.m4id.internal

import dev.doctorm4id.m4id.message.M4idMsg
import kotlinx.serialization.Serializable
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

@Serializable
class TestMsg(val msg: String) : M4idMsg() {
    override fun onClientReceived() {
        Minecraft.getInstance().player?.sendSystemMessage(Component.literal("Got Test Msg! It says: $msg"))
        println("Got Test Msg! It says: $msg")
    }
}