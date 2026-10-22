# meta
打孔纸带
倒带、停止、快进！

# main
打孔纸带是一种[便携式数据存储介质]，可以通过多种兼容设备进行读取和写入，例如[打孔纸带读取器](#punchtape_reader)、[打孔纸带写入器](data_input_machine.md#punchtapes)、[数据输入机](data_input_machine)和[印刷机](printing_press.md)。<br>
|[crafting]{source:"punchtape"}|
它们可以方便地进行[信息交换]和数据设备配置的[备份]。
# punchtape_reader
**打孔纸带读取器**是一种用于根据接收到的[数据包](data_main.md#packetsbasics)读取[打孔纸带]的设备。<br>
|[crafting]{source:"reader"}|
# punchtape_reader2
**打孔纸带读取器**有 3 种红石控制模式：
[无红石]——机器不会对红石信号做出反应
[红石触发发送]——机器在接收到红石信号时会重复发送最后读取的打孔纸带
[发送时输出红石]——机器在接收到数据包时会发出红石信号
# example_password_door
一个非常常见的打孔纸带使用示例是构建“密码门”，这是一种结合了[打孔纸带读取器]和通过[红石-数据接口]连接的门的安全系统。
|[scenario]{source:"punchtape_scenario"}|