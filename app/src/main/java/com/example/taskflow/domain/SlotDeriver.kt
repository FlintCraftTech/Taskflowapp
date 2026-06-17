package com.example.taskflow.domain

import com.example.taskflow.data.model.ScheduleSlot
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Pure date→slot placement logic for the Schedule view.
 *
 * A task's stored [date] is epoch milliseconds (see Task.date). Taskflow does dates, not
 * times-of-day (UX principle 7), so a date is treated as a logical *day*. The logical day a
 * given instant belongs to is governed by the "day begins at" boundary (SPEC §Settings → Day
 * begins at), which defaults to 4:00 AM until batch 0012 wires the user setting — so this object
 * takes [dayStartHour] as a parameter defaulting to [DEFAULT_DAY_START_HOUR] rather than reading
 * a setting that does not exist yet.
 *
 * Placement (SPEC §Data model): today → Today, tomorrow → Tomorrow, 2–7 days out → Soon,
 * 8+ days out → Later, before today → still Today (the no-shame zone, UX principle 4). Because
 * placement is derived from the date, the Tomorrow → Today rollover happens automatically: a task
 * dated for the next logical day shows on Tomorrow until the day-begins-at boundary passes, then
 * falls into Today on its own. No stored-state mutation is needed for that visual move; the
 * append-to-bottom reorder *mutation* on rollover is owned by batch 0012.
 */
object SlotDeriver {

    /** SPEC default day boundary (4:00 AM). Replaced by the user setting in batch 0012. */
    const val DEFAULT_DAY_START_HOUR: Int = 4

    /** The logical day [epochMillis] belongs to, shifting the boundary by [dayStartHour]. */
    fun logicalDate(
        epochMillis: Long,
        zone: ZoneId = ZoneId.systemDefault(),
        dayStartHour: Int = DEFAULT_DAY_START_HOUR,
    ): LocalDate =
        Instant.ofEpochMilli(epochMillis)
            .atZone(zone)
            .minusHours(dayStartHour.toLong())
            .toLocalDate()

    /**
     * Which slot a dated task falls into, relative to [nowMillis]. Past dates collapse to TODAY.
     */
    fun slotForDate(
        taskDateMillis: Long,
        nowMillis: Long,
        zone: ZoneId = ZoneId.systemDefault(),
        dayStartHour: Int = DEFAULT_DAY_START_HOUR,
    ): ScheduleSlot {
        val taskDay = logicalDate(taskDateMillis, zone, dayStartHour)
        val today = logicalDate(nowMillis, zone, dayStartHour)
        val diff = ChronoUnit.DAYS.between(today, taskDay)
        return when {
            diff <= 0L -> ScheduleSlot.TODAY
            diff == 1L -> ScheduleSlot.TOMORROW
            diff in 2L..7L -> ScheduleSlot.SOON
            else -> ScheduleSlot.LATER
        }
    }

    /** True when [taskDateMillis] falls before today's logical day — used to decide whether a
     *  Today row shows its date (Today shows a date only when it has slipped into the past). */
    fun isBeforeToday(
        taskDateMillis: Long,
        nowMillis: Long,
        zone: ZoneId = ZoneId.systemDefault(),
        dayStartHour: Int = DEFAULT_DAY_START_HOUR,
    ): Boolean =
        logicalDate(taskDateMillis, zone, dayStartHour)
            .isBefore(logicalDate(nowMillis, zone, dayStartHour))
}
