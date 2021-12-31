package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockChemicalPainter extends MultiblockStuctureBase<TileEntityChemicalPainter>
{
	public static MultiblockChemicalPainter instance = new MultiblockChemicalPainter();

	public MultiblockChemicalPainter()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/chemical_painter"));
		offset = new Vec3i(2,0, 0);
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
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.CHEMICAL_PAINTER.getMeta()));
		return (TileEntityChemicalPainter)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityChemicalPainter getMBInstance()
	{
		return new TileEntityChemicalPainter();
	}
}
