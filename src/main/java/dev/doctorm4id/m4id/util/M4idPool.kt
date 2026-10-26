@file:Suppress("unused")

package dev.doctorm4id.m4id.util

import kotlin.random.Random

class M4idPool<T> {

	private val normalEntries = mutableListOf<M4idPoolEntry<T>>()
	private val experimentalEntries = mutableListOf<M4idPoolEntry<T>>()

	private var normalTotalWeight = 0
	private var experimentalTotalWeight = 0

	fun addEntry(value: T, weightIn: Int) = add(value, weightIn, experimental = false)

	fun addExperimentalEntry(value: T, weightIn: Int) = add(value, weightIn, experimental = true)

	/**
	 * Returns a random entry.
	 *
	 * @param random The random source to use.
	 * @param includeExperimental Whether experimental entries can be selected.
	 * @return A random value, or null if the pool is empty.
	 */
	@JvmOverloads
	fun getRandomEntry(random: Random = Random.Default, includeExperimental: Boolean = false): T? {
		val totalWeight = normalTotalWeight + if (includeExperimental) experimentalTotalWeight else 0

		if (totalWeight <= 0) {
			return null
		}

		val target = random.nextInt(totalWeight)

		if (target < normalTotalWeight) {
			return pick(normalEntries, target)
		}

		if (includeExperimental) {
			return pick(experimentalEntries, target - normalTotalWeight)
		}

		return null
	}

	private fun add(value: T, weightIn: Int, experimental: Boolean) {
		require(weightIn > 0) { "Weight must be greater than 0" }

		val entry = M4idPoolEntry(value, weightIn)

		if (experimental) {
			experimentalEntries.add(entry)
			experimentalTotalWeight += weightIn
		} else {
			normalEntries.add(entry)
			normalTotalWeight += weightIn
		}
	}

	private fun pick(entries: List<M4idPoolEntry<T>>, target: Int): T? {
		var cumulative = 0

		for ((value, weight) in entries) {
			cumulative += weight

			if (target < cumulative) {
				return value
			}
		}

		return null
	}
}

data class M4idPoolEntry<T>(val value: T, val weight: Int, ) : Comparable<M4idPoolEntry<T>> {
	override fun compareTo(other: M4idPoolEntry<T>): Int = weight.compareTo(other.weight)
}