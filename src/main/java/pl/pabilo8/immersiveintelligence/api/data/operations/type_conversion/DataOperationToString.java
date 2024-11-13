package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeAccessor;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "to_string", allowedTypes = {DataType.class}, expression = "<string>", params = {"casted"}, expectedResult = DataTypeString.class)
public class DataOperationToString extends DataOperation
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

		return new DataTypeString(packet.getVarInType(DataType.class, f).toString());
	}
}
