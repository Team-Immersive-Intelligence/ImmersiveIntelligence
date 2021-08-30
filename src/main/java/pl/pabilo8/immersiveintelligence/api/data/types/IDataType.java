package pl.pabilo8.immersiveintelligence.api.data.types;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
@MethodsReturnNonnullByDefault
public interface IDataType
{
	/**
	 * @return data type name, should be snake_case
	 */
	String getName();

	/**
	 * @return value to string, if compound make it JSON styled, look at {@link NBTTagCompound#toString()}
	 */
	String valueToString();

	/**
	 * used to set default values, leave the variables unset for default
	 */
	void setDefaultValue();

	/**
	 * @param nbt to load values from
	 */
	void valueFromNBT(NBTTagCompound nbt);

	/**
	 * @return NBTTagCompound with variable's values
	 */
	NBTTagCompound valueToNBT();

	/**
	 * @return type color used by GUIs in rgbInt format, like in {@link net.minecraft.util.math.MathHelper#rgb(int, int, int)} or {@link net.minecraft.util.math.MathHelper#rgb(float, float, float)}
	 */
	int getTypeColour();

	/**
	 * @return texture location for GUI frames, *must* contain the file extension, II default is immersiveintelligence:textures/gui/data_types.png
	 */
	String textureLocation();

	/**
	 * @return frame offset number inside the texture file, see immersiveintelligence:textures/gui/data_types.png for reference
	 */
	int getFrameOffset();

	/**
	 * Sets header (type name) while saving the variable to NBT
	 * @return NBTTagCompound with header tag
	 */
	default NBTTagCompound getHeaderTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Type", getName());
		return tag;
	}
}
