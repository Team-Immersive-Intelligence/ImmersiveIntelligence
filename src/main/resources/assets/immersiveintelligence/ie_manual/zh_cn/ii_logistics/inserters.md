# meta
机械臂
这次没有黄色的！
# intro
|[item_display]{source:"inserters_full"}|
<br>精准插入设备，也称为“机械臂”，是一种用于将物品从一个容器转移到另一个容器的装置。物品机械臂由一个机械臂以及用于旋转和抓取物品的电机组成。流体机械臂则使用管道和自动阀门。机械臂可以从任意水平方向取物，也可以向任意水平方向输出。
# intro_2
机械臂的朝向可以使用[锤子](introduction#introductionHammer)进行配置。右键点击可设置输入方向，设备将转向该方向。潜行点击可设置输出方向。输出方向可以是除输入侧以外的任意一侧。<br>
机械臂需要电力才能运行，同时需要[数据](data_main.md)来指示其执行操作。
# basic_inserter
|[crafting]{source:"inserter_basic"}|
基础机械臂用于简单但受控的物品传输。它由基础电路和机械部件制成，是一种廉价且简便的方式，用于控制从某个方块向其他方块传输多少物品。
# basic_commands_1
|[data_variable]{type:"integer", direction:"in", letter:"e", name:"过期条件", description:"可选。任务（请求）应在执行多少次操作后结束。"}|
|[data_variable]{type:"integer", direction:"in", letter:"t", name:"单次取物数量", description:"可选。机械臂每次动作最多可拿取的物品数量。"}|
|[data_variable]{type:"string", direction:"in", letter:"i", name:"输入方向", description:"可选值：south, west, north, east，或整数 0-3（分别对应）。"}|
# basic_commands_2
|[data_variable]{type:"string", direction:"in", letter:"o", name:"输出方向", description:"可选值：south, west, north, east，或整数 0-3（分别对应）。"}|
|[data_variable]{type:"integer", direction:"in", letter:"1", name:"输入距离", description:"可选。整数 1-2。"}|
|[data_variable]{type:"integer", direction:"in", letter:"0", name:"输出距离", description:"可选。整数 1-2。"}|
# advanced_inserter
|[crafting]{source:"inserter_advanced"}|
高级机械臂是机械臂的升级版，允许过滤和筛选特定物品，并支持从矿车中提取箱子和桶。高级机械臂速度更快，但需要更多电力。
# advanced_commands
**输入变量：**<br>
|[data_variable]{type:"string", direction:"in", letter:"c", name:"命令", description:"可选值：add（添加）、remove（移除）和 clear（清除）。"}|
|[data_variable]{type:"string", direction:"in", letter:"a", name:"动作", description:"可选值：item（物品）、block_place（放置方块）、form_minecart（组装矿车）、into_minecart（放入矿车）。"}|
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"物品堆叠", description:"可选。物品堆叠或矿物词典字符串，物品必须与之匹配"}|
# fluid_inserter
|[crafting]{source:"inserter_fluid"}|
流体机械臂是一种精确控制流体传输的设备。在某些情况下，你可能只想输入特定量的流体，该设备正是为此而设计。
# fluid_inserter_commands
|[data_variable]{type:"string", direction:"in", letter:"m", name:"流体抽取模式", description:"可选值：set（设定）、add（追加）"}|
|[data_variable]{type:"integer", direction:"in", letter:"c", name:"流体数量", description:"要抽取的量，单位为毫桶（mB）"}|