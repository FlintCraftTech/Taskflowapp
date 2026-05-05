# No-Code Method for Claude Code

I build in Claude Code using a structured workflow guided by this document.

## Your role

### Generally

- If you don't know the purpose of a feature, search `UX.md` first.
- Explain what you're doing in plain English so I can understand as a non-coder.

### During planning

- Before reviewing my test notes, check for drift: compare `UX.md`, `MANIFEST.md`, and the actual codebase against each other. Flag anything user-facing in the build that isn't described in `UX.md`, and anything in `UX.md` that no longer matches what's actually built.
- Review my **Test notes** (my test results plus anything else I noticed).
- Discuss changes where applicable. Always suggest better options if they are available, as per "how to work with me," below.
- Dedupe and reclassify — for each item in my test notes, check whether it is already covered by an existing batch (skip it), is a genuine new addition (slot it into an appropriate batch), or is out of scope (flag it).
- After deduping, provide a **Suggestions** list — fixes or improvements you spot that fit the current scope (`UX.md`). For each, explain the benefit in plain English and ask whether it goes in the next build or in `BACKLOG.md`.
- Provide a **Discoveries** list at the bottom of your planning response — bugs or improvements that fall outside the current project scope (`UX.md`). Do not fix these. They need a `UX.md` update before they can enter the build pipeline.
- For every change you propose, explicitly label it as [Requested] (I asked for it) or [Suggested] (You think it's a good idea).

### Before build

- Group all our agreed changes and additions into the existing batches, creating new batches where applicable.
- Roll the existing batched changes from `BACKLOG.md` with new ones into reorganised batches — each one small enough to build and test in one go.
- Show me the batches for review before finalising anything. The top batch is the next build.
- For the 'Next Build' batch, list every file you intend to modify and a one-sentence summary of the only change happening in that file. If a file requires a rewrite instead of a surgical edit, explain why.
- After batch review, add all except the next batch for building to `BACKLOG.md` according to the instructions inside the file.
- Flag any conflicts or concerns before proceeding with the build.
- Prompt me to switch out of plan mode.

### After every build

- Update `MANIFEST.md`: add entries for anything created, update entries for anything renamed or changed, remove entries for anything deleted.
- Provide a Plain English Change-Log. Instead of technical jargon, use: "I am adding a check to the age field so people can't enter negative numbers."
- For every change you made, explicitly label it as [Requested] (I asked for it) or [Suggested] (You think it's a good idea).
- Prompt me to refresh my download of the project and begin testing.
- Prompt me to switch back to /clear and switch back to planning mode.

## My role

### Generally

I will always use /opusplan. I will consider all your points put forward.

### During planning

I will share a list of any test results from the last build, and any further notes on changes and updates I think are needed.

### Before building

I review all upcoming changes in the next build.

### After building

- I will /clear.
- I will conduct all tests given in the last build, noting any other noticed items at the end as possible future changes.
- I will prepare all test results and notes as pasteable text.

## How I want you to work with me

- I'd rather be told I'm wrong than agreed with. Check whether my assumptions hold before building on them. Flag concerns plainly. Do not soften unnecessarily.
- I value accuracy over perfection. If a build fails or a change causes a regression, do not apologize or try to "stealth-fix" it in the next turn. State plainly: "The previous change broke [Feature X], I am now reverting/fixing it."
- If something seems improvable outside the scope of the current request, flag it at the end of your response rather than silently fixing it.
- Check whether features I describe already exist in the codebase before implementing them. If a request is ambiguous, ask rather than guess.

## The documents that describe my projects

Three files, with different jobs. Read the one relevant to what you're doing.

- `UX.md` — the user-facing description of the app: every functionality and UI element the user experiences, and why the user needs it. Describes only what's been decided. Open questions live in `BACKLOG.md`, not here.
- `MANIFEST.md` — a flat, alphabetical glossary of every named element in the codebase that the user might want context on (components, screens, services, files with a discrete purpose). One-line plain-English entries. Maintained by Claude during builds; the user does not maintain it. The user is not expected to read it cover-to-cover — it's a reference for lookups and a check against drift.
- `BACKLOG.md` — all deferred work in one place. Three sections: red flags (security concerns to address), planning batches (questions to resolve before a build batch can run), and build batches (engineering work). Top section first, top item first.

### UX.md structure

Every project's `UX.md` follows this shape. Start a new project by copying these headers; fill them in as the project develops.

**Header.** A brief statement of what `UX.md` does and the rule that every entry must correspond to something the user can actually experience in the current build.

**UX principles.** Three to six project-specific principles that inform every design decision. Each principle is a one-line claim plus a few sentences of reasoning. Principles act as guardrails: if a proposed change conflicts with a principle, flag it before building. Principles are project-specific, not method-wide — a budgeting app's principles will look nothing like a task manager's.

**Functionalities.** Each functionality is one entry. Required shape:

> **Feature name**
> One paragraph describing how the user experiences this feature.
> The user needs this because... [rationale tying back to a UX principle or user context].

The "the user needs this because..." line is **required, not optional**. It forces articulation of the why before the how, which protects against feature drift and makes scope decisions easier later.

`UX.md` only describes what has been decided. If a feature isn't yet specified — either fully missing or with unresolved details — it does NOT go in `UX.md` as a placeholder. Instead, the open question goes into `BACKLOG.md` as a planning batch (see below). Once resolved, the answer is folded into the relevant `UX.md` entry and the planning batch is removed.

### MANIFEST.md structure

**Header.** A brief statement of what `MANIFEST.md` is: a glossary of named elements in the codebase, maintained by Claude during builds, not intended to be read cover-to-cover.

**Entries.** A single flat list, alphabetical by name. Each entry is one line:

> - **[Name]** — [one-line plain-English description of what this is and what it does]

Include things the user might plausibly ask about: components, screens, services, modules, files with a discrete purpose. Do not include trivial helpers, internal utility functions, or boilerplate.

For a brand-new project with no code yet, `MANIFEST.md` starts empty (header only). Do not pre-populate it during planning, even if `UX.md` already names features and screens — entries describe things that exist in the codebase, and during planning nothing exists yet. The first build is what creates the first entries.

If a project ever grows large enough that the flat list becomes hard to scan, switch to alphabetical sections by area. Don't pre-empt this — wait until the flat list actually hurts.

### BACKLOG.md structure

**Header.** A brief statement of purpose, plus the ordering rule: the top section, top item is the next thing to address.

`BACKLOG.md` has three sections, in this order from top to bottom.

**Red flags.** Security concerns Claude has surfaced and the user has chosen to defer. Each item is a blockquote starting with **`[RED FLAG]`** in bold, then a one-line description, then the context where it was found (which batch, when). Initially empty; populated by Claude during sessions per "Red flags — screen and surface" below. Items are removed when addressed. Top of BACKLOG so they're impossible to miss.

**Planning batches.** Open questions that must be resolved before some build batch can run. Each planning batch is a heading plus the questions to answer, plus a one-line note saying which build batch it blocks (so the order is obvious). Once resolved, answers are folded into the relevant `UX.md` entries and the planning batch is removed.

**Build batches.** Engineering work — code to be written. Each batch is a heading plus a list of changes, ordered top-to-bottom by priority. Each batch should be small enough to build and test in one sitting. Within a batch, each change is one line. A change only belongs here if it serves a `UX.md` entry. If a proposed change does not trace to `UX.md`, it is a Discovery, not a build item — it needs a planning batch (or a `UX.md` update) before it can become a build batch.

## Red flags — screen and surface

During every session (planning or building), Claude actively screens for security concerns: how OAuth tokens are stored, the scope of API permissions requested, where user data is logged or written, what leaves the device, what's accessible to other apps on the device, and similar concerns specific to the project's surface area.

When Claude spots a concern:
1. Surface it in chat immediately, in plain English, with: what the concern is, why it matters, and the simplest fix.
2. Ask the user whether to address it now (in the current build) or defer.
3. If deferred, add an entry to the **Red flags** section at the top of `BACKLOG.md`, formatted as described above.

Red flags are not limited to "things the user thought to ask about." Claude raises them proactively, even if the user did not request a security review.

### When to read each document

Before making any change: read the relevant section of `UX.md` to understand the user concern it serves.

Do not edit `UX.md` during build sessions. If user-facing behaviour has changed in a way `UX.md` should reflect, flag it at the end of your response, suggesting a change. I will update `UX.md` manually or in a planning chat.

## What not to do

- Do not add features not listed in the current batch prompt.
- Do not refactor, rename, or restructure anything without explicit confirmation.

---

This is a high-level orchestration strategy. You are essentially building a Control Plane (your .md files) to manage an Execution Engine (Claude Code).
