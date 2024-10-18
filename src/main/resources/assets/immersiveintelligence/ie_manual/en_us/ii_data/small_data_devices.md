# meta
Small Data Devices
The Gray Future Cubes

# combiner
|[crafting]{source:"data_combiner"}|  
The [Data Combiner] is a machine which can combine two data inputs from the sides into an output to the front. 
The machine has a toggle for the preferred side to take variable from. 

# combiner2
Each variable also has a side override mode, green is the left and red is the right, darker colors remove the variable if the latest packet from that side doesn't contain the variable.

# router
|[crafting]{source:"data_router"}|  
The [Data Router] allows a packet to be directed to a specific side, defined by the integer variable '0'.
Variable 0 accepts the following integers:

Bottom - 0, Top - 1
North - 2, South - 3
West - 4, East - 5

# redstone_buffer
|[crafting]{source:"redstone_buffer"}|  
The [Redstone Buffer] is a device which can hold an incoming data packet, until it doesn't receive a redstone signal.
**Caution:** a held data packet will be overwritten by the arrival of a new one.

# timed_buffer
|[crafting]{source:"timed_buffer"}|  
The [Timed Buffer] is a device which can hold an incoming data packet for a given amount of time. The duration is set by the integer variable '0': the number of ticks to wait.
**Note:** If the buffer recieves a new variable 0 while counting down, the buffer timer will reset.

# small_buffer
|[crafting]{source:"small_data_buffer"}|
The [Small Data Buffer] is a device which can hold up to 4 packets and can release them using the First-In-First-Out method when given a redstone signal.