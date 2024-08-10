package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import buildcraft.lib.fluid.FluidManager;
import com.google.common.collect.Multimap;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.objects.blocks.BlocksTFC;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import net.dries007.tfc.objects.blocks.wood.BlockPlanksTFC;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import net.dries007.tfc.objects.items.wood.ItemLumberTFC;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class TerrafirmaHelper extends IICompatModule
{
	@Override
	public void preInit()
	{
	}

	@Override
	public void registerRecipes()
	{
		IILogger.info("Registering TFC sawmill recipes");
		for (Tree tree : TFCRegistries.TREES.getValuesCollection())
		{
			ItemLumberTFC lumberItem = ItemLumberTFC.get(tree);
			ItemBlockTFC logItem = new ItemBlockTFC(BlockLogTFC.get(tree));
			ItemBlockTFC plankItem = new ItemBlockTFC(BlockPlanksTFC.get(tree));

			IILogger.info("Registering sawmill recipe: " + logItem.getBlock().getRegistryName() + " -> 8x " + lumberItem.getRegistryName());
			IILogger.info("Registering sawmill recipe: " + plankItem.getBlock().getRegistryName() + " -> 4x " + lumberItem.getRegistryName());

			SawmillRecipe.addRecipe(new ItemStack(lumberItem, 8), new IngredientStack(new ItemStack(logItem)), IIContent.itemMaterial.getStack(Materials.DUST_WOOD), Sawmill.torqueMin, 100, 1);
			SawmillRecipe.addRecipe(new ItemStack(lumberItem, 4), new IngredientStack(new ItemStack(plankItem)), IIContent.itemMaterial.getStack(Materials.DUST_WOOD), Sawmill.torqueMin, 100, 1);
		}
	}

	@Override
	public String getName()
	{
		return "Terrafirma";
	}

	@Override
	public void init()
	{

	}

	@Override
	public void postInit()
	{

	}
}
