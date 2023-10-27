package pl.pabilo8.immersiveintelligence.api.data;

import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataTypeNumeric;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 06.10.2023
 */
public class DataHandlingUtils
{
	//--- Simply Getting Parameters ---//

	public static boolean asBoolean(char variable, DataPacket packet)
	{
		return packet.getVarInType(DataTypeBoolean.class, packet.getPacketVariable(variable)).value;
	}

	public static int asInt(char variable, DataPacket packet)
	{
		return packet.getVarInType(IDataTypeNumeric.class, packet.getPacketVariable(variable)).intValue();
	}

	public static float asFloat(char variable, DataPacket packet)
	{
		return packet.getVarInType(IDataTypeNumeric.class, packet.getPacketVariable(variable)).floatValue();
	}

	public static String asString(char variable, DataPacket packet)
	{
		return packet.getPacketVariable(variable).valueToString();
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
		boolean present = packet.getPacketVariable(variable) instanceof IDataTypeNumeric;
		if(present)
			ifPresent.accept(((IDataTypeNumeric)packet.getPacketVariable(variable)).floatValue());
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
	public static DataPacket handleCallback(DataPacket packet, Function<String, IDataType> mapper)
	{
		//Detect any callback strings and give responses to them in a new packet
		DataPacket sent = new DataPacket();
		for(Entry<Character, IDataType> entry : packet.variables.entrySet())
			if(entry.getKey()!='c'&&entry.getValue() instanceof DataTypeString)
			{
				IDataType reply = mapper.apply(entry.getValue().valueToString());
				if(reply!=null)
					sent.setVariable(entry.getKey(), reply);
			}

		//If there are no callback variables, return null
		return sent.hasAnyVariables()?sent: null;
	}
}
