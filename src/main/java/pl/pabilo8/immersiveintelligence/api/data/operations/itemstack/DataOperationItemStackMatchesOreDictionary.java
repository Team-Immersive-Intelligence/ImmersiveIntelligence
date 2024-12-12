package pl.pabilo8.immersiveintelligence.api.data.operations.itemstack;

import blusunrize.immersiveengineering.common.util.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
@DataOperation.DataOperationMeta(name = "item_matches_oredict", allowedTypes = {DataTypeItemStack.class, DataTypeString.class}, params = {"stack", "ore_dictionary"}, expectedResult = DataTypeBoolean.class)
public class DataOperationItemStackMatchesOreDictionary extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeItemStack t1 = packet.getVarInType(DataTypeItemStack.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		//Yes
		return new DataTypeBoolean(Utils.compareToOreName(t1.value, t2.value));
	}
}
