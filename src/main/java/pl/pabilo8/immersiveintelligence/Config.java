package pl.pabilo8.immersiveintelligence;

import blusunrize.immersiveengineering.common.Config.Mapped;
import blusunrize.immersiveengineering.common.Config.SubConfig;
import blusunrize.immersiveengineering.common.world.IEWorldGen;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

/**
 * Created by Pabilo8 on 2019-05-12.
 */
@Mod.EventBusSubscriber
public class Config
{
	public static HashMap<String, Boolean> manual_bool = new HashMap<String, Boolean>();
	public static HashMap<String, Integer> manual_int = new HashMap<String, Integer>();
	public static HashMap<String, int[]> manual_intA = new HashMap<String, int[]>();
	public static HashMap<String, Double> manual_double = new HashMap<String, Double>();
	public static HashMap<String, double[]> manual_doubleA = new HashMap<String, double[]>();

	public static HashMap<String, Double> manual_float = new HashMap<String, Double>();
	public static HashMap<String, double[]> manual_floatA = new HashMap<String, double[]>();

	@net.minecraftforge.common.config.Config(modid = ImmersiveIntelligence.MODID)
	public static class IIConfig
	{
		@SubConfig
		public static Ores ores;
		@SubConfig
		public static Machines machines;
		@SubConfig
		public static Tools tools;
		@SubConfig
		public static Wires wires;

		@Comment({"The maximum frequency for basic radios."})
		public static int radioBasicMaxFrequency = 32;

		@Comment({"The maximum frequency for advanced radios."})
		public static int radioAdvancedMaxFrequency = 256;

		public static class Ores
		{
			@Comment({"Generation config for Platinum Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_platinum = new int[]{4, 0, 10, 2, 35};

			@Comment({"Generation config for Zinc Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_zinc = new int[]{8, 60, 95, 2, 55};

			@Comment({"Generation config for Tungsten Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_tungsten = new int[]{6, 0, 35, 2, 45};

			@Comment({"Generation config for Salt Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = Config.class, mapName = "manual_intA")
			public static int[] ore_salt = new int[]{4, 55, 75, 1, 65};

			@Comment({"Set this to true to allow retro-generation of Platinum Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_platinum = false;

			@Comment({"Set this to true to allow retro-generation of Zinc Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_zinc = false;

			@Comment({"Set this to true to allow retro-generation of Tungsten Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_tungsten = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_salt = false;

		}

		public static class Tools
		{
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

			//Soon?
			@Comment({"The energy capacity of the electric multitool. (when it will be added)"})
			@RequiresMcRestart
			public static int electric_multitool_capacity = 100000;

			@Comment({"The energy usage of the electric hammer (when mining, rotating or single tick when building advanced multiblocks)."})
			@RequiresMcRestart
			public static int electric_hammer_energy_per_use = 100;

			@Comment({"The energy usage of the electric wirecutter (when cutting wires)."})
			@RequiresMcRestart
			public static int electric_wirecutter_energy_per_use = 100;

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
			public static int precission_tool_buzzsaw_durability = 100;

			@Comment({"The durability (max number of uses) of the Precission Drill."})
			@RequiresMcRestart
			public static int precission_tool_drill_durability = 100;

			@Comment({"The durability (max number of uses) of the Precission Inserter."})
			@RequiresMcRestart
			public static int precission_tool_inserter_durability = 100;

			@Comment({"The durability (max number of uses) of the Precission Solderer."})
			@RequiresMcRestart
			public static int precission_tool_solderer_durability = 100;

			@Comment({"The durability (max number of uses) of the Precission Welder."})
			@RequiresMcRestart
			public static int precission_tool_welder_durability = 100;

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

			//Default bullet damage
			@Comment({"Basic bullet damage (for caliber 1)"})
			public static float basic_bullet_damage = 2.0f;

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
			public static SmallDataBuffer small_data_buffer;

			public static class RadioStation
			{
				@Comment({"Energy capacity of the radio station."})
				public static int energyCapacity = 32000;

				@Comment({"Energy usage when sending a signal."})
				public static int energyUsage = 8192;

				@Comment({"Energy usage when a radio station is working."})
				public static int energyUsageIdle = 512;

				@Comment({"Range of the radio station (in which the signals can be received) in blocks from center (radius)."})
				public static int radioRange = 48;

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

				@Comment({"Energy usage per one bullet making process (in IF)."})
				public static int energyUsage = 8000;

				@Comment({"Component capacity of the ammunition factory (in Pabilo8's Unit of Measurement™)."})
				public static int componentCapacity = 48;

				@Comment({"Max intake of the component per 20 ticks (in Pabilo8's Unit of Measurement™)."})
				public static int componentIntake = 6;

				@Comment({"Paint usage per one bullet making process (in mB)."})
				public static int paintUsage = 120;

				@Comment({"How long does it take to move a bullet to the next slot (in ticks)."})
				public static int conveyorTime = 35;

				@Comment({"Duration of filling a casing with gunpowder (in ticks)."})
				public static int gunpowderTime = 80;

				@Comment({"Duration of filling a core with components (in ticks)."})
				public static int coreTime = 100;

				@Comment({"Duration of one bullet making process (in ticks)."})
				public static int casingTime = 40;

				@Comment({"Duration of the bullet painting process (in ticks)."})
				public static int paintTime = 60;


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
				public static int maxOutput = 80;

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

			public static class SmallDataBuffer
			{
				@Comment({"Amount of data packets the machine can store."})
				public static int packetCapacity = 4;
			}
		}

		public static class Wires
		{
			@Comment({"The RGB color of the data wire."})
			public static int dataWireColouration = 0xb3d1d6;

			@Comment({"The maximum length of a single data wire."})
			public static int dataWireLength = 24;
		}
	}
}
