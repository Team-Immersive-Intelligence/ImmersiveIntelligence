package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2019
 */
@DataOperation.DataOperationMeta(name = "to_integer", allowedTypes = {DataType.class}, expression = "<integer>", params = {"casted"}, expectedResult = DataTypeInteger.class)
public class DataOperationToInteger extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType f = packet.evaluateVariable(data.getArgument(0), false);
		if(f instanceof NumericDataType)
			return new DataTypeInteger(((NumericDataType)f).intValue());
		if(f instanceof DataTypeBoolean)
			return new DataTypeInteger(((DataTypeBoolean)f).value?1: 0);
		return new DataTypeInteger(IIStringUtil.parseInt(f.toString()));
	}
}
