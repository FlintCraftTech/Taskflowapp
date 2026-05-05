# BACKLOG.md — Taskflow Deferred Work

All deferred work in one place. Three sections, in this order; top section first, top item first.

## Red flags

(Empty — Claude will populate this section during sessions if security concerns are surfaced and the user defers them. See `CLAUDE.md` § "Red flags — screen and surface".)

## Planning batches

Open questions that must be resolved before some build batch can run. Each planning batch lists the questions to answer and which build batch it blocks. Once resolved, fold answers into the relevant `UX.md` entries and remove the planning batch.

### Planning batch: Auth and accounts

- Single Google account only, or multi-account switcher?
- What happens on sign-out — does the app retain anything (cached data, settings), or fully reset?

Blocks: Build batch — Auth + read-only Today screen.

### Planning batch: Day begins at default

- What time does the app ship with as the default day boundary? (12:00 AM is the obvious default; 4:00 AM is more forgiving for night owls.)

Blocks: Build batch — Settings → Day begins at.

### Planning batch: Date picker design

Five design questions for UX.md *Date picker — side-scrolling date strip*:

- **Anchor and initial position.** When the dialogue opens for a dated task, does the strip start scrolled to the task's current date (so it's centered/visible)? When opened for an undated task, does it start at today?
- **Scroll range.** How far back into the past does the strip allow scrolling (for setting overdue dates, or for inspecting the past)? How far into the future? Is there a fast-forward affordance for jumping by months, or is everything reached by horizontal scroll?
- **Fade gradient.** Linear with distance? Step changes at screen boundaries (today vs Tomorrow's date, end of Soon range, end of Later range)? At what date does the tile become so grey it's barely readable?
- **Clearing the date.** How does the user un-set a date to make a task undated? A "no date" tile at the far left/right of the strip? A separate clear button next to the strip? A long-press on the selected tile?
- **Visible tile count.** How many date tiles are visible at once on a typical phone screen?

Blocks: Build batch — Side-scrolling date picker.

### Planning batch: Drag-to-Later edge case for parked tasks

- For a parked-on-Soon task whose original date was further out than today+8 (e.g., today+15), drag-to-Later currently pulls the date IN to today+8 (clearing parking and snapping). The trade-off is that a user who parks then unparks loses their original Later date. Confirm acceptable, or specify alternative behaviour.

Blocks: Build batch — Drag a task between screens to reschedule.

### Planning batch: Completed tray retention

- How long do completed tasks stay in the Today tray? Forever, until midnight, until N days have passed, or until the user manually clears them?

Blocks: Build batch — Add task and edit task (basic completed tray behaviour ships in that batch but retention rule is needed for the long-term behaviour).

### Planning batch: Overdue visual treatment

- What does an overdue task look like on Today? (Red date, red dot, prefix label, position above today's tasks?)

Blocks: Build batch — Auth + read-only Today screen (overdue tasks are rendered in this batch — a placeholder treatment can be used initially but the final visual needs deciding before polish).

### Planning batch: Empty state copy and visuals

- What does each screen look like with zero tasks? (Today is likely the most common empty state.)

Blocks: Polish of every screen-rendering build batch.

### Planning batch: Offline behaviour

- The Google Tasks API requires a connection. What does Taskflow show when offline — last-fetched cached state, an empty state with a banner, or nothing?

Blocks: General concern across all build batches; not specifically blocking until offline scenarios are tested.

### Planning batch: Help / Thanks / Report a bug content

- What lives behind each of the three remaining bottom-of-drawer entries? (Settings is now defined for Day begins at, but more settings may want to live there.)

Blocks: Polish of the Hamburger menu build batch.

### Planning batch: Notifications

- Does Taskflow surface any notifications (e.g., morning summary of Today), or is it purely a foreground app for v1?

Blocks: Nothing currently — there is no notifications build batch yet. Resolve to decide whether to add one.

### Planning batch: Search

- Is there a search box? If so, does it scope to the active list or search across all lists?

Blocks: Nothing currently — there is no search build batch yet. Resolve to decide whether to add one.

## Build batches

Engineering work. The top batch is the next build (after any one currently in progress). Each batch must be small enough to build and test in one sitting.

### Batch: Auth + read-only Today screen

The smallest possible thing that proves the Google Tasks API integration works end-to-end. No edit, no drag, no other screens.

- Set up Android Studio project, dependencies, and Google Sign-In with Tasks API scope.
- Implement Google Tasks API client (read-only for this batch): list user's task lists, list tasks in a list.
- Today screen: show every task in the user's default task list whose due date is today or earlier (overdue rolls in). Overdue tasks visually marked. Read-only — no checkbox interaction yet.
- Render overdue tasks with DD/MM date inline on the right; tasks dated today (non-overdue) show no date label. Per UX.md *Date display*.
- Empty state for zero tasks on Today.
- Serves UX.md: *Four time-horizon screens*, *Today screen*, *Date display*.

### Batch: Tomorrow, Soon, Later screens + navigation

- Add Tomorrow, Soon (2–7 days), Later (>7 days) screens, all read-only.
- Horizontal swipe navigation between the four screens (phone-home-screen-style paging).
- Soon and Later: tasks listed in date order; each task shows DD/MM inline.
- Tomorrow: each task shows DD/MM inline (per UX.md *Date display*).
- Recurring tasks: show next-due instance on Today/Tomorrow/Soon based on the next-due date returned by the API; do NOT show recurring tasks on Later.
- Empty states for each screen.
- Serves UX.md: *Tomorrow screen*, *Soon screen*, *Later screen*, *Date display*, *Recurring tasks* (display only).

### Batch: Hamburger menu

- Side drawer with two sections: lists at top, app actions pinned at the bottom.
- Top section: all of the user's Google Tasks lists, in their Google Tasks order.
- Tapping a list makes it the active list; all four screens repopulate from the new list.
- Active list name shown in the app bar.
- Bottom section: Settings, Help, Thanks, Report a bug — each rendered as a row that opens a placeholder screen for now (Settings is filled in by the next batch; Help/Thanks/Report a bug content defined in a later UX update).
- Serves UX.md: *Hamburger menu*.

### Batch: Settings → Day begins at

- Settings screen reachable from the hamburger drawer.
- Single time picker labelled "Day begins at." Stored locally on the device (no Google Tasks equivalent).
- Wire the chosen time into the day-rollover logic that decides which screen a dated task appears on (already implicitly using midnight from earlier batches; this batch makes it user-configurable).
- Serves UX.md: *Settings → Day begins at*.

### Batch: Onboarding import transforms

- Detect first-run / first-import.
- For undated parent tasks: place on Later, undated.
- For subtasks: strip any independent due date. Apply parent-inheritance rule (only-one-dated-child, all-dated-children-agree).
- For parents with conflicting child dates: parent inherits the soonest of the children's dates (whole task lives on that date's screen).
- One-time popup after import: explanation copy per UX.md (covers both undated-to-Later and subtask-date-stripping with parent inheritance).
- Serves UX.md: *Onboarding: imported tasks without dates go to Later*, *Subtasks live under their parent*.

### Batch: Add task and edit task (no date editing yet, no outliner subtasks, no recurrence editing)

- Floating-action-button to add a task. New task gets the active screen's date and goes into the active list.
- Edit dialogue: title (single line), notes, due date displayed but read-only (date editing comes in next batch), list. Recurrence field shown but greyed out, with the toast on tap.
- Tap-to-complete on a non-parent task. Completing a task is the only way completed tasks appear, so the Today completed tray comes online here too — basic version.
- Serves UX.md: *Add a new task*, *Edit a task* (partial), *Recurring tasks* (greyed-out edit), *Completed task tray on Today* (basic).

### Batch: Side-scrolling date picker

- Replace the read-only date display in the edit dialogue with the side-scrolling date strip.
- Tile rendering with DD/MM, fade-to-grey gradient with distance from today, today as visual anchor.
- Tap a tile to select it as the task's due date; visual highlight on the selected tile.
- Initial scroll position when dialogue opens: scrolled to the task's current date (or today for undated).
- Clear-date affordance per resolved planning batch.
- Serves UX.md: *Date picker — side-scrolling date strip*, *Edit a task* (date editing portion).

### Batch: Drag a task between screens to reschedule

- Long-press to pick up a task (home-screen-icon style).
- Drag to the left/right edge of the screen to swipe through to the adjacent time-horizon screen.
- For dated tasks: Drop on Today/Tomorrow → set date to today/tomorrow. Drop on Soon from Today/Tomorrow → set date to today+2. Drop on Soon from Later → keep date, park on Soon (local override). Drop on Later → set date to today+8 (clear any parking), preserving any time-of-day data on the underlying record. Show toast: "task postponed until [DD/MM]".
- For undated tasks: Drop on Today/Tomorrow → prompt for date (cannot be undated on Today/Tomorrow). Drop on Soon → stay undated, parked on Soon (local override). Drop on Later → no-op (clear any prior parking).
- Local parking state stored device-side; does not sync to Google Tasks; does not survive uninstall.
- Soon screen renders parked tasks inline by their date (no special grouping or visual marker).
- Reorder within a day using the Google Tasks `position` field.
- Verify Google Tasks API behaviour for updating only the date portion of the `due` field (preserving any time component).
- Serves UX.md: *Drag a task between screens to change its date*, *Soon screen*, *Tasks can be reordered within a day*.

### Batch: Subtasks under parent + parent expand/collapse

- Render subtasks nested under their parent on the parent's time-horizon screen (no independent placement).
- Parent tasks: expand/collapse control instead of a checkbox; tapping reveals/hides children inline beneath.
- Completion roll-up: completing all subtasks completes the parent; un-completing a subtask un-completes the parent and brings it back out of the Completed tray.
- When a parent is dragged between screens, its children move with it as a single unit (extends the previous batch's drag behaviour).
- Serves UX.md: *Subtasks live under their parent*, *Parent tasks expand/collapse instead of having a checkbox*.

### Batch: Outliner-style typing + drag-target icons (bin and promote)

- Edit dialogue text area renders parent + children as an indented outline.
- Enter at end of any line creates a new child line below it.
- Backspace at start of any line deletes the line and merges remaining text into the previous line (parent or sibling).
- Drag handles on each child line (parent line has no handle).
- On any task drag (from screen or from edit dialogue), reveal a row of drag-target icons fixed at the **top right** of the screen, with hover feedback (icon scales up / gains shadow when hit).
- Drag-target icons in this batch: **bin** (delete the task) on screen and dialogue drags, **promote** (subtask drag in dialogue only — promotes child to top-level task at the bottom of the parent's date group on the parent's screen, with the parent's date).
- If the parent ends up with zero children after a promotion, it reverts from expand/collapse to a checkbox.
- Serves UX.md: *Edit dialogue: outliner-style typing for subtasks*, *Drag-target icons* (bin + promote portion).

### Batch: Cut and paste (OS clipboard)

- Add **cut** icon to the drag-target icon row (both screen drags and dialogue drags).
- Drop on cut → remove task from Taskflow and write its content to the device's OS clipboard as plain text. When cutting a parent, the whole set goes as indented multi-line plain text (parent on first line, children indented).
- Paste uses the device's normal long-press context menu, **only inside an edit dialogue text field** (no paste affordance on time-horizon screens).
- Outliner editor must handle multi-line indented text on paste — interpret line breaks as new lines, indentation as hierarchy.
- Cut-clipboard-loss risk explicitly accepted in UX.md (no recently-cut buffer).
- Serves UX.md: *Drag-target icons* (cut and paste portion).

### Batch: Polish pass

This batch is intentionally vague — populate it after the first end-to-end test session reveals what's rough. Likely contents: empty-state copy, overdue visual treatment refinement, drag affordance feedback, error/offline banners, completed-tray retention rule.
