package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntityArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class MultiblockArithmeticLogicMachine extends MultiblockStuctureBase<TileEntityArithmeticLogicMachine>
{
	public static MultiblockArithmeticLogicMachine INSTANCE;

	public MultiblockArithmeticLogicMachine()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/arithmetic_logic_machine"));
		offset = new Vec3i(0, 1, 1);
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
	protected TileEntityArithmeticLogicMachine placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock0.getStateFromMeta(MetalMultiblocks0.ARITHMETIC_LOGIC_MACHINE.getMeta()));
		return (TileEntityArithmeticLogicMachine)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityArithmeticLogicMachine getMBInstance()
	{
		return new TileEntityArithmeticLogicMachine();
	}
}
