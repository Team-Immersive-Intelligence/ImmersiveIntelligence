package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSetDurability extends DataOperator
{
	public DataOperationSetDurability()
	{
		//Sets the durability of an itemstack
		name = "set_durability";
		sign = ">>D";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeItemStack.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1;
		DataPacketTypeInteger t2;
		int i1;

		t1 = ((DataPacketTypeItemStack)getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet));
		t2 = ((DataPacketTypeInteger)getVarInType(DataPacketTypeInteger.class, data.getType2(), packet));
		ItemStack stack = t1.value;
		stack.setItemDamage(t2.value);

		//Yes
		return new DataPacketTypeItemStack(stack);
	}
}
