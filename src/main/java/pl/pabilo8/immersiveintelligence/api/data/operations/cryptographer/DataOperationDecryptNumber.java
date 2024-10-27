package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

/**
 * @author GabrielV
 * @ii-approved 0.3.1
 * @since 18.09.2024
 */
@DataOperation.DataOperationMeta(name = "decrypt_number",
		allowedTypes = {DataTypeEncrypted.class, DataTypeString.class}, params = {"message", "password"},
		expectedResult = NumericDataType.class)
public class DataOperationDecryptNumber extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		byte[] encrypted = packet.getVarInType(DataTypeEncrypted.class, data.getArgument(0)).value.clone();
		DataType t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String password = t2.valueToString();
		String decrypted = Cryptographer.decryptToString(encrypted, password.getBytes(StandardCharsets.UTF_8));
		return new DataTypeInteger(Integer.parseInt(decrypted));
	}
}
