package dev.doctorm4id.m4id.platform.neoforge

object NeoforgeEventSubscriber {

/*	@JvmStatic
	@SubscribeEvent
	fun onPlayerDamage(event: LivingDamageEvent.Post) {
		if (event.entity is ServerPlayer && event.newDamage > 0) {

			val player = event.entity as ServerPlayer
			ExampleEventHandler().onPlayerHurt(player)
		}
	}*/

/*	@JvmStatic
	@SubscribeEvent
	fun onRegister(event: RegisterEvent) {
		ModRegistry.registerAll { id, supplier ->
			when (event.registryKey) {
				BuiltInRegistries.BLOCK.key() -> {
					val obj = supplier()
					if (obj is Block) {
						event.register(BuiltInRegistries.BLOCK.key(), id) { obj }
					}
				}
				BuiltInRegistries.ITEM.key() -> {
					val obj = supplier()
					if (obj is Item) {
						event.register(BuiltInRegistries.ITEM.key(), id) { obj }
					}
				}
			}
		}
	}*/
}
