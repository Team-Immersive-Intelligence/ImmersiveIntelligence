package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySawmill;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 13-04-2020
 */
public class MultiblockSawmill extends MultiblockStuctureBase<TileEntitySawmill>
{
	public static MultiblockSawmill INSTANCE;

	public MultiblockSawmill()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/sawmill"));
		offset = new Vec3i(2, 0, 0);
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
	protected TileEntitySawmill placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockWoodenMultiblock.getStateFromMeta(WoodenMultiblocks.SAWMILL.getMeta()));
		return (TileEntitySawmill)world.getTileEntity(pos);
	}

	@Override
	protected TileEntitySawmill getMBInstance()
	{
		return new TileEntitySawmill();
	}
}