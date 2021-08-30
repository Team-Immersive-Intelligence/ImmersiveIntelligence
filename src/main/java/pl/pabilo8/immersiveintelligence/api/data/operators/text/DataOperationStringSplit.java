package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringSplit extends DataOperator
{
	public DataOperationStringSplit()
	{
		//A boolean version of the 'equals' operation
		name = "string_split";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeString.class;
		expectedResult = DataPacketTypeArray.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		IDataType t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		IDataType t2 = getVarInType(DataPacketTypeString.class, data.getType2(), packet);

		//Lambdas are love, Lambdas are life!
		return new DataPacketTypeArray(Arrays.stream(t1.valueToString().split(t2.valueToString()))
				.map(DataPacketTypeString::new)
				.toArray(DataPacketTypeString[]::new));
	}
}
