package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 17.10.2026
 */
public class MultiblockChemicalReactor extends MultiblockStuctureBase<TileEntityChemicalReactor>
{
	public static MultiblockChemicalReactor INSTANCE;

	//Set 1 slots
	public static int SLOT_SET1_FLUID_INPUT1 = 0;
	public static int SLOT_SET1_FLUID_INPUT2 = 1;
	public static int SLOT_SET1_FLUID_OUTPUT = 2;
	public static int SLOT_SET1_ITEM_INPUT = 3;
	public static int SLOT_SET1_CATALYST_INPUT = 4;
	public static int SLOT_SET1_ITEM_OUTPUT = 5;

	//Set 2 slots
	public static int SLOT_SET2_FLUID_INPUT1 = 6;
	public static int SLOT_SET2_FLUID_INPUT2 = 7;
	public static int SLOT_SET2_FLUID_OUTPUT = 8;
	public static int SLOT_SET2_ITEM_INPUT = 9;
	public static int SLOT_SET2_CATALYST_INPUT = 10;
	public static int SLOT_SET2_ITEM_OUTPUT = 11;

	//Set 3 slots
	public static int SLOT_SET3_FLUID_INPUT1 = 12;
	public static int SLOT_SET3_FLUID_INPUT2 = 13;
	public static int SLOT_SET3_FLUID_OUTPUT = 14;
	public static int SLOT_SET3_ITEM_INPUT = 15;
	public static int SLOT_SET3_CATALYST_INPUT = 16;
	public static int SLOT_SET3_ITEM_OUTPUT = 17;

	public MultiblockChemicalReactor()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/chemical_reactor"));
		offset = new Vec3i(10, 0, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ENERGY_INPUT, "power");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");

		//Set 1
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_1.1");
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_1.2");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_item_1");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_catalyst_1");
		addPOI(MultiblockPOI.FLUID_OUTPUT, "output_fluid_1");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "output_item_1");

		//Set 2
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_2.1");
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_2.2");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_item_2");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_catalyst_2");
		addPOI(MultiblockPOI.FLUID_OUTPUT, "output_fluid_2");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "output_item_2");

		//Set 3
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_3.1");
		addPOI(MultiblockPOI.FLUID_INPUT, "input_fluid_3.2");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_item_3");
		addPOI(MultiblockPOI.ITEM_INPUT, "input_catalyst_3");
		addPOI(MultiblockPOI.FLUID_OUTPUT, "output_fluid_3");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "output_item_3");
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
		return MetalMultiblocks0.CHEMICAL_REACTOR.getMeta();
	}

	@Override
	protected TileEntityChemicalReactor getMBInstance()
	{
		return new TileEntityChemicalReactor();
	}
}
