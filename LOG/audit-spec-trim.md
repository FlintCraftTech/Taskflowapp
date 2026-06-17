# 34ccf66 — Audit SPEC.md against keep-vs-relocate criteria — routed 20 per-section findings to Captures (no SPEC edits)

Audited SPEC.md section by section against the audit-spec-trim batch's criteria: keep product truth (what a feature is, who it's for, how it works at the level the product needs, and why); flag exhaustive UX/implementation detail as a remove-candidate; and for each remove-candidate name where its detail relocates, so nothing is lost. The read produced 20 findings — 12 remove-candidates pointing at the build batches that will own the relocated detail (0002, 0004, 0005, 0006, 0008, 0009, 0010, 0011, 0016) plus REGISTRY, and 8 flags for /plan covering genuinely ambiguous classifications and redundancies the audit shouldn't pre-judge. The densest source was the date-picker section (three remove-candidates → 0006); one of those also caught a "to be finalised at build time" phrase that violates SPEC's own rule against undecided placeholders. No SPEC.md edits — the actual trimming is a later spec-edit batch the /plan marker already queues.

The findings were filed as one structured capture block, not 20 separate captures. The batch's output contract called for "a structured per-section findings list" (singular), and they're processed together in one /plan session, so one block keeps them readable as a unit while still listing each finding individually. Noted so a future session doesn't re-split them.

**Files touched:** SPEC.md (read only — the audit edited nothing). QUEUE.md received the findings capture.

**Routed to Captures:** `[spec-trim-audit-findings]` — 20 findings (F1–F20).

**Approval outcomes:** all findings approved as-is; none dropped or reworded.
