package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedMultiblock;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.blocks.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second.TileEntityRadar;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock0;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockPacker extends MultiblockStuctureBase<TileEntityPacker>
{
	public static MultiblockPacker instance = new MultiblockPacker();

	public MultiblockPacker()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/packer"));
		offset = new Vec3i(1,1, 0);
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock0, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityPacker placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock0.getStateFromMeta(IIBlockTypes_MetalMultiblock0.PACKER.getMeta()));
		return (TileEntityPacker)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityPacker getMBInstance()
	{
		return new TileEntityPacker();
	}
}
