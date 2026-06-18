package com.example.taskflow.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskflow.data.model.ScheduleSlot

/**
 * One Schedule page: the slot's active tasks, each tappable (to edit) with a leading checkbox (to
 * complete). On Today, the greyed completed tray (SPEC §Completed task tray on Today) hangs below the
 * active list. An empty state shows only when there is nothing active *and* nothing in the tray.
 */
@Composable
fun SlotPage(
    slot: ScheduleSlot,
    tasks: List<ScheduleTaskUi>,
    completed: List<ScheduleTaskUi>,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showTray = slot == ScheduleSlot.TODAY && completed.isNotEmpty()
    if (tasks.isEmpty() && !showTray) {
        EmptyState(slot, modifier)
        return
    }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tasks, key = { it.id }) { task ->
            TaskRow(
                task = task,
                completed = false,
                onToggle = { checked -> onToggleComplete(task.id, checked) },
                onClick = { onTaskClick(task.id) },
            )
            HorizontalDivider()
        }
        if (showTray) {
            item(key = "completed-header") { CompletedHeader() }
            items(completed, key = { "done-${it.id}" }) { task ->
                TaskRow(
                    task = task,
                    completed = true,
                    onToggle = { checked -> onToggleComplete(task.id, checked) },
                    onClick = { onTaskClick(task.id) },
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: ScheduleTaskUi,
    completed: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(end = 16.dp, top = 4.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Checkbox(checked = completed, onCheckedChange = onToggle)
        // Title takes the available width and wraps to multiple lines when long.
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (completed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            textDecoration = if (completed) TextDecoration.LineThrough else null,
            modifier = Modifier
                .weight(1f)
                .padding(top = 12.dp),
        )
        // Date label (when shown) stays aligned to the top-right of the row.
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

/** Divider-and-label that opens the greyed completed tray at the bottom of Today. */
@Composable
private fun CompletedHeader() {
    Text(
        text = "Completed",
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

/**
 * Placeholder empty state. Final empty-state copy and visuals are the parked "Empty state copy and
 * visuals" capture — written later in front of the real screen — so this is deliberately plain.
 */
@Composable
private fun EmptyState(slot: ScheduleSlot, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Nothing on ${slot.displayName()} yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(32.dp),
        )
    }
}

internal fun ScheduleSlot.displayName(): String = when (this) {
    ScheduleSlot.TODAY -> "Today"
    ScheduleSlot.TOMORROW -> "Tomorrow"
    ScheduleSlot.SOON -> "Soon"
    ScheduleSlot.LATER -> "Later"
}
