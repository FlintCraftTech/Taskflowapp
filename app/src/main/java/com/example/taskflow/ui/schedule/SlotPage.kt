package com.example.taskflow.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskflow.data.model.ScheduleSlot

/** One Schedule page: the slot's tasks, or an empty state when there are none. Read-only. */
@Composable
fun SlotPage(
    slot: ScheduleSlot,
    tasks: List<ScheduleTaskUi>,
    modifier: Modifier = Modifier,
) {
    if (tasks.isEmpty()) {
        EmptyState(slot, modifier)
        return
    }
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(tasks, key = { it.id }) { task ->
            TaskRow(task)
            HorizontalDivider()
        }
    }
}

@Composable
private fun TaskRow(task: ScheduleTaskUi) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        // Title takes the available width and wraps to multiple lines when long.
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        // Date label (when shown) stays aligned to the top-right of the row, beside the first
        // line of a wrapped title.
        if (task.dateLabel != null) {
            Text(
                text = task.dateLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
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
