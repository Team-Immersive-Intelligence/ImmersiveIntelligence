package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataTypeAccessor implements IDataType
{
	public char variable;

	public DataTypeAccessor(char variable)
	{
		this.variable = variable;
	}

	public DataTypeAccessor()
	{
	}

	@Nonnull
	@Override
	public String getName()
	{
		return "accessor";
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		return "@"+variable;
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

	@Nonnull
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
	public boolean equals(Object obj)
	{
		return obj instanceof DataTypeAccessor&&((DataTypeAccessor)obj).variable==variable;
	}
}
