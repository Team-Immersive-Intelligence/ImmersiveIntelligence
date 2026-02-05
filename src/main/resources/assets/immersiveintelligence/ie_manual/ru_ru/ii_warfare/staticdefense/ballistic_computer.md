# meta
Ballistic Computer
Electronically Integrated Numbers
# intro
|[multiblock]{mb:"II:BallisticComputer"}|
The Ballistic Computer is a complex calculation device that translates a position vector and bullet mass to yaw and pitch used by most [data-driven](data_main.md) [weapons](artillery_howitzer.md).
# data1
|[data_variable]{type:"integer", direction:"in", letter:"x", name:"X-coordinate", description:"The relative x-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"y", name:"Y-coordinate", description:"The relative y-coordinate"}|
|[data_variable]{type:"integer", direction:"in", letter:"z", name:"Z-coordinate", description:"The relative z-coordinate"}|
|[data_variable]{type:"float", direction:"in", letter:"m", name:"Shell mass", description:"The ballistic mass"}|

Inputting these values to the Ballistic Computer will output a ["p" (pitch)] and ["y" (yaw)] value.
# data2
|[data_variable]{type:"float", direction:"out", letter:"p", name:"Pitch value", description:"Pitch value to hit your location. From 0 to -105"}|
|[data_variable]{type:"float", direction:"out", letter:"y", name:"Yaw value", description:"Yaw value to hit your location. From 0 to 360"}|