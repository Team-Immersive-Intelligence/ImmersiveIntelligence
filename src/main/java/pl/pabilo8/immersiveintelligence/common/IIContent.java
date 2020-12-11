package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.blocks.ItemBlockIEBase;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
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
	public static final List<Block> blocks = new ArrayList<>();
	public static final List<Item> items = new ArrayList<>();
	public static final MachineUpgrade UPGRADE_INSERTER = CommonProxy.createMachineUpgrade("inserter"); //for crates
	public static final MachineUpgrade UPGRADE_FASTER_ENGINE = CommonProxy.createMachineUpgrade("faster_engine"); //increases machine speed
	public static final MachineUpgrade UPGRADE_SAW_UNREGULATOR = CommonProxy.createMachineUpgrade("saw_unregulator"); //more sawdust for cost of planks
	public static ItemIIMaterial item_material = new ItemIIMaterial();
	public static ItemIIMaterialIngot item_material_ingot = new ItemIIMaterialIngot();
	public static ItemIIMaterialPlate item_material_plate = new ItemIIMaterialPlate();
	public static ItemIIMaterialDust item_material_dust = new ItemIIMaterialDust();
	public static ItemIIMaterialNugget item_material_nugget = new ItemIIMaterialNugget();
	public static ItemIIMaterialWire item_material_wire = new ItemIIMaterialWire();
	public static ItemIIMaterialSpring item_material_spring = new ItemIIMaterialSpring();
	public static ItemIIMaterialGem item_material_gem = new ItemIIMaterialGem();
	public static ItemIIMaterialBoule item_material_boule = new ItemIIMaterialBoule();
	public static ItemIIMetalPressMold item_mold = new ItemIIMetalPressMold();
	public static ItemIIFunctionalCircuit item_circuit = new ItemIIFunctionalCircuit();
	public static ItemIIMotorBelt item_motor_belt = new ItemIIMotorBelt();
	public static ItemIIMotorGear item_motor_gear = new ItemIIMotorGear();
	public static ItemIISkycrateMount item_skycrate_mount = new ItemIISkycrateMount();
	public static ItemIILighter item_lighter = new ItemIILighter();
	public static ItemIIElectricHammer item_hammer = new ItemIIElectricHammer();
	public static ItemIITrenchShovel item_trench_shovel = new ItemIITrenchShovel();
	//Shares code with Immersive Energy, long live II-IEn Cooperation!
	public static List<Predicate<TileEntity>> tileEntitiesWeDontLike = new ArrayList<>();
	public static ItemIIElectricWirecutter item_wirecutter = new ItemIIElectricWirecutter();
	public static ItemIIWrench item_wrench = new ItemIIWrench();
	public static ItemIIElectricWrench item_electric_wrench = new ItemIIElectricWrench();
	public static ItemIIDrillHead item_drillhead = new ItemIIDrillHead();
	//Don't know if i should make a seperate item for a torque meter
	public static ItemIITachometer item_tachometer = new ItemIITachometer();
	public static ItemIIDataWireCoil item_data_wire_coil = new ItemIIDataWireCoil();
	public static ItemIISmallWireCoil item_small_wire_coil = new ItemIISmallWireCoil();
	public static ItemIIMinecart item_minecart = new ItemIIMinecart();
	public static ItemIIRadioConfigurator item_radio_configurator = new ItemIIRadioConfigurator();
	public static ItemIIMeasuringCup item_measuring_cup = new ItemIIMeasuringCup();
	public static ItemIIPrecissionTool item_precission_tool = new ItemIIPrecissionTool();
	public static ItemIIAssemblyScheme item_assembly_scheme = new ItemIIAssemblyScheme();
	public static ItemIISawblade item_sawblade = new ItemIISawblade();
	public static ItemIIBinoculars item_binoculars = new ItemIIBinoculars();
	public static ItemIIMachinegun item_machinegun = new ItemIIMachinegun();
	public static ItemIISubmachinegun item_submachinegun = new ItemIISubmachinegun();
	public static ItemIIWeaponUpgrade item_weapon_upgrade = new ItemIIWeaponUpgrade();
	public static ArmorMaterial ARMOR_MATERIAL_LIGHT_ENGINEER = EnumHelper.addArmorMaterial("light_engineer_armor", ImmersiveIntelligence.MODID+":light_engineer_armor", 42, new int[]{4, 7, 8, 4}, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2);
	//public static ItemIIArmorUpgrade item_armor_upgrade = new ItemIIArmorUpgrade();
	public static ItemIILightEngineerHelmet item_light_engineer_helmet = new ItemIILightEngineerHelmet();
	public static ItemIILightEngineerChestplate item_light_engineer_chestplate = new ItemIILightEngineerChestplate();
	public static ItemIILightEngineerLeggings item_light_engineer_leggings = new ItemIILightEngineerLeggings();
	public static ItemIILightEngineerBoots item_light_engineer_boots = new ItemIILightEngineerBoots();
	public static ItemIIAmmoArtillery item_ammo_artillery = new ItemIIAmmoArtillery();
	public static ItemIIAmmoAutocannon item_ammo_autocannon = new ItemIIAmmoAutocannon();
	public static ItemIIAmmoGrenade item_grenade = new ItemIIAmmoGrenade();
	public static ItemIIAmmoRailgunGrenade item_railgun_grenade = new ItemIIAmmoRailgunGrenade();
	public static ItemIIAmmoMachinegun item_ammo_machinegun = new ItemIIAmmoMachinegun();
	public static ItemIIAmmoSubmachinegun item_ammo_submachinegun = new ItemIIAmmoSubmachinegun();
	public static ItemIIAmmoRevolver item_ammo_revolver = new ItemIIAmmoRevolver();
	public static ItemIIAmmoStormRifle item_ammo_storm_rifle = new ItemIIAmmoStormRifle();
	public static ItemIIBulletMagazine item_bullet_magazine = new ItemIIBulletMagazine();
	public static ItemIIPunchtape item_punchtape = new ItemIIPunchtape();
	public static ItemIIPrintedPage item_printed_page = new ItemIIPrintedPage();
	public static ItemIITracerPowder item_tracer_powder = new ItemIITracerPowder();
	public static BlockIIOre block_ore = new BlockIIOre();
	public static BlockIIBase<IIBlockTypes_Metal> block_sheetmetal = (BlockIIBase)new BlockIIBase("sheetmetal", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> block_metal_storage = (BlockIIBase)new BlockIIBase("storage", Material.IRON,
			PropertyEnum.create("type", IIBlockTypes_Metal.class), ItemBlockIEBase.class, false).setOpaque(true)
			.setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> block_metal_slabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("storage_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_Metal> block_sheetmetal_slabs = (BlockIIBase<IIBlockTypes_Metal>)new BlockIISlab("sheetmetal_slab", Material.IRON, PropertyEnum.create("type", IIBlockTypes_Metal.class)).setHardness(3.0F).setResistance(10.0F);
	public static BlockIIBase<IIBlockTypes_StoneDecoration> block_stone_decoration = new BlockIIStoneDecoration();
	public static BlockIIBase<IIBlockTypes_ClothDecoration> block_cloth_decoration = new BlockIIClothDecoration();
	public static BlockIIBase<IIBlockTypes_MetalDecoration> block_metal_decoration = new BlockIIMetalDecoration();
	public static BlockIIBase<IIBlockTypes_ConcreteDecoration> block_concrete_decoration = new BlockIIConcreteDecoration();
	public static BlockIIMetalFortification block_metal_fortification = new BlockIIMetalFortification();
	public static BlockIIMetalFortification1 block_metal_fortification1 = new BlockIIMetalFortification1();
	public static BlockIIWoodenFortification block_wooden_fortification = new BlockIIWoodenFortification();
	public static BlockIIMetalDevice block_metal_device = new BlockIIMetalDevice();
	public static BlockIIDataConnector block_data_connector = new BlockIIDataConnector();
	public static BlockIISmallCrate block_small_crate = new BlockIISmallCrate();
	public static BlockIIMechanicalDevice block_mechanical_device = new BlockIIMechanicalDevice();
	public static BlockIIMechanicalDevice1 block_mechanical_device1 = new BlockIIMechanicalDevice1();
	public static BlockIIGearbox block_gearbox = new BlockIIGearbox();
	public static BlockIIMechanicalConnector block_mechanical_connector = new BlockIIMechanicalConnector();
	public static BlockIIWoodenMultiblock block_wooden_multiblock = new BlockIIWoodenMultiblock();
	public static BlockIIMetalMultiblock0 block_metal_multiblock0 = new BlockIIMetalMultiblock0();
	public static BlockIIMetalMultiblock1 block_metal_multiblock1 = new BlockIIMetalMultiblock1();
	public static BlockIIFluid block_fluid_ink_black;
	public static BlockIIFluid block_fluid_ink_cyan;
	public static BlockIIFluid block_fluid_ink_magenta;
	public static BlockIIFluid block_fluid_ink_yellow;
	public static BlockIIFluid block_fluid_etching_acid;
	public static BlockIIFluid block_fluid_sulfuric_acid;
	public static BlockIIFluid block_fluid_nitric_acid;
	public static BlockIIFluid block_fluid_hydrofluoric_acid;
	public static BlockIIFluid block_fluid_ammonia;
	public static BlockIIFluid block_fluid_methanol;
	public static BlockIIFluid block_fluid_brine;
	public static BlockIIFluid block_gas_hydrogen;
	public static BlockIIFluid block_gas_oxygen;
	public static BlockIIFluid block_gas_chlorine;
	public static Fluid fluid_ink_black;
	public static Fluid fluid_ink_cyan;
	public static Fluid fluid_ink_magenta;
	public static Fluid fluid_ink_yellow;
	public static Fluid fluid_etching_acid;
	public static Fluid fluid_sulfuric_acid;
	public static Fluid fluid_hydrofluoric_acid;
	public static Fluid fluid_nitric_acid;
	public static Fluid fluid_brine;
	public static Fluid gas_hydrogen;
	public static Fluid gas_oxygen;
	public static Fluid gas_chlorine;
	public static Fluid fluid_ammonia;
	public static Fluid fluid_methanol;

	static
	{
		IIContent.fluid_ink_black = makeFluid("ink", 2000, 2250);
		IIContent.fluid_ink_cyan = makeFluid("ink_cyan", 2000, 2250);
		IIContent.fluid_ink_magenta = makeFluid("ink_magenta", 2000, 2250);
		IIContent.fluid_ink_yellow = makeFluid("ink_yellow", 2000, 2250);
		IIContent.fluid_etching_acid = makeFluid("etching_acid", 1500, 1500);
		IIContent.fluid_sulfuric_acid = makeFluid("sulfuric_acid", 1500, 1500);
		IIContent.fluid_hydrofluoric_acid = makeFluid("hydrofluoric_acid", 1500, 1500);
		IIContent.fluid_nitric_acid = makeFluid("nitric_acid", 1500, 1500, "rdx_fluids/");
		IIContent.fluid_brine = makeFluid("brine", 1000, 1500);
		IIContent.gas_hydrogen = makeFluid("hydrogen", 0, 2250).setGaseous(true);
		IIContent.gas_oxygen = makeFluid("oxygen", 0, 2250).setGaseous(true);
		IIContent.gas_chlorine = makeFluid("chlorine", 0, 2250).setGaseous(true);
		IIContent.fluid_ammonia = makeFluid("ammonia", 1500, 1000, "rdx_fluids/");
		IIContent.fluid_methanol = makeFluid("methanol", 1500, 1000, "rdx_fluids/");

		IIContent.block_fluid_ink_black = new BlockIIFluid("ink", IIContent.fluid_ink_black, Material.WATER);
		IIContent.block_fluid_ink_cyan = new BlockIIFluid("ink_cyan", IIContent.fluid_ink_cyan, Material.WATER);
		IIContent.block_fluid_ink_magenta = new BlockIIFluid("ink_magenta", IIContent.fluid_ink_magenta, Material.WATER);
		IIContent.block_fluid_ink_yellow = new BlockIIFluid("ink_yellow", IIContent.fluid_ink_yellow, Material.WATER);
		IIContent.block_fluid_etching_acid = new BlockIIFluid("etching_acid", IIContent.fluid_etching_acid, Material.WATER);
		IIContent.block_fluid_sulfuric_acid = new BlockIIFluid("sulfuric_acid", IIContent.fluid_sulfuric_acid, Material.WATER);
		IIContent.block_fluid_nitric_acid = new BlockIIFluid("nitric_acid", IIContent.fluid_nitric_acid, Material.WATER);
		IIContent.block_fluid_hydrofluoric_acid = new BlockIIFluid("hydrofluoric_acid", IIContent.fluid_hydrofluoric_acid, Material.WATER);
		IIContent.block_fluid_brine = new BlockIIFluid("brine", IIContent.fluid_brine, Material.WATER);

		IIContent.block_fluid_ammonia = new BlockIIFluid("ammonia", IIContent.fluid_ammonia, Material.WATER);
		IIContent.block_fluid_methanol = new BlockIIFluid("methanol", IIContent.fluid_methanol, Material.WATER);

		IIContent.block_gas_hydrogen = new BlockIIFluid("hydrogen", IIContent.gas_hydrogen, Material.WATER);
		IIContent.block_gas_oxygen = new BlockIIFluid("oxygen", IIContent.gas_oxygen, Material.WATER);
		IIContent.block_gas_chlorine = new BlockIIFluid("chlorine", IIContent.gas_chlorine, Material.WATER);
	}
}
