package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class MultiblockSkyCratePost extends MultiblockStuctureBase<TileEntitySkyCratePost>
{
	public static MultiblockSkyCratePost INSTANCE;

	public MultiblockSkyCratePost()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/skycrate_post"));
		offset = new Vec3i(0, 1, 0);
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
		return WoodenMultiblocks.SKYCRATE_POST.getMeta();
	}

	@Override
	protected TileEntitySkyCratePost getMBInstance()
	{
		return new TileEntitySkyCratePost();
	}
}