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
<br>
All other variables will be returned with the [queried values](data_types.md) in the reply packet, or will [not be included](data_types.md#null) if no such property can be returned by the callback recipient.
# scenario0
To better illustrate this concept, let's take a look at the scenario below.
|[scenario]{}|
# scenario1
First, the [Data Input Machine](data_input_machine.md) sends a following packet to the machine through a [Duplex Connector](data_wiring.md#duplexconnector). We will be using a printing press with one bucket of Black ink and two buckets of Cyan for this example.
|[data_packet]{h:32,data:{j:{Type:"string",Value:"get_ink"},k:{Type:"string",Value:"get_onk_cyan"},i:{Type:"string",Value:"get_ink_cyan"}}}|
The duplex connector is used to separate the input and output onto different color channels, to protect the network from clutter and/or potentially breaking the setup.
# scenario2
Finally, to receive the [reply] from the machine, a [Debugger](data_wiring.md#debugger) was connected.
The output Packet looks as follows:
|[data_packet]{h:22,data:{j:{Type:"integer",Value:1000},i:{Type:"integer",Value:2000}}}|
The variables that contained [queries] were replaced with [answers] to them. One of the queries was invalid (there was a typo), therefore [nothing](data_types.md#null) was returned.
# scenario3
Now it's up to the Engineer *(that's you!)* to decide what to do with these received outputs. *Perhaps it's a starting
point of an automated supply system?*