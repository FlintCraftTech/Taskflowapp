package com.example.taskflow.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.TaskflowApplication
import com.example.taskflow.ui.common.PlaceholderScreen
import com.example.taskflow.ui.schedule.ScheduleScreen
import kotlinx.coroutines.launch

/**
 * Top-level navigation host. Wraps the app in a left-edge drawer (SPEC §Side menu) over the
 * navigation spine. The spine — the four Schedule slots then the Projects overview — is the home
 * surface; "deep" destinations (a Project, Strategy, the app actions) are shown as a placeholder
 * overlay in this batch. The drawer scrolls the spine for slot taps and opens overlays for the rest.
 */
@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskflowApplication
    val appViewModel: AppViewModel = viewModel(factory = AppViewModel.factory(app.projectRepository))
    val projects by appViewModel.projects.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = SpinePage.TODAY.ordinal) { SpinePage.entries.size }
    var overlay by remember { mutableStateOf<Overlay?>(null) }

    // An open overlay catches the system back, returning to the spine. (When the drawer is open it
    // owns back itself, so this only fires on the spine-with-overlay state.)
    BackHandler(enabled = overlay != null) { overlay = null }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                projects = projects,
                onSlot = { page ->
                    overlay = null
                    scope.launch {
                        drawerState.close()
                        pagerState.scrollToPage(page.ordinal)
                    }
                },
                onProject = { project ->
                    overlay = Overlay.ProjectView(project.id, project.name)
                    scope.launch { drawerState.close() }
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
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val currentOverlay = overlay
            if (currentOverlay == null) {
                ScheduleScreen(
                    pagerState = pagerState,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    projects = projects,
                    onProjectClick = { project ->
                        overlay = Overlay.ProjectView(project.id, project.name)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            } else {
                PlaceholderScreen(
                    title = currentOverlay.label,
                    onBack = { overlay = null },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                )
            }
        }
    }
}
