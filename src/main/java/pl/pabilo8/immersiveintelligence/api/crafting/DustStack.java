package pl.pabilo8.immersiveintelligence.api.crafting;

import net.minecraft.nbt.NBTTagCompound;

/**
 * A class, similar in concept to FluidStack, but representing dusts (solids)
 * Amount is measured in mB, same as for fluids
 * by default, one dust = 100mB
 * <p>currently only the Filler multiblock uses it, <b><u>but you can too!</u></b></p>
 */
public class DustStack
{
	public final String name;
	public int amount;

	public DustStack(String name, int amount)
	{
		this.name = name;
		this.amount = amount;
	}

	public DustStack(NBTTagCompound nbt)
	{
		this(nbt.getString("name"), nbt.getInteger("amount"));
	}

	public static DustStack getEmptyStack()
	{
		return new DustStack("", 0);
	}

	public DustStack getSubtracted(DustStack dust)
	{
		if(amount > dust.amount)
			return new DustStack(name, amount-dust.amount);
		return getEmptyStack();
	}

	public boolean isEmpty()
	{
		return "".equals(name)||amount==0;
	}

	public DustStack copy()
	{
		return new DustStack(name, amount);
	}

	public boolean canMergeWith(DustStack other)
	{
		return (isEmpty()||other.isEmpty())||other.name.equals(this.name);
	}

	public DustStack mergeWith(DustStack other)
	{
		return new DustStack(this.isEmpty()?other.name: name, this.amount+other.amount);
	}

	@Override
	public boolean equals(Object o)
	{
		if(this==o) return true;
		if(!(o instanceof DustStack))
			return false;
		DustStack dustStack = (DustStack)o;
		return amount==dustStack.amount&&name.equals(dustStack.name);
	}

	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("name", name);
		nbt.setInteger("amount", amount);
		return nbt;
	}
}
