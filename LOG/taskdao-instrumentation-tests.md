# [HASH] — /next test — ran the ten batch-0001 TaskDao instrumentation tests on the connected Pixel 6: all 10 passed, flipping TEST-LOG #002–011 from Skipped to Pass and verifying the data foundation (date round-trip, undated Soon/Later parking, Project assign + refile, slot + Project reorder persistence, subtask link, parent completion roll-up).

The data foundation built in batch 0001 shipped with ten TaskDao instrumentation tests written but never run — blocked first by the build failure (TEST-LOG #001), then by no connected device. Both blockers had cleared (build compiles since 2026-06-16; Pixel 6 reachable over wireless adb), so this session finally ran them. The wireless connection had dropped overnight and was re-established before the run. All ten passed on the first run via `:app:connectedDebugAndroidTest` (10 tests, 0 failures/errors/skipped, ~1.5 min). The result means TaskDao's storage and query behaviour — dates, undated slot parking, Project foreign keys and refiling, per-slot and per-Project sort-order persistence, subtask parent/child links, and parent completion roll-up — all hold, so the feature batches that build on TaskDao now stand on a checked base. One gap surfaced and was routed to Captures rather than run here (out of this batch's scope): ProjectDaoTest holds six written-but-unrun instrumentation tests with no TEST-LOG rows, the Project-storage companion to what was verified here.

**Tested:**
- Create a task with a date — saves and reads back correctly — ✓
- Create a task with no date — saves and reads back correctly — ✓
- Park an undated task on Soon, and on Later — placement persists — ✓
- Assign a task to a Project, then refile to a different Project — link updates — ✓
- Reorder tasks within a Schedule slot — order persists — ✓
- Reorder tasks within a Project — order persists — ✓
- Create a subtask under a parent — parent/child link saves — ✓
- Complete all of a parent's subtasks — parent rolls up to complete — ✓
- (All 10 underlying TaskDaoTest methods; covers TEST-LOG #002–011.)

**Routed to Captures:** ProjectDaoTest — six instrumentation tests never run or recorded, runnable now, for /plan to promote into a short test batch.
