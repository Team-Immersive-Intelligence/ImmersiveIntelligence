# meta
Ballistic Computer
Electronically Integrated Numbers

# 0
|[multiblock]{mb:"II:BallisticComputer"}|
The Ballistic Computer is a complex calculation device that translates a position vector and bullet mass to yaw and pitch used by most data-driven weapons.

# 1
|[data_variable]{type:"integer", direction:"in", letter:"x", name:"X-coordinates", description:"The x-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"y", name:"Y-coordinates", description:"The y-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"z", name:"Z-coordinates", description:"The z-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"m", name:"Shell mass", description:"The ballistic mass"}|

Inputting these value to the Ballistic Computer will output a "p" (pitch) and "y" (yaw) value.

# 2
|[data_variable]{type:"float", direction:"out", letter:"p", name:"Pitch value", description:"Pitch value to hit your location. From 0-(-105)"}|
|[data_variable]{type:"float", direction:"out", letter:"y", name:"Yaw value", description:"Yaw value to hit your location. From 0-360"}|