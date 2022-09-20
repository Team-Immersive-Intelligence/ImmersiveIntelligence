package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

public class MultiblockEmplacement extends MultiblockStuctureBase<TileEntityEmplacement>
{
	public static MultiblockEmplacement INSTANCE;

	public MultiblockEmplacement()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/emplacement"));
		offset = new Vec3i(1, 4, 0);
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
	protected TileEntityEmplacement placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.EMPLACEMENT.getMeta()));
		return (TileEntityEmplacement)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityEmplacement getMBInstance()
	{
		return new TileEntityEmplacement();
	}
}
