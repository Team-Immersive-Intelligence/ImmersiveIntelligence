package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import blusunrize.immersiveengineering.common.util.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "matches_oredict", allowedTypes = {DataTypeItemStack.class, DataTypeString.class}, params = {"stack", "ore_dictionary"}, expectedResult = DataTypeBoolean.class)
public class DataOperationMatchesOreDictionary extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1;
		DataTypeString t2;

		t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		//Yes
		return new DataTypeBoolean(Utils.compareToOreName(t1.value, t2.value));
	}
}
