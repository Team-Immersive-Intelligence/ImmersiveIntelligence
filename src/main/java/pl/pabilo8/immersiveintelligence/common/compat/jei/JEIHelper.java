/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.client.gui.GuiChemicalBath;
import pl.pabilo8.immersiveintelligence.client.gui.GuiElectrolyzer;
import pl.pabilo8.immersiveintelligence.client.gui.GuiPrecissionAssembler;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.compat.jei.bathing.BathingRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.bathing.BathingRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.electrolyzer.ElectrolyzerRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.electrolyzer.ElectrolyzerRecipeWrapper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.precission_assembler.PrecissionAssemblerRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.precission_assembler.PrecissionAssemblerRecipeWrapper;

import java.util.ArrayList;

@JEIPlugin
public class JEIHelper implements IModPlugin
{
	public static IJeiHelpers jeiHelpers;
	public static ArrayList<IIRecipeCategory> recipeCategories = new ArrayList<>();

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{
		//For registering item subtypes in jei

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{

	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
		recipeCategories.add(new BathingRecipeCategory(guiHelper));
		recipeCategories.add(new ElectrolyzerRecipeCategory(guiHelper));
		recipeCategories.add(new PrecissionAssemblerRecipeCategory(guiHelper));

		registry.addRecipeCategories(recipeCategories.toArray(new IIRecipeCategory[]{}));
	}

	@Override
	public void register(IModRegistry registryIn)
	{
		//Blacklist

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_printed_page, 1, 1));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_printed_page, 1, 2));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_printed_page, 1, 3));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_assembly_scheme, 1, OreDictionary.WILDCARD_VALUE));

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_bullet, 1, OreDictionary.WILDCARD_VALUE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_bullet_magazine, 1, OreDictionary.WILDCARD_VALUE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_wooden_multiblock, 1, OreDictionary.WILDCARD_VALUE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_metal_multiblock0, 1, OreDictionary.WILDCARD_VALUE));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_metal_multiblock1, 1, OreDictionary.WILDCARD_VALUE));

		ImmersiveIntelligence.logger.info("JEI has just requested our recipes, it seems that we even have a class for registering them!");

		registryIn.handleRecipes(BathingRecipe.class, BathingRecipeWrapper::new, "ii.bathing");
		registryIn.addRecipes(BathingRecipe.recipeList, "ii.bathing");
		registryIn.addRecipeClickArea(GuiChemicalBath.class, 16, 58, 19, 12, "ii.bathing");
		registryIn.addRecipeClickArea(GuiChemicalBath.class, 131, 57, 11, 13, "ii.bathing");

		registryIn.handleRecipes(ElectrolyzerRecipe.class, ElectrolyzerRecipeWrapper::new, "ii.electrolyzer");
		registryIn.addRecipes(ElectrolyzerRecipe.recipeList, "ii.electrolyzer");
		registryIn.addRecipeClickArea(GuiElectrolyzer.class, 16, 58, 19, 12, "ii.electrolyzer");

		registryIn.handleRecipes(PrecissionAssemblerRecipe.class, PrecissionAssemblerRecipeWrapper::new, "ii.precissionassembler");
		registryIn.addRecipes(PrecissionAssemblerRecipe.recipeList, "ii.precissionassembler");
		registryIn.addRecipeClickArea(GuiPrecissionAssembler.class, 16, 58, 19, 12, "ii.precissionassembler");

		for(IIRecipeCategory cat : recipeCategories)
			cat.addCatalysts(registryIn);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{
		ImmersiveIntelligence.logger.info("otak!");
	}
}