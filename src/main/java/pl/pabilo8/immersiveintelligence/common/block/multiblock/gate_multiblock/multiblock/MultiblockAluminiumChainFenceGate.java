package pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.fortification.BlockIIMetalChainFence.MetalFortifications;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIFenceGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumChainFenceGate.TileEntityAluminiumChainFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockAluminiumChainFenceGate extends MultiblockFenceGateBase<TileEntityAluminiumChainFenceGate>
{
	public static MultiblockAluminiumChainFenceGate INSTANCE;

	public MultiblockAluminiumChainFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/aluminium_chain_fence_gate"));
		INSTANCE = this;
	}

	@Override
	protected int getMeta()
	{
		return IIBlockTypes_FenceGate.ALUMINIUM_CHAIN.getMeta();
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
		protected SoundEvent getOpeningSound()
		{
			return IISounds.gateMetalOpen;
		}

		@Override
		protected SoundEvent getClosingSound()
		{
			return IISounds.gateMetalClose;
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
