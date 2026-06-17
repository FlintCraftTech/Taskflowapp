package com.example.taskflow.ui.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import kotlinx.coroutines.launch

/**
 * The Schedule view: the four slots (Today, Tomorrow, Soon, Later) as the left segment of the
 * left-to-right navigation spine (SPEC §Schedule view, UX principle 3). Today is the default open
 * page; swiping moves between adjacent pages. Later batches extend the spine rightward into the
 * Projects overview and Strategy pages — this batch builds only the schedule segment.
 *
 * Header: only the current page's name is shown, centred in a fixed-width frame (as wide as the
 * longest label, "Tomorrow"), and it slides in the same direction as the page when you move. A
 * chevron sits immediately either side of the word to hint the spine swipes (and is tappable),
 * hidden at a dead end — no left chevron on the first page, no right on the last. The screen
 * corners are deliberately left clear for the side-menu key (top-left) and the pick-up delete
 * target (top-right), both added in later batches.
 */
private val SLOTS: List<ScheduleSlot> = listOf(
    ScheduleSlot.TODAY,
    ScheduleSlot.TOMORROW,
    ScheduleSlot.SOON,
    ScheduleSlot.LATER,
)

/** The longest label — sizes the title frame so it doesn't resize as the word changes. */
private val LONGEST_LABEL: String = SLOTS.maxByOrNull { it.displayName().length }!!.displayName()

@Composable
fun ScheduleScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val app = context.applicationContext as TaskflowApplication
    val viewModel: ScheduleViewModel = viewModel(factory = ScheduleViewModel.factory(app.taskRepository))
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(initialPage = 0) { SLOTS.size }
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        SlotHeader(
            slot = SLOTS[pagerState.currentPage],
            hasPrevious = pagerState.currentPage > 0,
            hasNext = pagerState.currentPage < SLOTS.lastIndex,
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
            val slot = SLOTS[page]
            SlotPage(
                slot = slot,
                tasks = uiState.forSlot(slot),
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

@Composable
private fun SlotHeader(
    slot: ScheduleSlot,
    hasPrevious: Boolean,
    hasNext: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Chevron(glyph = "←", visible = hasPrevious, onClick = onPrevious)
        AnimatedSlotTitle(slot = slot)
        Chevron(glyph = "→", visible = hasNext, onClick = onNext)
    }
}

/** The current slot name, in a fixed-width frame, sliding in the same direction as a page move. */
@Composable
private fun AnimatedSlotTitle(slot: ScheduleSlot) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.clipToBounds()) {
        // Invisible widest label fixes the frame width so the title doesn't reflow per word.
        Text(
            text = LONGEST_LABEL,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.alpha(0f),
        )
        AnimatedContent(
            targetState = slot,
            transitionSpec = {
                // Forward (to a later slot): word leaves left, next enters from the right —
                // the same direction the page content travels. Backward mirrors it.
                val dir = if (targetState.ordinal > initialState.ordinal) 1 else -1
                slideInHorizontally { width -> dir * width }
                    .togetherWith(slideOutHorizontally { width -> -dir * width })
            },
            label = "slotTitle",
        ) { current ->
            Text(
                text = current.displayName(),
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
