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

- [HASH] — Audit SPEC.md against keep-vs-relocate criteria — routed 20 per-section findings to Captures (no SPEC edits) → audit-spec-trim.md
- 86d521b — SPEC.md — folded 2026-06-16 planning decisions into seven sections: Strategy doc added as a calm top row of the Projects side-menu section (above individual Projects); Project-view top card collapsed by default (expand opt-in); date-strip gained 5 details (centre-on-date open, ~1mo-past–12mo-future range + month-jump, capped-linear fade, left-edge labelled "no date" tile, 5–7 tiles visible); Day-begins-at default set to 4:00 AM; completed tray clears at the day-begins-at rollover; paused subscription reverts to local-only with device DB as source of truth; new scope note — no notifications in v1 (foreground-only, per principles 4 & 7). → spec-edit-planning-2026-06-16.md
- 1e4b29a — Migrated docs to the Sovereign Implementer vocabulary (UX→SPEC, BACKLOG→QUEUE, MANIFEST→REGISTRY, build-log→LOG; docs to project root; FAQ/ + .si-version added; CLAUDE.md rewritten) and ran a planning pass: 18 carried-over questions → 3 batches (spec-edit of 7 SPEC decisions; SPEC-trim audit + plan marker; SYSTEM-PROMPT reconciliation rule), 6 parks, 4 drops, 1 new capture; verified build compiles (TEST-LOG #001 Fail→Pass). → migration-and-plan-2026-06-16.md
- `001-project-skeleton-and-room-data-model.md` — 0001 — 2026-05-24 — Project skeleton and Room data model

---
*No-code method — Version 55.*
