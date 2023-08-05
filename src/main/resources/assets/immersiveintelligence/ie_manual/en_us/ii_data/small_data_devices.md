# meta
Small Data Devices
The Gray Future Cubes

# combiner
The [Data Combiner] is a machine which can combine two data inputs from the sides into an output to the front. 
The machine has a toggle for the preferred side to take variable from. 

Each variable also has a side override mode, green is the left and red is the right, darker colours remove 
the variable if the latest packet from that side doesn't contain the variable.

# router
The [Data Router] allows to direct a packet into a specific side, which is defined by one of the packet's 
variables (can be set on the router by sneak using hammer). The variable has to be an integer.

Top - 0, Bottom - 1
North - 2, South - 3
East - 4, West - 5

# redstone_buffer
The [Redstone Buffer] is a device which can hold an incoming data packet, until it doesn't receive a redstone signal.

# timed_buffer
The [Timed Buffer] is a device which can hold an incoming data packet for a given amount of time. 
The duration is set by the variable '0' (a number of ticks to wait).

# small_buffer
The [Small Data Buffer] is a device which can hold up to 4 packets and can release them using the 
First-In-First-Out method when given a redstone signal.