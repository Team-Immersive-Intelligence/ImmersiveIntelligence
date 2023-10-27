package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletComponent;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletCore;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalFortification1.IIBlockTypes_MetalFortification1;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.BlockIIMetalDevice.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIMine;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.immersiveintelligence.common.item.armor.ItemIIArmorUpgrade.ArmorUpgrades;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIWeaponUpgrade.WeaponUpgrades;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @author Bastian
 * @since 18-01-2020
 * @since 24-03-2023
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
						IIContent.itemAmmoArtillery.getBulletCore("core_lead", "piercing"),
						IIContent.itemAmmoMachinegun.getBulletCore("core_lead", "softpoint"),
						IIContent.itemAmmoAutocannon.getBulletCore("core_lead", "softpoint"),
						IIContent.itemAmmoMortar.getBulletCore("core_lead", "piercing"),
						IIContent.itemAmmoSubmachinegun.getBulletCore("core_lead", "softpoint"),
						IIContent.itemAmmoRevolver.getBulletCore("core_lead", "softpoint"),
						IIContent.itemAmmoAssaultRifle.getBulletCore("core_lead", "softpoint"),
						IIContent.itemAmmoLightArtillery.getBulletCore("core_lead", "piercing"),
						IIContent.itemNavalMine.getBulletCore("core_lead", "softpoint"),
						IIContent.itemGrenade.getBulletCore("core_lead", "softpoint"),
						IIContent.itemRailgunGrenade.getBulletCore("core_lead", "softpoint")

				));


		ArrayList<ManualPages> bullet_cores = new ArrayList<>();
		for(Entry<String, IAmmoCore> entry : AmmoRegistry.INSTANCE.registeredBulletCores.entrySet())
		{
			if(!entry.getValue().getMaterial().getExampleStack().isEmpty())
				bullet_cores.add(new IIManualPageBulletCore(ManualHelper.getManual(), entry.getValue()));
		}

		ArrayList<ManualPages> bullet_components = new ArrayList<>();
		for(Entry<String, IAmmoComponent> entry : AmmoRegistry.INSTANCE.registeredComponents.entrySet())
		{
			if(entry.getValue().showInManual()&&!entry.getValue().getMaterial().getExampleStack().isEmpty())
				bullet_components.add(new IIManualPageBulletComponent(ManualHelper.getManual(), entry.getValue()));
		}

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
		addEntry("chemdispenser");
		addEntry("flagpole");
		addEntry("explosives_mines")
				.addSource("crafting_radio_equipped_satchel", getSourceForItems(
						IIContent.itemAmmoCasing.getStack(Casings.RADIO_EXPLOSIVES)))
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
				.addSource("heavy_barrle", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.HEAVY_BARREL)))
				.addSource("water_cooling", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.WATER_COOLING)))
				.addSource("belt_fed_loader", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.BELT_FED_LOADER)))
				.addSource("second_magazine", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.SECOND_MAGAZINE)))
				.addSource("hasty_bipod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.HASTY_BIPOD)))
				.addSource("precise_bipod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.PRECISE_BIPOD)))
				.addSource("scope", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.SCOPE)))
				.addSource("ir_scope", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.INFRARED_SCOPE)))
				.addSource("shield", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.SHIELD)))
				.addSource("tripod", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.TRIPOD)))
				.addSource("sturdy_barrel", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.STURDY_BARREL)))
				.addSource("suppressor", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.SUPPRESSOR)))
				.addSource("bottom_loader", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.BOTTOM_LOADING)))
				.addSource("folding_stock", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.FOLDING_STOCK)))
				.addSource("rifle_grenade_launcher", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.RIFLE_GRENADE_LAUNCHER)))
				.addSource("stereoscopic_rangefinder", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.STEREOSCOPIC_RANGEFINDER)))
				.addSource("gyroscopic_stabilizer", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.GYROSCOPIC_STABILIZER)))
				.addSource("electric_firing_motor", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.ELECTRIC_FIRING_MOTOR)))
				.addSource("railgun_assisted_chamber", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.RAILGUN_ASSISTED_CHAMBER)))
				.addSource("sami_automatic", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.SEMI_AUTOMATIC)))
				.addSource("long_barrel", getSourceForItem(IIContent.itemWeaponUpgrade.getStack(WeaponUpgrades.EXTENDED_BARREL)));
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











        /*
        ManualHelper.addEntry("bullet_production", getCategory(),
                new ManualPages.Text(ManualHelper.getManual(), "bullet_production0"),
                new ManualPages.Text(ManualHelper.getManual(), "bullet_production1"),
                new ManualPages.Text(ManualHelper.getManual(), "bullet_production2"),
                new ManualPages.Text(ManualHelper.getManual(), "bullet_production3")
        );
        ManualHelper.addEntry("ammunition_workshop", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "ammunition_workshop0", MultiblockAmmunitionWorkshop.INSTANCE),
                new ManualPages.Text(ManualHelper.getManual(), "ammunition_workshop1")
        );
        ManualHelper.addEntry("projectile_workshop", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "projectile_workshop0", MultiblockProjectileWorkshop.INSTANCE),
                new ManualPages.Text(ManualHelper.getManual(), "projectile_workshop1"),
                new ManualPages.Text(ManualHelper.getManual(), "projectile_workshop2")
        );


        ArrayList<ManualPages> mg_pages = new ArrayList<>();
        mg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "machinegun0", new ItemStack(IIContent.itemMachinegun)));
        mg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "machinegun1"));

        Arrays.stream(WeaponUpgrades.values())
                .filter(u -> !u.isHidden())
                .filter(u -> u.toolset.contains(WeaponTypes.MACHINEGUN))
                .forEachOrdered(
                        u -> mg_pages.add(new Crafting(ManualHelper.getManual(),
                                "machinegun_upgrade_" + u.getName(),
                                IIContent.itemWeaponUpgrade.getStack(u)))
                );

        ManualHelper.addEntry("machinegun", getCategory(), mg_pages.toArray(new ManualPages[]{}));

        ArrayList<ManualPages> smg_pages = new ArrayList<>();
        smg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "submachinegun0", new ItemStack(IIContent.itemSubmachinegun)));
        smg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "submachinegun1"));
        Arrays.stream(WeaponUpgrades.values())
                .filter(u -> !u.isHidden())
                .filter(u -> u.toolset.contains(WeaponTypes.SUBMACHINEGUN))
                .forEachOrdered(
                        u -> mg_pages.add(new Crafting(ManualHelper.getManual(),
                                "submachinegun_upgrade_" + u.getName(),
                                IIContent.itemWeaponUpgrade.getStack(u)))
                );
        ManualHelper.addEntry("submachinegun", getCategory(), smg_pages.toArray(new ManualPages[]{}));

        ManualHelper.addEntry("grenades", getCategory(),
                new ManualPages.Text(ManualHelper.getManual(), "grenades0"),
                new ManualPages.Text(ManualHelper.getManual(), "grenades1")
        );

        ManualHelper.addEntry("fortifications", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "fortifications0", new ItemStack(IIContent.blockSandbags)),
                new ManualPages.Crafting(ManualHelper.getManual(), "fortifications1", new ItemStack(IIContent.blockMetalFortification1, 1, IIBlockTypes_MetalFortification1.TANK_TRAP.getMeta()))
        );

        ManualHelper.addEntry("explosives_mines", getCategory(),
                new ManualPages.Text(ManualHelper.getManual(), "explosives_mines0"),
                new ManualPages.ItemDisplay(ManualHelper.getManual(), "explosives_mines_landmine", new ItemStack(IIContent.blockTellermine)),
                new ManualPages.ItemDisplay(ManualHelper.getManual(), "explosives_mines_tripmine", new ItemStack(IIContent.blockTripmine)),
                new ManualPages.ItemDisplay(ManualHelper.getManual(), "explosives_mines_naval_mine", new ItemStack(IIContent.itemNavalMine)),
                new ManualPages.Text(ManualHelper.getManual(), "explosives_mines_naval_mine1"),

                new ManualPages.ItemDisplay(ManualHelper.getManual(), "explosives_mines_satchel0", new ItemStack(IIContent.blockRadioExplosives)),
                new ManualPages.Text(ManualHelper.getManual(), "explosives_mines_satchel1"),
                new ManualPages.Text(ManualHelper.getManual(), "explosives_mines_satchel2"),
                new ManualPages.Crafting(ManualHelper.getManual(), "explosives_mines_satchel3",
                        IIContent.itemAmmoCasing.getStack(Casings.RADIO_EXPLOSIVES)),

                new ManualPages.Crafting(ManualHelper.getManual(), "explosives_mines_sign", new ItemStack(IIContent.blockMineSign))
        );

        ManualHelper.addEntry("mine_detector", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "mine_detector", new ItemStack(IIContent.itemMineDetector)),
                new ManualPages.Text(ManualHelper.getManual(), "mine_detector_demining0"),
                new ManualPages.Text(ManualHelper.getManual(), "mine_detector_demining1")
        );

        ManualHelper.addEntry("mortar", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "mortar0", new ItemStack(IIContent.itemMortar)),
                new ManualPages.Text(ManualHelper.getManual(), "mortar1")
        );

        ArrayList<ManualPages> armor_pages = new ArrayList<>();
        armor_pages.add(new ManualPages.CraftingMulti(ManualHelper.getManual(), "light_engineer_armor0",
                new ItemStack(IIContent.itemLightEngineerHelmet),
                new ItemStack(IIContent.itemLightEngineerChestplate),
                new ItemStack(IIContent.itemLightEngineerLeggings),
                new ItemStack(IIContent.itemLightEngineerBoots)
        ));
        armor_pages.add(new ManualPages.Text(ManualHelper.getManual(), "light_engineer_armor1"));
        Arrays.stream(ArmorUpgrades.values())
                .filter(u -> !u.isHidden())
                .forEachOrdered(
                        u -> armor_pages.add(new Crafting(ManualHelper.getManual(),
                                "light_engineer_armor_upgrade_" + u.getName(),
                                IIContent.itemArmorUpgrade.getStack(u)))
                );
        ManualHelper.addEntry("light_engineer_armor", getCategory(), armor_pages.toArray(new ManualPages[]{}));

        ManualHelper.addEntry("emplacement", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "emplacement0", MultiblockEmplacement.INSTANCE),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement1"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement2")

        );

        ManualHelper.addEntry("emplacement_weapons", getCategory(),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_mg0", "immersiveintelligence:textures/misc/rotary.png;0;64;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_mg1"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_ir_observer0", "immersiveintelligence:textures/misc/rotary.png;64;64;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_ir_observer1"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_ir_observer2"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_flak0", "immersiveintelligence:textures/misc/rotary.png;128;64;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_flak1"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_heavy_chem0", "immersiveintelligence:textures/misc/rotary.png;192;64;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_heavy_chem1"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_heavy_railgun0", "immersiveintelligence:textures/misc/rotary.png;0;128;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_heavy_railgun1"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_tesla0", "immersiveintelligence:textures/misc/rotary.png;64;128;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_tesla1"),
                new ManualPages.Image(ManualHelper.getManual(), "emplacement_cpds0", "immersiveintelligence:textures/misc/rotary.png;128;128;64;64"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_cpds1"),
                new ManualPages.Text(ManualHelper.getManual(), "emplacement_cpds2")

        );

        ManualHelper.addEntry("artillery_howitzer", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "artillery_howitzer1", MultiblockArtilleryHowitzer.INSTANCE),
                new ManualPages.Text(ManualHelper.getManual(), "artillery_howitzer2"),
                new IIManualPageDataVariables(ManualHelper.getManual(), "artillery_howitzer", true)
                        .addEntry(new DataTypeString(), 'c')
                        .addEntry(new DataTypeInteger(), 'f')
                        .addEntry(new DataTypeInteger(), 'y'),
                new IIManualPageDataVariables(ManualHelper.getManual(), "artillery_howitzer2", true)
                        .addEntry(new DataTypeInteger(), 'p'),
                new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "artillery_howitzer1")
                        .addEntry(new DataTypeInteger(), "get_energy")
                        .addEntry(new DataTypeString(), "get_state")
                        .addEntry(new DataTypeInteger(), "get_state_num")
                        .addEntry(new DataTypeInteger(), "get_state_progress"),
                new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "artillery_howitzer2")
                        .addEntry(new DataTypeInteger(), "get_yaw")
                        .addEntry(new DataTypeInteger(), "get_pitch")
                        .addEntry(new DataTypeInteger(), "get_planned_yaw")
                        .addEntry(new DataTypeInteger(), "get_planned_pitch")
                        .addEntry(new DataTypeInteger(), "get_platform_height"),

                new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "artillery_howitzer3")
                        .addEntry(new DataTypeBoolean(), "get_door_opened")
                        .addEntry(new DataTypeBoolean(), "get_door_closed")
                        .addEntry(new DataTypeBoolean(), "get_door_opening"),
                new IIManualPageDataVariablesCallback(ManualHelper.getManual(), "artillery_howitzer4")
                        .addEntry(new DataTypeItemStack(), "get_loaded_shell")
                        .addEntry(new DataTypeItemStack(), "get_stored_shell")
        );

        ManualHelper.addEntry("ballistic_computer", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "ballistic_computer0", MultiblockBallisticComputer.INSTANCE),
                new IIManualPageDataVariables(ManualHelper.getManual(), "ballistic_computer", true)
                        .addEntry(new DataTypeInteger(), 'x', 'y', 'z')
                        .addEntry(new DataTypeInteger(), 'm'),
                new IIManualPageDataVariables(ManualHelper.getManual(), "ballistic_computer", false)
                        .addEntry(new DataTypeInteger(), 'y')
                        .addEntry(new DataTypeInteger(), 'p')
        );

        ManualHelper.addEntry("flagpole", getCategory(),
                new ManualPageMultiblock(ManualHelper.getManual(), "flagpole0", MultiblockFlagpole.INSTANCE),
                new ManualPages.Text(ManualHelper.getManual(), "flagpole1")
        );

        ManualHelper.addEntry("ammocrate", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "ammocrate0", new ItemStack(IIContent.blockMetalDevice, 1, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta()))
        );

        ManualHelper.addEntry("chemdispenser", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "chemdispenser0", new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.CHEMICAL_DISPENSER.getMeta())),
                new ManualPages.Text(ManualHelper.getManual(), "chemdispenser1"),
                new IIManualPageDataVariables(ManualHelper.getManual(), "chemdispenser", true)
                        .addEntry(new DataTypeInteger(), 'a')
                        .addEntry(new DataTypeBoolean(), 'i')
                        .addEntry(new DataTypeInteger(), 'y')
                        .addEntry(new DataTypeInteger(), 'p')
        );

        ManualHelper.addEntry("trench_shovel", getCategory(),
                new ManualPages.Crafting(ManualHelper.getManual(), "trench_shovel0", new ItemStack(IIContent.itemTrenchShovel))
        );
         */
	}
}
