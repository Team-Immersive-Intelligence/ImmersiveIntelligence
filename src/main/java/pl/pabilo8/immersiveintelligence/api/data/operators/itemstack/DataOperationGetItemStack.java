package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationGetItemStack extends DataOperator
{
	public DataOperationGetItemStack()
	{
		//Gets an itemstack's durability
		name = "get_itemstack";
		sign = "";
		allowedType1 = DataPacketTypeString.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeItemStack.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeString t1 = getVarInType(DataPacketTypeString.class, data.getType1(), packet);

		return new DataPacketTypeItemStack(
				new ItemStack(Optional.ofNullable(Item.getByNameOrId(t1.value))
						.orElse(Items.AIR))
		);
	}
}
