package dev.doctorm4id.m4id.ext

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

/**
 * Copies a block properties from the wanted block.
 */
fun Block.copyBlockProperties(): BlockBehaviour.Properties =
    BlockBehaviour.Properties.ofFullCopy(this);