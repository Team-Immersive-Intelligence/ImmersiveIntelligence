package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToInteger extends DataOperation
{
	public DataOperationToInteger()
	{
		name = "to_integer";
		expression = "<integer>";
		allowedTypes = new Class[]{IDataType.class};
		params = new String[]{"casted"};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType f;
		if(data.getArgument(0) instanceof DataTypeAccessor)
			f = ((DataTypeAccessor)data.getArgument(0)).getRealValue(packet);
		else
			f = data.getArgument(0);

		int e = 0;
		try
		{
			e = Integer.parseInt(packet.getVarInType(IDataType.class, f).valueToString());
		}
		catch(NumberFormatException ignored)
		{

		}

		return new DataTypeInteger(e);
	}
}
