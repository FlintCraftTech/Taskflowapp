# c70816b — SPEC.md — Later-by-Project rewrite: system Unassigned Project replaces null projectId; Later becomes Project-grouped expand/collapse cards (overview + per-Project view removed, dreaming surface folded in); new §Create or delete a Project; Strategy doc owns Project order with tier-split reorder/delete (free direct, paid via Claude); side menu + UX principles 3/5 + tray/drag-target refs realigned [later-by-project-spec-edit]

This spec-edit reworks the right end of Taskflow's navigation spine into one "zoom by time" flow — Today, Tomorrow, Soon, Later, Strategy — by folding Projects into the Later slot. Far-future tasks are now shown grouped by Project as expand/collapse cards rather than by date; the standalone Projects overview page and the per-Project view are both gone, and the per-Project "dreaming surface" (a Project's undated tasks) moves into its Later card. Every task now always has a Project: the nullable/unassigned state is replaced by a system "Unassigned" Project, pinned to the bottom of Later and excluded from the Strategy doc, so nothing renders orphaned. Alex pulled this off the post-core shelf to build now (2026-06-22).

Two gaps in the batch surfaced during the rewrite and were resolved with Alex in-session:

Reordering Projects was orphaned — the old mechanism (dragging Projects in the side menu, which also reordered the Strategy doc) dies when Projects leave the side menu. Resolution: the Strategy doc becomes the single source of Project order. Reordering is a high-level strategic act, so it's tier-split — free users drag Project headings in the Strategy-doc editor; paid users change order only through discussion with Claude, with a long-press on a Later card showing a "Discuss high-level strategy with Claude" toast. Required-on-paid was chosen over a soft nudge because a nudge that still allows direct drag undercuts the "deserves a check-in" intent.

Project creation (and deletion) had no home — the just-built "+ New Project" lived on the now-deleted overview page. Resolution: creation moves into the task editor's Project picker (a "New Project" entry → a foregrounded name card), and deletion happens on Later by long-pressing a card and dragging it to a delete target (free), with a deleted Project's tasks reassigned to Unassigned so nothing is lost; on paid, deletion also routes through Claude via the same toast. This added two sections beyond the batch's named scope — §Edit a task and a new §Create or delete a Project — both approved before writing.

Moving a task between Projects was kept as an edit-the-Project-picker action rather than a destroy-and-rewrite, identical on free and paid: delete-and-rewrite would lose a task's subtasks, notes, and recurrence, and free users (no Claude) must retain a manual path.

Four cross-references outside the named sections still described the removed surfaces — UX principle 3, UX principle 5, the Completed-task tray, and the Drag-target icons — and were folded in as mechanical cleanups so SPEC stays internally consistent.

None of the new Project-lifecycle UI (creation picker, reorder, delete, the toasts, the paid Claude-mediated path) exists in the queued build batches, and the paid behaviour belongs in SYSTEM-PROMPT.md, which is locked here. That follow-on is captured as [project-lifecycle-later] for /plan to split and queue, so SPEC describes the target while the build is tracked separately.

**Files touched:**
- SPEC.md — the ten section edits above plus the four cross-ref cleanups.
- QUEUE.md — [later-by-project-spec-edit] batch removed (locked at /next); [project-lifecycle-later] capture added.
- _build.md — deleted at close.

**Routed to Captures:**
- [project-lifecycle-later] — Project lifecycle UI (create/reorder/delete) + SYSTEM-PROMPT.md follow-on.
- [queue-maint-later-by-project] — queue cleanup after the rewrite: rework the parked "Empty state copy and visuals" item for the Later-card world; refresh drifted SPEC line-refs in [tomorrow-no-date-label] and [disable-drawer-swipe-open].
