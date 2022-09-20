package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityCoagulator;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.MetalMultiblocks1;

import javax.annotation.Nullable;

public class MultiblockCoagulator extends MultiblockStuctureBase<TileEntityCoagulator>
{
	public static MultiblockCoagulator INSTANCE;

	public MultiblockCoagulator()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coagulator"));
		offset = new Vec3i(3,1, 0);
		INSTANCE = this;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityCoagulator placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(MetalMultiblocks1.COAGULATOR.getMeta()));
		return (TileEntityCoagulator)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityCoagulator getMBInstance()
	{
		return new TileEntityCoagulator();
	}
}
