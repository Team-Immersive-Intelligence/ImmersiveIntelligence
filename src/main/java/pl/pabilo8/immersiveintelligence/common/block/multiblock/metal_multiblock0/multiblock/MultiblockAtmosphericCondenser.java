package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityAtmosphericCondenser;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 04.11.2026
 */

public class MultiblockAtmosphericCondenser extends MultiblockStuctureBase<TileEntityAtmosphericCondenser>
{
	public static MultiblockAtmosphericCondenser INSTANCE;
	public static final int BUCKET_ARGON_IN = 0, BUCKET_ARGON_OUT = 1;
	public static final int BUCKET_NITROGEN_IN = 2, BUCKET_NITROGEN_OUT = 3;
	public static final int BUCKET_OXYGEN_IN = 4, BUCKET_OXYGEN_OUT = 5;

	public MultiblockAtmosphericCondenser()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/atmospheric_condenser"));
		offset = new Vec3i(0, 0, 0);
		INSTANCE = this;

		addPOI(MultiblockPOI.ENERGY_INPUT, "energy");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.FLUID_OUTPUT, "output_argon", "output_nitrogen", "output_oxygen");
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
		return MetalMultiblocks0.ATMOSPHERIC_CONDENSER.getMeta();
	}

	@Override
	protected TileEntityAtmosphericCondenser getMBInstance()
	{
		return new TileEntityAtmosphericCondenser();
	}
}
