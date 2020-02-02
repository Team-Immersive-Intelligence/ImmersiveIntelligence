package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * Created by Pabilo8 on 2019-06-01.
 */
public class DataPacketTypeItemStack implements IDataType
{
	public ItemStack value;

	public DataPacketTypeItemStack(ItemStack i)
	{
		this.value = i.copy();
	}

	public DataPacketTypeItemStack()
	{

	}

	@Override
	public String getName()
	{
		return "itemstack";
	}

	@Override
	public String valueToString()
	{
		return value.toString();
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
		ImmersiveIntelligence.logger.info(this.value);
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound item_nbt = new NBTTagCompound();
		value.writeToNBT(item_nbt);
		nbt.setString("Type", "itemstack");
		nbt.setTag("Value", item_nbt);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x082730;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 6;
	}
}
