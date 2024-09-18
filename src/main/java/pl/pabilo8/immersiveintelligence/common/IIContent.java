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
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
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
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial;
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

	//adds razor wire on top of a gate
	public static final MachineUpgrade UPGRADE_RAZOR_WIRE = CommonProxy.createMachineUpgrade("razor_wire");
	//allows connecting redstone wire to a gate
	public static final MachineUpgrade UPGRADE_REDSTONE_ACTIVATION = CommonProxy.createMachineUpgrade("rs_activation");

	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_MACHINEGUN = EmplacementWeapon.register(EmplacementWeaponMachinegun::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_IROBSERVER = EmplacementWeapon.register(EmplacementWeaponInfraredObserver::new);

	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_AUTOCANNON = EmplacementWeapon.register(EmplacementWeaponAutocannon::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_CHEMTHROWER = EmplacementWeapon.register(EmplacementWeaponHeavyChemthrower::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_HEAVY_RAILGUN = EmplacementWeapon.register(EmplacementWeaponHeavyRailgun::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_SPOTLIGHT_TOWER = EmplacementWeapon.register(EmplacementWeaponSpotlightTower::new);
	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_TESLA = EmplacementWeapon.register(EmplacementWeaponTeslaCoil::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_MORTAR = EmplacementWeapon.register(EmplacementWeaponMortar::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_LIGHT_HOWITZER = EmplacementWeapon.register(EmplacementWeaponLightHowitzer::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_LIGHT_ROCKET_LAUNCHER = EmplacementWeapon.register(EmplacementWeaponLightRocketLauncher::new);
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_GUIDED_MISSILE_LAUNCHER = EmplacementWeapon.register(EmplacementWeaponGuidedMissileLauncher::new);


	public static final MachineUpgrade UPGRADE_EMPLACEMENT_WEAPON_CPDS = EmplacementWeapon.register(EmplacementWeaponCPDS::new);

	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_FALLBACK_GRENADES = CommonProxy.createMachineUpgrade("emplacement_grenades");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_STURDY_BEARINGS = CommonProxy.createMachineUpgrade("emplacement_bearings");

	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_HEAVYBARREL = CommonProxy.createMachineUpgrade("mg_heavy_barrel");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_WATERCOOLED = CommonProxy.createMachineUpgrade("mg_watercooled");
	//public static final MachineUpgrade UPGRADE_EMPLACEMENT_MACHINEGUN_BUNKER = CommonProxy.createMachineUpgrade("mg_bunker");

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
	//Don't know if i should make a seperate item for a torque meter
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
	public static final ItemIITracerPowder itemTracerPowder = new ItemIITracerPowder();

	//--- Blocks ---//

	//rubber
	public static final BlockIIRubberLog blockRubberLog = new BlockIIRubberLog();
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
	public static final BlockIIHMXDynamite blockHMXDynamite = new BlockIIHMXDynamite();

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
		IIContent.blockGasMustardGas = new BlockIIFluid("mustard_gas", IIContent.gasMustardGas, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.POISON, 60, 0));
	}

	//dummy method, called so that the static fields above get loaded
	static void init()
	{
		new ItemStack(IIContent.itemLightEngineerHelmet);
	}
}
