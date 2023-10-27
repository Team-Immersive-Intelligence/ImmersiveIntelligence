package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockAluminiumChainFenceGate extends MultiblockStuctureBase<TileEntityAluminiumChainFenceGate>
{
	public static MultiblockAluminiumChainFenceGate INSTANCE;

	public MultiblockAluminiumChainFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/aluminium_chain_fence_gate"));
		offset = new Vec3i(0, 1, 0);
		INSTANCE = this;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockFenceGateMultiblock;
	}

	@Override
	protected int getMeta()
	{
		return IIBlockTypes_FenceGate.ALUMINIUM_CHAIN.getMeta();
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockFenceGateMultiblock, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityAluminiumChainFenceGate placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockFenceGateMultiblock.getStateFromMeta(IIBlockTypes_FenceGate.ALUMINIUM_CHAIN.getMeta()));
		return (TileEntityAluminiumChainFenceGate)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityAluminiumChainFenceGate getMBInstance()
	{
		return new TileEntityAluminiumChainFenceGate();
	}

	public static class TileEntityAluminiumChainFenceGate extends TileEntityGateBase<TileEntityAluminiumChainFenceGate>
	{
		public TileEntityAluminiumChainFenceGate()
		{
			super(INSTANCE);
		}

		@Override
		public IBlockState getFenceState(@Nullable EnumFacing facingConnected)
		{
			return IIContent.blockMetalFortification.getStateFromMeta(MetalFortifications.ALUMINIUM_CHAIN_FENCE.getMeta())
					.withProperty(BlockFence.EAST, facingConnected==EnumFacing.EAST)
					.withProperty(BlockFence.WEST, facingConnected==EnumFacing.WEST)
					.withProperty(BlockFence.NORTH, facingConnected==EnumFacing.NORTH)
					.withProperty(BlockFence.SOUTH, facingConnected==EnumFacing.SOUTH)
					;
		}
	}
}
