package com.example.taskflow.ui.navigation

import com.example.taskflow.data.model.ScheduleSlot

/**
 * The pages of the navigation spine, left to right: the four Schedule slots then the Projects
 * overview. (The Strategy doc becomes a spine page in a later batch; in this batch it is only a
 * drawer row that opens a placeholder.) [title] is the header label; [slot] maps the four schedule
 * pages back to their domain slot and is null for the Projects overview.
 */
enum class SpinePage(val title: String, val slot: ScheduleSlot?) {
    TODAY("Today", ScheduleSlot.TODAY),
    TOMORROW("Tomorrow", ScheduleSlot.TOMORROW),
    SOON("Soon", ScheduleSlot.SOON),
    LATER("Later", ScheduleSlot.LATER),
    PROJECTS("Projects", null),
}

/**
 * A destination shown over the spine — reached by a drawer tap or a Project tap, not a swipe. Every
 * Overlay is a placeholder in batch 0003; the real screens arrive later (Project view 0004, Strategy
 * 0015, Settings 0012, Help/Thanks/Report-a-bug 0022, the AI choice flow 0019). [label] is the title
 * the placeholder shows.
 */
sealed interface Overlay {
    val label: String

    data class ProjectView(val projectId: Long, override val label: String) : Overlay
    data object Strategy : Overlay { override val label: String = "Strategy" }
    data object Settings : Overlay { override val label: String = "Settings" }
    data object Help : Overlay { override val label: String = "Help" }
    data object Thanks : Overlay { override val label: String = "Thanks" }
    data object ReportBug : Overlay { override val label: String = "Report a bug" }
    data object TurnOnAi : Overlay { override val label: String = "Turn on AI for the full experience" }
}
