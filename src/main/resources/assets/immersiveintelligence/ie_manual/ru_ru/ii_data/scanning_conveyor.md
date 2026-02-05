# meta
Scanning Conveyor
Get the camera!
# intro
@hammer;level_circuits
|[multiblock]{mb:"II:ScanningConveyor"}|
The **Scanning Conveyor
** is a device used for precise [item detection](data_types.md#itemstack) through sending [data packets](data_main.md#packetsbasics).
|[text]{mb:"II:ScanningConveyor"}|
# details
When an item passes through the Conveyor Scanner, its properties are read and immediately sent in the form of a Data Packet using the
**ItemStack** data type.
To further process the received packet, an [ALM](arithmetic_logic_machine.md) with the [proper circuit](_functional_circuits.md#itemstack) can be used.
# uses
The **Scanning Conveyor
** is most commonly used as a part of an item counter, a sorting machine, or as a trigger, activated by a specific item.
A redstone signal will turn it off, just like the regular [Conveyor](conveyor).
# scanner_variables
|[data_variable]{type:"itemstack", direction:"out", letter:"s", name:"Scanned item", description:"Recently scanned item (with its id, durability and NBT)"}|