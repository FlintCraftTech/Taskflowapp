# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

(none surfaced yet)

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

The detailed original spec for each build batch is archived at `archive/backlog-specs/` — the filename matches the batch number/slug. These came from the old folder-based backlog; the entry below is the summary, the archived file is the full spec.

### Build

**Later-by-Project screen — Project-grouped Later; remove Project view + overview** **[later-by-project-screen]**
Depends on: [unassigned-project-model]

The visible half of the redesign. Later stops showing far-future tasks by date and becomes a list of expand/collapse Project cards: one card per Project (every Project shown, even empty), the system Unassigned card pinned at the bottom. Each card holds that Project's far-future dated tasks (DD/MM kept) and its undated tasks, reorderable within the card. The two surfaces it replaces are deleted — the per-Project view (`ui/project/`) and the Projects overview page (`ui/projects/`) — and the side menu drops its per-Project list. Reaching a Project now means opening Later and expanding its card; there is no separate Project surface, which is what removes the "tapping a Project whisks you off the spine" feel. Depends on the data-model batch so every task already has a Project to group under.

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/SlotPage.kt` and `ScheduleViewModel.kt` — render the Later slot as Project-grouped expand/collapse cards, ordered to match the Strategy doc with Unassigned pinned bottom (empty Projects shown as empty cards); each card lists the Project's far-future dated + undated tasks; within-card drag-reorder persisted via the existing `updateProjectSortOrder`. Today/Tomorrow/Soon rendering unchanged. (A dedicated Later composable in `ui/schedule/` is fine if cleaner than branching SlotPage.) Soft anti-flood detail, pin against the real screen: a large Project's card can expand a chunk at a time (~7 tasks) rather than all at once — not load-bearing, decide on device.
- `app/src/main/java/com/example/taskflow/ui/project/ProjectScreen.kt` + `ProjectViewModel.kt` — delete; the per-Project view is gone.
- `app/src/main/java/com/example/taskflow/ui/projects/ProjectsOverviewScreen.kt` — delete; the overview page is gone.
- `app/src/main/java/com/example/taskflow/ui/navigation/AppViewModel.kt` — remove the Projects-overview spine page and per-Project overlay navigation; drop the projects flow that fed the drawer's per-Project list (Later's grouping is driven from the schedule layer).
- `app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt` — remove the Projects overview page from the spine pager and the Project-overlay host.
- `app/src/main/java/com/example/taskflow/ui/navigation/AppDrawer.kt` — drawer lists Today · Tomorrow · Soon · Later · Strategy plus app-actions; remove the per-Project entries and the empty-Projects teaching blurb.
- `app/src/main/java/com/example/taskflow/ui/navigation/Destination.kt` — remove the Project and Projects-overview destinations.
- `app/src/main/java/com/example/taskflow/ui/edit/EditTaskScreen.kt` + `EditTaskViewModel.kt` — the Project picker is the move-between-Projects path; ensure it lists user Projects and can set Unassigned, and confirm an undated task can be produced by clearing the date (the Q2b flag) — close the gap if the add flow can't yet.
- `app/src/main/java/com/example/taskflow/ui/common/PlaceholderScreen.kt` — remove if left unused after the overlay deletions; otherwise leave.

Test:
- User-run on device: Later shows every Project as an expand/collapse card with Unassigned pinned bottom; empty Projects appear as empty cards; a card holds its far-future dated tasks (DD/MM) and undated tasks; reordering within a card persists across relaunch.
- User-run on device: the Projects overview page is gone from the spine; no per-Project view opens; the side menu lists only Today · Tomorrow · Soon · Later · Strategy.
- User-run on device: moving a task to another Project via the editor refiles it and it shows under the new Project's card; an undated task created via the add flow (date cleared) lands in its Project's Later card.
- Claude-runnable: the project builds with no dangling references to the deleted Project view / overview screens.

**Spine header polish — title-slide overlap + Material chevron arrows** **[spine-header-polish]**

Two issues in the Schedule spine header (`ScheduleScreen.kt`), both seen on-device verifying 0003 and both predating it. (1) Moving backward through the spine (e.g. Soon → Tomorrow), the outgoing page name doesn't clear before the incoming arrives, so the two titles overlap mid-transition. (2) The ←/→ arrows are plain text glyphs — small and baseline-aligned, so they sit low; they should be Material chevrons, vertically centred with the title. No spec-edit; both are below SPEC's altitude.

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt` — stagger or fade the `AnimatedSpineTitle` / `AnimatedContent` transition so the outgoing title clears before the incoming arrives; replace the text-glyph `Chevron` composable with a Material chevron icon (`KeyboardArrowLeft`/`Right`), vertically centred with the title.
- `app/build.gradle.kts` — add the Material icons dependency only if the chosen glyph isn't in icons-core (KeyboardArrowLeft/Right are; the extended pack only if a different chevron is wanted).

Test:
- On a device: navigate backward through the spine (Soon → Tomorrow → Today) and confirm the titles no longer overlap; confirm the arrows render as centred Material chevrons. User-run.

**SPEC polish edits — Tomorrow date label + side-menu opener** **[polish-spec-edits]**
Blocks: [tomorrow-no-date-label], [disable-drawer-swipe-open]

Two polish batches below each folded a one-sentence SPEC change into a feature build. The method keeps SPEC edits out of feature builds, so both spec sentences are pulled out here into one spec-edit batch — combined rather than split per-feature to keep the extra ceremony to a single session. Each feature batch now depends on this one. No code here; SPEC only.

Spec-edit:
- SPEC §Schedule view: rewrite the date-label sentence so only Soon and Later show the DD/MM date. State that Tomorrow no longer shows a date label — the page name is the day signal. Leave Today's rule (label only when the date is in the past) unchanged. (Serves [tomorrow-no-date-label].)
- SPEC §Side menu: change "opens from the left edge" so it states the menu opens by tapping the ☰ button in the top bar (still sliding in from the left as a drawer). Record that swipe-to-open is intentionally disabled — one opener, the ☰ — so the gesture doesn't collide with the spine's horizontal-swipe navigation. (Serves [disable-drawer-swipe-open].)

**Drop the date label from Tomorrow** **[tomorrow-no-date-label]**
Depends on: [polish-spec-edits]

Tomorrow rows currently show their DD/MM date (SPEC §Schedule view), same as Soon and Later. But a Tomorrow task always carries exactly tomorrow's date — the slot is derived from the date, so nothing else can land there — which makes the label fully redundant with the page title. Tomorrow also has no past-date case the way Today does: a past-dated task falls onto Today, never Tomorrow, so dropping the label loses no stale-date signal. The date earns its place only on Soon (2–7 days) and Later (8+ days), where the page name doesn't tell you the actual day. Today keeps its existing rule unchanged — no label except when the date has slipped into the past. This is a design change, not a bug; the current Tomorrow labels follow the spec as written. Raised during the [add-flow-create-path-fixes] device check on 2026-06-19.

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleViewModel.kt` — in `dateLabelFor`, return no label for the Tomorrow slot (Tomorrow always holds tomorrow's date, so there's nothing to show). Update the KDoc above the function that currently reads "Tomorrow/Soon/Later rows show their date."

Test:
- On a device: a task on Tomorrow shows no date label; Soon and Later tasks still show their DD/MM date; a past-dated task on Today still shows its date. User-run.

**Open the side menu by ☰ only — disable drawer swipe-to-open** **[disable-drawer-swipe-open]**
Depends on: [polish-spec-edits]

The navigation drawer's default left-edge swipe-to-open collides with Taskflow's signature gesture: horizontal swipe is how the whole spine moves (Today ↔ Tomorrow ↔ Soon ↔ Later and onward). On Today — the leftmost, default page — a right-swipe has no previous spine page, so the drawer quietly claims it, and the same horizontal gesture means "open menu" near the edge but "change day" in the content area. That region-dependent meaning is the confusion. Resolution: the menu opens only by tapping the ☰ button; swipe-to-open is disabled. The Android edge-swipe-to-open convention was weighed and set aside — it carries less weight in an app that repurposes horizontal swipe as its core navigation, and the ☰ remains a standard, discoverable opener, so no affordance is truly lost. Verified in AppRoot.kt: the ☰ opens the drawer programmatically (`drawerState.open()`), unaffected by the gesture flag, and the spine's `HorizontalPager` is a separate gesture, also unaffected. One known side effect: disabling drawer gestures also removes swipe-to-close, but tapping the scrim or any menu item still closes it. Noticed on device 2026-06-20.

Build:
- `app/src/main/java/com/example/taskflow/ui/navigation/AppRoot.kt` — add `gesturesEnabled = false` to the `ModalNavigationDrawer`. The ☰ (`onMenuClick` → `drawerState.open()`) and scrim/item taps to close are unaffected; the `HorizontalPager` day-swipe is unaffected.

Test:
- On a device: a left-edge right-swipe no longer opens the menu; the ☰ button still opens it; horizontal swipe in the content area still changes the day and the chevrons still work; tapping the scrim or a menu item still closes the drawer. User-run.

**Verify the blank New-task form fix** **[verify-blank-new-task-form]**

Rolled from Deferred tests. The [add-flow-create-path-fixes] build fixed the stale New-task title by giving the add dialogue a fresh view-model store per open. This is the device check that confirms it on the installed build — it couldn't run in the build's own session, so it was deferred.

Test:
- On a device with the current build installed: add and save a task on a Schedule slot, then tap the FAB again on the same slot; confirm the New-task form opens with an empty Title field. Verifies the stale-title fix from [add-flow-create-path-fixes]. User-run.

- **0006 — side-scrolling-date-picker** — Horizontal date strip replacing read-only date display in edit dialogue.
- **0007 — recurring-tasks** — Recurrence rules and 30-day-capped instance rendering.
**Within-list task reorder by drag — Schedule slots** **[task-reorder-within-list]**

SPEC §Reorder within a Schedule slot specifies within-list drag-reorder of top-level tasks, but no batch builds it — 0008 is cross-slot reschedule, 0010 is subtask/outliner drag, 0011 is cut-and-paste. The data layer already persists order (`TaskDao.updateSlotSortOrder` / `getMaxSlotSortOrder`, DAO-tested in 0001); what's missing is the drag UI. This batch adds within-list drag-to-reorder to the Schedule slot task list (Today/Tomorrow/Soon), persisting via the existing DAO method. No spec-edit — SPEC already describes it. (Rescoped 2026-06-22: the Project-list reorder half moved into [later-by-project-screen], which builds within-card reorder on Later; the per-Project view this batch originally targeted no longer exists, so Later reorder is out of scope here.)

Build:
- `app/src/main/java/com/example/taskflow/ui/schedule/SlotPage.kt` and `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleViewModel.kt` — drag-to-reorder on the slot's task list, persisted via `updateSlotSortOrder`.
- `app/src/main/java/com/example/taskflow/data/repository/TaskRepository.kt` — expose the reorder call if not already surfaced.

Test:
- On a device: drag a task within a Schedule slot (Today/Tomorrow/Soon) to a new position and confirm the order persists across navigation/relaunch. User-run.

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

- **Verify Project-deletion data behaviour end-to-end once the delete UI exists.** [unassigned-project-model] built the repository logic: deleting a real Project reassigns its tasks to the system Unassigned Project (`TaskDao.reassignTasksToProject`, called by `ProjectRepository.delete`), and the Unassigned Project is undeletable (`ProjectRepository.delete` no-ops when `isSystem`). The reassign query was verified live on-device this session; the two guards were verified by code inspection. There is no automated test of `ProjectRepository.delete` itself and no delete gesture yet to exercise it end-to-end. When [project-lifecycle-later] builds the long-press-drag delete, its test pass should confirm: deleting a real Project moves its tasks onto Unassigned with none orphaned, and the Unassigned card cannot be deleted.
  Blocked by: [project-lifecycle-later] — behavioural; the delete gesture must exist to test this end-to-end.

### Parked

- **Schedule bucketing doesn't re-evaluate across the day boundary while the app stays open.** The Schedule view computes "now" each time the task flow re-emits — a DB change, or re-subscription when the app returns to foreground. If the app sits continuously in the foreground across the day-begins-at boundary (e.g. 4 AM) with no edits, a task won't move from Tomorrow into Today until the next emission or recomposition. It's an edge case (foreground-only app, and returning to the app re-subscribes and recomputes). Best handled when 0012 wires day-begins-at: add a boundary/lifecycle-resume tick that recomputes placement. Discovered building 0002.
  Blocked by: 0012 (settings-day-begins-at) — landing; fold the refresh tick into the day-begins-at wiring.
- **Subscription pause — Play Billing pause mechanics.** Verify what Google Play Billing actually exposes for subscription pause (largely Play-Store-controlled, not app-controlled). Staleness-prone, so check at build time, not now.
  Blocked by: the subscription-handling build (queue entry 0017) — behavioural trigger, no slug yet (0017 is still a rough placeholder); confirm the Play Billing pause API when that batch is authored.
- **Subscription pause — multi-device resume merge.** Policy is decided (paused → revert to local-only). Open: when a paused multi-device user resumes, which device's local state wins / how to merge. Entangled with cloud-sync design.
  Blocked by: the cloud-sync build (queue entry 0018) — behavioural trigger, no slug yet; decide the resume-merge strategy when that batch is designed.
- **Custom instruction text — production version.** The suggested proactive-use custom-instruction text (for users' Claude preferences) needs testing in Alex's own real use before publishing. Once tested, document in Help (0022) and fold into SYSTEM-PROMPT.md if relevant.
  Blocked by: Alex testing the proactive custom-instruction in her own Claude use — behavioural trigger, no slug; needs the app usable first.
- **Empty state copy and visuals.** Write copy + visuals for each empty state in the Later-by-Project world: a Schedule slot (Today / Tomorrow / Soon) with zero tasks; an empty **Later card** — a Project the user has but with nothing in it (cards always render, even empty); and **Later before the user has made any Project of their own**, where only the pinned Unassigned card shows. The old "empty Project" state is now the empty Later card; the old "Projects list before any Project exists" is gone with the side-menu Projects list — Later is the Projects surface now. Best written in front of the real screens, not against screens that don't exist yet.
  Blocked by: [later-by-project-screen] reaching its polish pass — behavioural; the Later cards must exist before their empty states can be written. (The zero-task Schedule-slot copy could be written now against shipped 0002, but writing all the empty states together against the real Later screen is cleaner.)
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
- **Execute-by-task-area across the spine — Project visible under near-term slots.** Raised by Alex during the [project-create] device test (2026-06-21), sharpened in /plan 2026-06-22. Later-by-Project shows far-future tasks grouped by area, but a user who only feels motivation for one area wants that Project's tasks wherever they sit, including Today, Tomorrow, and Soon. A task not in Later but carrying a Project reads as "scheduled," yet the near-term views show no Project label, and adding one risks breaking the day/Soon layouts. Alex (2026-06-22): there may be no good answer now, and leaving it open is acceptable.
  Blocked by: [later-by-project-screen] — behavioural; once the real Project-grouped Later ships, look at the cross-spine project-visibility question against it.
- **Project lifecycle UI for Later-by-Project — create, reorder, delete (+ SYSTEM-PROMPT.md)** **[project-lifecycle-later]**
  Blocked by: [later-by-project-screen] — landing; the Later cards and the editor Project picker must exist before this UI attaches. The paid Claude-mediated half additionally needs the remote MCP server (queue entry 0020) and Strategy-doc reconciliation (0021) — behavioural, name those when /plan splits this.

  The [later-by-project-spec-edit] rewrite describes how Projects are created, reordered, and deleted in the Later-by-Project world. None of that UI is in the queued build batches [unassigned-project-model] or [later-by-project-screen], so it needs its own build work. SPEC describes the target; this capture is so it actually gets built.

  What SPEC now specifies:
  - **Create:** the task edit dialogue's Project picker gains a "New Project" entry. Choosing it opens a small foregrounded card to type a name (name only). On entry the Project is appended to the end of the order — a new card at the bottom of Later (above pinned Unassigned) and a new heading-and-paragraph at the end of the Strategy doc — and the edited task is filed into it. The creation plumbing (`AppViewModel.createProject`) already exists from [project-create]; only the picker entry point and the name card are new, and the old Projects-overview "+ New Project" button goes away with that page.
  - **Reorder:** the Strategy doc owns Project order. Free tier — drag Project headings in the Strategy-doc editor. Paid tier — order changes only through discussion with Claude; long-pressing a Project card on Later shows a toast "Discuss high-level strategy with Claude."
  - **Delete:** free tier — long-press a Later card and drag to a delete target in the upper-right (same gesture as tasks); the deleted Project's tasks reassign to Unassigned (reassign logic is in [unassigned-project-model]). Paid tier — long-press shows the same toast; deletion goes through Claude.

  Why tier-split: reordering and deleting a Project are decisions about the shape of the user's life, so on paid they route through Claude rather than a quick gesture; free has no Claude, so it gets direct manual gestures. Alex's call, 2026-06-22.

  SYSTEM-PROMPT.md consequence: the paid-tier reorder/delete-via-Claude behaviour belongs in SYSTEM-PROMPT.md (how Claude handles a Project reorder/delete discussion, applies it, reflects it into the Strategy doc + Later). SYSTEM-PROMPT.md is locked in the spec-edit batch, so it's untouched and needs its own edit.

  /plan should likely split this: a free-tier UI batch (creation picker + name card, free heading-drag reorder, free long-press drag-to-delete, the toast shell) buildable once the Later cards exist; and a paid Claude-mediated batch (reorder/delete through Claude) plus the SYSTEM-PROMPT.md edit, which also needs the MCP server and reconciliation.
