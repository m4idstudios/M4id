@file:Suppress("unused")

package dev.doctorm4id.m4id.tick

import net.minecraft.world.level.Level
import kotlin.math.abs

class M4idTickApi {

	companion object {
		const val TPS: Int = 20

		const val TICKS_PER_MINUTE: Long = TPS * 60L
		const val TICKS_PER_HOUR: Long = TICKS_PER_MINUTE * 60L
		const val TICKS_PER_DAY: Long = TICKS_PER_HOUR * 24L
		const val TICKS_PER_WEEK: Long = TICKS_PER_DAY * 7L
		const val TICKS_PER_MONTH: Long = TICKS_PER_DAY * 30L
		const val TICKS_PER_YEAR: Long = TICKS_PER_DAY * 365L
	}

	// Seconds

	fun convertSecondsToTicks(seconds: Int): Int =
		Math.multiplyExact(seconds, TPS)

	fun convertSecondsToTicks(seconds: Long): Long =
		Math.multiplyExact(seconds, TPS.toLong())

	fun convertTicksToSeconds(ticks: Long): Long =
		ticks / TPS

	// Minutes

	fun convertMinutesToTicks(minutes: Int): Int =
		Math.multiplyExact(minutes, TICKS_PER_MINUTE.toInt())

	fun convertMinutesToTicks(minutes: Long): Long =
		Math.multiplyExact(minutes, TICKS_PER_MINUTE)

	fun convertTicksToMinutes(ticks: Long): Long =
		ticks / TICKS_PER_MINUTE

	// Hours

	fun convertHoursToTicks(hours: Int): Int =
		Math.multiplyExact(hours, TICKS_PER_HOUR.toInt())

	fun convertHoursToTicks(hours: Long): Long =
		Math.multiplyExact(hours, TICKS_PER_HOUR)

	fun convertTicksToHours(ticks: Long): Long =
		ticks / TICKS_PER_HOUR

	// Days

	fun convertDaysToTicks(days: Int): Int =
		Math.multiplyExact(days, TICKS_PER_DAY.toInt())

	fun convertDaysToTicks(days: Long): Long =
		Math.multiplyExact(days, TICKS_PER_DAY)

	fun convertTicksToDays(ticks: Long): Long =
		ticks / TICKS_PER_DAY

	// Weeks

	fun convertWeeksToTicks(weeks: Int): Int =
		Math.multiplyExact(weeks, TICKS_PER_WEEK.toInt())

	fun convertWeeksToTicks(weeks: Long): Long =
		Math.multiplyExact(weeks, TICKS_PER_WEEK)

	fun convertTicksToWeeks(ticks: Long): Long =
		ticks / TICKS_PER_WEEK

	// Months / Years

	fun convertMonthsToTicks(months: Long): Long =
		Math.multiplyExact(months, TICKS_PER_MONTH)

	fun convertYearsToTicks(years: Long): Long =
		Math.multiplyExact(years, TICKS_PER_YEAR)

	// Tick Checking

	/**
	 * Returns true if [ticks] have elapsed since [lastTickTime],
	 * or if the event has never occurred.
	 *
	 * Uses [abs] so that world restarts / dimension changes
	 * (where [currentTick] may be lower than [lastTickTime])
	 * are treated as "cooldown passed."
	 */
	fun hasTicksPassed(lastTickTime: Long, currentTick: Long, ticks: Long): Boolean {
		if (lastTickTime == -1L) return true
		return abs(currentTick - lastTickTime) >= ticks
	}

	/**
	 * Convenience overload using the level's current game time.
	 */
	fun hasTicksPassed(lastTickTime: Long, level: Level, ticks: Long): Boolean =
		hasTicksPassed(lastTickTime, level.gameTime, ticks)
}
