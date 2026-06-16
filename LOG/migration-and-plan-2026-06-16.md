# 1e4b29a — Migrated docs to the Sovereign Implementer vocabulary, then planned 18 carried-over questions into SPEC decisions, three batches, six parks, and a SPEC-trim audit

This session did two things. **First, a vocabulary migration**: the project moved from the no-code-method's older document names to the current structure — UX.md → SPEC.md, BACKLOG/ (a folder of 22 specs + 17 planning questions) collapsed into one QUEUE.md, MANIFEST.md → REGISTRY.md, build-log/ → LOG/. All docs moved from `no-code-method/` to the project root, because the current plugin's hooks hardcode root paths (the old per-project path-block in CLAUDE.md is a dead feature now). Scaffolded FAQ/ and .si-version; rewrote CLAUDE.md's plugin-managed block while preserving the project rules; archived the 22 original backlog specs under `archive/backlog-specs/`; kept SYSTEM-PROMPT.md and TEST-LOG.md as extra source-of-truth docs; fixed internal doc-name references throughout.

**Second, a planning pass** over the 17 carried-over design questions plus the build-failure note. The throughline: most were already answered by the spec, genuinely far-future (parked), or small product-truth decisions that fold into SPEC. One realisation drove the session's biggest output — the migrated SPEC *is* the old exhaustive UX.md, but SPEC is meant to be product truth, not a UX manual — producing a SPEC-trim audit batch.

Verified the app builds: **TEST-LOG #001 moved Fail → Pass** — the duplicate kotlin-android plugin declaration was already fixed in the working tree, and `assembleDebug` compiles (on Windows with `--no-watch-fs --no-daemon` to dodge a build-folder lock; recorded in CLAUDE.md).

**Queue changes:**
- Created **[spec-edit-planning-2026-06-16]** (top of Build): folds 7 decided items into SPEC — Strategy-doc entry at top of Projects, Project-view card collapsed by default, 5 date-picker details, Day-begins-at default 4:00 AM, completed-tray clears at rollover, paused subscription reverts to local-only, and a "no notifications in v1" scope note.
- Created **[audit-spec-trim]** + a "Plan session here" marker: audit SPEC for product-truth vs exhaustive detail, route findings to Captures, trim via a later spec-edit before feature builds.
- Created **[sysprompt-reconciliation-supersession]** (before 0021): write the "a new Strategy edit supersedes pending reconciliation suggestions" rule into SYSTEM-PROMPT.md.
- Updated the Deferred tests line: the 10 TaskDao instrumentation tests are no longer build-blocked, now device-blocked (no emulator/AVD).

**Captures routed:**
- **Dropped (resolved):** build-0001-does-not-compile (build verified); unassigned-tasks-visibility (no "Unassigned" entry, ever); add-connector-url-prefill (researched — modal deep-link works, field pre-fill doesn't; filed `resources/research/claude-add-custom-connector-prefill.md`); date-format-default (already in SPEC).
- **Resolved → spec-edit batch:** strategy-doc-side-menu-placement, project-card-default-collapsed, date-picker-design, day-begins-at-default, completed-tray-retention, notifications-scope.
- **Resolved → SYSTEM-PROMPT batch:** strategy-doc-conflict-resolution-ux.
- **Split:** subscription-pause (local-only direction → spec-edit; Play-Billing mechanics + multi-device merge parked).
- **Parked:** custom-instruction-production-text, empty-state-copy, help-thanks-bug-content, search-scope (confirmed feature, design recorded), onboarding-video-content, post-first-test-polish-review.
- **New capture:** completed-task-post-tray-fate (what becomes of a completed task after the tray clears — resolve before 0005).

**Rejected alternative kept on record:** an auto-advancement toggle ("pin tasks in place" — bump all dates +1 daily) was weighed and dropped — Soon spans 5 days, so pinning would force manual multi-day planning, and bumping every date would drift genuinely fixed-date tasks. Task advancement stays permanently on.
