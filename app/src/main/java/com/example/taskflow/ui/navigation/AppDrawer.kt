package com.example.taskflow.ui.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * The side drawer: one navigation list mirroring the spine top to bottom — the four Schedule slots
 * (Today · Tomorrow · Soon · Later) then a single Strategy doc row — with the app actions pinned
 * apart at the bottom (SPEC §Side menu). Tapping a slot moves the spine; tapping the Strategy row or
 * an app action opens a placeholder in this batch. Projects are not listed here — they live inside
 * Later (reached by swiping the spine to Later), which is how the user reaches any Project.
 */
@Composable
fun AppDrawer(
    onSlot: (SpinePage) -> Unit,
    onStrategy: () -> Unit,
    onAppAction: (Overlay) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Navigation list.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
            ) {
                SpinePage.entries.forEach { page ->
                    DrawerRow(text = page.title) { onSlot(page) }
                }
                DrawerRow(text = "Strategy") { onStrategy() }
            }
            // App actions, pinned apart at the bottom.
            HorizontalDivider()
            DrawerRow(text = "Settings") { onAppAction(Overlay.Settings) }
            DrawerRow(text = "Help") { onAppAction(Overlay.Help) }
            DrawerRow(text = "Thanks") { onAppAction(Overlay.Thanks) }
            DrawerRow(text = "Report a bug") { onAppAction(Overlay.ReportBug) }
            DrawerRow(text = "Turn on AI for the full experience") { onAppAction(Overlay.TurnOnAi) }
        }
    }
}

@Composable
private fun DrawerRow(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    )
}
