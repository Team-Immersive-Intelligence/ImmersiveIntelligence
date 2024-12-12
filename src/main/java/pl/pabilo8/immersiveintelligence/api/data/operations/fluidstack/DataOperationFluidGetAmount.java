package pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2024
 **/
@DataOperation.DataOperationMeta(name = "fluid_get_amount",
		allowedTypes = {DataTypeFluidStack.class}, params = {"stack"},
		expectedResult = DataTypeInteger.class)
public class DataOperationFluidGetAmount extends DataOperation
{

	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeFluidStack fs = packet.getVarInType(DataTypeFluidStack.class, data.getArgument(0));
		return new DataTypeInteger(fs.value==null?0: fs.value.amount);
	}
}
