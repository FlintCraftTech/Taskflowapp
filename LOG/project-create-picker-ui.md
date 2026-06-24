# [HASH] — Wired Project creation into the editor's Project picker — "New Project" card creates and files the task; AppViewModel deleted (creation relocated to EditTaskViewModel) [project-create-picker-ui]

Project creation had been impossible since [later-by-project-screen] removed the Projects-overview "+ New Project" button: the editor's Project picker listed only Unassigned plus existing Projects, and the creation plumbing (`AppViewModel.createProject`) had no caller. This wires creation into the one place a task's Project is already chosen, per SPEC §Create or delete a Project — the picker gains a "New Project" entry that opens a name-only card; on confirm the Project is created (appended at max-sort+1, with its empty Strategy entry alongside) and selected for the edited task, so it files in on save.

Build decision (the batch left it open): rather than make `AppViewModel.createProject` return the new id and thread that view-model into the dialogue, creation was relocated into `EditTaskViewModel` as `createProjectAndSelect`. That view-model already held `projectRepository` and owned the `projectId` the new id must set, so creation now lives with its one caller and its one output instead of spanning two view-models. The relocation left `AppViewModel` with no members and no callers anywhere, so the file was deleted — dead-code removal, not a feature cut. The rejected alternative (return-id from AppViewModel) would have added a second view-model to the dialogue purely to host a one-line insert the editor could do itself.

Device verification surfaced a UX trap that is not a defect in this build: a Later Project card reads "nothing in this project yet" when the Project's only tasks are near-term dated, because those tasks live on their Schedule slot, not in the Later card (which holds only undated + far-future tasks). The create-and-file is correct — the editor confirms the task is filed — but the empty-card copy misleads. Filed to Captures for /plan.

**Files touched:**
- `EditTaskScreen.kt` — "New Project" dropdown entry (below a divider) + new `NewProjectCard` name-only dialog (Create disabled until non-blank); `onCreateProject` wired to the view-model; factory passes `app.strategyRepository`; `NEW_PROJECT` constant.
- `EditTaskViewModel.kt` — added `createProjectAndSelect` (inserts Project + empty Strategy entry, selects it on the form); `strategyRepository` added to constructor and factory; `projectRepository` now a held property.
- `AppViewModel.kt` — deleted (orphaned after creation relocated into the editor).

**Routed to Captures:** Later card "nothing in this project yet" misleads when the Project's only tasks are near-term dated.
