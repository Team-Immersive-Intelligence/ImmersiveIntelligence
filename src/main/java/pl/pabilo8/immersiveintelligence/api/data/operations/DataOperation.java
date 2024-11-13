package pl.pabilo8.immersiveintelligence.api.data.operations;

import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import javax.annotation.Nonnull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a data operation that can be executed on a {@link DataPacket}, based on {@link DataTypeExpression} instructions.
 *
 * @author Pabilo8
 * @updated 09.10.2024
 * @ii-approved 0.3.1
 * @since 05.07.2019
 */
public abstract class DataOperation
{
	@Nonnull
	public abstract DataType execute(DataPacket packet, DataTypeExpression data);

	@Nonnull
	public DataOperationMeta getMeta()
	{
		return this.getClass().getAnnotation(DataOperationMeta.class);
	}

	/**
	 * Meta information about the operation, used to get class-wide information.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface DataOperationMeta
	{
		/**
		 * @return name of the operation
		 */
		String name();

		/**
		 * @return symbolic expression of the operation, if applicable
		 */
		String expression() default "";

		/**
		 * @return allowed input parameters for the operation
		 */
		Class<? extends DataType>[] allowedTypes();

		/**
		 * @return names of the input parameters
		 */
		String[] params();

		/**
		 * @return expected result type of the operation
		 */
		Class<? extends DataType> expectedResult();

		/**
		 * @return how many processor ticks the operation takes to execute
		 */
		int processorTime() default 1;

		/**
		 * @return whether the operation returns a result or is to be treated as a "void" operation
		 */
		boolean resultMatters() default true;
	}

	/**
	 * Class representing a null operation, used as a default value for an uninitialized {@link DataTypeExpression}.
	 *
	 * @author Pabilo8
	 * @ii-approved 0.3.1
	 * @since 27.10.2024
	 */
	@DataOperationMeta(name = "null", allowedTypes = {}, params = {}, expectedResult = DataTypeNull.class, resultMatters = false)
	public static class DataOperationNull extends DataOperation
	{
		public static final DataOperationNull INSTANCE = new DataOperationNull();
		public static final DataOperationMeta INSTANCE_META = INSTANCE.getMeta();
		public static final DataType[] INSTANCE_TYPES = new DataType[0];

		@Nonnull
		public DataType execute(DataPacket packet, DataTypeExpression data)
		{
			return new DataTypeNull();
		}
	}

}
