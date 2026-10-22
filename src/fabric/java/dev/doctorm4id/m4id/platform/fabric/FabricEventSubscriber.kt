package dev.doctorm4id.m4id.platform.fabric

//? fabric {

import dev.doctorm4id.m4id.data.M4idPersistence
import dev.doctorm4id.m4id.internal.M4idCommands
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader

class FabricEventSubscriber {

	fun registerEvents() {
		CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback(M4idCommands::register))

		ServerLifecycleEvents.SERVER_STARTED.register {
			M4idPersistence.loadServerResults()
		}

		ServerLifecycleEvents.SERVER_STOPPING.register {
			M4idPersistence.saveAllServerResults()
			if (FabricLoader.getInstance().environmentType != EnvType.CLIENT) {
				M4idPersistence.saveAllConfigResults()
			}
		}
	}
}

//? }
