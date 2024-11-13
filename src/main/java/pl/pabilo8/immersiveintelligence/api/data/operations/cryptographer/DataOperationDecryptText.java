package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEncrypted;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

/**
 * @author GabrielV
 * @ii-approved 0.3.1
 * @since 18.09.2024
 */
@DataOperation.DataOperationMeta(name = "decrypt_text",
		allowedTypes = {DataTypeEncrypted.class, DataTypeString.class}, params = {"encrypted", "password"},
		expectedResult = DataTypeString.class)
public class DataOperationDecryptText extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		byte[] encrypted = packet.getVarInType(DataTypeEncrypted.class, data.getArgument(0)).value.clone();
		DataType t1 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String password = t1.toString();
		String decrypted = Cryptographer.decryptToString(encrypted, password.getBytes(StandardCharsets.UTF_8));
		return new DataTypeString(decrypted);
	}
}
