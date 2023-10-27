package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockSteelChainFenceGate.TileEntitySteelChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockSteelChainFenceGate extends MultiblockStuctureBase<TileEntitySteelChainFenceGate>
{
	public static MultiblockSteelChainFenceGate INSTANCE;

	public MultiblockSteelChainFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/steel_chain_fence_gate"));
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
		return IIBlockTypes_FenceGate.STEEL_CHAIN.getMeta();
	}

	@Override
	protected TileEntitySteelChainFenceGate getMBInstance()
	{
		return new TileEntitySteelChainFenceGate();
	}

	public static class TileEntitySteelChainFenceGate extends TileEntityGateBase<TileEntitySteelChainFenceGate>
	{
		public TileEntitySteelChainFenceGate()
		{
			super(INSTANCE);
		}

		@Override
		public IBlockState getFenceState(@Nullable EnumFacing facingConnected)
		{
			return IIContent.blockMetalFortification.getStateFromMeta(MetalFortifications.STEEL_CHAIN_FENCE.getMeta())
					.withProperty(BlockFence.EAST, facingConnected==EnumFacing.EAST)
					.withProperty(BlockFence.WEST, facingConnected==EnumFacing.WEST)
					.withProperty(BlockFence.NORTH, facingConnected==EnumFacing.NORTH)
					.withProperty(BlockFence.SOUTH, facingConnected==EnumFacing.SOUTH)
					;
		}
	}
}
