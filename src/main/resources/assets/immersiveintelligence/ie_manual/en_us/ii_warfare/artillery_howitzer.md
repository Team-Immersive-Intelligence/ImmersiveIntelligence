# meta
Artillery howitzer
Bertha's Sister

# 0
|[multiblock]{mb:"II:ArtilleryHowitzer"}|
The Precision Bombardment Device, commonly known as the §oArtillery Howitzer§r is an indirect fire weapon, which excels at attacking targets on long distances.

# 1
The howitzer emplacement consists of a concrete bunker with the gun, command tables on the sides on the top and a steel door, which can be opened by providing a redstone signal. 
Ammunition has to be provided to the conveyor going down and loaded using the "load" command, after firing, the gun empty casing has to be unloaded with the "unload" command.
The machine requires large amounts of power to operate. To form it use a hammer on the concrete block sticking out at the top.

# 2
To operate the weapon and its targeting system, data from a DIM needs to be provided.
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Action mode", description:"What action the howitzer will preform", values:[["load","Loading the device"],["unload","Unloading the device"],["fire","Will fire a loaded shell"],]}|
Shell fuse time (f) detriments the time (in ticks) for the shell to explode; default -1.
|[data_variable]{type:"integer", direction:"in", letter:"f", name:"Fuse time", description:"The amount of time before the shell detonates", values:[["(-1)-200","Expressed i ticks (20t = 1sek)"],]}|

# 3
Gun yaw (y), the yaw axis is the way the weapon is pointing, measured on a scale 0-360
|[data_variable]{type:"integer", direction:"in", letter:"y", name:"Gun yaw", description:"The way the weapon points", values:[["1-360",""]]}|
Gun pitch (p) the pitch axis determines nose up or down.
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"Gun pitch", description:"Gun nose, up or down", values:[["0-(-105)",""]]}|

# 4
Data Callback
|[data_callback]{type:"integer", name:"get_energy", label:"Energy Stored", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_state", label:"Current state", returns:"idle, loading, unloading, shooting"}|
|[data_callback]{type:"integer", name:"get_state_num", label:"Current state in numbers", returns:"0,1,2,3"}|
|[data_callback]{type:"integer", name:"get_state_progress", label:"Returns state progress in percentage", returns:"1%-100%"}|
|[data_callback]{type:"integer", name:"get_yaw", label:"Current yaw", returns:"Current yaw"}|

# 5
|[data_callback]{type:"integer", name:"get_pitch", label:"Current pitch", returns:"Current pitch"}|
|[data_callback]{type:"integer", name:"get_planned_yaw", label:"Returns planned gun yaw", returns:"Planned yaw"}|
|[data_callback]{type:"integer", name:"get_planned_pitch", label:"Returns planned gun pitch", returns:"Planned pitch"}|

# 6
|[data_callback]{type:"integer", name:"get_platform_height", label:"Returns current platform height", returns:"Stored energy amount (IF)"}|
|[data_callback]{type:"integer", name:"get_door_opened", label:"Returns current state of door", returns:"Opened or closed"}|
|[data_callback]{type:"integer", name:"get_door_opening", label:"Returns current ongoing of door", returns:"Opening or closing"}|
