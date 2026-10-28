package dev.doctorm4id.m4id.platform.fabric.datagen

//? fabric {

import dev.doctorm4id.m4id.M4id
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.models.BlockModelGenerators
import net.minecraft.data.models.ItemModelGenerators

class FabricModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {

	override fun generateBlockStateModels(blockStateModelGenerator: BlockModelGenerators?) {

	}

	override fun generateItemModels(itemModelGenerator: ItemModelGenerators?) {

	}

	override fun getName(): String {
		return M4id.MOD_FRIENDLY_NAME+" Model Provider"
	}
}

//? }
