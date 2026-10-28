package dev.doctorm4id.m4id.platform.fabric.ext.client

//? fabric {

import com.mojang.blaze3d.platform.InputConstants
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping

fun KeyMapping.getBoundKey(): InputConstants.Key {
    return KeyBindingHelper.getBoundKeyOf(this)
}

//? }