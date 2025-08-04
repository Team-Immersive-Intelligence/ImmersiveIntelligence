package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.IIMultiblockRecipe;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.06.2025
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

		this.fluidInputList = Collections.singletonList(this.fluidInput);
		this.fluidOutputList = Arrays.asList(fluidOutputs);
	}
}