package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToString extends DataOperation
{
	public DataOperationToString()
	{
		//A boolean version of the 'equals' operation
		name = "to_string";
		expression = "<string>";
		allowedTypes = new Class[]{IDataType.class};
		params = new String[]{"stringified"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType f;
		if(data.getArgument(0) instanceof DataTypeAccessor)
			f = ((DataTypeAccessor)data.getArgument(0)).getRealValue(packet);
		else
			f = data.getArgument(0);

		return new DataTypeString(packet.getVarInType(IDataType.class, f).valueToString());
	}
}
