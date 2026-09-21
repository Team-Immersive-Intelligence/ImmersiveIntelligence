package pl.pabilo8.immersiveintelligence.common.util.fluid;

import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.MultiFluidTank;
import lombok.Setter;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Predicate;

/**
 * A {@link MultiFluidTank} that allows filtering of fluids that can be inserted or extracted.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 21.09.2026
 */
public class FilteredMultiFluidTank extends MultiFluidTank implements IFilteredTank<FilteredMultiFluidTank>
{
	@Setter
	private Predicate<FluidStack> inputFilter;
	@Setter
	private Predicate<FluidStack> outputFilter;

	//--- Constructors ---//

	public FilteredMultiFluidTank(int capacity)
	{
		super(capacity);
	}

	public FilteredMultiFluidTank(@Nullable FluidStack fluidStack, int capacity)
	{
		this(capacity);
		this.fill(fluidStack, true);
	}

	public FilteredMultiFluidTank(Fluid fluid, int amount, int capacity)
	{
		this(capacity);
		this.fill(new FluidStack(fluid, amount), true);
	}

	//--- Overrides ---//

	@Nullable
	@Override
	public FluidStack drain(FluidStack fluid, boolean doDrain)
	{
		if(outputFilter==null||(fluid!=null&&outputFilter.test(fluid)))
			return super.drain(fluid, doDrain);
		return null;
	}

	@Nullable
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if(this.fluids.isEmpty())
			return null;

		Iterator<FluidStack> it = this.fluids.iterator();
		while(it.hasNext())
		{
			FluidStack fluid = it.next();
			if(outputFilter==null||(fluid!=null&&outputFilter.test(fluid)))
			{
				//Copy the drained stack with a cap on amount
				int amount = Math.min(maxDrain, fluid.amount);
				FluidStack resource = Utils.copyFluidStackWithAmount(fluid, amount, true);

				//Drain fluid from the tank
				if(doDrain)
				{
					fluid.amount -= amount;
					if(fluid.amount <= 0)
						it.remove();
				}
				return resource;
			}
		}
		return null;
	}

	@Override
	public int fill(FluidStack fluid, boolean doFill)
	{
		if(inputFilter==null||(fluid!=null&&inputFilter.test(fluid)))
			return 0;
		return super.fill(fluid, doFill);
	}

}
