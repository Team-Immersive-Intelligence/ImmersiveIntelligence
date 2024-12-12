package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "itemstack_create", expression = "<itemstack>",
		allowedTypes = {DataTypeString.class, DataTypeInteger.class, DataTypeInteger.class}, params = {"item_id", "amount", "meta"},
		expectedResult = DataTypeItemStack.class)
public class DataOperationItemStackCreate extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		ItemStack stack = new ItemStack(Optional.ofNullable(Item.getByNameOrId(t1.value)).orElse(Items.AIR));

		if(!stack.isEmpty())
		{
			stack.setCount(packet.getVarInType(DataTypeInteger.class, data.getArgument(1)).value);
			stack.setItemDamage(packet.getVarInType(DataTypeInteger.class, data.getArgument(2)).value);
		}
		return new DataTypeItemStack(stack);
	}
}
