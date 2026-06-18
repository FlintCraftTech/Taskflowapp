# [HASH] — Side menu + Projects overview (batch 0003) — built the left-edge navigation drawer as a single spine-mirroring list (Today·Tomorrow·Soon·Later, Projects in order, Strategy) with app-actions pinned at the bottom, added the ☰ menu key to the spine header, and added the Projects overview spine page right of Later; a new ui/navigation/ shell (AppRoot drawer host + AppViewModel projects flow) drives the spine pager and opens placeholder overlays for Project/Strategy/app-action taps, with Project drag-reorder deferred to the Strategy-doc work (0015).

Batch 0003 builds the navigation shell against the revised SPEC §Side menu (the authority, superseding the archived spec's three-section framing) and §Projects overview. Until now the app was just MainActivity → the 0002 schedule pager, with no drawer or menu key. This batch wraps the app in a left-edge ModalNavigationDrawer hosted by a new `ui/navigation/AppRoot`, whose drawer lists the spine top-to-bottom — the four schedule slots, the Projects in saved order (or a first-draft pedagogical empty-state line when there are none), then a single Strategy row — with the app actions (Settings, Help, Thanks, Report a bug, "turn on AI") pinned apart at the bottom. The spine itself, previously four pages, gained a fifth: the Projects overview, rendered right of Later by generalizing ScheduleScreen's header and pager from ScheduleSlot to a new SpinePage enum. The ☰ menu key went in the top-left corner 0002 deliberately left clear.

Navigation architecture: a deliberately lightweight hand-rolled model rather than Jetpack Navigation Compose. The spine is the home surface (a shared pagerState the drawer scrolls for slot taps); "deep" destinations — a Project, Strategy, the app actions — are shown as a single placeholder overlay over the spine, with a BackHandler returning to it. NavHost was the intuitive alternative and was weighed; it lost because every deep destination is a placeholder in this batch (real builds are 0004/0015/0012/0022/0019), so a navigation-library dependency and its boilerplate buy nothing now — the overlay can be swapped for NavHost later if destinations multiply.

Project drag-reorder was scoped out (decided at lock, confirmed with Alex). The queue entry doesn't name it, there's no way to create a Project yet so there'd be nothing to reorder, and its one stated purpose — driving Strategy-doc heading order — needs the Strategy doc (batch 0015). It belongs with that later work.

Verified on a Pixel 6: assembleDebug compiles clean, the app launches without crashing, and the full navigation flow was confirmed on-device — Today renders with the menu key and correct dead-end chevron, the drawer shows every expected row, slot taps jump the spine, the Projects overview renders right of Later, and a placeholder opens and backs out. Two spine-header polish issues surfaced during the check — the ←/→ are small, baseline-low text glyphs, and the page-name slide overlaps moving backward (Soon→Tomorrow) — both pre-existing from 0002's header and carried unchanged here; both were routed to Captures as one header-polish item rather than expanded into this build.

**Files touched:**
- ui/navigation/Destination.kt — new (SpinePage enum + Overlay sealed interface)
- ui/navigation/AppViewModel.kt — new (Projects list flow in sort_order, read-only)
- ui/navigation/AppDrawer.kt — new (spine-mirroring nav list + app actions)
- ui/navigation/AppRoot.kt — new (drawer host, shared pager, overlay state, BackHandler)
- ui/common/PlaceholderScreen.kt — new (placeholder for not-yet-built destinations)
- ui/projects/ProjectsOverviewScreen.kt — new (Projects overview content + empty state)
- ui/schedule/ScheduleScreen.kt — refactored from the 4-slot pager into the spine host (5 pages, menu key, SpinePage header)
- MainActivity.kt — now hosts AppRoot()

**Routed to Captures:** Spine header polish (one item, two findings) — text-glyph ←/→ should be Material chevrons; backward title-slide overlap. Both pre-existing from 0002.
