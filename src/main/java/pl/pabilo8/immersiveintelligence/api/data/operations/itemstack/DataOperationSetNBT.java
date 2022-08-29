package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationSetNBT extends DataOperation
{
	public DataOperationSetNBT()
	{
		//Sets the NBT of an itemstack
		name = "set_nbt";
		allowedTypes = new Class[]{DataTypeItemStack.class, DataTypeString.class};
		params = new String[]{"stack","nbt"};
		expectedResult = DataTypeItemStack.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;
		DataTypeString t2;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		ItemStack stack = t1.value.copy();

		try
		{
			stack.setTagCompound(JsonToNBT.getTagFromJson(t2.value));
		} catch(NBTException e)
		{
			e.printStackTrace();
		}

		//Yes
		return new DataTypeItemStack(stack);
	}
}
