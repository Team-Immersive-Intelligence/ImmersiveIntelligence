# meta
POL
计算机革命
# intro
@level_computer
POL 是一种由[大型机]和[计算机]使用的编程语言，用于创建可执行的指令集，称为**程序**。
|[wip_notice]|
# code_compilation0
一个 **POL** 程序是由一系列语句组成的序列，每个语句占据单独的一行。在使用任何数据操作之前，你必须使用 **USE** 语句导入所需的[电路](_functional_circuits.md)库。
变量使用其类型名称声明，后跟以 **@** 为前缀的变量字母，以及可选的初始值。
# code_compilation0_example
|[pol_code]{code:"use BASIC\\n\\ninteger @a = 5\\nfloat @b = 3.14\\nstring @c = \"hello\"\\nboolean @d = 1"}| 
# code_compilation1
操作使用**波兰表示法**调用——操作符在前，后跟其参数。
操作的结果可以使用 **=** 符号存储到变量中。操作也可以使用其完整名称代替符号来调用。
# code_compilation1_example
|[pol_code]{code:";将 2 和 3 的和赋值给 @a\\ninteger @a = + 2 3\\n\\n;嵌套操作\\ninteger @b = * @a + 1 2\\n\\n;使用完整操作名\\nfloat @c = add 1.5 2.5"}| 
# code_execution0
程序默认**每个 tick 执行一条语句**。**WAIT** 语句会使程序暂停执行指定的 tick 数。
**IF** 语句检查一个条件，仅当条件满足时才执行下一条语句或代码块。**ELSE** 处理相反的情况。
# code_execution0_example
|[pol_code]{code:"integer @a = 10\\n\\nif > @a 5\\n  type \"a is big\"\\nelse\\n  type \"a is small\"\\n\\nwait 20"}| 
# code_execution1
**MARK** 和 **GOTO** 允许创建带标签的跳转点和循环。**EXEC** 跳转到标签并在之后返回，其行为类似于函数调用。
使用缩进创建的代码块（编译为 **BEGIN**/**END**）可将多条语句组合为一条。
# code_execution1_example
|[pol_code]{code:"mark loop:\\n  integer @a = + @a 1\\n  type @a\\n  if < @a 10\\n    goto loop:\\n\\nmark greet:\\n  type \"hello!\"\\nend\\n\\nexec greet:"}| 
# statements
**语句**是 **POL** 程序的构建块。每条**语句**都有一个独特的名称用于调用，并执行不同的操作。
操作的示例包括设置变量的值、停止程序或运行内部函数。
每条**语句**只占一行，但有些语句（如 if **语句**）需要另一条语句才能正常工作。
以下页面列出了所有**语句**、它们的用法以及示例用例。
# statements_use
**USE** 语句用于导入[电路](_functional_circuits.md)中的函数库，以供程序使用。
# statements_begin_end
**BEGIN** 和 **END** 语句用于标记程序的一个片段，称为*代码块*。
一个*代码块*被视为一条单独的语句，但如果它包含多条语句，则执行时间会更长，等于块内所有语句执行时间之和。
# statements_begin_end2
多个*代码块*可以互相嵌套，但每个 **BEGIN** 语句必须有一个对应的 **END** 语句。
尽管两者都是关键字，但推荐的使用方式是通过在代码中添加缩进来实现。
# statements_wait
**WAIT** 语句用于将程序暂停指定的 tick 数。
在此期间，程序不会执行任何其他语句。
# statements_sign
**SIGN** 语句用于将计算机终端上的 16 个彩色信号灯之一切换为点亮或熄灭状态。
这些状态是持久的，在另一个 **SIGN** 语句改变它们或终端断电之前，它们将保持不变。
# statements_mark
**MARK** 语句用于在程序中设置一个标签，可以通过 [GOTO](#statements_goto) 语句跳转到该标签。
**MARK** 标签不能包含空格或操作保留的名称，并且在程序内必须唯一。
# statements_goto
**GOTO** 语句用于跳转到由 [MARK](#statements_mark) 语句设置的标签。
这可用于在程序中创建循环或有条件跳转。
# statements_exec
**EXEC** 语句用于跳转到由 [MARK](#statements_mark) 语句设置的标签，然后返回到程序中的原始位置。
这可用于在程序中定义可调用的*函数*。
# statements_ext
**EXT** 语句用于从当前程序中调用另一个 POL 程序。
该程序将共享相同的内存，但不会共享调用它的程序所导入的函数和[外部设备]链接。
# statements_if
**IF** 语句用于在条件满足时执行一段代码块。
如果条件不满足，程序将跳过下一条指令或代码块。
# statements_else
**ELSE** 语句用于在前面的 [IF](#statements_if) 语句的条件不满足时执行一段代码块。
# statements_page
**PAGE** 语句用于将计算机的[内存页](data_main.md#packets_basics)切换到另一个页面。
这可用于为程序的不同例程设置独立的内存。
# statements_wipe
**WIPE** 语句用于清除计算机当前内存页的所有内容。
# statements_copy
**COPY** 语句用于将一个变量的值复制到另一个变量。
复制后的变量将具有与原始变量相同的值，但其中一个的变化不会影响另一个。
# statements_move
**MOVE** 语句用于将一个变量的值移动到另一个变量，使原始变量变为空。
# statements_swap
**SWAP** 语句用于交换内存中两个变量的值。