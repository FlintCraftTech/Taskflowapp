# Batch: Outliner-style typing + drag-target icons (bin and promote)

**Goal.** [To be filled in during the next planning session.]

**Outputs.** [To be filled in during the next planning session.]

**Success criteria.** [To be filled in during the next planning session.]

Changes:

- Edit dialogue text area renders parent + children as an indented outline.
- Enter at end of any line creates a new child line below it.
- Backspace at start of any line deletes the line and merges remaining text into the previous line (parent or sibling).
- Drag handles on each child line (parent line has no handle).
- On any task drag (from Schedule, Project view, or edit dialogue), reveal a row of drag-target icons fixed at the top right, with hover feedback.
- Drag-target icons in this batch: **bin** (delete) on screen and dialogue drags; **promote** (subtask drag in dialogue only) — promotes child to top-level task at the bottom of the parent's slot or Project, with the parent's date.
- If the parent ends up with zero children after a promotion, it reverts from expand/collapse to a checkbox.
- Serves UX.md: *Edit dialogue: outliner-style typing for subtasks*, *Drag-target icons* (bin + promote portion).
