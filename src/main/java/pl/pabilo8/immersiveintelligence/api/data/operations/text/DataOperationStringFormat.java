package pl.pabilo8.immersiveintelligence.api.data.operations.text;

import net.minecraft.util.text.TextFormatting;
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
public class DataOperationStringFormat extends DataOperation
{
	public DataOperationStringFormat()
	{
		//Applies TextFormatting to string
		name = "string_format";
		allowedTypes = new Class[]{DataTypeString.class, DataTypeInteger.class};
		params = new String[]{"text","formatting"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeInteger t2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));

		TextFormatting tf = TextFormatting.fromColorIndex(t2.value);
		if(tf==null)
			return new DataTypeString(t1.value);

		return new DataTypeString(tf+t1.value+TextFormatting.RESET);
	}
}
