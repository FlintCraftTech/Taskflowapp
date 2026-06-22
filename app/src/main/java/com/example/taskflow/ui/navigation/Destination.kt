package com.example.taskflow.ui.navigation

import com.example.taskflow.data.model.ScheduleSlot

/**
 * The pages of the navigation spine, left to right: the four Schedule slots. Later is grouped by
 * Project (there is no separate Projects page — Projects live inside Later). The Strategy doc becomes
 * a spine page in a later batch; for now it is a drawer row that opens a placeholder. [title] is the
 * header label; [slot] maps each page back to its domain slot.
 */
enum class SpinePage(val title: String, val slot: ScheduleSlot) {
    TODAY("Today", ScheduleSlot.TODAY),
    TOMORROW("Tomorrow", ScheduleSlot.TOMORROW),
    SOON("Soon", ScheduleSlot.SOON),
    LATER("Later", ScheduleSlot.LATER),
}

/**
 * A destination shown over the spine — reached by a drawer tap, not a swipe. Every Overlay is a
 * placeholder until its real screen is built (Strategy 0015, Settings 0012, Help/Thanks/Report-a-bug
 * 0022, the AI choice flow 0019). [label] is the title the placeholder shows.
 */
sealed interface Overlay {
    val label: String

    data object Strategy : Overlay { override val label: String = "Strategy" }
    data object Settings : Overlay { override val label: String = "Settings" }
    data object Help : Overlay { override val label: String = "Help" }
    data object Thanks : Overlay { override val label: String = "Thanks" }
    data object ReportBug : Overlay { override val label: String = "Report a bug" }
    data object TurnOnAi : Overlay { override val label: String = "Turn on AI for the full experience" }
}
