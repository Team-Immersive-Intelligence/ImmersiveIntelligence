package pl.pabilo8.immersiveintelligence.common.block.multiblocks.gate;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblocks.gate.MultiblockWoodenFenceGate.TileEntityWoodenFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_WoodenFortification;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockWoodenFenceGate extends MultiblockStuctureBase<TileEntityWoodenFenceGate>
{
	public static MultiblockWoodenFenceGate instance = new MultiblockWoodenFenceGate();

	public MultiblockWoodenFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/wooden_fence_gate"));
		offset = new Vec3i(0, 1, 0);
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockFenceGateMultiblock, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityWoodenFenceGate placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockFenceGateMultiblock.getStateFromMeta(IIBlockTypes_FenceGate.WOODEN.getMeta()));
		return (TileEntityWoodenFenceGate)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityWoodenFenceGate getMBInstance()
	{
		return new TileEntityWoodenFenceGate();
	}

	public static class TileEntityWoodenFenceGate extends TileEntityGateBase<TileEntityWoodenFenceGate>
	{
		public TileEntityWoodenFenceGate()
		{
			super(instance);
		}

		@Override
		public IBlockState getFenceState(@Nullable EnumFacing facingConnected)
		{
			return IIContent.blockWoodenFortification.getStateFromMeta(IIBlockTypes_WoodenFortification.WOODEN_STEEL_CHAIN_FENCE.getMeta())
					.withProperty(BlockFence.EAST,facingConnected==EnumFacing.EAST)
					.withProperty(BlockFence.WEST,facingConnected==EnumFacing.WEST)
					.withProperty(BlockFence.NORTH,facingConnected==EnumFacing.NORTH)
					.withProperty(BlockFence.SOUTH,facingConnected==EnumFacing.SOUTH)
					;
		}
	}
}
