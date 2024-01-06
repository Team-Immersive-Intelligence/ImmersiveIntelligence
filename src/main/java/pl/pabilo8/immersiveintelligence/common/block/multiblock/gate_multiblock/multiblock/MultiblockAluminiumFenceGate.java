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
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.BlockIIGateMultiblock.IIBlockTypes_FenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.multiblock.MultiblockAluminiumFenceGate.TileEntityAluminiumFenceGate;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.gate_multiblock.tileentity.TileEntityGateBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 23.12.2021
 */
public class MultiblockAluminiumFenceGate extends MultiblockFenceGateBase<TileEntityAluminiumFenceGate>
{
	public static MultiblockAluminiumFenceGate INSTANCE;

	public MultiblockAluminiumFenceGate()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/aluminium_fence_gate"));
		INSTANCE = this;
	}

	@Override
	protected int getMeta()
	{
		return IIBlockTypes_FenceGate.ALUMINIUM.getMeta();
	}

	@Override
	protected TileEntityAluminiumFenceGate getMBInstance()
	{
		return new TileEntityAluminiumFenceGate();
	}

	public static class TileEntityAluminiumFenceGate extends TileEntityGateBase<TileEntityAluminiumFenceGate>
	{
		public TileEntityAluminiumFenceGate()
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
