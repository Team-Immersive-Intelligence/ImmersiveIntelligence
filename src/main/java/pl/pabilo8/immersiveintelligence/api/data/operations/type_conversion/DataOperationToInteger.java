package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "to_integer", allowedTypes = {DataType.class}, expression = "<integer>", params = {"casted"}, expectedResult = DataTypeInteger.class)
public class DataOperationToInteger extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType f;
		if(data.getArgument(0) instanceof DataTypeAccessor)
			f = ((DataTypeAccessor)data.getArgument(0)).getRealValue(packet);
		else
			f = data.getArgument(0);

		int e = 0;
		try
		{
			e = Integer.parseInt(packet.getVarInType(DataType.class, f).toString());
		} catch(NumberFormatException ignored)
		{

		}

		return new DataTypeInteger(e);
	}
}
