# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

(none surfaced yet)

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

The detailed original spec for each build batch is archived at `archive/backlog-specs/` — the filename matches the batch number/slug. These came from the old folder-based backlog; the entry below is the summary, the archived file is the full spec.

**Run /plan before building.** Several batches below depend on open design questions still sitting in Captures (carried over from the old backlog's "Planning batches"). /plan resolves those and folds the answers into SPEC.md before the dependent build runs.

### Build

--- Plan session here: process the SPEC-trim audit findings (decide keep / relocate / remove per passage, and where relocated detail lives), then queue the spec-edit-trim — before feature builds proceed ---

- **0002 — schedule-view-date-derived-placement** — Four swipeable Schedule screens with date-derived task placement.
- **0003 — side-menu-schedule-projects-app-actions** — Side drawer with Schedule, Projects, and app-action sections.
- **0004 — project-view** — Project screen with scheduled-tasks card and undated-tasks list.
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

- **Batch 0001 — TaskDao instrumentation tests (10 tests).** Create-with-date, create-without-date, park on Soon/Later, assign/refile Project FK, reorder within slot and within project, create subtask, subtask completion roll-up (TEST-LOG #002–011). Build now compiles (TEST-LOG #001 resolved 2026-06-16), so these are no longer build-blocked. Still can't run: no Android emulator (AVD) or device is available on this machine. Runnable once an emulator is created or a device is connected — user-run, or I can run them if a device is reachable.

## Captures

Captured outside /plan. Picked up and routed during the next /plan session.

- **Completed-task fate after the Today tray clears** **[completed-task-post-tray-fate]**. Tray retention is now "clear at the day-begins-at rollover" (decided 2026-06-16). Open: when a completed task clears from the tray, what becomes of it — permanently deleted, or kept in a recoverable history/archive (and if so, reached how)? Batch 0005 needs this to know what "clear" does to the data. No forward blocker — resolve in a future /plan before 0005 builds the tray.

- **SPEC-trim audit findings — keep vs relocate** **[spec-trim-audit-findings]**. Output of the audit-spec-trim batch (2026-06-17). The `--- Plan session here ---` marker above the feature builds points at this. The audit read every SPEC.md section and classified each passage as product truth (KEEP) or exhaustive UX/implementation detail (REMOVE-CANDIDATE). Everything not listed below is KEEP by default — only the 20 findings here are flagged. REMOVE-CANDIDATE means: relocate the detail to the named batch or doc, keep the concept in SPEC. FLAG means genuinely ambiguous or a redundancy — a fate decision for /plan to make, not pre-judged. Nothing is removed yet; the actual trimming is a later spec-edit batch this /plan session should queue after deciding each finding.

  Framing
  - F1 (FLAG). SPEC.md:3-5. The intro defines SPEC as describing "every functionality and UI element as the user experiences it." That is a mandate for exactly the detail this audit trims. Reconcile the intro's scope statement with the trim direction first, or the spec-edit will contradict the doc's stated purpose.

  Data model
  - F2 (FLAG / if-removed → 0002). SPEC.md:41. The slot numeric thresholds (2–7 days = Soon, 8+ = Later, before today = Today). Ambiguous: these both define what the slots mean (product truth) and read as builder mechanics. Same numbers recur in F7.

  Schedule view
  - F3 (REMOVE → 0002, date-format dep on 0013). SPEC.md:53. Date-row layout: right-edge placement, multi-line wrap, date right-aligned at the top of the wrap. Keep: that Tomorrow/Soon/Later rows show dates and Today shows one only when the date is past.
  - F4 (REMOVE → 0002 / 0012). SPEC.md:55. The rollover placement mechanic ("appended to the bottom of Today's list, in their existing relative order"). Keep: that Tomorrow rolls into Today at the boundary, and the no-label/no-reorder no-shame behaviour.

  Project view
  - F5 (REMOVE → 0004). SPEC.md:83. Card scroll-bounding ("must leave a minimum number of below-the-card lines visible"). Keep: collapsed-by-default and read-only ordering.

  Drag between Schedule screens
  - F6 (REMOVE → 0008). SPEC.md:104. Gesture mechanics (long-press pick-up, drag to the edge to swipe, drop). Keep: that drag between screens reschedules.
  - F7 (REMOVE → 0008). SPEC.md:106-109. Exact date results per drop (today / tomorrow / today+2 / today+8; an undated task stays parked). Same numbers as F2.

  Date picker
  - F8 (REMOVE → 0006). SPEC.md:141. Fade mechanics (linear with distance, not stepped at slot boundaries, capped so far tiles stay readable).
  - F9 (REMOVE → 0006). SPEC.md:143. Strip dimensions (centred on the task's date or today, one month past to twelve months future, month-jump affordance, 5–7 tiles visible). The phrase "(to be finalised against a real screen at build time)" also violates SPEC's own line-5 rule against undecided placeholders, so it should leave on that ground alone.
  - F10 (REMOVE → 0006). SPEC.md:145. No-date tile placement and styling (left edge before today, labelled rather than greyed). Keep: that a no-date clearing affordance exists.

  Subtasks
  - F11 (REMOVE → 0009). SPEC.md:157. Parent tap mechanics (tap reveals indented subtasks, tap again hides, tap the title to open the dialogue). Keep: expand/collapse-instead-of-checkbox and completion roll-up.
  - F12 (REMOVE → 0010). SPEC.md:165-168. Outliner keystroke mechanics (Enter creates a child, Backspace merges, child drag handle, promote-on-drop, a parent with zero children reverts to a checkbox). Keep: that subtasks are edited as an outliner and that promote exists.

  Drag-target icons and cut/paste
  - F13 (REMOVE → 0010). SPEC.md:174-179. Icon placement and hover feedback (top-right fixed, per-context icon sets, grows larger with a shadow on hover).
  - F14 (REMOVE → 0011). SPEC.md:181. Cut/paste OS-clipboard flow mechanics (plain-text indentation round-trip, long-press to Paste, create-via-FAB-then-paste to make a new top-level task). Keep: the decision to use the OS clipboard and the risk-accepted paragraph.

  Add a task and the tray
  - F15 (REMOVE → REGISTRY / 0005). SPEC.md:124,126. The `projectId` null jargon. Keep the plain version: "Project defaults to unassigned."
  - F16 (REMOVE, low → 0005). SPEC.md:189. Tray minor mechanics ("moves to the bottom of the tray", "scrollable"). Keep: the tray concept and rollover-clear.

  Onboarding
  - F17 (REMOVE → 0016). SPEC.md:199. The exact onboarding card copy strings. Keep: that two cards make the Schedule/Project distinction.

  Redundancies (consolidate in the spec-edit)
  - F18 (FLAG). SPEC.md:73-77 vs 274. Projects-in-the-side-menu is described twice.
  - F19 (FLAG). SPEC.md:41 / 90-100 / 135. The date-set/clear-moves-the-task effect is stated three times.
  - F20 (FLAG). SPEC.md:69. The "tasks dated before today" help wording duplicates the parked Help-content capture; coordinate when 0022/Help is built.

---

(Raw captures collect below this line, then get processed and moved above it during /plan.)

### Parked

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
- **Onboarding video content.** The multi-page onboarding video must demonstrate Claude/MCP value concretely (for the free-vs-paid choice). Can't be produced until the Claude/MCP integration exists to film. Sequencing note: onboarding (0016) is queued ahead of the MCP work, so 0016 may need to ship with a placeholder video and have the real one added once the integration lands.
  Blocked by: the Claude/MCP integration builds (0019 AI-choice / MCP setup, 0020 remote MCP server, 0021 reconciliation) — behavioural, no slug; produce the video once there's a working integration to demonstrate.
- **Post-first-test polish review.** After the first end-to-end test, walk the test notes and decide which polish issues warrant their own SPEC.md entry and which fold into existing ones — polish that doesn't trace to SPEC.md is a capture, not a build item.
  Blocked by: the first end-to-end test having happened — behavioural, no slug; when it lands, run a /plan polish-review pass over the test notes.
