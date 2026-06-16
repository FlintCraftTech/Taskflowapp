# Batch: Project skeleton and Room data model

Status: shipped

**Goal.** Establish the local-first foundation that every subsequent batch builds on — a working Android project with Room entities matching the UX.md data model, a repository layer, and instrumentation tests proving the data layer works. No UI ships in this batch; the next batch (Schedule view) is the first to render anything on screen.

**Outputs.** A buildable Android Studio project (Kotlin/Jetpack Compose, MVVM architecture) with a Room database containing Task, Project, and Strategy doc entities. Tasks can be created with or without dates, parked undated on Soon/Later, assigned to or refiled between Projects, and reordered within a slot or Project. Instrumentation tests confirm every data path works.

**Success criteria.** The project builds and runs on an emulator. Instrumentation tests pass for: create a task with a date, create a task without a date, park an undated task on Soon, park an undated task on Later, assign a task to a Project, refile a task to a different Project, reorder tasks within a Schedule slot, reorder tasks within a Project's below-the-card list, create a subtask under a parent, complete a subtask, verify parent completion rolls up from children.

Changes:
- [Requested] Set up Android Studio project with Kotlin/Jetpack Compose, MVVM structure, and Room dependency.
- [Requested] Define core Room entities: Task (nullable `projectId` FK, nullable `date`, nullable `slot` for undated parked tasks on Soon/Later, `projectSuggestionDeclined` boolean, parent FK for subtasks, sort-order fields per slot and per Project), Project, Strategy doc (single-row table holding user-edited paragraph content keyed by Project; structural assembly at render time).
- [Requested] Implement DAOs and repository layer covering CRUD operations needed for the next two batches.
- [Requested] Write instrumentation tests verifying tasks can be created, dated, undated, parked on Soon, refiled between Projects, and reordered.

Files:
- [x] `app/src/main/AndroidManifest.xml` — Add android:name='.TaskflowApplication' to application tag. [Prerequisite, not in plan]
- [x] `gradle/libs.versions.toml` — Add Kotlin, KSP, Compose, Room, and Lifecycle version entries and library/plugin declarations.
- [x] `build.gradle.kts` — Add Kotlin and KSP plugin aliases to root-level plugin block.
- [x] `settings.gradle.kts` — Add KSP plugin to pluginManagement if needed for resolution.
- [x] `app/build.gradle.kts` — Apply Kotlin, KSP, and Compose plugins; add Room, Compose, and Lifecycle dependencies; remove view binding and fragment/navigation dependencies.
- [x] `app/src/main/java/com/example/taskflow/MainActivity.kt` — Rewrite as a minimal Compose activity (setContent with an empty scaffold); remove view-binding and fragment logic.
- [x] `app/src/main/java/com/example/taskflow/TaskflowApplication.kt` — Application subclass providing the Room database instance and repository singletons.
- [x] `app/src/main/java/com/example/taskflow/data/model/Task.kt` — Room entity for Task with all UX.md data-model fields (nullable projectId FK, nullable date, nullable slot, parentId FK, sort-order fields, completion state).
- [x] `app/src/main/java/com/example/taskflow/data/model/Project.kt` — Room entity for Project (name, sort order).
- [x] `app/src/main/java/com/example/taskflow/data/model/StrategyEntry.kt` — Room entity for Strategy doc entries (paragraph content keyed by Project FK).
- [x] `app/src/main/java/com/example/taskflow/data/model/ScheduleSlot.kt` — Enum for the four Schedule slots (Today, Tomorrow, Soon, Later) used by Task.slot.
- [x] `app/src/main/java/com/example/taskflow/data/local/TaskflowDatabase.kt` — Room database class registering all entities and providing DAO accessors.
- [x] `app/src/main/java/com/example/taskflow/data/local/TaskDao.kt` — DAO for Task CRUD: insert, update, delete, queries by slot/date/project, reorder within slot, reorder within project.
- [x] `app/src/main/java/com/example/taskflow/data/local/ProjectDao.kt` — DAO for Project CRUD: insert, update, delete, get all ordered.
- [x] `app/src/main/java/com/example/taskflow/data/local/StrategyEntryDao.kt` — DAO for StrategyEntry CRUD: upsert paragraph by project, get all ordered.
- [x] `app/src/main/java/com/example/taskflow/data/repository/TaskRepository.kt` — Repository wrapping TaskDao with suspend functions for all task operations.
- [x] `app/src/main/java/com/example/taskflow/data/repository/ProjectRepository.kt` — Repository wrapping ProjectDao with suspend functions for project operations.
- [x] `app/src/main/java/com/example/taskflow/data/repository/StrategyRepository.kt` — Repository wrapping StrategyEntryDao with suspend functions for strategy entry operations.
- [x] `app/src/androidTest/java/com/example/taskflow/data/local/TaskDaoTest.kt` — Instrumentation tests for TaskDao covering all success-criteria scenarios.
- [x] `app/src/androidTest/java/com/example/taskflow/data/local/ProjectDaoTest.kt` — Instrumentation tests for ProjectDao covering create, update, delete, ordering.

Tests:
- Project builds without errors after Compose/Room/KSP migration [Run and read] [Claude]
- Instrumentation test: create a task with a date and verify it is retrievable [Run and read] [Claude]
- Instrumentation test: create a task without a date and verify it is retrievable [Run and read] [Claude]
- Instrumentation test: park an undated task on Soon and verify slot is stored [Run and read] [Claude]
- Instrumentation test: park an undated task on Later and verify slot is stored [Run and read] [Claude]
- Instrumentation test: assign a task to a Project and verify the FK relationship [Run and read] [Claude]
- Instrumentation test: refile a task to a different Project and verify the FK updates [Run and read] [Claude]
- Instrumentation test: reorder tasks within a Schedule slot and verify persisted order [Run and read] [Claude]
- Instrumentation test: reorder tasks within a Project's below-the-card list and verify persisted order [Run and read] [Claude]
- Instrumentation test: create a subtask under a parent and verify parent-child relationship [Run and read] [Claude]
- Instrumentation test: complete a subtask and verify parent completion rolls up when all children are complete [Run and read] [Claude]

Serves UX.md: Data model.
