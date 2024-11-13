package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataTypeItemStack extends DataType
{
	public ItemStack value = ItemStack.EMPTY;

	public DataTypeItemStack(ItemStack i)
	{
		this.value = i.copy();
	}

	public DataTypeItemStack()
	{

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
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeItemStack&&((DataTypeItemStack)obj).value.isItemEqual(value);
	}

	@Override
	public String toString()
	{
		return String.format("%d*%s@%d%s",
				value.getCount(),
				value.getItem().getRegistryName(),
				value.getMetadata(),
				value.hasTagCompound()?value.getTagCompound().toString(): "");
	}
}
