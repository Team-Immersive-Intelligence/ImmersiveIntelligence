package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import crafttweaker.api.data.IData;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.cryptography.Cryptographer;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEncrypted;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.IILogger;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author GabrielV
 */
public class DataOperationEncryptText extends DataOperation
{
	public DataOperationEncryptText()
	{
		name = "encrypt_text";
		allowedTypes = new Class[]{DataTypeString.class,DataTypeString.class};
		params = new String[]{"message", "password"};
		expectedResult = DataTypeEncrypted.class;
	}

	@Nonnull
	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		byte[] bytes = Cryptographer.encrypt(t1.valueToString(), t2.valueToString());
		return new DataTypeEncrypted(bytes);
	}
}
