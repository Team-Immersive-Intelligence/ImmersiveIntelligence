# meta
Data Router
Round and Round the Packets go
# router
|[crafting]{source:"device"}|
The [Data Router] forwards packets using an ordered list of rules. Each rule selects accepted input sides, an output side, and an Allow or Deny action.
Jobs have unlimited uses. Requests have a finite use count and stop matching when that count reaches zero.
# filters
Rules can filter packet color and address, require variables, or compare one variable with an exact typed value.
A matching Allow rule sends a copy to its configured side. Later Allow rules can send additional copies. A matching Deny rule stops evaluation; it cannot recall packets already sent.
# controls
[Remove Control Variable] omits the matched value variable from the outgoing copy. If there is no value filter, it omits the required variables instead.
The old integer [0] routing convention is supplied by the default rules: 0 down, 1 up, 2 north, 3 south, 4 west, and 5 east. Those rules can be replaced with any supported filters.
