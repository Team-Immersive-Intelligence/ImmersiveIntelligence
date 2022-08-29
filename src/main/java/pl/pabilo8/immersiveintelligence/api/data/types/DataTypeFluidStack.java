package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeFluidStack implements IDataType
{
	@Nullable
	public FluidStack value;

	public DataTypeFluidStack(FluidStack i)
	{
		this.value = i.copy();
	}

	public DataTypeFluidStack()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "fluidstack";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{{"ie.manual.entry.def_value", "ie.manual.entry.empty"}};
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		if(value==null||value.getFluid()==null)
			return "Empty";

		return String.format("%d*%s%s",
				value.amount,
				value.getLocalizedName(),
				value.tag!=null?value.tag.toString(): "");
	}

	@Override
	public void setDefaultValue()
	{
		value = null;
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
	public int getTypeColour()
	{
		return 0x082730;
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
}
