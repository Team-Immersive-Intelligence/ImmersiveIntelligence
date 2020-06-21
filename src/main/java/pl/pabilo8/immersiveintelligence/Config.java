package pl.pabilo8.immersiveintelligence;

import blusunrize.immersiveengineering.common.Config.Mapped;
import blusunrize.immersiveengineering.common.Config.SubConfig;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.fml.common.Mod;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

/**
 * Created by Pabilo8 on 2019-05-12.
 */
@Mod.EventBusSubscriber
public class Config
{
	public static final String GEARS = "Gears: Copper, Brass, Iron, Steel, Tungsten";
	public static final String BELTS = "Belts: Cloth, Steel";

	@net.minecraftforge.common.config.Config(modid = ImmersiveIntelligence.MODID)
	public static class IIConfig
	{
		@SubConfig
		public static Ores ores;
		@SubConfig
		public static Machines machines;
		@SubConfig
		public static MechanicalDevices mechanicalDevices;
		@SubConfig
		public static Tools tools;
		@SubConfig
		public static Weapons weapons;
		@SubConfig
		public static Wires wires;


		@Comment({"The maximum frequency for basic radios."})
		public static int radioBasicMaxFrequency = 32;

		@Comment({"The maximum frequency for advanced radios."})
		public static int radioAdvancedMaxFrequency = 256;

		@Comment({"Should RPM be counted in real time or ingame time"})
		public static boolean rpmRealTime = true;

		public static class Ores
		{
			@Comment({"A blacklist of dimensions in which IE ores won't spawn. By default this is Nether (-1) and End (1)"})
			public static int[] oreDimBlacklistNormal = new int[]{-1, 1};

			@Comment({"A blacklist of dimensions in which IE ores won't spawn. By default this is the Overworld (0) and End (1)"})
			public static int[] oreDimBlacklistNether = new int[]{0, 1};

			@Comment({"Set this to false to disable the logging of the chunks that were flagged for retrogen."})
			public static boolean retrogen_log_flagChunk = true;
			@Comment({"Set this to false to disable the logging of the chunks that are still left to retrogen."})
			public static boolean retrogen_log_remaining = true;
			@Comment({"The retrogeneration key. Basically IE checks if this key is saved in the chunks data. If it isn't, it will perform retrogen on all ores marked for retrogen.", "Change this in combination with the retrogen booleans to regen only some of the ores."})
			public static String retrogen_key = "DEFAULT_II";

			@Comment({"Generation config for Platinum Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_platinum = new int[]{6, 0, 10, 2, 35};

			@Comment({"Generation config for Zinc Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_zinc = new int[]{10, 35, 95, 2, 55};

			@Comment({"Generation config for Tungsten Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_tungsten = new int[]{6, 0, 35, 2, 45};

			@Comment({"Generation config for Salt Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_salt = new int[]{12, 55, 95, 1, 65};

			@Comment({"Generation config for Fluorite Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_fluorite = new int[]{12, 1, 55, 1, 10};

			@Comment({"Set this to true to allow retro-generation of Platinum Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_platinum = false;

			@Comment({"Set this to true to allow retro-generation of Zinc Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_zinc = false;

			@Comment({"Set this to true to allow retro-generation of Tungsten Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_tungsten = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_salt = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_fluorite = false;

		}

		public static class Tools
		{
			@SubConfig
			public static SkycrateMounts skycrateMounts;

			@Comment({"A modifier to apply to the ammunition resupply time of the Ammunition Crate (weapons reload)."})
			public static float ammunition_crate_resupply_time = 1.0f;

			@Comment({"The Lighter fuel capacity in milibuckets (mB)."})
			@RequiresMcRestart
			public static int lighter_capacity = 1000;

			@Comment({"The capacity of the measuring cup (mB)."})
			@RequiresMcRestart
			public static int measuring_cup_capacity = 500;

			@Comment({"The energy capacity of the electric hammer in RF... i mean IF... i mean FE... that thing o' powerin'!."})
			@RequiresMcRestart
			public static int electric_hammer_capacity = 24000;

			@Comment({"The energy capacity of the electric wirecutter."})
			@RequiresMcRestart
			public static int electric_wirecutter_capacity = 24000;

			@Comment({"The energy capacity of the electric wrench."})
			@RequiresMcRestart
			public static int electric_wrench_capacity = 24000;

			//Soon?
			@Comment({"The energy capacity of the electric multitool. (when it will be added)"})
			@RequiresMcRestart
			public static int electric_multitool_capacity = 100000;

			@Comment({"The energy usage of the electric hammer (when mining, rotating, etc. )."})
			@RequiresMcRestart
			public static int electric_hammer_energy_per_use = 100;

			@Comment({"The energy usage of the electric hammer when building advanced multiblocks (per tick)."})
			@RequiresMcRestart
			public static int electric_hammer_energy_per_use_construction = 4096;

			@Comment({"The energy usage of the electric wirecutter (when cutting wires)."})
			@RequiresMcRestart
			public static int electric_wirecutter_energy_per_use = 100;

			@Comment({"The energy usage of the electric wrench (when destroying blocks / accessing GUIs)."})
			@RequiresMcRestart
			public static int electric_wrench_energy_per_use = 100;

			@Comment({"The durability of the engineer's wrench."})
			@RequiresMcRestart
			public static int wrench_durability = 256;

			@Comment({"Max zoom of the binoculars (in Blu's unit of distance measurement™)."})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float[] binoculars_max_zoom = new float[]{0.1f, 0.125f, 0.167f, 0.25f, 0.5f};

			@Comment({"Max zoom of the advanced binoculars (in Blu's Unit of Distance Measurement™)."})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float[] advanced_binoculars_max_zoom = new float[]{0.05f, 0.0625f, 0.0833f, 0.1f, 0.25f};

			@Comment({"The energy capacity of advanced binoculars (when using Infrared Sight)."})
			@RequiresMcRestart
			public static int advanced_binoculars_energy_capacity = 4000;

			@Comment({"The energy usage of advanced binoculars (when using Infrared Sight)."})
			@RequiresMcRestart
			public static int advanced_binoculars_energy_usage = 150;

			//Durability


			@Comment({"The durability (max number of uses) of the Precission Buzzsaw."})
			@RequiresMcRestart
			public static int precission_tool_buzzsaw_durability = 150;

			@Comment({"The durability (max number of uses) of the Precission Drill."})
			@RequiresMcRestart
			public static int precission_tool_drill_durability = 250;

			@Comment({"The durability (max number of uses) of the Precission Inserter."})
			@RequiresMcRestart
			public static int precission_tool_inserter_durability = 200;

			@Comment({"The durability (max number of uses) of the Precission Solderer."})
			@RequiresMcRestart
			public static int precission_tool_solderer_durability = 150;

			@Comment({"The durability (max number of uses) of the Precission Welder."})
			@RequiresMcRestart
			public static int precission_tool_welder_durability = 250;

			@Comment({"The durability (max number of uses) of the Precission Hammer."})
			@RequiresMcRestart
			public static int precission_tool_hammer_durability = 200;

			@Comment({"The durability (max number of uses) of the Iron Sawblade."})
			@RequiresMcRestart
			public static int sawblade_iron_durability = 150;

			@Comment({"The durability (max number of uses) of the Steel Sawblade."})
			@RequiresMcRestart
			public static int sawblade_steel_durability = 300;

			@Comment({"The durability (max number of uses) of the Tungsten Sawblade."})
			@RequiresMcRestart
			public static int sawblade_tungsten_durability = 550;

			//Usage Time

			@Comment({"The usage time of the Precission Buzzsaw."})
			@RequiresMcRestart
			public static int precission_tool_buzzsaw_usage_time = 140;

			@Comment({"The usage time of the Precission Drill."})
			@RequiresMcRestart
			public static int precission_tool_drill_usage_time = 140;

			@Comment({"The usage time of the Precission Inserter."})
			@RequiresMcRestart
			public static int precission_tool_inserter_usage_time = 60;

			@Comment({"The usage time of the Precission Solderer."})
			@RequiresMcRestart
			public static int precission_tool_solderer_usage_time = 80;

			@Comment({"The usage time of the Precission Welder."})
			@RequiresMcRestart
			public static int precission_tool_welder_usage_time = 160;

			@Comment({"The usage time of the Precission Hammer."})
			@RequiresMcRestart
			public static int precission_tool_hammer_usage_time = 40;

			public static class SkycrateMounts
			{
				@Comment({"The speed of the Mechanical Skycrate Mount."})
				@RequiresMcRestart
				public static float mech_speed = 0.25f;

				@Comment({"The energy (max distance to be traveled) of the Mechanical Skycrate Mount."})
				@RequiresMcRestart
				public static float mech_energy = 35;

				@Comment({"The speed of the Electric Skycrate Mount."})
				@RequiresMcRestart
				public static float electric_speed = 1f;

				@Comment({"The energy to distance ratio of the Electric Skycrate Mount. (in Immersive Flux per Meter)"})
				@RequiresMcRestart
				public static int electric_energy_ratio = 128;

				@Comment({"The energy capacity of the Electric Skycrate Mount (in Immersive Flux)."})
				@RequiresMcRestart
				public static float electric_energy = 55;

			}
		}

		public static class Machines
		{
			@SubConfig
			public static RadioStation radioStation;
			@SubConfig
			public static DataInputMachine dataInputMachine;
			@SubConfig
			public static ArithmeticLogicMachine arithmeticLogicMachine;
			@SubConfig
			public static PrintingPress printingPress;
			@SubConfig
			public static ChemicalBath chemicalBath;
			@SubConfig
			public static Electrolyzer electrolyzer;
			@SubConfig
			public static PrecissionAssembler precissionAssembler;
			@SubConfig
			public static ArtilleryHowitzer artilleryHowitzer;
			@SubConfig
			public static AmmunitionFactory ammunitionFactory;
			@SubConfig
			public static BallisticComputer ballisticComputer;
			@SubConfig
			public static MissileSilo missileSilo;
			@SubConfig
			public static ConveyorScanner conveyor_scanner;
			@SubConfig
			public static Inserter inserter;
			@SubConfig
			public static AdvancedInserter advanced_inserter;
			@SubConfig
			public static FluidInserter fluid_inserter;
			@SubConfig
			public static AdvancedFluidInserter advanced_fluid_inserter;
			@SubConfig
			public static ChemicalDispenser chemical_dispenser;
			@SubConfig
			public static SmallDataBuffer small_data_buffer;
			@SubConfig
			public static SkyCrateStation skycrate_station;
			@SubConfig
			public static SkyCartStation skycart_station;
			@SubConfig
			public static Packer packer;
			@SubConfig
			public static RedstoneInterface redstoneInterface;
			@SubConfig
			public static Sawmill sawmill;
			@SubConfig
			public static AlarmSiren alarmSiren;
			@SubConfig
			public static ProgrammableSpeaker speaker;

			public static class RedstoneInterface
			{

			}

			public static class AlarmSiren
			{
				@Comment({"The distance the siren can be heard from."})
				public static int soundRange = 16;
			}

			public static class ProgrammableSpeaker
			{
				@Comment({"The distance the speaker can be heard from."})
				public static int soundRange = 24;
			}

			public static class Packer
			{
				@Comment({"Energy capacity of the glorious boxing device also known as The Packer."})
				public static int energyCapacity = 16000;

				@Comment({"Energy usage of the packer (after dropping a stack inside)."})
				public static int energyUsage = 512;

				@Comment({"Duration of the container being pushed by conveyor into and out of the center (in ticks)."})
				public static int conveyorTime = 60;

				@Comment({"Duration of a single stack insertion process (in ticks)."})
				public static int timeInsertion = 5;
			}

			public static class SkyCrateStation
			{
				@Comment({"Rotations per minute required for the Skycrate Station to Work."})
				public static int rpmMin = 20;

				@Comment({"Max rotations per minute (reaching over this level doesn't change effectiveness)."})
				public static int rpmEffectiveMax = 80;

				@Comment({"Max rotations per minute (will break if over)."})
				public static int rpmBreakingMax = 240;

				@Comment({"Torque required for the Skycrate Station to Work."})
				public static int torqueMin = 4;

				@Comment({"Max Torque (reaching over this level doesn't change effectiveness)."})
				public static int torqueEffectiveMax = 8;

				@Comment({"Max Torque (will break if over)."})
				public static int torqueBreakingMax = 256;

				@Comment({"How long does it take for the station to put a crate onto the line. (in ticks)"})
				public static int outputTime = 240;

				@Comment({"How long does it take for the station to put a crate onto a minecart. (in ticks)"})
				public static int inputTime = 240;

				@Comment({"How long does it take for the minecart to drive into the station. (in ticks)"})
				public static int crateInTime = 60;

				@Comment({"How long does it take for the minecart to drive out of the station. (in ticks)"})
				public static int crateOutTime = 60;
			}

			public static class SkyCartStation
			{
				@Comment({"How long does it take for the minecart to drive into the station. (in ticks)"})
				public static int minecartInTime = 40;

				@Comment({"How long does it take for the minecart to drive out of the station. (in ticks)"})
				public static int minecartOutTime = 40;
			}

			public static class Sawmill
			{
				@Comment({"Rotations per minute required for the Sawmill to Work."})
				public static int rpmMin = 20;

				@Comment({"Max rotations per minute (will break if over)."})
				public static int rpmBreakingMax = 160;

				@Comment({"Torque required for the Sawmill to Work."})
				public static int torqueMin = 6;

				@Comment({"Max Torque (will break if over)."})
				public static int torqueBreakingMax = 140;
			}

			public static class RadioStation
			{
				@Comment({"Energy capacity of the radio station."})
				public static int energyCapacity = 32000;

				@Comment({"Energy usage when sending a signal."})
				public static int energyUsage = 8192;

				@Comment({"Energy usage when a radio station is working."})
				public static int energyUsageIdle = 512;

				@Comment({"Range of the radio station (in which the signals can be received) in blocks from center (radius)."})
				public static int radioRange = 64;

				@Comment({"How much the range decreases when there is bad weather (rain, snow) ( 0 - full range, 0.5 - half range, 1 - no range, etc.)"})
				public static float weatherHarshness = 0.5f;
			}

			public static class DataInputMachine
			{
				@Comment({"Energy capacity of the data input machine."})
				public static int energyCapacity = 16000;

				@Comment({"Energy usage when sending a signal."})
				public static int energyUsage = 2048;

				@Comment({"Energy per step of punching a tape (1/60 of the full energy needed)."})
				public static int energyUsagePunchtape = 128;

				@Comment({"Duration of tape punching process in ticks (1 tick = 1/20 Second)."})
				public static int timePunchtapeProduction = 160;
			}

			public static class ArithmeticLogicMachine
			{
				@Comment({"Energy capacity of the arithmetic-logic machine."})
				public static int energyCapacity = 24000;

				@Comment({"Energy usage of the arithmetic-logic machine per circuit."})
				public static int energyUsage = 2048;
			}

			public static class PrintingPress
			{
				@Comment({"Energy capacity of the printing press."})
				public static int energyCapacity = 24000;

				@Comment({"Energy usage of the printing press per page printed."})
				public static int energyUsage = 512;

				@Comment({"Page printing duration (in ticks)."})
				public static int printTime = 90;

				@Comment({"Ink used per character printed (mB)."})
				public static int printInkUsage = 8;
			}

			public static class ChemicalBath
			{
				@Comment({"Energy capacity of the chemical bath."})
				public static int energyCapacity = 16000;

				@Comment({"Fluid capacity of the chemical bath."})
				public static int fluidCapacity = 24000;
			}

			public static class Electrolyzer
			{
				@Comment({"Energy capacity of the electrolyzer."})
				public static int energyCapacity = 16000;

				@Comment({"Fluid capacity of the electrolyzer."})
				public static int fluidCapacity = 12000;
			}

			public static class PrecissionAssembler
			{
				@Comment({"Energy capacity of the chemical bath."})
				public static int energyCapacity = 16000;

				@Comment({"Hatch opening (or closing) time (in ticks)"})
				public static int hatchTime = 40;

			}

			public static class ArtilleryHowitzer
			{
				@Comment({"Energy capacity of the artillery howitzer."})
				public static int energyCapacity = 1000000;

				@Comment({"Energy usage when moving / rotating the platform."})
				public static int energyUsagePlatform = 1620;

				@Comment({"Energy usage when loading / unloading a shell."})
				public static int energyUsageLoader = 3192;

				@Comment({"Time needed for the platform to ascend/descend (in ticks."})
				public static int platformTime = 240;

				@Comment({"Time needed for the howitzer to fire (in ticks."})
				public static int fireTime = 35;

				@Comment({"How long does it take for the howitzer to rotate 90 degrees (in ticks)"})
				public static int rotateTime = 160;

				@Comment({"How long does it take for the howitzer to load a shell (in ticks)"})
				public static int loadTime = 140;

				@Comment({"How long does it take for the howitzer to move the shell by one item slot using conveyor (in ticks)"})
				public static int conveyorTime = 40;
			}

			public static class BallisticComputer
			{
				@Comment({"Energy capacity of the ballistic computer."})
				public static int energyCapacity = 24000;

				@Comment({"Energy capacity of the ballistic computer."})
				public static int energyUsage = 16000;
			}

			public static class ConveyorScanner
			{
				@Comment({"Energy capacity of the scanning conveyor."})
				public static int energyCapacity = 8000;

				@Comment({"Energy usage when scanning an ItemStack."})
				public static int energyUsage = 128;
			}

			public static class MissileSilo
			{
				@Comment({"Energy capacity of the missile silo (per one block of height)."})
				public static int energyCapacity = 2500000;
			}

			public static class AmmunitionFactory
			{
				@Comment({"Energy capacity of the ammunition factory (in IF)."})
				public static int energyCapacity = 32000;

				@Comment({"Energy usage per one tick of the bullet core making process (in IF)."})
				public static int energyUsageCore = 120;

				@Comment({"Energy usage per one tick of putting a core into a casing (in IF)."})
				public static int energyUsageCasing = 120;

				@Comment({"Energy usage per one tick of putting gunpowder into a casing (in IF)."})
				public static int energyUsageGunpowder = 120;

				@Comment({"Energy usage per one tick of the paint conveyor process (in IF)."})
				public static int energyUsagePaint = 120;

				@Comment({"Component capacity of the ammunition factory (in Pabilo8's Unit of Measurement™)."})
				public static int componentCapacity = 48;

				@Comment({"Max intake of the component per 20 ticks (in Pabilo8's Unit of Measurement™)."})
				public static int componentIntake = 6;

				@Comment({"Paint usage per one bullet making process (in mB)."})
				public static int paintUsage = 120;

				@Comment({"How long does it take to move a bullet to the next slot (in ticks)."})
				public static int conveyorTime = 50;

				@Comment({"Duration of filling a casing with gunpowder (in ticks)."})
				public static int gunpowderTime = 120;

				@Comment({"Duration of filling a core with components (in ticks)."})
				public static int coreTime = 160;

				@Comment({"Duration of one bullet making process (in ticks)."})
				public static int casingTime = 120;

				@Comment({"Duration of the bullet painting process (in ticks)."})
				public static int paintTime = 180;


			}

			public static class Inserter
			{
				@Comment({"Energy capacity of the inserter."})
				public static int energyCapacity = 2048;
				@Comment({"Energy usage of the inserter per item taken."})
				public static int energyUsage = 128;

				@Comment({"How long does it take for the inserter to pick up an item (in ticks)"})
				public static int grabTime = 20;

				@Comment({"How long does it take for the inserter to rotate 90 degrees (in ticks)"})
				public static int rotateTime = 10;

			}

			public static class AdvancedInserter
			{
				@Comment({"Energy capacity of the inserter."})
				public static int energyCapacity = 4096;
				@Comment({"Energy usage of the inserter per item taken."})
				public static int energyUsage = 256;

				@Comment({"How long does it take for the inserter to pick up an item (in ticks)"})
				public static int grabTime = 10;

				@Comment({"How long does it take for the inserter to rotate 90 degrees (in ticks)"})
				public static int rotateTime = 5;

			}

			public static class FluidInserter
			{
				@Comment({"Energy capacity of the inserter."})
				public static int energyCapacity = 2048;
				@Comment({"Energy usage of the inserter per item taken."})
				public static int energyUsage = 128;

				@Comment({"Max fluid output (in milibuckets per tick)"})
				public static int maxOutput = 500;

			}

			public static class AdvancedFluidInserter
			{
				@Comment({"Energy capacity of the inserter."})
				public static int energyCapacity = 4096;
				@Comment({"Energy usage of the inserter per item taken."})
				public static int energyUsage = 256;

				@Comment({"Max fluid output (in milibuckets per tick)"})
				public static int maxOutput = 240;

			}

			public static class ChemicalDispenser
			{
				@Comment({"Energy capacity of the chemical dispenser."})
				public static int energyCapacity = 2048;

				@Comment({"Energy usage of the chemical dispenser per one shot."})
				public static int energyUsage = 128;

				@Comment({"How long does it take for the chemical dispenser to rotate 45 degrees horizontally (in ticks)"})
				public static int rotateHTime = 240;

				@Comment({"How long does it take for the chemical dispenser to rotate 45 degrees vertically (in ticks)"})
				public static int rotateVTime = 180;

			}

			public static class SmallDataBuffer
			{
				@Comment({"Amount of data packets the machine can store."})
				public static int packetCapacity = 4;
			}
		}

		public static class Weapons
		{
			@SubConfig
			public static Machinegun machinegun;

			public static class Machinegun
			{
				@Comment({"Time required to reload a clip in MG."})
				public static int clipReloadTime = 35;

				@Comment({"Time required to fire a single bullet in MG."})
				public static int bulletFireTime = 2;

				@Comment({"Time required to set up the MG (in ticks)."})
				public static int setupTime = 50;

				@Comment({"Max scatter of heat, higher values will force the player to wait until the gun cools down."})
				public static int maxOverheat = 100;

				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float recoilHorizontal = 4f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilVertical = 4f;

				@Comment({"Fire rate multiplier when heavy barrel is mouted on mg."})
				public static float heavyBarrelFireRateMultiplier = 0.25f;

				@Comment({"Horizontal recoil after taking a shot with heavy barrel mounted."})
				public static float recoilHBHorizontal = 1.25f;

				@Comment({"Vertical recoil after taking a shot with heavy barrel mounted."})
				public static float recoilHBVertical = 1.25f;

				@Comment({"Water usage when water cooling upgrade is mounted on mg (in mB)."})
				public static int waterCoolingFluidUsage = 10;

				@Comment({"Coolant tank capacity of the water cooling upgrade."})
				public static int waterCoolingTankCapacity = 4000;

				@Comment({"Setup time multiplier when the precise bipod is mouted on mg."})
				public static float preciseBipodSetupTimeMultiplier = 2f;

				@Comment({"Recoil multiplier when the precise bipod is mouted on mg."})
				public static float preciseBipodRecoilMultiplier = 0.25f;

				@Comment({"Setup time multiplier when the hasty bipod is mouted on mg."})
				public static float hastyBipodSetupTimeMultiplier = 0.5f;

				@Comment({"Recoil multiplier when the hasty bipod is mouted on mg."})
				public static float hastyBipodRecoilMultiplier = 1.5f;

				@Comment({"Horizontal recoil multiplier when the double magazine upgrade is mouted on mg."})
				public static float recoilSecondMagazine = 1.65f;

				@Comment({"Setup time multiplier when the belt fed loader upgrade is mouted on mg."})
				public static float beltFedLoaderSetupTimeMultiplier = 0.5f;

				@Comment({"Infrared scope energy usage per tick"})
				public static int infraredScopeEnergyUsage = 15;

				@Comment({"Max zoom of a machinegun with a scope mounted (in Blu's Unit of Distance Measurement™)."})
				@RequiresMcRestart
				@Mapped(mapClass = Config.class, mapName = "manual_floatA")
				public static float[] machinegun_scope_max_zoom = new float[]{0.15f, 0.35f, 0.55f,};

				@Comment({"Shield's initial strength (resistance vs attacks)."})
				public static float shieldStrengthInitial = 45;

				@Comment({"Setup time multiplier when a shield is mouted on mg."})
				public static float shieldSetupTimeMultiplier = 0.5f;

			}
		}

		public static class Wires
		{
			@Comment({"The RGB color of the data wire."})
			public static int dataWireColouration = 0xb3d1d6;

			@Comment({"The maximum length of a single data wire."})
			public static int dataWireLength = 24;
		}

		public static class MechanicalDevices
		{
			@Comment({"Minimal RPM required by a gear to work.", GEARS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] gear_min_rpm = new int[]{10, 40, 120, 360, 720};

			@Comment({"Gears will break if RPM is higher than this value.", GEARS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] gear_max_rpm = new int[]{60, 200, 240, 800, 2400};

			@Comment({"Gears will break if Torque is higher than this value.", GEARS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] gear_max_torque = new int[]{6, 24, 32, 256, 512};

			//20 mins, 40 mins 1h, 2h, 4h
			@Comment({"Durability of the gear (decreases by 1 every 20 ticks (1 second)).", GEARS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] gear_max_durability = new int[]{2400, 4800, 7200, 14400, 28800};


			@Comment({"Belts will break if RPM is higher than this value.", BELTS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] belt_max_rpm = new int[]{960, 3600};

			@Comment({"Belts will break if Torque is higher than this value.", BELTS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] belt_max_torque = new int[]{48, 640};

			@Comment({"Torque loss, later multiplied by belt length.", BELTS})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float[] belt_torque_loss = new float[]{0.05f, 0.1f};

			@Comment({"Max length of the belt, works exactly like wires (measured in blocks).", BELTS})
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] belt_length = new int[]{8, 16};

			@Comment({"Immersive Flux to Rotary Flux conversion ratio (default 1 RoF = 1 RF)."})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float rof_conversion_ratio = 1f;

			@Comment({"Default torque, used as a fallback, when IE rotational device is not recognised."})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float dynamo_default_torque = 2f;

			@Comment({"Torque multiplier for the windmill."})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float dynamo_windmill_torque = 8f;

			@Comment({"Default torque, used as a fallback, when IE rotational device is not recognised."})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float dynamo_watermill_torque = 12f;
		}
	}
}
