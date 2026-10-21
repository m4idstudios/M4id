package dev.doctorm4id.m4id.ext.math

import dev.doctorm4id.m4id.ext.toVec3
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import kotlin.enums.enumEntries
import kotlin.math.*

// TODO: fix conflicting overloads.
/*fun Vec3.axialMask(dir: Direction): Vec3 {
    return multiply(dir.toVec3().abs())
}*/

private fun intSwitch(i: Int): Int {
    return (abs(i) - 1) * -1
}

// TODO: fix conflicting overloads.
/*fun Vec3.flipMask(dir: Direction): Vec3 {
    val unit = dir.normal
    val mask = Vec3i(intSwitch(unit.x), intSwitch(unit.y), intSwitch(unit.z))
    return Vec3(x * mask.x, y * mask.y, z * mask.z)
}*/

// TODO: fix conflicting overloads.
/*fun Vec3.hasZeroAxis(): Boolean {
    return enumEntries<Direction.Axis>().any {
        axisValue(it) == 0.0
    }
}*/

// Conversion Functions

// TODO: fix conflicting overloads.
/*fun Vec3.toArray(): DoubleArray {
    return doubleArrayOf(x, y, z)
}*/

// Math Functions

// TODO: fix conflicting overloads.
/*fun List<Vec3>.average(): Vec3 {
    return if (isEmpty()) {
        throw Exception("Cannot average an empty list of Vec3d! Must contain at least one element!")
    } else {
        val summed = this.reduce { a, b -> a.add(b) }
        summed * (1.0 / size)
    }
}*/

// TODO: fix conflicting overloads.
/*fun max(vecA: Vec3, vecB: Vec3): Vec3 {
    return Vec3(
        max(vecA.x, vecB.x),
        max(vecA.y, vecB.y),
        max(vecA.z, vecB.z)
    )
}*/

// TODO: fix conflicting overloads.
/*
fun min(vecA: Vec3, vecB: Vec3): Vec3 {
    return Vec3(
        min(vecA.x, vecB.x),
        min(vecA.y, vecB.y),
        min(vecA.z, vecB.z)
    )
}
*/


// Other Functions

// TODO: fix conflicting overloads.
/*fun Vec3.abs(): Vec3 {
    return Vec3(abs(x), abs(y), abs(z))
}*/

// TODO: fix conflicting overloads.
/*fun Vec3.ceil(): Vec3i {
    return BlockPos(ceil(x).toInt(), ceil(y).toInt(), ceil(z).toInt())
}*/

// TODO: fix conflicting overloads.
/*fun Vec3.floor(): Vec3i {
    return BlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())
}*/

// TODO: fix conflicting overloads.
/*fun Vec3.map(func: (it: Double) -> Double): Vec3 {
    return Vec3(func(x), func(y), func(z))
}*/

// TODO: fix conflicting overloads.
/*fun Vec3.rounded(): Vec3 {
    return Vec3(round(x), round(y), round(z))
}*/

// Mayb add these some day.
// rotateX
// rotateY
// normalize
// lerp