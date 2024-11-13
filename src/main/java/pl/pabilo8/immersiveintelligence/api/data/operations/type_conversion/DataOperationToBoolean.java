package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "to_boolean", allowedTypes = {DataType.class}, params = {"casted"}, expression = "<boolean>", expectedResult = DataTypeBoolean.class)
public class DataOperationToBoolean extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType type;
		if(data.getArgument(0) instanceof DataTypeAccessor)
			type = ((DataTypeAccessor)data.getArgument(0)).getRealValue(packet);
		else
			type = data.getArgument(0);

		if(type instanceof DataTypeBoolean)
			return type;
		else if(type instanceof DataTypeString)
			return new DataTypeBoolean(((DataTypeString)type).value.equalsIgnoreCase("TRUE"));
		else if(type instanceof DataTypeInteger)
			return new DataTypeBoolean(((DataTypeInteger)type).value > 0);

		return new DataTypeBoolean(false);
	}
}
