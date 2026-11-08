# meta
数据回调
此致于卿
# main
**数据回调** 是通过[数据系统](data_main.md)获取机器当前状态信息的一种通用方法。
要从支持回调的设备获得回复，数据包必须按照如下结构构建：
|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"},a:{Type:"string",Value:"get_something"},d:{Type:"string",Value:"get_something_else"}}}|
# main1
需要将变量 [c] 设置为 ["callback"]，以表明该数据包是一个[回调查询]。

|[data_packet]{h:32,data:{c:{Type:"string",Value:"callback"}}}|
所有其他变量将在回复数据包中返回对应的[查询值](data_types.md)，若回调接收方无法返回该属性，则[不会包含该变量](data_types.md#null)。
# scenario0
为了更好地说明这一概念，我们来看下面的场景。
|[scenario]{source:"data_callback_scenario"}|
# scenario1
首先，[数据输入机](data_input_machine.md)通过[双工连接器](data_wiring.md#duplexconnector)向目标机器发送如下数据包。在本示例中，我们使用一台印刷机，其中包含一桶黑色墨水和两桶青色墨水。
|[data_packet]{h:32,data:{j:{Type:"string",Value:"get_ink"},k:{Type:"string",Value:"get_onk_cyan"},i:{Type:"string",Value:"get_ink_cyan"}}}|
双工连接器用于将输入和输出分离到不同的颜色通道上，以保护网络免受信号杂乱和/或潜在故障的影响。
# scenario2
最后，为了接收来自机器的[回复]，连接了一个[调试器](data_wiring.md#debugger)。
输出的数据包如下所示：
|[data_packet]{h:22,data:{j:{Type:"integer",Value:1000},i:{Type:"integer",Value:2000}}}|
包含[查询]的变量被替换为对应的[答案]。其中一个查询无效（存在拼写错误），因此没有返回任何[内容](data_types.md#null)。
# scenario3
现在，就交由工程师——*也就是你！*——来决定如何处理这些接收到的输出数据。*也许这会是一个自动化供给系统的起点？*