package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 25.05.2026
 * @since 25.05.2026
 */


//TODO: adjust it to fit the inputs and outputs of the Coke Battery

public class CokeBatteryRecipe extends IIMultiblockRecipe

{
	public final FluidStack fluidInput;
	public final FluidStack[] fluidOutputs;

	public CokeBatteryRecipe(FluidStack fluidInput, FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2, int energy, int time)
	{
		super(fluidInput);
		int gcd = IIMath.gcd(fluidInput.amount, fluidOutput1.amount, (fluidOutput2!=null?fluidOutput2: fluidOutput1).amount, energy, time);
		fluidInput.amount /= gcd;
		fluidOutput1.amount /= gcd;
		if(fluidOutput2!=null)
			fluidOutput2.amount /= gcd;

		this.fluidOutputs = new FluidStack[2];
		this.fluidOutputs[0] = fluidOutput1;
		this.fluidOutputs[1] = fluidOutput2;
		this.fluidInput = fluidInput;
		this.setTimeAndEnergy(
				time/gcd,
				energy/gcd
		);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return new IIRecipeLayoutBuilder(152, 64)
				.withInputFluidTank(4, 3, fluidInput)
				.withOutputFluidTank(96+8+4, 3, fluidOutputs[0])
				.withOutputFluidTank(118+8+4, 3, fluidOutputs[1])
				.withMultiblockModel(32-8-2, -8)
				.withTimeInfo()
				.withPowerInfo()
				.build();
	}
}
