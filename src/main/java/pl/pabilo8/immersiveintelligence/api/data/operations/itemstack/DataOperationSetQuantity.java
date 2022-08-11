package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSetQuantity extends DataOperation
{
	public DataOperationSetQuantity()
	{
		//Sets the quantity (count) of an itemstack
		name = "set_quantity";

		allowedTypes = new Class[]{DataTypeItemStack.class, DataTypeInteger.class};
		params = new String[]{"stack","count"};
		expectedResult = DataTypeItemStack.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;
		DataTypeInteger t2;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeInteger.class, data.getArgument(1));
		ItemStack stack = t1.value.copy();
		stack.setCount(t2.value);

		//Yes
		return new DataTypeItemStack(stack);
	}
}
