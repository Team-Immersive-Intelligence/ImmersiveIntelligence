package pl.pabilo8.immersiveintelligence.common.util.block;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.common.blocks.BlockIEBase.IBlockEnum;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.IBatchOredictRegister;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * B O I L E R P L A T E (tm)
 *
 * @author Pabilo8
 * @since 01.09.2022
 */
public class IIBlockInterfaces
{
	/**
	 * Used by Block enums
	 */
	public interface IIBlockEnum extends IBlockEnum, ISerializableEnum
	{
		@Override
		default int getMeta()
		{
			return ((Enum<?>)this).ordinal();
		}

		@Nullable
		default IIBlockProperties getProperties()
		{
			return IIUtils.getEnumAnnotation(IIBlockProperties.class, this);
		}

		@Override
		default boolean listForCreative()
		{
			IIBlockProperties properties = getProperties();
			return properties==null||properties.hidden()!=TernaryValue.TRUE;
		}
	}

	/**
	 * Individual properties for SubBlocks<br>
	 * in integer values, -1 means the default value of the block
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface IIBlockProperties
	{
		/**
		 * @return whether this SubBlock requires a separate BlockState file
		 */
		boolean needsCustomState() default false;

		/**
		 * @return whether this SubBlock is visible as item
		 */
		TernaryValue hidden() default TernaryValue.UNSET;

		/**
		 * @return whether this SubBlock is visible as item
		 */
		TernaryValue fullCube() default TernaryValue.UNSET;

		/**
		 * @return max stack size of ItemBlock for this SubBlock, if different from block
		 */
		int stackSize() default -1;

		/**
		 * @return OreDictionary entries for this SubBlock
		 * Separate from {@link IBatchOredictRegister}
		 */
		String[] oreDict() default {};

		/**
		 * @return opacity, light absorption, if different from block
		 */
		int opacity() default -1;

		/**
		 * @return block hardness, if different from block
		 */
		float hardness() default -1;

		/**
		 * @return resistance to explosions, if different from block
		 */
		float blastResistance() default -1;

		/**
		 * @return additional tool types (i.e. hammer, wrench), combined with ones valid for this subtype's material
		 */
		String[] harvestTools() default {};

		/**
		 * @return whether this SubBlock requires a separate BlockState file
		 */
		String descKey() default "";

		/**
		 * @return harvest level, if different from block
		 */
		int harvestLevel() default -1;

		/**
		 * @return render layer(s), if different from block
		 */
		BlockRenderLayer[] renderLayer() default {};
	}

	public enum TernaryValue
	{
		//needs a default value
		UNSET,
		TRUE,
		FALSE;

		/**
		 * Always check if it's set first
		 */
		boolean isTrue()
		{
			return this!=FALSE;
		}

		public boolean isSet()
		{
			return this!=UNSET;
		}
	}


	/**
	 * Used by {@link BlockIITileProvider}
	 */
	public interface IITileProviderEnum extends IIBlockEnum
	{
		default Class<? extends TileEntity> getTile()
		{
			EnumTileProvider tp = IIUtils.getEnumAnnotation(EnumTileProvider.class, this);
			return tp==null?null: tp.tile();
		}
	}

	/**
	 * Used for automatic registration and applying the tile entity of this block
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface EnumTileProvider
	{
		Class<? extends TileEntity> tile();
	}


	/**
	 * Used by {@link pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock}
	 */
	public interface IITileMultiblockEnum extends IITileProviderEnum
	{
		@Nullable
		@Override
		default Class<? extends TileEntity> getTile()
		{
			EnumMultiblockProvider tp = IIUtils.getEnumAnnotation(EnumMultiblockProvider.class, this);
			return tp==null?null: tp.tile();
		}

		@Nullable
		default Class<? extends IMultiblock> getMultiblock()
		{
			EnumMultiblockProvider tp = IIUtils.getEnumAnnotation(EnumMultiblockProvider.class, this);
			return tp==null?null: tp.multiblock();
		}

		@Override
		default boolean listForCreative()
		{
			return false;
		}
	}

	/**
	 * Indirect extension of {@link EnumTileProvider}
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface EnumMultiblockProvider
	{
		Class<? extends TileEntity> tile();

		Class<? extends IMultiblock> multiblock();
	}
}
