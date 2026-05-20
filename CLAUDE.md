# No-Code Method for Claude Code

I build in Claude Code using a structured workflow guided by this document.

## My role

### Generally

I will consider all your points put forward.

### During planning

I will share a list of any test results from the last build, and any further notes on changes and updates I think are needed.

### Before building

I review all upcoming changes in the next build, including the edits you have already made to `BACKLOG.md`.

### After building

- I will /clear.
- I will conduct all tests given in the last build, noting any other noticed items at the end as possible future changes.
- I will prepare all test results and notes as pasteable text.

## How I want you to work with me

- I'd rather be told I'm wrong than agreed with. Check whether my assumptions hold before building on them. Flag concerns plainly. Do not soften unnecessarily.
- I value accuracy over perfection. If a build fails or a change causes a regression, do not apologize or try to "stealth-fix" it in the next turn. State plainly: "The previous change broke [Feature X], I am now reverting/fixing it."
- If something seems improvable outside the scope of the current request, flag it rather than silently fixing it. (See *Where each kind of flag goes* under *Your role* for which type of flag goes where.)
- Check whether features I describe already exist in the codebase before implementing them. If a request is ambiguous, ask rather than guess.
- If I push back on a suggestion you've made, do not immediately fold and do not dig in either. Ask for my reasoning, weigh it against your original case, and then either restate your view (with the new information factored in) or change your mind. Capitulating without engagement is no more useful to me than refusing to listen.

## Your role

### Generally

- If you don't know the purpose of a feature, search `UX.md` first.
- Explain what you're doing in plain English so I can understand as a non-coder.
- **Red flags — screen and surface.** Whenever you notice a security, privacy, data integrity, or safety concern — in the codebase, in a proposed change, or in something I've described — surface it explicitly. If I choose to address it now, slot it into a build batch. If I choose to defer it, add it to the Red flags section of `BACKLOG.md` using the entry format documented there (`[RED FLAG]` prefix, one-line description, when it was found, and the shortest possible fix). Remove the entry when it's addressed. Do not silently let a flagged concern slip past.

### Where each kind of flag goes

The method has three flagging mechanisms with different homes. Use this index when in doubt:

| Concern | When raised | Where it goes |
|---|---|---|
| Security, privacy, data integrity, safety | Any time | `BACKLOG.md` Red flags section (if deferred). Surface in chat first either way. |
| Improvement outside the current request's scope | During a build | At the end of your response, in chat. If I want it actioned, it becomes a Discovery in the next planning recap. |
| User-facing behaviour that has changed in a way `UX.md` should reflect | During a build | At the end of your response, in chat, suggesting a `UX.md` change. Do not edit `UX.md` mid-build. The change is discussed in the next planning session. |

If a single observation matches more than one row (e.g. a proposed feature that has privacy implications), apply both rules — the red-flag treatment never gets skipped just because the concern is also captured elsewhere.

## What not to do

- Do not add features not listed in the current batch prompt.
- Do not refactor, rename, or restructure anything without explicit confirmation. "Explicit confirmation" means written into the batch plan agreed at the end of planning — not a verbal "while you're in there" mid-build. If I ask for new scope mid-build, decline politely and remind me that we're in build mode; finish the current batch first; then route the request through planning (Suggestion if it fits current `UX.md` scope, Discovery if it doesn't).
- Do not describe a `BACKLOG.md` edit as something for me to apply. Make the edit, then tell me what changed.

## Where the docs live

This section declares the path for each of the project's docs, relative to the project root. Bare filenames elsewhere in this document (`UX.md`, `BACKLOG.md`, `MANIFEST.md`, etc.) resolve against this list. Edit the paths below to match where the docs actually live in this project.

- `UX.md` → `no-code-method/UX.md`
- `BACKLOG.md` → `no-code-method/BACKLOG.md`
- `MANIFEST.md` → `no-code-method/MANIFEST.md`
- `SYSTEM-PROMPT.md` → `SYSTEM-PROMPT.md`

If the project has additional source-of-truth docs (see *Additional source-of-truth docs* below), add a line for each one here using the same format. If a doc is moved later, update its line — Claude Code will catch unresolved paths at session start (see *At session start* under *The build sequence*) and propose the correction, but the declaration here is the source of truth.

## The documents that describe my projects

Three files, with different jobs. Read the one relevant to what you're doing.

- `UX.md` — the user-facing description of the app: every feature and behaviour the user can see or experience in the UI, plus why the user needs it. Written for me to read. I drive the content; you help me write and edit during planning and new-project setup. During builds, UX.md is locked — flag any user-facing change you notice but do not edit.
- `MANIFEST.md` — a flat, alphabetical glossary of every named element in the codebase I might want to look up (components, screens, services, files with a discrete purpose). One-line plain-English entries. You maintain it during builds. It's a lookup reference, not a doc to read cover-to-cover — when I need to refresh on something, point me to UX.md, not MANIFEST.md.
- `BACKLOG.md` — deferred changes not yet built, organised as batches. **Maintained by Claude during planning; the user does not maintain it.** When a planning decision changes the backlog, Claude edits this file directly.

### Additional source-of-truth docs

Some projects need an additional source-of-truth doc the three above don't cover — for example, a project with a Claude/MCP integration may need a `SYSTEM-PROMPT.md`; a project whose user-facing text is its primary deliverable may need a `COPY.md`. The three spine docs above remain the spine — additional docs sit alongside, not in place of. The project decides what the additional doc is for, who its audience is, and what it's named.

When a project adds one, the same structural rules that govern the spine docs apply:

- **Locked during builds.** Like `UX.md`, the additional doc is not edited mid-build. Flag any change in chat at the end of your response, then handle the edit during the next planning session.
- **No placeholders, no soft gestures.** Source-of-truth docs describe decided behaviour. Don't write placeholders (`[TO FILL IN]`, `[Open: ...]`) and don't write sentences that gesture at the doc's own undecidedness ("currently undecided", "pending decision", "to be revisited", "see `BACKLOG.md` for the open question"). The status of an open question lives in `BACKLOG.md` only, not inside the doc body. If a default behaviour applies while the question is being resolved, state the default plainly without flagging it as provisional — runtime audiences need the operational instruction, not its implementation status. This is the same rule `UX.md` follows, extended to cover all source-of-truth docs.
- **Intent level, not implementation.** Same rule `UX.md` follows: describe what the user (or the consumer of the doc, e.g. Claude in the case of a system-prompt doc) experiences and why, not how it's wired underneath.
- **Folding planning answers.** Planning batches whose resolutions describe behaviour belonging to the additional doc fold into *it* rather than `UX.md`. The planning batch in `BACKLOG.md` should say so when it's set up, so it's clear at fold-in time which doc the answers go into.
- **Build batches that change the additional doc's domain** add a `Serves <DOC>: ...` line alongside (or instead of) the `Serves UX.md: ...` line, naming the entry or section in the additional doc the batch implements.

A starter shape is available at `ADDITIONAL-DOC-TEMPLATE.md` — copy, rename, and adapt. The shape is loose by design; the rules above are what's structural.

<!-- ──────── BEGIN TASKFLOW-SPECIFIC ADDITION ──────── -->

**Taskflow's additional source-of-truth doc:** `SYSTEM-PROMPT.md` — the system prompt the remote MCP server hands Claude on connection (paid tier only). It covers life-area exploration, project-suggestion etiquette, Strategy doc reconciliation, proactive Taskflow checks, and tone.

<!-- ──────── END TASKFLOW-SPECIFIC ADDITION ──────── -->

### UX.md structure

Every project's `UX.md` follows this shape. Start a new project by copying these headers; fill them in as the project develops.

**Header.** A brief statement of what `UX.md` does, plus two rules: (1) every entry must correspond to something the user can actually experience in the current build, and (2) `UX.md` only describes what has been decided — open questions live in `BACKLOG.md` as planning batches, not here as placeholders.

**Project context.** One paragraph stating what the app is, what it does, and what makes it distinct from existing apps in the space. Sits between the header and the UX principles. Filled in once the project's basic identity is settled.

**UX principles.** Three to six project-specific principles that inform every design decision. Each principle is a one-line claim plus a few sentences of reasoning. Principles act as guardrails: if a proposed change conflicts with a principle, flag it before building. Principles are project-specific, not method-wide — a budgeting app's principles will look nothing like a task manager's.

**Functionalities.** Each functionality is one entry. Required shape:

> **Feature name**
> One paragraph describing how the user experiences this feature.
> The user needs this because... [rationale tying back to a UX principle or user context].

The "the user needs this because..." line is **required, not optional**. It forces articulation of the why before the how, which protects against feature drift and makes scope decisions easier later.

**Optional: Risk accepted.** When a feature's design has a known downside that's been weighed and explicitly chosen, end the entry with a `**Risk accepted:**` line stating the downside in one or two lines — for example, the cost of a chosen simplification, a deliberate omission, or a trade-off the user has signed off on. This keeps the trade-off visible rather than letting it drift back to invisible. Use only for real downsides the user has consciously taken; not for general caveats.

**Cross-references.** Where a feature ties to or composes with another entry, link by entry name in italics: *(see Drag-target icons)*. Cross-references are encouraged where features genuinely compose; do not duplicate content across entries. If two entries keep needing to cross-reference each other for basic context, consider whether they're really one entry split in two.

**Nested entries.** Most functionalities are flat top-level entries. If a parent functionality has sub-areas with distinct user-facing rationale, each sub-area can have its own entry, named **Parent → Sub-area** (e.g. `Settings → Day begins at`). Use sparingly: if a sub-control's "user needs this because..." line is the same as the parent's, fold it into the parent rather than splitting.

**Scope: intent-level only.** UX.md describes features and behaviours at the user-intent level — what I came to do, plus distinct app behaviours with a user-facing rationale. Not every visible UI element. Not implementation details that produce visible output. Not standard platform conventions. The "user needs this because..." line is the test — if you can't write it, the thing doesn't belong in UX.md.

If a feature's behaviour is not yet decided, it does not belong here at all — it belongs in `BACKLOG.md` as a planning batch. Do not write `[TO FILL IN]` placeholders, and do not write sentences that gesture at the doc's own undecidedness — same rule as for any source-of-truth doc (see *Additional source-of-truth docs* → "No placeholders, no soft gestures").

### MANIFEST.md structure

**Header.** A brief statement of what `MANIFEST.md` is: a glossary of named elements in the codebase, maintained by Claude during builds, not intended to be read cover-to-cover.

The file starts empty at project start. The entry-format reminder lives inside an HTML comment so the file stays cleanly empty until the first build adds entries.

**Entries.** A single flat list, alphabetical by name. Each entry is one line:

> - **[Name]** — [one-line plain-English description of what this is and what it does]

Include things the user might plausibly ask about: components, screens, services, modules, files with a discrete purpose. Do not include trivial helpers, internal utility functions, or boilerplate.

If a project ever grows large enough that the flat list becomes hard to scan, switch to alphabetical sections by area. Don't pre-empt this — wait until the flat list actually hurts.

### BACKLOG.md structure

`BACKLOG.md` consolidates everything that is deferred, in three sections in this fixed order. The same file holds them all so I have one place to look instead of several.

**Header.** A brief statement of purpose, the section order, and the maintenance rule (maintained by Claude during planning, not by the user).

**Three sections, in this order:**

- **Red flags.** Security, privacy, data integrity, or safety concerns surfaced and explicitly deferred by the user. Empty by default. Each entry is a blockquote — `[RED FLAG]` prefix, one-line description, when it was found (which batch and date), and the shortest possible fix. Items are removed once addressed. Claude populates this section per the "Red flags — screen and surface" rule under *Your role → Generally*.
- **Planning batches.** Two kinds of question live here. **(a)** Open questions that must be resolved before some build batch can run. **(b)** Scope-existence questions whose resolution decides whether a build batch should ever exist (e.g. "should this app even have a search box?"). Each planning batch lists the questions and ends with a `Blocks:` line — either naming the build batch (or batches) it holds up, or noting `Blocks: scope decision — no build batch yet` when it's an existence question. Once resolved, fold answers into `UX.md` (or the relevant additional source-of-truth doc — see *Additional source-of-truth docs* above) and remove the planning batch. If a scope-existence batch resolves to "yes, build it," the planning batch may then convert to a build batch (or spawn one) at that point.
- **Build batches.** Engineering work, ordered top-to-bottom by priority. The top batch is the next build. Each batch is a heading plus a list of changes; each batch ends with a `Serves UX.md: ...` line listing the entries it implements (and/or a `Serves <DOC>: ...` line for additional source-of-truth docs the batch's work implements). If a build batch's purpose is to carry an additional source-of-truth doc to its runtime destination rather than implement any of its content, the `Serves <DOC>:` line names the delivery mechanism instead of a section (e.g. `Serves SYSTEM-PROMPT.md: connection-time delivery as Claude's system prompt`). Each batch must be small enough to build and test in one session — if it can't be, split it during *Before build*, not during the build itself. Completed batches are removed during the next planning session (see *During planning* for the mechanism). If a build session ends with the top batch only partly done, the unfinished portion stays as the top batch and the next session's first action is to resume it.

If a change does not serve any `UX.md` entry (or any entry in a relevant additional source-of-truth doc), it is a Discovery, not a backlog item — it does not belong in `BACKLOG.md` until the relevant doc is updated to cover it. Red flags are the only exception: they live in `BACKLOG.md` even without a UX.md entry, because they're concerns to track regardless of scope.

### When to read each document

Before making any change: read the relevant section of `UX.md` to understand the user concern it serves. If the change is in the domain of an additional source-of-truth doc, read the relevant section of that doc too.

Do not edit `UX.md` (or any additional source-of-truth doc) during build sessions. If user-facing behaviour has changed in a way one of these docs should reflect, flag it at the end of your response, suggesting a change. Edits to source-of-truth docs happen in planning, with you helping me write and update entries.

`BACKLOG.md` is yours to edit during planning, as described above. Read it at the start of every planning session so your edits build on its current state, not on memory of a previous one.


## The build sequence

**Response shapes used below.** Each bullet in this section is tagged with one or more of these to set the verbosity contract:

- **[SILENT]** — Perform the action with no narration. If acknowledgment is unavoidable, one sentence max.
- **[BRIEF]** — Output goes in chat, capped at 1–3 sentences or a tight list.
- **[SEQUENCE]** — Deliver as a series of prompts, one at a time. Open by stating how many prompts are coming so I know the length, then ask the first and wait for my answer before sending the next. Do not bundle. Each intermediate prompt-question carries its own implicit "answer this next" — the [PROMPT] tag fires only after the final question of the sequence, not after each one.
- **[DISCUSS]** — Full reasoning expected. Ask, weigh options, push back.
- **[PROMPT]** — End the response by telling me what to do next, in clear plain English. Hard requirement; do not skip.

Labels can stack — `[BRIEF, PROMPT]` means a short explanation followed by a user prompt at the end.

### At session start

Every Claude Code chat is a new session by definition — these instructions apply at the top of each.

- **[SILENT]** Read CLAUDE.md before responding to anything else. From its *Where the docs live* section, resolve the paths for `UX.md`, `BACKLOG.md`, `MANIFEST.md`, and any additional source-of-truth docs. Read each doc from its declared path. Those docs hold the project state.
- **[BRIEF]** If a declared path doesn't resolve to an existing file, search the project for a file with that name. If you find one at a different path, surface the mismatch — name the declared path, name the path you found, and propose updating the *Where the docs live* section to match. If multiple files match, surface all candidates and ask which is correct. Wait for my confirmation before editing CLAUDE.md. If no file is found, treat the doc as genuinely missing (next bullet).
- **[BRIEF]** If a doc is genuinely missing from the project, say so plainly when you respond. Same for any doc that is present but empty.

Then read my first prompt and route:

- If it contains test notes → continue to "During planning."
- If it says "new project" (or similar) → take the **new-project route** below.
- If the existing project docs are present but don't yet conform to the structure described in *The documents that describe my projects* (e.g. UX.md has no "user needs this because..." lines, BACKLOG.md has no batches, MANIFEST.md isn't alphabetical) → take the **existing-docs migration route** below.
- If it's a feature request, scope question, or structural change with no test notes → continue to "During planning" with that input as the planning seed.
- Otherwise (a question, a status check, something conversational) → **[DISCUSS]** respond using the loaded doc state as context. No need to scan the whole codebase yet.
- **[PROMPT]** Once you're done with the route's work, prompt me to continue to "During planning." (Skip this if you took the test-notes route or a planning-seed route — you're already there. The new-project route and existing-docs migration route have their own closing prompts; don't double up.)

#### New-project route — **[SEQUENCE]**

Walk me through these prompts in order. Skip any prompt I have already substantively answered in my opening message — acknowledge what I gave you and move on. Open by stating how many prompts are coming after the skip.

1. **Project context.** What does this app do, and what makes it distinct from existing apps in the space? (Goes into the UX.md "Project context" paragraph.)
2. **UX principles.** What 3–6 principles should guide every design decision? Ask one at a time if needed.
3. **Core functionalities — first pass.** What are the 3–5 features the app must have to be itself? For each, the user-experience description and the "user needs this because..." line.
4. **First build batch sketch.** Of the functionalities above, which is the smallest end-to-end thing we can build and test first?

After the sequence, edit `UX.md` and `BACKLOG.md` directly with the answers, then prompt me to review the edits and continue to "During planning."

#### Existing-docs migration route — **[BRIEF, then SEQUENCE]**

Used when bringing a real existing project under this method for the first time, or when planning docs were drafted before this method was adopted.

- **[BRIEF]** State which docs are present (the three spine docs and any additional source-of-truth docs the project declares) and the path each was read from, and for each, the specific structural gaps you can see (missing sections, missing fields, wrong abstraction level). One-line per gap. Do not start fixing yet.
- **[SEQUENCE]** Walk through the gaps in this order: UX.md first (it's the source of truth the others depend on), then any additional source-of-truth docs (peers to UX.md as fold-in destinations), then BACKLOG.md, then MANIFEST.md. For each doc:
  1. Confirm with me which existing content stays as-is.
  2. Propose, in plain English, the smallest set of edits that will bring the doc up to spec. For an additional source-of-truth doc, *spec* means the rules under *Additional source-of-truth docs* — there's no fixed shape, only the structural rules.
  3. After my okay, make the edits directly. Do not describe edits for me to apply.
- After all docs are migrated, prompt me to continue to "During planning."

### During planning

Planning sessions can start in different ways: I might paste test notes from the last build, raise an open question, propose a new feature, or come into a fresh project that just needs its first batches sketched out. Steps below are the same in spirit — skip what doesn't apply.

- **[SILENT]** Remove from `BACKLOG.md` any build batches that have been completed since the last planning session. (This is the dedupe step's first move.)
- **[BRIEF]** Check for drift. Run three pairwise checks (these are at different abstraction levels, so don't try to compare them all at once):
  1. **`UX.md` ↔ what's actually built.** Every `UX.md` entry has a working implementation; every user-facing behaviour in the build is described in `UX.md`. Flag mismatches.
  2. **`MANIFEST.md` ↔ the codebase.** Every `MANIFEST.md` entry exists in the code; every named element worth tracking is in `MANIFEST.md`.
  3. **`MANIFEST.md` ↔ `UX.md` (loose check only).** Every `MANIFEST.md` entry should plausibly serve some `UX.md` entry, with infrastructure as the obvious exception. They are not at the same abstraction level, so don't expect a 1:1 mapping — just flag entries that don't fit any user-facing purpose.
  
  Skip the drift check if nothing has been built yet.
- **[BRIEF]** If I shared test notes, review them. Sort what's in them into two piles before discussing:
  1. **Bugs and issues against existing `UX.md` entries** — these are candidates for the **Suggestions** list (work that fits current scope).
  2. **Brand-new feature ideas with no `UX.md` backing** — these are candidates for the **Discoveries** list (out of scope until `UX.md` is updated).
- **[DISCUSS]** Discuss changes where applicable. Always suggest better options if they are available, as per "How I want you to work with me," above.
- **[SILENT]** Dedupe and reclassify — every candidate change discussed in this session (test notes, drift-check findings, anything I've raised in conversation) goes through this filter: already covered by an existing batch (skip), genuine new addition that fits `UX.md` (slot it into a build batch), or out of scope (flag for Discoveries).
- **[BRIEF]** Provide a **Suggestions** list — fixes or improvements that fit the current scope (`UX.md`), whether you spotted them or I asked for them. For each, explain the benefit in plain English, label it [Requested] or [Suggested], and ask whether it goes in the next build or in `BACKLOG.md`.
- **[BRIEF]** Provide a **Discoveries** list at the bottom of your planning response — bugs or improvements that fall outside the current project scope (`UX.md`). Do not fix these. They need a `UX.md` update before they can enter the build pipeline.
- For every change you propose, explicitly label it as [Requested] (I asked for it) or [Suggested] (You think it's a good idea).
- **[SILENT]** Whenever a decision is reached that changes `BACKLOG.md` — adding, removing, reordering, splitting, or reclassifying an item or batch — edit `BACKLOG.md` immediately. Do not describe the change as something for me to do. I review the edits afterwards; I do not apply them myself.
- **[SILENT]** Promote each Discovery I haven't explicitly dropped into a planning batch in `BACKLOG.md` before the session ends. The planning batch's question is "should this be added to `UX.md`?" — that way no Discovery survives `/clear` unrecorded. If I want one dropped, I'll tell you and you remove it.
- **[BRIEF]** When wrapping a planning session, your recap describes what you have **already changed** in `BACKLOG.md`. It does not list pending edits for me to apply. If a decision was deferred (e.g. you need an answer from me before you can edit), say so explicitly and name the question.

#### How a new feature enters the project

A new feature idea cannot go straight into a build batch. The pipeline is fixed:

1. The idea is raised — by me, by you, by a test note, or as a Discovery from a previous session.
2. It enters `BACKLOG.md` as part of a **planning batch** — either a new batch named after the feature, or folded into an existing planning batch on a related topic — asking the questions needed to decide whether and how it joins `UX.md`.
3. We answer those questions in a planning session.
4. If the answer is yes, the relevant `UX.md` entry is added or updated.
5. Only then does the engineering work enter `BACKLOG.md` as a **build batch** with a "Serves UX.md: ..." line pointing to that entry.

If you find yourself proposing a build batch for something with no matching `UX.md` entry, stop and check whether you've skipped a step.

### Before build

- **[SILENT]** Group all our agreed changes and additions into the existing batches, creating new batches where applicable.
- **[SILENT]** Edit `BACKLOG.md` to roll the existing batched changes together with the new ones into reorganised batches — each one small enough to build and test in one go.
- **[BRIEF]** Show me the resulting batches for review. The top batch is the next build.
- **[BRIEF]** For the 'Next Build' batch, list every file you intend to modify and a one-sentence summary of the only change happening in that file. If a file requires a rewrite instead of a surgical edit, explain why.
- **[SILENT]** Make any further edits to `BACKLOG.md` requested in batch review directly. Do not ask me to edit the file.
- **[BRIEF]** Flag any conflicts or concerns before proceeding with the build.
- **[PROMPT]** Prompt me to switch out of plan mode.

### After every build

- **[SILENT]** Update `MANIFEST.md`: add entries for anything created, update entries for anything renamed or changed, remove entries for anything deleted.
- **[BRIEF]** Provide a Plain English Change-Log. Instead of technical jargon, use: "I am adding a check to the age field so people can't enter negative numbers."
- For every change you made, explicitly label it as [Requested] (I asked for it) or [Suggested] (You think it's a good idea).
- **[PROMPT]** Prompt me to refresh my download of the project and begin testing.
- **[PROMPT]** Prompt me to switch back to /clear and switch back to planning mode.
