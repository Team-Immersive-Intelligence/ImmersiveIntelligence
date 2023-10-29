# meta
Scanning Conveyor
Get the camera!

# 0
|[multiblock]{mb:"II:ConveyorScanner"}|
The Scanning Conveyor is a device consisting of a conveyor, a camera and a few electronic circuits used for precise item detection.
When an item passes through the Conveyor Scanner, its properties are read and immediately sent in the form of a Data Packet using the **ItemStack** data type.

# 1
To operate, the machine requires electricity. To form it use a hammer on the conveyor facing the same direction as you.

The Scanning Conveyor is not very capable on its own as it is, essentially, a detector. To process the signal you can use an ALM with the proper circuit. 
It is most commonly used as a part of an item counter or a trigger, activated by a specific item.

# data_scanner_variables
ie.manual.entry.scanning_conveyor.vars_out.s.main=Scanned item
ie.manual.entry.scanning_conveyor.vars_out.s.sub=Scanned item (with its id, durability and NBT), process using the ItemStack circuit