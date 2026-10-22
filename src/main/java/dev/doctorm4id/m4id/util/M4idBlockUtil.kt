@file:Suppress("unused")

package dev.doctorm4id.m4id.util

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import kotlin.math.pow
import kotlin.math.sqrt

@Deprecated(message = "Please see ExtBlock.kt instead.", level = DeprecationLevel.WARNING)
object M4idBlockUtil {

	/**
	 * Checks if the block state is generally replaceable
	 * @param blockState The target block state
	 * @return true if replaceable, false otherwise
	 */
	@Deprecated(
		message = "See ExtBlock.kt instead.",
		replaceWith = ReplaceWith("blockState.isReplaceableByWater()")
	)
	fun isReplaceable(blockState: BlockState): Boolean {
		return isReplaceableByWater(blockState) || blockState.canBeReplaced()
	}

	/**
	 * Checks if the block state is replaceable by water.
	 * @param blockState The target block state
	 * @return true if replaceable by water, false otherwise
	 */
	@Deprecated(
		message = "See ExtBlock.kt instead.",
		replaceWith = ReplaceWith("blockState.isReplaceableByWater()")
	)
	fun isReplaceableByWater(blockState: BlockState): Boolean {
		return blockState.canBeReplaced(Fluids.WATER)
	}

	/**
	 * Checks if the block position is indestructible.
	 * @param pos The target block position
	 * @param world Level
	 * @return true if its indestructible, false otherwise
	 */
	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.isIndestructible(level)")
	)
	fun isIndestructible(pos: BlockPos, world: Level): Boolean {
		val blockState: BlockState = world.getBlockState(pos)
		return blockState.getDestroySpeed(world, pos) < 0
	}

	@Deprecated(
		message = "See ExtBlock.kt instead.",
		replaceWith = ReplaceWith("blockState.isWeakBlock()")
	)
	/**
	 * Checks if the block state is weak (replaceable or is air).
	 * @param blockState The target BlockState
	 * @return true if its weak, false otherwise
	 */
	fun isWeakBlock(blockState: BlockState): Boolean {
		return isReplaceable(blockState) || isAir(blockState)
	}

	@Deprecated(
		message = "See ExtBlock.kt instead.",
		replaceWith = ReplaceWith("blockState.isAir()")
	)
	/**
	 * Checks if the block state is air. This includes both regular air and cave air.
	 * @param blockState The target BlockState
	 * @return true if blockState is air, false otherwise
	 */
	fun isAir(blockState: BlockState?): Boolean {
		return blockState?.isAir == true || blockState?.`is`(Blocks.AIR) == true || blockState?.`is`(Blocks.CAVE_AIR) == true
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.getNeighborsCube(includePos)")
	)
	/**
	 * Will return an array list that represents a 3x3x3 cube of all block
	 * positions with the origin being the centroid. Does not include origin
	 * in this list if set.
	 * @param pos The target block position
	 * @param includePos If it should include the target block position
	 * @return A list of Neighbors in a 3x3x3 cube
	 */
	fun getNeighborsCube(pos: BlockPos, includePos: Boolean): ArrayList<BlockPos?> {
		val neighbors = ArrayList<BlockPos?>()
		for (i in -1..1) {
			for (j in -1..1) {
				for (k in -1..1) {
					if (i == 0 && j == 0 && k == 0 && !includePos) {
						continue
					}
					neighbors.add(pos.offset(i, j, k))
				}
			}
		}
		return neighbors
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.getAdjacentNeighbors()")
	)
	/**
	 * Will return an array list that represents the neighbors that are directly touching any face of the block
	 * @param pos The target block position
	 * @return A ArrayList of all adjacent neighbors
	 */
	fun getAdjacentNeighbors(pos: BlockPos): ArrayList<BlockPos> {
		// Only the six positions sharing a face with this one. For the full 3x3x3 cube use getNeighborsCube.
		val list = ArrayList<BlockPos>(6)
		for (direction in Direction.entries) {
			list.add(pos.relative(direction))
		}
		return list
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.getNeighborsXZPlane(includePos)")
	)
	/**
	 * Will return an array list representing a 2D layer
	 * @param pos The target block position / origin
	 * @param includePos If it should include the target block position / origin
	 * @return A ArrayList of block positions
	 */
	fun getNeighborsXZPlane(pos: BlockPos, includePos: Boolean): ArrayList<BlockPos> {
		val list = ArrayList<BlockPos>()
		list.add(pos.north())
		list.add(pos.north().east())
		list.add(pos.north().west())
		list.add(pos.east())
		list.add(pos.south())
		list.add(pos.south().east())
		list.add(pos.south().west())
		list.add(pos.west())
		if (includePos) list.add(pos)

		return list
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.isExposedToAir(level)")
	)
	/**
	 * Checks immediate blocks to see if any of them are air
	 * @param pos The target block position to check
	 * @param serverWorld The server world
	 * @return true if any air found, false otherwise
	 */
	fun isExposedToAir(pos: BlockPos, serverWorld: Level): Boolean {
		for (dx in -1..1) {
			for (dy in -1..1) {
				for (dz in -1..1) {
					if (dx == 0 && dy == 0 && dz == 0) continue
					if (this.isNotSolid(pos.offset(dx, dy, dz), serverWorld)) {
						return true
					}
				}
			}
		}
		return false
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.isSolid(level)")
	)
	/**
	 * Checks if the position is a solid block.
	 * @param pos The target block position to check
	 * @param level The server world
	 * @return true if pos is solid, false otherwise
	 */
	fun isSolid(pos: BlockPos?, level: Level): Boolean {
		return !isNotSolid(pos, level)
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.isNotSolid(level)")
	)
	/**
	 * Checks if the position is a non-solid block.
	 * @param pos The target block position to check
	 * @param level The world
	 * @return true if pos is non-solid, false otherwise
	 */
	fun isNotSolid(pos: BlockPos?, level: Level): Boolean {
		val state = level.getBlockState(pos!!)
		val canNotOcclude = !state.canOcclude()
		val isNotSolid = !state.isSolid
		val isAir = state.isAir
		val isNotSolidRender = !state.isSolidRender(level, pos)
		return canNotOcclude || isNotSolid || isAir || isNotSolidRender
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.getBlockDistance(otherPos)")
	)
	/**
	 * Gets the distance between two block positions as a float.
	 * Uses the standard 3D distance formula.
	 * @param pos1 First position
	 * @param pos2 Second position
	 * @return float
	 */
	fun getBlockDistance(pos1: BlockPos, pos2: BlockPos): Float {
		return sqrt(
			(pos2.x - pos1.x).toDouble().pow(2.0) + (pos2.y - pos1.y).toDouble()
				.pow(2.0) + (pos2.z - pos1.z).toDouble().pow(2.0)
		).toFloat()
	}

	@Deprecated(
		message = "See ExtBlockPos.kt instead.",
		replaceWith = ReplaceWith("blockPos.getBlockDistanceSquared(otherPos)")
	)
	/**
	 * Gets squared distance between two block positions.
	 * Useful for comparisons without sqrt.
	 * @param pos1 First position
	 * @param pos2 Second position
	 * @return int
	 */
	fun getBlockDistanceSquared(pos1: BlockPos, pos2: BlockPos): Int {
		val dx = pos2.x - pos1.x
		val dy = pos2.y - pos1.y
		val dz = pos2.z - pos1.z
		return dx * dx + dy * dy + dz * dz
	}

	@Deprecated(
		message = "See ExtLevel.kt instead.",
		replaceWith = ReplaceWith("level.setBlockIfChanged(pos, newState, flags)")
	)
	/**
	 * Set the block if changed
	 * @param serverLevel The world
	 * @param pos The target block position
	 * @param newState The new BlockState to set
	 * @param flags The flags for setBlock, default is 2 (Block.UPDATE_CLIENTS)
	 * @return true if the block was changed, false if the new state is the same as the old state
	 */
	fun setBlockIfChanged(serverLevel: ServerLevel, pos: BlockPos, newState: BlockState, flags: Int = 2): Boolean {
		val oldState = serverLevel.getBlockState(pos)
		if (oldState == newState) return false
		return serverLevel.setBlock(pos, newState, flags)
	}
}
