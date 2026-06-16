# BACKLOG.md — Taskflow Deferred Work

All deferred work in one place. Three sections, in this order; top section first, top item first.

Maintained by Claude during planning sessions. The user does not maintain this file directly — when a planning decision changes the backlog, Claude edits this file.

## Red flags

(Empty — Claude will populate this section during sessions if security concerns are surfaced and the user defers them. See the no-code-method document, "Red flags — screen and surface".)

## Planning batches

Two kinds of question live here. **(a)** Open questions that must be resolved before some build batch can run. **(b)** Scope-existence questions whose resolution decides whether a build batch should ever exist. Each planning batch is a heading, the questions to answer, and a `Blocks:` line. Once resolved, fold answers into the relevant `UX.md` entries (or into the relevant additional source-of-truth doc, if the project has one) and remove the planning batch.

### Planning batch: Unassigned dated tasks — visibility outside Schedule

- A task with a date but no Project (`projectId == null`) appears on its date-derived Schedule slot, but does not appear in any Project view (no Project to belong to). Is this acceptable, or does the side menu need a virtual "Unassigned" entry that lists all `projectId == null` tasks for the user to triage manually?
- Same question for *undated* tasks captured from Soon/Later without a Project — they live on Soon/Later, but are nowhere else. Is that fine?
- Whichever way this goes, free-tier users have no AI to suggest Projects, so the answer matters more for them than for paid-tier users.

Blocks: Build batch — Project skeleton and Room data model (the schema needs to know whether to surface `projectId == null` tasks anywhere) and Build batch — Side menu.

### Planning batch: Strategy doc — conflict resolution UX

- During ongoing reconciliation (paid tier), when Claude's reading of the Strategy doc surfaces tasks that may need to move or be deleted, what does the user actually see? A diff view? A reconciliation queue? Just inline conversational presentation in Claude's normal output?
- What happens to Claude's pending suggestions if the user makes another edit before responding to them?

Blocks: Build batch — Strategy doc reconciliation (paid tier).

### Planning batch: Strategy doc — placement in side menu

- Where does the Strategy doc entry live in the side menu? Top of the Projects section? Above Projects as its own row? In the bottom-section app actions? Somewhere else?

Blocks: Build batch — Strategy doc and life-area context.

### Planning batch: Project-view scheduled card — collapsed or expanded by default

- When a user opens a Project view, is the top "currently scheduled" card collapsed or expanded by default? Working instinct is collapsed (Project view is primarily the dreaming surface, expansion is opt-in). Not yet decided.

Blocks: Build batch — Project view.

### Planning batch: Add-custom-connector URL pre-fill

- Does claude.ai's add-custom-connector URL accept query-param pre-fill for connector name and URL? If yes, the AI-setup flow can deep-link with Taskflow's MCP details already populated. If no, the flow needs the user to type/paste them manually.

Blocks: Build batch — AI choice flow and MCP setup.

### Planning batch: Subscription pause

- Google Play has a native pause feature, but it is largely Play-Store-controlled, not app-controlled. Verify what the current Play Billing API exposes before designing the UX.
- During a paused subscription, what happens to the user's data?
  - (a) Cloud sync continues (Alex pays hosting for non-paying user).
  - (b) Sync stops and freezes (data movement on resume).
  - (c) Revert to local-only (clean but heavier transitions on resume).

Blocks: Build batch — Tier model and subscription handling.

### Planning batch: Custom instruction text — production version

- The suggested custom-instruction text for proactive Taskflow use is to be tested in Alex's own use before publishing. Once tested, the production version is documented in the Help section and folded into `SYSTEM-PROMPT.md` if relevant.

Blocks: Build batch — Help / Thanks / Report a bug content.

### Planning batch: Date picker design

Five design questions for `UX.md` — *Date picker — side-scrolling date strip*:

- **Anchor and initial position.** When the dialogue opens for a dated task, does the strip start scrolled to the task's current date (so it's centered/visible)? When opened for an undated task being scheduled, does it start at today?
- **Scroll range.** How far back into the past does the strip allow scrolling? How far into the future? Is there a fast-forward affordance for jumping by months, or is everything reached by horizontal scroll?
- **Fade gradient.** Linear with distance? Step changes at slot boundaries? At what date does the tile become so grey it's barely readable?
- **Clearing the date.** The "no date" tile is at one edge of the strip. Which edge — the start (left, before today) or the end (far future)? Is its appearance distinct from regular tiles?
- **Visible tile count.** How many date tiles are visible at once on a typical phone screen?

Blocks: Build batch — Side-scrolling date picker.

### Planning batch: Day begins at default

- What time does the app ship with as the default day boundary? (12:00 AM is the obvious default; 4:00 AM is more forgiving for night owls.)

Blocks: Build batch — Settings → Day begins at.

### Planning batch: Date format default

- DD/MM is documented as the default in `UX.md`. Is that confirmed, or should the app detect locale at first run and default to MM/DD for US users?
- Where in Settings does the toggle live — top-level, or grouped with other date/time settings?

Blocks: Build batch — Settings → Date format.

### Planning batch: Completed tray retention

- How long do completed tasks stay in the Today tray? Forever, until midnight, until N days have passed, or until the user manually clears them?

Blocks: Build batch — Add task and edit task (basic completed tray behaviour ships in that batch but the retention rule is needed for the long-term behaviour).

### Planning batch: Empty state copy and visuals

- What does each Schedule screen look like with zero tasks? What does an empty Project look like? What does the Projects list look like before the user has created any Projects?
- The empty-Projects state has to do real pedagogical work (per `UX.md` *Projects in the side menu*) — copy needs deciding.

Blocks: Polish of every screen-rendering build batch.

### Planning batch: Help, Thanks, Report a bug content

- What lives behind each of the three bottom-of-drawer entries? Help should cover MCP setup, the production version of the suggested custom-instruction text, and the description of "Tasks dated before today" behaviour (per UX principle 4 — describe without naming the category).

Blocks: Build batch — Help / Thanks / Report a bug content.

### Planning batch: Notifications

- Does Taskflow surface any notifications (e.g., morning summary of Today), or is it purely a foreground app for v1?

Blocks: scope decision — no build batch yet.

### Planning batch: Search

- Is there a search box? If so, does it scope to the current screen, the current Project, or across the entire database (tasks + Projects + Strategy doc)?

Blocks: scope decision — no build batch yet.

### Planning batch: Onboarding video content

- What does the multi-page onboarding video actually show? It needs to demonstrate Claude/MCP value concretely enough that a user choosing between free and paid has a real picture of what they're getting.

Blocks: Build batch — Onboarding flow.

### Planning batch: Post-first-test polish review

- After the first end-to-end test session, walk the test notes and decide which polish issues warrant their own `UX.md` entry and which fold into existing entries. Per the no-code-method "How a new feature enters the project" pipeline, polish that doesn't trace to `UX.md` is a Discovery, not a build item — so each polish issue must enter via planning before it can become a build batch.

Blocks: First polish work after the first build-and-test cycle.

## Build batches

Engineering work, ordered top-to-bottom by priority. The top batch is the next build (after any one currently in progress). Each batch must be small enough to build and test in one session. If a batch grows past that, split it.

A change only belongs here if it serves a `UX.md` entry (or an entry in a relevant additional source-of-truth doc). Items that don't trace to such an entry are Discoveries, not build items — they need a planning batch (or a `UX.md` update) before they enter this section.

- `0001-project-skeleton-and-room-data-model.md` — Local-first foundation: Android project setup, Room entities, DAOs
- `0002-schedule-view-date-derived-placement.md` — Four swipeable Schedule screens with date-derived task placement
- `0003-side-menu-schedule-projects-app-actions.md` — Side drawer with Schedule, Projects, and app-action sections
- `0004-project-view.md` — Project screen with scheduled-tasks card and undated-tasks list
- `0005-add-task-and-edit-task-minimum-viable.md` — FAB add, edit dialogue, tap-to-complete, completed tray
- `0006-side-scrolling-date-picker.md` — Horizontal date strip replacing read-only date display in edit dialogue
- `0007-recurring-tasks.md` — Recurrence rules and 30-day-capped instance rendering
- `0008-drag-task-between-schedule-screens.md` — Long-press drag to reschedule across Schedule slots
- `0009-subtasks-under-parent-expand-collapse.md` — Nested subtasks with parent expand/collapse and completion roll-up
- `0010-outliner-typing-drag-target-icons.md` — Outliner editor for subtasks plus bin and promote drag targets
- `0011-cut-and-paste-os-clipboard.md` — Cut drag target and OS-clipboard paste in edit dialogue
- `0012-settings-day-begins-at.md` — Day-boundary time picker wired into rollover and date anchor
- `0013-settings-date-format.md` — DD/MM or MM/DD setting applied app-wide
- `0014-json-export-and-import.md` — Full database export/import via JSON files
- `0015-strategy-doc-and-life-area-context.md` — Strategy doc editor, mechanical structure, life-area Room schema
- `0016-onboarding-flow.md` — First-run cards, AI-value video, free/paid choice
- `0017-tier-model-and-subscription-handling.md` — Google Play subscription, trial, local tier enforcement
- `0018-cloud-sync-paid-tier.md` — Push-pull sync between device Room DB and cloud backend
- `0019-ai-choice-flow-and-mcp-setup.md` — Claude setup path with connector deep-link and verification
- `0020-remote-mcp-server.md` — Hosted MCP server with tool surface and system-prompt delivery
- `0021-strategy-doc-reconciliation-paid-tier.md` — Initial and ongoing Strategy doc reconciliation via Claude
- `0022-help-thanks-report-a-bug-content.md` — Bottom-of-drawer screen content including help and custom instructions

---
*No-code method — Version 55.*
