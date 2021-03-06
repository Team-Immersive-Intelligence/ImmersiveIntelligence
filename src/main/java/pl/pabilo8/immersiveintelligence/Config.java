package pl.pabilo8.immersiveintelligence;

import blusunrize.immersiveengineering.common.Config.Mapped;
import blusunrize.immersiveengineering.common.Config.SubConfig;
import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

import java.util.Map;

/**
 * @author Pabilo8
 * @since 2019-05-12
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
		@SubConfig
		public static Vehicles vehicles;

		@Comment({"A list of all mods that IEn has integrated compatability for", "Setting any of these to false disables the respective compat"})
		public static Map<String, Boolean> compat = Maps.newHashMap(Maps.toMap(IICompatModule.moduleClasses.keySet(), (s) -> Boolean.TRUE));

		@Comment({"The maximum frequency for basic radios."})
		public static int radioBasicMaxFrequency = 32;

		@Comment({"The maximum frequency for advanced radios."})
		public static int radioAdvancedMaxFrequency = 256;

		@Comment({"Should RPM be counted in real time or ingame time"})
		public static boolean rpmRealTime = true;

		@Comment({"Whether basic circuits should be produced in II or IE way"})
		@RequiresMcRestart
		public static boolean changeCircuitProduction = true;

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
			public static int[] ore_platinum = new int[]{6, 0, 10, 2, 75};

			@Comment({"Generation config for Zinc Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_zinc = new int[]{10, 35, 95, 2, 100};

			@Comment({"Generation config for Tungsten Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_tungsten = new int[]{6, 0, 35, 2, 75};

			@Comment({"Generation config for Salt Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_salt = new int[]{12, 55, 95, 1, 75};

			@Comment({"Generation config for Fluorite Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_fluorite = new int[]{6, 1, 55, 1, 65};

			@Comment({"Generation config for Fluorite Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_phosphorus = new int[]{12, 1, 55, 1, 80};

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

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_phosphorus = false;
		}

		public static class Tools
		{
			@SubConfig
			public static SkycrateMounts skycrateMounts;

			@SubConfig
			public static TripodPeriscope tripodPeriscope;

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

			public static class TripodPeriscope
			{
				@Comment({"Determines how fast the Tripod Periscope can be set up (in ticks)."})
				@RangeInt(min = 0)
				@RequiresMcRestart
				public static int setup_time = 60;

				@Comment({"The yaw turn speed of the Tripod Periscope (in degrees)."})
				@RequiresMcRestart
				public static float turn_speed = 2.5f;

				@Comment({"Max zoom of a machinegun with a scope mounted (in Blu's Unit of Distance Measurement™)."})
				@RequiresMcRestart
				@Mapped(mapClass = Config.class, mapName = "manual_floatA")
				public static float[] tripod_zoom_steps = new float[]{0.01f, 0.02f, 0.04f, 0.0625f, 0.0833f, 0.125f, 0.25f, 0.5f};
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
			@SubConfig
			public static EffectCrates effectCrates;

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

			public static class EffectCrates
			{
				@Comment({"The amount of fluid a Medical Crate can store (in mB)"})
				public static int mediCrateTankSize = 4000;

				@Comment({"The amount of energy an inserter upgraded Medical Crate takes per one heal (in IF/RF/FE)"})
				public static int mediCrateEnergyPerAction = 50;

				@Comment({"The amount of energy an inserter upgraded Repair Crate takes per one repair (in IF/RF/FE)"})
				public static int repairCrateEnergyPerAction = 65;

				@Comment({"The amount of energy an inserter upgraded Ammunition Crate takes per one 4 second effect (in IF/RF/FE)"})
				public static int ammoCrateEnergyPerAction = 85;

				@Comment({"The amount of energy an inserter upgraded crate can store (in IF/RF/FE)"})
				public static int maxEnergyStored = 4000;

				@Comment({"The amount of energy an inserter upgraded crate can drain in one tick (in IF/RF/FE)"})
				public static int energyDrain = 40;
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

			public static class MechanicalPump
			{
				@Comment({"Rotations per minute required for the Sawmill to Work."})
				public static int rpmMin = 40;

				@Comment({"Max rotations per minute (will break if over)."})
				public static int rpmBreakingMax = 160;

				@Comment({"Torque required for the Sawmill to Work."})
				public static int torqueMin = 2;

				@Comment({"Max Torque (will break if over)."})
				public static int torqueBreakingMax = 40;
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

				@RangeDouble(min = 0, max = 1)
				@Comment({"How much the range decreases when there is bad weather (rain, snow) ( 0 - full range, 0.5 - half range, 1 - no range, etc.)"})
				public static double weatherHarshness = 0.5;
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

				@Comment({"The speed of howitzer shells in blocks/tick"})
				public static float howitzerVelocity = 10;
				
				@Comment({"the range modifier of the howitzer"})
				public static float howitzerRangeMP = 1;
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

			public static class Emplacement
			{
				@Comment({"Energy capacity of the emplacement."})
				public static int energyCapacity = 16000;

				@Comment({"Time for the multiblock to open/close the lid."})
				public static int lidTime = 240;
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
			public static Submachinegun submachinegun;
			@SubConfig
			public static Machinegun machinegun;
			@SubConfig
			public static EmplacementWeapons emplacementWeapons;
			@SubConfig
			public static Railgun railgun;
			@SubConfig
			public static Grenade grenade;
			@SubConfig
			public static Mines mines;

			public static class EmplacementWeapons
			{

				@SubConfig
				public static Autocannon autocannon;
				@SubConfig
				public static InfraredObserver infraredObserver;
				@SubConfig
				public static CPDS cpds;
				@SubConfig
				public static HeavyChemthrower heavyChemthrower;
				@SubConfig
				public static HeavyRailgun heavyRailgun;


				public static class Autocannon
				{
					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 4;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 4;

					@Comment({"Time required to reload all the magazines."})
					public static int reloadTime = 280;

					@Comment({"Time required to fire a single bullet."})
					public static int bulletFireTime = 3;
				}

				public static class InfraredObserver
				{
					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 2;

					@Comment({"Time needed for 90 degrees yaw rotation (in ticks)"})
					public static int yawRotateTime = 120;

					@Comment({"Time required for observer setup (lens attachment animation) (in ticks)."})
					public static int setupTime = 300;
				}

				public static class CPDS
				{
					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 8;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 8;
				}

				public static class HeavyChemthrower
				{
					@Comment({"Time required for chemthrower setup (barrel extension animation) (in ticks)."})
					public static int setupTime = 100;

					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 2;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 1;
				}

				public static class HeavyRailgun
				{
					@Comment({"Time required to fire a single shot."})
					public static int shotFireTime = 40;

					@Comment({"Time required for loading a single projectile."})
					public static int reloadConveyorTime = 20;

					@Comment({"Time required for replacing the ammo box."})
					public static int reloadAmmoBoxTime = 100;

					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 1.75f;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 0.65f;
				}
			}

			public static class Railgun
			{
				@Comment({"Make standard railgun rods to be able to penetrate mobs (depending on metal)."})
				public static boolean enablePenetration = true;

				@Comment({"Whether the railgun has recoil (pushes the shooter to back, depending on projectile mass)."})
				public static boolean railgunRecoil = true;
			}

			public static class Grenade
			{
				@Comment({
						"Changes looks of the grenades",
						"0 - new look",
						"1 - old look, but cores are colored",
						"2 - old look"
				})
				@RangeInt(min = 0, max = 2)
				public static int classicGrenades = 0;

				@Comment({"Grenade's initial speed modifier."})
				@RangeDouble(min = 0)
				public static double throwSpeedModifier = 1.65f;
			}

			public static class Mines
			{
				@Comment({
						"Changes color of the tripmine ",
						"0 - steel",
						"1 - green",
						"2 - dull-yellow"
				})
				@RangeInt(min = 0, max = 2)
				public static int tripmineColor = 0;
			}

			public static class Submachinegun
			{
				@Comment({"Whether the recoil is visible in first-person view."})
				public static boolean cameraRecoil = true;

				@Comment({"Time required to reload a magazine in SMG."})
				public static int clipReloadTime = 45;

				@Comment({"Time required to reload a magazine in SMG."})
				public static int aimTime = 10;

				@Comment({"Time required to fire a single bullet."})
				public static int bulletFireTime = 2;

				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float recoilHorizontal = 3f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilVertical = 4f;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float maxRecoilHorizontal = 30f;

				@Comment({"Maximum amount of vertical recoil."})
				public static float maxRecoilVertical = 45f;
			}

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

				@Comment({"Setup time multiplier when the mg is mounted on a tripod."})
				public static float tripodSetupTimeMultiplier = 4f;

				@Comment({"Recoil multiplier when the mg is mounted on a tripod."})
				public static float tripodRecoilMultiplier = 0.125f;

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
				public static float[] machinegun_scope_max_zoom = new float[]{0.55f, 0.35f, 0.15f};

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

			@Comment({"The RGB color of the small data wire."})
			public static int smallRedstoneWireColouration = 0xff2f2f;

			@Comment({"The maximum length of a single small data wire. Should not be much."})
			public static int smallRedstoneWireLength = 4;

			@Comment({"The RGB color of the small data wire."})
			public static int smallDataWireColouration = 0xb3d1d6;

			@Comment({"The maximum length of a single small data wire. Should not be much."})
			public static int smallDataWireLength = 4;
		}

		public static class Vehicles
		{
			@SubConfig
			public static Motorbike motorbike;

			public static class Motorbike
			{
				@Comment({"Fuel capacity of the motorbike."})
				public static int fuelCapacity = 12000;

				@Comment({"Fuel usage per one meter driven."})
				public static int fuelUsage = 6;

				@Comment({"Damage resistance of the wheels."})
				public static int wheelDurability = 40;

				@Comment({"Damage resistance of the engine."})
				public static int engineDurability = 100;

				@Comment({"Damage resistance of the fuel tank."})
				public static int fuelTankDurability = 80;

				@Comment({"Roll the camera when turning the motorbike."})
				public static boolean cameraRoll = false;

				@Comment({"Amount of fuel burn per time, dependent on diesel generator fluids (in mB)"})
				public static int fuelBurnAmount = 8;

			}

			public static class FieldHowitzer
			{
				@Comment({"Damage resistance of the wheels."})
				public static int wheelDurability = 40;

				@Comment({"Damage resistance of the gun frame."})
				public static int mainDurability = 160;

				@Comment({"Damage resistance of the gun barrel."})
				public static int gunDurability = 80;

				@Comment({"Damage resistance of the gun shield (both sides have common health)."})
				public static int shieldDurability = 160;

				@Comment({"Time required to tow a field howitzer."})
				public static int towingTime = 80;
			}
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

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent ev)
	{
		if(ev.getModID().equals(ImmersiveIntelligence.MODID))
		{
			ConfigManager.sync(ImmersiveIntelligence.MODID, net.minecraftforge.common.config.Config.Type.INSTANCE);
			// TODO: 29.11.2020 add when required
			//onConfigUpdate();
		}
	}
}
