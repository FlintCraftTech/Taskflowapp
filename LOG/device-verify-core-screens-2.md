# [HASH] — [device-verify-core-screens] device-verified the FAB-reachable add/render/complete loop on a Pixel 6 (0002 + 0005 non-Project): all passed except two create-path bugs — the Today slot stamps tomorrow's date (task lands on Tomorrow, not Today), and the New-task form keeps the previous title; both routed to Captures

This batch was the first time the FAB-reachable create / render / complete loop ran on a device. Batches 0002 and 0005 had shipped render-only or compile-verified, so these checks were deferred until the create path existed. A Pixel 6 (oriole) was connected, so Claude drove the checks directly and confirmed the assigned dates against the app's own SQLite database rather than trusting the on-screen labels.

The loop works almost everywhere. Tasks added from Tomorrow, Soon, and Later land and render correctly (Tomorrow shows its DD/MM date; Soon and Later park undated with no label). Long titles wrap to multiple lines with the date pinned top-right. The add FAB is present on the four Schedule slots and correctly absent on the Projects overview. Editing a task via a row tap persists title and notes with the date read-only. The complete → Today-tray → uncheck → return cycle works, including tapping a struck tray row to open its editor.

Two bugs surfaced, both on the create path. First: adding from the Today slot stores tomorrow's date — the task lands on Tomorrow with a date label instead of on Today with none. The database showed the Today slot storing the same noon-anchored instant as the Tomorrow slot, so this is a fixed "today + 1" day-offset error, not a timezone or evening artifact, and it will reproduce at any time of day. Second: the New-task form keeps the previously-saved title when the FAB is reopened (Notes and Project reset; only Title carries over). Both were routed to Captures for /plan to turn into a fix-batch; both likely live in the add/new-task flow and could be fixed together.

The Schedule bucketing itself is correct — it placed the wrongly-dated 20/06 task on Tomorrow exactly as expected, which is how the day-offset bug was isolated to the add step. Do Not Disturb was toggled on during automation to stop notification banners intercepting taps, and was restored to off. Five test tasks remain on the device (the DB was empty at start; there is no in-app delete yet).

**Tested:** (TEST-LOG rows 018–025)
- Today-add → Today, no date label — ✗ (stored 2026-06-20, rendered on Tomorrow with a 20/06 label)
- Tomorrow-add → Tomorrow, DD/MM label — ✓
- Soon-add / Later-add park undated, no label — ✓
- Long title wraps, date stays top-right — ✓
- FAB present on 4 Schedule slots, absent on Projects overview — ✓
- Row tap → edit dialogue; save persists title + notes; date read-only — ✓
- Complete → leaves slot → Today tray (greyed/struck); uncheck returns; tray row opens edit — ✓
- New-task form blank on reopen — ✗ (retains previous task's title)

**Routed to Captures:** BUG — Today-slot add stamps tomorrow's date; BUG — New-task form keeps the previous title
