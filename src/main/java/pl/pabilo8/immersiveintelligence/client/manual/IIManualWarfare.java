package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.Crafting;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCore;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletComponent;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageBulletCore;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariables;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariablesCallback;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockEmplacement;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.MultiblockProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalFortification;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalFortification1;
import pl.pabilo8.immersiveintelligence.common.items.weapons.ItemIIWeaponUpgrade;

import java.util.ArrayList;
import java.util.Map.Entry;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualWarfare extends IIManual
{
	public static IIManualWarfare INSTANCE = new IIManualWarfare();

	@Override
	public String getCategory()
	{
		return ClientProxy.CAT_WARFARE;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("warfare_main", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "warfare_main0")
		);

		ArrayList<ManualPages> bullet_cores = new ArrayList<>();
		for(Entry<String, IBulletCore> entry : BulletRegistry.INSTANCE.registeredBulletCores.entrySet())
		{
			if(!entry.getValue().getMaterial().getExampleStack().isEmpty())
				bullet_cores.add(new IIManualPageBulletCore(ManualHelper.getManual(), entry.getValue()));
		}

		ArrayList<ManualPages> bullet_components = new ArrayList<>();
		for(Entry<String, IBulletComponent> entry : BulletRegistry.INSTANCE.registeredComponents.entrySet())
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

		ManualHelper.addEntry("bullet_production", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "bullet_production0"),
				new ManualPages.Text(ManualHelper.getManual(), "bullet_production1"),
				new ManualPages.Text(ManualHelper.getManual(), "bullet_production2"),
				new ManualPages.Text(ManualHelper.getManual(), "bullet_production3")
		);
		ManualHelper.addEntry("ammunition_workshop", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "ammunition_workshop0", MultiblockAmmunitionWorkshop.instance),
				new ManualPages.Text(ManualHelper.getManual(), "ammunition_workshop1")
		);
		ManualHelper.addEntry("projectile_workshop", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "projectile_workshop0", MultiblockProjectileWorkshop.instance),
				new ManualPages.Text(ManualHelper.getManual(), "projectile_workshop1"),
				new ManualPages.Text(ManualHelper.getManual(), "projectile_workshop2")
		);


		ArrayList<ManualPages> mg_pages = new ArrayList<>();
		mg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "machinegun0", new ItemStack(IIContent.itemMachinegun)));
		mg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "machinegun1"));
		for(int i = 0; i < IIContent.itemWeaponUpgrade.getSubNames().length; i += 1)
			if(ItemIIWeaponUpgrade.WeaponUpgrades.values()[i].isValidFor("MACHINEGUN"))
				mg_pages.add(new Crafting(ManualHelper.getManual(), "machinegun_upgrade_"+IIContent.itemWeaponUpgrade.getSubNames()[i], new ItemStack(IIContent.itemWeaponUpgrade, 1, i)));

		ManualHelper.addEntry("machinegun", getCategory(), mg_pages.toArray(new ManualPages[]{}));

		ArrayList<ManualPages> smg_pages = new ArrayList<>();
		smg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "submachinegun0", new ItemStack(IIContent.itemSubmachinegun)));
		smg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "submachinegun1"));
		for(int i = 0; i < IIContent.itemWeaponUpgrade.getSubNames().length; i += 1)
			if(ItemIIWeaponUpgrade.WeaponUpgrades.values()[i].isValidFor("SUBMACHINEGUN"))
				smg_pages.add(new Crafting(ManualHelper.getManual(), "submachinegun_upgrade_"+IIContent.itemWeaponUpgrade.getSubNames()[i], new ItemStack(IIContent.itemWeaponUpgrade, 1, i)));
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
						Utils.getStackWithMetaName(IIContent.itemAmmoCasing, "radio_explosives")),

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
		for(int i = 0; i < IIContent.itemArmorUpgrade.getSubNames().length; i += 1)
			if(!IIContent.itemArmorUpgrade.isMetaHidden(i))
				armor_pages.add(new Crafting(ManualHelper.getManual(), "light_engineer_armor_upgrade_"+IIContent.itemArmorUpgrade.getSubNames()[i], new ItemStack(IIContent.itemArmorUpgrade, 1, i)));
		ManualHelper.addEntry("light_engineer_armor", getCategory(), armor_pages.toArray(new ManualPages[]{}));

		ManualHelper.addEntry("emplacement", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "emplacement0", MultiblockEmplacement.instance),
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
				new ManualPageMultiblock(ManualHelper.getManual(), "artillery_howitzer1", MultiblockArtilleryHowitzer.instance),
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
				new ManualPageMultiblock(ManualHelper.getManual(), "ballistic_computer0", MultiblockBallisticComputer.instance),
				new IIManualPageDataVariables(ManualHelper.getManual(), "ballistic_computer", true)
						.addEntry(new DataTypeInteger(), 'x', 'y', 'z')
						.addEntry(new DataTypeInteger(), 'm'),
				new IIManualPageDataVariables(ManualHelper.getManual(), "ballistic_computer", false)
						.addEntry(new DataTypeInteger(), 'y')
						.addEntry(new DataTypeInteger(), 'p')
		);

		ManualHelper.addEntry("flagpole", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "flagpole0", MultiblockFlagpole.instance),
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
	}
}
