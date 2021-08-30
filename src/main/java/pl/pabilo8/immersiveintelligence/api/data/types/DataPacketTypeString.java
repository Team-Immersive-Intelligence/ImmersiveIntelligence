package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class DataPacketTypeString implements IDataType
{
	public String value;

	public DataPacketTypeString(String i)
	{
		this.value = i;
	}

	public DataPacketTypeString()
	{

	}

	@Override
	public String getName()
	{
		return "string";
	}

	@Override
	public String valueToString()
	{
		return value;
	}

	@Override
	public void setDefaultValue()
	{
		this.value = "";
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.value = n.getString("Value");
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();
		nbt.setString("Value", value);
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0xb86300;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 3;
	}
}