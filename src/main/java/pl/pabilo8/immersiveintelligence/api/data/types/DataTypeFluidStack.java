package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeFluidStack extends DataType
{
	@Nullable
	public FluidStack value = null;

	public DataTypeFluidStack(FluidStack i)
	{
		this.value = i.copy();
	}

	public DataTypeFluidStack()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = FluidStack.loadFluidStackFromNBT(n.getCompoundTag("Value"));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		if(value!=null)
		{
			NBTTagCompound fluid_nbt = new NBTTagCompound();
			value.writeToNBT(fluid_nbt);
			nbt.setTag("Value", fluid_nbt);
		}
		return nbt;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeFluidStack&&(
				value==null?
						((DataTypeFluidStack)obj).value==null:
						value.isFluidStackIdentical(((DataTypeFluidStack)obj).value)
		);
	}

	@Override
	public String toString()
	{
		if(value==null||value.getFluid()==null)
			return "Empty";

		return String.format("%d*%s%s",
				value.amount,
				value.getLocalizedName(),
				value.tag!=null?value.tag.toString(): "");
	}
}
