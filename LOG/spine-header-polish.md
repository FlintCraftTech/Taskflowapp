# [HASH] — ScheduleScreen.kt — fixed the spine-title overlap during page transitions (slides now overlap with a crossfade instead of colliding mid-slide) and replaced the text-glyph chevron arrows with vertically-centred Material KeyboardArrowLeft/Right icons

Two polish issues in the Schedule spine header, both seen on-device and both predating this batch. First, moving between spine pages, the outgoing and incoming page titles slid past each other and overlapped in the centre mid-transition. Second, the ← / → navigation arrows were plain text glyphs — small and baseline-aligned, so they sat low rather than centred with the title.

For the overlap: the title's AnimatedContent transition originally ran the two slides fully concurrently in opposite directions, so the texts crossed. The first attempt fully staggered them — outgoing clears completely, then incoming starts — which removed the overlap but read as two separate beats on device. The shipped fix overlaps the slides slightly (180ms each, incoming delayed 100ms, ~80ms overlap) with a crossfade across the overlap, so it reads as one continuous motion while the fade keeps the brief overlap soft instead of a solid-title collision.

For the arrows: the Chevron composable now renders a Material3 Icon from Icons.AutoMirrored.Filled.KeyboardArrowLeft/Right, centred in its 48dp touch box, which the Row centres vertically against the title. AutoMirrored was chosen so the arrows flip correctly under RTL. No gradle change was needed — the icons resolve via material3's transitive icons-core, so the build batch's conditional dependency entry closed without an edit.

**Files touched:**
- app/src/main/java/com/example/taskflow/ui/schedule/ScheduleScreen.kt

**Routed to Captures:** none
