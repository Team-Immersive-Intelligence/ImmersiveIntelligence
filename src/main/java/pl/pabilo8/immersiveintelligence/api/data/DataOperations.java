package pl.pabilo8.immersiveintelligence.api.data;

import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationMeta;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation.DataOperationNull;
import pl.pabilo8.immersiveintelligence.api.data.operations.advanced_arithmetic.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic.DataOperationNAND;
import pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic.DataOperationNOR;
import pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic.DataOperationXNOR;
import pl.pabilo8.immersiveintelligence.api.data.operations.advanced_logic.DataOperationXOR;
import pl.pabilo8.immersiveintelligence.api.data.operations.arithmetic.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.array.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.comparators.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer.DataOperationDecryptNumber;
import pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer.DataOperationDecryptText;
import pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer.DataOperationEncryptNumber;
import pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer.DataOperationEncryptText;
import pl.pabilo8.immersiveintelligence.api.data.operations.document.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.entity.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.itemstack.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationAND;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationNOT;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationOR;
import pl.pabilo8.immersiveintelligence.api.data.operations.text.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion.*;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public class DataOperations
{
	private static final Map<String, Class<? extends DataOperation>> operations = new HashMap<>();
	private static final Map<String, DataOperationMeta> operationsMeta = new HashMap<>();

	static
	{
		//Placeholder for empty expression
		registerOperation(DataOperationNull.class);

		//Actual operations
		registerOperation(DataOperationAdd.class);
		registerOperation(DataOperationSubtract.class);
		registerOperation(DataOperationMultiply.class);
		registerOperation(DataOperationDivide.class);
		registerOperation(DataOperationModulo.class);
		registerOperation(DataOperationAbs.class);

		registerOperation(DataOperationPower.class);
		registerOperation(DataOperationRoot.class);
		registerOperation(DataOperationMax.class);
		registerOperation(DataOperationMin.class);

		registerOperation(DataOperationSign.class);
		registerOperation(DataOperationCeil.class);
		registerOperation(DataOperationRound.class);
		registerOperation(DataOperationFloor.class);

		registerOperation(DataOperationSin.class);
		registerOperation(DataOperationCos.class);
		registerOperation(DataOperationTan.class);

		registerOperation(DataOperationGreater.class);
		registerOperation(DataOperationLess.class);
		registerOperation(DataOperationGreaterOrEqual.class);
		registerOperation(DataOperationLessOrEqual.class);
		registerOperation(DataOperationEqual.class);

		registerOperation(DataOperationAND.class);
		registerOperation(DataOperationOR.class);
		registerOperation(DataOperationNOT.class);

		registerOperation(DataOperationNAND.class);
		registerOperation(DataOperationNOR.class);
		registerOperation(DataOperationXOR.class);
		registerOperation(DataOperationXNOR.class);

		registerOperation(DataOperationStringJoin.class);
		registerOperation(DataOperationStringSplit.class);
		registerOperation(DataOperationStringLength.class);
		registerOperation(DataOperationStringCharAt.class);
		registerOperation(DataOperationStringSubstring.class);
		registerOperation(DataOperationStringTrim.class);

		registerOperation(DataOperationStringHexcol.class);
		registerOperation(DataOperationStringFormat.class);

		registerOperation(DataOperationStringContains.class);
		registerOperation(DataOperationStringContainsCount.class);
		registerOperation(DataOperationStringReplaceFirst.class);
		registerOperation(DataOperationStringReplaceAll.class);

		registerOperation(DataOperationStringLowerCase.class);
		registerOperation(DataOperationStringUpperCase.class);
		registerOperation(DataOperationStringSnakeCase.class);
		registerOperation(DataOperationStringCamelCase.class);
		registerOperation(DataOperationStringReverse.class);

		registerOperation(DataOperationGetQuantity.class);
		registerOperation(DataOperationSetQuantity.class);
		registerOperation(DataOperationGetDurability.class);
		registerOperation(DataOperationSetDurability.class);
		registerOperation(DataOperationGetNBT.class);
		registerOperation(DataOperationSetNBT.class);
		registerOperation(DataOperationGetItemID.class);
		registerOperation(DataOperationGetItemStack.class);
		registerOperation(DataOperationIsStackEmpty.class);
		registerOperation(DataOperationCanStackWith.class);
		registerOperation(DataOperationMatchesOreDictionary.class);

		registerOperation(DataOperationGetEntityID.class);
		registerOperation(DataOperationGetEntityType.class);
		registerOperation(DataOperationGetEntityName.class);
		registerOperation(DataOperationGetEntityDimensionID.class);
		registerOperation(DataOperationGetEntityPosX.class);
		registerOperation(DataOperationGetEntityPosY.class);
		registerOperation(DataOperationGetEntityPosZ.class);

		registerOperation(DataOperationStart.class);
		registerOperation(DataOperationGet.class);
		registerOperation(DataOperationSet.class);
		registerOperation(DataOperationPop.class);
		registerOperation(DataOperationPush.class);
		registerOperation(DataOperationSwap.class);
		registerOperation(DataOperationArrayLength.class);

		registerOperation(DataOperationDocumentReadPage.class);
		registerOperation(DataOperationDocumentReadAllPagesArray.class);
		registerOperation(DataOperationDocumentReadAllPagesString.class);
		registerOperation(DataOperationDocumentGetAuthor.class);
		registerOperation(DataOperationDocumentGetTitle.class);

		registerOperation(DataOperationIsNull.class);
		registerOperation(DataOperationIsSameType.class);
		registerOperation(DataOperationToInteger.class);
		registerOperation(DataOperationToFloat.class);
		registerOperation(DataOperationToString.class);
		registerOperation(DataOperationToBoolean.class);
		registerOperation(DataOperationToNull.class);

		registerOperation(DataOperationEncryptText.class);
		registerOperation(DataOperationEncryptNumber.class);
		registerOperation(DataOperationDecryptText.class);
		registerOperation(DataOperationDecryptNumber.class);
	}

	public static void registerOperation(Class<? extends DataOperation> clazz)
	{
		DataOperation.DataOperationMeta meta = clazz.getAnnotation(DataOperation.DataOperationMeta.class);
		operations.put(meta.name(), clazz);
		operationsMeta.put(meta.name(), meta);
	}

	@Nonnull
	public static DataOperation getOperationInstance(String name)
	{
		Class<? extends DataOperation> c = operations.get(name);
		if(c!=null)
		{
			try
			{
				return c.newInstance();
			} catch(InstantiationException|IllegalAccessException ignored)
			{
			}
		}
		return DataOperationNull.INSTANCE;
	}

	public static DataOperationMeta getOperationMeta(String name)
	{
		return operationsMeta.get(name);
	}
}
