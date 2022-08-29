package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockArtilleryHowitzer extends MultiblockStuctureBase<TileEntityArtilleryHowitzer>
{
	public static MultiblockArtilleryHowitzer instance = new MultiblockArtilleryHowitzer();

	public MultiblockArtilleryHowitzer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/artillery_howitzer"));
		offset = new Vec3i(4, 5, 0);
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock0, 255, 0);
	}

	@Nullable
	@Override
	protected TileEntityArtilleryHowitzer placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.ARTILLERY_HOWITZER.getMeta()));
		return (TileEntityArtilleryHowitzer)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityArtilleryHowitzer getMBInstance()
	{
		return new TileEntityArtilleryHowitzer();
	}
}
