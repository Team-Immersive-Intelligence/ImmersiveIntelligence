package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileCokeBattery;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Carver (pabilo@iiteam.net)
 * @since 25.05.2026
 */
public class MultiblockCokeBattery extends MultiblockStuctureBase<TileCokeBattery>
{
	public static pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockCokeBattery INSTANCE;
	public static int SLOT_T0_BUCKET_INPUT = 0;
	public static int SLOT_T0_BUCKET_OUTPUT = 1;
	public static int SLOT_T1_BUCKET_INPUT = 2;
	public static int SLOT_T1_BUCKET_OUTPUT = 4;
	public static int SLOT_T2_BUCKET_INPUT = 3;
	public static int SLOT_T2_BUCKET_OUTPUT = 5;

	public MultiblockCokeBattery()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coke_battery"));
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
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.ELECTROLYZER.getMeta();
	}

	@Override
	protected TileCokeBattery getMBInstance()
	{
		return new TileCokeBattery();
	}
}

