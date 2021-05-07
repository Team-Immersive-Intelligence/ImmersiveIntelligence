package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSetNBT extends DataOperator
{
	public DataOperationSetNBT()
	{
		//Sets the NBT of an itemstack
		name = "set_nbt";
		sign = ">>NBT";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeString.class;
		expectedResult = DataPacketTypeItemStack.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1;
		DataPacketTypeString t2;

		t1 = ((DataPacketTypeItemStack)getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet));
		t2 = ((DataPacketTypeString)getVarInType(DataPacketTypeString.class, data.getType2(), packet));
		ItemStack stack = t1.value.copy();

		try
		{
			stack.setTagCompound(JsonToNBT.getTagFromJson(t2.value));
		} catch(NBTException e)
		{
			e.printStackTrace();
		}

		//Yes
		return new DataPacketTypeItemStack(stack);
	}
}
