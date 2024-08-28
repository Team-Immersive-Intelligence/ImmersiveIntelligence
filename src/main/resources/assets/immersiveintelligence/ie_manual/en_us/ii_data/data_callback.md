# meta
Data Callback
'Tis to thee

# main
**Data Callback** is a universal method for getting information about current states of machines through the [Data System](data_main.md)
To get a reply

# scenario0
To better illustrate this concept, let's take a look at the scenario below.
|[scenario]{}|

# scenario1
First, the [Data Input Machine](data_input_machine.md) sends a following packet to the machine through a [Duplex Connector](data_wiring.md#duplexconnector).
|[data_packet]{}|
This connector's special property is that it has separate colors for input and output, meaning the network won't get cluttered by packets trying to arrive at all the endpoints and potentially breaking the setup.

# scenario 2
Finally, to receive the [reply] from the machine, a [Debugger](data_wiring.md#debugger) was connected.
The output Packet looks as follows:
|[data_packet]{}|
The variables that contained [queries] were replaced with [answers] to them. One of the queries was invalid (there was a typo), therefore [nothing](data_types.md#null) was returned.
Now it's up to the Engineer to decide what to do with these received outputs, *perhaps it's a starting point of an automated resupply system?*