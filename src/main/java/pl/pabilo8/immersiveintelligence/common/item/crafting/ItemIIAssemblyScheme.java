package pl.pabilo8.immersiveintelligence.common.item.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.api.crafting.PrecisionAssemblerRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler;
import pl.pabilo8.immersiveintelligence.api.utils.ItemTooltipHandler.IAdvancedTooltipItem;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.immersiveintelligence.common.util.item.ItemIIBase;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 25.06.2019
 */
@IIItemProperties(category = IICategory.RESOURCES)
@GeneratedItemModels(itemName = "assembly_scheme")
public class ItemIIAssemblyScheme extends ItemIIBase implements IAdvancedTooltipItem
{
	private static final String descriptionKey = IIReference.DESCRIPTION_KEY+"assembly_scheme.";
	private static final String NBT_RECIPE_RESULT = "recipeItem";
	private static final String NBT_USE_COUNT = "createdItems";

	public ItemIIAssemblyScheme()
	{
		super("assembly_scheme", 8);
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	@ParametersAreNonnullByDefault
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
	{
		ItemStack s = ItemNBTHelper.getItemStack(stack, NBT_RECIPE_RESULT);
		tooltip.add(I18n.format(descriptionKey+"used_to_create",
				TextFormatting.GOLD+s.getDisplayName()
						+(s.getCount() > 1?TextFormatting.GRAY+" x "+TextFormatting.GOLD+s.getCount(): "")));
		tooltip.add(I18n.format(descriptionKey+"items_created",
				TextFormatting.GOLD+String.valueOf(ItemNBTHelper.getInt(stack, NBT_USE_COUNT))));

		PrecisionAssemblerRecipe recipe = getSchemeRecipe(stack);
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LSHIFT,
				descriptionKey+"info_hold1", tooltip))
		{
			tooltip.add(IIReference.COLOR_IMMERSIVE_ORANGE.getHexCol(I18n.format(descriptionKey+"materials")));
			if(recipe!=null)
				for(IngredientStack ingredient : recipe.inputs)
					tooltip.add("   "+TextFormatting.GOLD+ingredient.getExampleStack().getDisplayName()+(
							ingredient.inputSize > 1?
									TextFormatting.GRAY+" x "+TextFormatting.GOLD+ingredient.inputSize: ""));
		}
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, descriptionKey+"info_hold2", tooltip))
		{
			tooltip.add(IIReference.COLOR_ENGINEERS_BLUE.getHexCol(I18n.format(descriptionKey+"materials")));
			if(recipe!=null)
				for(String tool : recipe.tools)
					tooltip.add("   "+TextFormatting.GOLD+PrecisionAssemblerRecipe.getExampleToolStack(tool).getDisplayName());
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addAdvancedInformation(ItemStack stack, int offsetX, List<Integer> offsetsY)
	{
		PrecisionAssemblerRecipe recipe = getSchemeRecipe(stack);
		if(recipe==null)
			return;

		boolean upper = ItemTooltipHandler.canExpandTooltip(Keyboard.KEY_LSHIFT);
		if(upper)
			ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(0),
					Arrays.stream(recipe.inputs).map(IngredientStack::getExampleStack).toArray(ItemStack[]::new));
		if(ItemTooltipHandler.addExpandableTooltip(Keyboard.KEY_LCONTROL, "", null))
			ItemTooltipHandler.drawItemList(offsetX, offsetsY.get(upper?1: 0),
					Arrays.stream(recipe.tools)
							.map(PrecisionAssemblerRecipe::getExampleToolStack)
							.toArray(ItemStack[]::new)
			);
	}


	@Override
	public void onCreated(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityPlayer playerIn)
	{
		super.onCreated(stack, worldIn, playerIn);
		if(!ItemNBTHelper.hasKey(stack, NBT_USE_COUNT))
			ItemNBTHelper.setInt(stack, NBT_USE_COUNT, 0);
	}

	public ItemStack getSchemeStackForRecipe(PrecisionAssemblerRecipe recipe)
	{
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setTag(NBT_RECIPE_RESULT, recipe.output.serializeNBT());
		stack.setTagCompound(tag);
		return stack;
	}

	public ItemStack getSchemeOutput(ItemStack scheme)
	{
		if(ItemNBTHelper.hasKey(scheme, NBT_RECIPE_RESULT))
			return new ItemStack(ItemNBTHelper.getTagCompound(scheme, NBT_RECIPE_RESULT));
		return ItemStack.EMPTY;
	}

	@Nullable
	public PrecisionAssemblerRecipe getSchemeRecipe(ItemStack stack)
	{
		NBTTagCompound tag = ItemNBTHelper.getTagCompound(stack, NBT_RECIPE_RESULT);
		ItemStack recipeResult = new ItemStack(tag);

		return IIMultiblockRecipe.streamRecipes(PrecisionAssemblerRecipe.class)
				.filter(recipe -> recipe.output.isItemEqual(recipeResult))
				.findFirst().orElse(null);
	}

	public void increaseCreatedItems(ItemStack stack, int amount)
	{
		ItemNBTHelper.setInt(stack, NBT_USE_COUNT, ItemNBTHelper.getInt(stack, NBT_USE_COUNT)+amount);
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list)
	{
		if(this.isInCreativeTab(tab))
			list.addAll(IIMultiblockRecipe.streamRecipes(PrecisionAssemblerRecipe.class)
					.map(this::getSchemeStackForRecipe)
					.collect(Collectors.toList()));
	}
}
