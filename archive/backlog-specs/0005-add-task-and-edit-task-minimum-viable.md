# Batch: Add task and edit task — minimum viable

**Goal.** [To be filled in during the next planning session.]

**Outputs.** [To be filled in during the next planning session.]

**Success criteria.** [To be filled in during the next planning session.]

Changes:

- Floating-action-button to add a task. Capture inherits context:
  - From Today / Tomorrow: date auto-set to today / tomorrow.
  - From Soon / Later: no forced date or Project; the task lands on the slot the user pressed + from. If no date is set, the task is parked on that slot as undated.
  - From a Project view: filed in that Project, undated, below the card.
- Edit dialogue: title (single line), notes, Project (editable, including unassigned), date displayed but read-only for this batch (date editing comes in the side-scrolling date picker batch).
- Tap-to-complete on a non-parent task. Completed tasks move to the Today completed tray. Basic version of the tray comes online here.
- Recurrence field: present (it's now a Taskflow design choice rather than a Google Tasks API constraint).
- Serves UX.md: *Add a new task*, *Edit a task* (partial), *Completed task tray on Today* (basic), *Move between Schedule and Project*.
