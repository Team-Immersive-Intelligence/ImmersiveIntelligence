# meta
数据输入机
输入输出器
# intro
@hammer;level_circuits;upgradable
|[multiblock]{mb:"II:DataInputMachine",h:76}|  
**数据输入机**，简称 **DIM**，是一种用于设计**数据包**、将其发送至[数据网络](data_wiring)以及对[打孔纸带](punchtapes)进行操作的工具。
|[text]{mb:"II:DataInputMachine"}|
# setup
该机器的*用户界面*由两个部分组成。
|[item_display]{source:"punchtape",ornate:true}|
**存储**部分用于存放**数据存储设备**，例如[打孔纸带]和[内存电路]。
|[item_display]{source:"data_coil",ornate:true}|
**数据**部分用于编程由机器存储的数据包。
# editing_packets
**DIM** 会持久存储单个**数据包**，可通过*界面*轻松编辑。*数据部分*存储了一个**变量**列表。
按下加号按钮即可添加一个变量。
编辑变量时，可以通过左侧的上下箭头更改其字母，通过右侧的箭头更改其[类型](data_types.md)。
# punchtapes
**数据输入机**能够从[已写入的打孔纸带]中读取**数据包**，并将当前存储的**数据包**写入[空白纸带]。
这两种操作都可以通过将[打孔纸带]插入*上方槽位*来执行。处理完成后，[打孔纸带]将被输出至*下方槽位*。
# advanced_data_upgrade
|[upgrade_display]{upgrade:"immersiveintelligence:advanced_data"}|
**DIM** 可以通过安装专用控制面板进行升级，以支持输入[向量](data_types.md#vector)和[逻辑标签](data_types.md#logitag)等高级数据类型。