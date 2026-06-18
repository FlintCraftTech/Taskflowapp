package com.example.taskflow.ui.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskflow.data.model.ScheduleSlot
import com.example.taskflow.data.model.Task
import com.example.taskflow.data.repository.TaskRepository
import com.example.taskflow.domain.SlotDeriver
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** One task as the Schedule view renders it: title plus an optional date label (null = no label). */
data class ScheduleTaskUi(
    val id: Long,
    val title: String,
    val dateLabel: String?,
)

/** Per-slot lists for the four Schedule pages, plus the Today completed tray. */
data class ScheduleUiState(
    val today: List<ScheduleTaskUi> = emptyList(),
    val tomorrow: List<ScheduleTaskUi> = emptyList(),
    val soon: List<ScheduleTaskUi> = emptyList(),
    val later: List<ScheduleTaskUi> = emptyList(),
    // Completed top-level tasks, newest-completed-first is owned by 0012; for now query order.
    // Rendered only at the bottom of Today (SPEC §Completed task tray on Today).
    val completed: List<ScheduleTaskUi> = emptyList(),
) {
    fun forSlot(slot: ScheduleSlot): List<ScheduleTaskUi> = when (slot) {
        ScheduleSlot.TODAY -> today
        ScheduleSlot.TOMORROW -> tomorrow
        ScheduleSlot.SOON -> soon
        ScheduleSlot.LATER -> later
    }
}

/**
 * Exposes the four Schedule slots as derived lists plus the Today completed tray. Bucketing stays
 * read-only/derived (batch 0002); batch 0005 adds completion — [setCompleted] flips a task's state,
 * which the active/completed queries pick up so it leaves its slot and joins the tray (and back).
 *
 * [clock] and [zone] are injectable so the bucketing logic can be unit-tested without a device.
 * The date label uses DD/MM (SPEC default); batch 0013 adds the MM/DD setting.
 */
class ScheduleViewModel(
    private val repository: TaskRepository,
    private val clock: () -> Long = System::currentTimeMillis,
    private val zone: ZoneId = ZoneId.systemDefault(),
) : ViewModel() {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    val uiState: StateFlow<ScheduleUiState> =
        combine(
            repository.observeActiveTopLevel(),
            repository.getCompletedTasks(),
        ) { active, completed ->
            bucket(active, clock()).copy(
                completed = completed.map { ScheduleTaskUi(id = it.id, title = it.title, dateLabel = null) },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScheduleUiState())

    /** Complete or un-complete a task — the source of both leaving a slot and joining the tray. */
    fun setCompleted(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch { repository.updateCompletion(taskId, isCompleted) }
    }

    private fun bucket(tasks: List<Task>, now: Long): ScheduleUiState {
        val grouped = mutableMapOf<ScheduleSlot, MutableList<Task>>()
        for (task in tasks) {
            val slot = when {
                task.date != null -> SlotDeriver.slotForDate(task.date, now, zone)
                // Undated task parked on a slot (Soon/Later only, per SPEC). Undated with no slot
                // lives only in its Project view, so it is not shown on any Schedule page.
                task.slot != null -> task.slot
                else -> null
            } ?: continue
            grouped.getOrPut(slot) { mutableListOf() }.add(task)
        }
        return ScheduleUiState(
            today = grouped[ScheduleSlot.TODAY].orderedForSlot(ScheduleSlot.TODAY, now),
            tomorrow = grouped[ScheduleSlot.TOMORROW].orderedForSlot(ScheduleSlot.TOMORROW, now),
            soon = grouped[ScheduleSlot.SOON].orderedForSlot(ScheduleSlot.SOON, now),
            later = grouped[ScheduleSlot.LATER].orderedForSlot(ScheduleSlot.LATER, now),
        )
    }

    private fun List<Task>?.orderedForSlot(slot: ScheduleSlot, now: Long): List<ScheduleTaskUi> {
        if (this == null) return emptyList()
        // Stable order: stored slot_sort_order first (the query already sorts by it), with date
        // then id as tiebreakers so tasks pulled into Today from different origin slots have a
        // deterministic order. The precise append-to-bottom-on-rollover order is owned by 0012.
        return this
            .sortedWith(compareBy({ it.slotSortOrder }, { it.date ?: Long.MIN_VALUE }, { it.id }))
            .map { task -> task.toUi(slot, now) }
    }

    private fun Task.toUi(slot: ScheduleSlot, now: Long): ScheduleTaskUi =
        ScheduleTaskUi(
            id = id,
            title = title,
            dateLabel = dateLabelFor(this, slot, now),
        )

    /**
     * Tomorrow/Soon/Later rows show their date when they have one. Today rows show a date only
     * when it has slipped into the past — the only signal that a task is "still there" from a past
     * day, with no overdue label or colour (UX principle 4).
     */
    private fun dateLabelFor(task: Task, slot: ScheduleSlot, now: Long): String? {
        val date = task.date ?: return null
        val show = when (slot) {
            ScheduleSlot.TODAY -> SlotDeriver.isBeforeToday(date, now, zone)
            else -> true
        }
        if (!show) return null
        return Instant.ofEpochMilli(date).atZone(zone).toLocalDate().format(dateFormatter)
    }

    companion object {
        fun factory(repository: TaskRepository) = viewModelFactory {
            initializer { ScheduleViewModel(repository) }
        }
    }
}
