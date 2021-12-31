package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetItemID extends DataOperator
{
	public DataOperationGetItemID()
	{
		//Gets an itemstack's durability
		name = "get_item_id";
		sign = "";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1;

		t1 = getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet);
		ItemStack stack = t1.value;

		return new DataPacketTypeString(
				Optional.ofNullable(stack.getItem().getRegistryName())
						.orElse(new ResourceLocation("minecraft:air")).toString()
		);
	}
}
