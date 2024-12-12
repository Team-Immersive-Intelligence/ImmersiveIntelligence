package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "item_set_nbt", allowedTypes = {DataTypeItemStack.class, DataTypeString.class}, params = {"stack", "nbt"}, expectedResult = DataTypeItemStack.class)
public class DataOperationItemStackSetNBT extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
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
