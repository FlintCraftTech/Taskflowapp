# Batch: Schedule view (date-derived placement)

**Goal.** [To be filled in during the next planning session.]

**Outputs.** [To be filled in during the next planning session.]

**Success criteria.** [To be filled in during the next planning session.]

Changes:

- Four Schedule screens — Today, Tomorrow, Soon, Later — with horizontal swipe navigation between them.
- Each screen shows tasks placed on it: dated tasks whose date falls into the slot's range, plus undated tasks parked on Soon or Later. Today also shows tasks dated before today (no special treatment, no label, just present in their existing order).
- Per-slot sort order, read-only for this batch (no add, no edit, no drag).
- DD/MM date display (or MM/DD per the resolved Date format planning batch) on each task in Tomorrow / Soon / Later, and on Today only for tasks dated before today. Multi-line wrap when the title doesn't fit alongside the date.
- Tomorrow → Today rollover at day-begins-at: rolled-in tasks append to the bottom of Today's list, in their existing relative order from Tomorrow. Today's tasks that didn't get completed remain in place (no move-to-top).
- Empty states for each slot.
- Today is the default open screen.
- Serves UX.md: *Schedule view*, *Tasks dated before today*.
