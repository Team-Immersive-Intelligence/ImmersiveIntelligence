# meta
Data Callback
'Tis to thee
# main
**Data Callback** is a universal method for getting information about current states of machines through
the [Data System](data_main.md).
To get a reply from a device supporting callback, the packet has to be structured like this:
|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"},a:{Type:"string",Value:"get_something"},d:{Type:"string",Value:"get_something_else"}}}|
# main1
It's required to set variable [c] to ["callback"], to indicate that the packet is a [callback query].

|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"}}}|
All other variables will be returned with the [queried values](data_types.md) in the reply packet, or will [not be included](data_types.md#null) if no such property can be returned by the callback recipient.
# scenario0
To better illustrate this concept, let's take a look at the scenario below.
|[scenario]{source:"data_callback_scenario",h:112}|
# scenario1
The [Data Input Machine](data_input_machine.md) sends this query through a [Duplex Connector](data_wiring.md#duplex_connector). The Printing Press has 1000 mB of Black ink, 2000 mB of Cyan ink and 8000 IF of stored energy.
|[data_packet]{h:52,data:{c:{Type:"string",Value:"callback"},j:{Type:"string",Value:"get_black"},k:{Type:"string",Value:"get_ink_cyan"},i:{Type:"string",Value:"get_cyan"},e:{Type:"string",Value:"get_energy"}}}|
The duplex connector separates the query and reply onto different color channels.
# scenario2
The [reply] returns through the duplex connector to the [Debugger](data_wiring.md#debugger) beside the input machine:
|[data_packet]{h:32,data:{j:{Type:"integer",Value:1000},i:{Type:"integer",Value:2000},e:{Type:"integer",Value:8000}}}|
The variables that contained [queries] were replaced with [answers] to them. One of the queries was invalid (there was a typo), therefore [nothing](data_types.md#null) was returned.
# scenario3
Now it's up to the Engineer *(that's you!)* to decide what to do with these received outputs. *Perhaps it's a starting point of an automated supply system?*
