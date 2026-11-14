package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasDummyBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * @author Avalon (avalon@iiteam.net)
 * @since 8.10.2026
 */

public class TileEntityNewspaperStand extends TileEntityIIDirectional implements IHasDummyBlocks, IBlockBounds
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL);

	@SyncNBT(name = "dummy")
	public int subBlockID;

	@Override
	public void placeDummies(BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		BlockPos upperPos = pos.up();
		world.setBlockState(upperPos, state);
		if(world.getTileEntity(upperPos) instanceof TileEntityNewspaperStand)
		{
			TileEntityNewspaperStand upper = (TileEntityNewspaperStand)world.getTileEntity(upperPos);
			upper.subBlockID = 1;
			upper.facing = facing;
			upper.markDirty();
			world.notifyBlockUpdate(upperPos, state, state, 3);
		}
	}

	@Override
	public void breakDummies(BlockPos pos, IBlockState state)
	{
		BlockPos masterPos = pos.down(subBlockID);
		for(int i = 0; i <= 1; i++)
		{
			BlockPos partPos = masterPos.up(i);
			if(world.getTileEntity(partPos) instanceof TileEntityNewspaperStand)
				world.setBlockToAir(partPos);
		}
	}

	@Override
	public boolean isDummy()
	{
		return subBlockID > 0;
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public EnumFacing getFacingForPlacement(EntityLivingBase placer, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		return placer.getHorizontalFacing().getOpposite();
	}
}
