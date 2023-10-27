package pl.pabilo8.immersiveintelligence.api.data.operations.cryptographer;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.operations.DataOperation;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import javax.annotation.Nonnull;

/**
 * @author GabrielV
 */
public class DataOperationEncryptNumber extends DataOperation
{
	public DataOperationEncryptNumber()
	{
		name = "encrypt_number";
		allowedTypes = new Class[]{DataTypeInteger.class, DataTypeString.class};
		params = new String[]{"number", "password"};
		expectedResult = DataTypeEncrypted.class;
	}

	@Nonnull
	@Override
	public IDataType execute(DataPacket packet, DataTypeExpression data)
	{
		IDataType t1 = packet.getVarInType(DataTypeInteger.class, data.getArgument(0)),
				t2 = packet.getVarInType(DataTypeString.class, data.getArgument(1));
		String msg = t1.valueToString(), password = t2.valueToString();
		byte[] bytes = Cryptographer.encrypt(msg, password);
		return new DataTypeEncrypted(bytes);
	}
}
