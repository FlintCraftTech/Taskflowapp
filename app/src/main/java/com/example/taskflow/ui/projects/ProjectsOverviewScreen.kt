package com.example.taskflow.ui.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.taskflow.data.model.Project

/**
 * The Projects overview spine page (right of Later): a full-page list of the user's Projects in
 * saved order — the same list the drawer shows (SPEC §Projects overview). Tapping a Project opens
 * its Project view (a placeholder until batch 0004). A "+ New Project" affordance creates a Project
 * from a typed name (name-only — a Project's description is its Strategy-doc paragraph, written
 * later). The affordance shows both at the top of the list and in the empty state, since the empty
 * state is exactly when the first Project gets made. The empty-state copy here is a first draft; the
 * polished pedagogical copy is the parked "Empty state copy and visuals" work.
 */
@Composable
fun ProjectsOverviewContent(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onCreateProject: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    if (projects.isEmpty()) {
        EmptyProjects(onAddClick = { showCreateDialog = true }, modifier = modifier)
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                AddProjectRow(onClick = { showCreateDialog = true })
                HorizontalDivider()
            }
            items(projects, key = { it.id }) { project ->
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProjectClick(project) }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                )
                HorizontalDivider()
            }
        }
    }

    if (showCreateDialog) {
        CreateProjectDialog(
            onConfirm = { name ->
                onCreateProject(name)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false },
        )
    }
}

/** The "+ New Project" affordance pinned at the top of the list (SPEC §Projects overview). */
@Composable
private fun AddProjectRow(onClick: () -> Unit) {
    Text(
        text = "+ New Project",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    )
}

/**
 * Name-entry dialog for a new Project — a name is all creation asks for (SPEC §Projects overview).
 * Create stays disabled until the field is non-blank.
 */
@Composable
private fun CreateProjectDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Project") },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text("Name") },
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name) },
                enabled = name.isNotBlank(),
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    )
}

/** First-draft pedagogical empty state with a create affordance — see file header. */
@Composable
private fun EmptyProjects(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp),
        ) {
            Text(
                text = "No Projects yet.\n\nProjects hold the tasks that belong together — a goal, an " +
                    "area of your life, or something ongoing. When you make one, it shows up here and " +
                    "in the menu.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "+ New Project",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .clickable(onClick = onAddClick)
                    .padding(12.dp),
            )
        }
    }
}
