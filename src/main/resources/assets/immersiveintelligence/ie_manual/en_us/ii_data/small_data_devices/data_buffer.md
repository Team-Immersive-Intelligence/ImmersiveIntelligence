# meta
Data Buffer
You weren't standing here sir!
# buffer
|[crafting]{source:"device"}|
The [Data Buffer] holds 64 packets by default; its capacity can be changed in the configuration. Attach the input to the blue port and the output to the orange port. Use a hammer to rotate the device.
The device sends at most one ready packet each tick, in arrival order among ready packets. An unready packet does not block later ready packets.
# conditions
Add condition entries and select [AND] or [OR]. AND requires meeting every condition, OR requires any condition. An empty list permits all packets.
[Redstone Signal] compares the current strength with a value from 0 to 16.
[Last Received Delay] requires at least the specified ticks between successive arrivals. The first arrival has a delay of zero.
# values
[Time Delay] requires the specified ticks since arrival. Click [@] to read the delay from a numeric variable in that packet. A missing or non-numeric variable does not satisfy the delay.
[Variable Present] tests whether a letter exists. [Variable Matching] compares its type and exact value with the value set in the editor.
# queue
[Queue unmatched packets] keeps packets which fail the non-time conditions, so the queue can test them again. If disabled, those packets are discarded on arrival. Time Delay is omitted from this arrival test.
Queued packets use the full AND/OR condition list, including Time Delay. An OR condition can cause data output before a delay elapses.
# expiry
[Termination Time] is the lifetime in ticks from queue admission. Zero disables expiry. Expired packets are removed before output is tested.
Ready packets stay in the queue until output is available. New packet arrivals are discarded when the queue is full.
