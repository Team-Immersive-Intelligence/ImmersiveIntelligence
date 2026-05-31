package pl.pabilo8.immersiveintelligence.common.compat.jei;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.compat.jei.IEFluidTooltipCallback;
import com.google.common.collect.LinkedHashMultimap;
import mezz.jei.api.*;
import mezz.jei.api.ISubtypeRegistry.ISubtypeInterpreter;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IIngredientType;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
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
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent.MouseButton;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients.JEIDustStackHelper;
import pl.pabilo8.immersiveintelligence.common.compat.jei.ingredients.JEIDustStackRenderer;
import pl.pabilo8.immersiveintelligence.common.item.ammo.gun.ItemIIAmmoRevolver.RevolverAmmoPart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;

import javax.annotation.Nonnull;
import java.util.Collections;

@JEIPlugin
@SuppressWarnings("unused")
public class JEIHelper implements IModPlugin
{
	public static final IIngredientType<DustStack> DUSTSTACK = () -> DustStack.class;
	public static IJeiHelpers jeiHelpers;
	public static IModRegistry modRegistry;
	public static IJeiRuntime jeiRuntime;
	public static IDrawable slotDrawable;
	public static IEFluidTooltipCallback fluidTooltipCallback = new IEFluidTooltipCallback();
	@SuppressWarnings("rawtypes")
	LinkedHashMultimap<Class<? extends MultiblockRecipe>, IIRecipeJEICategory> categories = LinkedHashMultimap.create();

	public JEIHelper()
	{

	}

	@SideOnly(Side.CLIENT)
	public static void addRecipesDecoGuiLink(DecoComponent<?> gui, String categoryName)
	{
		if(jeiRuntime==null)
			return;
		IRecipesGui recipesGui = jeiRuntime.getRecipesGui();
		if(recipesGui==null)
			return;

		gui.withTranslatedTooltip("jei.tooltip.show.recipes")
				.withOnPressed((g, mouseButton, mouseX, mouseY) -> {
					if(mouseButton==MouseButton.LEFT)
					{
						recipesGui.showCategories(Collections.singletonList(categoryName));
						return true;
					}
					return false;
				});
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
		registry.register(DUSTSTACK, Collections.emptyList(), new JEIDustStackHelper(), new JEIDustStackRenderer());
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry)
	{
		jeiHelpers = registry.getJeiHelpers();
		//Recipes
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		slotDrawable = guiHelper.getSlotDrawable();
		categories.clear();
		categories.put(BathingRecipe.class, new IIRecipeJEICategory<>(BathingRecipe.class,
				IIContent.blockMetalMultiblock0.getStack(MetalMultiblocks0.CHEMICAL_BATH), "bathing"));
		categories.put(BathingRecipe.class, new IIRecipeJEICategory<>(BathingRecipe.class,
				IIContent.blockMetalMultiblock0.getStack(MetalMultiblocks0.CHEMICAL_BATH), "washing"));
		categories.put(ElectrolyzerRecipe.class, new IIRecipeJEICategory<>(ElectrolyzerRecipe.class,
				IIContent.blockMetalMultiblock0.getStack(MetalMultiblocks0.ELECTROLYZER)));
		categories.put(PrecisionAssemblerRecipe.class, new IIRecipeJEICategory<>(PrecisionAssemblerRecipe.class,
				IIContent.blockMetalMultiblock0.getStack(MetalMultiblocks0.PRECISION_ASSEMBLER)));
		categories.put(SawmillRecipe.class, new IIRecipeJEICategory<>(SawmillRecipe.class,
				IIContent.blockWoodenMultiblock.getStack(WoodenMultiblocks.SAWMILL)));
		categories.put(CoagulatorRecipe.class, new IIRecipeJEICategory<>(CoagulatorRecipe.class,
				IIContent.blockMetalMultiblock1.getStack(MetalMultiblocks1.COAGULATOR)));
		categories.put(VulcanizerRecipe.class, new IIRecipeJEICategory<>(VulcanizerRecipe.class,
				IIContent.blockMetalMultiblock1.getStack(MetalMultiblocks1.VULCANIZER)));
		categories.put(FillerRecipe.class, new IIRecipeJEICategory<>(FillerRecipe.class,
				IIContent.blockMetalMultiblock1.getStack(MetalMultiblocks1.FILLER)));
		categories.put(PaintingRecipe.class, new IIRecipeJEICategory<>(PaintingRecipe.class,
				IIContent.blockMetalMultiblock1.getStack(MetalMultiblocks1.CHEMICAL_PAINTER)));

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

		jeiHelpers.getIngredientBlacklist().addIngredientToBlacklist(new ItemStack(IIContent.itemAmmoRevolver,
				1, RevolverAmmoPart.UNUSED.ordinal()));

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
		categories.values().forEach(cat -> cat.register(registryIn));

		if(FMLCommonHandler.instance().getSide()==Side.CLIENT)
			IIGUI.registerDecoJEICompat(modRegistry);
	}

	@Override
	public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime)
	{
		this.jeiRuntime = jeiRuntime;
	}
}
