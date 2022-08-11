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
public class DataOperationSetDurability extends DataOperation
{
	public DataOperationSetDurability()
	{
		//Sets the durability of an itemstack
		name = "set_durability";
		allowedTypes = new Class[]{DataTypeItemStack.class,DataTypeInteger.class};
		params = new String[]{"stack","damage"};
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
		stack.setItemDamage(t2.value);

		//Yes
		return new DataTypeItemStack(stack);
	}
}
