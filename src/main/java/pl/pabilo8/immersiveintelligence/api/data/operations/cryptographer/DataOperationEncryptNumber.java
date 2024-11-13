package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeEncrypted;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;

/**
 * @author GabrielV
 * @ii-approved 0.3.1
 * @since 18.09.2024
 */
@DataOperation.DataOperationMeta(name = "encrypt_number",
		allowedTypes = {DataTypeInteger.class, DataTypeString.class}, params = {"number", "password"},
		expectedResult = DataTypeEncrypted.class)
public class DataOperationEncryptNumber extends DataOperation
{
	@Nonnull
	@Override
	public DataType execute(DataPacket packet, DataTypeExpression data)
	{
		DataType t1 = packet.getVarInType(DataTypeInteger.class, data.getArgument(0)),
				t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String msg = t1.toString(), password = t2.toString();
		byte[] bytes = Cryptographer.encrypt(msg, password);
		return new DataTypeEncrypted(bytes);
	}
}
