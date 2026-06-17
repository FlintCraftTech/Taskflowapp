# REGISTRY.md — Taskflow

A flat, alphabetical glossary of named elements in the Taskflow codebase that the user might want context on. One line per entry. Maintained by Claude during builds — the user does not maintain this file by hand. Not intended to be read cover-to-cover; use it as a reference when you encounter a name you want context on, and as the basis for drift checks against `SPEC.md`.

If this list ever grows long enough that scanning it becomes hard, switch to alphabetical sections by area (e.g., Screens, Services, Models, Components).

<!--
  Entry format:
    - **[Name]** — [one-line plain-English description of what this is and what it does] (path)
-->

- **MainActivity** — Compose activity / app entry point; hosts the Schedule view (ScheduleScreen) inside a MaterialTheme scaffold. (`app/src/main/java/com/example/taskflow/MainActivity.kt`)
- **Project** — Room entity representing a user-created project with a name and sort order. (`app/src/main/java/com/example/taskflow/data/model/Project.kt`)
- **ProjectDao** — DAO for Project CRUD: insert, update, delete, and ordered retrieval. (`app/src/main/java/com/example/taskflow/data/local/ProjectDao.kt`)
- **ProjectRepository** — Repository wrapping ProjectDao with suspend functions for project operations. (`app/src/main/java/com/example/taskflow/data/repository/ProjectRepository.kt`)
- **ScheduleScreen** — Compose Schedule view: the four slots as a HorizontalPager (the navigation spine's left segment, Today default), with a single-slot sliding header and tappable ←/→ navigation chevrons. (`app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt`)
- **ScheduleSlot** — Enum defining the four Schedule slots: Today, Tomorrow, Soon, Later. (`app/src/main/java/com/example/taskflow/data/model/ScheduleSlot.kt`)
- **ScheduleViewModel** — Buckets active top-level tasks into the four Schedule slots (via SlotDeriver) and exposes per-slot read-only UI lists with DD/MM date labels; also defines ScheduleUiState and ScheduleTaskUi. (`app/src/main/java/com/example/taskflow/ui/schedule/ScheduleViewModel.kt`)
- **SlotDeriver** — Pure date→slot placement logic (today/past→Today, +1→Tomorrow, 2–7→Soon, 8+→Later) with a day-begins-at boundary (4 AM default constant until batch 0012). (`app/src/main/java/com/example/taskflow/domain/SlotDeriver.kt`)
- **SlotPage** — Renders one Schedule slot: a read-only list of task rows (title + optional DD/MM date, multi-line wrap), or a placeholder empty state. (`app/src/main/java/com/example/taskflow/ui/schedule/SlotPage.kt`)
- **StrategyEntry** — Room entity for Strategy doc content, one paragraph per Project (keyed by Project FK). (`app/src/main/java/com/example/taskflow/data/model/StrategyEntry.kt`)
- **StrategyEntryDao** — DAO for StrategyEntry CRUD: upsert paragraph by project, ordered retrieval. (`app/src/main/java/com/example/taskflow/data/local/StrategyEntryDao.kt`)
- **StrategyRepository** — Repository wrapping StrategyEntryDao with suspend functions for strategy entry operations. (`app/src/main/java/com/example/taskflow/data/repository/StrategyRepository.kt`)
- **Task** — Room entity representing a task with nullable date, slot, projectId FK, parentId FK, completion state, and sort-order fields. (`app/src/main/java/com/example/taskflow/data/model/Task.kt`)
- **TaskDao** — DAO for Task CRUD: insert, update, delete, queries by slot/date/project, active top-level tasks (for the Schedule view), reorder within slot and project. (`app/src/main/java/com/example/taskflow/data/local/TaskDao.kt`)
- **TaskRepository** — Repository wrapping TaskDao with suspend functions for all task operations. (`app/src/main/java/com/example/taskflow/data/repository/TaskRepository.kt`)
- **TaskflowApplication** — Application subclass providing lazy singletons for the Room database and all repositories. (`app/src/main/java/com/example/taskflow/TaskflowApplication.kt`)
- **TaskflowDatabase** — Room database class registering Task, Project, and StrategyEntry entities and providing DAO accessors. (`app/src/main/java/com/example/taskflow/data/local/TaskflowDatabase.kt`)

---
*No-code method — Version 55.*
