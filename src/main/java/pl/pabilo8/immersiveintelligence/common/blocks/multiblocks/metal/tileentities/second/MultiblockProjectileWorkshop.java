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

public class MultiblockProjectileWorkshop extends MultiblockStuctureBase<TileEntityProjectileWorkshop>
{
	public static MultiblockProjectileWorkshop instance = new MultiblockProjectileWorkshop();

	public MultiblockProjectileWorkshop()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/projectile_workshop"));
		offset = new Vec3i(2, 1, 0);
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityProjectileWorkshop placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.PROJECTILE_WORKSHOP.getMeta()));
		return (TileEntityProjectileWorkshop)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityProjectileWorkshop getMBInstance()
	{
		return new TileEntityProjectileWorkshop();
	}
}
