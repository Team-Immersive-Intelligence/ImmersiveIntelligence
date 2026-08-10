package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityFiller;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

public class MultiblockFiller extends MultiblockStuctureBase<TileEntityFiller>
{
	public static MultiblockFiller INSTANCE;
	public static int SLOT_DUST = 0;
	public static int SLOT_INPUT = 1;

	public MultiblockFiller()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/filler"));
		offset = new Vec3i(1, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ITEM_INPUT, "inputs");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "conveyor_out");
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.FILLER.getMeta();
	}

	@Override
	protected TileEntityFiller getMBInstance()
	{
		return new TileEntityFiller();
	}
}
