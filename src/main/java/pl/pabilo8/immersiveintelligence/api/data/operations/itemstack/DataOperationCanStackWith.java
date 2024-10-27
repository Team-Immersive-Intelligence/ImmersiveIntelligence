package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "can_stack_with",
		allowedTypes = {DataTypeItemStack.class, DataTypeItemStack.class}, params = {"stack", "compared"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationCanStackWith extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1, t2;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(1));

		//Yes
		return new DataTypeBoolean(ItemStack.areItemStacksEqual(t1.value, t2.value));
	}
}
