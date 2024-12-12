package pl.pabilo8.immersiveintelligence.api.data.operations.vector;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeVector;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.11.2024
 **/
@DataOperation.DataOperationMeta(name = "vector_create", expression = "<vector>",
		allowedTypes = {NumericDataType.class, NumericDataType.class, NumericDataType.class}, params = {"x", "y", "z"},
		expectedResult = DataTypeVector.class)
public class DataOperationVectorCreate extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		NumericDataType t1 = packet.getVarInType(NumericDataType.class, data.getArgument(0));
		NumericDataType t2 = packet.getVarInType(NumericDataType.class, data.getArgument(1));
		NumericDataType t3 = packet.getVarInType(NumericDataType.class, data.getArgument(2));

		//Integer vector
		if(t1 instanceof DataTypeInteger&&t2 instanceof DataTypeInteger&&t3 instanceof DataTypeInteger)
			return new DataTypeVector(t1.intValue(), t2.intValue(), t3.intValue());
		else
			return new DataTypeVector(t1.floatValue(), t2.floatValue(), t3.floatValue());
	}
}
