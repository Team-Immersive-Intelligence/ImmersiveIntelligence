package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationIsStackEmpty extends DataOperation
{
	public DataOperationIsStackEmpty()
	{
		name = "is_stack_empty";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"stack"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value.isEmpty());
	}
}
