package pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFluidStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 12.12.2024
 **/
@DataOperation.DataOperationMeta(name = "fluid_set_nbt",
		allowedTypes = {DataTypeFluidStack.class, DataTypeString.class}, params = {"stack", "nbt"},
		expectedResult = DataTypeFluidStack.class)
public class DataOperationFluidSetNBT extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeFluidStack fs = packet.getVarInType(DataTypeFluidStack.class, data.getArgument(0));
		DataTypeString nbt = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		if(fs.value!=null&&!nbt.value.isEmpty())
			fs.value.tag = EasyNBT.parseNBT(nbt.value);
		return fs;
	}
}
