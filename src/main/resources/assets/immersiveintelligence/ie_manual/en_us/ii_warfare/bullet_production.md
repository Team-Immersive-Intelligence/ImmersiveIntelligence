# meta
Bullet Production
It's a shell, not a pencil!

# bullet
With a new arsenal of weaponry, the required ammunition to feed to your enemy is on a grand scale.
However, the assembly of bullets is still a rather intricate process that requires lots of steps.

A bullet consists of three parts:  the core, the projectile and the casing for a bullet component; the casing 
holds everything in place and is filled with gunpowder. The core will then be inserted to the casing.


# casings
|[item_display]{source:"casing"}|
Let’s start with the **casing**. Most casings can be produced using an appropriate mold in the [Metal Press] but some, like the [Radio Explosives](explosives_mines.md#radio_satchel0), require crafting.

# filler
|[multiblock]{mb:"II:Filler"}|
**The Filler** is a device used to fill containers with dusts. It's most important usage is filling bullet casings with gunpowder.

# filler_1
To form the filler, use the hammer on the center conveyor. It requires energy to operate. When dust is placed into the input slot it is moved to an internal inventory. The total amount of dust stored there is shown in mB.

# projectile_workshop
@upgradable
|[multiblock]{mb:"II:ProjectileWorkshop"}|
The **Projectile Workshop** is a machine used to manufacture or fill [cores]. To form it use a hammer on the left metal crate.
In default configuration, the projectile workshop produces [cores from metal nuggets](bullet_cores).
To operate, the machine requires energy. Through its interface the produced ammunition and core type can be set. It is also possible to do so using the data system.

# core_component
@upgradable
The Projectile Workshop can be upgraded to become a Projectile Filler. This changes the role to filling [cores] produced by other Projectile Workshops using [Bullet Components](bullet_components.md).
An amount of components can be specified using the interface or through the data system. The max amount is determined by the core type.

# projectile_workshop2
The core is produced in the **Projectile Workshop** using various metal nuggets, such as lead. Keep in mind to use [the appropriate metal for the job](bullet_cores.md).
Different rounds calls for different cores. A submachine gun can't fire Artillery shells, the different cores can hold different amounts of components.
Filling with [components] is optional and may be repeated multiple times with the same or different components.

# ammunition_assembler
|[multiblock]{mb:"II:AmmunitionAssembler"}|
The final stage is the bullet assembly in the **Ammunition Assembler**.
In this device, the core is inserted into the casing, remember to match the core type to the casing type.

# ammunition_assembler2
To form the **Ammunition Assembler** use a hammer on the center steel scaffolding that is exposed.
It also sets the fuse determining under what conditions its components will detonate. Fuses can be set through the interface or a data packet.

# heavy_ammuniton_assembler
|[multiblock]{mb:"II:HeavyAmmunitionAssembler"}|
This device is formed by using a hammer on the heavy engineering block between the wooden scaffolding
(Currently WIP)