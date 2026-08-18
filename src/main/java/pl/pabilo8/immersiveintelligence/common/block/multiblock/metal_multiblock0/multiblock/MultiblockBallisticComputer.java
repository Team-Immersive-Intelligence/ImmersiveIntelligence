package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityBallisticComputer;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockBallisticComputer extends MultiblockStuctureBase<TileEntityBallisticComputer>
{
	public static MultiblockBallisticComputer INSTANCE;

	public MultiblockBallisticComputer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/ballistic_computer"));
		offset = new Vec3i(0, 0, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.DATA_INPUT, "data_input");
		addPOI(MultiblockPOI.DATA_OUTPUT, "data_output");
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
		return MetalMultiblocks0.BALLISTIC_COMPUTER.getMeta();
	}

	@Override
	protected TileEntityBallisticComputer getMBInstance()
	{
		return new TileEntityBallisticComputer();
	}
}
