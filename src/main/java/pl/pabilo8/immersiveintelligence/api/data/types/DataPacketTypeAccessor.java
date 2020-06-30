package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataPacketTypeAccessor implements IDataType
{
	public char variable;

	public DataPacketTypeAccessor(char variable)
	{
		this.variable = variable;
	}

	public DataPacketTypeAccessor()
	{
	}

	@Override
	public String getName()
	{
		return "accessor";
	}

	@Override
	public String valueToString()
	{
		return "%"+variable+"%";
	}

	@Override
	public void setDefaultValue()
	{
		variable = 'a';
	}

	public IDataType getRealValue(DataPacket data)
	{
		return data.getPacketVariable(this.variable);
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.variable = n.getString("Variable").charAt(0);
	}

	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Type", "accessor");
		nbt.setString("Variable", String.valueOf(variable));
		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x161c26;
	}

	@Override
	public String textureLocation()
	{
		return ImmersiveIntelligence.MODID+":textures/gui/data_types.png";
	}

	@Override
	public int getFrameOffset()
	{
		return 5;
	}
}
