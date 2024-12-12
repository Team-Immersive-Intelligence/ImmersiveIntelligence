package pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2024
 **/
@DataOperation.DataOperationMeta(name = "fluidstack_create", expression = "<fluidstack>",
		allowedTypes = {DataTypeString.class, DataTypeInteger.class, DataTypeString.class}, params = {"fluid_id", "amount", "nbt"},
		expectedResult = DataTypeItemStack.class)
public class DataOperationFluidCreate extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		String value = packet.getVarInType(DataTypeString.class, data.getArgument(0)).value;
		int amount = packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value;
		String nbt = packet.getVarInType(DataTypeString.class, data.getArgument(2)).value;
		return new DataTypeFluidStack(value, amount, nbt);
	}
}
