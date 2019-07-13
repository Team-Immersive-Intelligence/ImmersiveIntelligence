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

			@Comment({"Set this to true to allow retro-generation of Platinum Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IEWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogen_platinum = false;

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


			@Comment({"The capacity of the electric hammer in RF... i mean IF... i mean FE... that thing o' powerin'!."})
			@RequiresMcRestart
			public static int electric_hammer_capacity = 24000;

			@Comment({"The capacity of the electric wirecutter."})
			@RequiresMcRestart
			public static int electric_wirecutter_capacity = 24000;

			@Comment({"The capacity of the electric multitool."})
			@RequiresMcRestart
			public static int electric_multitool_capacity = 100000;

			@Comment({"The energy usage of the electric hammer (when mining, rotating or single tick when building advanced multiblocks)."})
			@RequiresMcRestart
			public static int electric_hammer_energy_per_use = 100;

			@Comment({"The energy usage of the electric wirecutter (when cutting wires)."})
			@RequiresMcRestart
			public static int electric_wirecutter_energy_per_use = 100;
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
			public static RedstoneOutputMachine redstoneOutputMachine;
			@SubConfig
			public static ArtilleryHowitzer artilleryHowitzer;
			@SubConfig
			public static MissileSilo missileSilo;

			public static class RadioStation
			{
				@Comment({"Energy capacity of the radio station."})
				public static int energyCapacity = 32000;

				@Comment({"Energy usage when sending a signal."})
				public static int energyUsage = 16000;

				@Comment({"Energy usage when a radio station is working."})
				public static int energyUsageIdle = 1024;

				@Comment({"Range of the radio station (in which the signals can be received) in blocks from center (radius)."})
				public static int radioRange = 72;

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

			public static class RedstoneOutputMachine
			{
				@Comment({"Energy capacity of the data input machine."})
				public static int energyCapacity = 16000;
			}

			public static class ArtilleryHowitzer
			{
				@Comment({"Energy capacity of the artillery howitzer."})
				public static int energyCapacity = 1000000;
			}

			public static class MissileSilo
			{
				@Comment({"Energy capacity of the missile silo (per one block of height)."})
				public static int energyCapacity = 2500000;
			}
		}

		public static class Wires
		{
			@Comment({"The RGB color of the data wire."})
			public static int dataWireColouration = 0xb3d1d6;

			@Comment({"The maximum length of a single data wire."})
			public static int dataWireLength = 32;
		}
	}
}
