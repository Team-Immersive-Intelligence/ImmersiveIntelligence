# meta
打包机
更多管道与隧道！
# intro
|[multiblock]{mb:"II:Packer"}|
打包机是一种用于将物品批量装入容器（如板条箱或弹匣）的机器。
要形成该多方块结构，请在[垂直传送带]上使用[锤子](introduction#introductionHammer)。
# details
默认情况下，打包机提供 54 个物品输入存储槽。物品可以通过机器后方的一个传送带输入。
机器运行需要电力，并且需要在 3 格长的传送带上提供一个容器。
打包订单时，机器使用[任务系统](task_system.md)。任务可以通过其界面或使用[数据系统](data_main.md)进行添加、移除和调整。
# details_2
["打包"]任务用于将物品装入所提供的容器中。默认情况下，打包机可以装入任何物品（* 符号）及其最大可能数量。
可以通过选择不同模式来更改此行为。你可以在“按指定数量的物品槽”和“按指定数量的物品”之间选择。数量可以在界面的下方输入字段中指定。
# details_20
打包机也可以从已装满的容器中["拆包"]物品。只需通过 GUI 顶部的按钮切换模式即可。物品将被拆包到 54 个输出存储槽中，并输出到机器侧面标记的槽位。
# details_3
["使用矿物词典"]开关决定了是否匹配具有相同矿物词典键值的物品。["NBT 敏感"]开关决定了物品是否必须与提供的物品具有相同的 NBT 标签。默认情况下此开关为禁用状态。
# details_4
["填充"]、["排空"]、["充能"]和["放电"]任务的工作方式与["打包"]和["拆包"]任务类似。对于流体，["槽位"]指的是储罐，数量指的是以 mB 为单位的流体量。对于能量，仅考虑数量。有关可安装的升级，请参阅[升级](#upgrades1)。
# data_1
|[data_variable]{type:"string", direction:"in", letter:"c", name:"命令", description:"可选值：add（添加）、remove（移除）、clear（清除）。"}|
|[data_variable]{type:"string", direction:"in", letter:"a", name:"动作", description:"可选值：item（物品）、fluid（流体）、energy（能量）。"}|
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"物品堆叠", description:"可选。物品必须匹配的物品堆叠或矿物词典字符串。"}|
# data_2
|[data_variable]{type:"string", direction:"in", letter:"m", name:"模式", description:"可选值：amount（数量）、slot（槽位）、all_possible（全部可能）。可选，默认为 all_possible。"}|
|[data_variable]{type:"integer", direction:"in", letter:"e", name:"过期周期数", description:"可选。任务应在多少个周期后结束。"}|
|[data_variable]{type:"boolean", direction:"in", letter:"r", name:"重复", description:"可选。任务是否应在单个周期内重复多次。"}|
# upgrades_fluid
|[upgrade_display]{upgrade:"immersiveintelligence:packer_fluid"}|
[流体加载器]将打包机的物品存储转换为[96 桶]的流体存储，并将装载机制更改为填充桶等流体容器。一个功能更强的[装瓶机](bottlingMachine)！
# upgrades_energy
|[upgrade_display]{upgrade:"immersiveintelligence:packer_energy"}|
[能量加载器]将打包机的物品存储转换为[1600 万] IF 的能量存储。装载机制被替换为一组电气端子，用于快速为电容器或设备充电。
# upgrades_railway
|[upgrade_display]{upgrade:"immersiveintelligence:packer_railway"}|
[铁路升级]将打包机的装载传送带转换为一段铁轨。这允许打包机填充[存储矿车](skycrate_system.md#minecarts)。此升级可以与其他升级组合使用。
|[wip_notice]|
# upgrades_labeler
|[upgrade_display]{upgrade:"immersiveintelligence:packer_naming"}|
[命名印章]为打包机添加了一个标签制作器，允许其设置被打包物品的名称。此升级可以与其他升级组合使用。
# data_labeling_1
|[data_variable]{type:"itemstack", direction:"in", letter:"f", name:"标签过滤器", description:"可选。用于贴标的容器过滤器。物品堆叠或矿物词典字符串。"}|
|[data_variable]{type:"integer", direction:"in", letter:"b", name:"序列批号起始值", description:"可选。贴标的起始序列号；同时将当前序列号设置为该值。"}|
|[data_variable]{type:"logistic_tag", direction:"in", letter:"i", name:"物流标签输入", description:"可选。只贴标匹配此传入标签的容器。"}|
|[data_variable]{type:"logistic_tag", direction:"in", letter:"o", name:"物流标签输出", description:"应用于被贴标容器的标签。"}|
# data_labeling_2
|[data_variable]{type:"itemstack", direction:"in", letter:"t", name:"物流标签输入（物品堆叠形式）", description:"'i' 的可选替代。提供带有物流标签的物品堆叠。"}|
|[data_variable]{type:"itemstack", direction:"in", letter:"T", name:"物流标签输出（物品堆叠形式）", description:"'o' 的可选替代。提供带有物流标签的物品堆叠。"}|
|[data_variable]{type:"integer", direction:"in", letter:"x", name:"索引", description:"可选。用于 remove/remove_label 的索引。"}|