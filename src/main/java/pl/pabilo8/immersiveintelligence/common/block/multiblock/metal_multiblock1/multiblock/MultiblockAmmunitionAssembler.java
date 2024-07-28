package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionAssembler;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

public class MultiblockAmmunitionAssembler extends MultiblockStuctureBase<TileEntityAmmunitionAssembler>
{
	public static MultiblockAmmunitionAssembler INSTANCE;

	public MultiblockAmmunitionAssembler()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/ammunition_assembler"));
		offset = new Vec3i(1, 0, 0);
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
		return MetalMultiblocks1.AMMUNITION_ASSEMBLER.getMeta();
	}

	@Override
	protected TileEntityAmmunitionAssembler getMBInstance()
	{
		return new TileEntityAmmunitionAssembler();
	}
}
