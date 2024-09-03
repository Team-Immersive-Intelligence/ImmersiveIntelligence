# meta
Punchtapes
Rewind, Stop, Fast-Forward!

# main
Punchtapes are [portable data storage media] that can be read and written with a variety of compatible devices, such as the [Punchtape Reader](#punchtape_reader), [Punchtape Writer](data_input_machine.md#punchtapes), [Data Input Machine](data_input_machine) and [Printing Press](printing_press.md).<br> 
|[crafting]{source:"punchtape"}|
They allow easy [exchange of information] and storing [backup] of data device configuration.

# punchtape_reader
The **Punchtape Reader** is a device used to read [punchtapes] based on a received [data packet](data_main.md#packetsbasics).
|[crafting]{source:"reader"}|<br>

# punchtape_reader2
The **Punchtape Reader** has 3 modes of reaction to redstone:
[No Redstone] - the machine won't react to redstone signals
[Send on Redstone] - the machine will repeat sending last read punchtape on receiving a redstone signal
[Redstone on Send] - the machine will emit a redstone signal when it receives a packet

# example_password_door
A very common example for using a punchtape is building a "password door", a kind of security system combining a [Punchtape Reader] and a door connected through a [Redstone-Data Interface].
|[scenario]{}|
