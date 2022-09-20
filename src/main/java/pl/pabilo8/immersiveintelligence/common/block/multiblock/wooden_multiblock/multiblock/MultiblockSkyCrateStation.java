package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 2019-06-01
 */
public class MultiblockSkyCrateStation extends MultiblockStuctureBase<TileEntitySkyCrateStation>
{
	public static MultiblockSkyCrateStation INSTANCE;

	public MultiblockSkyCrateStation()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/skycrate_station"));
		offset = new Vec3i(1, 1, 1);
		INSTANCE = this;
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockWoodenMultiblock, 255, 0);
	}

	@Nullable
	@Override
	protected TileEntitySkyCrateStation placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockWoodenMultiblock.getStateFromMeta(WoodenMultiblocks.SKYCRATE_STATION.getMeta()));
		return (TileEntitySkyCrateStation)world.getTileEntity(pos);
	}

	@Override
	protected TileEntitySkyCrateStation getMBInstance()
	{
		return new TileEntitySkyCrateStation();
	}
}