package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.Fluid;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIBase;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIFluid;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIIOre;
import pl.pabilo8.immersiveintelligence.common.blocks.BlockIISlab;
import pl.pabilo8.immersiveintelligence.common.blocks.cloth.BlockIIClothDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.concrete.BlockIIConcreteDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.BlockIIMetalFortification;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.BlockIIMetalFortification1;
import pl.pabilo8.immersiveintelligence.common.blocks.fortification.BlockIIWoodenFortification;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.BlockIIDataConnector;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.BlockIIMetalDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.BlockIIMetalDevice;
import pl.pabilo8.immersiveintelligence.common.blocks.metal.BlockIISmallCrate;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.BlockIIMetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.BlockIIMetalMultiblock1;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.wooden.BlockIIWoodenMultiblock;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.BlockIIGearbox;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.BlockIIMechanicalConnector;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.BlockIIMechanicalDevice;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.BlockIIMechanicalDevice1;
import pl.pabilo8.immersiveintelligence.common.blocks.stone.BlockIIStoneDecoration;
import pl.pabilo8.immersiveintelligence.common.blocks.types.*;
import pl.pabilo8.immersiveintelligence.common.blocks.wooden.BlockIIMineSign;
import pl.pabilo8.immersiveintelligence.common.items.*;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.*;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerBoots;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerChestplate;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerHelmet;
import pl.pabilo8.immersiveintelligence.common.items.armor.ItemIILightEngineerLeggings;
import pl.pabilo8.immersiveintelligence.common.items.material.*;
import pl.pabilo8.immersiveintelligence.common.items.mechanical.ItemIIMotorBelt;
import pl.pabilo8.immersiveintelligence.common.items.mechanical.ItemIIMotorGear;
import pl.pabilo8.immersiveintelligence.common.items.tools.*;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIMachinegun;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIISubmachinegun;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static pl.pabilo8.immersiveintelligence.common.CommonProxy.makeFluid;

/**
 * @author Pabilo8
 * @since 08.12.2020
 */
public class IIContent
{
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();

	//Shares code with Immersive Energy, long live II-IEn Cooperation!
	public static List<Predicate<TileEntity>> tileEntitiesWeDontLike = new ArrayList<>();

	public static final MachineUpgrade UPGRADE_INSERTER = CommonProxy.createMachineUpgrade("inserter"); //for crates
	public static final MachineUpgrade UPGRADE_FASTER_ENGINE = CommonProxy.createMachineUpgrade("faster_engine"); //increases machine speed
	public static final MachineUpgrade UPGRADE_SAW_UNREGULATOR = CommonProxy.createMachineUpgrade("saw_unregulator"); //more sawdust for cost of planks

	public static ItemIIMaterial itemMaterial = new ItemIIMaterial();
	public static ItemIIMaterialIngot itemMaterialIngot = new ItemIIMaterialIngot();
	public static ItemIIMaterialPlate itemMaterialPlate = new ItemIIMaterialPlate();
	public static ItemIIMaterialDust itemMaterialDust = new ItemIIMaterialDust();
	public static ItemIIMaterialNugget itemMaterialNugget = new ItemIIMaterialNugget();
	public static ItemIIMaterialWire itemMaterialWire = new ItemIIMaterialWire();
	public static ItemIIMaterialSpring itemMaterialSpring = new ItemIIMaterialSpring();
	public static ItemIIMaterialGem itemMaterialGem = new ItemIIMaterialGem();
	public static ItemIIMaterialBoule itemMaterialBoule = new ItemIIMaterialBoule();
	public static ItemIIMetalPressMold itemPressMold = new ItemIIMetalPressMold();
	public static ItemIIFunctionalCircuit itemCircuit = new ItemIIFunctionalCircuit();
	public static ItemIIMotorBelt itemMotorBelt = new ItemIIMotorBelt();
	public static ItemIIMotorGear itemMotorGear = new ItemIIMotorGear();
	public static ItemIISkycrateMount itemSkycrateMount = new ItemIISkycrateMount();
	public static ItemIILighter itemLighter = new ItemIILighter();
	public static ItemIIElectricHammer itemHammer = new ItemIIElectricHammer();
	public static ItemIITrenchShovel itemTrenchShovel = new ItemIITrenchShovel();
	public static ItemIITripodPeriscope itemTripodPeriscope = new ItemIITripodPeriscope();
	public static ItemIIMineDetector itemMineDetector = new ItemIIMineDetector();

	public static ItemIIElectricWirecutter itemWirecutter = new ItemIIElectricWirecutter();
	public static ItemIIWrench itemWrench = new ItemIIWrench();
	public static ItemIIElectricWrench itemElectricWrench = new ItemIIElectricWrench();
	public static ItemIIDrillHead itemDrillhead = new ItemIIDrillHead();
	//Don't know if i should make a seperate item for a torque meter
	public static ItemIITachometer itemTachometer = new ItemIITachometer();
	public static ItemIIDataWireCoil itemDataWireCoil = new ItemIIDataWireCoil();
	public static ItemIISmallWireCoil itemSmallWireCoil = new ItemIISmallWireCoil();
	public static ItemIIMinecart itemMinecart = new ItemIIMinecart();
	public static ItemIIRadioConfigurator itemRadioConfigurator = new ItemIIRadioConfigurator();
	public static ItemIIMeasuringCup itemMeasuringCup = new ItemIIMeasuringCup();
	public static ItemIIPrecissionTool itemPrecissionTool = new ItemIIPrecissionTool();
	public static ItemIIAssemblyScheme itemAssemblyScheme = new ItemIIAssemblyScheme();
	public static ItemIISawblade itemSawblade = new ItemIISawblade();
	public static ItemIIBinoculars itemBinoculars = new ItemIIBinoculars();
	public static ItemIIMachinegun itemMachinegun = new ItemIIMachinegun();
	public static ItemIISubmachinegun itemSubmachinegun = new ItemIISubmachinegun();
	public static ItemIIWeaponUpgrade itemWeaponUpgrade = new ItemIIWeaponUpgrade();

	//public static ItemIIArmorUpgrade item_armor_upgrade = new ItemIIArmorUpgrade();
	public static ArmorMaterial ARMOR_MATERIAL_LIGHT_ENGINEER = EnumHelper.addArmorMaterial("light_engineer_armor", ImmersiveIntelligence.MODID+":light_engineer_armor", 42, new int[]{4, 7, 8, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);
	public static ItemIILightEngineerHelmet itemLightEngineerHelmet = new ItemIILightEngineerHelmet();
	public static ItemIILightEngineerChestplate itemLightEngineerChestplate = new ItemIILightEngineerChestplate();
	public static ItemIILightEngineerLeggings itemLightEngineerLeggings = new ItemIILightEngineerLeggings();
	public static ItemIILightEngineerBoots itemLightEngineerBoots = new ItemIILightEngineerBoots();

	public static ItemIIAmmoArtillery itemAmmoArtillery = new ItemIIAmmoArtillery();
	public static ItemIIAmmoAutocannon itemAmmoAutocannon = new ItemIIAmmoAutocannon();
	public static ItemIIAmmoGrenade itemGrenade = new ItemIIAmmoGrenade();
	public static ItemIIAmmoRailgunGrenade itemRailgunGrenade = new ItemIIAmmoRailgunGrenade();
	public static ItemIIAmmoMachinegun itemAmmoMachinegun = new ItemIIAmmoMachinegun();
	public static ItemIIAmmoSubmachinegun itemAmmoSubmachinegun = new ItemIIAmmoSubmachinegun();
	public static ItemIIAmmoRevolver itemAmmoRevolver = new ItemIIAmmoRevolver();
	public static ItemIIAmmoStormRifle itemAmmoStormRifle = new ItemIIAmmoStormRifle();

	public static ItemIIBulletMagazine itemBulletMagazine = new ItemIIBulletMagazine();

	public static ItemIIPunchtape itemPunchtape = new ItemIIPunchtape();
	public static ItemIIPrintedPage itemPrintedPage = new ItemIIPrintedPage();
	public static ItemIITracerPowder itemTracerPowder = new ItemIITracerPowder();

	public static BlockIIOre blockOre = new BlockIIOre();
	public static BlockIIBase<IIBlockTypes_Metal> blockSheetmetal = (BlockIIBase)new BlockIIBase("sheetmetal", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> blockMetalStorage = (BlockIIBase)new BlockIIBase("storage", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> blockMetalSlabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("storage_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> blockSheetmetalSlabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("sheetmetal_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_StoneDecoration> blockStoneDecoration = new BlockIIStoneDecoration();
	public static BlockIIBase<IIBlockTypes_ClothDecoration> blockClothDecoration = new BlockIIClothDecoration();
	public static BlockIIBase<IIBlockTypes_MetalDecoration> blockMetalDecoration = new BlockIIMetalDecoration();
	public static BlockIIBase<IIBlockTypes_ConcreteDecoration> blockConcreteDecoration = new BlockIIConcreteDecoration();
	public static BlockIIMetalFortification blockMetalFortification = new BlockIIMetalFortification();
	public static BlockIIMetalFortification1 blockMetalFortification1 = new BlockIIMetalFortification1();
	public static BlockIIWoodenFortification blockWoodenFortification = new BlockIIWoodenFortification();
	public static BlockIIMetalDevice blockMetalDevice = new BlockIIMetalDevice();
	public static BlockIIDataConnector blockDataConnector = new BlockIIDataConnector();
	public static BlockIISmallCrate blockSmallCrate = new BlockIISmallCrate();
	public static BlockIIMineSign blockMineSign = new BlockIIMineSign();

	public static BlockIIMechanicalDevice blockMechanicalDevice = new BlockIIMechanicalDevice();
	public static BlockIIMechanicalDevice1 blockMechanicalDevice1 = new BlockIIMechanicalDevice1();
	public static BlockIIGearbox blockGearbox = new BlockIIGearbox();
	public static BlockIIMechanicalConnector blockMechanicalConnector = new BlockIIMechanicalConnector();
	public static BlockIIWoodenMultiblock blockWoodenMultiblock = new BlockIIWoodenMultiblock();

	public static BlockIIMetalMultiblock0 blockMetalMultiblock0 = new BlockIIMetalMultiblock0();
	public static BlockIIMetalMultiblock1 blockMetalMultiblock1 = new BlockIIMetalMultiblock1();

	public static BlockIIFluid blockFluidInkBlack;
	public static BlockIIFluid blockFluidInkCyan;
	public static BlockIIFluid blockFluidInkMagenta;
	public static BlockIIFluid blockFluidInkYellow;
	public static BlockIIFluid blockFluidEtchingAcid;
	public static BlockIIFluid blockFluidSulfuricAcid;
	public static BlockIIFluid blockFluidNitricAcid;
	public static BlockIIFluid blockFluidHydrofluoricAcid;
	public static BlockIIFluid blockFluidAmmonia;
	public static BlockIIFluid blockFluidMethanol;
	public static BlockIIFluid blockFluidBrine;
	public static BlockIIFluid blockGasHydrogen;
	public static BlockIIFluid blockGasOxygen;
	public static BlockIIFluid blockGasChlorine;

	public static Fluid fluidInkBlack;
	public static Fluid fluidInkCyan;
	public static Fluid fluidInkMagenta;
	public static Fluid fluidInkYellow;
	public static Fluid fluidEtchingAcid;
	public static Fluid fluidSulfuricAcid;
	public static Fluid fluidHydrofluoricAcid;
	public static Fluid fluidNitricAcid;
	public static Fluid fluidBrine;
	public static Fluid gasHydrogen;
	public static Fluid gasOxygen;
	public static Fluid gasChlorine;
	public static Fluid fluidAmmonia;
	public static Fluid fluidMethanol;

	static
	{
		IIContent.fluidInkBlack = makeFluid("ink", 2000, 2250);
		IIContent.fluidInkCyan = makeFluid("ink_cyan", 2000, 2250);
		IIContent.fluidInkMagenta = makeFluid("ink_magenta", 2000, 2250);
		IIContent.fluidInkYellow = makeFluid("ink_yellow", 2000, 2250);
		IIContent.fluidEtchingAcid = makeFluid("etching_acid", 1500, 1500);
		IIContent.fluidSulfuricAcid = makeFluid("sulfuric_acid", 1500, 1500);
		IIContent.fluidHydrofluoricAcid = makeFluid("hydrofluoric_acid", 1500, 1500);
		IIContent.fluidNitricAcid = makeFluid("nitric_acid", 1500, 1500, "rdx_fluids/");
		IIContent.fluidBrine = makeFluid("brine", 1000, 1500);
		IIContent.gasHydrogen = makeFluid("hydrogen", 0, 2250).setGaseous(true);
		IIContent.gasOxygen = makeFluid("oxygen", 0, 2250).setGaseous(true);
		IIContent.gasChlorine = makeFluid("chlorine", 0, 2250).setGaseous(true);
		IIContent.fluidAmmonia = makeFluid("ammonia", 1500, 1000, "rdx_fluids/");
		IIContent.fluidMethanol = makeFluid("methanol", 1500, 1000, "rdx_fluids/");

		IIContent.blockFluidInkBlack = new BlockIIFluid("ink", IIContent.fluidInkBlack, Material.WATER);
		IIContent.blockFluidInkCyan = new BlockIIFluid("ink_cyan", IIContent.fluidInkCyan, Material.WATER);
		IIContent.blockFluidInkMagenta = new BlockIIFluid("ink_magenta", IIContent.fluidInkMagenta, Material.WATER);
		IIContent.blockFluidInkYellow = new BlockIIFluid("ink_yellow", IIContent.fluidInkYellow, Material.WATER);
		IIContent.blockFluidEtchingAcid = new BlockIIFluid("etching_acid", IIContent.fluidEtchingAcid, Material.WATER);
		IIContent.blockFluidSulfuricAcid = new BlockIIFluid("sulfuric_acid", IIContent.fluidSulfuricAcid, Material.WATER);
		IIContent.blockFluidNitricAcid = new BlockIIFluid("nitric_acid", IIContent.fluidNitricAcid, Material.WATER);
		IIContent.blockFluidHydrofluoricAcid = new BlockIIFluid("hydrofluoric_acid", IIContent.fluidHydrofluoricAcid, Material.WATER);
		IIContent.blockFluidBrine = new BlockIIFluid("brine", IIContent.fluidBrine, Material.WATER);

		IIContent.blockFluidAmmonia = new BlockIIFluid("ammonia", IIContent.fluidAmmonia, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
		IIContent.blockFluidMethanol = new BlockIIFluid("methanol", IIContent.fluidMethanol, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 40, 0));

		IIContent.blockGasHydrogen = new BlockIIFluid("hydrogen", IIContent.gasHydrogen, Material.WATER);
		IIContent.blockGasOxygen = new BlockIIFluid("oxygen", IIContent.gasOxygen, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.WATER_BREATHING, 40, 0));
		IIContent.blockGasChlorine = new BlockIIFluid("chlorine", IIContent.gasChlorine, Material.WATER)
				.setPotionEffects(new PotionEffect(MobEffects.BLINDNESS, 60, 0));
	}
}
