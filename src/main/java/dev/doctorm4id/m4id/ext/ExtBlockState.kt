package dev.doctorm4id.m4id.ext

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids

/**
 * Checks if the block state is generally replaceable
 */
fun BlockState.isReplaceable(): Boolean =
    this.isReplaceableByWater() || this.canBeReplaced()

/**
 * Checks if the block state is replaceable by water.
 */
fun BlockState.isReplaceableByWater(): Boolean =
    this.canBeReplaced(Fluids.WATER)

/**
 * Checks whether this block state is weak (replaceable or air).
 */
fun BlockState.isWeakBlock(): Boolean =
    isReplaceable() || isAir

/**
 * Checks whether this block state is air.
 *
 * Includes regular air, cave air, and other air block states.
 */
fun BlockState?.isAir(): Boolean =
    this?.isAir == true

/**
 * Checks whether this block state is non-solid.
 */
fun BlockState.isNotSolid(level: Level, pos: BlockPos): Boolean =
    !canOcclude() || !isSolid || isAir || !isSolidRender(level, pos)

/**
 * Checks whether this block state is solid.
 */
fun BlockState.isSolid(level: Level, pos: BlockPos): Boolean =
    !isNotSolid(level, pos)