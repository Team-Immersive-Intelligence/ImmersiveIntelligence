package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "item_is_empty", allowedTypes = {DataTypeItemStack.class}, params = {"stack"}, expectedResult = DataTypeBoolean.class)
public class DataOperationItemStackIsEmpty extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		return new DataTypeBoolean(packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value.isEmpty());
	}
}
