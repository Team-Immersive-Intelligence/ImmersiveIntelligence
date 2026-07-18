package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import net.minecraft.util.EnumFacing.Axis;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 06.07.2020
 */
public class TileEntityMineSign extends TileEntityIIDirectional implements IBlockBounds
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL)
			.withMirroringOnPlacement(true);
	private static final float[] boundsX = new float[]{0.4375f, 0, 0, 0.5625f, 1, 1};
	private static final float[] boundsY = new float[]{0, 0, 0.4375f, 1, 1, 0.5625f};

	@Override
	@Nonnull
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public float[] getBlockBounds()
	{
		return facing.getAxis()==Axis.X?boundsX: boundsY;
	}

}
