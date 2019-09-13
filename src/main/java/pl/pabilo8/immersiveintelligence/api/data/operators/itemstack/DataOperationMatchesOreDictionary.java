package pl.pabilo8.immersiveintelligence.api.data.operators.itemstack;

import blusunrize.immersiveengineering.common.util.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operators.DataOperator;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

/**
 * Created by Pabilo8 on 05-07-2019.
 */
public class DataOperationMatchesOreDictionary extends DataOperator
{
	public DataOperationMatchesOreDictionary()
	{
		//Checks whether the Itemstack matches a given oreDict value
		name = "matches_oredict";
		sign = "?ORE";
		allowedType1 = DataPacketTypeItemStack.class;
		allowedType2 = DataPacketTypeString.class;
		expectedResult = DataPacketTypeBoolean.class;
	}

	@Override
	public IDataType execute(DataPacket packet, DataPacketTypeExpression data)
	{
		DataPacketTypeItemStack t1;
		DataPacketTypeString t2;

		t1 = ((DataPacketTypeItemStack)getVarInType(DataPacketTypeItemStack.class, data.getType1(), packet));
		t2 = ((DataPacketTypeString)getVarInType(DataPacketTypeString.class, data.getType2(), packet));

		//Yes
		return new DataPacketTypeBoolean(Utils.compareToOreName(t1.value, t2.value));
	}
}
