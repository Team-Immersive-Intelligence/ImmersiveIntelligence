package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

public class MultiblockCoagulator extends MultiblockStuctureBase<TileEntityCoagulator>
{
	public static MultiblockCoagulator INSTANCE;

	public MultiblockCoagulator()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coagulator"));
		offset = new Vec3i(3, 1, 0);
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
		return MetalMultiblocks1.COAGULATOR.getMeta();
	}

	@Override
	protected TileEntityCoagulator getMBInstance()
	{
		return new TileEntityCoagulator();
	}
}
