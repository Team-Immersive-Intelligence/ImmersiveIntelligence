# meta
Packer
Even more Pipes and Tunnels!

# intro
|[multiblock]{mb:"II:Packer"}|
The Packer is a machine used to batch load items into containers, such as crates or bullet magazines. The packer requires a data signal determine how many items should be packed per operation.
To form it, use a hammer on the 

# details
By default, the packer offers 54 storage slots for items. They can be inputted through a single conveyor on the machines back.
To operation the machine, it requires electricity and a container to be provided on the 3 block long conveyor.
For the packing orders, the machine uses a [Task System](task_system.md). Tasks can be added, removed and tweaked through its interface, or the data system.

# details_2
The "Pack" task is used to load items into the provided container. By default, the packer can take any item (the * symbol) and the maximum possible amount of it. This can be changed by selecting a different mode. You can choose between taking in a select number of invintory slots, or number of items. The amount can be specified in the interface, in the input field below.

# details_3
The "Use OreDict" switch determines whether items with the same Ore Dictionary key will be matched. The "NBT Sensitive" switch determines weather the item has to have the same NBT tag as the one provided. This is disabled by default  

# details_4
The "Unpack", "Fill", "Unfill", "Charge" and "Discharge" tasks work in a similar way as the "Pack" task. For fluids, "Slots" are tanks and the amount is the fluid amount in mB. For energy, only the amount is taken into consideration.

# data_1
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"Possible values: add, remove, clear."}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Action", description:"Possible Values: Item, Fluid, energy."}|
|[data_variable]{type:"integer", direction:"in", letter:"s", name:"Stack", description:"Optional. the ItemStack or OreDict String the items have to match."}|

# data_2
|[data_variable]{type:"boolean", direction:"in", letter:"m", name:"Mode", description:"Possible values: amount, slot, all_possible. Optional, by default all_possible."}|
|[data_variable]{type:"boolean", direction:"in", letter:"e", name:"Expire After", description:"Optional. After how many cycles should the task end."}|