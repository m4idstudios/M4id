package dev.doctorm4id.m4id.logging

import dev.doctorm4id.m4id.M4id
import net.minecraft.resources.ResourceLocation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.apache.logging.log4j.Marker
import org.apache.logging.log4j.MarkerManager
import org.apache.logging.log4j.core.LoggerContext

class M4idLoggingApi internal constructor() {

    init {
        val ctx = LogManager.getContext(false) as LoggerContext
        M4id.Logger.debug("M4id Logging API Initialized.")
    }

    fun createLogger(modid: String): Logger {
        return LogManager.getLogger(modid)
    }

    fun createMarker(id: ResourceLocation): Marker {
        return MarkerManager.getMarker(id.toString())
    }

}