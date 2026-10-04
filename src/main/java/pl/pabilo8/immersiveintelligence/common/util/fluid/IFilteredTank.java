package pl.pabilo8.immersiveintelligence.common.util.fluid;


import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.util.function.Predicate;

/**
 * An extension of {@link IFluidTank} that allows filtering of fluids that can be inserted or extracted.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.09.2026
 */
public interface IFilteredTank<T extends IFilteredTank<T>> extends IFluidTank
{
	//--- Setters ---//

	default T withInputFilter(Predicate<FluidStack> inputFilter)
	{
		setInputFilter(inputFilter);
		//noinspection unchecked
		return ((T)this);
	}

	default T withOutputFilter(Predicate<FluidStack> outputFilter)
	{
		setOutputFilter(outputFilter);
		//noinspection unchecked
		return ((T)this);
	}

	void setInputFilter(Predicate<FluidStack> inputFilter);

	void setOutputFilter(Predicate<FluidStack> outputFilter);

	//--- Getters ---//

	default float getFillPercentage()
	{
		int cap = getCapacity();
		if(cap==0)
			return 0;
		return getFluidAmount()/(float)cap;
	}
}
