package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntityDieselEngine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * Defines the Diesel Engine structure and its ports.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.2
 * @since 14.10.2026
 */
public class MultiblockDieselEngine extends MultiblockStuctureBase<TileEntityDieselEngine>
{
	public static MultiblockDieselEngine INSTANCE;

	public MultiblockDieselEngine()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/diesel_engine"));
		offset = new Vec3i(1, 1, 1);
		INSTANCE = this;
		addPOI(MultiblockPOI.FLUID_INPUT, "input");
		addPOI(MultiblockPOI.ROTARY_OUTPUT, "rotary_output");
		addPOI(MultiblockPOI.MISC_HATCH, "maintenance_hatch");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockWoodenMultiblock;
	}

	@Override
	protected int getMeta()
	{
		return WoodenMultiblocks.DIESEL_ENGINE.getMeta();
	}

	@Override
	protected TileEntityDieselEngine getMBInstance()
	{
		return new TileEntityDieselEngine();
	}
}
