# 62852c2 — SPEC.md — §Projects overview gains manual name-only Project creation (an add affordance that asks only for a name; the new Project is appended to the end of the Projects sort order and mirrored in the side menu + Strategy doc; no description captured here — that's the later Strategy-doc paragraph). §Add a new task narrows "from any screen" to the real task-add surfaces (the four Schedule slots + a Project view) and states the Projects-overview affordance creates a Project, not a task.

This spec-edit describes manual Project creation so the feature batch [project-create] builds against a described surface. Until now SPEC assumed Projects could be created but no section said how, and the build had no creation path at all.

Creation is name-only. The user enters a Project name on the Projects overview; the new Project is appended to the end of the Projects list and shows in that same last position in the side menu and the Strategy doc. No description is captured at creation. The alternative — asking for a description up front — was weighed and set aside: a Project's description is its Strategy-doc paragraph, written later in the Strategy editor (or with Claude on the paid tier), so requiring one here would duplicate that editor and slow the add, against UX principle 5 (lightest capture). A new Project's Strategy paragraph appears automatically under the existing mechanical-structure rule, so §Strategy doc needed no change.

The same pass folded in the §Add "from any screen" spec-drift filed during 0005: the add FAB shipped only on the four Schedule slots and inside a Project view, never on the Projects overview, so §Add's wording was broader than what shipped. Narrowed it to the enumerated surfaces and added that the Projects-overview add affordance creates a Project, not a task — the same surface this batch touches, so the fix rode here rather than in its own pass.

One thing surfaced mid-edit: the batch rationale's "empty state says 'when you make one, it shows up here and in the menu'" was a paraphrase — the empty-Projects pedagogy actually lives in §Side menu, not §Projects overview. It didn't change the edit; both target sections were intact and edited as planned.

**Files touched:**
- SPEC.md — §Projects overview: added the name-only creation paragraph. §Add a new task: rewrote the opening sentence to narrow the task-add surfaces and exclude the Projects overview.

**Routed to Captures:** [later-by-project] — raised in this session's post-commit tail: a proposal to unite Later and the Projects overview into one Project-grouped "Later" screen (zoom-levels-of-time principle). Bears directly on [project-create], which should wait for /plan to resolve this before building, since it may move where Project creation lives.
