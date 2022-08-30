package pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.second;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockFiller extends MultiblockStuctureBase<TileEntityFiller>
{
	public static MultiblockFiller instance = new MultiblockFiller();

	public MultiblockFiller()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/filler"));
		offset = new Vec3i(1, 1, 0);
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Nullable
	@Override
	protected TileEntityFiller placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.FILLER.getMeta()));
		return (TileEntityFiller)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityFiller getMBInstance()
	{
		return new TileEntityFiller();
	}
}
