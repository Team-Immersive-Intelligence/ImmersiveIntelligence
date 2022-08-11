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
public class MultiblockBallisticComputer extends MultiblockStuctureBase<TileEntityBallisticComputer>
{
	public static MultiblockBallisticComputer instance = new MultiblockBallisticComputer();

	public MultiblockBallisticComputer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/ballistic_computer"));
		offset = new Vec3i(0,0, 0);
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
	protected TileEntityBallisticComputer placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.BALLISTIC_COMPUTER.getMeta()));
		return (TileEntityBallisticComputer)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityBallisticComputer getMBInstance()
	{
		return new TileEntityBallisticComputer();
	}
}
