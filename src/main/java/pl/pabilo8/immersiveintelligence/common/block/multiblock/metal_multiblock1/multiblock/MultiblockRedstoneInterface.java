package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockRedstoneInterface extends MultiblockStuctureBase<TileEntityRedstoneInterface>
{
	public static MultiblockRedstoneInterface INSTANCE;

	public MultiblockRedstoneInterface()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/redstone_data_interface"));
		offset = new Vec3i(0, 0, 0);
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
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityRedstoneInterface placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.REDSTONE_DATA_INTERFACE.getMeta()));
		return (TileEntityRedstoneInterface)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityRedstoneInterface getMBInstance()
	{
		return new TileEntityRedstoneInterface();
	}
}
