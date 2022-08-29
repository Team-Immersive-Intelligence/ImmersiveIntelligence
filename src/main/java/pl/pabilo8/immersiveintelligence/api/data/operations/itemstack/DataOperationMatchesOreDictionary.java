package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import blusunrize.immersiveengineering.common.util.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperationMatchesOreDictionary extends DataOperation
{
	public DataOperationMatchesOreDictionary()
	{
		//Checks whether the Itemstack matches a given oreDict value
		name = "matches_oredict";
		allowedTypes = new Class[]{DataTypeItemStack.class, DataTypeString.class};
		params = new String[]{"stack","ore_dictionary"};
		expectedResult = DataTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;
		DataTypeString t2;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		//Yes
		return new DataTypeBoolean(Utils.compareToOreName(t1.value, t2.value));
	}
}
