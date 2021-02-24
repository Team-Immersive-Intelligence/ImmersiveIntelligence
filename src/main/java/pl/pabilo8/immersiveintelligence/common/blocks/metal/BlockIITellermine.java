package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTellermine;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIITellermine extends BlockIIMine
{
	public BlockIITellermine()
	{
		super("tellermine", ItemBlockTripmine.class);
	}

	@Override
	protected TileEntity getMineTileEntity()
	{
		return new TileEntityTripMine();
	}

	public static class ItemBlockTripmine extends ItemBlockMineBase
	{
		public ItemBlockTripmine(Block b)
		{
			super(b);
		}


		@Override
		public String getName()
		{
			return "tellermine";
		}

		@Override
		public float getComponentCapacity()
		{
			return 0.45f;
		}

		@Override
		public int getGunpowderNeeded()
		{
			return 0;
		}

		@Override
		public int getCoreMaterialNeeded()
		{
			return 1;
		}

		@Override
		public float getInitialMass()
		{
			return 0.75f;
		}

		@Override
		public float getCaliber()
		{
			return 0.625f;
		}

		@Override
		public @Nonnull
		Class<? extends IBulletModel> getModel()
		{
			return ModelTellermine.class;
		}

		@Override
		public float getDamage()
		{
			return 0;
		}

		@Override
		public ItemStack getCasingStack(int amount)
		{
			return ItemStack.EMPTY;
		}
	}
}
