package com.example.taskflow.ui.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.taskflow.data.model.Task
import com.example.taskflow.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** One task as the Project view renders it: title plus an optional DD/MM date label. */
data class ProjectTaskUi(
    val id: Long,
    val title: String,
    val dateLabel: String?,
)

/**
 * The two task lists a Project screen shows: [datedTasks] for the top "currently scheduled" card,
 * [undatedTasks] for the below-card list.
 */
data class ProjectUiState(
    val datedTasks: List<ProjectTaskUi> = emptyList(),
    val undatedTasks: List<ProjectTaskUi> = emptyList(),
)

/**
 * Backs one Project screen, scoped to a single [projectId]. Exposes the Project's *dated* tasks
 * (ordered by their Schedule position via the DAO's `slot_sort_order` sort) for the top card, and its
 * *undated* tasks (in `project_sort_order`) for the below-card list — SPEC §Project view.
 *
 * Batch 0005 adds completion: [setCompleted] flips a task's state, and the filter below drops
 * completed tasks from both the card and the below-card list — completing a task in a Project view
 * removes it here and routes it to the Today tray (SPEC §Completed task tray on Today). Below-card
 * drag-reorder is still captured for a later batch.
 *
 * The date label uses DD/MM (SPEC default); batch 0013 adds the MM/DD setting. [zone] is injectable
 * so label formatting can be tested without a device.
 */
class ProjectViewModel(
    private val taskRepository: TaskRepository,
    projectId: Long,
    private val zone: ZoneId = ZoneId.systemDefault(),
) : ViewModel() {

    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    val uiState: StateFlow<ProjectUiState> =
        combine(
            taskRepository.getDatedTasksByProject(projectId),
            taskRepository.getUndatedTasksByProject(projectId),
        ) { dated, undated ->
            ProjectUiState(
                datedTasks = dated.filterNot { it.isCompleted }.map { it.toUi(withDate = true) },
                undatedTasks = undated.filterNot { it.isCompleted }.map { it.toUi(withDate = false) },
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProjectUiState())

    /** Complete or un-complete a task from the card or the below-card list (SPEC §Completed tray). */
    fun setCompleted(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch { taskRepository.updateCompletion(taskId, isCompleted) }
    }

    private fun Task.toUi(withDate: Boolean): ProjectTaskUi =
        ProjectTaskUi(
            id = id,
            title = title,
            dateLabel = if (withDate && date != null) {
                Instant.ofEpochMilli(date).atZone(zone).toLocalDate().format(dateFormatter)
            } else {
                null
            },
        )

    companion object {
        fun factory(taskRepository: TaskRepository, projectId: Long) = viewModelFactory {
            initializer { ProjectViewModel(taskRepository, projectId) }
        }
    }
}
