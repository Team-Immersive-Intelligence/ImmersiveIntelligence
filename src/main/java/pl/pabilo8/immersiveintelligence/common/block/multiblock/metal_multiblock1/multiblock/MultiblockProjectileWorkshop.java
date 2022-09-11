package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityProjectileWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockProjectileWorkshop extends MultiblockStuctureBase<TileEntityProjectileWorkshop>
{
	public static MultiblockProjectileWorkshop INSTANCE;

	public MultiblockProjectileWorkshop()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/projectile_workshop"));
		offset = new Vec3i(2, 1, 0);
		INSTANCE = this;
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
