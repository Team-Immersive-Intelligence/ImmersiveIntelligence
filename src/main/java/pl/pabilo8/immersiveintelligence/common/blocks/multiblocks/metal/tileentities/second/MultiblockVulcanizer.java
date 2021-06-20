package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockVulcanizer extends MultiblockStuctureBase<TileEntityVulcanizer>
{
	public static MultiblockVulcanizer instance = new MultiblockVulcanizer();

	public MultiblockVulcanizer()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/vulcanizer"));
		offset = new Vec3i(2, 1, 0);
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityVulcanizer placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.VULCANIZER.getMeta()));
		return (TileEntityVulcanizer)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityVulcanizer getMBInstance()
	{
		return new TileEntityVulcanizer();
	}
}
