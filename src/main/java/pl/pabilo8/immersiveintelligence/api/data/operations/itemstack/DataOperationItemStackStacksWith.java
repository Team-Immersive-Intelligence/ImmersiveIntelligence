package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraftforge.items.ItemHandlerHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.07.2019
 */
@DataOperation.DataOperationMeta(name = "item_stacks_with",
		allowedTypes = {DataTypeItemStack.class, DataTypeItemStack.class}, params = {"stack", "with"},
		expectedResult = DataTypeBoolean.class)
public class DataOperationItemStackStacksWith extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		DataTypeItemStack t2 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(1));
		//Yes

		return new DataTypeBoolean(ItemHandlerHelper.canItemStacksStackRelaxed(t1.value, t2.value));
	}
}
