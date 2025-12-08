# meta
Bullet Production
It's a shell, not a pencil!
# bullet
Projectile weaponry is as ancient as the sword. In modern warfare, significant amounts of ammunition may be required to overwhelm the enemy.
Despite their mass-produced nature, the assembly of [bullets] is still a rather intricate process that requires lots of steps.
Fortunately, the Engineering Department has successfully developed several machines that assist in the production process.
# bullet2
|[item_display]{source:"bullet_twoparts"}|
A [bullet] typically consists of two parts: the [core projectile](bullet_cores) and the [casing](#casings). The casing
holds everything in place and is [filled](#filler) with [gunpowder]. The core is inserted to the casing.
Optionally, bullets can be filled with [extra components](bullet_components) that add enhanced effects.
# casings
All bullets start with the **casing**. Most casings can be produced using an appropriate mold in the [Metal Press](metalPress) but some, like the [Radio Explosives](explosives_mines.md#radio_satchel0), require crafting.<br>
|[item_display]{source:"casing"}|
# filler
|[multiblock]{mb:"II:Filler"}|
**The Filler** is a device used to fill containers with dusts. The most important usage is filling [bullet casings] with [gunpowder].
# filler_1
To form the Filler, use the [hammer](introduction#introductionHammer) on the center conveyor. It requires energy to operate. When dust is placed into the input slot it is moved to an internal inventory. The total amount of dust stored there is shown in mB. One [gunpowder] is [100 mB].
# projectile_workshop
@upgradable
|[multiblock]{mb:"II:ProjectileWorkshop"}|
The **Projectile Workshop** is a machine used to manufacture or fill [cores]. To form it, use a [hammer](introduction#introductionHammer) on the left metal crate.
In default configuration, the projectile workshop produces [cores from metal nuggets](bullet_cores).
# projectile_ws_operation
To operate, the machine requires energy. Through the interface, the [produced ammunition] and [core type] can be set. It is also possible to do so using the [data system](data_main.md).
# projectile_workshop1
The core is produced in the **Projectile Workshop** using various metal nuggets. Keep in mind to use [the appropriate metal for the job](bullet_cores.md).
Different rounds calls for different cores - a submachine gun can't fire artillery shells! The different cores can hold different amounts of components.
Filling with [components](bullet_components.md) is [optional] and may be [repeated multiple times] with the [same or different components].
# projectile_workshop2
The Projectile Workshop can be upgraded to become a [Projectile Filler]. This changes the role to filling [cores] produced by other Projectile Workshops using [Bullet Components](bullet_components.md).
An amount of components can be specified using the interface or through the [data system](data_main.md). The max amount is determined by the core type.
# projectile_ws_data
**Input Variables:**
|[data_variable]{type:"string", direction:"in", letter:"b", name:"Bullet type", description:"The type of bullet."}|
|[data_variable]{type:"string", direction:"in", letter:"t", name:"Core type", description:"The type of core to make."}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Fill amount", description:"The amount of items the projectile filler should put in the core."}|
# ammunition_assembler
|[multiblock]{mb:"II:AmmunitionAssembler"}|
The final stage in bullet assembly is the **Ammunition Assembler**.
With this device, the [core] is inserted into the [casing]. Remember to match the core type to the casing type.
# ammunition_assembler2
To form the **Ammunition Assembler**, use a [hammer](introduction#introductionHammer) on the center steel scaffolding that is exposed.
It also sets the fuse determining under what conditions its components will detonate. Fuses can be set through the interface or a [data packet](data_main.md).
Ensure that bullet casings are fed to the close conveyor, and bullet cores are fed to the far one.
Finally, bullets may be loaded into [magazines](magazines.md) with the help of the [Packer](packer.md).
# ammunition_assembler_data
**Input Variables:**
|[data_variable]{type:"string", direction:"in", letter:"f", name:"Fuse", description:"Fuse properties. Defined by key strings 'fuse' and 'fuse_config.'"}|
# heavy_ammuniton_assembler
|[multiblock]{mb:"II:HeavyAmmunitionAssembler"}|
This device is formed by using a [hammer](introduction#introductionHammer) on the heavy engineering block between the wooden scaffolding.
|[wip_notice]{brief:1b}|