package dev.doctorm4id.m4id.platform.fabric.datagen

//? fabric {

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator

class FabricDataGeneratorEntrypoint : DataGeneratorEntrypoint {

	override fun onInitializeDataGenerator(generator: FabricDataGenerator) {

		val pack = generator.createPack()

		pack.addProvider(::FabricModelProvider)
	}
}

//?}
