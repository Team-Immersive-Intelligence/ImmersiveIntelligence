package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataPacketTypeBoolean implements IDataType
{
	public boolean value;

	public DataPacketTypeBoolean(boolean i)
	{
		this.value = i;
	}

	public DataPacketTypeBoolean()
	{
	}


	@Override
	public String getName()
	{
		return "boolean";
	}

	@Override
	public String valueToString()
	{
		return String.valueOf(value);
	}

	@Override
	public void setDefaultValue()
	{
		value = false;
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getBoolean("Value");
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setBoolean("Value", value);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x922020;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 1;
	}
}
