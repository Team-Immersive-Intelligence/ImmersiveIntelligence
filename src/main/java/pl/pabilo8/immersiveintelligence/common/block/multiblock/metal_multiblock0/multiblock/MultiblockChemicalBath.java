package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityChemicalBath;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class MultiblockChemicalBath extends MultiblockStructureBase<TileEntityChemicalBath>
{
	public static MultiblockChemicalBath INSTANCE;
	public static final int ITEM_IN = 0;
	public static final int ITEM_OUT = 1;
	public static final int BUCKET_IN = 2;
	public static final int BUCKET_OUT = 3;

	public MultiblockChemicalBath()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/chemical_bath"));
		offset = new Vec3i(2, 1, 1);
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
		return MetalMultiblocks0.CHEMICAL_BATH.getMeta();
	}

	@Override
	protected TileEntityChemicalBath getMBInstance()
	{
		return new TileEntityChemicalBath();
	}
}
