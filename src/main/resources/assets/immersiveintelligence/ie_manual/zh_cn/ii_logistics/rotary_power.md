# meta
旋转动力
万物开始转动
# torque and speed
旋转动力是电力的一种老式替代方案。它不使用电线和连接器，而是使用[轮子]和[传动皮带]。该动力系统涉及两个因素：扭矩——以 [TU]（扭矩单位）为单位，和转速——以 [DPT]（每 Tick 角度）为单位。不同的机器需要不同的扭矩和转速才能有效工作。
像[锯木机](Sawmill.md)和[机械泵](mechanical_pump.md)这样的设备使用机械动力。
# torque and speed 2
如果 [TU] 或 [DPT] 值超过机器的限制，机器会**爆炸**。为了防止这种情况发生，聪明的工程师可以使用[齿轮箱](rotary_power.md#gearboxes)来合并、分流和管理旋转动力设备。
# transmissions
|[crafting]{source:"transmission"}|
[变速箱]用于连接产生旋转能量的机械装置。它们可以将[风车](generator#generator2)或[水车](generator#generator1)连接到[传动皮带]网络，或将机械能输出到发电机。
# gearboxes
|[crafting]{source:"gearbox"}|
[齿轮箱]用于连接多个[轮子]。其侧面可以设置为输入、输出或无连接。来自所有输入的扭矩将汇总，然后平均分配到所有输出端。你可以使用齿轮箱合并扭矩以使其更高，或在两个或多个轮子之间分配扭矩。
# wheels
|[crafting]{source:"wheels"}|
[轮子]用于在设备之间传递机械（旋转）动力。放置后，可以使用[传动皮带]将其连接到另一个轮子，方式与[连接器](wiring)用电线连接相同。
# belts
|[crafting]{source:"belt"}|
[传动皮带]是每个旋转动力网络的主要传动元件。它们的区别在于能够传递的扭矩和转速不同。如果皮带承受的扭矩过高，它会脱落并掉落到地上。
# belt_materials1
|[item_display]{source:"belt_cloth"}|
<br>**布质皮带**由坚韧的织物和皮革制成。它们的扭矩耐受性最低，长度最短，但[成本低廉]，且传输扭矩的效率尚可。
# belt_materials2
|[item_display]{source:"belt_steel"}|
<br>**钢制履带**由钢板制成。它们具有[最高的扭矩耐受性]，长度可以是布质皮带的两倍，但与其他类型相比会损失大量扭矩。
# belt_materials3
|[item_display]{source:"belt_rubber"}|
<br>**橡胶皮带**由[硫化](vulcanizer.md)橡胶制成。它们具有较好的扭矩耐受性，长度可以达到布质皮带的两倍，并且是传输扭矩效率最高的。然而，其制造的[工业要求](vulcanizer.md)远高于其他类型。
# gears
|[item_display]{source:"gears"}|
齿轮可用于更改[齿轮箱](rotary_power.md#gearboxes)的扭矩与转速比。
*扭矩/转速比在生产时镌刻在齿轮上，方便忙碌的工程师随时查阅。*