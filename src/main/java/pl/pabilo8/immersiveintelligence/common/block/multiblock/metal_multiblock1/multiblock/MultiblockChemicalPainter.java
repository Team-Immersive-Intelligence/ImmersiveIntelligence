package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityChemicalPainter;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;

import javax.annotation.Nullable;

public class MultiblockChemicalPainter extends MultiblockStuctureBase<TileEntityChemicalPainter>
{
	public static MultiblockChemicalPainter INSTANCE;

	public MultiblockChemicalPainter()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/chemical_painter"));
		offset = new Vec3i(2,0, 0);
		INSTANCE = this;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityChemicalPainter placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.CHEMICAL_PAINTER.getMeta()));
		return (TileEntityChemicalPainter)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityChemicalPainter getMBInstance()
	{
		return new TileEntityChemicalPainter();
	}
}
