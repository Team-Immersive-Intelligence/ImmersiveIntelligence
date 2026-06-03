package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * A FluidTank with option to allow or disallow certain fluids being added or taken.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.08.2025
 */
public class FilteredFluidTank extends FluidTank
{
	private Predicate<FluidStack> inputFilter;
	private Predicate<FluidStack> outputFilter;

	public FilteredFluidTank(int capacity)
	{
		super(capacity);
	}

	public FilteredFluidTank(@Nullable FluidStack fluidStack, int capacity)
	{
		super(fluidStack, capacity);
	}

	public FilteredFluidTank(Fluid fluid, int amount, int capacity)
	{
		super(fluid, amount, capacity);
	}

	public FilteredFluidTank withInputFilter(Predicate<FluidStack> inputFilter)
	{
		this.inputFilter = inputFilter;
		return this;
	}

	public FilteredFluidTank withOutputFilter(Predicate<FluidStack> outputFilter)
	{
		this.outputFilter = outputFilter;
		return this;
	}

	public float getFillPercentage()
	{
		int cap = getCapacity();
		if(cap==0)
			return 0;
		return getFluidAmount()/(float)cap;
	}

	@Override
	public boolean canDrainFluidType(@Nullable FluidStack fluid)
	{
		return super.canDrainFluidType(fluid)&&(outputFilter==null||(fluid!=null&&outputFilter.test(fluid)));
	}

	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		return super.canFillFluidType(fluid)&&(inputFilter==null||inputFilter.test(fluid));
	}
}
