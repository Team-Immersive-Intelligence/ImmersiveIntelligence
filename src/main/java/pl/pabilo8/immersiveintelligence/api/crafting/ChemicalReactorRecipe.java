package pl.pabilo8.immersiveintelligence.api.crafting;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;

import javax.annotation.Nullable;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.10.2026
 */
public class ChemicalReactorRecipe extends IIMultiblockRecipe
{
	public final FluidStack fluidInput;
	public final FluidStack fluidInput2;
	public final IngredientStack itemInput;
	public final IngredientStack catalystInput;
	public final ItemStack itemOutput;
	public final FluidStack[] fluidOutputs;

	public ChemicalReactorRecipe(FluidStack fluidInput, FluidStack fluidInput2, IngredientStack itemInput, IngredientStack catalystInput, ItemStack itemOutput, FluidStack fluidOutput, int energy, int time)
	{
		super(fluidInput, fluidInput2, itemInput, catalystInput, itemOutput);
		this.fluidInput = fluidInput;
		this.fluidInput2 = fluidInput2;
		this.itemInput = itemInput;
		this.catalystInput = catalystInput;
		this.itemOutput = itemOutput;
		this.fluidOutputs = new FluidStack[]{fluidOutput};
		setTimeAndEnergy(time, energy);
	}

	public ChemicalReactorRecipe(FluidStack fluidInput, FluidStack fluidInput2, IngredientStack itemInput, IngredientStack catalystInput, ItemStack itemOutput, FluidStack[] fluidOutputs, int energy, int time)
	{
		super(fluidInput, fluidInput2, itemInput, catalystInput, itemOutput);
		this.fluidInput = fluidInput;
		this.fluidInput2 = fluidInput2;
		this.itemInput = itemInput;
		this.catalystInput = catalystInput;
		this.itemOutput = itemOutput;
		this.fluidOutputs = fluidOutputs;
		setTimeAndEnergy(time, energy);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return null;
	}
}
