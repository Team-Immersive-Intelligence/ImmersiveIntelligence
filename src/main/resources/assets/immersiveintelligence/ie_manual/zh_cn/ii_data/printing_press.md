# meta
印刷机
工程师的纸与笔

# press0
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:PrintingPress"}|
**印刷机**是一种能够根据数据包中接收到的内容，按需打印文本的机器。
|[text]{mb:"II:PrintingPress"}|

# press1
|[machine_recipe]{source:"paper_page"}|
[打印纸]是书籍用标准纸张的特殊薄型版本。通过在[金属冲压机](metalPress)中压制一张纸来制成。
打印好的页面应尽快从印刷机中取出，因为印刷机的输出篮中最多只能存放[12 页]。

# press_usage
印刷机通过[数据系统](data_main)进行控制；所需的变量在以下页面中描述。
除了[打印纸]之外，印刷机还需要液体墨水才能将文本打印到页面上。墨水的消耗量取决于当前打印字符的颜色。
墨水在[搅拌机](mixer)中通过将水与相应染料混合制成，以生成黑色、青色、品红色或黄色墨水。
印刷机还可以[升级](#punchtapes_upgrade)以扩展其功能。

# data_inputs
**数据输入：**
|[data_variable]{type:"string", direction:"in", letter:"m", name:"输出模式", description:"将要打印的文档类型", values:[["text","包含文本的页面"],["code","包含以 POL 编程语言编写的代码的页面"],["blueprint","包含可用于手动操作或由物流无人机使用的建筑蓝图页面"],["logi_tag","用于标记容器和物品的物流标签"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"份数", description:"将要打印的页数"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"打印文本", description:"将要打印的文本内容", requirements:{m:"text/code"}}|
打印[物流清单](../ii_logistics/task_system.md#logitags)需要一组不同的变量，并在[专用页面](../ii_logistics/task_system.md#logitag_printing)中描述。

# punchtapes_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/punchtapes"}|
[纸带处理器]升级允许打印[打孔纸带](punchtapes.md)，纸带上将包含接收到的数据包中的所有变量（'a'、'm' 和 't' 除外）。
**请提供[空白打孔纸带]来代替空白纸张。**<br>
|[data_variable]{type:"string", direction:"in", letter:"m", name:"输出模式", description:"将要打印的文档类型", values:[["punchtape","打印接收数据包中变量（除了本变量自身）的打孔纸带。不使用任何墨水。"]]}|
# enveloper
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/enveloper"}|
[信封机]升级允许打印[信封中的信件](envelopes.md)，以便填写地址并发送给其他玩家。
|[wip_notice]|
# batching
|[upgrade_display]{upgrade:"immersiveintelligence:printing_press/batching"}|
[批处理机制]升级允许一次打印多页文本，使**印刷机**能够制作报纸和书籍。
|[wip_notice]|
# data_callback
**数据回调：**
|[data_callback]{type:"integer", name:"get_ink", label:"墨水存量", returns:"黑色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"青色墨水存量", returns:"青色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"黄色墨水存量", returns:"黄色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"品红色墨水存量", returns:"品红色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_energy", label:"存储能量", returns:"存储能量值（IF）"}|
|[data_callback]{type:"integer", name:"get_paper", label:"纸张数量", returns:"空白纸张页数"}|