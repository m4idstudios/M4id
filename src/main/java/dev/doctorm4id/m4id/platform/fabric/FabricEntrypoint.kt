package dev.doctorm4id.m4id.platform.fabric

//? fabric {

import dev.doctorm4id.m4id.M4id
import net.fabricmc.api.ModInitializer

class FabricEntrypoint : ModInitializer {

	override fun onInitialize() {
		M4id.onInitialize()
		FabricEventSubscriber().registerEvents()

/*		Kambrik.Message.registerClientMessage(
			TestMsg.serializer(),
			TestMsg::class,
			Kambrik.idOf("test_msg")
		)*/
	}
}

//? }
