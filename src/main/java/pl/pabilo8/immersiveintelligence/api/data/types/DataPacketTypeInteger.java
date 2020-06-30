package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataPacketTypeInteger implements IDataType
{
	public int value;

	public DataPacketTypeInteger(int i)
	{
		this.value = i;
	}

	public DataPacketTypeInteger()
	{

	}

	@Override
	public String getName()
	{
		return "integer";
	}

	@Override
	public String valueToString()
	{
		return String.valueOf(value);
	}

	@Override
	public void setDefaultValue()
	{
		value = 0;
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getInteger("Value");
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Type", "integer");
		nbt.setInteger("Value", value);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x26732e;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 2;
	}
}
