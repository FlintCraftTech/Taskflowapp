# QUEUE

## Red flags

Security, privacy, and data-exposure risks Claude has surfaced — kept at the top so they're the first thing seen each session. Each carries a state: open, resolved, or accepted. Empty until a risk comes up.

(none surfaced yet)

## Batches

Worked top to bottom. Each batch is one /next session. Subheadings name the kind of work (Build, Test, Audit).

The detailed original spec for each build batch is archived at `archive/backlog-specs/` — the filename matches the batch number/slug. These came from the old folder-based backlog; the entry below is the summary, the archived file is the full spec.

**Run /plan before building.** Several batches below depend on open design questions still sitting in Captures (carried over from the old backlog's "Planning batches"). /plan resolves those and folds the answers into SPEC.md before the dependent build runs.

### Build

**Navigation spine — SPEC rewrite (right half)** **[nav-spine-spec-edit]**
Blocks: schedule-view-date-derived-placement, side-menu-schedule-projects-app-actions, project-view

Taskflow's navigation is being reframed from "four discrete swipeable Schedule screens plus a side menu" into a single left-to-right spine: Today · Tomorrow · Soon · Later · Projects · Strategy, swiped between adjacent pages, Today still the default open page. The organising idea is "zoom levels of time": a day is the finest grain, weeks are coarser, and past about a week time stops being the useful frame while Projects and Strategy take over. The spine lets navigation glide from arranging time into arranging projects rather than forcing everything onto a clock. This was committed in planning on 2026-06-17. The full redesign also has a left half — completed-history: a Search page, a Yesterday page, a day-detail card layer, and share-a-day — which stays parked as post-core work. This batch ships only the right half. The four schedule screens and their date-derived task placement are unchanged in substance; they become the left segment of the spine. So this batch reframes how SPEC describes navigation, ahead of the builds (0002 / 0003 / 0004) that implement it. It also completes three SPEC-trim audit findings that were deliberately held for exactly this rewrite — F3 and F4 (exhaustive Schedule-view layout and rollover mechanics) and F18 (the doubly-described Projects-in-the-side-menu) — because they live in the sections being rewritten; doing them here finishes the deferred trim in its intended home rather than redoing it later.

Spec-edit:
- §UX principles → principle 3: rewrite so the four slots are the left segment of a left-to-right spine that extends rightward into Projects and Strategy, reachable by swipe and not only via the side menu and the edit dialogue. Keep "Projects are not slices of the day" and the bounded-glanceable-horizon rationale.
- §Schedule view: (a) replace "presented as four discrete swipeable screens, navigated by horizontal swipe in the manner of a phone home screen" with the spine framing — the four are pages on one horizontal spine that continues into a Projects page and a Strategy page on the right; Today is still the default open page; swipe moves between adjacent pages. (b) Drop the date-row layout mechanics (right-edge placement, multi-line wrap, date right-aligned at the top of the wrap) — build detail owned by 0002 — keeping the concept that Tomorrow/Soon/Later rows show their date and Today shows one only when the date is past. [absorbs held finding F3] (c) Drop the rollover placement mechanic ("appended to the bottom of Today's list, in their existing relative order") — build detail owned by 0002/0012 — keeping the concept that Tomorrow rolls into Today at the day-begins-at boundary with no label, no reorder, no shame. [absorbs held finding F4]
- §Side menu + §Projects in the side menu: (a) rewrite §Side menu from three labelled sections (Schedule / Projects / app-actions) into a single navigation list that mirrors the spine top-to-bottom — Today, Tomorrow, Soon, Later, the projects list, then Strategy — with Settings, Help, Thanks, Report a bug, and the AI entry still pinned at the bottom as app-actions; this moves the Strategy-doc row from the top of the Projects section (set 2026-06-16) to after the projects list, its spine position. (b) Consolidate the doubly-described "Projects in the side menu": the standalone §Projects in the side menu section and the §Side menu subsection state the same thing twice — fold into one home as part of this rewrite (the spine also makes Projects reachable via the spine, not only the menu, so that section's framing changes regardless). [absorbs held finding F18]
- §Strategy doc: update the "single calm row at the top of the Projects section, above the individual Projects" sentence to match the new after-projects position. Keep the "reachable but never foregrounded" intent.
- Add a Projects overview as a spine page: swiping right from Later reaches a full-page list of the user's Projects (the same list the side menu shows); tapping a Project opens its Project view. Strategy is the page right of Projects.

- **0002 — schedule-view-date-derived-placement** — The Today / Tomorrow / Soon / Later screens built as the left segment of the navigation spine, with date-derived task placement. Builds against the revised SPEC (after nav-spine-spec-edit); the archived spec's "four discrete swipeable screens" framing is superseded — same four screens, now pages on the spine.
- **0003 — side-menu-schedule-projects-app-actions** — Side drawer as a single navigation list mirroring the spine (Today · Tomorrow · Soon · Later · projects list · Strategy) with app-actions pinned at the bottom, plus the Projects overview spine page. Builds against revised SPEC §Side menu; supersedes the archived spec's three-section framing and the Strategy-row-at-top placement.
- **0004 — project-view** — Project screen with scheduled-tasks card and undated-tasks list (internal layout unchanged by the spine). Now also reached via the Projects spine page, not only the side menu.
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
