# meta
Data Callback
'Tis to thee
# main
**Data Callback** is a universal method for getting information about current states of machines through
the [Data System](data_main.md).
To get a reply from a device supporting callback, the packet has to be structured like this:
|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"},a:{Type:"string",Value:"get_something"},d:{Type:"string",Value:"get_something_else"}}}|
# main1
|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"}}}|
It's required to set variable [c] to ["callback"], to indicate that the packet is a [callback query].
All other variables will be returned with the [queried values](data_types.md) in the reply packet, or will [not be included](data_types.md#null) if no such property can be returned by the callback recipient.
# scenario0
To better illustrate this concept, let's take a look at the scenario below.
|[scenario]{}|
# scenario1
First, the [Data Input Machine](data_input_machine.md) sends a following packet to the machine through a [Duplex Connector](data_wiring.md#duplexconnector). We will be using a printing press with one bucket of Black ink and two buckets of Cyan for this example.
|[data_packet]{h:32,data:{j:{Type:"string",Value:"get_ink"},k:{Type:"string",Value:"get_onk_cyan"},i:{Type:"string",Value:"get_ink_cyan"}}}|
This connector's special property is that it has separate colors for input and output, meaning the network won't get cluttered by packets trying to arrive at all the endpoints and potentially breaking the setup.
# scenario 2
Finally, to receive the [reply] from the machine, a [Debugger](data_wiring.md#debugger) was connected.
The output Packet looks as follows:
|[data_packet]{h:22,data:{j:{Type:"integer",Value:1000},i:{Type:"integer",Value:2000}}}|
The variables that contained [queries] were replaced with [answers] to them. One of the queries was invalid (there was a typo), therefore [nothing](data_types.md#null) was returned.
Now it's up to the Engineer to decide what to do with these received outputs, *perhaps it's a starting point of an automated supply system?*