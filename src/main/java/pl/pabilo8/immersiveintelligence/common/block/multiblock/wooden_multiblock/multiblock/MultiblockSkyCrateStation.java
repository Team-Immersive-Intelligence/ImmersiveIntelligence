package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
 */
public class MultiblockSkyCrateStation extends MultiblockStuctureBase<TileEntitySkyCrateStation>
{
	public static MultiblockSkyCrateStation INSTANCE;

	public MultiblockSkyCrateStation()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/skycrate_station"));
		offset = new Vec3i(1, 1, 1);
		INSTANCE = this;
		addPOI(MultiblockPOI.SKYCRATE_WIRE_MOUNT, "wire");
		addPOI(MultiblockPOI.ROTARY_INPUT, "rotary");
		addPOI(MultiblockPOI.REDSTONE_INPUT, "redstone");
		addPOI(MultiblockPOI.MISC_FLAGPOLE, "flag");
		addPOI(MultiblockPOI.ITEM_INPUT, "cargo_input");
		addPOI(MultiblockPOI.ITEM_OUTPUT, "cargo_output");
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
		return WoodenMultiblocks.SKYCRATE_STATION.getMeta();
	}

	@Override
	protected TileEntitySkyCrateStation getMBInstance()
	{
		return new TileEntitySkyCrateStation();
	}
}