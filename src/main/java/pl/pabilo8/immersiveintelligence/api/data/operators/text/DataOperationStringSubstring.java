package pl.pabilo8.immersiveintelligence.api.data.operators.text;

import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringSubstring extends DataOperator
{
	public DataOperationStringSubstring()
	{
		//Gets a substring (given integer to the end)
		name = "string_substring";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeString t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);
		DataPacketTypeInteger t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);

		return new DataPacketTypeString(t1.valueToString().substring(MathHelper.clamp(t2.value,0,t1.value.length())));
	}
}
