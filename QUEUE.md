# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

(none surfaced yet)

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

The detailed original spec for each build batch is archived at `archive/backlog-specs/` — the filename matches the batch number/slug. These came from the old folder-based backlog; the entry below is the summary, the archived file is the full spec.

### Build

- **0005 — add-task-and-edit-task-minimum-viable** — FAB add, edit dialogue, tap-to-complete, completed tray.
- **0006 — side-scrolling-date-picker** — Horizontal date strip replacing read-only date display in edit dialogue.
- **0007 — recurring-tasks** — Recurrence rules and 30-day-capped instance rendering.
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

- **Batch 0002 — Schedule view data-dependent rendering.** Needs tasks in the DB (no add path until 0005, or a manual DB seed). Verify on a device: dated tasks land in the correct slot (today + past on Today, tomorrow on Tomorrow, 2–7 days Soon, 8+ Later); past-dated tasks show on Today with a DD/MM date and no overdue label; Tomorrow/Soon/Later rows show their DD/MM date; a long task title wraps to multiple lines with the date staying top-right. (Render, Today-as-default, swipe + arrow-tap navigation, dead-end arrow hiding, and per-slot empty states were confirmed on a Pixel 6 on 2026-06-17.) Confirmed by: viewing seeded tasks on each page — runnable once 0005 lands a create path (or via a manual DB seed); Claude can run it then if a device is reachable.

- **Batch 0004 — Project view rendering.** Needs a Project with dated and undated tasks in the DB (no add path until 0005, or a manual DB seed). Verify on a device: opening a Project shows its screen; the "Scheduled" card is collapsed by default and toggles on tap; expanded, it lists the Project's dated tasks with DD/MM labels in Schedule order and is height-bounded so it scrolls internally without crowding out the list below; the below-card undated list renders in per-Project order; and the three empty states render (no tasks at all; dated-but-no-undated shows the card plus a "no unscheduled tasks" line; undated-but-no-dated shows just the list with no card). Confirmed by: viewing a seeded Project on a device — runnable once 0005 lands a create path (or via a manual DB seed); Claude can run it then if a device is reachable.

## Captures

Captured outside /plan. Picked up and routed during the next /plan session.

---

(Raw captures collect below this line, then get processed and moved above it during /plan.)

- **Spine header polish — two issues carried from batch 0002 (the ScheduleScreen header).** Both surfaced on-device while verifying 0003, and both pre-date this batch — 0003 only renamed the header code to span the new Projects page.
  1. *Title overlaps moving backward (e.g. Soon → Tomorrow).* The page name animates with a slide — the outgoing word slides out as the incoming slides in, both crossing the same centred frame (`AnimatedSpineTitle` / `AnimatedContent` in `app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt`). Going backward, "Soon" doesn't clear fast enough and overlaps "Tomorrow" mid-transition. Fix idea: stagger the slide so the outgoing word leaves before the incoming arrives, or speed up / fade the exit.
  2. *Arrows should be Material chevron icons, not text glyphs.* The ←/→ are plain text characters (`Chevron` composable, same file); they render small and sit low because a text glyph aligns to its baseline, not the row centre. Alex wants proper Material chevrons, vertically centred with the title. Note: Material icons aren't a current dependency — this likely needs adding `androidx.compose.material` material-icons (KeyboardArrowLeft/Right are in core; the chevron pair is in extended) plus an `app/build.gradle.kts` edit.

  Best handled as one small header-polish pass.

- **Task reorder-by-drag has no batch home — surfaced deferring 0004 (2026-06-18).** SPEC §Project view says the below-card undated list is "fully reorderable within the Project," and SPEC §Reorder within a Schedule slot says Schedule rows reorder by drag too. Both are *within-list* drag-reorder of top-level tasks. Neither has a dedicated batch: 0008 is cross-slot reschedule (drag a task to a *different* slot), 0010 is subtask/outliner drag, and 0011 is cut-and-paste — none cover reordering top-level tasks inside one slot or one Project.

  The data layer already supports it (`TaskDao.updateProjectSortOrder` / `updateSlotSortOrder` / `getMax…SortOrder` exist; reorder persistence was DAO-tested in 0001). What's missing is the drag-reorder UI. 0004 was built render-only (this session), so its below-card list shows in `project_sort_order` but can't be reordered yet; 0002's Schedule view deferred the same.

  Why captured: completion-from-card has a clear home in 0005, but within-list reorder-by-drag is genuinely unowned — without a batch it silently never ships. The two cases (Schedule slot, Project below-card) likely share one drag-reorder batch. For /plan to place.

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
