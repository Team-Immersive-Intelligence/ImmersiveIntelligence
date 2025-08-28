package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 08.08.2019
 */
public class PaintingRecipe extends IIMultiblockRecipe
{
	public final BiFunction<IIColor, ItemStack, ItemStack> process;
	public final IngredientStack itemInput;
	public IISoundAnimation productionAnimation;
	int paintAmount;

	public PaintingRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, Object itemInput, int energy, int time, int paintAmount)
	{
		super(ApiUtils.createIngredientStack(itemInput));

		this.process = process;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.paintAmount = (int)Math.floor((float)paintAmount);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = getExampleColoredItems();

		this.setTimeAndEnergy(time, energy);
	}

	@Deprecated
	public static PaintingRecipe addRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, IngredientStack itemInput, int energy, int time, int paintAmount)
	{
		return new PaintingRecipe(process, itemInput, energy, time, paintAmount);
	}

	private NonNullList<ItemStack> getExampleColoredItems()
	{
		NonNullList<ItemStack> list = NonNullList.create();
		Set<ItemStack> collect = Arrays.stream(EnumDyeColor.values())
				.map(IIColor::fromDye)
				.map(integer -> process.apply(integer, itemInput.getExampleStack().copy()))
				.collect(Collectors.toSet());
		list.addAll(collect);
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
