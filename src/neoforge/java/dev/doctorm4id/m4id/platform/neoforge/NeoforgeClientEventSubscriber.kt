package dev.doctorm4id.m4id.platform.neoforge

import dev.doctorm4id.m4id.M4id
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent

object NeoforgeClientEventSubscriber {

	@SubscribeEvent
	@JvmStatic
	fun onClientSetup(event: FMLClientSetupEvent) {
		M4id.onInitializeClient()
	}
}

