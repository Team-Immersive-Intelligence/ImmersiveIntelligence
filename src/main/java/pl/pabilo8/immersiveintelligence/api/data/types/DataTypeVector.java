package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 26.08.2022
 */
public class DataTypeVector implements IDataType
{
	public float x, y, z;
	public boolean integerVector;

	public DataTypeVector(float x, float y, float z)
	{
		this.integerVector = false;
	}

	public DataTypeVector(int x, int y, int z)
	{
		this.integerVector = true;
	}

	public DataTypeVector()
	{

	}

	@Nonnull
	@Override
	public String getName()
	{
		return "vector";
	}

	@Nonnull
	@Override
	public String[][] getTypeInfoTable()
	{
		return new String[][]{
				{"ie.manual.entry.def_value", "[0.0, 0.0, 0.0]"},
				{"ie.manual.entry.min_value", String.valueOf(Float.MIN_VALUE)},
				{"ie.manual.entry.max_value", String.valueOf(Float.MAX_VALUE)}
		};
	}

	@Nonnull
	@Override
	public String valueToString()
	{
		return String.format("[%s,%s,%s]", x, y, z);
	}

	@Override
	public void setDefaultValue()
	{
		x = y = z = 0;
	}

	@Override
	public void valueFromNBT(NBTTagCompound n)
	{
		this.x = n.getFloat("X");
		this.y = n.getFloat("Y");
		this.z = n.getFloat("Z");
		this.integerVector = n.getBoolean("IntegerVector");
	}

	@Nonnull
	@Override
	public NBTTagCompound valueToNBT()
	{
		NBTTagCompound nbt = getHeaderTag();

		nbt.setFloat("X", x);
		nbt.setFloat("Y", x);
		nbt.setFloat("Z", x);
		nbt.setBoolean("IntegerVector", integerVector);

		return nbt;
	}

	@Override
	public int getTypeColour()
	{
		return 0x9d8900;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof DataTypeVector))
			return false;

		DataTypeVector other = (DataTypeVector)obj;
		return other.x==x&&other.y==y&&other.z==z;
	}
}
