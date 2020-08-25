package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.Crafting;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletComponent;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCoreType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPages.BulletComponentDisplay;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPages.BulletCoreDisplay;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPages.DataVariablesDisplay;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

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
		for(Entry<String, IBulletCoreType> entry : BulletRegistry.INSTANCE.registeredBulletCores.entrySet())
		{
			if(!entry.getValue().getMaterial().getExampleStack().isEmpty())
				bullet_cores.add(new BulletCoreDisplay(ManualHelper.getManual(), entry.getValue()));
		}

		ArrayList<ManualPages> bullet_components = new ArrayList<>();
		for(Entry<String, IBulletComponent> entry : BulletRegistry.INSTANCE.registeredComponents.entrySet())
		{
			if(!entry.getValue().getMaterial().getExampleStack().isEmpty())
				bullet_components.add(new BulletComponentDisplay(ManualHelper.getManual(), entry.getValue()));
		}

		ManualHelper.addEntry("bullet_cores", getCategory(),
				bullet_cores.toArray(new ManualPages[]{})
		);

		ManualHelper.addEntry("bullet_components", getCategory(),
				bullet_components.toArray(new ManualPages[]{})
		);

		ArrayList<ManualPages> mg_pages = new ArrayList<>();
		mg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "machinegun0", new ItemStack(CommonProxy.item_machinegun)));
		mg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "machinegun1"));
		for(int i = 0; i < CommonProxy.item_weapon_upgrade.getSubNames().length; i += 1)
			mg_pages.add(new Crafting(ManualHelper.getManual(), "machinegun_upgrade_"+CommonProxy.item_weapon_upgrade.getSubNames()[i], new ItemStack(CommonProxy.item_weapon_upgrade, 1, i)));

		ManualPages[] mg_array = mg_pages.toArray(new ManualPages[]{});

		ManualHelper.addEntry("machinegun", getCategory(),
				mg_array
		);


		ManualHelper.addEntry("grenade", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "grenade0")
		);

		ManualHelper.addEntry("artillery_howitzer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "artillery_howitzer0", MultiblockArtilleryHowitzer.instance),
				new ManualPages.Text(ManualHelper.getManual(), "artillery_howitzer1"),
				new ManualPages.Text(ManualHelper.getManual(), "artillery_howitzer2"),
				new DataVariablesDisplay(ManualHelper.getManual(), "artillery_howitzer", true)
						.addEntry(new DataPacketTypeString(), 'c')
						.addEntry(new DataPacketTypeInteger(), 'f')
						.addEntry(new DataPacketTypeInteger(), 'y'),
				new DataVariablesDisplay(ManualHelper.getManual(), "artillery_howitzer2", true)
						.addEntry(new DataPacketTypeInteger(), 'p')
		);

		ManualHelper.addEntry("ballistic_computer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "ballistic_computer0", MultiblockBallisticComputer.instance),
				new DataVariablesDisplay(ManualHelper.getManual(), "ballistic_computer", true)
						.addEntry(new DataPacketTypeInteger(), 'x', 'y', 'z')
						.addEntry(new DataPacketTypeInteger(), 'm'),
				new DataVariablesDisplay(ManualHelper.getManual(), "ballistic_computer", false)
						.addEntry(new DataPacketTypeInteger(), 'y')
						.addEntry(new DataPacketTypeInteger(), 'p')
		);

		ManualHelper.addEntry("ammunition_factory", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "ammunition_factory0", MultiblockAmmunitionFactory.instance),
				new ManualPages.Text(ManualHelper.getManual(), "ammunition_factory1"),
				new ManualPages.Text(ManualHelper.getManual(), "ammunition_factory2"),
				new ManualPages.Text(ManualHelper.getManual(), "ammunition_factory3"),
				new ManualPages.Text(ManualHelper.getManual(), "ammunition_factory4")
		);

		ManualHelper.addEntry("ammocrate", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "ammocrate0", new ItemStack(CommonProxy.block_metal_device, 1, IIBlockTypes_MetalDevice.AMMUNITION_CRATE.getMeta()))
		);

		ManualHelper.addEntry("chemdispenser", getCategory(),
				new ManualPages.Crafting(ManualHelper.getManual(), "chemdispenser0", new ItemStack(CommonProxy.block_data_connector, 1, IIBlockTypes_Connector.CHEMICAL_DISPENSER.getMeta())),
				new ManualPages.Text(ManualHelper.getManual(), "chemdispenser1"),
				new DataVariablesDisplay(ManualHelper.getManual(), "chemdispenser", true)
						.addEntry(new DataPacketTypeInteger(), 'a')
						.addEntry(new DataPacketTypeBoolean(), 'i')
						.addEntry(new DataPacketTypeInteger(), 'y')
						.addEntry(new DataPacketTypeInteger(), 'p')
		);

	}
}
