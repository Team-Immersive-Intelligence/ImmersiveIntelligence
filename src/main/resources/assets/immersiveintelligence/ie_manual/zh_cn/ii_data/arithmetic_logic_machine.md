# meta
算术逻辑机
快速数学处理
# intro
@hammer;level_circuits
|[multiblock]{mb:"II:ArithmeticLogicMachine"}|
**算术逻辑机** 是一种电子设备，其主要任务是处理与修改数据。
|[text]{mb:"II:ArithmeticLogicMachine"}|
# details
当数据包从机器的任意一侧被接收时，它会由安装在机架中 4 个插槽之一上的[功能电路](_functional_circuits.md)进行处理。
机器会将数据包依次通过所有电路，尝试使用[表达式](data_types.md#expression)按字母顺序修改变量的值。
最后，数据包将从进入侧的对侧输出。
# details2
**__注意：__**
<br>
一个[表达式]会用其结果**覆盖变量的值**。
如果某个变量没有对应的表达式，它将**保持不变地通过**。
# expression
每个电路都提供自己的一套表达式，例如[高级算术电路](_functional_circuits.md#advanced_arithmetic)就拥有[算术电路](_functional_circuits.md#arithmetic)的所有表达式以及一些新增的表达式。
表达式还可以有一个*条件变量*，即一个[布尔](data_types.md#boolean)变量的[访问器](data_types.md#accessor)，当该值为 [TRUE] 时，表达式将不会被执行。
# accessor
该机器还提供了一种非标准数据类型，即[访问器](data_types.md#accessor)。它本身不持有任何值，但可以获取接收到的数据包中最近分配给该变量的值。

表达式中的变量可以通过点击“@”按钮切换为[访问器]。