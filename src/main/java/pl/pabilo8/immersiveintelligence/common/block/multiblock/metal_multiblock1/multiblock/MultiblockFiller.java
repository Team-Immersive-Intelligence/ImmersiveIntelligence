package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockFiller extends MultiblockStuctureBase<TileEntityFiller>
{
	public static MultiblockFiller INSTANCE;

	public MultiblockFiller()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/filler"));
		offset = new Vec3i(1, 1, 0);
		INSTANCE = this;
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
