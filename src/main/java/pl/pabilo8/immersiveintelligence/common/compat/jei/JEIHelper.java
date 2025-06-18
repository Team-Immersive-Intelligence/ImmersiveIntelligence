package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.compat.jei.IEFluidTooltipCallback;
import com.google.common.collect.LinkedHashMultimap;
import mezz.jei.api.*;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.*;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiChemicalBath;
import pl.pabilo8.immersiveintelligence.client.gui.block.GuiVulcanizer;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers.UpgradeGuiHandler;
import pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers.VulcanizerGuiHandler;
import pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers.*;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.stream.Collectors;

@JEIPlugin
@SuppressWarnings("unused")
public class JEIHelper implements IModPlugin
{
	public static IJeiHelpers jeiHelpers;
	public static IModRegistry modRegistry;
	public static IJeiRuntime jeiRuntime;
	public static IDrawable slotDrawable;
	public static IEFluidTooltipCallback fluidTooltipCallback = new IEFluidTooltipCallback();

	public JEIHelper()
	{

	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry)
	{
		subtypeRegistry.registerSubtypeInterpreter(IIContent.itemBulletMagazine, stack -> {
			if(!stack.isEmpty())
				return ItemNBTHelper.hasKey(stack, "bullets")?ISubtypeInterpreter.NONE: IIContent.itemBulletMagazine.getSubNames()[stack.getMetadata()];
			return ISubtypeInterpreter.NONE;
		});
	}

	@Override
	public void registerIngredients(@Nonnull IModIngredientRegistration registry)
	{

	}

	@SuppressWarnings("rawtypes")
	LinkedHashMultimap<Class<? extends MultiblockRecipe>, IIRecipeCategory> categories = LinkedHashMultimap.create();

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		//Recipes
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		slotDrawable = guiHelper.getSlotDrawable();
		categories.clear();
		categories.put(BathingRecipe.class, new BathingRecipeCategory(guiHelper, false));
		categories.put(BathingRecipe.class, new BathingRecipeCategory(guiHelper, true));
		categories.put(ElectrolyzerRecipe.class, new ElectrolyzerRecipeCategory(guiHelper));
		categories.put(PrecisionAssemblerRecipe.class, new PrecisionAssemblerRecipeCategory(guiHelper));
		categories.put(SawmillRecipe.class, new SawmillRecipeCategory(guiHelper));
		categories.put(VulcanizerRecipe.class, new VulcanizerRecipeCategory(guiHelper));
		categories.put(FillerRecipe.class, new FillerRecipeCategory(guiHelper));

		registry.addRecipeCategories(categories.values().toArray(new IRecipeCategory[0]));
	}

	@Override
	public void register(@Nonnull IModRegistry registryIn)
	{
		modRegistry = registryIn;
		//Blacklist

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemPunchtape, 1, 0));

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemPrintedPage, 1, 1));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemPrintedPage, 1, 2));
		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemPrintedPage, 1, 3));
		//jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(CommonProxy.item_assembly_scheme, 1, OreDictionary.WILDCARD_VALUE));

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemAmmoRevolver, 1, ItemIIAmmoRevolver.UNUSED));


		for(IAmmoTypeItem<?, ?> bullet : AmmoRegistry.getAllAmmoItems())
		{
			ItemStack stack = bullet.getAmmoStack(AmmoRegistry.MISSING_CORE, CoreType.SOFTPOINT, FuseType.CONTACT);
			stack.setTagCompound(new NBTTagCompound());
			jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(stack);
		}

		for(Block block : IIContent.BLOCKS)
			if(block instanceof BlockIIMultiblock)
				jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE));

		IILogger.info("JEI has just requested our recipes, it seems that we even have a class for registering them!");

		for(IIRecipeCategory<Object, IRecipeWrapper> cat : categories.values())
		{
			cat.addCatalysts(registryIn);
			modRegistry.handleRecipes(cat.getRecipeClass(), cat, cat.getRecipeCategoryUid());
		}

		modRegistry.addRecipes(BathingRecipe.recipeList.stream().filter(bathingRecipe -> !bathingRecipe.isWashing).collect(Collectors.toList()), "ii.bathing");
		modRegistry.addRecipes(BathingRecipe.recipeList.stream().filter(bathingRecipe -> bathingRecipe.isWashing).collect(Collectors.toList()), "ii.washing");
		modRegistry.addRecipeClickArea(GuiChemicalBath.class, 16, 58, 19, 12, "ii.bathing", "ii.washing");
		modRegistry.addRecipeClickArea(GuiChemicalBath.class, 131, 57, 19, 13, "ii.bathing", "ii.washing");

		modRegistry.addRecipes(IIMultiblockRecipe.getRecipes(ElectrolyzerRecipe.class), "ii.electrolyzer");
		modRegistry.addRecipes(PrecisionAssemblerRecipe.recipeList, "ii.precision_assembler");
		modRegistry.addRecipes(IIMultiblockRecipe.getRecipes(SawmillRecipe.class), "ii.sawmill");

		modRegistry.addRecipes(VulcanizerRecipe.recipeList.values(), "ii.vulcanizer");
		modRegistry.addRecipeClickArea(GuiVulcanizer.class, 71, 24, 30, 30, "ii.vulcanizer");
		modRegistry.addAdvancedGuiHandlers(new VulcanizerGuiHandler());

		modRegistry.addRecipes(IIMultiblockRecipe.getRecipes(FillerRecipe.class), "ii.filler");
		modRegistry.addAdvancedGuiHandlers(new UpgradeGuiHandler());

		if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
			IIGUI.registerDecoJEICompat(modRegistry);
	}

	@SideOnly(Side.CLIENT)
	public static void addRecipesDecoGuiLink(GuiComponentDecoBase<?> gui, String categoryName)
	{
		if(jeiRuntime==null)
			return;
		IRecipesGui recipesGui = jeiRuntime.getRecipesGui();
		if(recipesGui==null)
			return;

		gui.withTranslatedTooltip("jei.tooltip.show.recipes")
				.withOnPressed((g, mouseX, mouseY) -> {
					recipesGui.showCategories(Collections.singletonList(categoryName));
					return true;
				});
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{
		this.jeiRuntime = jeiRuntime;
	}
}