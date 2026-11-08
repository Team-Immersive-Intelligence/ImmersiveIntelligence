# meta
弹道计算机
电子集成数字系统
# intro
|[multiblock]{mb:"II:BallisticComputer"}|
弹道计算机是一种复杂的计算设备，可将相对目标位置和弹药数据转换为某些[数据驱动](data_main.md)型[武器](artillery_howitzer.md)所需的偏航角和俯仰角。
# guide
计算俯仰角和偏航角时，[弹道计算机]需要的是*相对*坐标。
这意味着对于计算机而言，[0,0,0] 是被控制机器的**中心**。请确保正确计算坐标，以免炮弹偏离目标。
[目标]可以通过[向量类型](../../ii_data/data_types.md#vector)传递（需要[高级输入设备](../../ii_data/data_input_machine.md#advanced_data_upgrade)），或通过 [x、y 和 z 坐标](../../ii_data/data_types.md#float)传递。
# target
|[data_variable]{type:"vector", direction:"in", letter:"v", name:"目标向量", description:"以向量形式表示的相对目标位置"}|
|[data_variable]{type:"float", direction:"in", letter:"x", name:"X 坐标", description:"目标的相对 X 坐标"}|
|[data_variable]{type:"float", direction:"in", letter:"y", name:"Y 坐标", description:"目标的相对 Y 坐标"}|
|[data_variable]{type:"float", direction:"in", letter:"z", name:"Z 坐标", description:"目标的相对 Z 坐标"}|
# projectile
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"弹药堆叠", description:"用于读取质量和标准初速的弹药物品"}|
|[data_variable]{type:"float", direction:"in", letter:"m", name:"抛射体质量", description:"抛射体的弹道质量。当未提供有效弹药堆叠时必需"}|
|[data_variable]{type:"float", direction:"in", letter:"f", name:"抛射体初速", description:"自定义抛射体初速。默认使用重型炮弹速度"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"弹药类型", description:"已注册的弹药类型", values:[["artillery_8bCal","重型火炮"], ["artillery_6bCal_long","中型火炮"], ["artillery_6bCal","轻型火炮"], ["mortar_6bCal","轻型火炮"]]}|
# projectile2
|[data_variable]{type:"boolean", direction:"in", letter:"d", name:"直射模式", description:"使用直射模式而非默认的弹道轨迹"}|

提供[弹药堆叠](bullet_production.md)会同时提供[质量]和[初速]值。
否则，输入数据包必须包含[质量]和[初速]的值。初速可以作为[数字](../../ii_data/data_types.md#float)传递，也可以通过提供[弹药类型]传递。
# output
输入有效的目标和抛射体数据将输出[偏航角]、[俯仰角]和[命中时间]值。
前面页面中列出的所有有效输入变量将从输出数据包中移除。
|[data_variable]{type:"float", direction:"out", letter:"p", name:"俯仰角值", description:"用于瞄准武器的俯仰角值"}|
|[data_variable]{type:"float", direction:"out", letter:"y", name:"偏航角值", description:"用于瞄准武器的偏航角值，范围 0 到 360"}|
|[data_variable]{type:"float", direction:"out", letter:"t", name:"命中前时间", description:"炮弹到达目标所需预估时间，单位为 tick"}|
# passthrough
如果**输入**到[计算机]中的是偏航角和俯仰角，它会将其直接传递到**输出**，不进行任何计算。
<br>
|[data_variable]{type:"float", direction:"in", letter:"y", name:"偏航角值", description:"直接传递到输出的输入偏航角"}|
|[data_variable]{type:"float", direction:"in", letter:"p", name:"俯仰角值", description:"直接传递到输出的输入俯仰角"}|