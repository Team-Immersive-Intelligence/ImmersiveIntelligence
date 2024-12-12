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
@DataOperation.DataOperationMeta(name = "fluid_set_amount",
		allowedTypes = {DataTypeFluidStack.class, DataTypeInteger.class}, params = {"stack", "amount"},
		expectedResult = DataTypeFluidStack.class)
public class DataOperationFluidSetAmount extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeFluidStack fs = packet.getVarInType(DataTypeFluidStack.class, data.getArgument(0));
		DataTypeInteger amount = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));
		if(fs.value!=null)
			fs.value.amount = amount.value;
		return fs;
	}
}
