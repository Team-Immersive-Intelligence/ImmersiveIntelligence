# meta
Scanning Conveyor
Get the camera!

# intro
@hammer;level_circuits
|[multiblock]{mb:"II:ScanningConveyor"}|
The **Scanning Conveyor** is a device used for precise [item detection](data_types.md#itemstack) through sending [data packets](data_main.md#packetsbasics).
|[text]{mb:"II:ScanningConveyor"}|

# details
When an item passes through the Conveyor Scanner, its properties are read and immediately sent in the form of a Data Packet using the **ItemStack** data type.
Same as the regular [Conveyor](conveyor), using a redstone signal turn it off. 

To further process the received packet an ALM with the [proper circuit](functional_circuits.md#itemstack) can be used. 
It is most commonly used as a part of an item counter, a sorting machine, or as a trigger, activated by a specific item.

# scanner_variables
|[data_variable]{type:"itemstack", direction:"out", letter:"s", name:"Scanned item", description:"Recently scanned item (with its id, durability and NBT)"}|