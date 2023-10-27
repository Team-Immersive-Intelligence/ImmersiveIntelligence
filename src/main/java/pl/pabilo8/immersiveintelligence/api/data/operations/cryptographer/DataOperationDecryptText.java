package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEncrypted;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

public class DataOperationDecryptText extends DataOperation
{
	public DataOperationDecryptText()
	{
		name = "decrypt_text";
		allowedTypes = new Class[]{DataTypeEncrypted.class, DataTypeString.class};
		params = new String[]{"message", "password"};
		expectedResult = DataTypeString.class;
	}

	@Nonnull
	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		byte[] encrypted = packet.getVarInType(DataTypeEncrypted.class, data.getArgument(0)).value.clone();
		IDataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String password = t1.valueToString();
		String decrypted = Cryptographer.decryptToString(encrypted, password.getBytes(StandardCharsets.UTF_8));
		return new DataTypeString(decrypted);
	}
}
