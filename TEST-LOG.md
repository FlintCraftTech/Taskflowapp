# TEST-LOG.md

This file records the test outcomes of every shipped build batch, one row per test. Maintained by Claude during builds and planning; the user reviews and confirms in planning sessions.

The column shape, status meanings, and entry format are documented in the comment block below this table.

| # | Date | Session | Component | Test Description | Type | Verifier | Status | Confirmed Explicitly | Notes |
|---|---|---|---|---|---|---|---|---|---|
| 001 | 2026-05-24 | 0001 | TaskflowDatabase | Project builds without errors after Compose/Room/KSP migration | Run and read | Claude | Pass | Yes (2026-06-16) | Originally failed: duplicate kotlin-android plugin declaration (AGP 9.2.1 auto-applies it). Fix landed in app/build.gradle.kts (explicit declaration removed). Re-verified 2026-06-16 during /plan: `assembleDebug` succeeds — Kotlin compile, Room KSP code-gen, and packaging all pass. Windows note: build with `--no-watch-fs --no-daemon` to avoid build-folder lock errors. |
| 002 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: create a task with a date and verify it is retrievable | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 003 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: create a task without a date and verify it is retrievable | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 004 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: park an undated task on Soon and verify slot is stored | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 005 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: park an undated task on Later and verify slot is stored | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 006 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: assign a task to a Project and verify the FK relationship | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 007 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: refile a task to a different Project and verify the FK updates | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 008 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: reorder tasks within a Schedule slot and verify persisted order | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 009 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: reorder tasks within a Project's below-the-card list and verify persisted order | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 010 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: create a subtask under a parent and verify parent-child relationship | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 011 | 2026-06-18 | 0001 | TaskDao | Instrumentation test: complete a subtask and verify parent completion rolls up when all children are complete | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest in the taskdao-instrumentation-tests session — passed. Previously Skipped pending build fix (#001) + connected device. |
| 012 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: create a Project and verify it is retrievable | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |
| 013 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: update a Project and verify the changes are persisted | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |
| 014 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: delete a Project and verify it is removed | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |
| 015 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: retrieve all Projects and verify they return sorted by sortOrder | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |
| 016 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: reorder Projects and verify the persisted order changes | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |
| 017 | 2026-06-18 | 0001 | ProjectDao | Instrumentation test: query the max sortOrder and verify it returns the correct value | Run and read | Claude | Pass | No | Ran on Pixel 6 (oriole) via connectedDebugAndroidTest (class-filtered to ProjectDaoTest) in the projectdao-instrumentation-tests session — passed. Written in batch 0001 but never run until now; closes the Project half of the data-foundation verification. |

<!--
Entry format:
| 001 | YYYY-MM-DD | <session tag, OR YYYY-MM-DD if the project doesn't keep tags> | <Component name from REGISTRY.md, or plain English if cross-component> | <One-sentence test description specific enough to re-run from> | Look and click / Programmatic / Hybrid | User / Claude / Both | Pass / Fail / Skipped / blank | Yes (YYYY-MM-DD) / No | <observations, surprises, reason if Skipped, regression context if Fail> |

Status meanings:
- Pass — tested, behaved as expected
- Fail — tested, did not behave as expected; details in Notes
- Skipped — explicitly not tested this round; reason required in Notes
- (blank) — test session is open; user has not yet confirmed an outcome

Confirmed Explicitly meanings:
- Yes (YYYY-MM-DD) — the user named this specific row in the planning-session read-back; date is when the confirmation happened
- No — Status was filled in without explicit per-row user confirmation; only valid as a transient state during session-open
-->

---
*No-code method — Version 55.*
