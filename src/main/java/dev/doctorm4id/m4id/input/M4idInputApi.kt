package dev.doctorm4id.m4id.input

import dev.doctorm4id.m4id.M4id

class M4idInputApi internal constructor() {

    init {
        M4id.Logger.debug("M4id Input API Initialized.")
    }

    private val keyBinds = mutableListOf<M4idKeybind>()

    fun updateNormBinds() {
        for (bind in keyBinds) {
            if (!bind.realTime) {
                bind.update()
            }
        }
    }

    fun updateRealBinds() {
        for (bind in keyBinds) {
            if (bind.realTime) {
                bind.update()
            }
        }
    }

    fun registerBinding(
        key: M4idModifiedBind,
        realTime: Boolean = false,
        bindingDsl: M4idKeybind.() -> Unit
    ): M4idKeybind {
        val m4idKeybind = M4idKeybind(
            key, realTime
        ).apply(bindingDsl)
        keyBinds.add(m4idKeybind)
        return m4idKeybind
    }

}