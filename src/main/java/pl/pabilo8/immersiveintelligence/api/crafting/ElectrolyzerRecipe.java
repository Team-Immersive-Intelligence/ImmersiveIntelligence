package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayoutBuilder;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 05.10.2026
 * @ii-approved 0.3.1
 * @since 08.08.2019
 */
public class ElectrolyzerRecipe extends IIMultiblockRecipe
{
	public final FluidStack fluidInput;
	public final FluidStack[] fluidOutputs;

	public ElectrolyzerRecipe(FluidStack fluidInput, FluidStack fluidOutput1, @Nullable FluidStack fluidOutput2, int energy, int time)
	{
		super(fluidInput);
		fluidInput = fluidInput.copy();
		fluidOutput1 = fluidOutput1.copy();
		fluidOutput2 = fluidOutput2==null?null: fluidOutput2.copy();
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
		completeRegistration(this.fluidInput, this.fluidOutputs[0], this.fluidOutputs[1]);
	}

	@Nullable
	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		IIRecipeLayoutBuilder builder = new IIRecipeLayoutBuilder(152, 64)
				.withInputFluidTank(4, 3, fluidInput)
				.withOutputFluidTank(96+8+4, 3, fluidOutputs[0])
				.withMultiblockModel(32-8-2, -8)
				.withTimeInfo()
				.withPowerInfo();
		if(fluidOutputs[1]!=null)
			builder.withOutputFluidTank(130, 3, fluidOutputs[1]);
		return builder.build();
	}
}
