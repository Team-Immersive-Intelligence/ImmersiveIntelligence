# meta
扫描传送带
把相机拿来！
# intro
@hammer;level_circuits
|[multiblock]{mb:"II:ScanningConveyor"}|
**扫描传送带**是一种用于通过[数据包](data_main.md#packetsbasics)进行精确[物品检测](data_types.md#itemstack)的设备。
|[text]{mb:"II:ScanningConveyor"}|
# details
当物品经过扫描传送带时，其属性会被读取，并立即以**物品堆叠**数据类型的形式通过数据包发送。
要进一步处理接收到的数据包，可以使用装有[相应电路](_functional_circuits.md#itemstack)的[ALM](arithmetic_logic_machine.md)来从物品堆叠中提取所需信息。
# uses
**扫描传送带**最常用的用途是作为物品计数器、分类机的一部分，或作为由特定物品触发的触发器。
红石信号会使其关闭，与普通的[传送带](conveyor)一样。
# scanner_variables
|[data_variable]{type:"itemstack", direction:"out", letter:"s", name:"扫描到的物品", description:"最近扫描到的物品（包含其 ID、耐久度和 NBT）"}|