package pl.pabilo8.immersiveintelligence.common.util.fluid;

import lombok.Setter;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.function.Predicate;

/**
 * A {@link FluidTank} that allows filtering of fluids that can be inserted or extracted.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.08.2025
 */
public class FilteredFluidTank extends FluidTank implements IFilteredTank<FilteredFluidTank>
{
	@Setter
	private Predicate<FluidStack> inputFilter;
	@Setter
	private Predicate<FluidStack> outputFilter;

	//--- Constructors ---//

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
