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
import net.minecraftforge.fluids.FluidRegistry;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.UpgradePurpose;
import pl.pabilo8.immersiveintelligence.common.ammo.components.*;
import pl.pabilo8.immersiveintelligence.common.ammo.components.explosives.AmmoComponentHMX;
import pl.pabilo8.immersiveintelligence.common.ammo.components.explosives.AmmoComponentRDX;
import pl.pabilo8.immersiveintelligence.common.ammo.components.explosives.AmmoComponentTNT;
import pl.pabilo8.immersiveintelligence.common.ammo.components.incendiary.AmmoComponentWhitePhosphorus;
import pl.pabilo8.immersiveintelligence.common.ammo.components.nuke.AmmoComponentNuke;
import pl.pabilo8.immersiveintelligence.common.ammo.cores.*;
import pl.pabilo8.immersiveintelligence.common.ammo.propellants.*;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice;
import pl.pabilo8.immersiveintelligence.common.block.fortification.*;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDecoration;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITellermine;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITripmine;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIITripwireConnector;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIGateMultiblock;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIGearbox;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.BlockIIMechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.block.simple.*;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIConcreteDecoration.ConcreteDecorations;
import pl.pabilo8.immersiveintelligence.common.block.simple.BlockIIMetalBase.Metals;
import pl.pabilo8.immersiveintelligence.common.item.*;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoRailgunGrenade;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIINavalMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoArtilleryHeavy;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoArtilleryLight;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoArtilleryMedium;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoMortar;
import pl.pabilo8.immersiveintelligence.common.item.ammo.grenade.ItemIIAmmoGrenade;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.*;
import pl.pabilo8.immersiveintelligence.common.item.ammo.missile.ItemIIAmmoGuidedMissile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.missile.ItemIIAmmoRocketHeavy;
import pl.pabilo8.immersiveintelligence.common.item.ammo.missile.ItemIIAmmoRocketLight;
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
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 08.12.2020
 */
public class IIContent
{
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();
	public static final List<Class<? extends TileEntity>> TILE_ENTITIES = new ArrayList<>();
	public static final List<IMultiblock> MULTIBLOCKS = new ArrayList<>();

	public static final IICreativeTab II_CREATIVE_TAB = new IICreativeTab(MODID);

	//--- Upgrades ---//
	//allows filling items with fluids
	public static final Upgrade UPGRADE_PACKER_FLUID = new Upgrade("packer_fluid")
			.withType(UpgradePurpose.FULL_CONVERSION);
	//allows charging items with IF
	public static final Upgrade UPGRADE_PACKER_ENERGY = new Upgrade("packer_energy")
			.withType(UpgradePurpose.FULL_CONVERSION);
	//allows (re)naming items on conveyor
	public static final Upgrade UPGRADE_PACKER_NAMING = new Upgrade("packer_naming")
			.withType(UpgradePurpose.SPECIAL);
	//makes packer accept minecarts instead of items
	public static final Upgrade UPGRADE_PACKER_RAILWAY = new Upgrade("packer_railway")
			.withType(UpgradePurpose.FULL_CONVERSION);

	//used by effect crates
	public static final Upgrade UPGRADE_INSERTER = new Upgrade("inserter")
			.withType(UpgradePurpose.SPECIAL);
	//allows to use belt fed upgrade for mg
	public static final Upgrade UPGRADE_MG_LOADER = new Upgrade("mg_loader")
			.withType(UpgradePurpose.SPECIAL);

	//increases machine speed
	public static final Upgrade UPGRADE_IMPROVED_GEARBOX = new Upgrade("improved_gearbox")
			.withType(UpgradePurpose.SPEED);
	//more sawdust for cost of planks
	public static final Upgrade UPGRADE_SAW_UNREGULATOR = new Upgrade("saw_unregulator")
			.withType(UpgradePurpose.EFFICIENCY);

	//allows printing punchtapes
	public static final Upgrade UPGRADE_PRESS_PUNCHTAPES = new Upgrade("printing_press/punchtapes")
			.withType(UpgradePurpose.SPECIAL);
	//allows printing bound pages, books and newspapers
	public static final Upgrade UPGRADE_PRESS_BATCHING = new Upgrade("printing_press/batching")
			.withType(UpgradePurpose.SPECIAL);
	//allows printing envelopes
	public static final Upgrade UPGRADE_PRESS_ENVELOPER = new Upgrade("printing_press/enveloper")
			.withType(UpgradePurpose.SPECIAL);

	//allows the radar to detect radio signal emitters and send their positions
	public static final Upgrade UPGRADE_RADIO_LOCATORS = new Upgrade("radio_locators");

	//changes the projectile workshop to *fill* projectiles
	public static final Upgrade UPGRADE_CORE_FILLER = new Upgrade("core_filler")
			.withType(UpgradePurpose.FULL_CONVERSION);

	//adds razor wire on top of a gate
	public static final Upgrade UPGRADE_RAZOR_WIRE = new Upgrade("razor_wire")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);
	//allows connecting redstone wire to a gate
	public static final Upgrade UPGRADE_REDSTONE_ACTIVATION = new Upgrade("rs_activation")
			.withType(UpgradePurpose.SPECIAL);

	//data input machine upgrade
	public static final Upgrade UPGRADE_ADVANCED_DATA = new Upgrade("advanced_data")
			.withType(UpgradePurpose.DATA);
	//arithemtic logic machine upgrade
	public static final Upgrade UPGRADE_MEMORY = new Upgrade("memory")
			.withType(UpgradePurpose.DATA);
	public static final Upgrade UPGRADE_CIRCUIT_RACKS = new Upgrade("circuit_racks")
			.withType(UpgradePurpose.CAPACITY);

	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN =
			new UpgradeEmplacementWeapon<>("machinegun", EmplacementWeaponMachinegun::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER =
			new UpgradeEmplacementWeapon<>("infrared_observer", EmplacementWeaponInfraredObserver::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON =
			new UpgradeEmplacementWeapon<>("autocannon", EmplacementWeaponAutocannon::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER =
			new UpgradeEmplacementWeapon<>("heavy_chemthrower", EmplacementWeaponHeavyChemthrower::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN =
			new UpgradeEmplacementWeapon<>("heavy_railgun", EmplacementWeaponHeavyRailgun::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_SEARCHLIGHT =
			new UpgradeEmplacementWeapon<>("searchlight", EmplacementWeaponSearchlight::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER =
			new UpgradeEmplacementWeapon<>("spotlight_tower", EmplacementWeaponSpotlightTower::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_TESLA =
			new UpgradeEmplacementWeapon<>("tesla", EmplacementWeaponTeslaCoil::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_CPDS =
			new UpgradeEmplacementWeapon<>("cpds", EmplacementWeaponCPDS::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_MORTAR =
			new UpgradeEmplacementWeapon<>("mortar", EmplacementWeaponMortar::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER =
			new UpgradeEmplacementWeapon<>("light_howitzer", EmplacementWeaponLightHowitzer::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_MLRS =
			new UpgradeEmplacementWeapon<>("rocket_launcher", EmplacementWeaponRocketLauncher::new);
	public static final Upgrade UPGRADE_EMPLACEMENT_WEAPON_GUIDED_MISSILE_LAUNCHER =
			new UpgradeEmplacementWeapon<>("guided_missile_launcher", EmplacementWeaponGuidedMissileLauncher::new);

	public static final Upgrade UPGRADE_SOVEREIGNTY = new Upgrade("sovereignty")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);

	public static final Upgrade UPGRADE_EMPLACEMENT_FALLBACK_GRENADES = new Upgrade("emplacement/emergency_smoke")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);
	public static final Upgrade UPGRADE_EMPLACEMENT_STURDY_BEARINGS = new Upgrade("emplacement/sturdy_bearings")
			.withType(UpgradePurpose.EFFICIENCY);

	public static final Upgrade UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL = new Upgrade("emplacement/machinegun/heavy_barrel")
			.withType(UpgradePurpose.SPEED);
	public static final Upgrade UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED = new Upgrade("emplacement/machinegun/watercooled")
			.withType(UpgradePurpose.EFFICIENCY);
	public static final Upgrade UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER = new Upgrade("emplacement/machinegun/additional_fortifications")
			.withType(UpgradePurpose.ARMOR);

	public static final Upgrade UPGRADE_FLAGPOLE_CAPTURE_DEFIANCE = new Upgrade("flagpole/capture_defiance")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);
	public static final Upgrade UPGRADE_FLAGPOLE_TASER_LOCKS = new Upgrade("flagpole/taser_locks")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);
	public static final Upgrade UPGRADE_FLAGPOLE_DISTRESS_SIGNAL = new Upgrade("flagpole/distress_signal")
			.withType(UpgradePurpose.DEFENSE_SYSTEM);
	public static final Upgrade UPGRADE_FLAGPOLE_UNIT_POST = new Upgrade("flagpole/unit_post")
			.withType(UpgradePurpose.FULL_CONVERSION);

	public static final Upgrade UPGRADE_VEHICLE_SMALL_STORAGE = new Upgrade("vehicle/small/storage")
			.withType(UpgradePurpose.CAPACITY);
	public static final Upgrade UPGRADE_VEHICLE_SMALL_ADDITIONAL_TANK = new Upgrade("vehicle/small/fluid_tank")
			.withType(UpgradePurpose.CAPACITY);
	public static final Upgrade UPGRADE_VEHICLE_ADDITIONAL_PASSENGER_SEAT = new Upgrade("vehicle/small/passenger_seat")
			.withType(UpgradePurpose.CAPACITY);
	public static final Upgrade UPGRADE_VEHICLE_WOODGAS = new Upgrade("vehicle/woodgas")
			.withType(UpgradePurpose.SPECIAL);

	//--- Items ---//
	//materials
	public static final ItemIIMaterial itemMaterial = new ItemIIMaterial();
	public static final ItemIIMaterialIngot itemMaterialIngot = new ItemIIMaterialIngot();
	public static final ItemIIMaterialPlate itemMaterialPlate = new ItemIIMaterialPlate();
	public static final ItemIIMaterialRod itemMaterialRod = new ItemIIMaterialRod();
	public static final ItemIIMaterialDust itemMaterialDust = new ItemIIMaterialDust();
	public static final ItemIIMaterialNugget itemMaterialNugget = new ItemIIMaterialNugget();
	public static final ItemIIMaterialWire itemMaterialWire = new ItemIIMaterialWire();
	public static final ItemIIMaterialSpring itemMaterialSpring = new ItemIIMaterialSpring();
	public static final ItemIIMaterialGem itemMaterialGem = new ItemIIMaterialGem();
	public static final ItemIIMaterialBoule itemMaterialBoule = new ItemIIMaterialBoule();
	public static final ItemIIMetalPressMold itemPressMold = new ItemIIMetalPressMold();
	public static final ItemIIVulcanizerMold itemVulcanizerMold = new ItemIIVulcanizerMold();
	public static final ItemIIFunctionalCircuit itemCircuit = new ItemIIFunctionalCircuit();
	public static final ItemIIMotorBelt itemMotorBelt = new ItemIIMotorBelt();
	public static final ItemIIMotorGear itemMotorGear = new ItemIIMotorGear();
	//ammo
	public static final ItemIIAmmoCasing itemAmmoCasing = new ItemIIAmmoCasing();
	public static final ItemIIAmmoArtilleryHeavy itemAmmoHeavyArtillery = new ItemIIAmmoArtilleryHeavy();
	public static final ItemIIAmmoArtilleryMedium itemAmmoMediumArtillery = new ItemIIAmmoArtilleryMedium();
	public static final ItemIIAmmoArtilleryLight itemAmmoLightArtillery = new ItemIIAmmoArtilleryLight();
	public static final ItemIIAmmoMortar itemAmmoMortar = new ItemIIAmmoMortar();
	public static final ItemIIAmmoGuidedMissile itemAmmoGuidedMissile = new ItemIIAmmoGuidedMissile();
	public static final ItemIIAmmoRocketHeavy itemAmmoRocketHeavy = new ItemIIAmmoRocketHeavy();
	public static final ItemIIAmmoRocketLight itemAmmoRocketLight = new ItemIIAmmoRocketLight();
	public static final ItemIIAmmoLightGun itemAmmoLightGun = new ItemIIAmmoLightGun();
	public static final ItemIIAmmoAutocannon itemAmmoAutocannon = new ItemIIAmmoAutocannon();
	public static final ItemIIAmmoRailgunGrenade itemRailgunGrenade = new ItemIIAmmoRailgunGrenade();
	public static final ItemIIAmmoGrenade itemGrenade = new ItemIIAmmoGrenade();
	public static final ItemIIAmmoMachinegun itemAmmoMachinegun = new ItemIIAmmoMachinegun();
	public static final ItemIIAmmoAssaultRifle itemAmmoAssaultRifle = new ItemIIAmmoAssaultRifle();
	public static final ItemIIAmmoSubmachinegun itemAmmoSubmachinegun = new ItemIIAmmoSubmachinegun();
	public static final ItemIIAmmoRevolver itemAmmoRevolver = new ItemIIAmmoRevolver();
	public static final ItemIIBulletMagazine itemBulletMagazine = new ItemIIBulletMagazine();
	public static final ItemIICasingPouch itemCasingPouch = new ItemIICasingPouch();
	//tools
	public static final ItemIISkycrateMount itemSkycrateMount = new ItemIISkycrateMount();
	public static final ItemIILighter itemLighter = new ItemIILighter();
	public static final ItemIIElectricHammer itemHammer = new ItemIIElectricHammer();
	public static final ItemIIElectricWrench itemElectricWrench = new ItemIIElectricWrench();
	public static final ItemIIElectricWirecutter itemWirecutter = new ItemIIElectricWirecutter();
	public static final ItemIIWrench itemWrench = new ItemIIWrench();
	public static final ItemIITrenchShovel itemTrenchShovel = new ItemIITrenchShovel();
	public static final ItemIITripodPeriscope itemTripodPeriscope = new ItemIITripodPeriscope();
	public static final ItemIIMineDetector itemMineDetector = new ItemIIMineDetector();
	public static final ItemIIDrillHead itemDrillhead = new ItemIIDrillHead();
	public static final ItemIITachometer itemTachometer = new ItemIITachometer();
	public static final ItemIIDataWireCoil itemDataWireCoil = new ItemIIDataWireCoil();
	public static final ItemIISmallWireCoil itemSmallWireCoil = new ItemIISmallWireCoil();
	public static final ItemIITripWireCoil itemTripWireCoil = new ItemIITripWireCoil();
	public static final ItemIIMinecart itemMinecart = new ItemIIMinecart();
	public static final ItemIIRadioTuner itemRadioTuner = new ItemIIRadioTuner();
	public static final ItemIIMeasuringCup itemMeasuringCup = new ItemIIMeasuringCup();
	public static final ItemIIPrecisionTool itemPrecisionTool = new ItemIIPrecisionTool();
	public static final ItemIIAssemblyScheme itemAssemblyScheme = new ItemIIAssemblyScheme();
	public static final ItemIISawBlade itemSawblade = new ItemIISawBlade();
	public static final ItemIIBinoculars itemBinoculars = new ItemIIBinoculars();
	public static final ItemIIMachinegun itemMachinegun = new ItemIIMachinegun();
	public static final ItemIISubmachinegun itemSubmachinegun = new ItemIISubmachinegun();
	public static final ItemIIAssaultRifle itemAssaultRifle = new ItemIIAssaultRifle();
	public static final ItemIIRifle itemRifle = new ItemIIRifle();
	public static final ItemIIMortar itemMortar = new ItemIIMortar();
	public static final ItemIIWeaponUpgrade itemWeaponUpgrade = new ItemIIWeaponUpgrade();
	//armor
	public static final ArmorMaterial ARMOR_MATERIAL_LIGHT_ENGINEER = EnumHelper.addArmorMaterial("light_engineer_armor",
			ImmersiveIntelligence.MODID+":light_engineer_armor", 42,
			new int[]{4, 7, 8, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);
	public static final ItemIILightEngineerHelmet itemLightEngineerHelmet = new ItemIILightEngineerHelmet();
	public static final ItemIILightEngineerChestplate itemLightEngineerChestplate = new ItemIILightEngineerChestplate();
	public static final ItemIILightEngineerLeggings itemLightEngineerLeggings = new ItemIILightEngineerLeggings();
	public static final ItemIILightEngineerBoots itemLightEngineerBoots = new ItemIILightEngineerBoots();
	public static final ItemIIArmorUpgrade itemArmorUpgrade = new ItemIIArmorUpgrade();
	//Backpacks
	public static final ItemIIAdvancedPowerPack itemAdvancedPowerPack = new ItemIIAdvancedPowerPack();
	public static final String NBT_AdvancedPowerpack = "II:Powerpack";
	//data
	@IBatchOredictRegister(oreDict = "punchtape")
	public static final ItemIIPunchtape itemPunchtape = new ItemIIPunchtape();
	public static final ItemIIPrintedPage itemPrintedPage = new ItemIIPrintedPage();
	public static final ItemIILogisticTag itemLogisticTag = new ItemIILogisticTag();
	public static final ItemIITracerPowder itemTracerPowder = new ItemIITracerPowder();
	//rubber
	public static final BlockIIRubberLog blockRubberLog = new BlockIIRubberLog();

	//--- Blocks ---//
	public static final BlockIIRubberLeaves blockRubberLeaves = new BlockIIRubberLeaves();
	public static final BlockIIRubberSapling blockRubberSapling = new BlockIIRubberSapling();
	public static final BlockIICharredLog blockCharredLog = new BlockIICharredLog();
	//metal
	@IBatchOredictRegister(oreDict = "ore")
	public static final BlockIIOre blockOre = new BlockIIOre();
	@IBatchOredictRegister(oreDict = "sheetmetal")
	public static final BlockIIMetalBase blockSheetmetal = new BlockIIMetalBase("sheetmetal");
	@IBatchOredictRegister(oreDict = "block")
	public static final BlockIIMetalBase blockMetalStorage = new BlockIIMetalBase("storage");
	@IBatchOredictRegister(oreDict = "slab")
	public static final BlockIISlab<Metals> blockMetalSlabs = new BlockIISlab<>(blockSheetmetal);
	@IBatchOredictRegister(oreDict = "slabSheetmetal")
	public static final BlockIISlab<Metals> blockSheetmetalSlabs = new BlockIISlab<>(blockMetalStorage);
	//regular blocks
	public static final BlockIISandbags blockSandbags = new BlockIISandbags();
	public static final BlockIIClothDecoration blockClothDecoration = new BlockIIClothDecoration();
	public static final BlockIIMetalDecoration blockMetalDecoration = new BlockIIMetalDecoration();
	//b e t o n
	public static final BlockIIConcreteDecoration blockConcreteDecoration = new BlockIIConcreteDecoration();
	public static final BlockIISlab<ConcreteDecorations> blockConcreteSlabs = new BlockIISlab<>(blockConcreteDecoration);
	public static final BlockIIStairs[] blockIIConcreteStairs = BlockIIConcreteDecoration.getStairs();
	//mesh fences
	public static final BlockIIMetalChainFence blockMetalFortification = new BlockIIMetalChainFence();
	public static final BlockIIWoodenChainFence blockWoodenFortification = new BlockIIWoodenChainFence();
	//tank trap
	public static final BlockIIMetalFortification1 blockMetalFortification1 = new BlockIIMetalFortification1();
	//devices
	public static final BlockIIMetalDevice blockMetalDevice = new BlockIIMetalDevice();
	public static final BlockIIMetalDevice1 blockMetalDevice1 = new BlockIIMetalDevice1();
	public static final BlockIIDataDevice blockDataConnector = new BlockIIDataDevice();
	public static final BlockIISmallCrate blockSmallCrate = new BlockIISmallCrate();
	//explosives
	public static final BlockIIAdvancedExplosives blockAdvancedExplosives = new BlockIIAdvancedExplosives();
	//ammunition
	public static final BlockIIMineSign blockMineSign = new BlockIIMineSign();
	public static final BlockIITripmine blockTripmine = new BlockIITripmine();
	public static final BlockIITellermine blockTellermine = new BlockIITellermine();
	public static final BlockIIRadioExplosives blockRadioExplosives = new BlockIIRadioExplosives();
	public static final ItemIINavalMine itemNavalMine = new ItemIINavalMine();
	public static final BlockIITripwireConnector blockTripwireConnector = new BlockIITripwireConnector();
	//rotary devices
	public static final BlockIIMechanicalDevice blockMechanicalDevice = new BlockIIMechanicalDevice();
	public static final BlockIIMechanicalDevice1 blockMechanicalDevice1 = new BlockIIMechanicalDevice1();
	public static final BlockIIGearbox blockGearbox = new BlockIIGearbox();
	public static final BlockIIMechanicalConnector blockMechanicalConnector = new BlockIIMechanicalConnector();
	public static final BlockIIWoodenMultiblock blockWoodenMultiblock = new BlockIIWoodenMultiblock();
	//multiblocks
	public static final BlockIIMetalMultiblock0 blockMetalMultiblock0 = new BlockIIMetalMultiblock0();
	public static final BlockIIMetalMultiblock1 blockMetalMultiblock1 = new BlockIIMetalMultiblock1();
	public static final BlockIIGateMultiblock blockFenceGateMultiblock = new BlockIIGateMultiblock();
	//--- Ammunition System ---//
	//ammo cores
	public static final AmmoCore ammoCoreCopper = new AmmoCoreCopper();
	public static final AmmoCore ammoCoreBrass = new AmmoCoreBrass();
	public static final AmmoCore ammoCoreLead = new AmmoCoreLead();
	public static final AmmoCore ammoCoreIron = new AmmoCoreIron();
	public static final AmmoCore ammoCoreSteel = new AmmoCoreSteel();
	public static final AmmoCore ammoCoreTungsten = new AmmoCoreTungsten();
	public static final AmmoCore ammoCoreUranium = new AmmoCoreUranium();
	public static final AmmoCore ammoCorePabilium = new AmmoCorePabilium();
	public static final AmmoCore ammoCoreRubber = new AmmoCoreRubber();
	//ammo components
	public static final AmmoComponent ammoComponentTNT = new AmmoComponentTNT();
	public static final AmmoComponent ammoComponentRDX = new AmmoComponentRDX();
	public static final AmmoComponent ammoComponentHMX = new AmmoComponentHMX();
	public static final AmmoComponent ammoComponentNuke = new AmmoComponentNuke();
	public static final AmmoComponent ammoComponentWhitePhosphorus = new AmmoComponentWhitePhosphorus();
	public static final AmmoComponent ammoComponentFirework = new AmmoComponentFirework();
	public static final AmmoComponent ammoComponentTracerPowder = new AmmoComponentTracerPowder();
	public static final AmmoComponent ammoComponentFlarePowder = new AmmoComponentFlarePowder();
	public static final AmmoComponent ammoComponentPropaganda = new AmmoComponentPropaganda();
	public static final AmmoComponent ammoComponentTesla = new AmmoComponentTesla();
	public static final AmmoComponent ammoComponentFish = new AmmoComponentFish();
	//ammo propellants
	public static final AmmoPropellant ammoPropellantGunpowder = new AmmoPropellantGunpowder();
	public static final AmmoPropellant ammoPropellantCordite = new AmmoPropellantCordite();
	public static final AmmoPropellant ammoPropellantHMX = new AmmoPropellantHMX();
	public static final AmmoPropellant ammoPropellantRDX = new AmmoPropellantRDX();
	public static final AmmoPropellant ammoPropellantRocketFuel = new AmmoPropellantRocketFuel();
	public static final AmmoPropellant ammoPropellantExperimentalRocketFuel = new AmmoPropellantRocketFuelExperimental();
	public static final AmmoPropellant ammoPropellantStableRocketFuel = new AmmoPropellantRocketFuelStable();
	//Shares code with Immersive Energy, long live II-IEn Cooperation!
	public static List<Predicate<TileEntity>> tileEntitiesWeDontLike = new ArrayList<>();
	//fluid blocks
	public static BlockIIFluid blockFluidInkBlack, blockFluidInkCyan, blockFluidInkMagenta, blockFluidInkYellow;
	public static BlockIIFluid blockFluidEtchingAcid, blockFluidSulfuricAcid, blockFluidNitricAcid, blockFluidHydrofluoricAcid, blockFluidFormicAcid;
	public static BlockIIFluid blockFluidAmmonia, blockFluidMethanol;
	public static BlockIIFluid blockFluidBrine;
	public static BlockIIFluid blockGasHydrogen, blockGasOxygen, blockGasChlorine, blockGasCO2, blockGasCO;
	public static BlockIIFluid blockGasMustardGas, blockGasTearGas, blockGasPhosgeneGas, blockGasHydrogenCyanideGas,blockGasTabunGas,blockGasSarinGas,blockGasSomanGas,blockGasPlagueGas;
	public static BlockIIFluid blockFluidLatex;
	public static BlockIIFluid blockFluidHerbicide;
	//fluids
	public static Fluid fluidInkBlack, fluidInkCyan, fluidInkMagenta, fluidInkYellow;
	public static Fluid fluidEtchingAcid, fluidSulfuricAcid, fluidHydrofluoricAcid, fluidNitricAcid, fluidFormicAcid;
	public static Fluid fluidAmmonia, fluidMethanol;
	public static Fluid fluidBrine;
	public static Fluid gasHydrogen, gasOxygen, gasChlorine, gasCO2, gasCO;
	public static Fluid gasMustardGas, gasTearGas, gasPhosgeneGas,gasHydrogenCyanideGas,gasTabunGas,gasSarinGas,gasSomanGas,gasPlagueGas;
	public static Fluid fluidLatex;
	public static Fluid fluidHerbicide;
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

		IIContent.gasTearGas = makeFluid("tear_gas", -500+100, 240);
		IIContent.gasPhosgeneGas = makeFluid("phosgene_gas", -200+100, 120).setGaseous(true);
		IIContent.gasHydrogenCyanideGas = makeFluid("hydrogen_cyanide_gas", -1500+100, 180).setGaseous(true);
		IIContent.gasTabunGas = makeFluid("tabun_gas", 250, 140);
		IIContent.gasSarinGas = makeFluid("sarin_gas", 320, 80).setGaseous(true);
		IIContent.gasSomanGas = makeFluid("soman_gas", 300, 180).setGaseous(true);

		IIContent.fluidHerbicide = makeFluid("herbicide", 1030, 1002);

		//Should not have color or transparency, should be fully invisible.
		IIContent.gasPlagueGas = makeFluid("plague", -1500+100, 180).setGaseous(true);

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
		//IIContent.blockGasChlorine = new BlockIIFluid("chlorine", IIContent.gasChlorine, Material.WATER)
		//		.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 1));
		IIContent.blockGasCO2 = new BlockIIFluid("carbon_dioxide", IIContent.gasCO2, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
		IIContent.blockGasCO = new BlockIIFluid("carbon_oxide", IIContent.gasCO, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
		//IIContent.blockGasMustardGas = new BlockIIFluid("mustard_gas", IIContent.gasMustardGas, Material.WATER)
		//		.setPotionEffects(new PotionEffect(MobEffects.POISON, 60, 0));


		//22.04.2026 Carver: gasworks.

		IIContent.blockGasChlorine = new BlockIIFluid("chlorine", IIContent.gasChlorine, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.poisonirritant, 60, 0))
				.setPotionEffects(new PotionEffect(IIPotions.suffocator, 60, 1));

		//Mustard gas is more effective than Chlorine due to it being much bigger irritant despite lower lethality.
		//* Hydrofluoric Acid, Salt Dust, Ethanol
		IIContent.blockGasMustardGas = new BlockIIFluid("mustard_gas", IIContent.gasMustardGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.poisonirritant, 60, 2));

		//ethanol+chlorine
		IIContent.blockGasTearGas = new BlockIIFluid("tear_gas", IIContent.gasTearGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.poisonirritant, 40, 0));

		//phosgene is colorless. It should have a delayed effect application.
		//Carbon Monoxide, Chlorine Gas
		IIContent.blockGasPhosgeneGas = new BlockIIFluid("phosgene_gas", IIContent.gasPhosgeneGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.suffocatordelayed1, 60, 0))
				.setPotionEffects(new PotionEffect(IIPotions.poisonirritant, 120, 0));

		//Hydrogen, Carbon Monoxide, Ammonia
		IIContent.blockGasHydrogenCyanideGas = new BlockIIFluid("hydrogen_cyanide_gas", IIContent.gasHydrogenCyanideGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.suffocator, 80, 0))
				.setPotionEffects(new PotionEffect(IIPotions.corrosion, 60, 0))
				.setPotionEffects(new PotionEffect(IIPotions.poisonirritant, 120, 3))
				.setPotionEffects(new PotionEffect(MobEffects.NAUSEA, 120, 0))
				.setPotionEffects(new PotionEffect(MobEffects.MINING_FATIGUE, 480, 2));

		//Phosphorus, Chlorine,Ammonia, Salt Dust
		IIContent.blockGasTabunGas = new BlockIIFluid("tabun_gas", IIContent.gasTabunGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.suffocator, 120, 0))
				.setPotionEffects(new PotionEffect(IIPotions.neuroparalitic, 480, 0))
				.setPotionEffects(new PotionEffect(MobEffects.HUNGER, 360, 4));

		//sarin is twice as deadly than tabun, but is less persistent
		//Hydrofluoric Acid, Ammonia, Phosphorus, Sulfur Dust
		IIContent.blockGasSarinGas = new BlockIIFluid("sarin_gas", IIContent.gasSarinGas, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.NAUSEA, 120, 0))
				.setPotionEffects(new PotionEffect(IIPotions.neuroparalitic, 240, 2));

		//Cheaper version of sarin that is much more toxic by 2-4 times. However much more unstable and technically spreads worse.
		// Ethanol, Phosphorus ,Sulfuric Acid,Salt Dust
		IIContent.blockGasSomanGas = new BlockIIFluid("soman_gas", IIContent.gasSomanGas, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.NAUSEA, 120, 1))
				.setPotionEffects(new PotionEffect(IIPotions.neuroparalitic, 120, 4));

		//23.04.2026 Carver: added herbicide as a start of biological weapons.
		//Kills plants and foliage and farmland. Less effective than tear gas in tactical effeiciency despite its toxicity.
		//copper and nitrate dust, ethanol (alt: ethanol, sulfur, copper).
		IIContent.blockFluidHerbicide = new BlockIIFluid("herbicide", IIContent.fluidHerbicide, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.HUNGER, 80, 4))
				.setPotionEffects(new PotionEffect(MobEffects.NAUSEA, 40, 0));

		IIContent.blockFluidHerbicide = new BlockIIFluid("herbicide", IIContent.fluidHerbicide, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.HUNGER, 80, 4))
				.setPotionEffects(new PotionEffect(MobEffects.NAUSEA, 40, 0));

		//Unobtainable, used as a tool for plague.
		IIContent.blockGasPlagueGas = new BlockIIFluid("plague", IIContent.gasPlagueGas, Material.WATER)
				.setPotionEffects(new PotionEffect(IIPotions.bioweapon5_1, 3456020, 0));
	}

	//dummy method, called so that the static fields above get loaded
	static void init()
	{
		new ItemStack(IIContent.itemLightEngineerHelmet);
	}

	//TODO: 12.09.2025 rework fluids
	public static void refreshFluidReferences()
	{
		IIContent.fluidInkBlack = FluidRegistry.getFluid("ink");
		IIContent.fluidInkCyan = FluidRegistry.getFluid("ink_cyan");
		IIContent.fluidInkMagenta = FluidRegistry.getFluid("ink_magenta");
		IIContent.fluidInkYellow = FluidRegistry.getFluid("ink_yellow");
		IIContent.fluidEtchingAcid = FluidRegistry.getFluid("etching_acid");
		IIContent.fluidSulfuricAcid = FluidRegistry.getFluid("sulfuric_acid");
		IIContent.fluidHydrofluoricAcid = FluidRegistry.getFluid("hydrofluoric_acid");
		IIContent.fluidFormicAcid = FluidRegistry.getFluid("formic_acid");
		IIContent.fluidNitricAcid = FluidRegistry.getFluid("nitric_acid");
		IIContent.fluidBrine = FluidRegistry.getFluid("brine");
		IIContent.gasHydrogen = FluidRegistry.getFluid("hydrogen");
		IIContent.gasOxygen = FluidRegistry.getFluid("oxygen");
		IIContent.gasCO2 = FluidRegistry.getFluid("carbon_dioxide");
		IIContent.gasCO = FluidRegistry.getFluid("carbon_monoxide");
		IIContent.gasChlorine = FluidRegistry.getFluid("chlorine");
		IIContent.fluidAmmonia = FluidRegistry.getFluid("ammonia");
		IIContent.fluidMethanol = FluidRegistry.getFluid("methanol");
		IIContent.fluidLatex = FluidRegistry.getFluid("latex");
		IIContent.gasMustardGas = FluidRegistry.getFluid("mustard_gas");

		IIContent.gasTearGas = FluidRegistry.getFluid("tear_gas");
		IIContent.gasPhosgeneGas = FluidRegistry.getFluid("phosgene_gas");
		IIContent.gasHydrogenCyanideGas = FluidRegistry.getFluid("hydrogen_cyanide_gas");
		IIContent.gasTabunGas = FluidRegistry.getFluid("tabun_gas");
		IIContent.gasSarinGas = FluidRegistry.getFluid("sarin_gas");
		IIContent.gasSomanGas = FluidRegistry.getFluid("soman_gas");

		IIContent.fluidHerbicide = FluidRegistry.getFluid("herbicide");
		//should not obtainable by normal means.
		IIContent.gasPlagueGas = FluidRegistry.getFluid("plague");
	}
}
