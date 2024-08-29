package pl.pabilo8.immersiveintelligence.common.compat;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.Utils;
import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidRegistry;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Sawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.item.crafting.ItemIIMaterial.Materials;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		//Fresh Water -> Hydrogen + Oxygen
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("fresh_water", 750),
				FluidRegistry.getFluidStack("oxygen", 250),
				FluidRegistry.getFluidStack("hydrogen", 500),
				160, 80);
		//Salt Water -> Hydrogen + Chlorine (less efficient)
		ElectrolyzerRecipe.addRecipe(FluidRegistry.getFluidStack("salt_water", 750),
				FluidRegistry.getFluidStack("chlorine", 375),
				FluidRegistry.getFluidStack("hydrogen", 375),
				200, 180);

		IILogger.info("Registering TFC sawmill recipes");
		CraftingManager.REGISTRY.forEach(TerrafirmaHelper::woodRecipe);
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

	private static void woodRecipe(IRecipe recipe)
	{
		ItemStack out = recipe.getRecipeOutput().copy();
		if(!Utils.compareToOreName(out, "lumber"))
			return;
		out.setCount((int)(out.getCount()*1.5));

		List<ItemStack> logStacks = new ArrayList<>();
		for(Ingredient ingredient : recipe.getIngredients())
			Arrays.stream(ingredient.getMatchingStacks())
					.filter(stack -> Utils.compareToOreName(stack, "logWood"))
					.forEachOrdered(logStacks::add);
		if(logStacks.isEmpty())
			return;

		SawmillRecipe.addRecipe(out, new IngredientStack(logStacks).setUseNBT(false),
				IIContent.itemMaterial.getStack(Materials.DUST_WOOD),
				Sawmill.torqueMin+2, 400, 1);
	}
}
