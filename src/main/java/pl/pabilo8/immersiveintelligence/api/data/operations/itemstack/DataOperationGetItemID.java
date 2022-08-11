package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetItemID extends DataOperation
{
	public DataOperationGetItemID()
	{
		//Gets an itemstack's durability
		name = "get_item_id";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"stack"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		ItemStack stack = t1.value;

		return new DataTypeString(
				Optional.ofNullable(stack.getItem().getRegistryName())
						.orElse(new ResourceLocation("minecraft:air")).toString()
		);
	}
}
