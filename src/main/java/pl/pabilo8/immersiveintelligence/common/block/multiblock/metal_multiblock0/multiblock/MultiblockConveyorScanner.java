package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityConveyorScanner;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockConveyorScanner extends MultiblockStuctureBase<TileEntityConveyorScanner>
{
	public static MultiblockConveyorScanner INSTANCE;

	public MultiblockConveyorScanner()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/conveyor_scanner"));
		offset = new Vec3i(0, 1, 0);
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
		return MetalMultiblocks0.CONVEYOR_SCANNER.getMeta();
	}

	@Override
	protected TileEntityConveyorScanner getMBInstance()
	{
		return new TileEntityConveyorScanner();
	}
}
