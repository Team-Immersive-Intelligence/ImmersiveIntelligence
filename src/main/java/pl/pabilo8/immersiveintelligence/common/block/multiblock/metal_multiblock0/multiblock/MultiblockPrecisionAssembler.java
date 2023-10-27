package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityPrecisionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockPrecisionAssembler extends MultiblockStuctureBase<TileEntityPrecisionAssembler>
{
	public static MultiblockPrecisionAssembler INSTANCE;

	public MultiblockPrecisionAssembler()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/precision_assembler"));
		offset = new Vec3i(2, 0, 0);
		INSTANCE = this;
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockMetalMultiblock0;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.PRECISION_ASSEMBLER.getMeta();
	}

	@Override
	protected TileEntityPrecisionAssembler getMBInstance()
	{
		return new TileEntityPrecisionAssembler();
	}
}
