package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * Created by Pabilo8 on 2019-06-01.
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
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Type", "null");
		return nbt;
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