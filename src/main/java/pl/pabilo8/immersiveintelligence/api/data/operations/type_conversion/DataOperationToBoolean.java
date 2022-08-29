package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToBoolean extends DataOperation
{
	public DataOperationToBoolean()
	{
		name = "to_boolean";
		expression = "<boolean>";
		allowedTypes = new Class[]{IDataType.class};
		params = new String[]{"casted"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType type;
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
