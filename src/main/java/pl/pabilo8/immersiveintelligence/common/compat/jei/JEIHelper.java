/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.client.gui.GuiFluidSorter;
import blusunrize.immersiveengineering.client.gui.GuiIEContainerBase;
import blusunrize.immersiveengineering.common.util.compat.jei.FluidSorterGhostHandler;
import blusunrize.immersiveengineering.common.util.compat.jei.IEFluidTooltipCallback;
import blusunrize.immersiveengineering.common.util.compat.jei.IEGhostItemHandler;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecissionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.SawmillRecipe;
import pl.pabilo8.immersiveintelligence.client.gui.GuiChemicalBath;
import pl.pabilo8.immersiveintelligence.client.gui.GuiElectrolyzer;
import pl.pabilo8.immersiveintelligence.client.gui.GuiPrecissionAssembler;
import pl.pabilo8.immersiveintelligence.client.gui.GuiSawmill;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.compat.jei.bathing.BathingRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.electrolyzer.ElectrolyzerRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.precission_assembler.PrecissionAssemblerRecipeCategory;
import pl.pabilo8.immersiveintelligence.common.compat.jei.sawmill.SawmillRecipeCategory;

import java.util.LinkedHashMap;
import java.util.Map;

@JEIPlugin
public class JEIHelper implements IModPlugin
{
	public static IJeiHelpers jeiHelpers;
	public static IModRegistry modRegistry;
	public static IDrawable slotDrawable;
	public static ITooltipCallback fluidTooltipCallback = new IEFluidTooltipCallback();

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{

	}

	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{

	}

	Map<Class, IIRecipeCategory> categories = new LinkedHashMap<>();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		//Recipes
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		slotDrawable = guiHelper.getSlotDrawable();
		categories.put(BathingRecipe.class, new BathingRecipeCategory(guiHelper));
		categories.put(ElectrolyzerRecipe.class, new ElectrolyzerRecipeCategory(guiHelper));
		categories.put(PrecissionAssemblerRecipe.class, new PrecissionAssemblerRecipeCategory(guiHelper));
		categories.put(SawmillRecipe.class, new SawmillRecipeCategory(guiHelper));

		registry.addRecipeCategories(categories.values().toArray(new IRecipeCategory[0]));
	}

	@Override
	public void register(IModRegistry registryIn)
	{
		modRegistry = registryIn;
		//Blacklist

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.item_printed_page, 1, 1));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.item_printed_page, 1, 2));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.item_printed_page, 1, 3));
		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_assembly_scheme, 1, OreDictionary.WILDCARD_VALUE));

		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_bullet, 1, OreDictionary.WILDCARD_VALUE));

		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_wooden_multiblock, 1, OreDictionary.WILDCARD_VALUE));
		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_metal_multiblock0, 1, OreDictionary.WILDCARD_VALUE));
		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.block_metal_multiblock1, 1, OreDictionary.WILDCARD_VALUE));

		ImmersiveIntelligence.logger.info("JEI has just requested our recipes, it seems that we even have a class for registering them!");

		modRegistry.addGhostIngredientHandler(GuiIEContainerBase.class, new IEGhostItemHandler());
		modRegistry.addGhostIngredientHandler(GuiFluidSorter.class, new FluidSorterGhostHandler());
		//modRegistry.addRecipeCatalyst(new ItemStack(IEContent.blockMetalMultiblock, 1, BlockTypes_MetalMultiblock.ASSEMBLER.getMeta()), VanillaRecipeCategoryUid.CRAFTING);

		for(IIRecipeCategory<Object, IRecipeWrapper> cat : categories.values())
		{
			cat.addCatalysts(registryIn);
			modRegistry.handleRecipes(cat.getRecipeClass(), cat, cat.getRecipeCategoryUid());
		}

		modRegistry.addRecipes(BathingRecipe.recipeList, "ii.bathing");
		modRegistry.addRecipeClickArea(GuiChemicalBath.class, 16, 58, 19, 12, "ii.bathing");
		modRegistry.addRecipeClickArea(GuiChemicalBath.class, 131, 57, 11, 13, "ii.bathing");

		modRegistry.addRecipes(ElectrolyzerRecipe.recipeList, "ii.electrolyzer");
		modRegistry.addRecipeClickArea(GuiElectrolyzer.class, 66, 45, 47, 4, "ii.electrolyzer");
		modRegistry.addRecipeClickArea(GuiElectrolyzer.class, 113, 42, 6, 10, "ii.electrolyzer");

		modRegistry.addRecipes(PrecissionAssemblerRecipe.recipeList, "ii.precissionassembler");
		modRegistry.addRecipeClickArea(GuiPrecissionAssembler.class, 49, 45, 78, 4, "ii.precissionassembler");
		modRegistry.addRecipeClickArea(GuiPrecissionAssembler.class, 127, 40, 7, 14, "ii.precissionassembler");
		modRegistry.addRecipeClickArea(GuiPrecissionAssembler.class, 67, 49, 6, 8, "ii.precissionassembler");
		modRegistry.addRecipeClickArea(GuiPrecissionAssembler.class, 85, 49, 6, 8, "ii.precissionassembler");
		modRegistry.addRecipeClickArea(GuiPrecissionAssembler.class, 103, 49, 6, 8, "ii.precissionassembler");

		modRegistry.addRecipes(SawmillRecipe.recipeList, "ii.sawmill");
		modRegistry.addRecipeClickArea(GuiSawmill.class, 33, 42, 43, 4, "ii.sawmill");
		modRegistry.addRecipeClickArea(GuiSawmill.class, 76, 38, 6, 12, "ii.sawmill");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
	{

	}


	private IIRecipeCategory getFactory(Class recipeClass)
	{
		IIRecipeCategory factory = this.categories.get(recipeClass);

		if(factory==null&&recipeClass!=Object.class)
			factory = getFactory(recipeClass.getSuperclass());

		return factory;
	}
}