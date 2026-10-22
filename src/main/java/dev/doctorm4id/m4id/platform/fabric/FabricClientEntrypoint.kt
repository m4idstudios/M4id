package dev.doctorm4id.m4id.platform.fabric

//? fabric {

import dev.doctorm4id.m4id.M4id
import dev.doctorm4id.m4id.data.M4idPersistence
import dev.doctorm4id.m4id.message.M4idMsg
import dev.doctorm4id.m4id.platform.fabric.bridge.M4idSharedApiFabric
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents

class FabricClientEntrypoint : ClientModInitializer {

	override fun onInitializeClient() {
        M4id.onInitializeClient()

		M4idSharedApiFabric.clientMessageRegistrar = { id ->
			ClientPlayNetworking.registerGlobalReceiver(id) { payload, context ->
				(payload as M4idMsg).onClientReceived()
			}
		}
		M4idSharedApiFabric.clientMessageSender = { msg ->
			ClientPlayNetworking.send(msg)
		}

		// Client data lifecycle management

		WorldRenderEvents.LAST.register(WorldRenderEvents.Last {
			M4id.Input.updateRealBinds()
		})

		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
			M4id.Input.updateNormBinds()
		})

		ClientLifecycleEvents.CLIENT_STOPPING.register {
			M4idPersistence.saveAllConfigResults()
		}
	}
}

//? }
