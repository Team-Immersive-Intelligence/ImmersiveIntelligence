package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataPacketTypeNull implements IDataType
{
	public DataPacketTypeNull()
	{
	}

	@Override
	public String getName()
	{
		return "null";
	}

	@Override
	public String valueToString()
	{
		return "null";
	}

	@Override
	public void setDefaultValue()
	{

	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{

	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		return getHeaderTag();
	}

	@Override
	public int getTypeColour()
	{
		return 0x8f2fb3;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 0;
	}
}