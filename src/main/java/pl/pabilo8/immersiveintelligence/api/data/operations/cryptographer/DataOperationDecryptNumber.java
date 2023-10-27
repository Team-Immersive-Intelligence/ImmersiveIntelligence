package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

public class DataOperationDecryptNumber extends DataOperation
{
	public DataOperationDecryptNumber()
	{
		name = "decrypt_number";
		allowedTypes = new Class[]{DataTypeEncrypted.class, DataTypeString.class};
		params = new String[]{"message", "password"};
		expectedResult = DataTypeInteger.class;
	}

	@Nonnull
	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		byte[] encrypted = packet.getVarInType(DataTypeEncrypted.class, data.getArgument(0)).value.clone();
		IDataType t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String password = t2.valueToString();
		String decrypted = Cryptographer.decryptToString(encrypted, password.getBytes(StandardCharsets.UTF_8));
		return new DataTypeInteger(Integer.parseInt(decrypted));
	}
}
