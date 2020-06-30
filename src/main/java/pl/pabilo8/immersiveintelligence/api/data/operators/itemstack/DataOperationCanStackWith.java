package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationCanStackWith extends DataOperator
{
	public DataOperationCanStackWith()
	{
		//Whether the two itemstacks are compatible
		name = "can_stack_with";
		sign = ">S<";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeItemStack.class;
		expectedResult = DataPacketTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1, t2;

		t1 = ((DataPacketTypeItemStack)getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet));
		t2 = ((DataPacketTypeItemStack)getVarInType(DataPacketTypeItemStack.class, data.getType2(), packet));

		//Yes
		return new DataPacketTypeBoolean(ItemStack.areItemStacksEqual(t1.value, t2.value));
	}
}
