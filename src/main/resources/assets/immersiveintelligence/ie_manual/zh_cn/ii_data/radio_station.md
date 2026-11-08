# meta
无线电站
无线之线(TM)
# intro
@hammer_electric;level_circuits
|[multiblock]{mb:"II:RadioStation"}|
**无线电站**是一种用于无线发送、接收和中继[数据包](data_main.md#packetsbasics)的设备。由于它是一种高级结构，需要在[高级电子工程方块]上使用[电动锤](electric_tools.md)来搭建。
# details
**无线电站**可以向最远距离为 [<config;i;radio_station_range>] 格的兼容无线电设备发送[数据包](data_main.md#packetsbasics)。
恶劣的天气条件，如[下雨]或[下雪]，会[缩短这个范围]，因此，在规划无线电网络建设时，工程师应使用能够抵消此影响的范围。
|[scenario]{source:"wireless_connection"}|
# tuner
**无线电站**的频率可以通过使用**无线电调谐器**来设置。
**无线电调谐器**有两种版本，具有不同的[频率范围]。初出茅庐的工程师通常会从基础无线电调谐器开始：
|[crafting]{source:"basic_radio_tuner"}|
# advtuner
对于足够资深的工程师，可以使用**高级无线电调谐器**来访问更宽的频率范围。
|[crafting]{source:"advanced_radio_tuner"}|
# usage
要[更改]调谐器的当前频率，*潜行并向上或向下滚动滚轮*。
要[查看]设备设置的频率，*潜行右键点击*该设备即可。<br>
请注意，[某些数据设备](explosives_mines#radio_satchel0)使用受限的[基础频率范围]，因此无法将其配置为高于 <config;i;radio_station_range> 的频率。