package pl.pabilo8.immersiveintelligence.api.data.operators.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentGetAuthor extends DataOperator
{
	public DataOperationDocumentGetAuthor()
	{
		name = "document_get_author";
		sign = "";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeNull.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		ItemStack stack = getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet).value;
		return new DataPacketTypeString(ItemNBTHelper.getString(stack,"author"));
	}
}
