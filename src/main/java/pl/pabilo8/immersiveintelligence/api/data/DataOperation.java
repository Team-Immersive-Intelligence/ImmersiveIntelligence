package pl.pabilo8.immersiveintelligence.api.data;

import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_arithmetic.DataOperationMax;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_arithmetic.DataOperationMin;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_arithmetic.DataOperationPower;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_arithmetic.DataOperationRoot;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_logic.DataOperationNAND;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_logic.DataOperationNOR;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_logic.DataOperationXNOR;
import pl.pabilo8.immersiveintelligence.api.data.operators.advanced_logic.DataOperationXOR;
import pl.pabilo8.immersiveintelligence.api.data.operators.arithmetic.*;
import pl.pabilo8.immersiveintelligence.api.data.operators.comparators.*;
import pl.pabilo8.immersiveintelligence.api.data.operators.itemstack.*;
import pl.pabilo8.immersiveintelligence.api.data.operators.logic.DataOperationAND;
import pl.pabilo8.immersiveintelligence.api.data.operators.logic.DataOperationNOT;
import pl.pabilo8.immersiveintelligence.api.data.operators.logic.DataOperationOR;
import pl.pabilo8.immersiveintelligence.api.data.operators.text.DataOperationJoin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperation
{
	public static final Map<String, Class> operations = new HashMap<>();

	static
	{
		operations.put("add", DataOperationAdd.class);
		operations.put("subtract", DataOperationSubtract.class);
		operations.put("multiply", DataOperationMultiply.class);
		operations.put("divide", DataOperationDivide.class);
		operations.put("modulo", DataOperationModulo.class);

		operations.put("power", DataOperationPower.class);
		operations.put("root", DataOperationRoot.class);
		operations.put("max", DataOperationMax.class);
		operations.put("min", DataOperationMin.class);

		operations.put("greater", DataOperationGreater.class);
		operations.put("less", DataOperationLess.class);
		operations.put("greater_or_equal", DataOperationGreaterOrEqual.class);
		operations.put("less_or_equal", DataOperationLessOrEqual.class);
		operations.put("equal", DataOperationEqual.class);

		operations.put("and", DataOperationAND.class);
		operations.put("or", DataOperationOR.class);
		operations.put("not", DataOperationNOT.class);

		operations.put("nand", DataOperationNAND.class);
		operations.put("nor", DataOperationNOR.class);
		operations.put("xor", DataOperationXOR.class);
		operations.put("xnor", DataOperationXNOR.class);

		operations.put("join", DataOperationJoin.class);

		//operations.put("longer",DataOperationLonger.class);
		//operations.put("shorter",DataOperationShorter.class);
		//operations.put("length_equal",DataOperationLengthEqual.class);
		//operations.put("filter",DataOperationFilter.class);
		//operations.put("format",DataOperationFormat.class);

		operations.put("get_quantity", DataOperationGetQuantity.class);
		operations.put("set_quantity", DataOperationSetQuantity.class);
		operations.put("get_durability", DataOperationGetDurability.class);
		operations.put("set_durability", DataOperationSetDurability.class);
		operations.put("get_nbt", DataOperationGetNBT.class);
		operations.put("set_nbt", DataOperationSetNBT.class);
		operations.put("can_stack_with", DataOperationCanStackWith.class);
		operations.put("matches_oredict", DataOperationMatchesOreDictionary.class);
	}

}
