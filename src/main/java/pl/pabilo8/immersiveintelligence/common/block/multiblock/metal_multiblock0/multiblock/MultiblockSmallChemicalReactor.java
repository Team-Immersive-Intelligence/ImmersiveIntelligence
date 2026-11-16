package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.BlockIIMetalMultiblock0.MetalMultiblocks0;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity.TileEntitySmallChemicalReactor;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.BlockIIMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;
/**
 * @author Avalon (avalon@iiteam.net)
 * @since 14.11.2026
 */
public class MultiblockSmallChemicalReactor extends MultiblockStuctureBase<TileEntitySmallChemicalReactor>
{
	public static MultiblockSmallChemicalReactor INSTANCE;

	public MultiblockSmallChemicalReactor()
	{
		super(new ResourceLocation(ImmersiveIntelligence.MODID, "multiblocks/small_chemical_reactor"));
		offset = new Vec3i(0, 0, 4);
		INSTANCE = this;
	}

	@Override
	public boolean createStructure(World world, BlockPos startPos, EnumFacing side, EntityPlayer player)
	{
		EnumFacing preferred = side.getOpposite();
		if(preferred.getAxis().isVertical())
			preferred = EnumFacing.fromAngle(player.rotationYaw);

		EnumFacing facing = findFormationFacing(world, startPos, preferred);
		return facing!=null&&super.createStructure(world, startPos, facing.getOpposite(), player);
	}

	@Nullable
	EnumFacing findFormationFacing(World world, BlockPos startPos, EnumFacing preferred)
	{
		if(structureCheck(world, startPos, preferred, false)||structureCheck(world, startPos, preferred, true))
			return preferred;

		for(EnumFacing facing : EnumFacing.HORIZONTALS)
			if(facing!=preferred&&(structureCheck(world, startPos, facing, false)||structureCheck(world, startPos, facing, true)))
				return facing;
		return null;
	}

	@Override
	protected boolean useNewOffset()
	{
		return false;
	}

	@Override
	protected BlockIIMultiblock<?> getBlock()
	{
		return IIContent.blockMetalMultiblock0;
	}

	@Override
	protected int getMeta()
	{
		return MetalMultiblocks0.SMALL_CHEMICAL_REACTOR.getMeta();
	}

	@Override
	protected TileEntitySmallChemicalReactor getMBInstance()
	{
		return new TileEntitySmallChemicalReactor();
	}
}
