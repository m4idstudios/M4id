package dev.doctorm4id.m4id.data

import dev.doctorm4id.m4id.M4id
import java.util.concurrent.CopyOnWriteArrayList

object M4idPersistence {
    val serverDataFiles: MutableList<ServerDataFile> = CopyOnWriteArrayList()
    val configDataFiles: MutableList<ConfigDataFile> = CopyOnWriteArrayList()

    fun saveAllConfigResults() {
        M4id.Logger.debug("Saving config results")
        configDataFiles.forEach { dataFile -> trySave(dataFile) }
    }

    fun saveAllServerResults() {
        M4id.Logger.debug("Saving server results")
        serverDataFiles.forEach { dataFile -> trySave(dataFile) }
    }

    fun loadServerResults() {
        serverDataFiles.forEach { dataFile ->
            dataFile.load()
        }
    }

    // One file failing to save shouldn't stop the rest from being written
    private fun trySave(dataFile: DataFile) {
        try {
            dataFile.save()
        } catch (e: Exception) {
            M4id.Logger.error("Unable to save ${dataFile.src}", e)
        }
    }

}