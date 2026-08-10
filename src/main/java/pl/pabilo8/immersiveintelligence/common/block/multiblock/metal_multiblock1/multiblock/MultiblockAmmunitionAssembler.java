package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

public class MultiblockAmmunitionAssembler extends MultiblockStuctureBase<TileEntityAmmunitionAssembler>
{
	public static final int SLOT_CORE = 0;
	public static final int SLOT_CASING = 1;
	public static final int SLOT_OUTPUT = 2;
	public static final String NBT_KEY_EFFECT = "effect";
	public static MultiblockAmmunitionAssembler INSTANCE;

	public MultiblockAmmunitionAssembler()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/ammunition_assembler"));
		offset = new Vec3i(1, 0, 0);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ITEM_INPUT, "item_input");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "item_output");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone_input");
		addPOI(MultiblockPOI.DATA_INPUT, "data_input");
		addPOI(MultiblockPOI.ENERGY_INPUT, "energy_input");
	}

	@Override
	protected BlockIIMultiblock<MetalMultiblocks1> getBlock()
	{
		return IIContent.blockMetalMultiblock1;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks1.AMMUNITION_ASSEMBLER.getMeta();
	}

	@Override
	protected TileEntityAmmunitionAssembler getMBInstance()
	{
		return new TileEntityAmmunitionAssembler();
	}
}
