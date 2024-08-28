# meta
Data
Controlling the Flow

# intro
With the growth of a facility such as a factory or power plant, grows the need for a [control system] to manage it.
For a simple use case, the old good redstone wiring will suffice, but with an increasing amount of needed connections the messy nature of redstone starts to show - and that's an appropriate moment to introduce the [Data System].

# packets_basics
**Data** is a system based on sending **Packets** of information through a [Network](data_wiring) of connected [Data Devices].
In contrast to redstone, a **Data Packet** is not a constant signal, but a singular message, sent when there's a need and received immediately after.

A **Packet** can store up to 36 [Variables] of various
[Types](data_types.md), providing lots of space to contain various information.
Each [Variable] is labelled by one of the 26 letters and numbers 0-9. 

# packets_demo1
To better illustrate things, let's take a look at an example Data Packet. Such packet can be easily created using a [Data Input Machine](data_input_machine.md) and then sent to a [Data Network] connected to it.  
|[data_packet]{h:32,data:{a:{Type:"string",Value:"Hello, World!"},b:{Type:"boolean",Value:1b},c:{Type:"integer",Value:123}}}|  
As you see, even a simple **Packet** like the above example carries much more information than what could be passed by a Redstone wire at once!

# packets_demo2
Variable 'a' is a [String](data_types.md#string) containing the text ["Hello, World!"]  
Variable 'b' is a [Boolean](data_types.md#boolean) containing the value [True], an equivalent to a [redstone "on" signal]  
Variable 'c' is an [Integer](data_types.md#integer) containing the number [123]

All those values were fitted into a single **Packet**, making the transmission much more [compact] and [straight-forward] that if it were to be built using traditional redstone circuitry.

# packets_info
Moreover, unlike Redstone, Data is [lossless], meaning that **Data Packets** **__don't lose__** any information, no matter what distance they travel.
