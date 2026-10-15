# meta
任务系统与物流标签
保持简单，傻瓜

# intro
**任务系统**是一种通过单个可迭代任务来组织机器工作安排的概念。许多数据设备都使用任务，例如[机械臂](inserters.md)、[打包机](packer.md)和[数据路由器](small_data_devices.md#router)。任务分为两组：
**作业**——除非手动移除，否则将无限执行的任务。
**请求**——临时性任务，完成后（例如，在机械臂拾取了特定数量的物品后）会自动移除自身。
# details
默认情况下，任务按从旧到新的顺序执行。如果某个任务无法执行，它将被跳过。在完成一个[请求]（**无论成功与否**）后，会检查它是否应被移除。
# logitags
|[item_display]{source:"logitag_item"}|
**物流清单**，简称 **LogiTags**，是 II 物流机器（如[打包机](packer.md)和[机械臂](inserters.md)）使用的信息标签。
它们包含关于货物的名称、文本描述、[所有者身份]、起点、目的地、颜色标记和批次号等信息，并且可以通过合成应用于[板条箱](engineers_crates.md)等容器。
# logitag_printing
要创建**物流清单**，需要一台装有[空白页]的[印刷机](../ii_data/printing_press.md)。
将清单信息传递给机器的最简单方式是使用单个[LogiTag](../ii_data/data_types.md#logitag)类型变量。<br>
|[data_variable]{type:"logitag", direction:"in", letter:"l", name:"物流标签", description:"将要打印的物流标签", requirements:{m:"logi_tag"}}|
由于此类型被视为[高级类型]，因此需要升级过的[数据输入机](../ii_data/data_input_machine.md)。
# logitag_printing2
另一种传递**LogiTag**信息的方式是使用现有物流清单物品的[物品堆叠](../ii_data/data_types.md#itemstack)。<br>
|[data_variable]{type:"itemstack", direction:"in", letter:"s", name:"物流标签", description:"将要打印的物流标签", requirements:{m:"logi_tag"}}|
也可以使用一组数据包变量来完成：<br><br>
|[data_variable]{type:"string", direction:"in", letter:"n", name:"名称", description:"货物的名称"}|
|[data_variable]{type:"string", direction:"in", letter:"d", name:"描述", description:"可选的长文本描述"}|
# logitag_printing3
|[data_variable]{type:"integer", direction:"in", letter:"b", name:"批次号", description:"同名称系列货物中的编号"}|
|[data_variable]{type:"string", direction:"in", letter:"f", name:"发件人", description:"发件人姓名"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"收件人", description:"收件人姓名"}|
|[data_variable]{type:"string", direction:"in", letter:"o", name:"所有者", description:"该货物所有者的身份标识"}|
|[data_variable]{type:"string", direction:"in", letter:"p", name:"颜色", description:"分配给该货物的颜色，十六进制格式"}|
# logitag_printing4
最后这种方式**不推荐**，但如果你不关心在[数据包]中占用空间，它仍然是一种可行的选择。
[LogiTag]中的[所有者身份](../ii_warfare/terrain_control/owner_identity.md)指的是通常所说的*派系系统*。