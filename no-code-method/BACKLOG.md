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

### Batch: Project skeleton and Room data model

The smallest possible thing that proves the local-first foundation works.

- Set up Android Studio project, Kotlin/Jetpack Compose, MVVM structure, Room dependency.
- Define the core entities: Task (with nullable `projectId` FK, nullable `date`, nullable `slot` for undated parked tasks on Soon/Later, `projectSuggestionDeclined` boolean, parent FK for subtasks, sort-order fields per slot and per Project), Project, Strategy doc (single-row table holding the user-edited paragraph content keyed by Project; structural assembly happens at render time).
- DAOs and repository layer covering the CRUD operations needed for the next two batches.
- No UI yet — verify with simple instrumentation tests that tasks can be created, dated, undated, parked on Soon, refiled, reordered.
- Serves UX.md: *Data model*.

### Batch: Schedule view (date-derived placement)

- Four Schedule screens — Today, Tomorrow, Soon, Later — with horizontal swipe navigation between them.
- Each screen shows tasks placed on it: dated tasks whose date falls into the slot's range, plus undated tasks parked on Soon or Later. Today also shows tasks dated before today (no special treatment, no label, just present in their existing order).
- Per-slot sort order, read-only for this batch (no add, no edit, no drag).
- DD/MM date display (or MM/DD per the resolved Date format planning batch) on each task in Tomorrow / Soon / Later, and on Today only for tasks dated before today. Multi-line wrap when the title doesn't fit alongside the date.
- Tomorrow → Today rollover at day-begins-at: rolled-in tasks append to the bottom of Today's list, in their existing relative order from Tomorrow. Today's tasks that didn't get completed remain in place (no move-to-top).
- Empty states for each slot.
- Today is the default open screen.
- Serves UX.md: *Schedule view*, *Tasks dated before today*.

### Batch: Side menu — Schedule + Projects + app actions

- Side drawer opens from the left edge with three sections: Schedule slots at the top, Projects in the middle (with reordering enabled — reorder here drives Strategy doc heading order), app actions pinned at the bottom.
- Tapping a Schedule slot opens that screen. Tapping a Project opens its Project view (placeholder until next batch). Tapping the Strategy doc entry opens the editor (placeholder until later batch).
- Empty Projects section shows pedagogical empty-state copy.
- Bottom-section rows: Settings, Help, Thanks, Report a bug, "turn on AI for the full experience" — placeholders for now.
- Serves UX.md: *Side menu*, *Projects in the side menu*.

### Batch: Project view

- Opening a Project shows its dedicated screen.
- Top: a collapsible card listing this Project's *dated* tasks, ordered by their position in the Schedule view. Read-only with respect to ordering. Tasks can be completed from the card. Card is scroll-bounded so a minimum number of below-the-card lines stays visible.
- Below the card: undated tasks in this Project, fully reorderable within the Project.
- Default collapsed/expanded state for the card per the resolved planning batch.
- Empty states (Project with no tasks; Project with dated tasks but no undated tasks; Project with undated tasks but none dated).
- Serves UX.md: *Project view*.

### Batch: Add task and edit task — minimum viable

- Floating-action-button to add a task. Capture inherits context:
  - From Today / Tomorrow: date auto-set to today / tomorrow.
  - From Soon / Later: no forced date or Project; the task lands on the slot the user pressed + from. If no date is set, the task is parked on that slot as undated.
  - From a Project view: filed in that Project, undated, below the card.
- Edit dialogue: title (single line), notes, Project (editable, including unassigned), date displayed but read-only for this batch (date editing comes in the side-scrolling date picker batch).
- Tap-to-complete on a non-parent task. Completed tasks move to the Today completed tray. Basic version of the tray comes online here.
- Recurrence field: present (it's now a Taskflow design choice rather than a Google Tasks API constraint).
- Serves UX.md: *Add a new task*, *Edit a task* (partial), *Completed task tray on Today* (basic), *Move between Schedule and Project*.

### Batch: Side-scrolling date picker

- Replace the read-only date display in the edit dialogue with the side-scrolling date strip.
- Tile rendering with DD/MM (or MM/DD per setting), fade-to-grey gradient with distance from today, today as the visual anchor.
- Tap a tile to select it as the task's date; visual highlight on the selected tile.
- "No date" tile at one edge of the strip — selecting it clears the date.
- When a date is set on a previously-undated task, the task moves into Schedule (and into the Project's card if it has a Project).
- Initial scroll position when dialogue opens per the resolved planning batch.
- Serves UX.md: *Date picker — side-scrolling date strip*, *Edit a task* (date editing portion), *Move between Schedule and Project*.

### Batch: Recurring tasks

- Recurrence rule editing in the edit dialogue (daily / weekly / monthly / custom — confirm shapes during this batch).
- Render every instance of a recurring task in the visible window, capped at 30 days from today.
- Manually-dated one-off tasks are NOT capped — they appear in Later regardless of how far out they are.
- Completing a recurring instance marks that instance complete and leaves future instances alone.
- Serves UX.md: *Recurring tasks*.

### Batch: Drag a task between Schedule screens to reschedule

- Long-press to pick up a task; drag to the left/right edge of the screen to swipe through to the adjacent slot.
- Drop on Today / Tomorrow → set date to today / tomorrow.
- Drop on Soon → set date to today + 2 (start of Soon range). If task was undated, leave it undated and park on Soon.
- Drop on Later → set date to today + 8 (start of Later range). If task was undated, leave it undated and park on Later.
- Reorder within a slot using per-slot sort order.
- When a parent is dragged, children travel as a single unit.
- Serves UX.md: *Drag a task between Schedule screens to reschedule*, *Reorder within a Schedule slot*.

### Batch: Subtasks under parent + parent expand/collapse

- Render subtasks nested under their parent on the parent's Schedule screen and in the parent's Project view.
- Parent tasks: expand/collapse control instead of a checkbox; tapping reveals/hides children inline beneath.
- Completion roll-up: completing all subtasks completes the parent; un-completing a subtask un-completes the parent and brings it back out of the Completed tray.
- When a parent is dragged between Schedule screens or refiled to a different Project, its children move with it as a single unit.
- Serves UX.md: *Subtasks live under their parent*, *Parent tasks expand/collapse instead of having a checkbox*.

### Batch: Outliner-style typing + drag-target icons (bin and promote)

- Edit dialogue text area renders parent + children as an indented outline.
- Enter at end of any line creates a new child line below it.
- Backspace at start of any line deletes the line and merges remaining text into the previous line (parent or sibling).
- Drag handles on each child line (parent line has no handle).
- On any task drag (from Schedule, Project view, or edit dialogue), reveal a row of drag-target icons fixed at the top right, with hover feedback.
- Drag-target icons in this batch: **bin** (delete) on screen and dialogue drags; **promote** (subtask drag in dialogue only) — promotes child to top-level task at the bottom of the parent's slot or Project, with the parent's date.
- If the parent ends up with zero children after a promotion, it reverts from expand/collapse to a checkbox.
- Serves UX.md: *Edit dialogue: outliner-style typing for subtasks*, *Drag-target icons* (bin + promote portion).

### Batch: Cut and paste (OS clipboard)

- Add **cut** icon to the drag-target icon row (both screen drags and dialogue drags).
- Drop on cut → remove task from Taskflow and write its content to the device's OS clipboard as plain text. Cutting a parent writes the whole set as indented multi-line plain text.
- Paste uses the device's normal long-press context menu, only inside an edit dialogue text field.
- Outliner editor handles multi-line indented text on paste — line breaks become new lines, indentation becomes hierarchy.
- Cut-clipboard-loss risk explicitly accepted in `UX.md` (no recently-cut buffer).
- Serves UX.md: *Drag-target icons* (cut and paste portion).

### Batch: Settings → Day begins at

- Settings screen reachable from the side menu's bottom section.
- Single time picker labelled "Day begins at." Stored locally on the device.
- Default value per the resolved planning batch.
- Wire the chosen time into the Tomorrow → Today rollover and into the side-scrolling date picker's "today" anchor.
- Serves UX.md: *Settings*, *Settings → Day begins at*.

### Batch: Settings → Date format

- Two-option setting: DD/MM (default) or MM/DD.
- Applies everywhere a date is shown — Schedule task rows, date picker tiles, Strategy doc, anywhere else.
- Default per the resolved Date format default planning batch.
- Serves UX.md: *Settings → Date format*.

### Batch: JSON export and import

- Settings entry "Export to JSON" produces a JSON file containing tasks, Projects, Strategy doc descriptions, ordering metadata, `projectSuggestionDeclined` flags.
- Settings entry "Import from JSON" accepts a previously-exported file and restores the database from it. Import warns the user that it will replace existing data.
- Serves UX.md: *JSON export and import*.

### Batch: Strategy doc and life-area context

- Strategy doc reachable from the side menu (placement per the resolved planning batch).
- In-app markdown editor for the Strategy doc.
- Mechanical structure: Project headings auto-generated from Project names, in side-menu order. User edits only the description paragraphs under each heading. Reordering Projects in the side menu reorders the heading-and-paragraph pairs.
- Share button — surfaces Android's standard share sheet for the doc (or a portion thereof).
- This batch is the free-tier surface: editor + mechanical structure, no AI reconciliation. Reconciliation is its own batch.
- Life-area data: Room schema for Claude's life-area picture, with no user-facing surface (accessed only by Claude via MCP tools).
- Serves UX.md: *Strategy doc*.

### Batch: Onboarding flow

- First-run flow: two cards explaining Schedule vs Projects, then the multi-page video, then the AI choice ("Skip AI for now" / "How do I set up Claude?"), with the X-in-corner escape hatch wired in.
- "Turn on AI for the full experience" entry in the side menu re-triggers the AI choice.
- Onboarding video content per the resolved planning batch.
- Serves UX.md: *Onboarding — first run*, *Tier model — free and paid*.

### Batch: Tier model and subscription handling

- Google Play subscription wiring for the paid tier.
- 30-day trial of the paid tier handled through Play.
- Subscription pause behaviour per the resolved planning batch.
- Local enforcement: free tier disables cloud sync and MCP; paid tier enables both.
- Serves UX.md: *Tier model — free and paid*.

### Batch: Cloud sync (paid tier)

- Push-pull sync between the device's Room database and the cloud backend.
- Conflict handling for cross-device edits (paid users may have multiple devices).
- This batch precedes the MCP server batch because the MCP server reads from the cloud-side data store, not directly from a device.
- Serves UX.md: *Tier model — free and paid* (cloud-sync portion).

### Batch: AI choice flow and MCP setup

- The "How do I set up Claude?" path: explanation screen, deep-link into Anthropic's add-custom-connector modal, instructions that work whether or not the URL accepts pre-fill (per the resolved planning batch).
- In-app verification screen confirming the connector is reachable.
- Serves UX.md: *Onboarding — first run* (Claude-setup portion), *Claude integration via remote MCP*.

### Batch: Remote MCP server

- The MCP server itself: hosted, reachable on the public internet, authenticating to a specific user's cloud-synced data.
- Tool surface: read tasks, create tasks, set/clear date, refile to Project, get/update user life-area profile, read and edit Strategy doc descriptions, mark complete.
- Server-instructions field: serve `SYSTEM-PROMPT.md` as the connection-time system prompt to Claude.
- Serves UX.md: *Claude integration via remote MCP*.
- Serves SYSTEM-PROMPT.md: connection-time delivery as Claude's system prompt.

### Batch: Strategy doc reconciliation (paid tier)

- Initial reconciliation on first AI-mode open of the Strategy doc area: Claude reads existing content, identifies tasks that contradict or are missing, presents in groups, asks. Never silently edits.
- Ongoing reconciliation: every time the user submits a Strategy doc edit, Claude diffs and surfaces downstream task impact.
- Conflict-resolution UX per the resolved planning batch.
- Serves UX.md: *Strategy doc* (reconciliation portion).
- Serves SYSTEM-PROMPT.md: Strategy doc reconciliation section.

### Batch: Help / Thanks / Report a bug content

- Fill in the three remaining bottom-of-drawer screens with real content.
- Help covers MCP setup, the production version of the suggested custom-instruction text, and the "Tasks dated before today stay on Today" description (no category name).
- Serves UX.md: *Side menu* (bottom-section content portion), *Tasks dated before today* (help-doc copy).

