# meta
Bullet Production
It's a shell, not a pencil!

# bullet
With the new arsenal of weaponry, the requirement for ammunition to feed your enemy is on a grand scale.
But the assembly of bullets is still a rather intricate process that requires lots of steps.

A bullet consists of two parts:  the core, the projectile and the casing for a bullet component; the casing, the case
that holds everything in place and is filled with gunpowder. The core will be inserted to the casing.


# casings
|[item_display]{source:"casing"}|

Let’s start with the **casing**. Most casings can be produced using an appropriate mold in the <link;metalPress;§o§nmetal §o§npress§r;>, but some, like the <link;explosives_mines;§o§nradio §o§nexplosives§r;5>, require crafting.<br>

# filler

|[multiblock]{mb:"II:Filler"}|

**The Filler** is a device used to fill containers with dust. It's most important purpose is filling bullet casings with gunpowder.

# filler_1
To form the filler use the hammer on the center conveyor. It requires energy to operate. When dust is placed into the input slot it is moved to the main tank, the total amount of dust stored there is shown in mB.

# PorForge
@upgradable
|[multiblock]{mb:"II:ProjectileWorkshop"}|
**Projectile Forge** is a machine used to manufacture or fill [cores]. To form it use a hammer on the left metal crate.
In default configuration, the projectile workshop produces [cores from metal nuggets](bullet_cores). 

# ProWo 2
|[item_display]{source:"cores"}|

To operate, the machine requires energy. Through its interface the produced ammunition and core type can be set. 
It is also possible to do so using the data system.

# core_component
@upgradable

The Projectile Workshop can be upgraded to become a Projectile Filler. This changes the role to filling [cores] produced by other Projectile Workshops using <link;bullet_components;§o§nComponents§r;>.

Tha amount of components can be specified using the interface or through the data system. The max amount is determined by the core type.

# ProWor 3
The core is produced in the **Projectile Forge** using various metal nuggets, such as lead. Keep in mind to use [the appropriate metal for the job](bullet_cores)

Different rounds calls for different cores. A submachine gun can't fire Artillery shells, the different cores can hold different amounts of components.

Filling with [components] is optional and may be repeated multiple times with the same or different components.


# AmmoWorkshop
|[multiblock]{mb:"II:AmmunitionWorkshop"}|
The final stage is the bullet assembly in the **Ammunition Workshop**. 
In this device, the core is inserted into the casing, remember to match the core type to the casing type.

# AmmoWorkshop2
To form the **Ammunition Workshop** use the hammer on the center steel scaffolding that is exposed.

It also sets the fuse determining under what conditions its components will detonate. Fuses can be set through the interface or a data packet. To operate, the machine requires electricity.
