package pl.pabilo8.immersiveintelligence.api.data.operations;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 05-07-2019
 */
public abstract class DataOperation
{
	@Nonnull
	public String name;
	@Nullable
	public String expression;
	@Nonnull
	public Class<? extends IDataType>[] allowedTypes;
	@Nullable
	public String[] params;
	public Class<? extends IDataType> expectedResult;

	@Nonnull
	public abstract IDataType execute(DataPacket packet, DataTypeExpression data);

}
