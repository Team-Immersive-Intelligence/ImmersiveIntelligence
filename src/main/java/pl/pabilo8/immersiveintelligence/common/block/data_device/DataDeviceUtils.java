package pl.pabilo8.immersiveintelligence.common.block.data_device;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

/**
 * Provides bounded configuration readers and exact value matching for small data devices.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public final class DataDeviceUtils
{
	public static final int MAX_RULES = 64;
	public static final int MAX_TICKS = 12096000;

	private DataDeviceUtils()
	{
	}

	public static char readLetter(NBTTagCompound nbt, String key)
	{
		String value = nbt.getString(key);
		return value.length()==1&&DataPacket.isValidVariable(value.charAt(0))?value.charAt(0): 'a';
	}

	public static DataType readValue(NBTTagCompound nbt, String key)
	{
		try
		{
			return IIDataTypeUtils.getVarFromNBT(nbt.getCompoundTag(key));
		} catch(RuntimeException exception)
		{
			return new DataTypeNull();
		}
	}

	public static boolean matches(DataType actual, DataType expected)
	{
		return actual.getClass()==expected.getClass()&&actual.valueToNBT().equals(expected.valueToNBT());
	}

	public static NBTTagList boundedList(NBTTagCompound nbt, String key)
	{
		NBTTagList source = nbt.getTagList(key, 10);
		NBTTagList result = new NBTTagList();
		for(int i = 0; i < Math.min(source.tagCount(), MAX_RULES); i++)
			result.appendTag(source.getCompoundTagAt(i).copy());
		return result;
	}
}
