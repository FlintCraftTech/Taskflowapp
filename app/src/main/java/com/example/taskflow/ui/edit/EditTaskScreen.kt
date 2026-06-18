package com.example.taskflow.ui.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
 * The task edit dialogue (SPEC §Edit a task), shown full-screen over the spine or a Project view.
 * Edits title, notes, and Project (including "unassigned"); the date is displayed but read-only this
 * batch — the side-scrolling date picker that makes it editable is batch 0006.
 *
 * The same dialogue serves *new* tasks: the FAB opens it with the surface's inherited context already
 * resolved by [EditTaskViewModel] (SPEC §Add a new task). Save inserts or updates, then [onClose].
 */
@Composable
fun EditTaskScreen(
    target: EditTarget,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskflowApplication
    val viewModel: EditTaskViewModel = viewModel(
        // Key by the target so opening a different task (or a fresh add) rebuilds the form rather
        // than reusing the previous dialogue's fields.
        key = "edit-${target.viewModelKey()}",
        factory = EditTaskViewModel.factory(app.taskRepository, app.projectRepository, target),
    )
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        EditHeader(
            isNew = state.isNew,
            canSave = state.canSave,
            onClose = onClose,
            onSave = { viewModel.save(onClose) },
        )
        HorizontalDivider()

        if (state.loading) return@Column

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = state.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
            )
            ProjectField(
                projectId = state.projectId,
                projects = state.projects.map { it.id to it.name },
                onProjectChange = viewModel::onProjectChange,
            )
            DateField(dateLabel = state.dateLabel)
        }
    }
}

@Composable
private fun EditHeader(
    isNew: Boolean,
    canSave: Boolean,
    onClose: () -> Unit,
    onSave: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clickable(onClick = onClose),
            contentAlignment = Alignment.Center,
        ) {
            // Text glyph rather than a Material icon — the icon pack isn't a project dependency.
            Text(
                text = "←",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Text(
            text = if (isNew) "New task" else "Edit task",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f),
        )
        TextButton(onClick = onSave, enabled = canSave) {
            Text("Save")
        }
    }
}

/** Editable Project picker: a button showing the current Project (or "Unassigned") plus a menu. */
@Composable
private fun ProjectField(
    projectId: Long?,
    projects: List<Pair<Long, String>>,
    onProjectChange: (Long?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val currentName = projects.firstOrNull { it.first == projectId }?.second ?: UNASSIGNED

    Column {
        Text(
            text = "Project",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(currentName, modifier = Modifier.weight(1f), textAlign = TextAlign.Start)
                Text(if (expanded) "▾" else "▸")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text(UNASSIGNED) },
                    onClick = {
                        onProjectChange(null)
                        expanded = false
                    },
                )
                projects.forEach { (id, name) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            onProjectChange(id)
                            expanded = false
                        },
                    )
                }
            }
        }
    }
}

/**
 * The date, shown read-only this batch. The side-scrolling date strip that makes it editable is
 * batch 0006; until then the dialogue shows the date the task already carries (or "No date").
 */
@Composable
private fun DateField(dateLabel: String) {
    Column {
        Text(
            text = "Date",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = dateLabel, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.width(12.dp))
            Text(
                text = "(set in a later update)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private const val UNASSIGNED = "Unassigned"

/** A stable key per target so the dialogue's view-model is rebuilt when the target changes. */
private fun EditTarget.viewModelKey(): String = when (this) {
    is EditTarget.Existing -> "existing-$taskId"
    is EditTarget.NewOnSlot -> "new-slot-$slot"
    is EditTarget.NewInProject -> "new-project-$projectId"
}
