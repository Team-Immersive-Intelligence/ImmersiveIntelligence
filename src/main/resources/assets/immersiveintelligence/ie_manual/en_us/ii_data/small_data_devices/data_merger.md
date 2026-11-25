# meta
Data Merger
Better Together
# merger
|[crafting]{source:"device"}|
The [Data Merger], formerly called the Data Combiner, receives packets from its left and right sides and sends merged packets forwards.
Its rules specify which variables to cache from each input and which arrivals trigger output. Jobs remain active; requests expire after their configured number of accepted variables.
# cache
Each rule selects a variable and its accepted input sides. An accepted variable updates that side's cache.
[Right Overrides Cache] gives the right cache priority when both selected caches contain the variable. Otherwise the left cache wins.
# output
An arrival sends a packet only when at least one accepting rule marks its variable for sending.
The outgoing packet starts as a copy of that arrival. Selected cached variables are then applied. [Right Overrides Output] permits replacement of values already in that packet; when disabled, the cache only fills missing values.
