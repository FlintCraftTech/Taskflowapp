package com.example.taskflow.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * The Later page (SPEC §Schedule view — Later grouped by Project). Unlike the other Schedule slots,
 * Later is a vertical list of expand/collapse cards — one per Project, every Project shown (even
 * empty), with the system Unassigned card pinned to the bottom. The cards arrive already ordered by
 * [ScheduleViewModel] (Strategy-doc order, Unassigned last); this composable only renders them.
 *
 * A card is **collapsed by default** — expanding is an opt-in tap — so Later opens as a calm overview
 * of the user's areas of life rather than a wall of tasks. Within-card drag-reorder is a separate
 * batch (see the QUEUE capture); tasks here render, complete, and open to edit, but don't yet drag.
 */
@Composable
fun LaterPage(
    cards: List<LaterProjectCard>,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        cards.forEach { card ->
            ProjectCard(
                card = card,
                onToggleComplete = onToggleComplete,
                onTaskClick = onTaskClick,
            )
            HorizontalDivider()
        }
    }
}

/**
 * One Project's card: a tappable header (name + task count + chevron) over an expandable body. The
 * body shows the Project's Later tasks, or a plain empty message when the Project has none — empty
 * cards still appear so the user sees every area of life on Later.
 */
@Composable
private fun ProjectCard(
    card: LaterProjectCard,
    onToggleComplete: (Long, Boolean) -> Unit,
    onTaskClick: (Long) -> Unit,
) {
    // Collapsed by default; per-card, keyed by projectId so each card keeps its own expand state.
    var expanded by rememberSaveable(card.projectId) { mutableStateOf(false) }

    Surface(tonalElevation = if (card.isUnassigned) 0.dp else 1.dp, modifier = Modifier.fillMaxWidth()) {
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
                    text = card.projectName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (card.isUnassigned) MaterialTheme.colorScheme.onSurfaceVariant
                    else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = card.tasks.size.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                if (card.tasks.isEmpty()) {
                    EmptyCardMessage(isUnassigned = card.isUnassigned)
                } else {
                    // Height-bounded so a large Project's card scrolls within itself rather than
                    // crowding the cards below (soft anti-flood — SPEC §Schedule view).
                    LazyColumn(modifier = Modifier.heightIn(max = 280.dp)) {
                        items(card.tasks, key = { it.id }) { task ->
                            TaskRow(
                                task = task,
                                onToggleComplete = onToggleComplete,
                                onTaskClick = onTaskClick,
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

/** A Later-card task row: leading checkbox completes it; tapping the row opens its edit dialogue. */
@Composable
private fun TaskRow(
    task: ScheduleTaskUi,
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
        // All tasks here are active (completed rows leave for the Today tray), so the box is always
        // unchecked, and checking it completes the task and sends it to the tray.
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
 * Plain placeholder empty state inside an expanded card. Final empty-state copy and visuals are the
 * parked "Empty state copy and visuals" capture — written later in front of the real screen — so
 * this is deliberately plain.
 */
@Composable
private fun EmptyCardMessage(isUnassigned: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (isUnassigned) "Nothing unfiled." else "Nothing in this Project yet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
