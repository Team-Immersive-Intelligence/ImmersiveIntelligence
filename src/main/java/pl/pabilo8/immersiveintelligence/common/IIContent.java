package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.ammo.emplacement_weapons.*;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice;
import pl.pabilo8.immersiveintelligence.common.block.fortification.*;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITellermine;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITripmine;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITripwireConnector;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement.EmplacementWeapon;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIGearbox;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.simple.*;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIMetalBase.Metals;
import pl.pabilo8.immersiveintelligence.common.item.*;
import pl.pabilo8.immersiveintelligence.common.item.ammo.*;
import pl.pabilo8.immersiveintelligence.common.item.armor.*;
import pl.pabilo8.immersiveintelligence.common.item.crafting.*;
import pl.pabilo8.immersiveintelligence.common.item.crafting.material.*;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIDataWireCoil;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIPunchtape;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIISmallWireCoil;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorBelt;
import pl.pabilo8.immersiveintelligence.common.item.mechanical.ItemIIMotorGear;
import pl.pabilo8.immersiveintelligence.common.item.tools.*;
import pl.pabilo8.immersiveintelligence.common.item.tools.backpack.ItemIIAdvancedPowerPack;
import pl.pabilo8.immersiveintelligence.common.item.weapons.*;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIISlab;
import pl.pabilo8.immersiveintelligence.common.util.block.BlockIIStairs;
import pl.pabilo8.immersiveintelligence.common.world.BiomeWasteland;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.ImmersiveIntelligence.MODID;
import static pl.pabilo8.immersiveintelligence.common.CommonProxy.makeFluid;

/**
 * @author Pabilo8
 * @since 08.12.2020
 */
public class IIContent
{
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();
	public static final List<Class<? extends TileEntity>> TILE_ENTITIES = new ArrayList<>();
	public static final List<IMultiblock> MULTIBLOCKS = new ArrayList<>();

	public static final IICreativeTab II_CREATIVE_TAB = new IICreativeTab(MODID);

	//Shares code with Immersive Energy, long live II-IEn Cooperation!
	public static List<Predicate<TileEntity>> tileEntitiesWeDontLike = new ArrayList<>();

	//--- Upgrades ---//

	public static final MachineUpgrade UPGRADE_PACKER_FLUID = CommonProxy.createMachineUpgrade("packer_fluid"); //allows filling items with fluids
	public static final MachineUpgrade UPGRADE_PACKER_ENERGY = CommonProxy.createMachineUpgrade("packer_energy"); //allows charging items with IF
	public static final MachineUpgrade UPGRADE_PACKER_NAMING = CommonProxy.createMachineUpgrade("packer_naming"); //allows (re)naming items on conveyor
	public static final MachineUpgrade UPGRADE_PACKER_RAILWAY = CommonProxy.createMachineUpgrade("packer_railway"); //makes packer accept minecarts instead of items

	//used by effect crates
	public static final MachineUpgrade UPGRADE_INSERTER = CommonProxy.createMachineUpgrade("inserter");
	//increases machine speed
	public static final MachineUpgrade UPGRADE_IMPROVED_GEARBOX = CommonProxy.createMachineUpgrade("improved_gearbox");
	//more sawdust for cost of planks
	public static final MachineUpgrade UPGRADE_SAW_UNREGULATOR = CommonProxy.createMachineUpgrade("saw_unregulator");
	//allows to use belt fed upgrade for mg
	public static final MachineUpgrade UPGRADE_MG_LOADER = CommonProxy.createMachineUpgrade("mg_loader");

	//allows the radar to detect radio signal emitters and send their positions
	public static final MachineUpgrade UPGRADE_RADIO_LOCATORS = CommonProxy.createMachineUpgrade("radio_locators");

	//changes the projectile workshop to *fill* projectiles
	public static final MachineUpgrade UPGRADE_CORE_FILLER = CommonProxy.createMachineUpgrade("core_filler");

	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN = EmplacementWeapon.register(EmplacementWeaponMachinegun::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER = EmplacementWeapon.register(EmplacementWeaponInfraredObserver::new);

	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON = EmplacementWeapon.register(EmplacementWeaponAutocannon::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER = EmplacementWeapon.register(EmplacementWeaponHeavyChemthrower::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN = EmplacementWeapon.register(EmplacementWeaponHeavyRailgun::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER = EmplacementWeapon.register(EmplacementWeaponSpotlightTower::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_TESLA = EmplacementWeapon.register(EmplacementWeaponTeslaCoil::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_MORTAR = EmplacementWeapon.register(EmplacementWeaponMortar::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER = EmplacementWeapon.register(EmplacementWeaponLightHowitzer::new);

	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_CPDS = EmplacementWeapon.register(EmplacementWeaponCPDS::new);

	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_FALLBACK_GRENADES = CommonProxy.createMachineUpgrade("emplacement_grenades");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_STURDY_BEARINGS = CommonProxy.createMachineUpgrade("emplacement_bearings");

	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL = CommonProxy.createMachineUpgrade("mg_heavy_barrel");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED = CommonProxy.createMachineUpgrade("mg_watercooled");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER = CommonProxy.createMachineUpgrade("mg_bunker");

	//--- Items ---//

	//materials
	public static ItemIIMaterial itemMaterial = new ItemIIMaterial();
	public static ItemIIMaterialIngot itemMaterialIngot = new ItemIIMaterialIngot();
	public static ItemIIMaterialPlate itemMaterialPlate = new ItemIIMaterialPlate();
	public static ItemIIMaterialRod itemMaterialRod = new ItemIIMaterialRod();
	public static ItemIIMaterialDust itemMaterialDust = new ItemIIMaterialDust();
	public static ItemIIMaterialNugget itemMaterialNugget = new ItemIIMaterialNugget();
	public static ItemIIMaterialWire itemMaterialWire = new ItemIIMaterialWire();
	public static ItemIIMaterialSpring itemMaterialSpring = new ItemIIMaterialSpring();
	public static ItemIIMaterialGem itemMaterialGem = new ItemIIMaterialGem();
	public static ItemIIMaterialBoule itemMaterialBoule = new ItemIIMaterialBoule();
	public static ItemIIMetalPressMold itemPressMold = new ItemIIMetalPressMold();
	public static ItemIIVulcanizerMold itemVulcanizerMold = new ItemIIVulcanizerMold();
	public static ItemIIFunctionalCircuit itemCircuit = new ItemIIFunctionalCircuit();
	public static ItemIIMotorBelt itemMotorBelt = new ItemIIMotorBelt();
	public static ItemIIMotorGear itemMotorGear = new ItemIIMotorGear();

	//ammo
	public static ItemIIAmmoCasing itemAmmoCasing = new ItemIIAmmoCasing();
	public static ItemIIAmmoArtillery itemAmmoArtillery = new ItemIIAmmoArtillery();
	public static ItemIIAmmoMortar itemAmmoMortar = new ItemIIAmmoMortar();
	public static ItemIIAmmoLightArtillery itemAmmoLightArtillery = new ItemIIAmmoLightArtillery();
	public static ItemIIAmmoAutocannon itemAmmoAutocannon = new ItemIIAmmoAutocannon();
	public static ItemIIAmmoGrenade itemGrenade = new ItemIIAmmoGrenade();
	public static ItemIIAmmoRailgunGrenade itemRailgunGrenade = new ItemIIAmmoRailgunGrenade();
	public static ItemIIAmmoMachinegun itemAmmoMachinegun = new ItemIIAmmoMachinegun();
	public static ItemIIAmmoAssaultRifle itemAmmoAssaultRifle = new ItemIIAmmoAssaultRifle();
	public static ItemIIAmmoSubmachinegun itemAmmoSubmachinegun = new ItemIIAmmoSubmachinegun();
	public static ItemIIAmmoRevolver itemAmmoRevolver = new ItemIIAmmoRevolver();

	public static ItemIIBulletMagazine itemBulletMagazine = new ItemIIBulletMagazine();
	public static ItemIICasingPouch itemCasingPouch = new ItemIICasingPouch();

	//tools
	public static ItemIISkycrateMount itemSkycrateMount = new ItemIISkycrateMount();
	public static ItemIILighter itemLighter = new ItemIILighter();
	public static ItemIIElectricHammer itemHammer = new ItemIIElectricHammer();
	public static ItemIITrenchShovel itemTrenchShovel = new ItemIITrenchShovel();
	public static ItemIIElectricWirecutter itemWirecutter = new ItemIIElectricWirecutter();
	public static ItemIIWrench itemWrench = new ItemIIWrench();
	public static ItemIIElectricWrench itemElectricWrench = new ItemIIElectricWrench();

	public static ItemIITripodPeriscope itemTripodPeriscope = new ItemIITripodPeriscope();
	public static ItemIIMineDetector itemMineDetector = new ItemIIMineDetector();

	public static ItemIIDrillHead itemDrillhead = new ItemIIDrillHead();
	//Don't know if i should make a seperate item for a torque meter
	public static ItemIITachometer itemTachometer = new ItemIITachometer();
	public static ItemIIDataWireCoil itemDataWireCoil = new ItemIIDataWireCoil();
	public static ItemIISmallWireCoil itemSmallWireCoil = new ItemIISmallWireCoil();
	public static ItemIITripWireCoil itemTripWireCoil = new ItemIITripWireCoil();
	public static ItemIIMinecart itemMinecart = new ItemIIMinecart();
	public static ItemIIRadioTuner itemRadioTuner = new ItemIIRadioTuner();
	public static ItemIIMeasuringCup itemMeasuringCup = new ItemIIMeasuringCup();
	public static ItemIIPrecisionTool itemPrecissionTool = new ItemIIPrecisionTool();
	public static ItemIIAssemblyScheme itemAssemblyScheme = new ItemIIAssemblyScheme();
	public static ItemIISawBlade itemSawblade = new ItemIISawBlade();
	public static ItemIIBinoculars itemBinoculars = new ItemIIBinoculars();
	public static ItemIIMachinegun itemMachinegun = new ItemIIMachinegun();
	public static ItemIISubmachinegun itemSubmachinegun = new ItemIISubmachinegun();
	public static ItemIIAssaultRifle itemAssaultRifle = new ItemIIAssaultRifle();
	public static ItemIIRifle itemRifle = new ItemIIRifle();
	public static ItemIIMortar itemMortar = new ItemIIMortar();
	public static ItemIIWeaponUpgrade itemWeaponUpgrade = new ItemIIWeaponUpgrade();

	//armor
	public static ArmorMaterial ARMOR_MATERIAL_LIGHT_ENGINEER = EnumHelper.addArmorMaterial("light_engineer_armor",
			ImmersiveIntelligence.MODID+":light_engineer_armor", 42,
			new int[]{4, 7, 8, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);
	public static ItemIILightEngineerHelmet itemLightEngineerHelmet = new ItemIILightEngineerHelmet();
	public static ItemIILightEngineerChestplate itemLightEngineerChestplate = new ItemIILightEngineerChestplate();
	public static ItemIILightEngineerLeggings itemLightEngineerLeggings = new ItemIILightEngineerLeggings();
	public static ItemIILightEngineerBoots itemLightEngineerBoots = new ItemIILightEngineerBoots();
	public static ItemIIArmorUpgrade itemArmorUpgrade = new ItemIIArmorUpgrade();

	//Backpacks
	public static ItemIIAdvancedPowerPack itemAdvancedPowerPack = new ItemIIAdvancedPowerPack();
	public static final String NBT_AdvancedPowerpack = "II:Powerpack";


	//data
	@IBatchOredictRegister(oreDict = "punchtape")
	public static ItemIIPunchtape itemPunchtape = new ItemIIPunchtape();
	public static ItemIIPrintedPage itemPrintedPage = new ItemIIPrintedPage();
	public static ItemIITracerPowder itemTracerPowder = new ItemIITracerPowder();

	//--- Blocks ---//

	//rubber
	public static BlockIIRubberLog blockRubberLog = new BlockIIRubberLog();
	public static BlockIIRubberLeaves blockRubberLeaves = new BlockIIRubberLeaves();
	public static BlockIIRubberSapling blockRubberSapling = new BlockIIRubberSapling();
	public static BlockIICharredLog blockCharredLog = new BlockIICharredLog();

	//metal
	@IBatchOredictRegister(oreDict = "ore")
	public static BlockIIOre blockOre = new BlockIIOre();
	@IBatchOredictRegister(oreDict = "sheetmetal")
	public static BlockIIMetalBase blockSheetmetal = new BlockIIMetalBase("sheetmetal");
	@IBatchOredictRegister(oreDict = "block")
	public static BlockIIMetalBase blockMetalStorage = new BlockIIMetalBase("storage");
	@IBatchOredictRegister(oreDict = "slab")
	public static BlockIISlab<Metals> blockMetalSlabs = new BlockIISlab<>(blockSheetmetal);
	@IBatchOredictRegister(oreDict = "slabSheetmetal")
	public static BlockIISlab<Metals> blockSheetmetalSlabs = new BlockIISlab<>(blockMetalStorage);

	//regular blocks
	public static BlockIISandbags blockSandbags = new BlockIISandbags();
	public static BlockIIClothDecoration blockClothDecoration = new BlockIIClothDecoration();
	public static BlockIIMetalDecoration blockMetalDecoration = new BlockIIMetalDecoration();

	//b e t o n
	public static BlockIIConcreteDecoration blockConcreteDecoration = new BlockIIConcreteDecoration();
	public static BlockIISlab<ConcreteDecorations> blockConcreteSlabs = new BlockIISlab<>(blockConcreteDecoration);
	public static BlockIIStairs[] blockIIConcreteStairs = BlockIIConcreteDecoration.getStairs();

	//mesh fences
	public static BlockIIMetalChainFence blockMetalFortification = new BlockIIMetalChainFence();
	public static BlockIIWoodenChainFence blockWoodenFortification = new BlockIIWoodenChainFence();
	//tank trap
	public static BlockIIMetalFortification1 blockMetalFortification1 = new BlockIIMetalFortification1();

	//devices
	public static BlockIIMetalDevice blockMetalDevice = new BlockIIMetalDevice();
	public static BlockIIMetalDevice1 blockMetalDevice1 = new BlockIIMetalDevice1();
	public static BlockIIDataDevice blockDataConnector = new BlockIIDataDevice();
	public static BlockIISmallCrate blockSmallCrate = new BlockIISmallCrate();

	//ammunition
	public static BlockIIMineSign blockMineSign = new BlockIIMineSign();
	public static BlockIITripmine blockTripmine = new BlockIITripmine();
	public static BlockIITellermine blockTellermine = new BlockIITellermine();
	public static BlockIIRadioExplosives blockRadioExplosives = new BlockIIRadioExplosives();
	public static ItemIINavalMine itemNavalMine = new ItemIINavalMine();
	public static BlockIITripwireConnector blockTripwireConnector = new BlockIITripwireConnector();

	//rotary devices
	public static BlockIIMechanicalDevice blockMechanicalDevice = new BlockIIMechanicalDevice();
	public static BlockIIMechanicalDevice1 blockMechanicalDevice1 = new BlockIIMechanicalDevice1();
	public static BlockIIGearbox blockGearbox = new BlockIIGearbox();
	public static BlockIIMechanicalConnector blockMechanicalConnector = new BlockIIMechanicalConnector();
	public static BlockIIWoodenMultiblock blockWoodenMultiblock = new BlockIIWoodenMultiblock();

	//multiblocks
	public static BlockIIMetalMultiblock0 blockMetalMultiblock0 = new BlockIIMetalMultiblock0();
	public static BlockIIMetalMultiblock1 blockMetalMultiblock1 = new BlockIIMetalMultiblock1();
	public static BlockIIFenceGateMultiblock blockFenceGateMultiblock = new BlockIIFenceGateMultiblock();

	//fluid blocks
	public static BlockIIFluid blockFluidInkBlack, blockFluidInkCyan, blockFluidInkMagenta, blockFluidInkYellow;
	public static BlockIIFluid blockFluidEtchingAcid, blockFluidSulfuricAcid, blockFluidNitricAcid, blockFluidHydrofluoricAcid, blockFluidFormicAcid;
	public static BlockIIFluid blockFluidAmmonia, blockFluidMethanol;
	public static BlockIIFluid blockFluidBrine;
	public static BlockIIFluid blockGasHydrogen, blockGasOxygen, blockGasChlorine, blockGasCO2, blockGasCO;
	public static BlockIIFluid blockGasMustardGas;
	public static BlockIIFluid blockFluidLatex;

	//fluids
	public static Fluid fluidInkBlack, fluidInkCyan, fluidInkMagenta, fluidInkYellow;
	public static Fluid fluidEtchingAcid, fluidSulfuricAcid, fluidHydrofluoricAcid, fluidNitricAcid, fluidFormicAcid;
	public static Fluid fluidAmmonia, fluidMethanol;
	public static Fluid fluidBrine;
	public static Fluid gasHydrogen, gasOxygen, gasChlorine, gasCO2, gasCO;
	public static Fluid gasMustardGas;
	public static Fluid fluidLatex;

	//biomes
	public static BiomeWasteland biomeWasteland = new BiomeWasteland();

	static
	{
		IIContent.fluidInkBlack = makeFluid("ink", 6000, 2250);
		IIContent.fluidInkCyan = makeFluid("ink_cyan", 6000, 2250);
		IIContent.fluidInkMagenta = makeFluid("ink_magenta", 6000, 2250);
		IIContent.fluidInkYellow = makeFluid("ink_yellow", 6000, 2250);
		IIContent.fluidEtchingAcid = makeFluid("etching_acid", 2900, 1200);
		IIContent.fluidSulfuricAcid = makeFluid("sulfuric_acid", 1830, 2670);
		IIContent.fluidHydrofluoricAcid = makeFluid("hydrofluoric_acid", 1170, 981);
		IIContent.fluidFormicAcid = makeFluid("formic_acid", 1221, 1784);
		IIContent.fluidNitricAcid = makeFluid("nitric_acid", 1510, 2500, "rdx_fluids/");
		IIContent.fluidBrine = makeFluid("brine", 1030, 1002);
		IIContent.gasHydrogen = makeFluid("hydrogen", -1000+100, 88).setGaseous(true);
		IIContent.gasOxygen = makeFluid("oxygen", -1000+200, 204).setGaseous(true);
		IIContent.gasCO2 = makeFluid("carbon_dioxide", -1000+100, 147).setGaseous(true);
		IIContent.gasCO = makeFluid("carbon_monoxide", -1000+145, 166).setGaseous(true);
		IIContent.gasChlorine = makeFluid("chlorine", -1000+100, 132).setGaseous(true);
		IIContent.fluidAmmonia = makeFluid("ammonia", 771, 1007, "rdx_fluids/");
		IIContent.fluidMethanol = makeFluid("methanol", 792, 553, "rdx_fluids/");
		IIContent.fluidLatex = makeFluid("latex", 4300, 3500);
		IIContent.gasMustardGas = makeFluid("mustard_gas", 127, 340); //heavier than water

		IIContent.blockFluidInkBlack = new BlockIIFluid("ink", IIContent.fluidInkBlack, Material.WATER);
		IIContent.blockFluidInkCyan = new BlockIIFluid("ink_cyan", IIContent.fluidInkCyan, Material.WATER);
		IIContent.blockFluidInkMagenta = new BlockIIFluid("ink_magenta", IIContent.fluidInkMagenta, Material.WATER);
		IIContent.blockFluidInkYellow = new BlockIIFluid("ink_yellow", IIContent.fluidInkYellow, Material.WATER);
		IIContent.blockFluidEtchingAcid = new BlockIIFluid("etching_acid", IIContent.fluidEtchingAcid, Material.WATER);
		IIContent.blockFluidSulfuricAcid = new BlockIIFluid("sulfuric_acid", IIContent.fluidSulfuricAcid, Material.WATER);
		IIContent.blockFluidNitricAcid = new BlockIIFluid("nitric_acid", IIContent.fluidNitricAcid, Material.WATER);
		IIContent.blockFluidHydrofluoricAcid = new BlockIIFluid("hydrofluoric_acid", IIContent.fluidHydrofluoricAcid, Material.WATER);
		IIContent.blockFluidFormicAcid = new BlockIIFluid("formic_acid", IIContent.fluidFormicAcid, Material.WATER);
		IIContent.blockFluidBrine = new BlockIIFluid("brine", IIContent.fluidBrine, Material.WATER);
		IIContent.blockFluidLatex = new BlockIIFluid("latex", IIContent.fluidLatex, Material.WATER);

		IIContent.blockFluidAmmonia = new BlockIIFluid("ammonia", IIContent.fluidAmmonia, Material.WATER);
		IIContent.blockFluidMethanol = new BlockIIFluid("methanol", IIContent.fluidMethanol, Material.WATER);

		IIContent.blockGasHydrogen = new BlockIIFluid("hydrogen", IIContent.gasHydrogen, Material.WATER);
		IIContent.blockGasOxygen = new BlockIIFluid("oxygen", IIContent.gasOxygen, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.WATER_BREATHING, 20, 0));
		IIContent.blockGasChlorine = new BlockIIFluid("chlorine", IIContent.gasChlorine, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 1));
		IIContent.blockGasCO2 = new BlockIIFluid("carbon_dioxide", IIContent.gasCO2, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
		IIContent.blockGasCO = new BlockIIFluid("carbon_oxide", IIContent.gasCO, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
		IIContent.blockGasHydrogen = new BlockIIFluid("mustard_gas", IIContent.gasMustardGas, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.POISON, 60, 0));
	}

	//dummy method, called so that static fields above get loaded
	static void init()
	{
		new ItemStack(IIContent.itemLightEngineerHelmet);
	}
}
