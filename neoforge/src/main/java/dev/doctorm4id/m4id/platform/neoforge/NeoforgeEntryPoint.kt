package dev.doctorm4id.m4id.platform.neoforge

import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.bridge.M4idBridge
import dev.doctorm4id.m4id.internal.M4idCommands
import dev.doctorm4id.m4id.platform.neoforge.bridge.M4idSharedApiForge
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.event.RegisterCommandsEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(M4id.MOD_ID)
class NeoforgeEntryPoint {

	init {
		M4id.onInitialize()

		FORGE_BUS.addListener(M4idForgeEvents::registerCommands)

		MOD_CONTEXT.getKEventBus().register(M4idModCommonEvents::class.java)

		runForDist(
			clientTarget = {
				// Register mod event bus
				MOD_CONTEXT.getKEventBus().register(NeoforgeClientEventSubscriber::class.java)
			},
			serverTarget = { }
		)
	}

	object M4idForgeEvents {
		@JvmStatic
		@SubscribeEvent
		fun registerCommands(evt: RegisterCommandsEvent) {
			M4idCommands.register(evt.dispatcher, evt.buildContext, evt.commandSelection)
		}
	}

	object M4idModCommonEvents {
		@JvmStatic
		@SubscribeEvent
		fun registerPayloads(event: RegisterPayloadHandlersEvent) {
			M4id.Logger.info("Registering network payloads..")
			(M4idBridge as M4idSharedApiForge).registerPayloads(event)
		}
	}
}
