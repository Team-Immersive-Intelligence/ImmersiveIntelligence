package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEncrypted;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author GabrielV
 * @ii-approved 0.3.1
 * @since 18.09.2024
 */
@DataOperation.DataOperationMeta(name = "encrypt_text",
		allowedTypes = {DataTypeString.class, DataTypeString.class}, params = {"text", "password"},
		expectedResult = DataTypeEncrypted.class)
public class DataOperationEncryptText extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataTypeString t1 = packet.getVarInType(DataTypeString.class, data.getArgument(0));
		DataTypeString t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));

		byte[] bytes = Cryptographer.encrypt(t1.toString(), t2.toString());
		return new DataTypeEncrypted(bytes);
	}
}
