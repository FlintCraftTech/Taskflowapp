# SPEC.md — Taskflow User Experience

This document describes every functionality and UI element as the user experiences it, and why the user needs it. Every entry must correspond to something that actually exists in the current build. If an entry cannot be traced to an existing feature, it is not a current user experience — it is a plan, and it belongs in `QUEUE.md`.

`SPEC.md` only describes what has been decided. Open questions and undecided details do NOT live here as placeholders, and do NOT live here as sentences that gesture at the doc's own undecidedness (e.g. "currently undecided", "pending decision"). Open questions live in `QUEUE.md` as captures.

## Project context

Taskflow is a native Android task manager (Kotlin / Jetpack Compose, MVVM with Room) designed around executive dysfunction. It separates two thinking modes most task apps conflate: a Schedule view for committed, time-bounded execution, and Projects for divergent, no-time-pressure planning. A Strategy doc sits above Projects to organise the medium- and long-term picture. Taskflow's data lives locally on the device by default; cloud sync is the bundle that unlocks Claude integration on the paid tier, where the user talks to Claude in their normal Claude client (web, desktop, or mobile) and Claude reaches Taskflow through a remote MCP server. Taskflow does not import from, sync to, or export to any external task app.

## UX principles for Taskflow

These inform every design decision. Entries below should serve one or more of these principles. If a proposed change conflicts with a principle, flag the conflict before building.

1. **Two thinking modes, with a bright line between them.** The Schedule view (Today / Tomorrow / Soon / Later) is convergent — what's committed, what's next. Projects are divergent — what's possible, no time pressure. The transition between them is a deliberate per-task act in both directions; the app does not silently move a task from one to the other. Most task apps live entirely in convergent mode and quietly punish users who try to think divergently about future possibility, which is the failure mode Projects exist to prevent.

2. **Local-first source of truth.** Taskflow's own Room database is the source of truth for everything: tasks, Projects, the Strategy doc, ordering. No syncing to or importing from external task apps. Android Auto Backup (the OS-level feature) and an explicit JSON export/import screen handle portability for free-tier users. Cloud sync exists only as the paid tier and is the precondition for Claude integration.

3. **Time horizon, not list, structures execution.** The four Schedule slots — Today, Tomorrow, Soon, Later — map onto how attention naturally scales with time. Splitting them into discrete swipeable screens (rather than one infinite scroll) makes each horizon a bounded, glanceable thing. Projects answer different questions ("what's possible?", "where does this belong?") and are reached via the side menu and from inside a task's edit dialogue. Projects are not slices of the day.

4. **No-shame zone.** Taskflow does not name, count, or visually pressure tasks that have slipped past their date. They simply remain on Today, in the order the user placed them, until the user does them or moves them. The app has no "overdue" label or category — the help docs describe the behaviour without naming it as a category at all.

5. **Capture inherits context.** Adding a task from a Schedule screen places it on that screen. Adding from inside a Project files it in that Project. The default capture path never asks the user to choose between Schedule and Project, or to set a date on Soon/Later capture — those decisions are made by where the user was when they tapped Add (and whatever defaults follow from that). Moving a task between Schedule and a Project later, or assigning a date to an undated task, is a separate, deliberate act done in the edit modal.

6. **Spatial action equals temporal change.** Dragging a task between Schedule screens changes when it's committed for. Reordering within a screen changes its sequence. The user reschedules and reprioritises by moving things, not by opening editors and adjusting fields. The screen a task is on IS its time horizon, expressed visually.

7. **Taskflow does dates, not times-of-day.** Taskflow has no time-of-day picker anywhere except for a single setting that controls when "today" rolls over for the user (see Settings → Day begins at). Clock-time scheduling — "this meeting at 3pm" — is a different problem from time-horizon scheduling — "this task sometime soon." Taskflow's distinctive contribution is the four-screen time-horizon view above the level of clock time, where Soon and Later are real categories rather than empty space between dated events. Folding clock-time into Taskflow would dilute the thing that makes it itself. If the user wants clock-time scheduling, that's a calendar app's job.

## Functionalities

### Data model

A task has the following user-visible properties:

- A **Project**, which may be a real Project or unassigned (no Project).
- An optional **date**.
- An optional **Schedule slot** (Today / Tomorrow / Soon / Later), used only for undated tasks placed on Soon or Later. Dated tasks derive their slot from their date.

Where a task appears on the user's screens follows from these properties:

- **Dated tasks** appear in Schedule on the slot derived from their date (today → Today, tomorrow → Tomorrow, 2–7 days out → Soon, 8 or more days out → Later, before today → still Today). They also appear in their Project's top card if they have a Project.
- **Undated tasks captured from (or dragged to) Soon or Later** appear on that slot. They do not appear in any Project's top card (cards list dated tasks). They appear in their Project's below-the-card area if they have a Project.
- **Undated tasks with no Schedule placement** appear only below the card in their Project view.

Today and Tomorrow tasks always have a date — capturing on those screens always sets the date, and there is no path to land an undated task there. Undated parking is a Soon/Later-only behaviour.

The user needs this because Projects answer "where does this belong?" while Schedule answers "when am I doing it?" — two questions that should not collapse into one. Allowing tasks to live on Soon or Later without a date preserves the "I don't know exactly when, but soon" feeling that the user genuinely has, without forcing a date pick they can't yet make.

### Schedule view

The four Schedule slots — **Today**, **Tomorrow**, **Soon**, **Later** — are presented as four discrete swipeable screens, navigated by horizontal swipe in the manner of a phone home screen. Each screen shows the tasks placed on that slot. Today is the default open screen. Project membership is not visible at this level; the only place a task's Project is visible during execution is inside its edit dialogue.

Each task on Tomorrow, Soon, and Later shows its date (when the task has one) inline on the right edge of the row in **DD/MM** format (or **MM/DD** if the user has selected that in Settings — see *Settings → Date format*). Today's tasks do not show a DD/MM label except when the date is in the past (then the date communicates how stale the task is). If the task title is too long to fit alongside the date, the row wraps to multi-line; the date stays right-aligned at the top of the wrap.

**At the day-begins-at boundary** (see *Settings → Day begins at*), Tomorrow's tasks roll into Today, appended to the bottom of Today's list, in their existing relative order from Tomorrow. Today's tasks that did not get completed simply remain on Today in the order the user placed them — they do not move to the top, they do not gain a label, they do not change appearance. They are just still there.

The user needs this because execution lives in a single time continuum. While executing, the user wants to see what they have committed for now, next, this week, and beyond — sliced by attention horizon, not by category. The "still there from before" behaviour is deliberate (UX principle 4): a task that slips past its date should not become a daily reminder of failure.

### Recurring tasks

Recurring tasks created in Taskflow appear on whichever Schedule slot each instance's date falls into. Unlike one-off tasks, **every instance** in the visible window is shown — so a weekly Monday task appears as four separate rows in Later for the next month.

Recurring tasks are capped at **30 days into the future**: instances dated more than a month from today are not shown until the world catches up to within a month of them. A yearly birthday reminder, for example, is invisible until you're within a month of the next instance. Manually-dated one-off tasks are not capped — a task you've manually dated for next year appears in Later normally.

The user needs this because recurring tasks generate an indefinite tail of future instances. Showing all of them would flood Later; showing only the next would lose the rhythm the user actually wants to see (every week's "water plants," for example, sitting there as a checklist of the coming month). The 30-day cap is the compromise.

### Tasks dated before today

A task whose date is in the past stays on Today, in the position the user placed it, until the user completes it or moves it. The app does not call attention to it — no overdue label, no red colour, no visual pressure, no auto-reordering to the top of the list. The DD/MM date inline on the right is the only signal that the date has slipped, and only because that information is useful to the user when they choose to read it. Help docs describe the behaviour without naming the category — they say something like "Tasks dated before today stay on Today, in the order you placed them, until you do them or move them."

The user needs this because a no-shame zone (UX principle 4) means refusing to dramatize the past. Most "overdue" UI exists to nag the user; nagging is exactly the failure mode this app is built to avoid.

### Projects in the side menu

A side menu, opened from the left edge, lists the user's Projects. Tapping a Project opens its Project view. The Projects section is visible by default, even when empty — its empty state explains what Projects are for, doing pedagogical work for new users without requiring an onboarding step.

The user needs this because Projects are a primary navigation surface, not a buried feature, but they are a different question from "what's next?" and belong off the swipeable Schedule screens (UX principle 3).

### Project view

A Project screen has two parts:

- **Top card — currently scheduled (dated) tasks.** A collapsible card listing this Project's dated tasks, ordered by their position in the Schedule view. The card is read-only with respect to ordering: task order is authored only in the Schedule view, and the Project view reflects that order. Tasks can be completed from the card. The card is scroll-bounded — it must leave a minimum number of below-the-card lines visible at the bottom of the screen, so the unscheduled list is never crowded out.
- **Below the card — undated tasks in this Project.** Fully reorderable within the Project. This is the dreaming surface — the place tasks live when they belong to this area of life but have not been committed for any specific time.

When an undated task in this list is given a date (in the edit modal), it moves up into the top card and appears in the corresponding Schedule slot.

The user needs this because Projects must show both "what am I currently doing in this area?" (the dated tasks, for context) and "what else is in this area waiting?" (the undated tasks, for divergent thinking). Authoring task order only in Schedule and reflecting it in the card, while authoring the below-the-card order in each Project, gives each surface one clean ordering authority.

### Move between Schedule and Project

A task moves between Schedule and a Project as a side-effect of editing two of its properties:

- **Adding or changing a date** moves the task into Schedule on the corresponding slot (and into the Project's card, if it has a Project).
- **Removing a date** drops the task from Schedule (and from any Project card) and into below-the-card in its Project view, if it has a Project. If it has no Project, the task lives on whichever Schedule slot it was previously on as an undated parked task — this is only possible on Soon/Later.
- **Changing the Project** refiles the task. If the task is dated, it appears in the new Project's card as well as continuing on its Schedule slot.

Drag-to-reschedule between Schedule screens (covered separately) changes the date directly via gesture.

The user needs this because the bright line between Schedule and Project (UX principle 1) is preserved by making transitions explicit. Adjusting a task's date is the only way to push it into Schedule or pull it back; the app never silently changes which side of the line a task lives on.

### Drag a task between Schedule screens to reschedule

Tasks on Schedule screens are draggable in the manner of icons on a phone home screen. Long-pressing a task picks it up; the user can drag it to the left or right edge of the current screen to swipe through to the adjacent slot, and drop it there. Dropping a task on a different slot updates its date as follows:

- **Drop on Today** → date becomes today.
- **Drop on Tomorrow** → date becomes tomorrow.
- **Drop on Soon** → date becomes the start of the Soon range (today + 2). If the dragged task was undated, it remains undated and is parked on Soon.
- **Drop on Later** → date becomes the start of the Later range (today + 8). If the dragged task was undated, it remains undated and is parked on Later.

When a parent task is dragged, its subtasks travel with it as a single unit — the parent's date (or parking) is rewritten and the children continue under it on the new screen.

The user needs this because rescheduling is the single most common edit a person makes to a task list, and spatial drag-to-reschedule is faster and more intuitive than opening an editor and tapping a date field (UX principle 6).

### Reorder within a Schedule slot

Within a single Schedule screen, the user reorders tasks by drag, without leaving the slot. The new order persists in the local database per-slot. Within a Project view, the below-the-card list has its own per-Project order.

The user needs this because tasks in the same slot still have a sequence — what to do first, what to do last — and that sequence is the user's own ranking, not derivable from anything else.

### Add a new task

The user adds a task from any screen via a floating action button or equivalent affordance. Where the new task goes follows capture-inherits-context (UX principle 5):

- **From Today or Tomorrow** — date is auto-set to today or tomorrow; the task lands on that screen. Project defaults to unassigned (`projectId` is null).
- **From Soon or Later** — the task lands on that screen. The user is not forced to pick a date; if they leave the date empty, the task remains an undated parked task on that slot. The user is not forced to pick a Project either; if they don't pick one, `projectId` is null.
- **From inside a Project view** — the task is filed in that Project, undated, below the card.

The user can change the Project, the date, or both inside the edit dialogue afterwards.

The user needs this because the most natural starting point for a new task is the place the user is currently looking at, with the lightest possible required input. Forcing a date pick on Soon/Later capture, or a Project pick when neither was implied, would slow capture down — and slow capture is exactly the friction Taskflow is trying to remove.

### Edit a task

Tapping a task opens an edit dialogue showing its title, notes, Project, and date. The Project field is editable so the user can refile a task. The date field is the side-scrolling date strip (see *Date picker — side-scrolling date strip*) — users can set a date, change a date, or clear a date. Setting a date on a previously-undated task pushes it into Schedule (and into the Project's card if it has a Project); clearing the date drops it out of Schedule. There is no time-of-day picker anywhere in this dialogue (UX principle 7).

The user needs this because they need a way to change the two things drag-to-reschedule cannot change in a single gesture: which Project the task lives in, and whether it's dated at all.

### Date picker — side-scrolling date strip

The date field inside the edit dialogue is a horizontal, side-scrollable strip of date tiles, embedded directly in the dialogue (not a popup). Each tile shows a single date in DD/MM format (or MM/DD per the user's setting). The user scrolls left and right to find a date and taps a tile to select it. The selected tile is visually highlighted. Date tiles fade to grey the further they are from today — today is the visual anchor and the least-grey tile, giving the user a sense of how far they have scrolled.

A "no date" option at the strip's edge allows the user to clear the date.

The user needs this because the standard Android date picker (a calendar grid in a popup) is heavy for this app's purpose. Most date assignments in Taskflow are within a week or two of today, and a horizontal strip lets the user scrub through nearby dates as fast as their thumb. Embedding the picker in the dialogue keeps the editing flow continuous.

### Subtasks live under their parent

A subtask appears nested under its parent on whichever Schedule screen or in whichever Project the parent lives. Subtasks do not have their own date, do not have their own Project (they inherit the parent's), and do not have their own Schedule placement. When a parent is moved between screens or refiled to a different Project, its children move with it as a single unit.

The user needs this because subtasks are sub-units of a parent task, not independent items. Treating them as part of their parent — moving when the parent moves, listed under the parent on the parent's surface, sharing one date — keeps each surface focused on the unit of work the user actually thinks about (the parent task).

### Parent tasks expand/collapse instead of having a checkbox

A parent task (one with subtasks) does not have a checkmark on Schedule screens or in the Project view. Where the checkmark would be, there is an expand/collapse control. Tapping it reveals the parent's subtasks inline beneath the parent on the current screen, indented. Tapping it again hides them. Tapping the parent's title (not the expand control) opens the edit dialogue. A parent is considered complete only when all its subtasks are complete; completing the last subtask completes the parent and sends it to the Completed tray (see below). Un-completing any subtask un-completes the parent and brings it back out of the tray.

The user needs this because manually checking a parent while subtasks remain open creates an obvious state mismatch. Driving completion from the children up makes the parent's completion state derivable rather than user-entered.

### Edit dialogue: outliner-style typing for subtasks

Inside the edit dialogue, a task and its subtasks are rendered as a small outliner — the parent task's title on the top line, child task titles indented beneath. The text area behaves like a normal paragraph editor:

- Pressing **Enter** at the end of a line creates a new line below it. A new line typed below the parent becomes a child of that parent.
- Pressing **Backspace** at the start of a line deletes that line and merges its remaining text into the end of the previous line — the same behaviour as in any paragraph editor. The previous line can be the parent or a sibling child.

Each child line has a drag handle on its left edge (the parent line does not — only subtasks have handles). Dragging a child by its handle reveals the drag-target icon row (see *Drag-target icons*); dropping the child on the **promote** icon promotes it to a top-level task at the bottom of the parent's slot or Project, with the parent's date (if any). If the parent ends up with zero children after a promotion, it stops being a parent and reverts to a checkbox.

The user needs this because adding and breaking down subtasks are the highest-frequency edits a person makes when capturing work. Lifting subtask creation into the same gestures used for any text editing makes capturing a parent-with-children as fast as typing a paragraph. The promote target covers the natural follow-up — "actually this child should stand on its own" — without requiring a separate menu or mode.

### Drag-target icons

Whenever the user picks up a task by drag (whether from a Schedule screen, a Project view, or inside an edit dialogue), a row of drag-target icons appears at the **top right** of the screen, fixed in place. The set of icons depends on context:

- **From a Schedule screen or a Project view:** a **bin** icon (delete the task) and a **cut** icon (remove the task from Taskflow and place its content on the device clipboard).
- **From inside an edit dialogue (subtask only):** a **bin** icon, a **cut** icon, and a **promote** icon (the standard "list" Material icon with the arrow pointing up). Promote behaviour is described above.

When the user drags over a target icon, the icon visually responds — for example, growing slightly larger and gaining a shadow — so the user can see they have hit the right target before releasing.

The cut/paste flow uses the **device's OS clipboard**, not a Taskflow-internal one. Dropping a task on **cut** removes it from Taskflow and writes its content to the OS clipboard as **plain text**. When the user cuts a parent, the whole set (parent plus all children) goes to the clipboard as a single block of indented multi-line plain text. Paste is only available **inside an edit dialogue** — the user long-presses inside any text field and chooses Paste from the device's standard context menu. The outliner editor interprets pasted text by line breaks and indentation: a multi-line indented block restored Taskflow-to-Taskflow round-trips its hierarchy. Pasting non-Taskflow text inserts that text as new lines per the same indentation rule. To create a new top-level task from a cut task, the user creates an empty new task via the FAB, opens its edit dialogue, and pastes there.

**Risk accepted:** if the OS clipboard is overwritten between cut and paste (e.g., by another app copying a notification number), the cut task is lost permanently. The user has accepted this trade-off in exchange for the simplicity of using the device's native clipboard.

The user needs this because the operations Taskflow exposes on a dragged task (delete, cut, promote) are conceptually parallel — each is "send the task to a destination" — and giving them the same drag-to-icon shape keeps the gesture vocabulary consistent. Routing paste through the device's normal paste mechanism means the user's existing muscle memory works.

### Completed task tray on Today

At the bottom of the Today screen is a greyed-out, scrollable list of completed tasks. When the user checks off any task on any Schedule screen or in any Project view, that task moves to the bottom of this tray. Tapping a completed task in the tray opens its edit dialogue, where the user can uncheck individual subtasks (which un-completes the parent and removes it from the tray) or otherwise modify the task.

The user needs this because seeing what they have already done provides a sense of progress, and routing all completions through one tray (rather than letting them disappear from each screen individually) gives the user one place to find and undo a mis-tapped completion.

### Onboarding — first run

The first time the user opens Taskflow, a short onboarding flow runs:

- **Two cards making the Schedule / Project distinction explicit.** "Schedule is where you commit — when, what's next." "Projects are where you dream — what's possible, no time pressure."
- **A multi-page video demonstrating the AI value.** What the paid tier with Claude unlocks in practice.
- **Two choices at the end.** *Skip AI for now (free)* or *How do I set up Claude?*. If the user chooses Claude setup, a follow-up screen explains they need a Claude account and deep-links into Anthropic's add-custom-connector modal where possible.
- **An X-in-corner escape hatch at any point** drops the user into no-AI mode (free tier).

The user needs this because the Schedule / Project split is the structural thing the user must understand before the app makes sense, and the AI choice is real and visible — the free tier is genuinely complete, not a hobbled trial — so the user should be able to see what they're choosing between rather than have it hidden.

### Tier model — free and paid

Two tiers, presented openly during onboarding and re-accessible from the side menu:

- **Free tier.** Local-only. No cloud sync. No Claude integration. The user gets the full Schedule, full Projects, and the full Strategy doc editor (with its mechanically-generated structure — see *Strategy doc*), plus manual JSON export/import. Free is "hard mode" by design — the user writes everything themselves. Free is not hidden, not punished, and not a time-limited trial. It is a complete product.
- **Paid tier.** Cloud sync plus Claude integration via remote MCP, bundled. Cloud sync is the infrastructure precondition for the MCP server to be reachable from Anthropic's servers; the two cannot meaningfully be separated.
- **Trial.** 30 days of paid tier, handled through Google Play.

The side menu always shows a "turn on AI for the full experience" entry that re-triggers the AI choice flow.

The user needs this because the value Taskflow offers on the paid tier — Claude integration into the user's normal Claude environment — is real, but it requires the user to already use Claude. Forcing every user into that path would mis-position the app. Offering both tiers honestly lets users self-select.

### Claude integration via remote MCP

On the paid tier, the user talks to Claude in their **normal Claude client** (claude.ai web, desktop app, or mobile app — there is no chat UI inside Taskflow). Taskflow exposes its data via a remote MCP server reachable on the public internet. The user adds the MCP connector once via claude.ai on the web; from there, it syncs to every Claude client on the user's account.

Claude's behaviour through MCP is governed by `SYSTEM-PROMPT.md` (the system prompt the MCP server hands Claude on connection) — covering life-area exploration, project-suggestion etiquette, Strategy doc reconciliation, proactive Taskflow checks, and tone.

The user needs this because the value proposition is the deep integration into the user's full Claude environment — Taskflow showing up where they already chat with Claude about everyday issues. An in-app chat would be a smaller, weaker product even if it reached a wider audience. Routing through MCP commits Taskflow to a narrower audience of Claude users and accepts that as the correct positioning.

### Strategy doc

The Strategy doc is a single document that lives above Projects in the structure Claude sees. Its **structure is mechanically generated**: each Project gets a heading (the Project's name) and a paragraph (the user's description) underneath, in the order matching the Projects list in the side menu. There are no life-area headers. There are no time-zoom buckets. Reordering Projects in the side menu reorders the corresponding heading-and-paragraph pairs in the Strategy doc.

The doc reads roughly as a chronological-by-priority piece — for example, *"this Project is prioritised above all else,"* *"this Project is for in about six months' time after Project B is completed,"* *"this Project is indefinitely postponed"* — with the user composing the paragraphs under each Project's auto-generated heading.

The doc is reachable from the side menu but is not foregrounded in everyday navigation; Taskflow still presents primarily as a task app.

**On the free tier**, the user edits the descriptions directly in an in-app markdown editor; structure (headings + order) is auto-managed. There is no Claude reconciliation.

**On the paid tier**, the same editor is used. On the user's first opening of the Strategy doc area after switching to the paid tier (e.g., starting a trial), Claude reads the existing content and looks for tasks that contradict the Strategy or seem missing from it; it presents what it finds in groups and asks the user what to do — never silently editing. After the initial pass, every time the user submits an edit, Claude reconciles downstream tasks the same way.

A **share button** in the Strategy doc area lets the user share the doc (or a portion of it) via Android's standard share sheet — to a partner, parent, friend, or another app. Sharing the strategic picture with the people in the user's life is part of what gives a Strategy doc its function.

Life areas (e.g. Family, Work, Health) are not in the doc structure or anywhere in the UI. They are an abstract framing layer Claude carries (paid tier only) — see `SYSTEM-PROMPT.md` for how Claude builds and uses that picture.

The user needs this because long-range planning is real work the user does, and a structure that holds both moment-by-moment execution (Schedule) and decade-spanning intent (Strategy) without conflating them is the point of Taskflow. Mechanical structure means free-tier users still get a coherent doc shape without Claude in the loop, and paid-tier users get reconciliation rather than re-authoring. Sharing is what turns strategic intent from a private artifact into something the people who matter to the user can engage with.

### JSON export and import

A screen in Settings exports the user's full database — tasks, Projects, Strategy doc, ordering — to a JSON file. The same screen imports a JSON file produced by an export. Export and import are explicit, user-driven actions; nothing happens automatically. Android's Auto Backup is also active by default for all users (this is the OS-level feature that backs up app data to the user's own Google Drive and restores on reinstall) — Taskflow does not disable it.

The user needs this because portability matters even with no external integrations. JSON export/import gives every user explicit control over their data, including free-tier users who never get cloud sync, and provides a recovery path beyond Auto Backup for people who want it.

### Settings

The Settings screen is reachable from the side menu's bottom section (alongside Help, Thanks, and Report a bug — see *Side menu*). Settings holds the user-configurable controls that don't live on a task or a screen.

The user needs this because there is a small set of app-level controls (Day begins at, Date format, JSON export/import, AI tier) that need a home, and Settings is where Android users expect to find them.

### Settings → Day begins at

The Settings screen contains a single time picker called **Day begins at**. This is the only time picker in the entire app (UX principle 7). It controls when "today" rolls over for the user — used for the Tomorrow → Today rollover, for the calendar's "today" anchor in the side-scrolling date picker, and for any other place the app needs to know whether the user considers themselves to be in a new day yet.

The user needs this because people who stay up past midnight do not consider the day to have ended — a task they meant to do "today" at 1 AM should still be on Today, not Tomorrow. A single, configurable cutoff lets the user define their own day boundary without polluting the rest of the app with time pickers.

### Settings → Date format

A two-option setting for how dates are displayed throughout the app: **DD/MM** (default) or **MM/DD**. The setting applies everywhere a date is shown — on Schedule task rows, on date picker tiles, on the Strategy doc, anywhere.

The user needs this because date conventions vary by region and Taskflow ships with international users in mind. A single, central setting beats hard-coding one convention or trying to detect locale automatically.

### Side menu

A side menu opens from the left edge with three sections:

- **Top section — Schedule.** Today, Tomorrow, Soon, Later. Tapping a slot opens that screen.
- **Middle section — Projects.** The user's Projects, in the order the user has placed them. Tapping a Project opens its Project view. Visible even when empty (the empty state explains what Projects are for). Reordering Projects here also reorders the corresponding heading-and-paragraph pairs in the Strategy doc (see *Strategy doc*).
- **Bottom section — App actions.** Pinned to the bottom of the drawer, separated from the navigation: **Settings**, **Help**, **Thanks**, and **Report a bug**, plus a "turn on AI for the full experience" entry that re-triggers the AI choice flow on the free tier.

The user needs this because the four Schedule screens and the Projects list are both primary navigation surfaces and need to be reachable in one tap, but they are different questions and belong in their own sections. App-level actions belong off the Schedule and Project surfaces (which should be only about tasks) and the bottom of the menu is the obvious place for them.

---
*No-code method — Version 55.*
