package pl.pabilo8.immersiveintelligence.api.data.operations.document;

import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.item.ItemStack;
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
public class DataOperationDocumentGetAuthor extends DataOperation
{
	public DataOperationDocumentGetAuthor()
	{
		name = "document_get_author";
		allowedTypes = new Class[]{DataTypeItemStack.class};
		params = new String[]{"document"};
		expectedResult = DataTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		ItemStack stack = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0)).value;
		return new DataTypeString(ItemNBTHelper.getString(stack,"author"));
	}
}
