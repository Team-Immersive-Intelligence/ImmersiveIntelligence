package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationStringTrim extends DataOperation
{
	public DataOperationStringTrim()
	{
		//Gets a substring (from 0 to given integer)
		name = "string_trim";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeInteger.class};
		params = new String[]{"text","to_index"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeInteger t2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		return new DataTypeString(t1.valueToString().substring(0, MathHelper.clamp(t2.value, 0, t1.value.length())));
	}
}
