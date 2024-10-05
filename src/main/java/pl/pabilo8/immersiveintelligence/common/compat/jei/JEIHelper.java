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
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.api.ammo.AmmoRegistry;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem;
import pl.pabilo8.immersiveintelligence.api.crafting.*;
import pl.pabilo8.immersiveintelligence.client.gui.block.*;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.compat.jei.gui_handlers.*;
import pl.pabilo8.immersiveintelligence.common.compat.jei.recipe_handlers.*;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@JEIPlugin
@SuppressWarnings("unused")
public class JEIHelper implements IModPlugin
{
	public static IJeiHelpers jeiHelpers;
	public static IModRegistry modRegistry;
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

		modRegistry.addRecipes(ElectrolyzerRecipe.recipeList, "ii.electrolyzer");
		modRegistry.addRecipeClickArea(GuiElectrolyzer.class, 66, 45, 47, 4, "ii.electrolyzer");
		modRegistry.addRecipeClickArea(GuiElectrolyzer.class, 113, 42, 6, 10, "ii.electrolyzer");

		modRegistry.addRecipes(PrecisionAssemblerRecipe.recipeList, "ii.precision_assembler");
		modRegistry.addRecipeClickArea(GuiPrecisionAssembler.class, 49, 45, 78, 4, "ii.precision_assembler");
		modRegistry.addRecipeClickArea(GuiPrecisionAssembler.class, 127, 40, 7, 14, "ii.precision_assembler");
		modRegistry.addRecipeClickArea(GuiPrecisionAssembler.class, 67, 49, 6, 8, "ii.precision_assembler");
		modRegistry.addRecipeClickArea(GuiPrecisionAssembler.class, 85, 49, 6, 8, "ii.precision_assembler");
		modRegistry.addRecipeClickArea(GuiPrecisionAssembler.class, 103, 49, 6, 8, "ii.precision_assembler");

		modRegistry.addRecipes(SawmillRecipe.RECIPES, "ii.sawmill");
		modRegistry.addRecipeClickArea(GuiSawmill.class, 33, 42, 43, 4, "ii.sawmill");
		modRegistry.addRecipeClickArea(GuiSawmill.class, 76, 38, 6, 12, "ii.sawmill");

		modRegistry.addRecipes(VulcanizerRecipe.recipeList.values(), "ii.vulcanizer");
		modRegistry.addRecipeClickArea(GuiVulcanizer.class, 71, 24, 30, 30, "ii.vulcanizer");
		modRegistry.addAdvancedGuiHandlers(new VulcanizerGuiHandler());

		modRegistry.addRecipes(FillerRecipe.recipeList, "ii.filler");
		modRegistry.addRecipeClickArea(GuiFiller.class, 41, 2, 60, 60, "ii.filler");

		modRegistry.addAdvancedGuiHandlers(new UpgradeGuiHandler());
		modRegistry.addAdvancedGuiHandlers(new AmmoCrateGuiHandler(),
				new ArithmeticLogicMachineGuiHandler(),
				new DataInputMachineGuiHandler(),
				new RedstoneInterfaceGuiHandler.Data(),
				new RedstoneInterfaceGuiHandler.Redstone(),
				new EmplacementGuiHandler()
		);

		//modRegistry.addRecipes(RecipeMinecart.listAllRecipes, VanillaRecipeCategoryUid.CRAFTING);
		//modRegistry.addRecipes(RecipeCrateConversion.listAllRecipes, VanillaRecipeCategoryUid.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{

	}
}