package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockDataInputMachine extends MultiblockStuctureBase<TileEntityDataInputMachine>
{
	public static MultiblockDataInputMachine INSTANCE;

	public MultiblockDataInputMachine()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/data_input_machine"));
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
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock0, 255, 0);
	}

	@Nullable
	@Override
	protected TileEntityDataInputMachine placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock0.getStateFromMeta(MetalMultiblocks0.DATA_INPUT_MACHINE.getMeta()));
		return (TileEntityDataInputMachine)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityDataInputMachine getMBInstance()
	{
		return new TileEntityDataInputMachine();
	}
}
