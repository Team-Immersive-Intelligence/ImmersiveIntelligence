package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCokeBattery;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Carver (carver@iiteam.net)
 * @since 25.05.2026
 */
public class MultiblockCokeBattery extends MultiblockStuctureBase<TileEntityCokeBattery>
{
	public static MultiblockCokeBattery INSTANCE;
	public static final int SLOT_T0_BUCKET_INPUT = 0;
	public static final int SLOT_T0_BUCKET_OUTPUT = 1;
	public static final int SLOT_T1_BUCKET_INPUT = 2;
	public static final int SLOT_T1_BUCKET_OUTPUT = 4;
	public static final int SLOT_T2_BUCKET_INPUT = 3;
	public static final int SLOT_T2_BUCKET_OUTPUT = 5;

	public MultiblockCokeBattery()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coke_battery"));
		offset = new Vec3i(1, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy_input");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone_input");
		addPOI(MultiblockPOI.FLUID_INPUT, "fluid_input");
		addPOI(MultiblockPOI.FLUID_OUTPUT, "fluid_output");
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
		return MetalMultiblocks1.COKE_BATTERY.getMeta();
	}

	@Override
	protected TileEntityCokeBattery getMBInstance()
	{
		return new TileEntityCokeBattery();
	}
}
