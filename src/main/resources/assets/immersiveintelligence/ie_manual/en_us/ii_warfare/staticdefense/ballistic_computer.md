# meta
Ballistic Computer
Electronically Integrated Numbers
# intro
|[multiblock]{mb:"II:BallisticComputer"}|
The Ballistic Computer is a complex calculation device that translates a relative target position and ammunition data into yaw and pitch used by some [data-driven](data_main.md) [weapons](artillery_howitzer.md).
# guide
When calculating pitch and yaw, the [Ballistic Computer] expects *relative* coordinates.
This means that to the computer, [0,0,0] is the **center** of the controlled machine. Ensure that you correctly calculate the coordinates lest your shells miss their targets.
A [target] can be passed using either a [vector type](../../ii_data/data_types.md#vector), which requires an [advanced input device](../../ii_data/data_input_machine.md#advanced_data_upgrade) or [x,y, and z coordinates](../../ii_data/data_types.md#float).
# target
|[data_variable]{type:"vector", direction:"in", letter:"v", name:"Target vector", description:"Relative target position as a vector"}|
|[data_variable]{type:"float", direction:"in", letter:"x", name:"X-coordinate", description:"Relative X-coordinate of the target"}|
|[data_variable]{type:"float", direction:"in", letter:"y", name:"Y-coordinate", description:"Relative Y-coordinate of the target"}|
|[data_variable]{type:"float", direction:"in", letter:"z", name:"Z-coordinate", description:"Relative Z-coordinate of the target"}|
# projectile
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"Ammunition stack", description:"Ammunition item used to read both mass and standard velocity"}|
|[data_variable]{type:"float", direction:"in", letter:"m", name:"Projectile mass", description:"Projectile ballistic mass. Required when no valid ammunition stack is supplied"}|
|[data_variable]{type:"float", direction:"in", letter:"f", name:"Projectile velocity", description:"Custom projectile velocity. Defaults to the heavy artillery shell velocity"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Ammunition type", description:"Registered ammunition type", values:[["artillery_8bCal","Heavy Artillery"], ["artillery_6bCal_long","Medium Artillery"], ["artillery_6bCal","Light Artillery"], ["mortar_6bCal","Light Artillery"]]}|
# projectile2
|[data_variable]{type:"boolean", direction:"in", letter:"d", name:"Direct fire", description:"Use direct-fire instead of the default ballistic trajectory"}|

Providing an [ammunition stack](bullet_production.md) supplies both the [mass] and [velocity] values.
Otherwise, the input packet has to contain values for [mass] and [velocity]. Velocity can be passed as a [number](../../ii_data/data_types.md#float) or by providing an [ammunition type].
# output
Inputting valid target and projectile data will output a [yaw], [pitch] and [impact time] value.
All valid input variables listed in prior pages are removed from the output packet.
|[data_variable]{type:"float", direction:"out", letter:"p", name:"Pitch value", description:"Pitch value used to aim the weapon"}|
|[data_variable]{type:"float", direction:"out", letter:"y", name:"Yaw value", description:"Yaw value used to aim the weapon, from 0 to 360"}|
|[data_variable]{type:"float", direction:"out", letter:"t", name:"Time before impact", description:"Estimated time for the shell to reach its target, in ticks"}|
# passthrough
In case yaw and pitch angles are **input** into the [Computer], it will pass them to the **output**, without doing any calculations.
<br>
|[data_variable]{type:"float", direction:"in", letter:"y", name:"Yaw value", description:"Input yaw passed directly to the output"}|
|[data_variable]{type:"float", direction:"in", letter:"p", name:"Pitch value", description:"Input pitch passed directly to the output"}|
