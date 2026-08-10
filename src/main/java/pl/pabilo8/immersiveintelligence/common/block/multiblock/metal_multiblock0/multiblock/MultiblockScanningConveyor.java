package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityScanningConveyor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockScanningConveyor extends MultiblockStuctureBase<TileEntityScanningConveyor>
{
	public static MultiblockScanningConveyor INSTANCE;

	public MultiblockScanningConveyor()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/scanning_conveyor"));
		offset = new Vec3i(0, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.DATA_OUTPUT, "data");
		addPOI(MultiblockPOI.ENERGY_INPUT, "power");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
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
		return MetalMultiblocks0.SCANNING_CONVEYOR.getMeta();
	}

	@Override
	protected TileEntityScanningConveyor getMBInstance()
	{
		return new TileEntityScanningConveyor();
	}
}
