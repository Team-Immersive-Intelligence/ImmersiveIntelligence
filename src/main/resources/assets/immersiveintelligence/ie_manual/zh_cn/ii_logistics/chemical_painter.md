# meta
化学染色器
胜过千把刷子
# intro
@hammer;level_advanced_industrial
|[multiblock]{mb:"II:ChemicalPainter"}|
**化学染色器**是一种使用墨水对物品进行精确颜色染色的机器。
|[text]{mb:"II:ChemicalPainter"}|
# details
手动染色既繁琐又不够精确，仅仅为了得到想要的颜色就可能浪费大量染料。
[化学染色器]消除了这个问题。

需要将青色、品红色、黄色和黑色墨水分别输入到机器背面的四个流体输入槽中。
电力需要接入顶部的输入接口。物品可以通过手动方式或通过朝内的传送带输入。
# uses
除了对羊毛或地毯等物品进行染色外，化学染色器还可以用于在子弹上涂刷标记，使其更易于视觉识别，或用于对[护甲](light_engineer_armor.md)和[背包](improved_capacitor_backpack.md)进行涂装。
大多数染色后的物品之后可以使用[化学浸洗槽](chemical_bath)进行清洗，以恢复其原始颜色。
# interface
颜色通过界面或数据系统进行设置。界面中有 3 个按钮用于切换颜色模型：
[R] - [RGB]
[C] - [CMYK]
[H] - [HSV]

如果需要使用数据控制，以下页面包含所需变量的描述。
# data_output
**数据输入：**<br>
|[data_variable]{type:"integer", direction:"in", letter:"p", name:"油漆颜色", description:"表示 RGB 油漆颜色的数字。其十六进制形式应为 RRGGBB 格式，其中 R 为红色，G 为绿色，B 为蓝色"}|
|[data_variable]{type:"string", direction:"in", letter:"p", name:"油漆颜色", description:"颜色的十六进制字符串代码，不带任何额外的前导或尾随字符"}|
# data_callback
回调变量
|[data_callback]{type:"integer", name:"get_energy", label:"存储能量", returns:"存储能量值（IF）"}|
|[data_callback]{type:"integer", name:"get_progress", label:"生产进度", returns:"进度值 0:1"}|
|[data_callback]{type:"integer", name:"get_color", label:"颜色", returns:"当前使用的颜色，以 RGB 整数表示"}|
|[data_callback]{type:"integer", name:"get_ink", label:"墨水存量", returns:"黑色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_cyan", label:"青色墨水存量", returns:"青色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_yellow", label:"黄色墨水存量", returns:"黄色墨水存量（mB）"}|
|[data_callback]{type:"integer", name:"get_ink_magenta", label:"品红色墨水存量", returns:"品红色墨水存量（mB）"}|