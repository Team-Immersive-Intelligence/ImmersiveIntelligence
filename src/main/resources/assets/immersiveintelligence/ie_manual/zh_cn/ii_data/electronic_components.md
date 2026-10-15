# meta
电子元件
组装完成了？
# intro
|[item_display]{source:"all_circuits"}|
**电路**是最重要且最常用的电子部件之一。
共有四种**电路**，在以下页面中描述。

# circuit_types
[基础电路](#basic_general)——一种使用真空管和红石制成的初级电路。
[高级电路](#advanced_general)——一种增强型电路，使用[高级电子合金(TM)]和改进的真空管。
[加密电路](#cryptographic_board)——一种增强型电路，专为加密信息并防止窥探而设计。
[处理器电路](#processor_general)——最精密的电路，主要通过硅在[复杂的组装过程](precision_assembler)中制成。
# blueprints
所有类型的电路板都需要专门的蓝图来制作。
|[crafting]{source:"circuit_blueprints"}|
*该蓝图的设计旨在*[*匹配*]*将要使用的电子元件。*
# basic_general
|[item_display]{source:"basic_general"}|
**基础电路**是常见且廉价的电路。
其通用设计使其易于[改装](_functional_circuits.md)以执行简单的数据操作，但它们的主要用途是构建各种**数据设备**，例如[印刷机](printing_press)、[算术逻辑机](arithmetic_logic_machine)和[数据输入机](data_input_machine.md)。
# basic_components
|[item_display]{source:"basic_electronic_element"}|
**基础电子元件**是在[精密组装机](precision_assembler.md)中，使用[镍板](metalPress)、四个[红石]和一个[真空管](components#2.md)制成的。
# basic_board
|[blueprint]{source:"basic_circuit_blueprints"}|
要制作基础裸板，需要使用[绝缘玻璃]和铜板。之后，必须在[化学浸洗槽](chemical_bath.md)中使用[蚀刻酸]对板子进行蚀刻。
这会形成电路板正常运行所需的连接。
# advanced_electronic_alloy
|[item_display]{source:"advanced_electronic_alloy"}|
受工程部保护的**高级电子合金(TM)** 是一种*导电且耐用*的复合材料，用于制造[高级电路](#advanced_board)。
# advanced_electronic_alloy_craft
|[crafting]{source:"advanced_electronic_alloy"}|
该合金由康铜、琥珀金和铂以 3:2:1 的比例混合制成，产出三个单位。
由于需要先进的冶金工艺，将**高级电子合金(TM)** 加工成其他形态需要使用[电弧炉](arcfurnace)。
# advanced_general
|[item_display]{source:"%SECTION%"}|
**高级电路**是其前身的更精良版本，采用双层电路板、**高级电子合金(TM)** 和改进的真空管制成。
它们广泛用于[无线数据传输](radio_station)和**高级数据设备**，例如[高级机械臂](inserters.md)和[改进型自动武器](emplacement.md)。
# advanced_components
|[item_display]{source:"advanced_electronic_element"}|
**高级电子元件**是在[精密组装机](precision_assembler.md)的帮助下制造的，为其提供正确的方案，并提供一个[基础电子元件](#basic_components)以及一个高级真空管。
# advanced_board
|[blueprint]{source:"advanced_circuit_blueprints"}|
**高级电路板**是基础电路板的更精良版本。它拥有更多连接空间，并需要更高级的部件。
要制作**高级电路板**，需要将两块**基础电路板**与一块[高级电子合金](#advanced_electronic_alloy)板结合。这也会使所需的蚀刻酸量翻倍。
# cryptographic_board
|[item_display]{source:"cryptographic_circuit_board"}|
**加密电路板**是[高级电路板](#advanced_board)的特别定制版本。它用于制造[加密电路](_functional_circuits.md#cryptographer)。
# processor_general
|[item_display]{source:"%SECTION%"}|
**处理器电路**是最精密、功能最强大的电路。
由硅、高级电子合金以及各种其他材料经过复杂的组装工艺制成，它们充当[大型机计算机]、[无人机]和许多[先进武器系统]的电子大脑。
# processor_components
|[item_display]{source:"processor_electronic_element"}|
作为当代最先进的电子元件，**处理器**能够对数据执行[编程操作](data_pol.md)。
其制造工艺精密复杂，需要根据方案在[精密组装机](precision_assembler.md)中组合 32 个**晶体管**和两个**高级真空管**。
# processor_board
|[blueprint]{source:"processors_blueprints"}|
**裸处理器板**是一种紧凑型高性能电路基底，由一块**高级电子合金(TM)板**夹在两块**裸高级电路板**之间构成。