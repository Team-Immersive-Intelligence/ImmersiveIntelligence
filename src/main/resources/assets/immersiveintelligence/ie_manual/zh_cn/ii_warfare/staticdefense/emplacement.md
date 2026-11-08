# meta
武器站
锁定并装填！
# 0
@hammer;level_circuits;upgradeable;repairable;paintable
|[multiblock]{mb:"II:Emplacement"}|
**武器站**是一种重型防御结构，用于保护工厂的周边区域。要形成该多方块结构，请在中间上方的沙袋方块上使用[锤子](introduction#introductionHammer)。
# 1
**武器站**的防御范围取决于所安装的[模块](emplacement_weapons)。
武器站默认未安装武器。相反，需要使用[扳手]在工事上组装武器。
基础工事结构还可以接受适用于所有武器模块的通用升级。请参阅[炮台升级](#upgrades)。
# 1_1
使用扳手交互时，你可以选择要安装的模块，每个模块都需要各自的资源来建造。选择武器后，你需要使用[电动扳手](electric_tools.md#wrench)与工事交互并拥有所有所需物品来完成组装。
# 2
**武器站**需要[电力]、[弹药]，并且默认需要[红石信号]才能运行。如果红石信号被切断，武器将缩回工事内部。
不同的武器需要不同类型的[弹药](bullet_production)，例如，[机炮](emplacement_weapons.md#flak0)需要的口径比[机枪巢](emplacement_weapons.md#mg0)更大。
# 3
**武器站**的武器可能会受损并自动修复。你可以设置炮台武器在进入修复状态之前的耐久度最低阈值。
在修复期间，武器会隐藏在工事内部，并且不会响应命令。请注意，如果武器本身受到足够多的伤害，武器站**可能会被摧毁**，这也意味着你将损失用于建造它的资源。
# 4
武器站允许存储最多 [4 个目标过滤任务]。每个任务有 [3 个基本组]，用于过滤实体。第一组是[类型：]生物、动物、载具或抛射体，可以指定额外参数（ID）。第二组是[名称]，第三组是[队伍]。组内的参数使用 OR 连接，然后各组之间使用 AND 连接。例如：选择队伍“a”和“b”中名称为“c”的所有生物或动物。
# 5
**注意！** 工程部(TM)对于因目标输入错误而造成的任何财产损失或人员伤亡概不负责。
*请在下方签字确认你已阅读并理解此警告。*<br>
X
------------------------------
# upgrades0
|[upgrade_display]{upgrade:"immersiveintelligence:emplacement/emergency_smoke"}|
当武器站的武器严重受损时，会释放烟雾以提供掩护并降低敌人的命中率。
|[upgrade_display]{upgrade:"immersiveintelligence:emplacement/sturdy_bearings"}|
更高质量的轴承可提高武器的最大旋转速度。但是，炮台需要供应润滑剂才能从该升级中获益。