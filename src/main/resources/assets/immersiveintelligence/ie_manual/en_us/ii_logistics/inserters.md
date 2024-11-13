# meta
Inserters
No yellow this time!

# intro
|[item_display]{source:"inserters_full"}|
The Precision Insertion Device, also known as the Inserter is a device used to transfer items from one inventory to another. Item inserters consists of an arm and motors for rotation and handling the item pickup. Fluid inserters use pipes and automatic valves. Inserters are able to take and output from and to any horizontal side desired.

# intro_2
The Inserter's sides can be configured with a hammer (sneak to set output). Inserters require electricity to run and data to tell them what to do.

# basic_inserter
|[crafting]{source:"inserter_basic"}|
The basic inserter is used for simple, but controlled item transfer. Made of basic circuits and mechanical components, it is a cheap and easy way to determine how many items should be transferred from one block to others.

# basic_commands_1
|[data_variable]{type:"integer", direction:"in", letter:"e", name:"Expire after", description:"Optional. After what amount of operations should the task (request) end."}|
|[data_variable]{type:"integer", direction:"in", letter:"t", name:"Item Take Amount", description:"Optional. Max amount of items the inserter will take per turn."}|
|[data_variable]{type:"string", direction:"in", letter:"i", name:"Input Direction", description:"Optional Values:, north, east, south, west or Integer 0-2"}|

# basic_commands_2
|[data_variable]{type:"string", direction:"in", letter:"o", name:"Output Direction", description:"Optional Values: north, east, south, west, or Integer 0-2."}|
|[data_variable]{type:"integer", direction:"in", letter:"1", name:"Input Distance", description:"Optional. Integer 1-2."}|
|[data_variable]{type:"integer", direction:"in", letter:"0", name:"Output Distance", description:"Optional. Integer 1-2."}|

# advanced_inserter
|[crafting]{source:"inserter_advanced"}|
The Advanced Inserter is an upgraded version of the Inserter, which allows filtering, and picking certain items, along with allowing crates and barrels to be picked from minecarts. The Advanced Inserters are much faster, but they require more power

# advanced_commands
***Input Variables:***
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"possible values: add, remove, and clear."}|
|[data_variable]{type:"string", direction:"in", letter:"a", name:"Action", description:"Possible values: item, block_place, form_minecart, into_minecart."}|
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"Stack", description:"Optional. Itemstack or oreDict String the items have to match"}|

# fluid_inserter
|[crafting]{source:"inserter_fluid"}|
The fluid Inserter is a precise limiter of fluid transfer. In some situations, you want to insert only a specific amount of fluid. This device does exactly that

# fluid_inserter_commands

|[data_variable]{type:"string", direction:"in", letter:"m", name:"Fluid take Mode", description:"possible values: set,
add"}|
|[data_variable]{type:"integer", direction:"in", letter:"c", name:"Amount of fluid to take", description:"in Milibuckets"}|
