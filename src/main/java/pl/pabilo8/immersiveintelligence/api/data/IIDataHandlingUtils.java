package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 28.08.2024
 */
public class IIDataHandlingUtils
{
	//--- Meta Information ---//

	@SuppressWarnings("unchecked")
	public static <T extends DataType> TypeMetaInfo<T> getTypeMeta(@Nonnull Class<T> klass)
	{
		return (TypeMetaInfo<T>)IIDataTypeUtils.metaTypesByClass.get(klass);
	}

	//--- Simply Getting Parameters ---//

	public static boolean asBoolean(char variable, DataPacket packet)
	{
		return packet.getVarInType(DataTypeBoolean.class, packet.getPacketVariable(variable)).value;
	}

	public static int asInt(char variable, DataPacket packet)
	{
		return packet.getVarInType(NumericDataType.class, packet.getPacketVariable(variable)).intValue();
	}

	public static float asFloat(char variable, DataPacket packet)
	{
		return packet.getVarInType(NumericDataType.class, packet.getPacketVariable(variable)).floatValue();
	}

	public static String asString(char variable, DataPacket packet)
	{
		return packet.getPacketVariable(variable).toString();
	}

	//--- IfPresent Parameters ---//

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static boolean expectingNumericParam(char variable, DataPacket packet, Consumer<Float> ifPresent)
	{
		boolean present = packet.getPacketVariable(variable) instanceof NumericDataType;
		if(present)
			ifPresent.accept(((NumericDataType)packet.getPacketVariable(variable)).floatValue());
		return present;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static boolean expectingBooleanParam(char variable, DataPacket packet, Consumer<Boolean> ifPresent)
	{
		boolean present = packet.getPacketVariable(variable) instanceof DataTypeBoolean;
		if(present)
			ifPresent.accept(((DataTypeBoolean)packet.getPacketVariable(variable)).value);
		return present;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static boolean expectingStringParam(char variable, DataPacket packet, Consumer<String> ifPresent)
	{
		boolean present = packet.getPacketVariable(variable) instanceof DataTypeString;
		if(present)
			ifPresent.accept(((DataTypeString)packet.getPacketVariable(variable)).value);
		return present;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param e         enum class
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static <T extends Enum<T> & ISerializableEnum> boolean expectingEnumParam(char variable, DataPacket packet, Class<T> e, Consumer<T> ifPresent)
	{
		boolean present = packet.getPacketVariable(variable) instanceof DataTypeString;
		if(present)
		{
			String name = ((DataTypeString)packet.getPacketVariable(variable)).value;
			try
			{
				T found = T.valueOf(e, name.toUpperCase());
				ifPresent.accept(found);
			} catch(IllegalArgumentException|NullPointerException exception)
			{
				return false;
			}
		}
		return present;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @param mapping   function that maps the name into enum constants
	 * @return whether ifPresent was performed
	 */
	public static <T extends Enum<T> & ISerializableEnum> boolean expectingEnumParam(char variable, DataPacket packet, Function<String, T> mapping, Consumer<T> ifPresent)
	{
		boolean present = packet.getPacketVariable(variable) instanceof DataTypeString;
		if(present)
		{
			T found = mapping.apply(((DataTypeString)packet.getPacketVariable(variable)).value);
			if(found!=null)
				ifPresent.accept(found);
			else
				present = false;
		}
		return present;
	}

	//--- Callback ---//

	@Nullable
	public static DataPacket handleCallback(DataPacket packet, Function<String, DataType> mapper)
	{
		//Detect any callback strings and give responses to them in a new packet
		DataPacket sent = new DataPacket();
		for(Entry<Character, DataType> entry : packet.variables.entrySet())
			if(entry.getKey()!='c'&&entry.getValue() instanceof DataTypeString)
			{
				DataType reply = mapper.apply(entry.getValue().toString());
				if(reply!=null)
					sent.setVariable(entry.getKey(), reply);
			}

		//If there are no callback variables, return null
		return sent.hasAnyVariables()?sent: null;
	}

	//--- Sending ---//

	/**
	 * Sends a {@link DataPacket} to an adjacent device or connector
	 *
	 * @param packet the packet to send
	 * @param world  the world the sending device is in
	 * @param pos    the position of the sending device
	 * @param facing the direction for receiver position offset
	 */
	public static boolean sendPacketAdjacently(DataPacket packet, World world, BlockPos pos, EnumFacing facing)
	{
		BlockPos off = pos.offset(facing);
		//Checking if the position is loaded
		if(!world.isBlockLoaded(off))
			return false;
		TileEntity te = world.getTileEntity(off);

		//Sending directly to a device
		if(te instanceof IDataDevice)
		{
			((IDataDevice)te).onReceive(packet.clone(), facing.getOpposite());
			return true;
		}
		//Sending to a wire network
		else if(te instanceof IDataConnector)
		{
			((IDataConnector)te).sendPacket(packet.clone());
			return true;
		}
		return false;
	}
}
