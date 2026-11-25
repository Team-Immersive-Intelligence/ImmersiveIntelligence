# meta
Redstone Sequencer
Now Trains arrive on Time
# sequencer
|[crafting]{source:"device"}|
The [Redstone Sequencer] emits configurable strength at its orange output port. Data commands arrive at the opposite blue port.
Create repeating [Jobs] or finite [Requests]. They execute in list order. Jobs remain in the sequence, requests are removed after their repeat count reaches zero.
# timing
Each step has a strength from 0 to 15 and a duration in ticks. Add zero-strength steps to create gaps between pulses.
[Constant signal] holds the selected strength until the next command advances or restarts the sequence. [Running] starts or stops output.
The sequence and its current progress survive world saves.
# commands
Set string [c] to a command:
[add] appends a step; [remove] deletes the zero-based index in integer [i]; [clear] deletes all steps.
[start] restarts from the first step. [stop] turns output off. [next] completes the current step, including a constant step.
# parameters
For [add], numeric [s] sets strength and [t] sets duration. Boolean [h] enables constant output. Boolean [j] selects a repeating job.
Numeric [e] selects a finite request and sets its repeat count. Defaults are strength 15, duration 20 ticks and a repeating job. Strengths and durations are clamped to valid ranges.
# editing
The screen edits a snapshot of the sequence. Stop the sequencer before editing requests which are being consumed.
If data commands or request completion change the sequence while its screen is open, the server rejects the stale edit. Close and reopen the screen to load the current sequence.
