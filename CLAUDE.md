# CLAUDE.md

<!-- ▼ PLUGIN-MANAGED — do not edit between these markers. Updated on /setup and plugin reinstall. ▼ -->

This project uses the Sovereign Implementer method.

## Project docs

- **SPEC.md** — product truth. What it is, who it's for, how it works.
- **QUEUE.md** — work queue, top-to-bottom. Red flags (security, privacy, and breach risks Claude surfaced, kept at the top so they're seen first — each carries an open, resolved, or accepted state), Batches (Build/Test/Audit subheadings), Deferred tests (one line per planned test that couldn't run in its own session — source batch slug, what to verify, what confirms it with a runnability tail; /done writes entries here, /plan rolls the runnable ones into test batches, /done's close-out removes any line this session's activity already confirmed), Captures (split by `---` — processed above with slugs, raw appended below). Items removed from active flow carry `Blocked by:` (trigger-based) or `Parked:` (indefinite) headers. A `--- Plan session here: <reason> ---` marker between batches means /next halts there until a /plan session addresses the named reason.
- **REGISTRY.md** — components list. Updated after each build.
- **LOG/** — session records: what was built, tested, decided. One file per session entry, plus index.md one-line summaries naming each entry file.
- **FAQ/** — workflow FAQ. Index loaded at session start; details in FAQ/faq.md.

## Workflow

- `/setup` — scaffold project docs (done if you're reading this).
- `/plan` — queue management, captures, design questions.
- `/next` — execute the top batch (build, test, or audit). `/next freeform` does loosely-scoped work that isn't any of those — an ad-hoc change or a discussion of edits already made.
- `/done` — record, update docs, commit.

## Rules for Claude

- SPEC.md is read-only during builds. Edit it only during /plan.
- Only touch files listed in the active build scope. Halt and ask if you need more.
- One build at a time. Never start a second build while _build.md exists — finish and /done before starting another. (A planning session in a separate chat alongside a build is allowed.)
- State problems plainly. Don't hide them or silently fix unrelated things.
- Route discoveries to QUEUE.md rather than acting on them immediately.

## Language

Language: English

<!-- ▲ PLUGIN-MANAGED — do not edit above this line. ▲ -->

## Project rules

**Building on Windows:** this machine hits Gradle's "Unable to delete directory … build" lock (Gradle's file-system watching, and/or Defender, holding handles on `build/`). Build with `--no-watch-fs --no-daemon` — e.g. `.\gradlew.bat :app:assembleDebug --no-watch-fs --no-daemon`. If a stale lock persists, stop the daemon (`.\gradlew.bat --stop`) and delete `app\build`. Verified working 2026-06-16.

**Additional source-of-truth doc — `SYSTEM-PROMPT.md`:** the system prompt the remote MCP server hands Claude on connection (paid tier only). It covers life-area exploration, project-suggestion etiquette, Strategy doc reconciliation, proactive Taskflow checks, and tone. Treat it as locked during builds the same way SPEC.md is — the scope hook does not auto-lock it, so this is a rule to follow, not an enforced one: don't edit it during a build unless it's named in `_build.md`'s Files: list. Planning resolutions that describe SYSTEM-PROMPT.md behaviour fold into it rather than SPEC.md. Build batches that change its domain carry a `Serves SYSTEM-PROMPT.md: ...` line.

**Additional record doc — `TEST-LOG.md`:** a table of test outcomes per shipped build batch, one row per test, maintained by Claude during builds and planning. It predates this project's move to LOG/-based session records; it's kept as the running test history. New per-session test results are recorded in the LOG/ entry; reflect material test outcomes (pass/fail/skip) in TEST-LOG.md too when a build or test batch runs tests.

**Archived backlog specs — `archive/backlog-specs/`:** the detailed per-batch spec files (and the original backlog `INDEX.md`) from before this project adopted the single-file QUEUE.md. The summary of each lives in QUEUE.md's Batches; the archived file is the full original spec. Reference material — not maintained going forward.

**Migration note:** this project was migrated from the method's older document vocabulary (UX.md → SPEC.md, BACKLOG/ → QUEUE.md, MANIFEST.md → REGISTRY.md, build-log/ → LOG/). Docs now live at the project root, where the current plugin's hooks expect them; the old per-project path-block JSON is no longer used.
