package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class PaintingRecipe extends MultiblockRecipe
{
	public final BiFunction<IIColor, ItemStack, ItemStack> process;
	public final IngredientStack itemInput;

	public static LinkedList<PaintingRecipe> recipeList = new LinkedList<>();

	int paintAmount;
	int totalProcessTime;
	int totalProcessEnergy;

	public PaintingRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, Object itemInput, int energy, int time, int paintAmount)
	{
		this.process = process;
		this.itemInput = ApiUtils.createIngredientStack(itemInput);
		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.totalProcessTime = (int)Math.floor((float)time);
		this.paintAmount = (int)Math.floor((float)paintAmount);

		this.inputList = Lists.newArrayList(this.itemInput);
		this.outputList = getExampleColoredItems();
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

	public static PaintingRecipe addRecipe(BiFunction<IIColor, ItemStack, ItemStack> process, IngredientStack itemInput, int energy, int time, int paintAmount)
	{
		PaintingRecipe r = new PaintingRecipe(process, itemInput, energy, time, paintAmount);
		recipeList.add(r);
		return r;
	}

	public static List<PaintingRecipe> removeRecipesForInput(ItemStack stack)
	{
		List<PaintingRecipe> list = new ArrayList<>();
		Iterator<PaintingRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			PaintingRecipe ir = it.next();
			if(ir.itemInput.matchesItemStack(stack))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static PaintingRecipe findRecipe(ItemStack input)
	{
		return recipeList.stream().filter(recipe -> recipe.itemInput.matchesItemStack(input)).findFirst().orElse(null);
	}

	@Override
	public NonNullList<ItemStack> getActualItemOutputs(TileEntity te)
	{
		if(te instanceof TileEntityChemicalPainter)
			return NonNullList.from(ItemStack.EMPTY, process.apply(((TileEntityChemicalPainter)te).color, ((TileEntityChemicalPainter)te).inventory.get(0).copy()));
		return NonNullList.from(ItemStack.EMPTY);
	}

	@Override
	public int getMultipleProcessTicks()
	{
		return 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setTag("item_input", itemInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public static PaintingRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack item_input = IngredientStack.readFromNBT(nbt.getCompoundTag("item_input"));
		FluidStack fluid_input = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid_input"));

		return findRecipe(item_input.stack);
	}

	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}

	public int getPaintAmount()
	{
		return paintAmount;
	}
}