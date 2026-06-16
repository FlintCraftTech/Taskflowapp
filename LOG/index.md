# LOG Index

One-line summaries of each session, newest first; each line names the session's full entry file in this folder. Maintained by Claude — /done writes one entry per completed session. Not for cover-to-cover reading — search and scan when you need the "why" behind a previous session's choices.

The entry format is documented in the comment block below.

<!--
Entry format (newest first):

## <Session> — YYYY-MM-DD — One-line summary

**What shipped.** Short plain-English paragraph describing concrete deliverables. Reference TEST-LOG row range rather than restating test outcomes.

**Decisions taken and why.** Two or three bullets on load-bearing decisions — what was chosen, alternatives considered, what tipped the call. Skip housekeeping; focus on choices shaping future sessions.

**Pivots and surprises.** Anything that turned out differently than the plan expected — a bug, a wrong assumption, an external fact discovered mid-build.

**Carried forward.** Items raised but not done, with destination (which capture, QUEUE entry, or "not pursued — reason").
-->

- [HASH] — Migrated docs to the Sovereign Implementer vocabulary (UX→SPEC, BACKLOG→QUEUE, MANIFEST→REGISTRY, build-log→LOG; docs to project root; FAQ/ + .si-version added; CLAUDE.md rewritten) and ran a planning pass: 18 carried-over questions → 3 batches (spec-edit of 7 SPEC decisions; SPEC-trim audit + plan marker; SYSTEM-PROMPT reconciliation rule), 6 parks, 4 drops, 1 new capture; verified build compiles (TEST-LOG #001 Fail→Pass). → migration-and-plan-2026-06-16.md
- `001-project-skeleton-and-room-data-model.md` — 0001 — 2026-05-24 — Project skeleton and Room data model

---
*No-code method — Version 55.*
