package dev.doctorm4id.m4id.util

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

@Deprecated(message = "To be removed.", level = DeprecationLevel.WARNING)
object M4idCommonUtil {

	/**
	 * Copies a block properties from the wanted block.
	 * This is mainly makes it easier for across versions.
	 * @param block Block to copy properties from.
	 * @return The block behavior properties.
	 */
	@Deprecated(
		message = "See ExtBlock.kt instead.",
		replaceWith = ReplaceWith("Block.copyBlockProperties()")
	)
	fun copyBlockProperties(block: Block): BlockBehaviour.Properties {
		//? if >=1.20.5 {
		return BlockBehaviour.Properties.ofFullCopy(block);
		//?} else {
		/*return BlockBehaviour.Properties.copy(block);
		*///?}
	}
}
