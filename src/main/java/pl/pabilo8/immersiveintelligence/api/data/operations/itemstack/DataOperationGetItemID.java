package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "get_item_id",
		allowedTypes = {DataTypeItemStack.class}, params = {"stack"},
		expectedResult = DataTypeString.class)
public class DataOperationGetItemID extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
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
