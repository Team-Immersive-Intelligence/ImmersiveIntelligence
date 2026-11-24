package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;

import javax.annotation.Nullable;



/**
 * @author Avalon (avalon@iiteam.net)
 * @since 21.11.2026
 * the chemical reactor can use two fluids, an item and a catalyst to produce an item and or fluid
 * set up in a way where you can pick and choose the inputs/ not need all of them to function
 * put null in for "blank space" in the recipes/ unused slots.
 * Fluid tank does not matter for the recipies
 */
public class SmallChemicalReactorRecipe extends IIMultiblockRecipe
{
	public final FluidStack fluidInput1;
	@Nullable
	public final FluidStack fluidInput2, fluidOutput;
	@Nullable
	public final IngredientStack itemInput, catalyst;
	public final ItemStack itemOutput;

	public SmallChemicalReactorRecipe(FluidStack fluidInput1, @Nullable FluidStack fluidInput2,
			@Nullable IngredientStack itemInput, @Nullable IngredientStack catalyst, @Nullable FluidStack fluidOutput,
			ItemStack itemOutput, int energy, int time)
	{
		super(fluidInput1, fluidInput2, itemInput, catalyst, fluidOutput, itemOutput);
		this.fluidInput1 = fluidInput1.copy();
		this.fluidInput2 = fluidInput2==null?null: fluidInput2.copy();
		this.itemInput = itemInput;
		this.catalyst = catalyst;
		this.fluidOutput = fluidOutput==null?null: fluidOutput.copy();
		this.itemOutput = itemOutput.copy();
		this.inputList = Lists.newArrayList();
		if(itemInput!=null)
			this.inputList.add(itemInput);
		if(catalyst!=null)
			this.inputList.add(catalyst);
		this.outputList = NonNullList.withSize(1, ItemStack.EMPTY);
		if(!itemOutput.isEmpty())
			this.outputList.set(0, itemOutput.copy());
		setTimeAndEnergy(time, energy);
		completeRegistration(this.fluidInput1, this.fluidInput2, itemInput, catalyst, this.fluidOutput, this.itemOutput);
	}

	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(170, 64)
				.withInputFluidTank(4, 3, fluidInput1)
				.withMultiblockModel(58-10, 5-10)
				.withTimeInfo()
				.withPowerInfo();
		if(fluidInput2!=null)
			builder.withInputFluidTank(26, 3, fluidInput2);
		if(itemInput!=null)
			builder.withInputSlot(49, 4, itemInput);
		if(catalyst!=null)
			builder.withToolSlot(49, 30, catalyst);
		if(fluidOutput!=null)
			builder.withOutputFluidTank(126, 3, fluidOutput);
		if(!itemOutput.isEmpty())
			builder.withOutputSlot(150, 18, itemOutput);
		return builder.build();
	}
}
