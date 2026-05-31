# meta
Packer
Even more Pipes and Tunnels!
# intro
|[multiblock]{mb:"II:Packer"}|
The Packer is a machine used to batch load items into containers, such as crates or bullet magazines.
To form it, use a [hammer](introduction#introductionHammer) on the [vertical conveyor].
# details
By default, the packer offers 54 input storage slots for items. They can be inserted through a single conveyor on the rear of the machine.
To operation the machine, it requires electricity and a container to be provided on the 3 block long conveyor.
For packing orders, the machine uses a [Task System](task_system.md). Tasks can be added, removed and tweaked through its interface, or the [data system](data_main.md).
# details_2
The ["Pack"] task is used to load items into the provided container. By default, the packer can take any item (the * symbol) and the maximum possible amount of it.
This can be changed by selecting a different mode. You can choose between taking in a select number of inventory slots, or number of items. The amount can be specified in the interface, in the input field below.
# details_20
The Packer can also ["Unpack"] items from filled containers. Simply switch the mode with the button on the top of the GUI. Items will be unpacked into the 54 output storage slots and will output to the marked slot on the side of the machine.
# details_3
The ["Use OreDict"] switch determines whether items with the same Ore Dictionary key will be matched. The ["NBT Sensitive"] switch determines whether the item has to have the same NBT tag as the one provided. This is disabled by default.
# details_4
The ["Fill"], ["Unfill"], ["Charge"] and ["Discharge"] tasks work in a similar way as the ["Pack"] and ["Unpack"] tasks. For fluids, ["Slots"] are tanks and the amount is the fluid amount in mB. For energy, only the amount is taken into consideration. Refer to [Upgrades](#upgrades1) for details on what can be installed.
# data_1
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"Possible values: add, remove, clear."}|
|[data_variable]{type:"string", direction:"in", letter:"a", name:"Action", description:"Possible Values: item, fluid, energy."}|
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"Stack", description:"Optional. the ItemStack or OreDict String the items have to match."}|
# data_2
|[data_variable]{type:"string", direction:"in", letter:"m", name:"Mode", description:"Possible values: amount, slot, all_possible. Optional, by default all_possible."}|
|[data_variable]{type:"integer", direction:"in", letter:"e", name:"Expire After", description:"Optional. After how many cycles should the task end."}|
|[data_variable]{type:"boolean", direction:"in", letter:"r", name:"Repeat", description:"Optional. Whether the task should be repeated multiple times in one cycle."}|
# upgrades_fluid
|[upgrade_display]{upgrade:"immersiveintelligence:packer_fluid"}|
The [Fluid Loader] transforms the Packer's item storage into [96 buckets] of fluid storage and changes the loading mechanism to fill fluid containers like barrels. A souped-up [Bottling Machine](bottlingMachine)!
# upgrades_energy
|[upgrade_display]{upgrade:"immersiveintelligence:packer_energy"}|
The [Energy Loader] converts the Packer's item storage into [16 million] IF of energy storage. The loading mechanism is replaced with a set of electrical terminals to rapidly charge capacitors or equipment.
# upgrades_railway
|[upgrade_display]{upgrade:"immersiveintelligence:packer_railway"}|
The [Railway Upgrade] transforms the Packer's loading conveyor into a set of rails. This allows the Packer to fill [storage Minecarts](skycrate_system.md#minecarts). This upgrade can be combined with other upgrades.
# upgrades_labeler
|[upgrade_display]{upgrade:"immersiveintelligence:packer_naming"}|
The [Naming Stamp] adds a label maker to the Packer and allows it to set the name of the packed item. This upgrade can be combined with other upgrades.
# data_labeling_1
|[data_variable]{type:"itemstack", direction:"in", letter:"f", name:"Label Filter", description:"Optional. Container filter for labeling. ItemStack or OreDict String."}|
|[data_variable]{type:"integer", direction:"in", letter:"b", name:"Serial Batch Start", description:"Optional. Starting serial number for labeling; also sets current serial to this value."}|
|[data_variable]{type:"logistic_tag", direction:"in", letter:"i", name:"LogiTag Input", description:"Optional. Only label containers matching this incoming tag."}|
|[data_variable]{type:"logistic_tag", direction:"in", letter:"o", name:"LogiTag Output", description:"Tag applied to labeled containers."}|
# data_labeling_2
|[data_variable]{type:"itemstack", direction:"in", letter:"t", name:"LogiTag In (as ItemStack)", description:"Optional alternative to 'i'. Provide an ItemStack carrying a Logistic Tag."}|
|[data_variable]{type:"itemstack", direction:"in", letter:"T", name:"LogiTag Out (as ItemStack)", description:"Optional alternative to 'o'. Provide an ItemStack carrying a Logistic Tag."}|
|[data_variable]{type:"integer", direction:"in", letter:"x", name:"Index", description:"Optional. Index for remove/remove_label."}|

