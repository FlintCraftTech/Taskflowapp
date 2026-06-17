# Batch: Side-scrolling date picker

**Goal.** [To be filled in during the next planning session.]

**Outputs.** [To be filled in during the next planning session.]

**Success criteria.** [To be filled in during the next planning session.]

Changes:

- Replace the read-only date display in the edit dialogue with the side-scrolling date strip.
- Tile rendering with DD/MM (or MM/DD per setting); today as the visual anchor. Tiles fade with distance from today: the fade scales linearly with that distance (not stepped at the Schedule-slot boundaries) and is capped so even far-off tiles stay readable rather than fading out entirely.
- Tap a tile to select it as the task's date; visual highlight on the selected tile.
- "No date" tile at the **left edge** of the strip, before today — visually distinct and labelled "no date" (not merely greyed) so it reads as a deliberate choice, not just another faded date tile. Selecting it clears the date.
- When a date is set on a previously-undated task, the task moves into Schedule (and into the Project's card if it has a Project).
- Strip dimensions: spans roughly **one month into the past to twelve months into the future**, with a month-jump fast-forward affordance for crossing that range quickly. Roughly **five to seven tiles visible at once** (final tile count finalised against a real screen at build time).
- When the dialogue opens, the strip is centred on the task's own date if it has one, or on today if the task is undated.
- Serves UX.md: *Date picker — side-scrolling date strip*, *Edit a task* (date editing portion), *Move between Schedule and Project*.
