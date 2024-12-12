package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 26.08.2022
 */
public class DataTypeVector extends DataType
{
	public float x = 0, y = 0, z = 0;
	public boolean integerVector;

	public DataTypeVector(float x, float y, float z)
	{
		this.integerVector = false;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public DataTypeVector(Vec3d vector)
	{
		this((float)vector.x, (float)vector.y, (float)vector.z);
	}

	public DataTypeVector(int x, int y, int z)
	{
		this.integerVector = true;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public DataTypeVector()
	{
		this.integerVector = false;
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
	public boolean equals(Object obj)
	{
		if(!(obj instanceof DataTypeVector))
			return false;

		DataTypeVector other = (DataTypeVector)obj;
		return other.x==x&&other.y==y&&other.z==z;
	}

	@Override
	public String toString()
	{
		return String.format("[%s,%s,%s]", x, y, z);
	}
}
