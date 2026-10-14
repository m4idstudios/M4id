package dev.doctorm4id.m4id.text

import net.minecraft.ChatFormatting
import net.minecraft.server.MinecraftServer
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.player.Player

fun CommandSourceStack.sendFailure(literal: String, text: M4idTextBuilder<MutableComponent>.() -> Unit = {}) {
    sendFailure(textLiteral(literal, text))
}

fun CommandSourceStack.sendSuccess(literal: String, broadcastToOps: Boolean = false, dsl: M4idTextBuilder<MutableComponent>.() -> Unit = {}) {
    sendSuccess({ textLiteral(literal, dsl) }, broadcastToOps)
}

fun MinecraftServer.broadcastSystemMessage(literal: String = "", overlay: Boolean = false, text: M4idTextBuilder<MutableComponent>.() -> Unit) {
    playerList.broadcastSystemMessage(textLiteral(literal, text), overlay)
}

fun Player.sendMessage(literal: String = "", vararg formats: ChatFormatting, actionBar: Boolean = false, text: M4idTextBuilder<MutableComponent>.() -> Unit = {}) {
    sendSystemMessage(
        textLiteral(literal) {
            format(*formats)
            apply(text)
        }
    )
}