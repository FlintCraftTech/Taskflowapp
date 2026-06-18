package com.example.taskflow.ui.project

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.TaskflowApplication

/**
 * A Project's dedicated screen (SPEC §Project view), shown as an overlay over the spine. Two parts:
 * a collapsible top card of this Project's *dated* tasks (the "currently scheduled" surface) over a
 * below-card list of its *undated* tasks (the dreaming surface).
 *
 * Render-only for batch 0004: tasks display but can't yet be completed (0005) or reordered by drag
 * (captured for a later batch). The card is **collapsed by default** — a Project opens on its
 * dreaming surface, not its committed work — and expanding it is an opt-in tap.
 */
@Composable
fun ProjectScreen(
    projectName: String,
    projectId: Long,
    onBack: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskflowApplication
    // Key by projectId so opening a different Project rebuilds the view-model against the new id
    // rather than reusing the previous Project's tasks.
    val viewModel: ProjectViewModel = viewModel(
        key = "project-$projectId",
        factory = ProjectViewModel.factory(app.taskRepository, projectId),
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        ProjectHeader(projectName = projectName, onBack = onBack)
        HorizontalDivider()
        ProjectBody(
            state = uiState,
            onToggleComplete = viewModel::setCompleted,
            onTaskClick = onTaskClick,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ProjectHeader(projectName: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "←",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(text = projectName, style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
private fun ProjectBody(
    state: ProjectUiState,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Empty state #1: a Project with no tasks at all.
    if (state.datedTasks.isEmpty() && state.undatedTasks.isEmpty()) {
        EmptyMessage(
            text = "Nothing in this Project yet.",
            modifier = modifier,
        )
        return
    }
    Column(modifier = modifier.fillMaxSize()) {
        // The card appears only when there are dated tasks; with none, an absent card is the
        // "nothing scheduled" state (empty state #3 — undated tasks but none dated).
        if (state.datedTasks.isNotEmpty()) {
            ScheduledCard(
                tasks = state.datedTasks,
                onToggleComplete = onToggleComplete,
                onTaskClick = onTaskClick,
            )
        }
        if (state.undatedTasks.isNotEmpty()) {
            UndatedList(
                tasks = state.undatedTasks,
                onToggleComplete = onToggleComplete,
                onTaskClick = onTaskClick,
                modifier = Modifier.weight(1f),
            )
        } else {
            // Empty state #2: dated tasks in the card, but nothing unscheduled below it.
            EmptyMessage(
                text = "No unscheduled tasks here yet.",
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/**
 * The "currently scheduled" card: this Project's dated tasks, ordered by their Schedule position.
 * Collapsed by default; expanding is opt-in. When expanded the list is height-bounded and scrolls
 * within the card so it can't crowd out the below-card list (SPEC §Project view).
 */
@Composable
private fun ScheduledCard(
    tasks: List<ProjectTaskUi>,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(tonalElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = "Scheduled · ${tasks.size}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                // Text glyph rather than a Material icon — the icon pack isn't a project dependency
                // (see the spine-header chevron capture). ▾ = expanded, ▸ = collapsed.
                Text(
                    text = if (expanded) "▾" else "▸",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (expanded) {
                HorizontalDivider()
                LazyColumn(modifier = Modifier.heightIn(max = 240.dp)) {
                    items(tasks, key = { it.id }) { task ->
                        TaskRow(task = task, onToggleComplete = onToggleComplete, onTaskClick = onTaskClick)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
    HorizontalDivider()
}

/** The below-card list: this Project's undated tasks, in their per-Project order. */
@Composable
private fun UndatedList(
    tasks: List<ProjectTaskUi>,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tasks, key = { it.id }) { task ->
            TaskRow(task = task, onToggleComplete = onToggleComplete, onTaskClick = onTaskClick)
            HorizontalDivider()
        }
    }
}

/** A Project task row: leading checkbox completes it; tapping the row opens its edit dialogue. */
@Composable
private fun TaskRow(
    task: ProjectTaskUi,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTaskClick(task.id) }
            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // All Project tasks are active here (completed rows are filtered out) — so the box is always
        // unchecked, and checking it completes the task and sends it to the Today tray.
        Checkbox(checked = false, onCheckedChange = { checked -> onToggleComplete(task.id, checked) })
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp),
        )
        if (task.dateLabel != null) {
            Text(
                text = task.dateLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

/**
 * Plain placeholder empty state. Final empty-state copy and visuals are the parked "Empty state copy
 * and visuals" capture — written later in front of the real screen — so this is deliberately plain,
 * matching the Schedule view's EmptyState.
 */
@Composable
private fun EmptyMessage(text: String, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp),
        )
    }
}
