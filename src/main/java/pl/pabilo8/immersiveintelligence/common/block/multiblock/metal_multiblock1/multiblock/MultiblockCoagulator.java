package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

public class MultiblockCoagulator extends MultiblockStuctureBase<TileEntityCoagulator>
{
	public static MultiblockCoagulator INSTANCE;
	public static final int SLOT_INPUT1 = 0, SLOT_INPUT2 = 1, SLOT_OUTPUT1 = 2, SLOT_OUTPUT2 = 3, SLOT_OUTPUT = 4;

	public MultiblockCoagulator()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coagulator"));
		offset = new Vec3i(3, 1, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.FLUID_INPUT, "fluid_inputs");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "item_outputs");
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.MISC_CONTROL_PANEL, "control_panel");
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.COAGULATOR.getMeta();
	}

	@Override
	protected TileEntityCoagulator getMBInstance()
	{
		return new TileEntityCoagulator();
	}
}
