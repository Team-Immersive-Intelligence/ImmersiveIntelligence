package pl.pabilo8.immersiveintelligence.api.data;

import com.google.common.annotations.VisibleForTesting;
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
import pl.pabilo8.immersiveintelligence.api.data.operations.fluidstack.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.itemstack.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationAND;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationNOT;
import pl.pabilo8.immersiveintelligence.api.data.operations.logic.DataOperationOR;
import pl.pabilo8.immersiveintelligence.api.data.operations.map.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.text.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.type_conversion.*;
import pl.pabilo8.immersiveintelligence.api.data.operations.vector.*;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class that handles registration and instantiation of {@link DataOperation Data Operations} and their {@link DataOperationMeta metadata}.
 *
 * @author Pabilo8
 * @updated 28.10.2024
 * @ii-approved 0.3.1
 * @since 05-07-2019
 */
public class IIDataOperationUtils
{
	private static final Map<String, Class<? extends DataOperation>> operations = new HashMap<>();
	private static final Map<String, DataOperationMeta> operationsMeta = new HashMap<>();

	public static void registerDataOperations()
	{
		//Basic arithmetic
		registerOperation(DataOperationAdd.class);
		registerOperation(DataOperationSubtract.class);
		registerOperation(DataOperationMultiply.class);
		registerOperation(DataOperationDivide.class);
		registerOperation(DataOperationModulo.class);
		registerOperation(DataOperationAbs.class);

		//Advanced arithmetic
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

		//Comparator
		registerOperation(DataOperationGreater.class);
		registerOperation(DataOperationLess.class);
		registerOperation(DataOperationGreaterOrEqual.class);
		registerOperation(DataOperationLessOrEqual.class);
		registerOperation(DataOperationEqual.class);

		//Logic
		registerOperation(DataOperationAND.class);
		registerOperation(DataOperationOR.class);
		registerOperation(DataOperationNOT.class);

		registerOperation(DataOperationNAND.class);
		registerOperation(DataOperationNOR.class);
		registerOperation(DataOperationXOR.class);
		registerOperation(DataOperationXNOR.class);

		//String
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

		//ItemStack
		registerOperation(DataOperationItemStackCreate.class);
		registerOperation(DataOperationItemStackGetCount.class);
		registerOperation(DataOperationItemStackSetCount.class);
		registerOperation(DataOperationItemStackGetMeta.class);
		registerOperation(DataOperationItemStackSetMeta.class);
		registerOperation(DataOperationItemStackGetNBT.class);
		registerOperation(DataOperationItemStackSetNBT.class);
		registerOperation(DataOperationItemStackGetItemID.class);
		registerOperation(DataOperationItemStackIsEmpty.class);
		registerOperation(DataOperationItemStackStacksWith.class);
		registerOperation(DataOperationItemStackMatchesOreDictionary.class);

		//Entity
		registerOperation(DataOperationEntityGetID.class);
		registerOperation(DataOperationEntityGetType.class);
		registerOperation(DataOperationEntityGetName.class);
		registerOperation(DataOperationEntityGetDimensionID.class);
		registerOperation(DataOperationEntityGetPos.class);
		registerOperation(DataOperationEntityGetPosX.class);
		registerOperation(DataOperationEntityGetPosY.class);
		registerOperation(DataOperationEntityGetPosZ.class);

		//Array
		registerOperation(DataOperationArrayCreate.class);
		registerOperation(DataOperationArrayGet.class);
		registerOperation(DataOperationArraySet.class);
		registerOperation(DataOperationArrayPop.class);
		registerOperation(DataOperationArrayPush.class);
		registerOperation(DataOperationArraySwap.class);
		registerOperation(DataOperationArrayLength.class);

		//Document
		registerOperation(DataOperationDocumentReadPage.class);
		registerOperation(DataOperationDocumentReadAllPagesArray.class);
		registerOperation(DataOperationDocumentReadAllPagesString.class);
		registerOperation(DataOperationDocumentGetAuthor.class);
		registerOperation(DataOperationDocumentGetTitle.class);

		//Type conversion
		registerOperation(DataOperationIsNull.class);
		registerOperation(DataOperationIsSameType.class);
		registerOperation(DataOperationToInteger.class);
		registerOperation(DataOperationToFloat.class);
		registerOperation(DataOperationToString.class);
		registerOperation(DataOperationToBoolean.class);
		registerOperation(DataOperationToNull.class);

		//Vector
		registerOperation(DataOperationVectorCreate.class);
		registerOperation(DataOperationVectorCreateAngle.class);
		registerOperation(DataOperationVectorAdd.class);
		registerOperation(DataOperationVectorSub.class);
		registerOperation(DataOperationVectorMul.class);
		registerOperation(DataOperationVectorScale.class);

		registerOperation(DataOperationVectorDot.class);
		registerOperation(DataOperationVectorCross.class);
		registerOperation(DataOperationVectorNormalize.class);
		registerOperation(DataOperationVectorDistance.class);

		registerOperation(DataOperationVectorLength.class);
		registerOperation(DataOperationVectorGetX.class);
		registerOperation(DataOperationVectorGetY.class);
		registerOperation(DataOperationVectorGetZ.class);
		registerOperation(DataOperationVectorSetX.class);
		registerOperation(DataOperationVectorSetY.class);
		registerOperation(DataOperationVectorSetZ.class);
		registerOperation(DataOperationVectorGetYaw.class);
		registerOperation(DataOperationVectorGetPitch.class);

		//Map
		registerOperation(DataOperationMapCreate.class);
		registerOperation(DataOperationMapSet.class);
		registerOperation(DataOperationMapGet.class);
		registerOperation(DataOperationMapRemove.class);
		registerOperation(DataOperationMapClear.class);
		registerOperation(DataOperationMapContains.class);
		registerOperation(DataOperationMapKeys.class);
		registerOperation(DataOperationMapValues.class);

		//FluidStack
		registerOperation(DataOperationFluidCreate.class);
		registerOperation(DataOperationFluidGetAmount.class);
		registerOperation(DataOperationFluidSetAmount.class);
		registerOperation(DataOperationFluidGetID.class);
		registerOperation(DataOperationFluidGetNBT.class);
		registerOperation(DataOperationFluidSetNBT.class);
		registerOperation(DataOperationFluidIsEmpty.class);
		registerOperation(DataOperationFluidStacksWith.class);


		//Cryptographer
		registerOperation(DataOperationEncryptText.class);
		registerOperation(DataOperationEncryptNumber.class);
		registerOperation(DataOperationDecryptText.class);
		registerOperation(DataOperationDecryptNumber.class);
	}

	@VisibleForTesting
	public static Set<String> getAllOperationNames()
	{
		return operations.keySet();
	}

	public static <T extends DataOperation> void registerOperation(Class<T> clazz)
	{
		if(clazz==DataOperation.class)
			return;
		if(!clazz.isAnnotationPresent(DataOperation.DataOperationMeta.class))
		{
			IILogger.error("DataOperation "+clazz.getName()+" is missing DataOperationMeta annotation!");
			return;
		}

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
