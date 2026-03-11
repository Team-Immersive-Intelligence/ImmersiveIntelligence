package pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.util.IIStringUtil;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2019
 */
@DataOperation.DataOperationMeta(name = "to_float", allowedTypes = {DataType.class}, expression = "<float>", params = {"casted"}, expectedResult = DataTypeFloat.class)
public class DataOperationToFloat extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType f = packet.evaluateVariable(data.getArgument(0), false);
		return new DataTypeFloat(IIStringUtil.parseFloat(packet.getVarInType(DataType.class, f).toString()));
	}
}
