package pl.pabilo8.immersiveintelligence.api.data.types;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public interface IDataType
{
	String getName();

	String valueToString();

	void setDefaultValue();

	void valueFromNBT(NBTTagCompound nbt);

	NBTTagCompound valueToNBT();

	int getTypeColour();

	String textureLocation();

	int getFrameOffset();
}
