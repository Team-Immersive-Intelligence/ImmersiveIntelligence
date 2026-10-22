# meta
子弹生产
这是弹壳，不是铅笔！
# bullet
射弹武器与剑一样古老。在现代战争中，可能需要大量弹药来压制敌人。
尽管是批量生产，但[子弹]的组装仍然是一个相当复杂的过程，需要许多步骤。
幸运的是，工程部已成功开发出多款辅助生产过程的机器。
# bullet2
|[item_display]{source:"bullet_twoparts"}|
一颗[子弹]通常由两部分组成：[核心弹头](bullet_cores)和[弹壳](#casings)。弹壳
固定所有部件，并[填充](#filler)有[火药]。弹头被插入弹壳中。
可选地，子弹可以装填[额外组件](bullet_components)以增加增强效果。
# casings
所有子弹都始于**弹壳**。大多数弹壳可以使用适当的模具在[金属冲压机](metalPress)中生产，但有些，如[无线电炸药](explosives_mines.md#radio_satchel0)，则需要合成。<br>
|[item_display]{source:"casing"}|
# filler
|[multiblock]{mb:"II:Filler"}|
**填充机**是一种用于将粉末填充到容器中的设备。其最重要的用途是将[火药]填充到[子弹弹壳]中。
# filler_1
要形成填充机，请在中心传送带上使用[锤子](introduction#introductionHammer)。它需要能量才能运行。当粉末放入输入槽时，它会被移动到内部存储中。其中存储的粉末总量以 mB 显示。一份[火药]为 [100 mB]。
# projectile_workshop
@upgradable
|[multiblock]{mb:"II:ProjectileWorkshop"}|
**弹头工坊**是一种用于制造或填充[弹头]的机器。要形成该多方块结构，请在左侧的金属板条箱上使用[锤子](introduction#introductionHammer)。
在默认配置下，弹头工坊使用[金属粒生产弹头](bullet_cores)。
# projectile_ws_operation
机器运行需要能量。通过界面可以设置[生产的弹药]和[弹头类型]。也可以使用[数据系统](data_main.md)进行设置。
# projectile_workshop1
弹头在**弹头工坊**中使用各种金属粒生产。请注意[为任务选择适当的金属](bullet_cores.md)。
不同的弹药需要不同的弹头——冲锋枪不能发射炮弹！不同类型的弹头可以容纳不同数量的组件。
装填[组件](bullet_components.md)是[可选的]，并且可以使用[相同或不同的组件]进行[多次装填]。
# projectile_workshop2
|[upgrade_display]{upgrade:"immersiveintelligence:core_filler"}|
弹头工坊可以升级为[弹头填充机]。这会将其功能改为使用[子弹组件](bullet_components.md)填充由其他弹头工坊生产的[弹头]。
组件的数量可以通过界面或[数据系统](data_main.md)指定。最大数量取决于弹头类型。
# projectile_ws_data
**输入变量：**
|[data_variable]{type:"string", direction:"in", letter:"b", name:"子弹类型", description:"子弹的类型。"}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"弹头类型", description:"要制造的弹头类型。"}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"填充数量", description:"弹头填充机应放入弹头中的物品数量。"}|
# ammunition_assembler
|[multiblock]{mb:"II:AmmunitionAssembler"}|
子弹组装的最后阶段是**弹药组装机**。
通过该设备，[弹头]被插入[弹壳]中。请记住将弹头类型与弹壳类型匹配。
# ammunition_assembler2
要形成**弹药组装机**，请在暴露的中心钢制脚手架方块上使用[锤子](introduction#introductionHammer)。
它还可以设置引信，确定其组件在何种条件下会引爆。引信可以通过界面或[数据包](data_main.md)设置。
确保子弹弹壳被送入近端传送带，弹头被送入远端传送带。
最后，子弹可以在[打包机](packer.md)的帮助下装入[弹匣](magazines.md)。
# ammunition_assembler_data
**输入变量：**
|[data_variable]{type:"string", direction:"in", letter:"f", name:"引信", description:"引信属性。由关键词字符串 'fuse' 和 'fuse_config' 定义。"}|
