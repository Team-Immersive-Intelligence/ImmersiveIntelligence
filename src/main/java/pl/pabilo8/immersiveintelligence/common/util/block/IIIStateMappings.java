package pl.pabilo8.immersiveintelligence.common.util.block;

import javax.annotation.Nullable;
import java.util.List;

public interface IIIStateMappings<T extends Enum<T>>
{
	/**
	 * @return The name of the block, used for the blockstate location, if {@link #getMappingsName()} is not specified
	 */
	String getMappingsName();

	/**
	 * @param meta      The meta ID of the block
	 * @param itemBlock Whether the mapping is for an itemBlock of the block or the block itself
	 * @return Custom mapping for the blockstate JSON
	 */
	@Nullable
	String getMappingsExtension(int meta, boolean itemBlock);

	/**
	 * @return The enum values of the block, used for the blockstate JSON
	 */
	@Nullable
	T[] getMappingsEnum();

	/**
	 * @return A list of legacy, TMT-based TESR renderers for the model
	 */
	@Nullable
	@Deprecated
	default List<T> getLegacyTESR()
	{
		return null;
	}

	interface IIISingleMetaStateMappings extends IIIStateMappings<DummyEnum>
	{
		@Nullable
		@Override
		default String getMappingsExtension(int meta, boolean itemBlock)
		{
			return null;
		}

		@Nullable
		@Override
		default DummyEnum[] getMappingsEnum()
		{
			return DummyEnum.values();
		}
	}

	enum DummyEnum
	{
		NULL
	}
}