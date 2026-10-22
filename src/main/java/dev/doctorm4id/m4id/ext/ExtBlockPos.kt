package dev.doctorm4id.m4id.ext

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.sqrt

/**
 * Checks if the block position is indestructible.
 */
fun BlockPos.isIndestructible(level: Level): Boolean =
    level.getBlockState(this).getDestroySpeed(level, this) < 0

/**
 * Gets all positions in a 3x3x3 cube centered on this position.
 *
 * @param includePos Whether to include this position.
 */
fun BlockPos.neighborsCube(includePos: Boolean = false): List<BlockPos> =
    buildList( if (includePos) 27 else 26) {
        for (x in -1..1) {
            for (y in -1..1) {
                for (z in -1..1) {
                    if (!includePos && x == 0 && y == 0 && z == 0) continue
                    add(offset(x, y, z))
                }
            }
        }
    }

/**
 * Gets the six positions sharing a face with this position.
 */
fun BlockPos.adjacentNeighbors(): List<BlockPos> =
    Direction.entries.map(::relative)

/**
 * Gets the eight surrounding positions on the horizontal XZ plane.
 *
 * @param includePos Whether to include this position.
 */
fun BlockPos.neighborsXZPlane(includePos: Boolean = false): List<BlockPos> =
    buildList(if (includePos) 9 else 8) {
        for (x in -1..1) {
            for (z in -1..1) {
                if (x == 0 && z == 0) {
                    if (includePos) add(this@neighborsXZPlane)
                    continue
                }
                add(offset(x, 0, z))
            }
        }
    }

/**
 * Checks whether any of the 26 surrounding positions are non-solid.
 */
fun BlockPos.isExposedToAir(level: Level): Boolean =
    neighborsCube().any { neighbor ->
        level.getBlockState(neighbor).isNotSolid(level, neighbor)
    }

/**
 * Checks whether the block at this position is solid.
 */
fun BlockPos.isSolid(level: Level): Boolean {
    val state = level.getBlockState(this)
    return state.isSolid(level, this)
}

/**
 * Checks whether the block at this position is non-solid.
 */
fun BlockPos.isNotSolid(level: Level): Boolean {
    val state = level.getBlockState(this)
    return state.isNotSolid(level, this)
}

/**
 * Gets the 3D distance between this position and another block position.
 */
fun BlockPos.distanceToBlock(other: BlockPos): Float =
    sqrt(distanceToBlockSquared(other).toDouble()).toFloat()

/**
 * Gets the squared 3D distance between this position and another position.
 * Prefer this when only comparing distances.
 */
fun BlockPos.distanceToBlockSquared(other: BlockPos): Int {
    val dx = other.x - x
    val dy = other.y - y
    val dz = other.z - z
    return dx * dx + dy * dy + dz * dz
}