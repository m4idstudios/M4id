package dev.doctorm4id.m4id.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

/**
 * Scans the world, starting at one BlockPos and moving in one direction, until a condition is met
 * or the maximum distance is reached.
 */
fun Level.scanFor(startPos: BlockPos, direction: Direction = Direction.UP, maxDistance: Int = 64, until: Level.(pos: BlockPos) -> Boolean): BlockPos? {
    var current: BlockPos

    for (i in 0..maxDistance) {
        current = startPos.relative(direction, i)
        if (until(current)) {
            return current
        }
    }

    return null
}

/**
 * Sets the block at this position only if its state differs.
 *
 * @return Whether the block was successfully changed.
 */
fun Level.setBlockIfChanged(pos: BlockPos, newState: BlockState, flags: Int = 2): Boolean {
    if (getBlockState(pos) == newState) return false
    return setBlock(pos, newState, flags)
}