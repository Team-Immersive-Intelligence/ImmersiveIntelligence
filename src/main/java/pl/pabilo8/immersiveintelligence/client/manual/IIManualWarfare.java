package pl.pabilo8.immersiveintelligence.client.manual;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualPages.Crafting;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.client.ClientProxy;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockAmmunitionFactory;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first.MultiblockBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalDevice;

import java.util.ArrayList;

/**
 * Created by Pabilo8 on 18-01-2020.
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

		ArrayList<ManualPages> mg_pages = new ArrayList<>();
		mg_pages.add(new ManualPages.Crafting(ManualHelper.getManual(), "machinegun0", new ItemStack(CommonProxy.item_machinegun)));
		mg_pages.add(new ManualPages.Text(ManualHelper.getManual(), "machinegun1"));
		for(int i = 0; i < CommonProxy.item_machinegun_upgrade.getSubNames().length; i += 1)
			mg_pages.add(new Crafting(ManualHelper.getManual(), "machinegun_upgrade_"+CommonProxy.item_machinegun_upgrade.getSubNames()[i], new ItemStack(CommonProxy.item_machinegun_upgrade, 1, i)));

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
				new ManualPages.Text(ManualHelper.getManual(), "artillery_howitzer3")
		);

		ManualHelper.addEntry("ballistic_computer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "ballistic_computer0", MultiblockBallisticComputer.instance),
				new ManualPages.Text(ManualHelper.getManual(), "ballistic_computer1")
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
				new ManualPages.Text(ManualHelper.getManual(), "chemdispenser2")
		);

	}
}
