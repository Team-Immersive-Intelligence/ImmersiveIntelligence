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
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockPrecisionAssembler extends MultiblockStuctureBase<TileEntityPrecisionAssembler>
{
	public static MultiblockPrecisionAssembler INSTANCE;

	public static int SLOT_TOOL1 = 0, SLOT_TOOL2 = 1, SLOT_TOOL3 = 2;
	public static int SLOT_SCHEME = 3;
	public static int SLOT_INGREDIENT1 = 4, SLOT_INGREDIENT2 = 5, SLOT_INGREDIENT3 = 6, SLOT_INGREDIENT4 = 7;
	public static int SLOT_OUTPUT = 8, SLOT_OUTPUT_TRASH = 9;

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
