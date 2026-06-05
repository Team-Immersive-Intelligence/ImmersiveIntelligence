package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCartStation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStructureBase;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 01.06.2019
 */
public class MultiblockSkyCartStation extends MultiblockStructureBase<TileEntitySkyCartStation>
{
	public static MultiblockSkyCartStation INSTANCE;

	public MultiblockSkyCartStation()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/skycart_station"));
		offset = new Vec3i(1, 1, 1);
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
		return IIContent.blockWoodenMultiblock;
	}

	@Override
	protected int getMeta()
	{
		return WoodenMultiblocks.SKYCART_STATION.getMeta();
	}

	@Override
	protected TileEntitySkyCartStation getMBInstance()
	{
		return new TileEntitySkyCartStation();
	}
}
