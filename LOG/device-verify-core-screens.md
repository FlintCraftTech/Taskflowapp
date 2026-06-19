# [HASH] — /next test (aborted) — opened [device-verify-core-screens] to run the deferred 0002/0004/0005 device checks, found the batch's "FAB seeds everything" premise false, aborted and requeued it, and filed two captures (reshape direction + project-creation gap)

This session opened the device-verification test batch to run the on-device checks deferred from batches 0002, 0004, and 0005, now that 0005 shipped a create path. The current post-0005 debug build was installed on a connected Pixel 6. Before any check ran, reading the add-flow code and pulling the device database showed the batch's core assumption — that the 0005 add-FAB could seed all the data the three checks need — is false in two independent ways.

The FAB can only date a task today or tomorrow; Soon/Later adds are undated, and the edit dialogue's date is read-only until batch 0006. So there is no path through the app to a task dated 2–7 days out, 8+ days out, or in the past, which makes three of 0002's render checks unreachable until 0006. Separately, the device has zero Projects and the build has no project-creation UI anywhere, so 0004 and the Project-dependent parts of 0005 cannot run at all without seeding a Project.

The deeper problem is a mis-plan: the batch sat at the top of the queue assuming it only depended on 0005, but it actually depends on 0006 (date checks) and on an unbuilt project-creation feature (Project checks). The alternative considered was to seed data and run only the FAB-reachable slice now; the user chose instead to abort and revert to planning rather than carry a partly-broken batch forward, since the dependency tangle needs reshaping in /plan regardless. The batch text was returned to QUEUE.md verbatim, and two captures were filed to drive the reshape.

**Tested:** none — aborted before any check ran. The three planned checks ([0002] Schedule rendering, [0004] Project rendering, [0005] add/edit/complete) were not run and return to the queue with the batch.

**Routed to Captures:**
- [device-verify-core-screens] reshape direction — split the date-matrix checks to ride with 0006, gate the Project checks on project-creation existing, note the FAB-reachable slice is runnable now.
- Project-creation feature/queue gap — no batch builds it, SPEC assumes it; needs a spec-edit plus a build batch ordered ahead of the 0004 verification.
