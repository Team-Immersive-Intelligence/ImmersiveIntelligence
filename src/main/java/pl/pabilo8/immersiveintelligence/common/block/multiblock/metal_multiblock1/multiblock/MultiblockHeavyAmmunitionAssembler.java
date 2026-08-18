package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityHeavyAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

public class MultiblockHeavyAmmunitionAssembler extends MultiblockStuctureBase<TileEntityHeavyAmmunitionAssembler>
{
	public static MultiblockHeavyAmmunitionAssembler INSTANCE;

	public MultiblockHeavyAmmunitionAssembler()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/heavy_ammunition_assembler"));
		offset = new Vec3i(4, 0, 1);
		INSTANCE = this;

		//POI
		addPOI(MultiblockPOI.ITEM_INPUT, "item_inputs");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "item_out");
		addPOI(MultiblockPOI.FLUID_INPUT, "component_fluid_in");
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
		return MetalMultiblocks1.HEAVY_AMMUNITION_ASSEMBLER.getMeta();
	}

	@Override
	protected TileEntityHeavyAmmunitionAssembler getMBInstance()
	{
		return new TileEntityHeavyAmmunitionAssembler();
	}
}
