package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFlagpole;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

public class MultiblockFlagpole extends MultiblockStuctureBase<TileEntityFlagpole>
{
	public static MultiblockFlagpole INSTANCE;

	public MultiblockFlagpole()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/flagpole"));
		offset = new Vec3i(1, 3, 0);
		INSTANCE = this;
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityFlagpole placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.FLAGPOLE.getMeta()));
		return (TileEntityFlagpole)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityFlagpole getMBInstance()
	{
		return new TileEntityFlagpole();
	}
}
