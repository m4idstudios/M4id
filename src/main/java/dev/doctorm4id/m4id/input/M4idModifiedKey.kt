package dev.doctorm4id.m4id.input

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.client.Minecraft
import org.lwjgl.glfw.GLFW

sealed class M4idModifiedBind(val keyMod: M4idKeyModifier = M4idKeyModifier.EMPTY) {

    abstract fun getIsPressed(): Boolean

    class Key(val keyCode: InputConstants.Key, mod: M4idKeyModifier = M4idKeyModifier.EMPTY) : M4idModifiedBind(mod) {
        override fun getIsPressed(): Boolean {
            return InputConstants.isKeyDown(Minecraft.getInstance().window.window, keyCode.value) && keyMod.getIsPressed()
        }
    }

    class Mouse(val key: Int, mod: M4idKeyModifier = M4idKeyModifier.EMPTY) : M4idModifiedBind(mod) {
        override fun getIsPressed(): Boolean {
            return GLFW.glfwGetMouseButton(
                Minecraft.getInstance().window.window,
                key
            ) == 1 && keyMod.getIsPressed()
        }
    }
}