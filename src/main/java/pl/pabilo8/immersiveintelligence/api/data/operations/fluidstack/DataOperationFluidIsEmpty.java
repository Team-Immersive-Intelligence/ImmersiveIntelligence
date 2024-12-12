package pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2024
 **/
@DataOperation.DataOperationMeta(name = "fluid_is_empty",
		allowedTypes = {DataTypeFluidStack.class}, params = {"stack"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationFluidIsEmpty extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeFluidStack fs = packet.getVarInType(DataTypeFluidStack.class, data.getArgument(0));
		return new DataTypeBoolean(fs.value!=null&&fs.value.amount > 0);
	}
}
