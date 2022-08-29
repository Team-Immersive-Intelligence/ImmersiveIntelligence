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
public class DataOperationGetQuantity extends DataOperation
{
	public DataOperationGetQuantity()
	{
		//Gets the quantity (count) of the itemstack
		name = "get_quantity";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"stack"};
		expectedResult = DataTypeInteger.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		ItemStack stack = t1.value;

		return new DataTypeInteger(stack.getCount());
	}
}
