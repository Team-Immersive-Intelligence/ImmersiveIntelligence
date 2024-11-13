package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "to_float", allowedTypes = {DataType.class}, expression = "<float>", params = {"casted"}, expectedResult = DataTypeFloat.class)
public class DataOperationToFloat extends DataOperation
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

		float e = 0;
		try
		{
			e = Float.parseFloat(packet.getVarInType(DataType.class, f).toString());
		} catch(NumberFormatException ignored)
		{

		}

		return new DataTypeFloat(e);
	}
}
