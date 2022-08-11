package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeItemStack implements IDataType
{
	public ItemStack value;

	public DataTypeItemStack(ItemStack i)
	{
		this.value = i.copy();
	}

	public DataTypeItemStack()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "itemstack";
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
		return String.format("%d*%s@%d%s",
				value.getCount(),
				value.getItem().getRegistryName(),
				value.getMetadata(),
				value.hasTagCompound()?value.getTagCompound().toString(): "");
	}

	@Override
	public void setDefaultValue()
	{
		value = ItemStack.EMPTY;
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = new ItemStack(n.getCompoundTag("Value"));
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		NBTTagCompound item_nbt = new NBTTagCompound();
		value.writeToNBT(item_nbt);
		nbt.setTag("Value", item_nbt);
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
		return obj instanceof DataTypeItemStack&&((DataTypeItemStack)obj).value.isItemEqual(value);
	}
}
