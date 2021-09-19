package pl.pabilo8.immersiveintelligence.api.data.operators;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public abstract class DataOperator
{
	public String name, sign;
	public Class<? extends IDataType> allowedType1, allowedType2;
	public Class<? extends IDataType> expectedResult;

	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		return new DataPacketTypeNull();
	}

	@Nonnull
	public static <T extends IDataType> T getVarInType(Class<T> preferred, @Nullable IDataType actual, DataPacket packet)
	{
		IDataType type;
		if(actual instanceof DataPacketTypeAccessor)
			type = ((DataPacketTypeAccessor)actual).getRealValue(packet);
		else
			type = actual;

		if(preferred.isInstance(type))
		{
			return (T)type;
		}
		else
		{
			try
			{
				IDataType p = preferred.newInstance();
				p.setDefaultValue();
				return preferred.cast(p);
			} catch(InstantiationException|IllegalAccessException e)
			{
				e.printStackTrace();
			}
			//This shouldn't be there.
			//If you get this you really messed up something.
			return null;
		}
	}
}
