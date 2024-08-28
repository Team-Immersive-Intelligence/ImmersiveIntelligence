# meta
Data Input Machine
The Input Outputter

# intro
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:DataInputMachine",h:76}|  
The **Data Input Machine**, or **DIM** is a device used for designing **Data Packets**, sending them to a [Data Network](data_wiring) and operating on [Punchtapes](punchtapes).
|[text]{mb:"II:DataInputMachine"}|

# setup
The machine's *User Interface* consists of two sections.
|[item_display]{source:"punchtape",ornate:true}|
The **Storage** section is used to store **Data Storage Devices**, such as [Punchtapes] and [Memory Circuits].
|[item_display]{source:"data_coil",ornate:true}|
The **Data** section is used for programming the Data Packet stored by the machine.

# editing_packets
The **DIM** persistently stores a single **Data Packet** that can be easily edited through the *interface*. The *Data section* consists of a list of variables.
By default, there are no variables.

# editing_variables
When editing a variable, you can change its letter by using the up and down arrows, to change its type hold shift when clicking it.

# punchtapes
The **Data Input Machine** is capable of reading a **Packet** from a [Written Punchtape] and writing the currently stored **Packet** to [An Empty One].
Both operations can be performed by inserting a [Punchtape] into the *upper slot*. After processing, the [Punchtape] will be outputted into the *lower slot*.

# circuit_interface_upgrade 
The **Circuit Interface Upgrade** allows the **DIM** to instantaneously save and read data on [Memory Circuits]. It adds 3 additional *slots* to the *interface*.
Circuits inserted into the slot marked with a *red dot* will be written to, while ones inserted into the *green dot* slot will be read.
After reading, the circuit will be transferred into the *lower slot*.