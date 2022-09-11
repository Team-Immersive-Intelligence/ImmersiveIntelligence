package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionWorkshop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * @author Pabilo8
 * @since 08-08-2019
 */
public class AmmunitionWorkshopRecipe extends MultiblockRecipe
{
	public final BiFunction<ItemStack, ItemStack, ItemStack> process;
	public final IngredientStack coreInput, casingInput;

	public static final LinkedList<AmmunitionWorkshopRecipe> recipeList = new LinkedList<>();

	final int totalProcessTime;
	final int totalProcessEnergy;

	public AmmunitionWorkshopRecipe(BiFunction<ItemStack, ItemStack, ItemStack> process, Object coreInput, Object casingInput, int energy, int time)
	{
		this.process = process;
		this.coreInput = ApiUtils.createIngredientStack(coreInput);
		this.casingInput = ApiUtils.createIngredientStack(casingInput);

		this.totalProcessEnergy = (int)Math.floor((float)energy);
		this.totalProcessTime = (int)Math.floor((float)time);

		this.inputList = Lists.newArrayList(this.coreInput, this.casingInput);
		this.outputList = getExampleItems();
	}

	private NonNullList<ItemStack> getExampleItems()
	{
		return NonNullList.from(ItemStack.EMPTY,
				process.apply(coreInput.getExampleStack(), casingInput.getExampleStack().copy())
		);
	}

	public static AmmunitionWorkshopRecipe addRecipe(BiFunction<ItemStack, ItemStack, ItemStack> process, IngredientStack coreInput, IngredientStack casingInput, int energy, int time)
	{
		AmmunitionWorkshopRecipe r = new AmmunitionWorkshopRecipe(process, coreInput, casingInput, energy, time);
		recipeList.add(r);
		return r;
	}

	public static List<AmmunitionWorkshopRecipe> removeRecipesForCore(ItemStack stack)
	{
		List<AmmunitionWorkshopRecipe> list = new ArrayList<>();
		Iterator<AmmunitionWorkshopRecipe> it = recipeList.iterator();
		while(it.hasNext())
		{
			AmmunitionWorkshopRecipe ir = it.next();
			if(ir.coreInput.matchesItemStack(stack))
			{
				list.add(ir);
				it.remove();
			}
		}
		return list;
	}

	public static AmmunitionWorkshopRecipe findRecipe(ItemStack inputCore, ItemStack inputCasing)
	{
		return recipeList.stream()
				.filter(recipe -> recipe.coreInput.matchesItemStackIgnoringSize(inputCore))
				.filter(recipe -> recipe.casingInput.matchesItemStackIgnoringSize(inputCasing))
				.findFirst().orElse(null);
	}

	@Override
	public NonNullList<ItemStack> getActualItemOutputs(TileEntity te)
	{
		if(te instanceof TileEntityAmmunitionWorkshop)
			return NonNullList.from(ItemStack.EMPTY, process.apply(((TileEntityAmmunitionWorkshop)te).inventory.get(0), ((TileEntityAmmunitionWorkshop)te).inventory.get(1).copy()));
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
		nbt.setTag("core", coreInput.writeToNBT(new NBTTagCompound()));
		nbt.setTag("casing", casingInput.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	public static AmmunitionWorkshopRecipe loadFromNBT(NBTTagCompound nbt)
	{
		IngredientStack core = IngredientStack.readFromNBT(nbt.getCompoundTag("core"));
		IngredientStack casing = IngredientStack.readFromNBT(nbt.getCompoundTag("casing"));
		return findRecipe(core.stack,casing.stack);
	}

	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}
}