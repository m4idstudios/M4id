package dev.doctorm4id.m4id.platform.fabric.ext.fapi

//? fabric {

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer

fun ServerPlayer.getPacketSender(): PacketSender {
    return ServerPlayNetworking.getSender(this)
}

//? }

