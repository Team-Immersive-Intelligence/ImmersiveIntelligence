package pl.pabilo8.immersiveintelligence.client.manual.categories;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.lib.manual.ManualPages;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualCategory;
import pl.pabilo8.immersiveintelligence.client.manual.pages.IIManualPageDataVariables;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIWoodenChainFence.WoodenFortifications;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.*;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockPacker;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCratePost;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIMinecart.Minecarts;
import pl.pabilo8.immersiveintelligence.common.item.ItemIISkycrateMount.SkycrateMounts;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.ArrayList;

/**
 * @author Pabilo8
 * @since 18-01-2020
 */
public class IIManualCategoryLogistics extends IIManualCategory
{
	public static IIManualCategoryLogistics INSTANCE = new IIManualCategoryLogistics();

	@Override
	public String getCategory()
	{
		return IIReference.CAT_LOGISTICS;
	}

	@Override
	public void addPages()
	{
		ManualHelper.addEntry("logistics", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "logistics0")
		);

		ItemStack inserter0, inserter1, inserter2;
		inserter0 = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.INSERTER.getMeta());
		inserter1 = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.ADVANCED_INSERTER.getMeta());
		inserter2 = new ItemStack(IIContent.blockDataConnector, 1, IIBlockTypes_Connector.FLUID_INSERTER.getMeta());

		ManualHelper.addEntry("inserters", getCategory(),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "inserters0", inserter0, inserter1, inserter2),
				new ManualPages.Crafting(ManualHelper.getManual(), "inserters_basic", inserter0),
				new IIManualPageDataVariables(ManualHelper.getManual(), "inserters_basic", true)
						.addEntry(new DataTypeString(), 'c')
						.addEntry(new DataTypeString(), 'a')
						.addEntry(new DataTypeItemStack(), 's'),
				new IIManualPageDataVariables(ManualHelper.getManual(), "inserters_basic", true)
						.addEntry(new DataTypeInteger(), 'e')
						.addEntry(new DataTypeInteger(), 't'),
				new IIManualPageDataVariables(ManualHelper.getManual(), "inserters_basic", true)
						.addEntry(new DataTypeString(), 'i')
						.addEntry(new DataTypeString(), 'o')
						.addEntry(new DataTypeInteger(), '1')
						.addEntry(new DataTypeInteger(), '0'),
				new IIManualPageDataVariables(ManualHelper.getManual(), "inserter_obsolete", true)
						.addEntry(new DataTypeString(), 'm')
						.addEntry(new DataTypeInteger(), 'c'),
				new ManualPages.Crafting(ManualHelper.getManual(), "inserters_advanced", inserter1),
				new ManualPages.Crafting(ManualHelper.getManual(), "inserters_fluid", inserter2),
				new IIManualPageDataVariables(ManualHelper.getManual(), "inserters_fluid", true)
						.addEntry(new DataTypeString(), 'm')
						.addEntry(new DataTypeInteger(), 'c')
		);
		ManualHelper.addEntry("task_system", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "task_system0"),
				new ManualPages.Text(ManualHelper.getManual(), "task_system1")
		);

		ManualHelper.addEntry("packer", getCategory(),
				new ManualPageMultiblock(ManualHelper.getManual(), "packer0", MultiblockPacker.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "packer1"),
				new ManualPages.Text(ManualHelper.getManual(), "packer2"),
				new ManualPages.Text(ManualHelper.getManual(), "packer3"),
				new ManualPages.Text(ManualHelper.getManual(), "packer4"),
				new IIManualPageDataVariables(ManualHelper.getManual(), "packer", true)
						.addEntry(new DataTypeString(), 'c')
						.addEntry(new DataTypeInteger(), 'a')
						.addEntry(new DataTypeInteger(), 's')
						.addEntry(new DataTypeBoolean(), 'm'),
				new IIManualPageDataVariables(ManualHelper.getManual(), "packer", true)
						.addEntry(new DataTypeBoolean(), 'e')
		);

		ManualHelper.addEntry("skycrates", getCategory(),
				new ManualPages.Text(ManualHelper.getManual(), "skycrates0"),
				new ManualPageMultiblock(ManualHelper.getManual(), "skycrates1", MultiblockSkyCrateStation.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "skycrates2"),
				new ManualPageMultiblock(ManualHelper.getManual(), "skycrates3", MultiblockSkyCartStation.INSTANCE),
				new ManualPages.Text(ManualHelper.getManual(), "skycrates4"),
				new ManualPageMultiblock(ManualHelper.getManual(), "skycrates5", MultiblockSkyCratePost.INSTANCE),
				new ManualPages.ItemDisplay(ManualHelper.getManual(), "skycrates6",
						IIContent.itemMinecart.getStack(Minecarts.WOODEN_CRATE),
						IIContent.itemMinecart.getStack(Minecarts.REINFORCED_CRATE),
						IIContent.itemMinecart.getStack(Minecarts.STEEL_CRATE),
						IIContent.itemMinecart.getStack(Minecarts.WOODEN_BARREL),
						IIContent.itemMinecart.getStack(Minecarts.METAL_BARREL)
				),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "skycrates7",
						IIContent.itemSkycrateMount.getStack(SkycrateMounts.MECHANICAL),
						IIContent.itemSkycrateMount.getStack(SkycrateMounts.ELECTRIC))
		);

		ArrayList<ItemStack> fences = new ArrayList<>();
		for(WoodenFortifications v : WoodenFortifications.values())
			fences.add(new ItemStack(IIContent.blockWoodenFortification, 1, v.getMeta()));
		for(MetalFortifications v : MetalFortifications.values())
			fences.add(new ItemStack(IIContent.blockMetalFortification, 1, v.getMeta()));


		ManualHelper.addEntry("chain_fences", getCategory(),
				new ManualPages.CraftingMulti(ManualHelper.getManual(), "chain_fences0", fences.toArray()),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences1", MultiblockWoodenFenceGate.INSTANCE),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences2", MultiblockWoodenChainFenceGate.INSTANCE),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences3", MultiblockSteelFenceGate.INSTANCE),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences4", MultiblockSteelChainFenceGate.INSTANCE),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences5", MultiblockAluminiumFenceGate.INSTANCE),
				new ManualPageMultiblock(ManualHelper.getManual(), "chain_fences6", MultiblockAluminiumChainFenceGate.INSTANCE)
		);

	}
}
