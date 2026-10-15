# meta
火炮榴弹炮
贝莎的姐妹
# intro
@level_circuits,hammer_electric,upgradable
|[multiblock]{mb:"II:ArtilleryHowitzer"}|
*精确轰炸装置*，通常称为**火炮榴弹炮**，是一种间接射击武器，擅长攻击远距离目标。
# forming_block
在红石工程方块对面的混凝土上使用[锤子](introduction#introductionHammer)即可形成该多方块结构。
# details
**榴弹炮**工事由一个混凝土掩体组成，包含[炮台平台]、[装填机构]、指挥桌和一扇[钢门]，可以通过提供[红石信号]来开启钢门。<br>
[弹药](bullet_production.md#bullet)需供应至[向下]的传送带，而弹壳则通过[向上]的传送带输出。
该机器需要**大量电力**才能运行。
# operation
**榴弹炮**通过[数据包](data_main#packetsbasics)发出的指令进行操作。<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"命令", description:"榴弹炮将执行的操作", values:[["load","将弹药装填至平台"],["unload","从平台卸载弹药"],["aim","瞄准给定角度，但不会开火"],["fire","以给定角度发射已装填的炮弹"]]}|
此外，某些命令如 [fire] 和 [aim] 可能需要额外的变量才能执行，示例数据包可能如下所示：
|[data_packet]{h:34,data:{c:{Type:"string",Value:"fire"},p:{Type:"float",Value:45},y:{Type:"float",Value:123}}}|
# loading
在开火之前，必须先装填一些[弹药](bullet_production.md#bullet)。炮台[平台]自带一个最多可容纳 4 发炮弹的储存架，以便在射击时快速取用。使用 [load] 命令之一进行装填。<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"装填命令", description:"榴弹炮将执行的装填操作", values:[["load","装填一发炮弹"],["load_all","装填至多 4 发炮弹，直到平台无空位"],["load1","装填一发炮弹至第 1 槽位"],["load2","装填一发炮弹至第 2 槽位"],["load3","装填一发炮弹至第 3 槽位"],["load4","装填一发炮弹至第 4 槽位"]]}|
# unloading
火炮开火后，会在[平台储存架]中留下一个[空弹壳]。要移除储存架中的弹壳或弹药，可使用 [unload] 命令之一。<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"卸载命令", description:"榴弹炮将执行的卸载操作", values:[["unload","卸载一发炮弹"],["unload_all","完全清空平台储存架"],["unload1","从第 1 槽位卸载炮弹"],["unload2","从第 2 槽位卸载炮弹"],["unload3","从第 3 槽位卸载炮弹"],["unload4","从第 4 槽位卸载炮弹"]]}|
# firing
要发出 [fire] 或 [aim] 命令，[数据包](data_main#packetsbasics)中必须包含火炮[垂直角度]和[水平角度]的变量。
|[data_variable]{type:"float", direction:"in", letter:"y", name:"火炮偏航角", description:"炮管的水平角度", value:"0 到 360"}|
|[data_variable]{type:"float", direction:"in", letter:"p", name:"火炮俯仰角", description:"炮管的垂直角度", value:"0 到 -105"}|
除了直接传递角度之外，也可以使用[弹道计算机](ballistic_computer)来解析[三维位置]，并使用[扫描传送带](scanning_conveyor)来获取[炮弹]的重量。
# callback0
**数据回调**
|[data_callback]{type:"integer", name:"get_energy", label:"存储能量", returns:"存储能量值（IF）"}|
|[data_callback]{type:"string", name:"get_state", label:"当前状态", returns:"idle（空闲）、loading（装填中）、unloading（卸载中）、shooting（射击中）"}|
|[data_callback]{type:"integer", name:"get_state_num", label:"当前状态（数字）", returns:"0、1、2、3"}|
|[data_callback]{type:"float", name:"get_state_progress", label:"状态进度（百分比）", returns:"进度值 0:1"}|
|[data_callback]{type:"float", name:"get_yaw", label:"当前偏航角", returns:"当前偏航角"}|
|[data_callback]{type:"float", name:"get_pitch", label:"当前俯仰角", returns:"当前俯仰角"}|
|[data_callback]{type:"float", name:"get_planned_yaw", label:"计划偏航角", returns:"计划偏航角"}|
|[data_callback]{type:"float", name:"get_planned_pitch", label:"计划俯仰角", returns:"计划俯仰角"}|
# callback2
|[data_callback]{type:"float", name:"get_platform_height", label:"当前平台升降高度", returns:"进度值 0:1"}|
|[data_callback]{type:"string", name:"get_door_opened", label:"门当前状态", returns:"Opened（已开）或 Closed（已关）"}|
|[data_callback]{type:"string", name:"get_door_opening", label:"门当前动作", returns:"Opening（正在打开）或 Closing（正在关闭）"}|