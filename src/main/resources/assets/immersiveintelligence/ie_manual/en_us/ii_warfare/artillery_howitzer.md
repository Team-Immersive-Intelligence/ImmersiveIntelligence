# meta
Artillery Howitzer
Bertha's Sister

# intro
@level_circuits,hammer_electric,upgradable
|[multiblock]{mb:"II:ArtilleryHowitzer"}|
The *Precision Bombardment Device*, commonly known as the **Artillery Howitzer** is an indirect weapon, excellent for attacking targets over long distances.

# forming_block
It can be formed by using the [hammer](introduction#introductionHammer) on the concrete opposite the redstone engineering block.

# details
The **Howitzer** emplacement consists of a concrete bunker with the [gun platform], a [loading mechanism], command tables and [steel door], which can be opened by providing a [redstone signal].<br>
[Ammunition](bullet_production.md#bullet) has to be provided to the [downward] conveyor and a storage for spent casings to the [upward] conveyor.
The machine requires **large amounts of power** to operate.

# operation
The **Howitzer** operates through commands issued to it through [data packets](data_main#packetsbasics).<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"What action the howitzer will perform", values:[["load","Loading ammunition to platform"],["unload","Unloading from platform"],["aim","Will aim at given angles, but will not fire"],["fire","Will fire a loaded shell at given angle"]]}|
Additionally, some commands, like [fire] and [aim] may require additional variables to perform them, an example packet might look like this:
|[data_packet]{h:34,data:{c:{Type:"string",Value:"fire"},p:{Type:"float",Value:45},y:{Type:"float",Value:123}}}|

# loading
Before the gun can be fired, it has to be loaded with some [ammunition](bullet_production.md#bullet). The gun [platform] comes with a storage for up to 4 shells for quick access during firing. To load them use one of the [load] commands.<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Load command", description:"What loading action the howitzer will perform", values:[["load","Loads a single shell"],["load_all","Loads up to 4 shells until there is no space in the platform"],["load1","Loads a shell to 1st slot"],["load2","Loads a shell to 2nd slot"],["load3","Loads a shell to 3rd slot"],["load4","Loads a shell to 4th slot"]]}|

# unloading
After the gun fires, it leaves an [empty casing] in the [platform storage]. To remove a casing or an ammunition piece from the storage use one of the [unload] commands. <br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Unload command", description:"What unloading action the howitzer will perform", values:[["unload","Unloads a single shell"],["unload_all","Fully empties the platform storage"],["unload1","Unloads the shell from 1st slot"],["unload2","Unloads the shell from 2nd slot"],["unload3","Unloads the shell from 3rd slot"],["unload4","Unloads the shell from 4th slot"]]}|

# firing
To issue a [fire] or [aim] command, the [data packet](data_main#packetsbasics) has to contain variables for [vertical] and [horizontal angle] the gun will be oriented at.
|[data_variable]{type:"float", direction:"in", letter:"y", name:"Gun yaw", description:"The horizontal angle of the weapon barrel", value:"0 to 360"}|
|[data_variable]{type:"float", direction:"in", letter:"p", name:"Gun pitch", description:"The vertical angle of the weapon barrel", value:"0 to -105"}|
Instead of passing the angle directly, a [Ballistic Computer](ballistic_computer) can be used to interpret a [3D position] and a [scanning conveyor](scanning_conveyor) can get the weight of the [shell].

# callback0
**Data Callback**
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_state", label:"Current State", returns:"idle, loading, unloading, shooting"}|
|[data_callback]{type:"integer", name:"get_state_num", label:"Current State in numbers", returns:"0,1,2,3"}|
|[data_callback]{type:"integer", name:"get_state_progress", label:"State progress in percentage", returns:"Progress value 0:1"}|
|[data_callback]{type:"integer", name:"get_yaw", label:"Current yaw", returns:"Current yaw"}|
|[data_callback]{type:"integer", name:"get_pitch", label:"Current pitch", returns:"Current pitch"}|
|[data_callback]{type:"integer", name:"get_planned_yaw", label:"Planned gun yaw", returns:"Planned yaw"}|
|[data_callback]{type:"integer", name:"get_planned_pitch", label:"Planned gun pitch", returns:"Planned pitch"}|

# callback2
|[data_callback]{type:"integer", name:"get_platform_height", label:"Current platform elevation", returns:"Progress value 0:1"}|
|[data_callback]{type:"integer", name:"get_door_opened", label:"Current state of door", returns:"Opened or closed"}|
|[data_callback]{type:"integer", name:"get_door_opening", label:"Current ongoing of door", returns:"Opening or closing"}|