package pl.pabilo8.immersiveintelligence.api.data;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.api.LogisticTag;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType.TypeMetaInfo;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
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
		return packet.getVarInType(DataTypeBoolean.class, packet.get(variable)).value;
	}

	public static int asInt(char variable, DataPacket packet)
	{
		return packet.getVarInType(NumericDataType.class, packet.get(variable)).intValue();
	}

	public static float asFloat(char variable, DataPacket packet)
	{
		return packet.getVarInType(NumericDataType.class, packet.get(variable)).floatValue();
	}

	public static String asString(char variable, DataPacket packet)
	{
		return packet.get(variable).toString();
	}

	@Nullable
	public static <T extends Enum<T> & ISerializableEnum> T asEnum(char variable, DataPacket packet, Class<T> e)
	{
		boolean present = packet.get(variable) instanceof DataTypeString;
		if(present)
		{
			String name = ((DataTypeString)packet.get(variable)).value;
			try
			{
				return T.valueOf(e, name.toUpperCase());
			} catch(IllegalArgumentException|NullPointerException exception)
			{
				return null;
			}
		}
		return null;
	}

	public static IngredientStack asIngredient(char variable, DataPacket packet)
	{
		return ingredientFromData(packet.get(variable));
	}

	//--- Optional ---//

	public static Optional<Boolean> optionalBoolean(char variable, DataPacket packet)
	{
		DataType data = packet.get(variable);
		if(data instanceof DataTypeBoolean)
			return Optional.of(((DataTypeBoolean)data).value);
		return Optional.empty();
	}

	public static Optional<Integer> optionalInt(char variable, DataPacket packet)
	{
		DataType data = packet.get(variable);
		if(data instanceof NumericDataType)
			return Optional.of(((NumericDataType)data).intValue());
		return Optional.empty();
	}

	public static Optional<Float> optionalFloat(char variable, DataPacket packet)
	{
		DataType data = packet.get(variable);
		if(data instanceof NumericDataType)
			return Optional.of(((NumericDataType)data).floatValue());
		return Optional.empty();
	}

	public static Optional<String> optionalString(char variable, DataPacket packet)
	{
		DataType data = packet.get(variable);
		if(data instanceof DataTypeString)
			return Optional.of(((DataTypeString)data).value);
		return Optional.empty();
	}

	public static Optional<DataTypeEntity> optionalEntity(char variable, DataPacket packet)
	{
		DataType entityData = packet.get(variable);
		if(entityData instanceof DataTypeEntity)
			return Optional.of((DataTypeEntity)entityData);
		return Optional.empty();
	}

	public static Optional<IIColor> optionalColor(char variable, DataPacket packet)
	{
		DataType colorData = packet.get(variable);
		if(colorData instanceof DataTypeInteger)
			return Optional.of(IIColor.fromPackedRGB(MathHelper.clamp(
					asInt(variable, packet), 0, 0xffffff)));
		else if(colorData instanceof DataTypeString)
		{
			String string = asString(variable, packet).toLowerCase();
			//Try to match TextFormatting first
			for(TextFormatting tf : TextFormatting.values())
				if(tf.getFriendlyName().equals(string))
					return Optional.of(IIColor.fromTextFormatting(tf));
			//Or dye color
			for(EnumDyeColor dye : EnumDyeColor.values())
				if(dye.getUnlocalizedName().equals(string))
					return Optional.of(IIColor.fromDye(dye));
			//Otherwise treat it as a hex color
			return Optional.of(IIColor.fromHex(string));
		}
		return Optional.empty();
	}

	public static Optional<LogisticTag> optionalLogisticTag(char variable, DataPacket packet)
	{
		DataType data = packet.get(variable);
		if(data instanceof DataTypeLogisticTag)
			return Optional.of(((DataTypeLogisticTag)data).value);
		return Optional.empty();
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
		boolean present = packet.get(variable) instanceof NumericDataType;
		if(present)
			ifPresent.accept(((NumericDataType)packet.get(variable)).floatValue());
		return present;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static boolean expectingIntegerParam(char variable, DataPacket packet, Consumer<Integer> ifPresent)
	{
		boolean present = packet.get(variable) instanceof NumericDataType;
		if(present)
			ifPresent.accept(((NumericDataType)packet.get(variable)).intValue());
		return present;
	}

	/**
	 * Checks for a vector parameter in the packet, either as a single vector variable, three separate numeric variables, or yaw+pitch variables.
	 *
	 * @param packet   the packet to check
	 * @param vector   consumer for the vector parameter, can be null to avoid checking
	 * @param yawPitch consumer for the yaw and pitch parameters, can be null to avoid checking
	 * @return true if the packet contains a valid vector or yaw+pitch parameter, false otherwise
	 */
	public static boolean expectingVectorParam(DataPacket packet, @Nullable Consumer<Vec3d> vector, @Nullable Consumer<Vec2f> yawPitch)
	{
		if(vector!=null)
		{
			DataType v = packet.get('v');
			//vector data type option
			if(v instanceof DataTypeVector)
			{
				DataTypeVector casted = (DataTypeVector)v;
				vector.accept(new Vec3d(
						(int)casted.x,
						(int)casted.y,
						(int)casted.z
				));
				return true;
			}
			//3 numeric variables option
			else if(packet.has('x', 'y', 'z'))
			{
				DataType x = packet.get('x');
				DataType y = packet.get('y');
				DataType z = packet.get('z');
				if(x instanceof NumericDataType&&y instanceof NumericDataType&&z instanceof NumericDataType)
				{
					vector.accept(new Vec3d(
							((NumericDataType)x).floatValue(),
							((NumericDataType)y).floatValue(),
							((NumericDataType)z).floatValue()
					));
					return true;
				}
			}
		}
		//Checking for yaw and pitch parameters
		if(yawPitch!=null&&packet.has('y', 'p'))
		{
			DataType y = packet.get('y');
			DataType p = packet.get('p');
			if(y instanceof NumericDataType&&p instanceof NumericDataType)
			{
				yawPitch.accept(new Vec2f(
						//yaw
						((NumericDataType)y).floatValue(),
						//pitch
						((NumericDataType)p).floatValue()
				));
				return true;
			}
		}
		return false;
	}

	/**
	 * @param variable  variable index in packet
	 * @param packet    packet
	 * @param ifPresent performed if variable exists in packet and matches type
	 * @return whether ifPresent was performed
	 */
	public static boolean expectingBooleanParam(char variable, DataPacket packet, Consumer<Boolean> ifPresent)
	{
		boolean present = packet.get(variable) instanceof DataTypeBoolean;
		if(present)
			ifPresent.accept(((DataTypeBoolean)packet.get(variable)).value);
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
		boolean present = packet.get(variable) instanceof DataTypeString;
		if(present)
			ifPresent.accept(((DataTypeString)packet.get(variable)).value);
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
		boolean present = packet.get(variable) instanceof DataTypeString;
		if(present)
		{
			String name = ((DataTypeString)packet.get(variable)).value;
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
		boolean present = packet.get(variable) instanceof DataTypeString;
		if(present)
		{
			T found = mapping.apply(((DataTypeString)packet.get(variable)).value);
			if(found!=null)
				ifPresent.accept(found);
			else
				present = false;
		}
		return present;
	}

	//--- Callback ---//

	public static boolean isCallbackPacket(DataPacket packet)
	{
		return packet.get('c').toString().equals("callback");
	}

	@Nullable
	public static DataPacket handleCallback(DataPacket packet, Function<String, DataType> mapper)
	{
		//Detect any callback strings and give responses to them in a new packet
		DataPacket sent = new DataPacket();
		packet.forEach((name, value) -> {
			if(name!='c'&&value instanceof DataTypeString)
			{
				DataType reply = mapper.apply(value.toString());
				if(reply!=null)
					sent.set(name, reply);
			}
		});

		//If there are no callback variables, return null
		return sent.isEmpty()?sent: null;
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

	public static IngredientStack ingredientFromData(DataType dataType)
	{
		if(dataType instanceof DataTypeItemStack)
			return new IngredientStack((((DataTypeItemStack)dataType).value.copy()));
		else if(dataType instanceof DataTypeString)
			return new IngredientStack(dataType.toString());
		else
			return new IngredientStack("*");
	}
}
