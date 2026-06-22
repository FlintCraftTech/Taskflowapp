package com.example.taskflow.ui.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskflow.TaskflowApplication
import com.example.taskflow.data.model.ScheduleSlot
import com.example.taskflow.ui.navigation.SpinePage
import kotlinx.coroutines.launch

/**
 * The navigation spine: the four Schedule slots (Today, Tomorrow, Soon, Later) as a left-to-right
 * HorizontalPager (SPEC §Schedule view, UX principle 3). Today is the default open page; swiping
 * moves between adjacent pages, and the drawer can jump to any slot by driving the shared
 * [pagerState]. Later is grouped by Project (see [LaterPage]); Projects have no page of their own.
 *
 * Header: the current page's name, centred in a fixed-width frame (as wide as the longest label),
 * sliding in the direction of travel, flanked by chevrons that hide at the spine's ends. The menu
 * key sits in the top-left corner the layout leaves clear; the top-right corner stays clear for the
 * pick-up delete target added in a later batch.
 */
@Composable
fun ScheduleScreen(
    pagerState: PagerState,
    onMenuClick: () -> Unit,
    onTaskClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskflowApplication
    val viewModel: ScheduleViewModel =
        viewModel(factory = ScheduleViewModel.factory(app.taskRepository, app.projectRepository))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        SpineHeader(
            page = SpinePage.entries[pagerState.currentPage],
            hasPrevious = pagerState.currentPage > 0,
            hasNext = pagerState.currentPage < SpinePage.entries.lastIndex,
            onMenuClick = onMenuClick,
            onPrevious = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
            onNext = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
        )
        HorizontalDivider()
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) { page ->
            val slot = SpinePage.entries[page].slot
            if (slot == ScheduleSlot.LATER) {
                // Later is grouped by Project — a list of expand/collapse cards, not a flat list.
                LaterPage(
                    cards = uiState.laterCards,
                    onToggleComplete = viewModel::setCompleted,
                    onTaskClick = onTaskClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                SlotPage(
                    slot = slot,
                    tasks = uiState.forSlot(slot),
                    completed = uiState.completed,
                    onToggleComplete = viewModel::setCompleted,
                    onTaskClick = onTaskClick,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

/** The longest spine label — sizes the title frame so it doesn't resize as the word changes. */
private val LONGEST_LABEL: String = SpinePage.entries.maxByOrNull { it.title.length }!!.title

@Composable
private fun SpineHeader(
    page: SpinePage,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onMenuClick: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Menu key in the cleared top-left corner (SPEC §Side menu).
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
                .clickable(onClick = onMenuClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "☰",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        // Centred current-page title flanked by chevrons.
        Row(verticalAlignment = Alignment.CenterVertically) {
            Chevron(glyph = "←", visible = hasPrevious, onClick = onPrevious)
            AnimatedSpineTitle(page = page)
            Chevron(glyph = "→", visible = hasNext, onClick = onNext)
        }
    }
}

/** The current page name, in a fixed-width frame, sliding in the same direction as a page move. */
@Composable
private fun AnimatedSpineTitle(page: SpinePage) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.clipToBounds()) {
        // Invisible widest label fixes the frame width so the title doesn't reflow per word.
        Text(
            text = LONGEST_LABEL,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alpha(0f),
        )
        AnimatedContent(
            targetState = page,
            transitionSpec = {
                // Forward (to a later page): word leaves left, next enters from the right — the same
                // direction the page content travels. Backward mirrors it.
                val dir = if (targetState.ordinal > initialState.ordinal) 1 else -1
                slideInHorizontally { width -> dir * width }
                    .togetherWith(slideOutHorizontally { width -> -dir * width })
            },
            label = "spineTitle",
        ) { current ->
            Text(
                text = current.title,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** A tappable chevron with a fixed 48dp touch target, sitting immediately beside the title. When
 *  [visible] is false it keeps its space (so the title stays centred) but shows nothing — used at
 *  the spine's ends. */
@Composable
private fun Chevron(glyph: String, visible: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .then(if (visible) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        if (visible) {
            Text(
                text = glyph,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
