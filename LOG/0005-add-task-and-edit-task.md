# [HASH] — 0005 — add-task-and-edit-task-minimum-viable (batch 0005) — added task creation (a Scaffold FAB driving a capture-inherits-context add flow), a new edit dialogue (`ui/edit/` — EditTaskScreen + EditTaskViewModel: title, notes, editable Project incl. unassigned, read-only date), tap-to-complete on non-parent rows across both Schedule (SlotPage) and Project views, and a basic completed tray on Today; wired through ScheduleScreen/ScheduleViewModel, ProjectScreen/ProjectViewModel, and AppRoot. Recurrence deferred to 0007 (no recurrence column), date-editing to 0006, tray auto-clear to 0012.

0005 turns the render-only screens of 0002–0004 into a usable app: tasks can now be created, edited, completed, and seen in a completed tray. As with 0004, the archived spec was a stub (Goal/Outputs/Success unfilled, old "UX.md" vocabulary), so the build worked from SPEC.md as product truth, reconciling the archived "Changes" list against it.

The edit dialogue (new `ui/edit/`) is the spine of the batch — both the add flow and the edit flow run through it. A sealed `EditTarget` distinguishes an existing task from a new one, and for a new task resolves the inherited context (SPEC §Add): Today/Tomorrow set the date (anchored at noon, clear of the day-begins-at edge) and leave the slot derived; Soon/Later park the task undated on that slot; a Project view files it undated below the card. Title, notes, and Project (including "Unassigned") are editable; the date is shown read-only with a "set in a later update" hint, because date editing is the side-scrolling picker of 0006. Save inserts or updates and never writes the date this batch.

Recurrence was deferred to 0007. The archived spec listed "Recurrence field: present," but SPEC §Edit omits it and the Task entity has no recurrence column to persist one — so a field now would be dead UI, the same half-feature 0004 refused. A non-persisting placeholder field was weighed and rejected; recurrence is 0007's, which adds the data model. The user was offered the escape to add a placeholder anyway and didn't take it.

Completion got a uniform model: every non-parent row — Schedule slot, Project card, Project below-card — has a leading checkbox (toggles completion) plus a clickable row (opens the editor). This wires completion-from-card, which 0004's log had deferred to 0005. For un-completing, a completion toggle inside the edit dialogue was rejected — it would push the dialogue past SPEC §Edit's title/notes/Project/date; instead the tray row carries its own checked checkbox, so unchecking it there un-completes, uniform with active rows, and the dialogue stays exactly as SPEC describes.

The completed tray is the basic version on Today: it lists all completed top-level tasks (greyed, struck through) below the active list, with an empty state only when both the active list and the tray are empty. Auto-clear at the day-begins-at rollover is deferred to 0012 — there is no completedAt column to filter on and no day-begins-at setting yet — so for now the tray reflects whatever is completed.

FAB placement raised a spec-drift: the add FAB sits on the four Schedule slots and inside a Project view (the contexts SPEC §Add enumerates) but not on the Projects-overview page, where an added task would have no date, slot, or Project and be invisible. That makes SPEC §Add's "from any screen" literally broader than what shipped, so the /done spec-drift check filed a capture for /plan to tighten the wording or define add-from-overview.

Two smaller notes: new-task slot ordering is a best-effort append — the DAO's max-sort-order query is keyed on the stored slot column, so dated rows aren't counted, and precise dated ordering remains 0012's (per the existing ScheduleViewModel note). And the data layer needed no changes — TaskDao/TaskRepository already carried insert, updateCompletion, getCompletedTasks, and the max-sort-order helpers — so the batch was UI and view-model only. Compile is clean; the on-device runtime check is deferred to the Deferred tests section (and 0005 makes the 0002 and 0004 render checks device-runnable too). Within-list drag-reorder stays the unowned 0004 capture.

**Files touched:**
- app/src/main/java/com/example/taskflow/ui/edit/EditTaskViewModel.kt — created (~210 lines)
- app/src/main/java/com/example/taskflow/ui/edit/EditTaskScreen.kt — created (~210 lines)
- app/src/main/java/com/example/taskflow/ui/schedule/ScheduleViewModel.kt — edited (combine active/completed, setCompleted)
- app/src/main/java/com/example/taskflow/ui/schedule/SlotPage.kt — edited (row checkbox + clickable, Today completed tray)
- app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt — edited (onTaskClick, pass completion + tray to SlotPage)
- app/src/main/java/com/example/taskflow/ui/project/ProjectViewModel.kt — edited (setCompleted, stored taskRepository)
- app/src/main/java/com/example/taskflow/ui/project/ProjectScreen.kt — edited (onTaskClick, row checkbox + clickable on card + below-card)
- app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt — edited (editTarget state, FAB, EditTaskScreen host, onTaskClick wiring)
- (Destination.kt was in scope but untouched — EditTarget lives in the edit package.)

**Routed to Captures:** SPEC §Add "from any screen" vs no-FAB-on-Projects-overview (spec-edit for /plan).
