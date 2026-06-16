# FAQ

Answers to common questions about how this project's workflow operates.

## What do /plan, /next, and /done each do?

They split work into three modes. **/plan** is for thinking — queue management, captures, design questions. **/next** is for doing — picks the top batch and builds it. **/done** is for closing — records, updates docs, commits. Always in order: plan, do, close.

## What's the difference between Batches and Captures in QUEUE.md?

**Batches** are ready-to-build work — entries under Build/Test subheadings, worked top to bottom. One batch per /next session. **Captures** is an inbox — ideas, questions, and observations from builds or between sessions. Not actionable yet — during /plan, each gets discussed and either promoted, parked, or dropped.

## How are entries organized in the queue?

Batches group entries under **Build**, **Test**, and **Audit** subheadings. Build entries create or change things. Test entries verify things work. Audit entries review what exists and route findings back into the queue. Not every batch needs a Test section — only when verification isn't self-evident. Captures are plain bullets — each carries its own reasoning inline.

## What is `/next freeform`?

A fourth kind of /next session, for work that isn't a build, a test, or an audit — an ad-hoc change, talking through edits you've already made, or surfacing something without the pressure of sorting it out right away. Reach for it when none of the other three fit. It keeps the safety rails — Claude still asks before touching a file, and still flags risks — but drops the fixed step list, so it suits work that doesn't know its shape up front. One thing it won't do: process your captures. A freeform session can jot ideas into Captures, but promoting, parking, or dropping them is /plan's job — Claude will say so and offer to move to /plan when captures pile up.

## What is the Red flags section at the top of QUEUE.md?

It's where Claude lists security and privacy risks it has spotted — anything that could expose your data or your users' data, or amount to a breach. It sits at the very top of the queue so it's the first thing you see each session; a risk you should know about shouldn't be buried. The section stays empty until something comes up.

Each red flag carries one of three states:

- **Open** — the risk has been raised but not yet dealt with.
- **Resolved** — the risk has been fixed or designed out; the work no longer carries it.
- **Accepted** — you were told the risk plainly and chose to go ahead anyway. That choice is written into the session log: what you were warned about, and that you agreed to proceed. It's a clear record if the risk ever matters later.

Claude raises and updates these — you don't maintain the section. Accepting a risk is a decision only you can make.

## What is the "Deferred tests" section in QUEUE.md?

A waiting list for tests that couldn't run in the session that planned them — some only become checkable later, some need you to try something, some wait on an outside event. When /done closes a session and a planned test couldn't run, it adds a one-line entry here: which batch the test came from, what to verify, and what confirms it. /plan reads this list each session and folds the ones that can now run into a test batch; and when a later session happens to confirm one along the way, /done removes its line and records the result in the session log. Claude writes and clears this section — you don't maintain it.

## I closed the app in the middle of a build. What happens when I reopen it?

Nothing is lost. `_build.md` tracks progress. When you reopen, session start detects the unfinished build. Run /next to resume.

## Is it safe to clear the conversation or start a new session between steps?

After /done, yes — everything is recorded in the session log and committed, so a fresh conversation loses nothing. Before /done, the plugin can still recover: it reads its working file (`_build.md` or `_plan.md`) rather than relying on the conversation, so an interrupted build or planning session picks up from the file. But closing with /done first is the clean habit — it's the moment the work becomes a permanent record instead of something the plugin has to reconstruct.

## Can I edit SPEC.md while doing a build?

No. SPEC.md is read-only during builds to prevent the spec from shifting under active work. Spec issues get noted for /plan. Edit freely during /plan.

## I just had an idea for a feature. How do I record it without losing my train of thought?

Tell Claude. It gets added to Captures without derailing current work. Next /plan session picks it up for discussion and routing.

## The queue is empty. Does that mean the project is done?

No — an empty queue is a normal resting state. Run /plan when you have ideas or want to review. The project is done when you say it is.

## What is _build.md? Should I edit it?

The active build's working file. It does four jobs: carries the batch being built (so QUEUE.md stays free while the build runs), lists which files the build may change (the plugin's safety check blocks edits to anything else), ticks off finished steps (so an interrupted session can resume without redoing work), and keeps the batch's reasoning (so /done can write the session record). Claude manages it — don't edit it. Deleted when /done closes the session; if it exists at session start, a previous build was interrupted and /next will offer to resume.

## What is _plan.md? Should I edit it?

A planning session's working file — the planning counterpart to _build.md. When /plan starts working through your captures, it creates `_plan.md` to track where it is: which items it's processing, the current one, and what it has routed so far (promoted, parked, or dropped). It does three jobs: it survives a cleared or compacted conversation, it lets an interrupted /plan pick up where it stopped, and it gives /done a record of what was decided. Claude manages it — don't edit it. /done deletes it when the planning session closes; if it exists at session start, a previous /plan was interrupted and you can resume with /plan.

## What is REGISTRY.md for?

A list of every component — what files exist and what each does. Claude updates it after every build. You don't need to maintain it.

## What happens if Claude needs to touch something outside the current batch?

Claude stops and asks. It stays within batch scope. If something else needs changing: "I need to edit [file] because [reason]. Add to scope?"

## What does "Parked" mean in the queue?

Items you've decided not to work on now but don't want to lose. During /plan, parking moves an item to the Parked subsection until revisited. Dropping removes it entirely.

Parked items carry one of two reason lines that signal whether they come back automatically:

- `Blocked by: [slug] + condition` — a trigger exists. When the named item ships or the condition fires, Claude offers to unpark it during the next /plan or /next.
- `Parked: short reason` — no trigger. The item stays parked until you bring it up; Claude won't auto-surface it.

Nothing leaves active flow without one of these — prose alone isn't enough for Claude to track it mechanically.

## What does a "Plan session here" line in the queue mean?

It's a planning checkpoint Claude placed between batches. When /next reaches it, /next stops and tells you a planning session is needed first, naming the reason — usually because the next work depends on a decision, or on findings that only get sorted out in /plan. Run /plan: it handles the named reason and removes the line, and then /next can carry on. You don't add these yourself — Claude places them when it sees a planning moment coming.

## How do I know what was done in a previous session?

Check LOG/. `index.md` has one-line summaries with commit hashes (newest first), and each line ends with the name of that session's full entry file. The entry file holds the detail — files touched, reasoning, captures routed. For design rationale, search the index, then open the named file.
