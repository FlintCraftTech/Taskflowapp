# [HASH] — Data model — non-null `Task.projectId` defaulting to a seeded system "Unassigned" Project; destructive v2 migration seeds it via `onOpen`; Project-delete reassigns tasks to it; Strategy mirroring excludes it [unassigned-project-model]

Later-by-Project needs every task to sit under a Project, so the old "no Project / null" state had to go. `Task.projectId` became non-null, defaulting to a system "Unassigned" Project seeded at a well-known id (1). Unassigned is flagged `is_system`: excluded from the user-facing ordered-Project lists (so it can be pinned separately later), excluded from Strategy-doc mirroring (`StrategyRepository.upsert` no-ops for it), and undeletable. The tasks→projects foreign key changed from `SET_NULL` to `RESTRICT`, since a non-null column can't be set null; deleting a real Project reassigns its tasks to Unassigned in `ProjectRepository.delete` before the row goes.

Migration was made destructive + reseed rather than a preserving in-place migration. Reason: making a column non-null needs the table rebuilt, and a byte-exact table-recreation with schema-export off is fragile and unverifiable without a device — too much risk for what is only recreatable pre-release test data. The user was warned the choice wipes current on-device data and approved it.

Seeding location was the real lesson, caught on-device. The seed first sat in `onCreate`, which never fires on the destructive-migration path (Room calls `onDestructiveMigration` there) — so an upgraded device got no Unassigned Project. Moving the seed into `onDestructiveMigration` then crashed the app: that callback runs mid-migration, after the old tables are dropped but before the new ones exist, so the INSERT hit a missing table. Final form seeds in `onOpen` with `INSERT OR IGNORE` — the one callback that always runs with the schema fully in place, and idempotent. Verified by downgrading the device DB to v1 and relaunching: clean v1→v2 migration, Unassigned seeded, no crash.

Scope grew by three files beyond the batch's seven, each an unavoidable consequence of the non-null change, each approved: `TaskflowApplication.kt` (wire `TaskDao` into `ProjectRepository` for delete-reassignment), `TaskDaoTest.kt` (seed Unassigned so the FK-bound tests still pass; +2 new tests), and `EditTaskViewModel.kt` (coerce a null Project pick to Unassigned at the two task-write sites, to keep it compiling). The "undeletable" guard and the repository reassign-then-delete orchestration are verified by inspection only — no delete UI exists yet to exercise them end-to-end — so that verification is captured against [project-lifecycle-later].

Verified: 18/18 data-layer instrumentation tests pass on a Pixel 6; main + test sources compile; the destructive-migration seed confirmed live on-device.

**Files touched:**
- `app/src/main/java/com/example/taskflow/data/model/Project.kt` — `is_system` flag + `UNASSIGNED_PROJECT_ID`/`UNASSIGNED_PROJECT_NAME` constants
- `app/src/main/java/com/example/taskflow/data/model/Task.kt` — `projectId` non-null (default Unassigned); FK `SET_NULL` → `RESTRICT`
- `app/src/main/java/com/example/taskflow/data/local/TaskflowDatabase.kt` — v2; destructive fallback; `onOpen` seed callback
- `app/src/main/java/com/example/taskflow/data/local/ProjectDao.kt` — `getSystemUnassigned`; ordered queries filter `is_system = 0`
- `app/src/main/java/com/example/taskflow/data/repository/ProjectRepository.kt` — `TaskDao` injected; delete-with-reassign; `getSystemUnassigned`
- `app/src/main/java/com/example/taskflow/data/repository/StrategyRepository.kt` — `upsert` excludes the system Project
- `app/src/main/java/com/example/taskflow/data/local/TaskDao.kt` — `reassignTasksToProject` bulk query
- `app/src/main/java/com/example/taskflow/TaskflowApplication.kt` — pass `taskDao` into `ProjectRepository`
- `app/src/main/java/com/example/taskflow/ui/edit/EditTaskViewModel.kt` — null Project → Unassigned at the two write sites
- `app/src/androidTest/java/com/example/taskflow/data/local/TaskDaoTest.kt` — seed Unassigned in setUp; +2 tests

**Routed to Captures:** Verify Project-deletion data behaviour end-to-end once the delete UI exists (blocked by [project-lifecycle-later]).
