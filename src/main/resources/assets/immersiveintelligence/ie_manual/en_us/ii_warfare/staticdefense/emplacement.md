# meta
Emplacement
Locked and Loaded!
# 0
@hammer;level_circuits;upgradeable;repairable;paintable
|[multiblock]{mb:"II:Emplacement"}|
**The Emplacement** is a heavy defensive structure used for protecting the perimeter of your factory. To form it, use a [hammer](introduction#introductionHammer) on the upper middle sandbag block.
# 1
An **Emplacement** can defend a vast amount of area depending on what [module](emplacement_weapons) that is installed.
Emplacements do not have a weapon installed by default. Instead, the weapon is assembled on the emplacement using the [Wrench].
The base emplacement structure can also recieve common upgrades that apply to all weapon modules. Refer to [Emplacement Upgrades](#upgrades0).
# 1_1
When interacting with a wrench, you have a selection of modules to install, each requiring their own resources to be built. Upon selecting the weapon, you need to assemble it by interacting with the emplacement using the [Electric Wrench](electric_tools.md#wrench) and have all the items.
# 2
The **Emplacement** requires [electricity], [ammunition] and by default, a [redstone signal] in order to operate. If the redstone signal is cut off, the weapon will retract back into the emplacement.
The different weapons requires different types of [ammunition](bullet_production), for example, the [Autocannon](emplacement_weapons.md#flak0) requires a heavier caliber than the [Machinegun Nest](emplacement_weapons.md#mg0)
# 3
**Emplacement** weapons can be damaged and automatically repaired. You can set up the minimal threshold of the emplacement weapon's health before it goes into repair state.
During repair, it will hide inside the Emplacement and will not react to commands. Note that Emplacements **can be destroyed** if enough damage is inflicted to the gun itself, which also means that you will lose resources used to construct it.
# 4
**Decision Tree Target Selection**
The [Targets] tab stores up to [16 presets], each with its own decision tree. Select an active preset to control automatic targeting.
Each node has a [condition] and a [weight]. A target must match the root condition. Matching nodes add their weights; a failed condition skips that node and its children.
# 4_1
Weights from all matching branches are added together. A [negative final weight] excludes a target from automatic selection and the shared target list; zero remains eligible.
The weapon selects the highest-scoring target it can engage. Equal scores favour its current target. Detection, terrain visibility, weapon angle limits and attack range still apply.
# 4_2
**Target Conditions**
Conditions include entity category, registry ID, mod ID, name, current or maximum health, distance, horizontal distance, horizontal direction, on-ground, in-water, on-fire and faction relationship.
Categories include mobs, animals, players, NPCs, vehicles and artillery projectiles. Direction means [north, east, south or west] from the weapon's position, regardless of its current yaw.
# 4_3
**Example Tree**
Use an [Any] root with weight 0. Give a [Mob] child weight 10 and add an [On Fire] child beneath it with weight 5. Burning mobs score 15; other mobs score 10.
A separate [Friendly] faction branch with weight -100 excludes friendly targets in this example. Manual [Fire Missions] take priority over automatic selection and do not use tree weights.
# data0
**Data Control**
Enable [Data Control] in the Config tab to accept commands and callback requests through the data port. Send a string command in variable [c].
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Command", description:"Targeting action", values:[["fire","Queue a Fire Mission"],["aim","Aim without firing"],["stop","Pause targeting; keep missions"],["resume","Resume targeting"]]}|
Commands require operating power and respect platform, setup and repair requirements.
# data1
**Platform and Repair**
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Control command", description:"Platform or repair action", values:[["opendoor","Request platform ascent"],["closedoor","Request platform descent"],["repair","Enable or cancel forced repair"],["rscontrol","Enable or disable redstone control"]]}|
|[data_variable]{type:"boolean", direction:"in", letter:"b", name:"Enable", description:"true enables; false disables"}|
For [repair], b defaults to true. Forced repair continues to full health. [rscontrol] requires b. Redstone and service logic can override platform commands. The [door] alias only requests ascent when b is true.
# data2
**Fire Mission Queue**
|[data_variable]{type:"string", direction:"in", letter:"c", name:"Queue command", description:"Manage queued missions", values:[["clear","Clear missions and resume automatic targeting"],["target","Move mission i to the front and resume"],["targetreset","Resume and refresh automatic targeting"]]}|
|[data_variable]{type:"integer", direction:"in", letter:"i", name:"Mission index", description:"Zero-based queue index", value:"Default: 0"}|
[targetreset] keeps the mission queue and active tree preset. It does not select a different preset.
# data3
**Entity Targets**
Both [fire] and [aim] accept an entity, a relative position, or yaw and elevation. The first valid format is used, in that order.
|[data_variable]{type:"entity", direction:"in", letter:"e", name:"Target entity", description:"An entity in the same world"}|
|[data_variable]{type:"integer", direction:"in", letter:"a", name:"Shot count", description:"For fire; 0 or less is unlimited"}|
Entity missions default to unlimited shots; position missions default to one.
# data3_1
**Aiming-only Missions**
[aim] replaces an earlier aim-only mission and finishes when the requested orientation is reached. It ignores the shot count and does not fire.
Explicit missions take priority over automatic targets, while still respecting weapon limits and operating requirements.
# data4
**Relative Position Targets**
|[data_variable]{type:"vector", direction:"in", letter:"v", name:"Target offset", description:"Block offset from the weapon"}|
Alternatively, supply numeric [x], [y] and [z] for the three offset components. Here y is the [vertical offset], not yaw.
The offset is relative to the weapon's block position, not an absolute world coordinate.
# data5
**Yaw and Elevation Targets**
|[data_variable]{type:"float", direction:"in", letter:"y", name:"Target yaw", description:"0 south, 90 west, 180 north, 270 east"}|
|[data_variable]{type:"float", direction:"in", letter:"p", name:"Target elevation", description:"Degrees; positive is above the horizon"}|
|[data_variable]{type:"integer", direction:"in", letter:"d", name:"Target distance", description:"Distance in blocks; defaults to 40"}|
These values define a position to aim at. The weapon computes its firing angles according to its firing mode and limits. Its pitch callbacks use [negative pitch for upward aim].
# data7
**Facing Command: c = facing**
|[data_variable]{type:"string", direction:"in", letter:"f", name:"Facing", description:"north, east, south or west"}|
[facing] also accepts integer f: [2 north, 3 south, 4 west, 5 east]. Turrets retain target pitch and respect yaw limits. The Observer retracts before changing facing.
# data7_1
**Heavy Chemthrower Ignition**
Send [c = ignite] to change ignition on the Heavy Chemthrower.
|[data_variable]{type:"boolean", direction:"in", letter:"i", name:"Ignition", description:"true ignites; false disables ignition"}|
Other weapon modules do not accept this command.
# callback0
**Data Callbacks**
Send [c = callback] and put callback names in other string variables. Replies use those same variables. [Data Control] must be enabled; [Data Output] is not required.
|[data_packet]{h:34,data:{c:{Type:"string",Value:"callback"},e:{Type:"string",Value:"energy"},h:{Type:"string",Value:"weapon_health"}}}|
Unknown callbacks, absent weapons and unsupported weapon callbacks return [null].
# callback1
**Base Callbacks**
|[data_callback]{type:"integer", name:"energy", label:"Stored Energy", returns:"Stored energy in IF"}|
|[data_callback]{type:"boolean", name:"door", label:"Platform Requested Open", returns:"Whether the platform is requested open"}|
|[data_callback]{type:"boolean", name:"door_open", label:"Platform Fully Open", returns:"Whether ascent has finished"}|
|[data_callback]{type:"boolean", name:"door_closed", label:"Platform Fully Closed", returns:"Whether descent has finished"}|
# callback2
|[data_callback]{type:"boolean", name:"repairing", label:"Repair State", returns:"Whether the weapon is in repair state"}|
|[data_callback]{type:"boolean", name:"repair_forced", label:"Forced Repair", returns:"Whether full repair was requested"}|
|[data_callback]{type:"boolean", name:"resupplying", label:"Base Resupply", returns:"Whether Base resupply is active"}|
|[data_callback]{type:"boolean", name:"data_control", label:"Data Control", returns:"Whether data control is enabled"}|
|[data_callback]{type:"boolean", name:"data_output", label:"Data Output", returns:"Whether target sharing is enabled"}|
# callback3
**Weapon Callbacks**
|[data_callback]{type:"string", name:"weapon_name", label:"Weapon Name", returns:"Installed weapon registry name"}|
|[data_callback]{type:"float", name:"weapon_health", label:"Weapon Health", returns:"Health in half-hearts"}|
Turrets and the Infrared Observer also provide the following angle callbacks:
|[data_callback]{type:"float", name:"weapon_yaw", label:"Current Yaw", returns:"Current absolute yaw in degrees"}|
|[data_callback]{type:"float", name:"weapon_pitch", label:"Current Pitch", returns:"Current pitch in degrees; negative aims upward"}|
# callback4
|[data_callback]{type:"float", name:"weapon_target_yaw", label:"Target Yaw", returns:"Requested absolute yaw in degrees"}|
|[data_callback]{type:"float", name:"weapon_target_pitch", label:"Target Pitch", returns:"Requested pitch in degrees"}|
|[data_callback]{type:"float", name:"weapon_setup", label:"Setup Progress", returns:"0 to 1; null when no setup is used"}|
[weapon_setup_progress] is an alias for [weapon_setup].
|[data_callback]{type:"string", name:"weapon_facing", label:"Observer Facing", returns:"Infrared Observer's current cardinal facing"}|
# callback5
**Ammunition and Recoil**
Gun modules provide:
|[data_callback]{type:"integer", name:"weapon_ammo", label:"Platform Ammunition", returns:"Remaining rounds in the platform"}|
|[data_callback]{type:"boolean", name:"weapon_reloading", label:"Reloading", returns:"Whether weapon loading or unloading is active"}|
|[data_callback]{type:"itemstack", name:"weapon_loaded_ammo", label:"Loaded Ammunition", returns:"Next ammunition stack to fire"}|
|[data_callback]{type:"vector", name:"weapon_recoil", label:"Recoil", returns:"Yaw recoil, pitch recoil and zero"}|
[weapon_recoil_yaw] and [weapon_recoil_pitch] return the individual float components.
# callback6
**Module-specific Callbacks**
The Machinegun also accepts [weapon_overheat] (float), [weapon_coolant] (fluidstack), [weapon_coolant_remaining] (integer), [weapon_heavy_barrel] and [weapon_water_cooled] (boolean).
# callback7
The Heavy Chemthrower accepts [weapon_fluid] (fluidstack), [weapon_fluid_remaining] (integer) and [weapon_ignited] (boolean).
The Tesla Coil accepts [weapon_energy] and [weapon_charge] (integer), [weapon_charge_progress] (float) and [weapon_charged] (boolean).
# data_output
**Sharing Spotted Targets**
Enable [Data Output] in Config to publish the targets accepted by the active decision tree through the data port.
|[data_variable]{type:"array", direction:"out", letter:"e", name:"Spotted targets", description:"Array of entity data"}|
Entity positions are relative to the weapon's block position. The automatically selected target is placed first. Negative scores are excluded. [stop] publishes an empty list while targeting is paused.
# upgrades0
|[upgrade_display]{upgrade:"immersiveintelligence:emplacement/emergency_smoke"}|
When the Emplacement's weapon is heavily damaged, smoke will be discharged to provide cover and reduce the enemy's accuracy.
|[upgrade_display]{upgrade:"immersiveintelligence:emplacement/sturdy_bearings"}|
Higher quality bearings increase the maximum weapon rotation speed.
# 5
**Attention!** The Engineering Department (TM) is not liable for any property damage or death inflicted by incorrect targeting inputs. *Sign below to confirm you have read and understood this warning.*<br>
X
------------------------------