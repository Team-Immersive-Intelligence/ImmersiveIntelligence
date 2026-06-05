package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityElectrolyzer;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockElectrolyzer extends MultiblockStructureBase<TileEntityElectrolyzer>
{
	public static MultiblockElectrolyzer INSTANCE;
	public static int SLOT_T0_BUCKET_INPUT = 0;
	public static int SLOT_T0_BUCKET_OUTPUT = 1;
	public static int SLOT_T1_BUCKET_INPUT = 2;
	public static int SLOT_T1_BUCKET_OUTPUT = 4;
	public static int SLOT_T2_BUCKET_INPUT = 3;
	public static int SLOT_T2_BUCKET_OUTPUT = 5;

	public MultiblockElectrolyzer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/electrolyzer"));
		offset = new Vec3i(1, 1, 0);
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
		return MetalMultiblocks0.ELECTROLYZER.getMeta();
	}

	@Override
	protected TileEntityElectrolyzer getMBInstance()
	{
		return new TileEntityElectrolyzer();
	}
}
