# c667d53 — TEST-LOG.md — added rows #012–017 recording the six ProjectDao instrumentation tests (create, update, delete, ordered retrieval, reorder persistence, max-sort-order) run on the Pixel 6, closing the Project half of the batch-0001 data-foundation verification left unrun since 0001.

The six ProjectDaoTest instrumentation tests were written back in batch 0001 alongside the TaskDao tests but were never run or recorded. The TaskDao half was verified last session (all 10 passed, TEST-LOG #002–011); this session closes the gap on the Project-storage half. Ran the full ProjectDaoTest class on the connected Pixel 6 (oriole) via connectedDebugAndroidTest, class-filtered to ProjectDaoTest. All six passed (testsuite: 6 tests, 0 failures, 0 errors, 0 skipped), recorded as TEST-LOG rows #012–017. With both halves green, the Room data foundation — Task and Project storage, ordering, and relationships — is now verified end to end on a real device.

The first two runs failed on the known Windows file-lock (AccessDeniedException), but on the connected-test output and report directories rather than the main build dir, and despite the `--no-watch-fs --no-daemon` flags. Clearing the three stale androidTest directories let the third run pass. No code or test changes — environmental only.

**Tested:**
- createProject_isRetrievable — ✓ (TEST-LOG #012)
- updateProject_changesArePersisted — ✓ (TEST-LOG #013)
- deleteProject_isRemoved — ✓ (TEST-LOG #014)
- getAllOrdered_returnsSortedBySortOrder — ✓ (TEST-LOG #015)
- reorderProjects_persistedOrderChanges — ✓ (TEST-LOG #016)
- getMaxSortOrder_returnsCorrectValue — ✓ (TEST-LOG #017)

**Routed to Captures:** none — the lighter-touch fix for the connected-test file-lock was discussed and you chose not to capture it; the existing CLAUDE.md project note is sufficient.
