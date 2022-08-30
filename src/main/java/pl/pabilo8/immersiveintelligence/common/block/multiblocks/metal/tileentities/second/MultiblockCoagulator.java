package pl.pabilo8.immersiveintelligence.common.block.multiblocks.metal.tileentities.second;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.types.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockCoagulator extends MultiblockStuctureBase<TileEntityCoagulator>
{
	public static MultiblockCoagulator instance = new MultiblockCoagulator();

	public MultiblockCoagulator()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/coagulator"));
		offset = new Vec3i(3,1, 0);
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
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.COAGULATOR.getMeta()));
		return (TileEntityCoagulator)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityCoagulator getMBInstance()
	{
		return new TileEntityCoagulator();
	}
}
