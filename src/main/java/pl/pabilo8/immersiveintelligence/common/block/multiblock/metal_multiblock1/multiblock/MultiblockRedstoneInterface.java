package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockRedstoneInterface extends MultiblockStuctureBase<TileEntityRedstoneInterface>
{
	public static MultiblockRedstoneInterface INSTANCE;

	public MultiblockRedstoneInterface()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/redstone_data_interface"));
		offset = new Vec3i(0, 0, 0);
		INSTANCE = this;
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.REDSTONE_DATA_INTERFACE.getMeta();
	}

	@Override
	protected TileEntityRedstoneInterface getMBInstance()
	{
		return new TileEntityRedstoneInterface();
	}
}
