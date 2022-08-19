package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationToFloat extends DataOperation
{
	public DataOperationToFloat()
	{
		name = "to_float";
		expression = "<float>";
		allowedTypes = new Class[]{IDataType.class};
		params = new String[]{"casted"};
		expectedResult = DataTypeFloat.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType f;
		if(data.getArgument(0) instanceof DataTypeAccessor)
			f = ((DataTypeAccessor)data.getArgument(0)).getRealValue(packet);
		else
			f = data.getArgument(0);

		float e = 0;
		try
		{
			e = Float.parseFloat(packet.getVarInType(IDataType.class, f).valueToString());
		}
		catch(NumberFormatException ignored)
		{

		}

		return new DataTypeFloat(e);
	}
}
