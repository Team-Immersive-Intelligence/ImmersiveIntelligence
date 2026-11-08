package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.*;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.ComponentType;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @since 08.08.2019
 */
public class ChemicalPainterRecipe extends IIMultiblockRecipe
{
	public final BiFunction<IIColor, ItemStack, ItemStack> process;
	public final IngredientStack itemInput;
	public IISoundAnimation productionAnimation;
	int paintAmount;

	public ChemicalPainterRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, Object itemInput, int energy, int time, int paintAmount)
	{
		super(ApiUtils.createIngredientStack(itemInput));

		this.process = process;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.paintAmount = (int)Math.floor((float)paintAmount);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = getExampleColoredItems();

		this.setTimeAndEnergy(time, energy);
		completeRegistration(this.itemInput, this.outputList.toArray());
	}

	@Deprecated
	public static ChemicalPainterRecipe addRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, IngredientStack itemInput, int energy, int time, int paintAmount)
	{
		return new ChemicalPainterRecipe(process, itemInput, energy, time, paintAmount);
	}

	private NonNullList<ItemStack> getExampleColoredItems()
	{
		NonNullList<ItemStack> list = NonNullList.create();
		for(EnumDyeColor dye : EnumDyeColor.values())
			for(ItemStack stack : getColoredItems(IIColor.fromDye(dye)))
				if(list.stream().noneMatch(existing -> ItemStack.areItemStacksEqual(existing, stack)))
					list.add(stack);
		return list;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("item_input", itemInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	protected void loadClientSideContent()
	{
		this.productionAnimation = new IISoundAnimation(IIReference.RES_II.with("chemical_painter/production_sounds"))
				.compile(this.getTotalProcessTime());
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return getColoredLayout(IIColor.fromDye(EnumDyeColor.MAGENTA));
	}

	private NonNullList<ItemStack> getColoredItems(IIColor color)
	{
		NonNullList<ItemStack> results = NonNullList.create();
		for(ItemStack alternative : itemInput.getStackList())
		{
			if(alternative.isEmpty())
				continue;
			ItemStack input = alternative.copy();
			input.setCount(itemInput.inputSize);
			ItemStack output = process.apply(color, input);
			if(!output.isEmpty()&&results.stream().noneMatch(stack -> ItemStack.areItemStacksEqual(stack, output)))
				results.add(output.copy());
		}
		return results;
	}

	@SideOnly(Side.CLIENT)
	public List<IIMultiblockRecipe> getDisplayRecipes()
	{
		List<IIRecipeLayout> variants = new ArrayList<>();
		for(EnumDyeColor dye : EnumDyeColor.values())
			for(ItemStack input : IIRecipeLayout.getDisplayItems(itemInput))
			{
				IIColor color = IIColor.fromDye(dye);
				ItemStack output = process.apply(color, input.copy());
				if(output.isEmpty())
					continue;
				IngredientStack exampleInput = new IngredientStack(input).setUseNBT(itemInput.useNBT);
				variants.add(getColoredLayout(color, exampleInput, java.util.Collections.singletonList(output.copy())));
			}
		return variants.isEmpty()?java.util.Collections.emptyList(): java.util.Collections.singletonList(
				new IIRecipeDisplay(getName()+"_colours", getTotalProcessTime(), getTotalProcessEnergy(), variants));
	}

	private IIRecipeLayout getColoredLayout(IIColor color)
	{
		return getColoredLayout(color, itemInput, getColoredItems(color));
	}

	private IIRecipeLayout getColoredLayout(IIColor color, IngredientStack input, List<ItemStack> outputs)
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(180, 64)
				.withInputSlot(4, 23-4, input)
				.withMultiblockModel(56+8, -6-4, 88, 64, "default")
				.withTimeInfo().withPowerInfo();

		//Keep the count of each output alternative.
		if(!outputs.isEmpty())
			builder.withOutputSlot(158, 23-4, outputs);
		int[] amounts = {getCyanAmount(color), getMagentaAmount(color), getYellowAmount(color), getBlackAmount(color)};
		net.minecraftforge.fluids.Fluid[] fluids = {IIContent.fluidInkCyan, IIContent.fluidInkMagenta, IIContent.fluidInkYellow, IIContent.fluidInkBlack};

		//Add ink tanks with amounts for all the color variants
		for(int i = 0; i < fluids.length; i++)
			builder.withComponent(LayoutComponent.builder(ComponentType.FLUID_TANK, 26+(i%2*22), 4+(i/2*22))
					.size(22, 22)
					.input()
					.data(new FluidStack(fluids[i], amounts[i]))
					.build()
			);

		return builder.build();
	}

	public int getCyanAmount(IIColor color)
	{
		return (int)(paintAmount*color.getCMYK()[0]);
	}

	public int getMagentaAmount(IIColor color)
	{
		return (int)(paintAmount*color.getCMYK()[1]);
	}

	public int getYellowAmount(IIColor color)
	{
		return (int)(paintAmount*color.getCMYK()[2]);
	}

	public int getBlackAmount(IIColor color)
	{
		return (int)(paintAmount*color.getCMYK()[3]);
	}
}
