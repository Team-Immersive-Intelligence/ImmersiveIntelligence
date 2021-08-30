package pl.pabilo8.immersiveintelligence.api.data.operators.document;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationDocumentReadPage extends DataOperator
{
	public DataOperationDocumentReadPage()
	{
		//A boolean version of the 'equals' operation
		name = "document_read_page";
		sign = "";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeInteger.class;
		expectedResult = DataPacketTypeString.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1 = getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet);
		DataPacketTypeInteger t2 = getVarInType(DataPacketTypeInteger.class, data.getType2(), packet);

		return new DataPacketTypeString("");
	}
}
