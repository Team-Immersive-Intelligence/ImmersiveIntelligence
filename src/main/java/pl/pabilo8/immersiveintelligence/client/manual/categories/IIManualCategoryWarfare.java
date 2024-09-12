package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoComponent;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletComponent;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletCore;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalFortification1.IIBlockTypes_MetalFortification1;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrade;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @author Bastian
 * @author Avalon
 * @since 18-01-2020
 * @since 24-03-2023
 * @since 9-6-2024
 */
public class IIManualCategoryWarfare extends IIManualCategory
{
	public static IIManualCategoryWarfare INSTANCE = new IIManualCategoryWarfare();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_WARFARE;
	}

	@Override
	public void addPages()
	{
		addEntry("warfare_main");
		addEntry("bullet_production")
				.addSource("casing", getSourceForItems(Arrays.stream(IIContent.itemAmmoCasing.getSubItems()).map(IIContent.itemAmmoCasing::getStack).toArray(ItemStack[]::new)))
				.addSource("cores", getSourceForItems(
						IIContent.itemAmmoHeavyArtillery.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoMachinegun.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoAutocannon.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoMortar.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoSubmachinegun.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoRevolver.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoAssaultRifle.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemAmmoLightArtillery.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemNavalMine.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemGrenade.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT),
						IIContent.itemRailgunGrenade.getAmmoCoreStack(IIContent.ammoCoreLead, CoreType.SOFTPOINT)

				));


		ArrayList<ManualPages> bullet_cores = new ArrayList<>();
		for(AmmoCore entry : AmmoRegistry.getAllCores())
			if(!entry.getMaterial().getExampleStack().isEmpty())
				bullet_cores.add(new IIManualPageBulletCore(ManualHelper.getManual(), entry));

		ArrayList<ManualPages> bullet_components = new ArrayList<>();
		for(AmmoComponent entry : AmmoRegistry.getAllComponents())
			if(entry.showInManual()&&!entry.getMaterial().getExampleStack().isEmpty())
				bullet_components.add(new IIManualPageBulletComponent(ManualHelper.getManual(), entry));

		ManualHelper.addEntry("bullet_cores", getCategory(),
				bullet_cores.toArray(new ManualPages[]{})
		);

		ManualHelper.addEntry("bullet_components", getCategory(),
				bullet_components.toArray(new ManualPages[]{})
		);

		addEntry("ammocrate")
				.addSource("crafting", getSourceForItems(IIContent.blockMetalDevice.getStack(IIBlockTypes_MetalDevice.AMMUNITION_CRATE))
				);
		addEntry("casing_pouch")
				.addSource("crafting", getSourceForItems(IIContent.itemCasingPouch.getStack(1))
				);

		addEntry("emplacement");
		addEntry("emplacement_weapons");
		addEntry("chemdispenser")
			.addSource("chemical_dispenser", getSourceForItem(IIContent.blockDataConnector.getStack(IIBlockTypes_Connector.CHEMICAL_DISPENSER)));
		addEntry("flagpole");
		addEntry("explosives_mines")
				.addSource("crafting_radio_equipped_satchel", getSourceForItems(IIContent.itemAmmoCasing.getStack(Casing.RADIO_EXPLOSIVES)))
				.addSource("landmines", getSourceForItem((IIContent.blockTellermine.getStack(BlockIIMine.IIBlockTypes_Mine.MAIN))))
				.addSource("tripmine", getSourceForItem((IIContent.blockTripmine.getStack(BlockIIMine.IIBlockTypes_Mine.MAIN))))
				.addSource("navalmine", getSourceForItem((IIContent.itemNavalMine.getStack(ItemIIAmmoBase.AmmoParts.BULLET))));
		addEntry("grenades");
		addEntry("fortifications")
				.addSource("anti_tank_trap", getSourceForItem(IIContent.blockMetalFortification1.getStack(IIBlockTypes_MetalFortification1.TANK_TRAP)));
		addEntry("light_engineer_armor")
				.addSource("lea", getSourceForItems(new ItemStack(IIContent.itemLightEngineerHelmet), new ItemStack(IIContent.itemLightEngineerChestplate), new ItemStack(IIContent.itemLightEngineerLeggings), new ItemStack(IIContent.itemLightEngineerBoots)))
				.addSource("gas_mask", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.GASMASK)))
				.addSource("ir_headgear", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.INFILTRATOR_GEAR)))
				.addSource("tech_headgear", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.TECHNICIAN_GEAR)))
				.addSource("engineer_headgear", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.ENGINEER_GEAR)))
				.addSource("steel_armor_plates", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.STEEL_ARMOR_PLATES)))
				.addSource("composite_armor_plates", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.COMPOSITE_ARMOR_PLATES)))
				.addSource("hazmat_coating", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.HAZMAT_COATING)))
				.addSource("heat_resistant_coating", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.HEAT_RESISTANT_COATING)))
				.addSource("anti_static_mesh", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.ANTI_STATIC_MESH)))
				.addSource("camo_mesh", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.CAMOUFLAGE_MESH)))
				.addSource("ir_absorbing_mesh", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.INFRARED_ABSORBING_MESH)))
				.addSource("scuba_tank", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.SCUBA_TANK)))
				.addSource("helipack", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.HELIPACK)))
				.addSource("exoskeleton", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.EXOSKELETON)))
				.addSource("boot_reinforcement", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.BOOT_REINFORCEMENT)))
				.addSource("flippers", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.FLIPPERS)))
				.addSource("snow_rackets", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.SNOW_RACKETS)))
				.addSource("internal_springs", getSourceForItems(IIContent.itemArmorUpgrade.getStack(ArmorUpgrades.INTERNAL_SPRINGS)));
		addEntry("machinegun")
				.addSource("machinegun", getSourceForItem(new ItemStack(IIContent.itemMachinegun)));
		addEntry("weapon_upgrades")
				.addSource("heavy_barrel", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.HEAVY_BARREL)))
				.addSource("water_cooling", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.WATER_COOLING)))
				.addSource("belt_fed_loader", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.BELT_FED_LOADER)))
				.addSource("second_magazine", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.SECOND_MAGAZINE)))
				.addSource("hasty_bipod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.HASTY_BIPOD)))
				.addSource("precise_bipod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.PRECISE_BIPOD)))
				.addSource("scope", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.SCOPE)))
				.addSource("ir_scope", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.INFRARED_SCOPE)))
				.addSource("shield", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.SHIELD)))
				.addSource("tripod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.TRIPOD)))
				.addSource("sturdy_barrel", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.STURDY_BARREL)))
				.addSource("suppressor", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.SUPPRESSOR)))
				.addSource("bottom_loader", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.BOTTOM_LOADING)))
				.addSource("folding_stock", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.FOLDING_STOCK)))
				.addSource("rifle_grenade_launcher", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.RIFLE_GRENADE_LAUNCHER)))
				.addSource("stereoscopic_rangefinder", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.STEREOSCOPIC_RANGEFINDER)))
				.addSource("gyroscopic_stabilizer", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.GYROSCOPIC_STABILIZER)))
				.addSource("electric_firing_motor", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.ELECTRIC_FIRING_MOTOR)))
				.addSource("railgun_assisted_chamber", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.RAILGUN_ASSISTED_CHAMBER)))
				.addSource("sami_automatic", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.SEMI_AUTOMATIC)))
				.addSource("long_barrel", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrade.EXTENDED_BARREL)));
		addEntry("mine_detector")
				.addSource("mine_detector", getSourceForItem(new ItemStack(IIContent.itemMineDetector)));
		addEntry("mortar")
				.addSource("mortar", getSourceForItem(new ItemStack(IIContent.itemMortar)));
		addEntry("submachinegun")
				.addSource("submachinegun", getSourceForItem(new ItemStack(IIContent.itemSubmachinegun)));
		addEntry("trench_shovel")
				.addSource("trench_shovel", getSourceForItem(new ItemStack(IIContent.itemTrenchShovel)));
		addEntry("artillery_howitzer");
		addEntry("ballistic_computer");
		addEntry("explosive_mine_sign")
				.addSource("mine_sign", getSourceForItem(new ItemStack(IIContent.blockMineSign)));

	}
}
