package com.example.taskflow.domain

import com.example.taskflow.data.model.ScheduleSlot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

/**
 * Unit tests for the pure date→slot placement logic. Fixed UTC zone and a fixed reference "now"
 * keep results deterministic regardless of the machine's time zone. dayStartHour is pinned to the
 * 4 AM default the production code uses.
 */
class SlotDeriverTest {

    private val zone: ZoneId = ZoneOffset.UTC
    private val dayStart = SlotDeriver.DEFAULT_DAY_START_HOUR // 4

    /** Epoch millis for [date] at [hour]:00 UTC. */
    private fun millis(date: LocalDate, hour: Int = 12): Long =
        date.atTime(hour, 0).atZone(ZoneOffset.UTC).toInstant().toEpochMilli()

    private fun slot(taskDate: LocalDate, now: Long, hour: Int = 12): ScheduleSlot =
        SlotDeriver.slotForDate(millis(taskDate, hour), now, zone, dayStart)

    // --- slotForDate: placement by day distance (mid-day "now", well clear of the 4 AM edge) ---

    @Test
    fun todayDate_isToday() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertEquals(ScheduleSlot.TODAY, slot(today, now))
    }

    @Test
    fun pastDates_collapseToToday() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertEquals(ScheduleSlot.TODAY, slot(today.minusDays(1), now))
        assertEquals(ScheduleSlot.TODAY, slot(today.minusDays(10), now))
    }

    @Test
    fun tomorrowDate_isTomorrow() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertEquals(ScheduleSlot.TOMORROW, slot(today.plusDays(1), now))
    }

    @Test
    fun twoToSevenDaysOut_isSoon() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertEquals(ScheduleSlot.SOON, slot(today.plusDays(2), now))
        assertEquals(ScheduleSlot.SOON, slot(today.plusDays(7), now))
    }

    @Test
    fun eightOrMoreDaysOut_isLater() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertEquals(ScheduleSlot.LATER, slot(today.plusDays(8), now))
        assertEquals(ScheduleSlot.LATER, slot(today.plusDays(30), now))
    }

    // --- day-begins-at boundary: before 4 AM, "today" is still the previous calendar day ---

    @Test
    fun before4am_todayIsPreviousCalendarDay() {
        // now = 2026-06-17 02:00 UTC → logical today = 2026-06-16
        val now = millis(LocalDate.of(2026, 6, 17), hour = 2)
        // A task on 2026-06-16 is the logical "today".
        assertEquals(ScheduleSlot.TODAY, slot(LocalDate.of(2026, 6, 16), now))
        // The calendar day that just started (2026-06-17) is still "tomorrow" until 4 AM.
        assertEquals(ScheduleSlot.TOMORROW, slot(LocalDate.of(2026, 6, 17), now))
    }

    @Test
    fun at4am_dayHasRolledOver() {
        // now = 2026-06-17 04:00 UTC → logical today = 2026-06-17
        val now = millis(LocalDate.of(2026, 6, 17), hour = 4)
        assertEquals(ScheduleSlot.TODAY, slot(LocalDate.of(2026, 6, 17), now))
        assertEquals(ScheduleSlot.TOMORROW, slot(LocalDate.of(2026, 6, 18), now))
    }

    // --- logicalDate: the 4 AM cutoff itself ---

    @Test
    fun logicalDate_just_before_4am_is_previous_day() {
        val instant = millis(LocalDate.of(2026, 6, 17), hour = 3)
        assertEquals(LocalDate.of(2026, 6, 16), SlotDeriver.logicalDate(instant, zone, dayStart))
    }

    @Test
    fun logicalDate_at_4am_is_same_day() {
        val instant = millis(LocalDate.of(2026, 6, 17), hour = 4)
        assertEquals(LocalDate.of(2026, 6, 17), SlotDeriver.logicalDate(instant, zone, dayStart))
    }

    // --- isBeforeToday: drives whether a Today row shows its date ---

    @Test
    fun isBeforeToday_trueOnlyForPast() {
        val today = LocalDate.of(2026, 6, 17)
        val now = millis(today)
        assertTrue(SlotDeriver.isBeforeToday(millis(today.minusDays(1)), now, zone, dayStart))
        assertFalse(SlotDeriver.isBeforeToday(millis(today), now, zone, dayStart))
        assertFalse(SlotDeriver.isBeforeToday(millis(today.plusDays(1)), now, zone, dayStart))
    }
}
