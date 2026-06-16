# 86d521b — SPEC.md — folded 2026-06-16 planning decisions into seven sections (Strategy-doc row, collapsed Project card, date-strip details, 4 AM day default, tray rollover-clear, paused-sub local-only, no notifications in v1)

This spec-edit folds the seven product decisions made in the 2026-06-16 planning session into SPEC.md, so the feature batches that depend on them (0004 Project view, 0005 completed tray, 0006 date picker, 0012 day-begins-at, 0015 Strategy doc, 0017 tier model) build against current product truth rather than a stale spec. SPEC.md is read-only during feature builds, so a dedicated spec-edit batch is the only place these decisions can land.

Two decisions touched more than one passage. The Strategy-doc-as-calm-top-row decision was written into both the §Side menu Middle section (where the row sits, above individual Projects) and the §Strategy doc reachability line (which previously said only "reachable from the side menu"), so the two descriptions stay consistent. The date-strip details refined existing wording rather than only appending: the old "fade to grey" line became a capped-linear fade that isn't stepped at slot boundaries, and the old generic "no date option at the strip's edge" became a labelled tile pinned to the left edge before today.

The no-notifications-in-v1 decision was written as its own deliberate-absence section (in the spirit of the existing "no overdue label"), tied to UX principles 4 and 7, and marked revisitable post-v1.

**Files touched:**
- SPEC.md — seven sections edited (Side menu, Strategy doc, Project view top card, date strip, Day begins at, Completed task tray, Tier model) plus a new "No notifications in v1" section.

**Routed to Captures:** none
