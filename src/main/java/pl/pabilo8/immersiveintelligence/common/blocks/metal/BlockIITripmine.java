package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelTripMine;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 05.02.2021
 */
public class BlockIITripmine extends BlockIIMine
{
	public BlockIITripmine()
	{
		super("tripmine", ItemBlockTripmine.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		BlockStateContainer base = super.createBlockState();
		IUnlistedProperty[] unlisted = (base instanceof ExtendedBlockState)?((ExtendedBlockState)base).getUnlistedProperties().toArray(new IUnlistedProperty[0]): new IUnlistedProperty[0];
		unlisted = Arrays.copyOf(unlisted, unlisted.length+1);
		unlisted[unlisted.length-1] = IEProperties.CONNECTIONS;
		return new ExtendedBlockState(this, base.getProperties().toArray(new IProperty[0]), unlisted);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		state = super.getExtendedState(state, world, pos);
		if(state instanceof IExtendedBlockState)
		{
			IExtendedBlockState ext = (IExtendedBlockState)state;
			TileEntity te = world.getTileEntity(pos);
			if(!(te instanceof TileEntityImmersiveConnectable))
				return state;
			state = ext.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable)te).genConnBlockstate());
		}
		return state;
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
			return "tripmine";
		}

		@Override
		public float getComponentCapacity()
		{
			return 0.45f;
		}

		@Override
		public int getGunpowderNeeded()
		{
			return 2;
		}

		@Override
		public int getCoreMaterialNeeded()
		{
			return 2;
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
		public @Nonnull Class<? extends IBulletModel> getModel()
		{
			return ModelTripMine.class;
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
