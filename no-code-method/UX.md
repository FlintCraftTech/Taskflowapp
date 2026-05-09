# UX.md — Taskflow User Experience

This document describes every functionality and UI element as the user experiences it, and why the user needs it. Every entry must correspond to something that actually exists in the current build. If an entry cannot be traced to an existing feature, it is not a current user experience — it is a plan, and it belongs in `BACKLOG.md`.

Taskflow is a personal task manager for Android. It uses the Google Tasks API as its only backend — there is no separate database. What makes Taskflow different from the official Google Tasks app is the framing: tasks are organised by **time horizon** (when they are due), not by list.

## UX principles for Taskflow

These inform every design decision. Entries below should serve one or more of these principles. If a proposed change conflicts with a principle, flag the conflict before building.

1. **Time horizon, not list, is the primary frame.** People plan their day by asking "what's due today?", their week by asking "what's coming up?", and rarely by asking "what's on my Work list?". The four screens (Today, Tomorrow, Soon, Later) map onto how attention naturally scales with time. Lists exist, but they live in a side drawer because they answer a less frequent question.

2. **Google Tasks is the source of truth for tasks.** Every task and every task's date round-trips through the Google Tasks API. No local task database, no shadow state for task content. This keeps the user's data portable, recoverable from the official Google Tasks app, and consistent with anything else they wire into Google Tasks. A small set of UX-only preferences live locally and do not survive uninstall — currently the Day begins at setting, plus screen-parking overrides for tasks whose displayed screen does not match their date (undated tasks parked on Soon, and Later-dated tasks parked on Soon). That's the explicit trade-off for not maintaining a parallel database.

3. **Every task is on a screen, even if it has no date.** Undated tasks vanish into "someday" and never get done. Taskflow forces every task onto one of the four time-horizon screens. Tasks on Today and Tomorrow always have a specific date (today's date, tomorrow's date). Tasks on Soon and Later may have a specific date OR may be undated. A dated task auto-progresses through the screens as time advances (a task dated three days from now appears on Soon today, on Tomorrow in two days, on Today in three days). An undated task stays put on whichever of Soon or Later the user placed it on, until the user moves it or gives it a date. Either way, no task is allowed to live in a "someday" pile outside the four screens.

4. **Spatial action equals temporal change.** Dragging a task between screens changes when it lives in the user's mental schedule. For dated tasks, the drag rewrites the date; for undated tasks, it changes which screen they sit on. Either way, the user reschedules by moving things, not by opening an editor and adjusting a field. The screen a task is on IS its time horizon, expressed visually.

5. **Taskflow does dates, Calendar does times.** Taskflow has no time-of-day picker anywhere in the app except for a single setting that controls when "today" rolls over (see *Settings → Day begins at*). If a task needs a specific time of day, it belongs in Google Calendar — dragging items up and down a Calendar day is faster and more accurate than any time picker, and Calendar already does this well. Keeping time-of-day out of Taskflow keeps Taskflow focused on the one thing it does that Calendar doesn't: the four-screen time-horizon view.

6. **Honest about Google Tasks API limits.** Where the Google Tasks API does not expose something (notably recurrence rules), Taskflow shows the limitation rather than hiding it. Greyed-out fields, plain-language toasts, and a clear pointer to the official Google Tasks app for the operations Taskflow cannot perform. The UI is structured so that if the API is later updated, those greyed-out fields can be made live with minimal changes.

## Functionalities

### Four time-horizon screens

The app has four primary screens: **Today**, **Tomorrow**, **Soon** (due 2–7 days from now), and **Later** (due more than 7 days from now). The four screens are navigated by horizontal swipe in the manner of a phone home screen — each screen is a discrete page, and the user swipes left/right to move between them. A task appears on exactly one screen, determined by its due date relative to the current local date. Recurring tasks appear on whichever screen their next instance falls into; they are never shown on Later (see Recurring tasks).

The user needs this because time horizon, not list membership, is how they actually plan (UX principle 1). Splitting into four discrete swipeable pages — rather than one infinite scroll sorted by date — makes each horizon a bounded, glanceable thing, and uses a navigation gesture (page swipe) the user already knows from their home screen.

### Today screen

The Today screen shows every task in the active list whose due date is today, plus every task whose due date is in the past (overdue tasks roll forward into Today rather than being hidden on a date the user has already passed). Overdue tasks are visually marked so the user knows they did not originally belong to today. Below the active task list, a greyed-out "Completed" tray shows tasks the user has checked off — see Completed task tray.

The user needs this because the question "what do I need to do today?" must include things they meant to do yesterday but did not. Hiding overdue items on dates that have already gone by is the failure mode this screen exists to prevent.

### Tomorrow screen

The Tomorrow screen shows every task in the active list whose due date is tomorrow.

The user needs this because tomorrow is the planning horizon for "what should I prepare for tonight?" — distinct enough from Today that conflating them under "Upcoming" loses information.

### Soon screen

The Soon screen shows every task in the active list whose due date is between 2 and 7 days from now (inclusive), plus any task that the user has manually parked on Soon (whether undated, or dated with a date outside the Soon range — see *Drag a task between screens to change its date*). Tasks are listed in date order; each dated task shows its DD/MM date inline (see *Date display*). Parked tasks with dates appear in the position their date implies (potentially after the natural Soon dates if their date is in the Later range). Parked undated tasks appear at the bottom. There is no special visual marker distinguishing parked tasks from naturally-on-Soon tasks — the date itself (or absence of one) is the only cue.

The name "Soon" is deliberately chosen over "This Week" because week boundaries are ambiguous (does Wednesday's "this week" include Sunday?) and the 7-day rolling window has no such ambiguity.

The user needs this because the 2–7 day horizon is where most actively-scheduled work lives, and the user wants to see it without committing to a calendar grid.

### Later screen

The Later screen shows every task in the active list whose due date is more than 7 days from now, plus any undated tasks (which originate here by default — see *Onboarding* and *Add a new task*). Tasks are listed in date order; each dated task shows its DD/MM date inline (see *Date display*). Undated tasks appear at the bottom. Recurring tasks are NOT shown here — only their next-due instance is shown, on whichever of Today/Tomorrow/Soon it falls into.

The user needs this because "more than a week away" is a single mental category — the user does not need to plan around exact dates that far out, they only need to know the task exists and roughly when it will surface.

### Date display

Every dated task shows its date inline on the right edge of the task row, in **DD/MM** format, regardless of which calendar year the date falls in. If the task name is too long to fit alongside the date, the task name is truncated with an ellipsis. Tasks with no date show no date label.

The DD/MM label is shown per screen as follows:

- **Today** — shown only on overdue tasks (where the date communicates how overdue). Tasks with today's actual date do not show a DD/MM label, since "today" is implied by the screen.
- **Tomorrow** — shown on every dated task on the screen.
- **Soon** — shown on every dated task on the screen.
- **Later** — shown on every dated task on the screen.

The user needs this because the time horizon screen tells you roughly when a task is due, but for actively-scheduled work the user often needs the specific day at a glance — particularly on Soon and Later where the screen spans many possible dates, and on Today where overdue tasks need to communicate how stale they are.

### Hamburger menu

A hamburger menu on the left edge opens a side drawer with two sections.

**Top section — Lists.** Every task list in the user's Google Tasks account, in the same order the user has them in Google Tasks. Tapping a list makes it the active list; the four time-horizon screens then show only tasks from that list. The currently active list name is shown in the app bar.

**Bottom section — App actions.** Pinned to the bottom of the drawer, separated from the lists: **Settings**, **Help**, **Thanks**, and **Report a bug**.

The user needs this because lists answer a less frequent question than time horizons (UX principle 1) and belong off the primary navigation, but they still need to be reachable in one tap when the user does want to switch context (e.g., from Personal to Work). The bottom section keeps app-level actions out of the time-horizon screens (which should be only about tasks) while making them findable in the obvious place.

### Settings → Day begins at

The Settings screen contains a single time picker called **Day begins at**. This is the only time picker in the entire app (UX principle 5). It controls when "today" rolls over to the next day for the purposes of screen placement: a task dated for the 5th of the month appears on the Today screen from "Day begins at" on the 5th until "Day begins at" on the 6th, at which point it rolls onto Tomorrow's screen (or rolls into the Today list if it was already due that day).

The user needs this because people who stay up past midnight do not consider the day to have ended — a task they meant to do "today" at 1 AM should still be on Today, not Tomorrow. A single, configurable cutoff lets the user define their own day boundary without polluting the rest of the app with time pickers.

### Drag a task between screens to change its date

Tasks are draggable in the manner of icons on a phone home screen. Long-pressing a task picks it up; the user can then drag it to the left or right edge of the current screen to swipe through to the adjacent time-horizon screen, and drop it there. Dropping a task on a different screen changes its time horizon. Most drops also rewrite the task's date; one specific drop (Later → Soon) leaves the date alone and parks the task on the destination screen as a local override instead.

For a **dated** task:

- Drop on **Today** → date becomes today.
- Drop on **Tomorrow** → date becomes tomorrow.
- Drop on **Soon** from Today or Tomorrow → date becomes the start of the Soon range (today + 2). On the next day this task will sit on Tomorrow; the day after, on Today.
- Drop on **Soon** from Later → date is left alone; the task is **parked on Soon** as a local override. The task appears on Soon despite its date being in the Later range. It will sit on Soon "weirdly long" — longer than the natural six-day Soon window — until its date eventually enters Tomorrow's range (date = today + 1), at which point natural progression takes over and the task advances to Tomorrow, then Today. Parking is local to the device (see Principle 2) and does not survive uninstall.
- Drop on **Later** → date becomes the start of the Later range (today + 8); any parking is cleared. A toast confirms: "task postponed until [DD/MM]". Any time-of-day data on the task (which Taskflow does not display or edit) is left untouched on the underlying record. On the next day this task will sit on Soon; it will continue to advance one screen per day until it reaches its assigned date.

For an **undated** task (which originates on Later by default, since the Google Tasks API has no way to remember any other screen for an undated task):

- Drop on **Today** → user is prompted to give it today's date (it cannot remain undated on Today).
- Drop on **Tomorrow** → user is prompted to give it tomorrow's date (it cannot remain undated on Tomorrow).
- Drop on **Soon** → the task stays undated and is parked on Soon (local override, same kind as the Later → Soon parking above).
- Drop on **Later** → no-op (already there); any prior Soon-parking is cleared.

When a parent task is dragged, its subtasks travel with it as a single unit — the parent's date (or screen parking) is rewritten and the children continue to live under the parent on the new screen.

The user needs this because rescheduling is the single most common edit a person makes to a task list, and spatial drag-to-reschedule is faster and more intuitive than opening an editor and tapping a date field (UX principle 4). The Later → Soon parking exception preserves the user's previously-considered Later date — pulling a task forward to "show me this sooner" should not destroy the date the user previously chose.

### Tasks can be reordered within a day

Within a single day's group of tasks, the user can reorder tasks by drag (without leaving the day). The new order is saved using Google Tasks' `position` field, so the order persists across devices.

The user needs this because tasks on the same day still have an order — what to do first, what to do last — and that order is the user's own ranking, not a date-based one.

### Add a new task

The user can add a new task from any screen via a floating action button or equivalent affordance. The new task is created with a default due date matching the current screen: today's date on Today, tomorrow's date on Tomorrow, today+2 (start of Soon range) on Soon, today+8 (start of Later range) on Later — matching the cross-screen drag rule. The new task is created in the currently active list. The user can change or clear the date inside the edit dialogue via the date strip (see *Date picker — side-scrolling date strip*).

The user needs this because every task must end up on a screen (UX principle 3), and the most natural starting date for a new task is the one matching the screen the user is currently looking at.

### Edit a task

Tapping a task opens an edit dialogue showing its title, notes, due date, list, and recurrence rule. The due date is set via the in-dialogue date strip (see *Date picker — side-scrolling date strip*) — no time-of-day picker anywhere in this dialogue (UX principle 5). The recurrence rule is **visible but greyed out** — tapping it shows a toast: "Edit recurrence in the Google Tasks app." This is intentional; the Google Tasks API does not expose recurrence to third parties, so Taskflow cannot read or write recurrence rules even though it can see that a task is recurring. All other fields are editable.

The user needs this because they need a way to change task details, and they need to understand — without confusion or a feeling that Taskflow is broken — why one field is uneditable (UX principle 6).

### Date picker — side-scrolling date strip

The date field inside the edit dialogue is a horizontal, side-scrollable strip of date tiles, embedded directly in the dialogue (not a popup). Each tile shows a single date in DD/MM format. The user scrolls left and right to find a date, and taps a tile to select it as the task's due date. The selected tile is visually highlighted.

Date tiles fade to grey the further they are from today, in either direction. The intent is to give the user a sense of "how far have I scrolled" so they don't get lost mid-scroll on a long horizontal list. Today is the visual anchor — the least-grey tile.

The user needs this because the standard Android date picker (a calendar grid in a popup) is heavy for this app's purpose. Most date assignments in Taskflow are within a week or two of today, and a horizontal strip lets the user scrub through nearby dates as fast as their thumb. Embedding the picker in the dialogue (rather than popping out a modal) keeps the editing flow continuous.

### Edit dialogue: outliner-style typing for subtasks

Inside the edit dialogue, a task and its subtasks are rendered as a small outliner — the parent task's title on the top line, child task titles indented beneath. The text area behaves like a normal paragraph editor:

- Pressing **Enter** at the end of a line creates a new line below it. A new line typed below the parent becomes a child of that parent.
- Pressing **Backspace** at the start of a line deletes that line and merges its remaining text into the end of the previous line — exactly the way backspace-at-line-start behaves in any paragraph editor. The previous line can be the parent or a sibling child.

Each child line in the dialogue has a drag handle on its left edge (the parent line does not — only subtasks have handles). Dragging a child by its handle reveals a row of drag-target icons (see *Drag-target icons*); dropping the child on the **promote** icon promotes it to a top-level task at the bottom of the actionable tasks list on the parent's screen, with the same date as the parent. If the parent ends up with zero children after a promotion, it stops being a parent and reverts to having a checkbox.

The user needs this because adding and breaking down subtasks are the highest-frequency edits a person makes when capturing work. Lifting subtask creation into the same gestures used for any text editing (Enter for new line, Backspace for delete-into-previous) makes capturing a parent-with-children as fast as typing a paragraph. The promote target covers the natural follow-up — "actually this child should stand on its own" — without requiring a separate menu or mode.

### Drag-target icons

Whenever the user picks up a task by drag (whether from a time-horizon screen or from inside an edit dialogue), a row of drag-target icons appears at the **top right** of the screen, in a fixed position. The set of icons depends on context:

- **From a time-horizon screen:** a **bin** icon (delete the task) and a **cut** icon (remove the task and place its content on the device clipboard for pasting elsewhere).
- **From inside an edit dialogue (subtask only):** a **bin** icon, a **cut** icon, and a **promote** icon (the standard "list" Material icon with the arrow pointing up). Promote behaviour is described in *Edit dialogue: outliner-style typing for subtasks*.

When the user drags over a target icon, the icon visually responds — for example, growing slightly larger and gaining a shadow — so the user can see they have hit the right target before releasing.

The cut/paste flow uses the **device's OS clipboard**, not a Taskflow-internal one. Dropping a task on **cut** removes it from Taskflow and writes its content to the OS clipboard as **plain text**. When the user cuts a parent, the whole set (parent + all children) goes to the clipboard as a single block of indented multi-line plain text — the parent title on the first line, child titles indented beneath, mirroring the structure shown in the edit dialogue.

Paste is only available **inside an edit dialogue** (not on a time-horizon screen) — the user long-presses inside any text field of a dialogue and chooses Paste from the device's standard context menu. The outliner editor interprets the pasted text by its line breaks and indentation: a multi-line indented block restored Taskflow-to-Taskflow round-trips its hierarchy. Pasting non-Taskflow text just inserts that text into the editor, where it becomes new lines per the same indentation rule. To create a new top-level task from a cut task, the user creates an empty new task via the FAB, opens its edit dialogue, and pastes there.

**Risk accepted:** if the OS clipboard is overwritten between cut and paste (e.g., by another app copying a notification number), the cut task is lost permanently. The user has accepted this trade-off in exchange for the simplicity of using the device's native clipboard.

The user needs this because the operations Taskflow exposes on a dragged task (delete, cut, promote) are conceptually parallel — each one is "send the task to a destination" — and giving them all the same drag-to-icon shape keeps the gesture vocabulary consistent. Routing paste through the device's normal paste mechanism, rather than building a Taskflow-internal paste UI, means the user's existing muscle memory works.

### Onboarding: imported tasks without dates go to Later

The first time the user signs in, Taskflow imports their existing Google Tasks. Two transformations happen during import:

**Undated parents go to Later.** Any top-level task without a due date is placed on the Later screen, undated. (Once placed there, the task can stay undated indefinitely or the user can give it a date later.)

**Subtask dates are stripped, with parent inheritance.** Subtasks lose any independent due date they had in Google Tasks (see *Subtasks live under their parent*). For each parent, the parent's date is set as follows:

- If the parent already has a date, it keeps that date. Subtask dates are discarded.
- If the parent has no date AND exactly one subtask has a date, the parent inherits that subtask's date.
- If the parent has no date AND multiple subtasks have dates that all agree, the parent inherits that shared date.
- If the parent has no date AND multiple subtasks have dates that disagree, the parent inherits the soonest of those dates. The whole task (parent plus all children) then lives on the screen matching that date.

After import, a one-time popup explains in plain language what happened: undated tasks went to Later, and subtasks lost their independent dates (with the parent-inheritance rule above). The popup makes the import behaviour explicit so the user understands why their data looks different.

The user needs this because silently changing the user's existing data without explanation would break trust, and because Taskflow's "subtasks live under their parent" model is incompatible with the Google Tasks model where subtasks can have independent dates — the import has to bridge the two.

### Subtasks live under their parent

Google Tasks supports one level of subtasks. In Taskflow, subtasks always appear nested under their parent on whichever time-horizon screen the parent is on. They do not have their own due date and do not get their own time-horizon placement. When a parent is dragged to a different screen, its children move with it as a single unit (see *Drag a task between screens to change its date*).

A subtask is visible on the main screen when its parent is expanded (see *Parent tasks expand/collapse instead of having a checkbox*). When the parent is collapsed, the subtasks are hidden but still counted in any parent-level summary.

The Google Tasks API technically allows a subtask to carry its own due date, but Taskflow does not expose this — subtask dates are stripped on import (see *Onboarding: imported tasks without dates go to Later*) and cannot be set inside the Taskflow edit dialogue.

The user needs this because subtasks are sub-units of a parent task, not independent items. Treating them as part of their parent — moving when the parent moves, listed under the parent on the parent's screen, sharing one due date — keeps the time-horizon view focused on the unit of work the user actually thinks about (the parent task) and avoids the conflict-of-truth problem where a parent's date and a child's date disagree.

### Parent tasks expand/collapse instead of having a checkbox

A parent task (one with subtasks) does not have a checkmark on the time-horizon screens. Where the checkmark would be, there is an expand/collapse control. Tapping it reveals the parent's subtasks inline beneath the parent on the current screen, indented. Tapping it again hides them. Tapping the parent's title (not the expand control) opens the edit dialogue as normal.

A parent is considered complete only when all its subtasks are complete. Completing the last subtask automatically completes the parent and sends the parent to the Completed tray (see below). Un-completing any subtask un-completes the parent and brings it back out of the tray onto its time-horizon screen.

The user needs this because manually checking a parent while subtasks remain open would create a state mismatch between Taskflow and Google Tasks. Driving completion from the children up makes the parent's completion state derivable rather than user-entered.

### Completed task tray on Today

At the bottom of the Today screen is a greyed-out, scrollable list of completed tasks. When the user checks off any task on any screen, that task moves to the bottom of this tray. Tapping a completed task in the tray opens its edit dialogue, where the user can uncheck individual subtasks (which un-completes the parent and removes it from the tray) or otherwise modify the task.

The user needs this because seeing what they have already done on a given day provides a sense of progress, and routing all completions through one tray (rather than letting them disappear from each screen individually) gives the user one place to find and undo a mis-tapped completion.

### Recurring tasks

Recurring tasks created in the Google Tasks app appear in Taskflow on whichever time-horizon screen their next-due date falls into (Today, Tomorrow, or Soon). They never appear on Later — only the next instance is shown. Their recurrence rule cannot be edited from inside Taskflow (see Edit a task). Completing a recurring task in Taskflow marks the current instance complete via the Google Tasks API, and the next instance will appear when the recurrence schedule says it should.

The user needs this because recurring tasks are real work that should show up alongside one-off tasks, but the recurrence rule itself is owned by Google Tasks and Taskflow must not pretend otherwise (UX principle 6).

