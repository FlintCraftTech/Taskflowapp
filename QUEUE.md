# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

(none surfaced yet)

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

The detailed original spec for each build batch is archived at `archive/backlog-specs/` — the filename matches the batch number/slug. These came from the old folder-based backlog; the entry below is the summary, the archived file is the full spec.

### Build

**Add-flow create-path fixes — Today-add date + stale New-task title** **[add-flow-create-path-fixes]**

Device verification on 2026-06-19 found two bugs in the 0005 add flow, both surfacing on the create path's first real on-device exercise. (1) Adding a task with the FAB on the Today slot stores tomorrow's date — the task lands on Tomorrow with a date label instead of on Today with none. Confirmed against the SQLite database, not just the on-screen label. (2) After saving a task, reopening the New-task form on the same slot shows the previous task's title still in the Title field.

Bug 1's cause is not the obvious one. The capture guessed the date logic or the slot-to-date mapping, but a full read of the chain — the FAB's slot read, the page-to-slot map, the add ViewModel, and the date math — shows every link computes a Today add as today's date correctly, and Soon/Later/Tomorrow all work on device. The defect is not visible statically, so this batch reproduces it on a device first, then traces and fixes it. Bug 2's cause is known: the add screen keys its form-holder by slot, so a second add on the same slot reuses the prior form; the fix is a fresh form per add. Notes and Project only appeared to reset because they were empty both times.

No SPEC change — both are deviations from what SPEC §Add a new task already specifies (a Today add is dated today and lands on Today; capture is the lightest input).

Build:
- Reproduce and fix the Today-slot add storing tomorrow's date, so a Today add stores today's date and lands on Today with no label. Investigate the FAB→slot→date chain to find the actual defect: `app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt`, `app/src/main/java/com/example/taskflow/ui/navigation/Destination.kt`, `app/src/main/java/com/example/taskflow/ui/edit/EditTaskViewModel.kt`, `app/src/main/java/com/example/taskflow/domain/SlotDeriver.kt`. If the traced cause is unit-testable, add a regression test under `app/src/test/java/com/example/taskflow/`.
- Reset the New-task form on each FAB open so the Title field opens blank: `app/src/main/java/com/example/taskflow/ui/edit/EditTaskScreen.kt` (the per-slot ViewModel key), and `app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt` if a per-open identifier is needed to force a fresh form.

Test:
- On a device: add a task from the Today slot; confirm it lands on Today with no date label and stores today's date. Re-confirm Tomorrow still stores tomorrow and Soon/Later still park undated, since the fix touches the shared chain. User-run.
- On a device: add and save a task, then tap the FAB again on the same slot; confirm the New-task form opens with an empty Title. User-run.

**Project creation — spec-edit** **[project-create-spec-edit]**
Blocks: [project-create]

SPEC assumes Projects can be created — §Projects overview's empty state says "when you make one, it shows up here and in the menu" — but no section describes the gesture, and the build has no creation path at all. This spec-edit adds the manual creation behaviour so the feature batch builds against a described surface. Creation is name-only: the user enters a Project name on the Projects overview and it is appended to the Projects list, appearing in the same position in the side menu and the Strategy doc. No description is asked at creation — the description is the Strategy-doc paragraph, authored later in the Strategy editor (0015) or with Claude on the paid tier — so requiring it here would duplicate that editor and slow the add (UX principle 5, lightest capture). A new Project's Strategy paragraph appears automatically under the existing mechanical-structure rule, so no §Strategy-doc change is needed. This batch also folds in the §Add "from any screen" spec-drift filed during 0005: the add FAB shipped only on the four Schedule slots and inside a Project view, not on the Projects overview, so §Add's "from any screen" wording is broader than what shipped — the same Projects-overview surface this batch touches, so the wording fix rides here rather than in its own pass.

Spec-edit:
- SPEC §Projects overview: add that the page provides a way to create a new Project. Creating one asks only for a name; the new Project is appended to the end of the Projects list and shows in the same order in the side menu and Strategy doc. State that no description is captured here — it is the Strategy-doc paragraph, written later.
- SPEC §Add a new task: the opening says the user adds a task "from any screen via a floating action button or equivalent affordance." Narrow "from any screen" to the real task-add surfaces the section already enumerates — the four Schedule slots and a Project view. Add that the Projects-overview page is not a task-add surface; its add affordance creates a Project (per the §Projects overview edit above).

**Create a Project (manual) — Projects overview** **[project-create]**
Depends on: [project-create-spec-edit]

There is no way to create a Project in the build: the Projects overview and side menu only list existing ones, no UI calls `projectRepository.insert`, and the device DB stays empty — so the Project view, the Strategy doc, and Project-assignment in the task editor all have nothing to work with, and the deferred Project device-verification can't run. This batch adds the manual path: an add affordance on the Projects overview that asks for a name and creates the Project at the end of the Projects sort order, creating its empty Strategy entry alongside so the Strategy-doc paragraph exists. Name-only, per the spec-edit; the description is authored later.

Build:
- `app/src/main/java/com/example/taskflow/ui/projects/ProjectsOverviewScreen.kt` — add an add affordance opening a small name-entry dialog (a new dialog composable under `ui/projects/` if it needs its own file).
- `app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt` and `app/src/main/java/com/example/taskflow/ui/navigation/AppViewModel.kt` — wire a `createProject(name)` action from the overview affordance.
- `app/src/main/java/com/example/taskflow/data/repository/ProjectRepository.kt`, `app/src/main/java/com/example/taskflow/data/repository/StrategyRepository.kt`, and `app/src/main/java/com/example/taskflow/data/local/ProjectDao.kt` — insert the Project at next sort-order (max + 1, adding a max-sort-order query if absent) and upsert its empty `StrategyEntry`.

Test:
- **Project creation.** On a device: the Projects-overview add affordance creates a Project from a typed name; it appears on the Projects overview and in the side menu in last position; it is selectable as the Project in a task's edit dialogue.
- **[0004] Project view rendering.** Opening the created Project: the screen shows; the "Scheduled" card is collapsed by default and toggles on tap; the below-card undated list renders in per-Project order; the three empty states render (nothing at all; card + "no unscheduled tasks" line; list with no card). Confirmed by: populating the created Project through the FAB and editor.
- **[0005] Project add / refile.** The add FAB inside a Project view files an undated task below the card; saving a Project change in a task's editor refiles it to the chosen Project; completing a task from a Project surface sends it to the Today tray. Confirmed by: exercising the Project flows against the created Project.

**Spine header polish — title-slide overlap + Material chevron arrows** **[spine-header-polish]**

Two issues in the Schedule spine header (`ScheduleScreen.kt`), both seen on-device verifying 0003 and both predating it. (1) Moving backward through the spine (e.g. Soon → Tomorrow), the outgoing page name doesn't clear before the incoming arrives, so the two titles overlap mid-transition. (2) The ←/→ arrows are plain text glyphs — small and baseline-aligned, so they sit low; they should be Material chevrons, vertically centred with the title. No spec-edit; both are below SPEC's altitude.

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt` — stagger or fade the `AnimatedSpineTitle` / `AnimatedContent` transition so the outgoing title clears before the incoming arrives; replace the text-glyph `Chevron` composable with a Material chevron icon (`KeyboardArrowLeft`/`Right`), vertically centred with the title.
- `app/build.gradle.kts` — add the Material icons dependency only if the chosen glyph isn't in icons-core (KeyboardArrowLeft/Right are; the extended pack only if a different chevron is wanted).

Test:
- On a device: navigate backward through the spine (Soon → Tomorrow → Today) and confirm the titles no longer overlap; confirm the arrows render as centred Material chevrons. User-run.

- **0006 — side-scrolling-date-picker** — Horizontal date strip replacing read-only date display in edit dialogue.
- **0007 — recurring-tasks** — Recurrence rules and 30-day-capped instance rendering.
**Within-list task reorder by drag — Schedule slots + Project below-card** **[task-reorder-within-list]**
Depends on: [project-create]

SPEC §Reorder within a Schedule slot and §Project view both specify within-list drag-reorder of top-level tasks, but no batch builds it — 0008 is cross-slot reschedule, 0010 is subtask/outliner drag, 0011 is cut-and-paste. The data layer already persists order (`TaskDao.updateSlotSortOrder` / `updateProjectSortOrder` / `getMax…SortOrder`, DAO-tested in 0001); what's missing is the drag UI on the two lists. This batch adds within-list drag-to-reorder to the Schedule slot list and the Project below-card list, persisting via the existing DAO methods. No spec-edit — SPEC already describes it. (Depends on [project-create] only so the Project-list reorder can be tested against a real Project; the Schedule-list half is independent.)

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/SlotPage.kt` and `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleViewModel.kt` — drag-to-reorder on the slot's task list, persisted via `updateSlotSortOrder`.
- `app/src/main/java/com/example/taskflow/ui/project/ProjectScreen.kt` and `app/src/main/java/com/example/taskflow/ui/project/ProjectViewModel.kt` — drag-to-reorder on the below-card undated list, persisted via `updateProjectSortOrder`.
- `app/src/main/java/com/example/taskflow/data/repository/TaskRepository.kt` — expose the reorder calls if not already surfaced.

Test:
- On a device: drag a task within a Schedule slot to a new position and confirm the order persists across navigation/relaunch; drag a task within a created Project's below-card list and confirm the per-Project order persists. User-run.

- **0008 — drag-task-between-schedule-screens** — Long-press drag to reschedule across Schedule slots.
- **0009 — subtasks-under-parent-expand-collapse** — Nested subtasks with parent expand/collapse and completion roll-up.
- **0010 — outliner-typing-drag-target-icons** — Outliner editor for subtasks plus bin and promote drag targets.
- **0011 — cut-and-paste-os-clipboard** — Cut drag target and OS-clipboard paste in edit dialogue.
- **0012 — settings-day-begins-at** — Day-boundary time picker wired into rollover and date anchor.
- **0013 — settings-date-format** — DD/MM or MM/DD setting applied app-wide.
- **0014 — json-export-and-import** — Full database export/import via JSON files.
- **0015 — strategy-doc-and-life-area-context** — Strategy doc editor, mechanical structure, life-area Room schema.
- **0016 — onboarding-flow** — First-run cards, AI-value video, free/paid choice.
- **0017 — tier-model-and-subscription-handling** — Google Play subscription, trial, local tier enforcement.
- **0018 — cloud-sync-paid-tier** — Push-pull sync between device Room DB and cloud backend.
- **0019 — ai-choice-flow-and-mcp-setup** — Claude setup path with connector deep-link and verification.
- **0020 — remote-mcp-server** — Hosted MCP server with tool surface and system-prompt delivery.
**SYSTEM-PROMPT.md — reconciliation: pending-suggestion supersession** **[sysprompt-reconciliation-supersession]**
Serves SYSTEM-PROMPT.md.

`SYSTEM-PROMPT.md` describes Strategy-doc reconciliation but doesn't say what happens when the user submits a new Strategy edit while a prior reconciliation's suggestions are still unanswered. Decided in planning 2026-06-16: the new edit wins. This batch writes that rule into the doc so the reconciliation feature (batch 0021) is built against a complete description.

Build:
- Edit `SYSTEM-PROMPT.md` → §Strategy doc reconciliation → *Ongoing reconciliation*: add that a new Strategy doc edit submitted while prior suggestions are still pending triggers fresh reconciliation against the latest version, superseding (folding in) the prior pass's unanswered suggestions rather than stacking them. Keep one line of rationale in the doc: stale suggestions against a superseded version confuse; newest text is the source of truth.

- **0021 — strategy-doc-reconciliation-paid-tier** — Initial and ongoing Strategy doc reconciliation via Claude.
- **0022 — help-thanks-report-a-bug-content** — Bottom-of-drawer screen content including help and custom instructions.

### Parked

## Deferred tests

Planned tests that couldn't run in their own session. /plan rolls the runnable ones into a test batch.

- **[device-verify-core-screens → 0002] Schedule date-matrix rendering.** On a device, verify dated tasks render with their DD/MM label in the right slot for the cases the FAB can't seed yet: a 2–7-day date in Soon, an 8+-day date in Later, and a past date staying on Today (DD/MM, no overdue label). Confirmed by: user-run on device once 0006 ships date-editing — the only path to those dates. (Slot math already unit-tested in 0002; the non-Today DD/MM render is exercised by the Tomorrow check in [device-verify-core-screens].)

## Captures

Captured outside /plan. Picked up and routed during the next /plan session.

---

(Raw captures collect below this line, then get processed and moved above it during /plan.)

### Parked

- **Schedule bucketing doesn't re-evaluate across the day boundary while the app stays open.** The Schedule view computes "now" each time the task flow re-emits — a DB change, or re-subscription when the app returns to foreground. If the app sits continuously in the foreground across the day-begins-at boundary (e.g. 4 AM) with no edits, a task won't move from Tomorrow into Today until the next emission or recomposition. It's an edge case (foreground-only app, and returning to the app re-subscribes and recomputes). Best handled when 0012 wires day-begins-at: add a boundary/lifecycle-resume tick that recomputes placement. Discovered building 0002.
  Blocked by: 0012 (settings-day-begins-at) — landing; fold the refresh tick into the day-begins-at wiring.
- **Subscription pause — Play Billing pause mechanics.** Verify what Google Play Billing actually exposes for subscription pause (largely Play-Store-controlled, not app-controlled). Staleness-prone, so check at build time, not now.
  Blocked by: the subscription-handling build (queue entry 0017) — behavioural trigger, no slug yet (0017 is still a rough placeholder); confirm the Play Billing pause API when that batch is authored.
- **Subscription pause — multi-device resume merge.** Policy is decided (paused → revert to local-only). Open: when a paused multi-device user resumes, which device's local state wins / how to merge. Entangled with cloud-sync design.
  Blocked by: the cloud-sync build (queue entry 0018) — behavioural trigger, no slug yet; decide the resume-merge strategy when that batch is designed.
- **Custom instruction text — production version.** The suggested proactive-use custom-instruction text (for users' Claude preferences) needs testing in Alex's own real use before publishing. Once tested, document in Help (0022) and fold into SYSTEM-PROMPT.md if relevant.
  Blocked by: Alex testing the proactive custom-instruction in her own Claude use — behavioural trigger, no slug; needs the app usable first.
- **Empty state copy and visuals.** Write copy + visuals for each empty state: Schedule screens with zero tasks, an empty Project, and the Projects list before any Project exists (the empty-Projects state does pedagogical work per SPEC). Best written in front of the real screen, not against screens that don't exist yet.
  Blocked by: the screen-rendering builds reaching their polish pass (0002 Schedule, 0003 side menu / Projects, 0004 Project view) — behavioural, no slug; write each empty state against the real screen.
- **Help, Thanks, Report a bug content.** Content for the three bottom-of-drawer screens (batch 0022). Help should cover MCP setup, the production custom-instruction text, and the "tasks dated before today" behaviour described without naming the category (the SPEC §Tasks dated before today wording is ready).
  Blocked by: the production custom-instruction text (itself parked, behavioural) and the MCP setup design (0019/0020) — write Help content when 0022 is built and those inputs are ready.
- **Search.** Confirmed feature, deferred. There IS a search box — finding tasks gets overwhelming at volume. Design decided: search results display like the in-Project/category listing — task details are visible but a task CANNOT be marked complete from the results list (read-only display). Tapping a result navigates to where the task actually lives, so the user can complete or edit it there. Still to pin at build time: search scope (current screen / current Project / whole database — tasks + Projects + Strategy doc).
  Parked: confirmed for a later build, no batch yet; revisit when scoping post-core work. When unparked it needs a spec-edit (add §Search to SPEC) plus a feature batch.
- **Completed-history sub-system (left half of the navigation spine)** **[nav-completed-history]**
  Parked: post-core design thread — the left end of the navigation spine. The right half (spine backbone, Projects/Strategy pages, side-menu mirror) was promoted to [nav-spine-spec-edit] in /plan 2026-06-17; this is the remainder. Trigger-less — revisit when taking up post-core work. Was [nav-zoom-spine-and-completed-history] before that split. Grew out of [completed-task-post-tray-fate] during /plan 2026-06-17 (evidence citation, not a dependency).

  **What it covers.** The pages left of Today on the spine: a **Search / completed-history page** (leftmost), a **Yesterday page**, a **day-detail card layer**, and **share-a-day**.

  **Completed-history (the Search page).** The leftmost page lists completed tasks in completion order, most recent at top, with date headers between days. Typing a search narrows what shows below; the relevant date headers still display above each day's results.

  **Yesterday page.** A spine page, not a card. Entangled with completed-history: since past-due tasks stay on Today (principle 4), Yesterday's content is essentially what was completed yesterday — so its design belongs with this sub-system, not the schedule screens.

  **Day-detail card layer (a separate axis).** Tapping a result, a group, or a date header opens that day on a **card in the foreground** — deliberately a *different* left-right axis from the spine, signalled by the card visual. Swipe right = previous (older) day slides in from the left; swipe left = next (newer) day slides in from the right. Swiping left past the newest card (day-before-yesterday) slides the whole card layer *and* the Search page off in one smooth motion, returning the user to the main spine, landing on **Yesterday**. Only from a day card can the user tap a single task to edit or uncomplete it.

  **Share-a-day.** A share button on a day screen shares that single day's completed tasks. Format open — leaning PNG or PDF (PNG seen as most universally readable and still printable); plain-text and app-sensitive output also considered. Reconcile with the existing Strategy-doc share button, which uses the Android share sheet.

  **SPEC consequences when developed.** Adds new sections for the completed-history page, the Yesterday page, the day-card layer, and share-a-day, and extends the spine described by [nav-spine-spec-edit] leftward (Search · Yesterday, left of Today). The right-half SPEC rewrite is handled by [nav-spine-spec-edit], which also absorbs the held spec-trim findings F3/F4/F18 (they live in the Schedule-view and side-menu sections it rewrites). This left-half item adds only the new completed-history sections; it does not touch those findings.

  **Open sub-questions (resolve before this can promote):**
  1. Reconcile with the parked Search feature (find *active* tasks, read-only results, tap to jump to where the task lives). Is the leftmost page completed-history only, with active-task search separate/later — or one unified search covering both?
  2. Share format (PNG / PDF / plain text / app-sensitive) — research-worthy before deciding.
  3. Swipe and card-layer interaction detail — best finalized against a real screen.
- **Onboarding video content.** The multi-page onboarding video must demonstrate Claude/MCP value concretely (for the free-vs-paid choice). Can't be produced until the Claude/MCP integration exists to film. Sequencing note: onboarding (0016) is queued ahead of the MCP work, so 0016 may need to ship with a placeholder video and have the real one added once the integration lands.
  Blocked by: the Claude/MCP integration builds (0019 AI-choice / MCP setup, 0020 remote MCP server, 0021 reconciliation) — behavioural, no slug; produce the video once there's a working integration to demonstrate.
- **Post-first-test polish review.** After the first end-to-end test, walk the test notes and decide which polish issues warrant their own SPEC.md entry and which fold into existing ones — polish that doesn't trace to SPEC.md is a capture, not a build item.
  Blocked by: the first end-to-end test having happened — behavioural, no slug; when it lands, run a /plan polish-review pass over the test notes.
