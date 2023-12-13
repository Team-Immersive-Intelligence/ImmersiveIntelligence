package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIWoodenChainFence.WoodenFortifications;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockWoodenChainFenceGate.TileEntityWoodenChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockWoodenChainFenceGate extends MultiblockFenceGateBase<TileEntityWoodenChainFenceGate>
{
	public static MultiblockWoodenChainFenceGate INSTANCE;

	public MultiblockWoodenChainFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/wooden_chain_fence_gate"));
		INSTANCE = this;
	}

	@Override
	protected int getMeta()
	{
		return IIBlockTypes_FenceGate.WOODEN_CHAIN.getMeta();
	}

	@Override
	protected TileEntityWoodenChainFenceGate getMBInstance()
	{
		return new TileEntityWoodenChainFenceGate();
	}

	public static class TileEntityWoodenChainFenceGate extends TileEntityGateBase<TileEntityWoodenChainFenceGate>
	{
		public TileEntityWoodenChainFenceGate()
		{
			super(INSTANCE);
		}

		@Override
		protected SoundEvent getOpeningSound()
		{
			return IISounds.gateWoodenOpen;
		}

		@Override
		protected SoundEvent getClosingSound()
		{
			return IISounds.gateWoodenClose;
		}

		@Override
		public IBlockState getFenceState(@Nullable EnumFacing facingConnected)
		{
			return IIContent.blockWoodenFortification.getStateFromMeta(WoodenFortifications.WOODEN_STEEL_CHAIN_FENCE.getMeta())
					.withProperty(BlockFence.EAST, facingConnected==EnumFacing.EAST)
					.withProperty(BlockFence.WEST, facingConnected==EnumFacing.WEST)
					.withProperty(BlockFence.NORTH, facingConnected==EnumFacing.NORTH)
					.withProperty(BlockFence.SOUTH, facingConnected==EnumFacing.SOUTH)
					;
		}
	}
}
