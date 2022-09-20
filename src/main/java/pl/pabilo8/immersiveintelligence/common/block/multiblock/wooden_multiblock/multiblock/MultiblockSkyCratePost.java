package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.BlockIIWoodenMultiblock.WoodenMultiblocks;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity.TileEntitySkyCratePost;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

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
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockWoodenMultiblock, 255, 0);
	}

	@Nullable
	@Override
	protected TileEntitySkyCratePost placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockWoodenMultiblock.getStateFromMeta(WoodenMultiblocks.SKYCRATE_POST.getMeta()));
		return (TileEntitySkyCratePost)world.getTileEntity(pos);
	}

	@Override
	protected TileEntitySkyCratePost getMBInstance()
	{
		return new TileEntitySkyCratePost();
	}
}