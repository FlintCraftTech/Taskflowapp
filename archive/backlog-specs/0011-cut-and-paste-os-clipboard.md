# Batch: Cut and paste (OS clipboard)

**Goal.** [To be filled in during the next planning session.]

**Outputs.** [To be filled in during the next planning session.]

**Success criteria.** [To be filled in during the next planning session.]

Changes:

- Add **cut** icon to the drag-target icon row (both screen drags and dialogue drags).
- Drop on cut → remove task from Taskflow and write its content to the device's OS clipboard as plain text. Cutting a parent writes the whole set as indented multi-line plain text.
- Paste uses the device's normal long-press context menu, only inside an edit dialogue text field.
- Outliner editor handles multi-line indented text on paste — line breaks become new lines, indentation becomes hierarchy.
- Cut-clipboard-loss risk explicitly accepted in `UX.md` (no recently-cut buffer).
- Serves UX.md: *Drag-target icons* (cut and paste portion).
