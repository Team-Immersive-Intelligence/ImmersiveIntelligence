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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.client.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 27.07.2021
 */
public class TerrafirmaHelper extends IICompatModule
{
	private final ResLoc tfcRoot;
	private final ResLoc lumberRes;
	private final ResLoc logRes;
	private final ResLoc planksRes;

	public TerrafirmaHelper()
	{
		/*
		* IDs:
		* lumber: tfc:wood/lumber/x
		* log: tfc:wood/log/x
		 */

		// TODO: Finish TFC Compat adding new sawmill recipes
		tfcRoot = ResLoc.root(TerraFirmaCraft.MOD_ID);
		lumberRes = ResLoc.of(tfcRoot, "wood/lumber/");
		logRes = ResLoc.of(tfcRoot, "wood/log/");
		planksRes = ResLoc.of(tfcRoot, "wood/planks/");
	}

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
			ResLoc logres = ResLoc.of(logRes, tree.getRegistryName().getResourcePath());
			ResLoc lumber = ResLoc.of(lumberRes, tree.getRegistryName().getResourcePath());
			ResLoc planks = ResLoc.of(planksRes, tree.getRegistryName().getResourcePath());
			IILogger.info("Registering sawmill recipe: " + logres + " -> 8x " + lumber);
			IILogger.info("Registering sawmill recipe: " + planks + " -> 4x " + lumber);

			SawmillRecipe.addRecipe(new ItemStack(new ItemLumberTFC(tree), 8), new IngredientStack(new ItemStack(new ItemBlockTFC(new BlockLogTFC(tree)), 1)), IIContent.itemMaterial.getStack(Materials.DUST_WOOD), Sawmill.torqueMin, 100, 1);
			SawmillRecipe.addRecipe(new ItemStack(new ItemLumberTFC(tree), 4), new IngredientStack(new ItemStack(new ItemBlockTFC(new BlockPlanksTFC(tree)), 1)), IIContent.itemMaterial.getStack(Materials.DUST_WOOD), Sawmill.torqueMin, 100, 1);
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
