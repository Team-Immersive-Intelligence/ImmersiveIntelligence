package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.Config.Mapped;
import blusunrize.immersiveengineering.common.Config.SubConfig;
import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.RadioStation;
import pl.pabilo8.immersiveintelligence.common.compat.IICompatModule;
import pl.pabilo8.immersiveintelligence.common.world.IIWorldGen;

import java.util.Map;

/**
 * @author Pabilo8
 * @since 2019-05-12
 */
@Mod.EventBusSubscriber
public class IIConfigHandler
{
	public static final String GEARS = "Gears: Copper, Brass, Iron, Steel, Tungsten";
	public static final String BELTS = "Belts: Cloth, Steel, Rubber";

	@net.minecraftforge.common.config.Config(modid = ImmersiveIntelligence.MODID)
	public static class IIConfig
	{
		@SubConfig
		@LangKey("Graphics")
		public static Graphics graphics;
		@SubConfig
		@LangKey("Ores")
		public static Ores ores;
		@SubConfig
		@LangKey("Machines")
		public static Machines machines;
		@SubConfig
		@LangKey("Mechanical Devices")
		public static MechanicalDevices mechanicalDevices;
		@SubConfig
		@LangKey("Tools")
		public static Tools tools;
		@SubConfig
		@LangKey("Weapons")
		public static Weapons weapons;
		@SubConfig
		@LangKey("Wires")
		public static Wires wires;
		@SubConfig
		@LangKey("Vehicles")
		public static Vehicles vehicles;

		@Comment({"A list of all mods that II has integrated compatability for", "Setting any of these to false disables the respective compat"})
		public static Map<String, Boolean> compat = Maps.newHashMap(Maps.toMap(IICompatModule.moduleClasses.keySet(), (s) -> Boolean.TRUE));

		@Comment({"The maximum frequency for basic radios."})
		public static int radioBasicMaxFrequency = 32;

		@Comment({"The maximum frequency for advanced radios."})
		@LangKey("Wires")
		public static int radioAdvancedMaxFrequency = 256;

		@Comment({"Whether basic circuits should be produced in II or IE way"})
		@RequiresMcRestart
		public static boolean changeCircuitProduction = true;

		@Comment({"Whether the IE revolver should be a Early Engineering-tier weapon"})
		public static boolean changeRevolverProduction = true;

		@Comment({"Whether the the railgun should require a gun stock instead of a grip to be constructed"})
		public static boolean changeRailgunProduction = true;

		@Comment({"Whether the the chemthrower should require a gun stock instead of a grip to be constructed"})
		public static boolean changeChemthrowerProduction = true;

		@Comment({"Whether Tungsten should be smeltable in the vanilla furnace"})
		@RequiresMcRestart
		public static boolean smeltableTungsten = false;

		@Comment({"Whether Advanced Electronic Alloy should be smeltable in the vanilla furnace"})
		@RequiresMcRestart
		public static boolean smeltableAEA = false;

		@Comment({"A list of all entities for which a fakeplayer should be used when shooter is not a player"})
		public static String[] bulletFakeplayerWhitelist = new String[]{
				"minecraft:ender_dragon"
		};

		@Comment({"Whether the II Creative Tab should be divided into sub-tabs (Australian Tabs(tm))."})
		@RequiresMcRestart
		public static boolean australianCreativeTabs = true;

		public static class Graphics
		{
			@Comment({"Enable Tactile AMT - dynamic collision boxes for multiblocks that use animations for positioning them."})
			public static boolean tactileAMT = true;

			@Comment({"Whether gun recoil is visible in first-person view."})
			public static boolean cameraRecoil = true;

			@Comment({"Whether screen shake effect should be displayed."})
			public static boolean cameraScreenShake = true;

			@Comment({"Enable camera roll motion."})
			public static boolean cameraRoll = false;

			@Comment({"Determines how hand animations should be displayed on II models",
					"0 - disabled",
					"1 - first person only",
					"2 - 1st and 3rd person (may be incompatible with mods modifying the player model)"})
			@RangeInt(min = 0, max = 2)
			public static int AMTHandDisplayMode = 1;

			@RequiresMcRestart
			@Comment({"Max amount of block penetrations that will be rendered. 0 will disable rendering."})
			public static int maxPenetratedBlocks = 64;

			@Comment({"Furthest distance II explosion effects should be visible at."})
			public static int explosionMessageDistance = 256;

			@RangeInt(min = 0)
			@Comment({"Max amount of particles that can exist within the particle system."})
			public static int maxAllowedParticles = 20000;

			@RangeInt(min = 0)
			@Comment({"Max amount of particles that will be simulated."})
			public static int maxSimulatedParticles = 6000;

			@RangeInt(min = 0)
			@Comment({"Max amount of particles that will be drawn. Should be less or equal to maxSimulatedParticles."})
			public static int maxDrawnParticles = 1000;

			@Comment({"Determines how look of II explosion particles",
					"0 - vanilla",
					"1 - vanilla enhanced with block particles",
					"2 - overhauled",
					"3 - overhauled + debris"
			})
			@RangeInt(min = 0, max = 3)
			public static int explosionParticlesStyle = 3;
		}

		public static class Ores
		{
			@Comment({"A blacklist of dimensions in which IE ores won't spawn. By default this is Nether (-1) and End (1)"})
			public static int[] oreDimBlacklistNormal = new int[]{-1, 1};

			@Comment({"A blacklist of dimensions in which IE ores won't spawn. By default this is the Overworld (0) and End (1)"})
			public static int[] oreDimBlacklistNether = new int[]{0, 1};

			@Comment({"Set this to false to disable the logging of the chunks that were flagged for retrogen."})
			public static boolean retrogenLogFlagChunk = true;

			@Comment({"Set this to false to disable the logging of the chunks that are still left to retrogen."})
			public static boolean retrogenLogRemaining = true;

			@Comment({"The retrogeneration key. Basically IE checks if this key is saved in the chunks data. If it isn't, it will perform retrogen on all ores marked for retrogen.", "Change this in combination with the retrogen booleans to regen only some of the ores."})
			public static String retrogenKey = "DEFAULT_II";

			@Comment({"Generation config for Platinum Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] orePlatinum = new int[]{6, 0, 10, 2, 75};

			@Comment({"Generation config for Zinc Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] oreZinc = new int[]{10, 35, 95, 2, 100};

			@Comment({"Generation config for Tungsten Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] oreTungsten = new int[]{6, 0, 35, 2, 75};

			@Comment({"Generation config for Salt Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] oreSalt = new int[]{12, 55, 95, 1, 75};

			@Comment({"Generation config for Fluorite Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] oreFluorite = new int[]{6, 1, 55, 1, 65};

			@Comment({"Generation config for Fluorite Ore.", "Parameters: Vein size, lowest possible Y, highest possible Y, veins per chunk, chance for vein to spawn (out of 100). Set vein size to 0 to disable the generation"})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] orePhosphorus = new int[]{12, 1, 55, 1, 80};

			@Comment({"Set this to true to allow retro-generation of Platinum Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenPlatinum = false;

			@Comment({"Set this to true to allow retro-generation of Zinc Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenZinc = false;

			@Comment({"Set this to true to allow retro-generation of Tungsten Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenTungsten = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenSalt = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenFluorite = false;

			@Comment({"Set this to true to allow retro-generation of Salt Ore."})
			@RequiresMcRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean retrogenPhosphorus = false;

			@Comment({"Whether rubber trees should be generated."})
			@RequiresWorldRestart
			@Mapped(mapClass = IIWorldGen.class, mapName = "retrogenMap")
			public static boolean genRubberTrees = true;

			@RangeInt(min = 0, max = 100)
			@RequiresWorldRestart
			public static int genRubberTreesChance = 15;
		}

		public static class Tools
		{
			@SubConfig
			public static SkycrateMounts skycrateMounts;

			@SubConfig
			public static TripodPeriscope tripodPeriscope;

			@Comment({"A modifier to apply to the ammunition resupply time of the Ammunition Crate (weapons reload)."})
			public static float ammunitionCrateResupplyTime = 1.0f;

			@Comment({"The Lighter fuel capacity in milibuckets (mB)."})
			@RequiresMcRestart
			public static int lighterCapacity = 1000;

			@Comment({"The capacity of the measuring cup (mB)."})
			@RequiresMcRestart
			public static int measuringCupCapacity = 500;

			@Comment({"The energy capacity of the electric hammer in RF... i mean IF... i mean FE... that thing o' powerin'!."})
			@RequiresMcRestart
			public static int electricHammerCapacity = 24000;

			@Comment({"The energy capacity of the electric wirecutter."})
			@RequiresMcRestart
			public static int electricWirecutterCapacity = 24000;

			@Comment({"The energy capacity of the electric wrench."})
			@RequiresMcRestart
			public static int electricWrenchCapacity = 24000;

			//Soon?
			@Comment({"The energy capacity of the electric multitool. (when it will be added)"})
			@RequiresMcRestart
			public static int electricMultitoolCapacity = 100000;

			@Comment({"The energy usage of the electric hammer (when mining, rotating, etc. )."})
			@RequiresMcRestart
			public static int electricHammerEnergyPerUse = 100;

			@Comment({"The energy usage of the electric hammer when building advanced multiblocks (per tick)."})
			@RequiresMcRestart
			public static int electricHammerEnergyPerUseConstruction = 4096;

			@Comment({"The energy usage of the electric wirecutter (when cutting wires)."})
			@RequiresMcRestart
			public static int electricWirecutterEnergyPerUse = 100;

			@Comment({"The energy usage of the electric wrench (when destroying blocks / upgrading)."})
			@RequiresMcRestart
			public static int electricWrenchEnergyPerUse = 1000;

			@Comment({"The upgrade progress added per use of the engineer's wrench (default 1IF=1 Point of Progress™)."})
			@RequiresMcRestart
			public static int electricWrenchUpgradeProgress = 1000;

			@Comment({"The durability of the engineer's wrench."})
			@RequiresMcRestart
			public static int wrenchDurability = 256;

			@Comment({"The upgrade progress added per use of the engineer's wrench."})
			@RequiresMcRestart
			public static int wrenchUpgradeProgress = 350;

			@Comment({"Max zoom of the binoculars (in Blu's Unit of Magnification Measurement™)."})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float[] binocularsMaxZoom = new float[]{0.1f, 0.125f, 0.167f, 0.25f, 0.5f};

			@Comment({"Max zoom of the advanced binoculars (in Blu's Unit of Magnification Measurement™)."})
			@RequiresMcRestart
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float[] advancedBinocularsMaxZoom = new float[]{0.05f, 0.0625f, 0.0833f, 0.1f, 0.25f, 0.5f};

			@Comment({"The energy capacity of advanced binoculars (when using Infrared Sight)."})
			@RequiresMcRestart
			public static int advancedBinocularsEnergyCapacity = 4000;

			@Comment({"The energy usage of advanced binoculars (when using Infrared Sight)."})
			@RequiresMcRestart
			public static int advancedBinocularsEnergyUsage = 150;

			//Durability


			@Comment({"The durability (max number of uses) of the Precision Buzzsaw."})
			@RequiresMcRestart
			public static int precisionToolBuzzsawDurability = 150;

			@Comment({"The durability (max number of uses) of the Precision Drill."})
			@RequiresMcRestart
			public static int precisionToolDrillDurability = 250;

			@Comment({"The durability (max number of uses) of the Precision Inserter."})
			@RequiresMcRestart
			public static int precisionToolInserterDurability = 200;

			@Comment({"The durability (max number of uses) of the Precision Solderer."})
			@RequiresMcRestart
			public static int precisionToolSoldererDurability = 150;

			@Comment({"The durability (max number of uses) of the Precision Welder."})
			@RequiresMcRestart
			public static int precisionToolWelderDurability = 250;

			@Comment({"The durability (max number of uses) of the Precision Hammer."})
			@RequiresMcRestart
			public static int precisionToolHammerDurability = 200;

			@Comment({"The durability (max number of uses) of the Iron Sawblade."})
			@RequiresMcRestart
			public static int sawbladeIronDurability = 150;

			@Comment({"The durability (max number of uses) of the Steel Sawblade."})
			@RequiresMcRestart
			public static int sawbladeSteelDurability = 300;

			@Comment({"The durability (max number of uses) of the Tungsten Sawblade."})
			@RequiresMcRestart
			public static int sawbladeTungstenDurability = 550;

			//Usage Time

			@Comment({"The usage time of the Precision Buzzsaw."})
			@RequiresMcRestart
			public static int precisionToolBuzzsawUsageTime = 140;

			@Comment({"The usage time of the Precision Drill."})
			@RequiresMcRestart
			public static int precisionToolDrillUsageTime = 140;

			@Comment({"The usage time of the Precision Inserter."})
			@RequiresMcRestart
			public static int precisionToolInserterUsageTime = 60;

			@Comment({"The usage time of the Precision Solderer."})
			@RequiresMcRestart
			public static int precisionToolSoldererUsageTime = 80;

			@Comment({"The usage time of the Precision Welder."})
			@RequiresMcRestart
			public static int precisionToolWelderUsageTime = 160;

			@Comment({"The usage time of the Precision Hammer."})
			@RequiresMcRestart
			public static int precisionToolHammerUsageTime = 40;

			public static class SkycrateMounts
			{
				@Comment({"The speed of the Mechanical Skycrate Mount."})
				@RequiresMcRestart
				public static float mechSpeed = 0.25f;

				@Comment({"The energy (max distance to be traveled) of the Mechanical Skycrate Mount."})
				@RequiresMcRestart
				public static float mechEnergy = 35;

				@Comment({"The speed of the Electric Skycrate Mount."})
				@RequiresMcRestart
				public static float electricSpeed = 1f;

				@Comment({"The energy to distance ratio of the Electric Skycrate Mount. (in Immersive Flux per Meter)"})
				@RequiresMcRestart
				public static int electricEnergyRatio = 128;

				@Comment({"The energy capacity of the Electric Skycrate Mount (in Immersive Flux)."})
				@RequiresMcRestart
				public static float electricEnergy = 55;

			}

			public static class TripodPeriscope
			{
				@Comment({"Determines how fast the Tripod Periscope can be set up (in ticks)."})
				@RangeInt(min = 0)
				@RequiresMcRestart
				public static int setupTime = 60;

				@Comment({"The yaw turn speed of the Tripod Periscope (in degrees)."})
				@RequiresMcRestart
				public static float turnSpeed = 2.5f;

				@Comment({"Max zoom of a machinegun with a scope mounted (in Blu's Unit of Distance Measurement™)."})
				@RequiresMcRestart
				@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
				public static float[] tripodZoomSteps = new float[]{0.01f, 0.02f, 0.04f, 0.0625f, 0.0833f, 0.125f, 0.25f, 0.5f};
			}

			@Comment({"The capacity of the Improved Capacitor Backpack (in IF)."})
			@RequiresMcRestart
			public static int advancedPowerpackCapacity = 1000000;
		}

		public static class Machines
		{
			@SubConfig
			public static LatexCollector latexCollector;
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
			public static PrecisionAssembler precisionAssembler;
			@SubConfig
			public static Coagulator coagulator;
			@SubConfig
			public static Vulcanizer vulcanizer;
			@SubConfig
			public static ArtilleryHowitzer artilleryHowitzer;
			@SubConfig
			public static BallisticComputer ballisticComputer;
			@SubConfig
			public static MissileSilo missileSilo;
			@SubConfig
			public static ScanningConveyor scanningConveyor;
			@SubConfig
			public static Inserter inserter;
			@SubConfig
			public static AdvancedInserter advancedInserter;
			@SubConfig
			public static FluidInserter fluidInserter;
			@SubConfig
			public static AdvancedFluidInserter advancedFluidInserter;
			@SubConfig
			public static ChemicalDispenser chemicalDispenser;
			@SubConfig
			public static SmallDataBuffer smallDataBuffer;
			@SubConfig
			public static SkyCrateStation skycrateStation;
			@SubConfig
			public static SkyCartStation skycartStation;
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
			@SubConfig
			public static Emplacement emplacement;
			@SubConfig
			public static Filler filler;
			@SubConfig
			public static VehicleWorkshop vehicleWorkshop;

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

			public static class Filler
			{
				@Comment({"Energy capacity of the the Filler (in IF)."})
				public static int energyCapacity = 16000;

				@RequiresWorldRestart
				@Comment({"Dust capacity of the the Filler (in mB, 1 dustStack is 100mB)."})
				public static int dustCapacity = 32000;
			}

			public static class VehicleWorkshop
			{
				@Comment({"Energy capacity of the the Vehicle Workshop (in IF)."})
				public static int energyCapacity = 100000;

				@RequiresWorldRestart
				@Comment({"Fluid (diesel) capacity of the the Vehicle Workshop (in mB)."})
				public static int dieselCapacity = 24000;
			}

			public static class EffectCrates
			{
				@Comment({"The amount of fluid a Medical Crate can store (in mB)"})
				public static int mediCrateTankSize = 4000;

				@Comment({"The amount of fluid a Medical Crate uses after applying the effect (in mB)"})
				public static int mediCrateFluidDrain = 250;

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

				@Comment({"Additional energy capacity of the packer when the charging module upgrade is present."})
				public static int energyCapacityUpgrade = 16000000;

				@Comment({"Additional fluid capacity of the packer when the pump module upgrade is present."})
				public static int fluidCapacityUpgrade = 96000;

				@Comment({"Energy usage of the packer (if any stack/fluid/energy input/output is performed)."})
				public static int energyUsage = 4096;

				@Comment({"Duration of the packing process (in ticks)."})
				public static int actionTime = 260;
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
				@Comment({"Energy required to construct the radio station (in IF)."})
				public static int constructionEnergy = 200000;

				@Comment({"Energy capacity of the radio station (in IF)."})
				public static int energyCapacity = 32000;

				@Comment({"Energy usage when sending a signal (in IF)."})
				public static int energyUsage = 2048;

				@Comment({"Energy usage when a radio station is working."})
				public static int energyUsageIdle = 512;

				@Comment({"Range of the radio station (in which the signals can be received) in blocks from center (radius)."})
				@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_int")
				public static int radioRange = 128;

				@RangeDouble(min = 0, max = 1)
				@Comment({"How much the range decreases when there is bad weather (rain, snow) ( 1 - full range, 0.5 - half range, 0 - no range, etc.)"})
				public static double weatherHarshness = 0.75;
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
				public static int timePunchtapeProduction = 80;
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
				public static int energyUsage = 2048;

				@Comment({"Page printing duration (in ticks)."})
				public static int printTime = 90;

				@Comment({"Ink used per character printed (mB)."})
				public static int printInkUsage = 2;
			}

			public static class ChemicalBath
			{
				@Comment({"Energy capacity of the chemical bath."})
				public static int energyCapacity = 16000;

				@RequiresWorldRestart
				@Comment({"Fluid capacity of the chemical bath."})
				public static int fluidCapacity = 24000;
			}

			public static class ChemicalPainter
			{
				@Comment({"Energy capacity of the chemical painter."})
				public static int energyCapacity = 16000;

				@RequiresWorldRestart
				@Comment({"Fluid capacity of each tank of the chemical painter."})
				public static int fluidCapacity = 8000;
			}

			public static class Electrolyzer
			{
				@Comment({"Energy capacity of the electrolyzer."})
				public static int energyCapacity = 16000;

				@RequiresWorldRestart
				@Comment({"Fluid capacity of the electrolyzer."})
				public static int fluidCapacity = 12000;
			}

			public static class PrecisionAssembler
			{
				@Comment({"Energy capacity of the chemical bath."})
				public static int energyCapacity = 16000;

				@Comment({"Hatch opening (or closing) time (in ticks)"})
				public static int hatchTime = 40;

			}

			public static class ArtilleryHowitzer
			{
				@Comment({"Energy capacity of the artillery howitzer."})
				public static int energyCapacity = 4000000;

				@Comment({"Additional energy usage when moving the platform, loading or shooting."})
				public static int energyUsageActive = 1024;

				@Comment({"Passive energy usage of the howitzer (in IF)."})
				public static int energyUsagePassive = 2048;

				@Comment({"Time needed for the platform to ascend/descend (in ticks)."})
				public static int platformTime = 200;

				@Comment({"Time needed for the howitzer to fire (in ticks)."})
				public static int gunFireTime = 240;

				@RangeDouble(min = 0.0, max = 1.0)
				@Comment({"In what moment of animation should the gun fire the bullet (0.0-1.0 fraction of gunFireTime)"})
				public static double gunFireMoment = 0.5989;

				@Comment({"Time needed to open the howitzer door (in ticks)."})
				public static int doorTime = 248;

				@Comment({"How many degrees per tick can the howitzer rotate (in degrees)"})
				public static int rotateSpeed = 2;

				@Comment({"How long does it take for the howitzer to load or unload a shell to the platform shell rack (in ticks)"})
				public static int loadRackTime = 360;

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

			public static class ScanningConveyor
			{
				@Comment({"Energy capacity of the scanning conveyor."})
				public static int energyCapacity = 8000;

				@Comment({"Energy usage when scanning an ItemStack."})
				public static int energyUsage = 128;
			}

			public static class MissileSilo
			{
				@Comment({"Energy capacity of the missile silo (per one block of height) (in IF)."})
				public static int energyCapacity = 2500000;
			}

			public static class Radar
			{
				@Comment({"Energy required to construct the radar (in IF)."})
				public static int constructionEnergy = 200000;

				@Comment({"Energy capacity of the radar (in IF)."})
				public static int energyCapacity = 64000;

				@Comment({"Energy usage of the radar per tick (in IF)."})
				public static int energyUsage = 2048;
			}

			public static class Emplacement
			{
				@Comment({"Energy capacity of the emplacement (in IF)."})
				public static int energyCapacity = 32000;

				@Comment({"Time for the multiblock to open/close the lid (in ticks)."})
				public static int lidTime = 240;

				@Comment({"Interval for the multiblock weapon to update sighted targets (in ticks)."})
				public static int sightUpdateTime = 10;

				@Comment({"Energy required to perform a single repair action (in IF)."})
				public static int repairCost = 1024;

				@Comment({"Time for a single repair action (in ticks)."})
				public static int repairDelay = 40;

				@Comment({"Amount of turret health restored during single repair action (in half-hearts)."})
				public static int repairAmount = 4;
			}

			public static class Inserter
			{
				@Comment({"Energy capacity of the inserter (in IF)"})
				public static int energyCapacity = 4096;
				@Comment({"Energy usage of the inserter per tick (in IF)"})
				public static int energyUsage = 128;
				@Comment({"How long does it take for the inserter to perform a task (in ticks)"})
				public static int taskTime = 20;
				@Comment({"How many items can be taken per single operation"})
				public static int maxTake = 32;
			}

			public static class AdvancedInserter
			{
				@Comment({"Energy capacity of the inserter (in IF)"})
				public static int energyCapacity = 4096;
				@Comment({"Energy usage of the inserter per tick (in IF)"})
				public static int energyUsage = 256;
				@Comment({"How long does it take for the inserter to perform a task (in ticks)"})
				public static int taskTime = 10;
				@Comment({"How many items can be taken per single operation"})
				public static int maxTake = 64;

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

			public static class FuelStation
			{
				@Comment({"Energy capacity of the fuel station."})
				public static int energyCapacity = 16000;

				@Comment({"Energy amount used per one mB of fluid transfered."})
				public static int energyUsage = 4;

				@Comment({"Amount of fluid transfered per tick."})
				public static int fluidTransfer = 120;

				@RequiresWorldRestart
				@Comment({"Fluid capacity of the fuel station."})
				public static int fluidCapacity = 64000;
			}

			public static class LatexCollector
			{
				@Comment({"Collection time of a single bucket (in ticks)."})
				public static int collectTime = 12000;
			}

			public static class CO2Collector
			{
				@Comment({"CO2 collection time (in ticks)."})
				public static int blastFurnaceCollectTime = 20;

				@Comment({"CO2 amount collected (in mB)."})
				public static int blastFurnaceCollectAmount = 5;

				@Comment({"CO2 collection time (in ticks)."})
				public static int fermenterCollectTime = 20;

				@Comment({"CO2 amount collected (in mB)."})
				public static int fermenterCollectAmount = 2;
			}

			public static class Vulcanizer
			{
				@Comment({"Energy capacity of the vulcanizer (in IF)."})
				public static int energyCapacity = 16000;
			}

			public static class Coagulator
			{
				@Comment({"Energy capacity of the coagulator (in IF)."})
				public static int energyCapacity = 16000;

				@Comment({"Fluid capacity of a coagulator tank (in mB)."})
				public static int fluidCapacity = 16000;

				@Comment({"Default bucket wait time for a coagulator process, when not specified in recipe (in ticks)."})
				public static int bucketTime = 400;

				@Comment({"How long does it take to move a crane 1 block (in ticks)."})
				public static int craneMoveTime = 20;

				@Comment({"Duration of crane bucket actions (in ticks)."})
				public static int craneGrabTime = 20;
			}

			public static class AmmunitionAssembler
			{
				@Comment({"Energy capacity of the ammunition workshop (in IF)."})
				public static int energyCapacity = 16000;
			}

			public static class ProjectileWorkshop
			{
				@Comment({"Energy capacity of the Projectile Workshop (in IF)."})
				public static int energyCapacity = 16000;

				@Comment({"Energy usage of the Projectile Workshop during core production (in IF/t)"})
				public static int productionEnergyUsage = 50;

				@Comment({"Energy usage of the Projectile Workshop during core production (in IF/t)"})
				public static int fillingEnergyUsage = 70;

				@Comment({"Fluid capacity of a Projectile Workshop coolant (water) tank (in mB)."})
				public static int coolantTankCapacity = 6000;

				@Comment({"Fluid capacity of a Projectile Workshop fluid bullet component buffer tank (in mB)."})
				public static int componentTankCapacity = 1000;

				@Comment({"Maximum amount of bullet component stored in a Projectile Workshop (16 is default amount per one item / 1000mB of a fluid)."})
				public static int componentCapacity = 512;

				@Comment({"How long does it take to produce a projectile (in ticks, multiplied by caliber)."})
				public static int productionTime = 35;

				@Comment({"How long does it take to fill a projectile with components (in ticks, multiplied by caliber)."})
				public static int fillingTime = 35;
			}
		}

		public static class Weapons
		{
			//thanks for reminding me, trainmaniac ^^
			@Comment({"Whether blocks should be damaged when hit by bullets"})
			public static boolean blockDamage = true;

			@Comment({"Whether bullets should whistle, when flying close to player (slightly affects performance)"})
			public static boolean bulletsWhistleSound = true;
			@Comment({"Whether artillery shells should whistle, when about to impact"})
			public static boolean artilleryImpactSound = true;

			@SubConfig
			@LangKey("Ammunition")
			public static Ammunition ammunition;
			@SubConfig
			@LangKey("Submachinegun")
			public static Submachinegun submachinegun;
			@SubConfig
			@LangKey("Machinegun")
			public static Machinegun machinegun;
			@SubConfig
			@LangKey("Rifle")
			public static Rifle rifle;
			@SubConfig
			@LangKey("Assault Rifle")
			public static AssaultRifle assaultRifle;
			@SubConfig
			@LangKey("Mortar")
			public static Mortar mortar;
			@SubConfig
			@LangKey("Emplacement Weapons")
			public static EmplacementWeapons emplacementWeapons;
			@SubConfig
			@LangKey("Railgun")
			public static Railgun railgun;
			@SubConfig
			@LangKey("Grenade")
			public static Grenade grenade;
			@SubConfig
			@LangKey("Mines")
			public static Mines mines;
			@SubConfig
			@LangKey("Light Engineer Armor")
			public static LightEngineerArmor lightEngineerArmor;

			public static class LightEngineerArmor
			{
				@Comment({"The energy usage of the Infiltrator's Headgear when IR is active (in IF / 20 ticks)."})
				@RangeInt(min = 0)
				public static int irHeadgearEnergyUsage = 150;

				@Comment({"The energy usage of the Technician's Headgear when it is active (in IF / 20 ticks)."})
				@RangeInt(min = 0)
				public static int technicianHeadgearEnergyUsage = 50;

				@Comment({"The energy usage of the Engineer's Headgear when it is active (in IF / 20 ticks)."})
				@RangeInt(min = 0)
				public static int engineerHeadgearEnergyUsage = 225;

				@Comment({"The multiplier of electric damage taken when an anti-static mesh is worn under water (in half-hearts)."})
				@RangeDouble(min = 0)
				public static double antiStaticMeshWaterDamageMod = 1.5;

				@Comment({"The energy usage of the exoskeleton when running (in IF)."})
				@RangeInt(min = 0)
				public static int exoskeletonEnergyUsage = 200;

				@Comment({"The oxygen capacity of the scuba tank (in mB)."})
				@RangeInt(min = 0)
				@RequiresMcRestart
				public static int scubaTankCapacity = 10000;

				@Comment({"The oxygen capacity of the scuba tank (in mB / 20 ticks)."})
				@RangeInt(min = 0)
				public static int scubaTankUsage = 20;

			}

			public static class Mortar
			{
				@Comment({"Determines how fast the Mortar can be set up (in ticks)."})
				@RangeInt(min = 0)
				@RequiresMcRestart
				public static int setupTime = 100;

				@Comment({"Determines how fast the Mortar can be fired (in ticks)."})
				@RangeInt(min = 0)
				@RequiresMcRestart
				public static int shootTime = 80;

				@Comment({"The pitch turn speed of the Mortar (in degrees)."})
				@RequiresMcRestart
				public static float pitchSpeed = 1f;
			}

			public static class EmplacementWeapons
			{

				@SubConfig
				public static Autocannon autocannon;
				@SubConfig
				public static Machinegun machinegun;
				@SubConfig
				public static TeslaCoil teslaCoil;
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

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 200;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 24;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 2048;
				}

				public static class Machinegun
				{
					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 4;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 4;

					@Comment({"Time required to reload all the magazines."})
					public static int reloadTime = 200;

					@Comment({"Time required to fire a single bullet."})
					public static int bulletFireTime = 2;

					@Comment({"Time required for machinegun nest setup (barrel attachment animation) (in ticks)"})
					public static int setupTime = 150;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 150;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 24;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 1024;
				}

				public static class TeslaCoil
				{
					@Comment({" \"reload\" time after dealing damage (in ticks)."})
					public static int damageDelay = 10;

					@Comment({"Damage dealt to enemies (in half-hearts)."})
					public static int damage = 10;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 3096;

					@Comment({"Energy used per shot (in IF)"})
					public static int energyUsage = 2048;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 200;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 0;

					@Comment({"Enemy attack range (in blocks)"})
					public static float attackRadius = 16;
				}

				public static class InfraredObserver
				{
					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 2;

					@Comment({"Yaw rotation speed (degrees/tick, rotates only if sent a data packet, requires lowering the platform)"})
					public static int yawRotateTime = 1;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 100;

					@Comment({"Time required for observer setup (lens attachment animation) (in ticks)."})
					public static int setupTime = 300;

					//3.5 chunks
					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 56;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 1024;
				}

				public static class CPDS
				{
					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 8;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 8;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 350;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 16;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 4096;

					@Comment({"Time required to reload the magazine."})
					public static int reloadTime = 120;
				}

				public static class HeavyChemthrower
				{
					@Comment({"Time required for chemthrower setup (barrel extension animation) (in ticks)."})
					public static int setupTime = 100;

					@Comment({"Fluid tank capacity of the heavy chemthrower (in mB)."})
					public static int tankCapacity = 8000;

					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 2;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 1;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 120;

					@Comment({"Time required to fire a single chemthrower particle (in ticks)."})
					public static int sprayTime = 0;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 16;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 2048;
				}

				public static class HeavyRailgun
				{
					@Comment({"Time required to fire a single shot."})
					public static int shotFireTime = 40;

					@Comment({"Time required for loading a single projectile."})
					public static int reloadConveyorTime = 20;

					@Comment({"Time required for replacing the ammo box."})
					public static int reloadAmmoBoxTime = 100;

					@Comment({"Starting/max health of the turret (in half-hearts)"})
					public static int maxHealth = 350;

					@Comment({"Yaw rotation speed (degrees/tick)"})
					public static float yawRotateSpeed = 1.75f;

					@Comment({"Pitch rotation speed (degrees/tick)"})
					public static float pitchRotateSpeed = 0.65f;

					@Comment({"Enemy detection range (in blocks)"})
					public static float detectionRadius = 16;

					@Comment({"Base energy usage per tick (in IF)."})
					public static int energyUpkeepCost = 2048;
				}
			}

			public static class Railgun
			{
				@Comment({"If disabled, II will not make any changes to IE railgun. This also disables using railgun grenades (as they use a custom entity)."})
				public static boolean enableRailgunOverride = true;

				@Comment({"Make standard railgun rods to be able to penetrate mobs (depending on metal)."})
				public static boolean enablePenetration = true;

				@Comment({"Whether the railgun has recoil (pushes the shooter to back, depending on projectile mass)."})
				public static boolean railgunRecoil = true;

				@Comment({"Whether the railgun can only be used when in mainhand."})
				public static boolean disableRailgunOffhand = true;
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
				public static float throwSpeedModifier = 1.65f;
			}

			public static class Mines
			{
				@Comment({
						"Changes color of the tripmine ",
						"0 - steel",
						"1 - green",
						"2 - dull-yellow (makes your tripmine a true S-Mine straight out of ze [REDACTED])"
				})
				@RangeInt(min = 0, max = 2)
				public static int tripmineColor = 0;

				@Comment({"Time before colision is being detected for navy mine after placing it (in ticks)"})
				public static int navalMineArmTime = 60;


				@Comment({"Energy capacity of radio explosives (in IF)."})
				public static int energyCapacity = 240000;

				@Comment({"Energy usage by radio explosives per tick (in IF)."})
				public static int energyUsage = 10;

				@Comment({"Radio signal range of the radio explosives (radius)."})
				public static int radioRange = 32;

				@RangeDouble(min = 0, max = 1)
				@Comment({"How much the range decreases when there is bad weather (rain, snow) ( 1 - full range, 0.5 - half range, 0 - no range, etc.)"})
				public static double weatherHarshness = 0.35;

				@Comment({"A list of all entities which will not activate the Tripmine"})
				public static String[] tripmineBlacklist = new String[]{
						"minecraft:chicken",
						"minecraft:rabbit",
						"immersiveintelligence:bullet",
						"immersiveintelligence:silverfish",
						"immersiveintelligence:endermite"
				};

			}

			public static class Rifle
			{
				@Comment({"Time required to reload a stick magazine in SMG. (in ticks)"})
				public static int bulletReloadTime = 20;

				@Comment({"Time required to reload a drum magazine in SMG. (in ticks)"})
				public static int magazineReloadTime = 65;

				@Comment({"Time required to aim the SMG. (in ticks)"})
				public static int clipSize = 4;

				@Comment({"Time required to aim the SMG. (in ticks)"})
				public static int aimTime = 15;

				@Comment({"Time required to fire a single bullet."})
				public static int bulletFireTime = 20;

				@Comment({"Time required to fire a single bullet."})
				public static int bulletFireTimeSemiAuto = 10;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float longBarrelVelocityMod = 2.25f;

				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float recoilHorizontal = 0.5f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilVertical = 2.75f;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float maxRecoilHorizontal = 3.5f;

				@Comment({"Maximum amount of vertical recoil."})
				public static float maxRecoilVertical = 12f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilSemiAutoVertical = 1f;

				@Comment({"Range in which enemies will be attracted to the gunshot."})
				public static int enemyAttractRange = 32;
			}

			public static class Submachinegun
			{
				@Comment({"Time required to reload a stick magazine in SMG. (in ticks)"})
				public static int clipReloadTime = 45;

				@Comment({"Time required to reload a drum magazine in SMG. (in ticks)"})
				public static int drumReloadTime = 80;

				@Comment({"Time required to aim the SMG. (in ticks)"})
				public static int aimTime = 10;

				@Comment({"Time required to aim the SMG with folded stock upgrade."})
				public static int aimTimeFoldedStock = 4;

				@Comment({"Time required to fire a single bullet."})
				public static int bulletFireTime = 2;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float sturdyBarrelVelocityMod = 1.25f;

				//some people are able to report "bugs", yet unable to reset a config file when updating versions
				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float newRecoilHorizontal = 0.345f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float newRecoilVertical = 0.625f;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float newMaxRecoilHorizontal = 14.5f;

				@Comment({"Maximum amount of vertical recoil."})
				public static float newMaxRecoilVertical = 9f;

				@Comment({"Range in which enemies will be attracted to the gunshot."})
				public static int enemyAttractRange = 36;

				@Comment({"Range in which enemies will be attracted to the gunshot for a suppressed SMG."})
				public static int enemyAttractRangeSuppressor = 6;
			}

			public static class AssaultRifle
			{
				//--- Stock ---//

				@Comment({"Time required to reload a magazine in AR. (in ticks)"})
				public static int clipReloadTime = 45;

				@Comment({"Time required to load a railgun grenade to an upgraded AR. (in ticks)"})
				public static int grenadeReloadTime = 80;

				@Comment({"Time required to aim the AR. (in ticks)"})
				public static int aimTime = 15;

				@Comment({"Time required to fire a single bullet in Auto mode."})
				public static int bulletFireTimeAuto = 2;

				@Comment({"Time required to fire a single bullet in Semi-Auto mode."})
				public static int bulletFireTimeSemiAuto = 5;

				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float recoilHorizontal = 0.345f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilVertical = 0.425f;

				@Comment({"Maximum amount of horizontal recoil."})
				public static float maxRecoilHorizontal = 8.5f;

				@Comment({"Maximum amount of vertical recoil."})
				public static float maxRecoilVertical = 8.5f;

				@Comment({"Range in which enemies will be attracted to the gunshot."})
				public static int enemyAttractRange = 36;

				//--- Upgrades ---//

				@Comment({"Energy used per tick by the Infrared Scope upgrade."})
				public static int upgradeIRScopeEnergy = 20;

				@Comment({"Energy used per shot by the Electric Firing Motor upgrade."})
				public static int upgradeFiringMotorEnergy = 10;

				@Comment({"Energy used per shot by the Railgun Chamber upgrade."})
				public static int upgradeRailgunChamberEnergy = 40;

				@Comment({"Energy used per tick by the Gyroscopic Stabilizer upgrade."})
				public static int upgradeStabilizerEnergy = 10;

				@Comment({"Energy used per tick by the Stereoscopic Rangefinder upgrade."})
				public static int upgradeRangefinderEnergy = 1024;
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
				public static int maxOverheat = 250;

				@Comment({"Amount of horizontal recoil after taking a shot."})
				public static float recoilHorizontal = 2.5f;

				@Comment({"Amount of vertical recoil after taking a shot."})
				public static float recoilVertical = 2.5f;

				@Comment({"Fire rate multiplier when heavy barrel is mouted on mg."})
				public static float heavyBarrelFireRateMultiplier = 0.25f;

				@Comment({"Horizontal recoil after taking a shot with heavy barrel mounted."})
				public static float recoilHBHorizontal = 1.25f;

				@Comment({"Vertical recoil after taking a shot with heavy barrel mounted."})
				public static float recoilHBVertical = 1.25f;

				@Comment({"Water usage when water cooling upgrade is mounted on mg (in mB)."})
				public static int waterCoolingFluidUsage = 10;

				@RequiresMcRestart
				@Comment({"Coolant tank capacity of the water cooling upgrade."})
				public static int waterCoolingTankCapacity = 4000;

				@Comment({"Setup time multiplier when the mg is mounted on a tripod."})
				public static float tripodSetupTimeMultiplier = 2.5f;

				@Comment({"Recoil multiplier when the mg is mounted on a tripod."})
				public static float tripodRecoilMultiplier = 0.125f;

				@Comment({"Setup time multiplier when the precise bipod is mouted on mg."})
				public static float preciseBipodSetupTimeMultiplier = 1.5f;

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
				@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
				public static float[] machinegunScopeMaxZoom = new float[]{0.55f, 0.35f, 0.15f};

				@Comment({"Shield's initial strength (resistance vs attacks)."})
				public static float shieldStrengthInitial = 45;

				@Comment({"Setup time multiplier when a shield is mouted on mg."})
				public static float shieldSetupTimeMultiplier = 0.5f;

			}
		}

		public static class Ammunition
		{
			@Comment({"Whether explosive ammo components can break blocks"})
			public static boolean blockDamage = true;

			@Comment({"Default velocity of a mortar shell."})
			public static float mortarVelocity = 8f*0.7f;

			@Comment({"Default velocity of a light howitzer shell."})
			public static float lightHowiVelocity = 10f*0.7f;

			@Comment({"Default velocity of an artillery howitzer shell."})
			public static float artilleryHowiVelocity = 70f*0.7f;

			@Comment({"Default velocity of a railgun grenade."})
			public static float railgunGrenadeVelocity = 5f*0.7f;

			@Comment({"Default velocity of a thrown grenade."})
			public static float grenadeVelocity = 1f;

			@Comment({"Default velocity of a machinegun bullet."})
			public static float autocannonVelocity = 6f*0.7f;

			@Comment({"Default velocity of a machinegun bullet."})
			public static float mgVelocity = 6.5f*0.7f;

			@Comment({"Default velocity of a submachinegun bullet."})
			public static float smgVelocity = 5f*0.7f;

			@Comment({"Default velocity of an assault rifle bullet."})
			public static float stgVelocity = 6.5f*0.7f;

			@Comment({"Default velocity of a revolver cartridge."})
			public static float revolverVelocity = 6f*0.7f;
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

			@Comment({"The RGB color of the data wire."})
			public static int tripwireColouration = 0x967e6d;

			@Comment({"The maximum length of the tripwire. Should be medium range at best."})
			public static int tripwireLength = 8;
		}

		public static class Vehicles
		{
			@SubConfig
			public static Motorbike motorbike;
			@SubConfig
			public static FieldHowitzer fieldHowitzer;


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

				@Comment({"Time required to set up field howitzer after moving it."})
				public static int setupTime = 15;

				@Comment({"Time required to reload a field howitzer."})
				public static int reloadTime = 80;

				@Comment({"Time required to fire a single shell using the field howitzer."})
				public static int fireTime = 50;
			}
		}

		public static class MechanicalDevices
		{
			@Comment({"The modifier of internal torque of a machine using this gear.", GEARS})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float[] gearTorqueModifier = new float[]{0.25f, 0.5f, 1f, 1.5f, 2f};

			@Comment({"Belts will break if Torque is higher than this value.", BELTS})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] beltMaxTorque = new int[]{50, 200, 100};

			@Comment({"Torque loss, later multiplied by belt length.", BELTS})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float[] beltTorqueLoss = new float[]{0.05f, 0.1f, 0.01f};

			@Comment({"Max length of the belt, works exactly like wires (measured in blocks).", BELTS})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_intA")
			public static int[] beltLength = new int[]{8, 16, 16};

			@Comment({"Immersive Flux to Rotary Flux conversion ratio (default 4 RoF = 1 IF)."})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float rofConversionRatio = 4f;

			@Comment({"Default torque, used as a fallback, when IE rotational device is not recognised."})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float dynamoDefaultTorque = 16f;

			@Comment({"Torque multiplier for the windmill."})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float dynamoWindmillTorque = 20f;

			@Comment({"Torque multiplier for the windmill."})
			@Mapped(mapClass = IIConfigHandler.class, mapName = "manual_floatA")
			public static float dynamoWatermillTorque = 24f;

			@Comment({"Torque multiplier for the axle from MysticalMechanics."})
			@Mapped(mapClass = Config.class, mapName = "manual_floatA")
			public static float dynamoAxleTorque = 18f;
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent ev)
	{
		Config.manual_bool.put("petroleumHere", false);
		Config.manual_bool.put("baublesHere", false);
		Config.validateAndMapValues(IIConfig.class);

		Config.manual_int.put("radio_station_range", RadioStation.radioRange);

		if(ev.getModID().equals(ImmersiveIntelligence.MODID))
			ConfigManager.sync(ImmersiveIntelligence.MODID, Type.INSTANCE);
	}
}
