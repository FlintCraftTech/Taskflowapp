package com.example.taskflow.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.taskflow.ui.common.PlaceholderScreen
import com.example.taskflow.ui.edit.EditTarget
import com.example.taskflow.ui.edit.EditTaskScreen
import com.example.taskflow.ui.schedule.ScheduleScreen
import kotlinx.coroutines.launch

/**
 * Top-level navigation host. Wraps the app in a left-edge drawer (SPEC §Side menu) over the
 * navigation spine. The spine — the four Schedule slots (Today · Tomorrow · Soon · Later) — is the
 * home surface; Projects live inside Later (grouped cards), so there is no separate Project surface.
 * The remaining "deep" destinations (Strategy, the app actions) are shown as a placeholder overlay
 * in this batch. The drawer scrolls the spine for slot taps and opens overlays for the rest.
 */
@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = SpinePage.TODAY.ordinal) { SpinePage.entries.size }
    var overlay by remember { mutableStateOf<Overlay?>(null) }
    // The edit dialogue sits above the spine and any overlay; it's its own state so closing it
    // returns to whatever was underneath rather than the bare spine.
    var editTarget by remember { mutableStateOf<EditTarget?>(null) }

    // System back closes the edit dialogue first, then an open overlay. (When the drawer is open it
    // owns back itself, so this only fires on the spine-with-overlay-or-edit state.)
    BackHandler(enabled = editTarget != null || overlay != null) {
        if (editTarget != null) editTarget = null else overlay = null
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onSlot = { page ->
                    overlay = null
                    scope.launch {
                        drawerState.close()
                        pagerState.scrollToPage(page.ordinal)
                    }
                },
                onStrategy = {
                    overlay = Overlay.Strategy
                    scope.launch { drawerState.close() }
                },
                onAppAction = { action ->
                    overlay = action
                    scope.launch { drawerState.close() }
                },
            )
        },
        modifier = modifier,
    ) {
        Scaffold(
            floatingActionButton = {
                // The add FAB (SPEC §Add a new task). It appears on the four Schedule slots — the
                // surfaces whose context a new task inherits — and is hidden over the placeholder
                // overlays and the edit dialogue itself. On Later the new task is created undated and
                // defaults to the Unassigned card.
                val showFab = editTarget == null && overlay == null
                if (showFab) {
                    val slot = SpinePage.entries[pagerState.currentPage].slot
                    FloatingActionButton(onClick = { editTarget = EditTarget.NewOnSlot(slot) }) {
                        // Text glyph rather than a Material icon — the icon pack isn't a dependency.
                        Text(text = "+", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            val currentEdit = editTarget
            val currentOverlay = overlay
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            val openEditor: (Long) -> Unit = { taskId -> editTarget = EditTarget.Existing(taskId) }

            if (currentEdit != null) {
                EditTaskScreen(
                    target = currentEdit,
                    onClose = { editTarget = null },
                    modifier = contentModifier,
                )
            } else if (currentOverlay == null) {
                ScheduleScreen(
                    pagerState = pagerState,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onTaskClick = openEditor,
                    modifier = contentModifier,
                )
            } else {
                PlaceholderScreen(
                    title = currentOverlay.label,
                    onBack = { overlay = null },
                    modifier = contentModifier,
                )
            }
        }
    }
}
