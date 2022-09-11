package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.TileEntityAmmunitionWorkshop;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.BlockIIMetalMultiblock1.IIBlockTypes_MetalMultiblock1;

import javax.annotation.Nullable;

public class MultiblockAmmunitionWorkshop extends MultiblockStuctureBase<TileEntityAmmunitionWorkshop>
{
	public static MultiblockAmmunitionWorkshop INSTANCE;

	public MultiblockAmmunitionWorkshop()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/ammunition_workshop"));
		offset = new Vec3i(1, 0, 0);
		INSTANCE = this;
	}

	@Override
	protected void addBlockEvent(World world, BlockPos pos)
	{
		world.addBlockEvent(pos, IIContent.blockMetalMultiblock1, 255, 0);
	}

	@Override
	@Nullable
	protected TileEntityAmmunitionWorkshop placeTile(World world, BlockPos pos)
	{
		world.setBlockState(pos, IIContent.blockMetalMultiblock1.getStateFromMeta(IIBlockTypes_MetalMultiblock1.AMMUNITION_WORKSHOP.getMeta()));
		return (TileEntityAmmunitionWorkshop)world.getTileEntity(pos);
	}

	@Override
	protected TileEntityAmmunitionWorkshop getMBInstance()
	{
		return new TileEntityAmmunitionWorkshop();
	}
}
