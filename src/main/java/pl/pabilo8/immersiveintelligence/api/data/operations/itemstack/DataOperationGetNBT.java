package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetNBT extends DataOperation
{
	public DataOperationGetNBT()
	{
		//Gets itemstack NBT in a string form
		name = "get_nbt";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"stack"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;
		int i1;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		ItemStack stack = t1.value;
		NBTTagCompound tag;
		if(stack.hasTagCompound())
			tag = stack.getTagCompound();
		else
			tag = new NBTTagCompound();


		//Yes
		return new DataTypeString(tag.toString());
	}
}
