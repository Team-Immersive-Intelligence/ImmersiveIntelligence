package pl.pabilo8.immersiveintelligence.common.block.fortification.tileentity;

import net.minecraft.util.math.AxisAlignedBB;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 16.08.2019
 */
public class TileEntitySandbags extends TileEntityIIDirectional implements IAdvancedBounds
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL)
			.withRotation(true);

	@Override
	@Nonnull
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		List<AxisAlignedBB> aabb = new ArrayList<>();
		switch(facing)
		{
			case NORTH:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0f, 0f, 0.5f));
			}
			break;
			case SOUTH:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0f, 0f, 0.5f).offset(0f, 0f, 0.5f));
			}
			break;
			case EAST:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0.5f, 0f, 0f).offset(0.5f, 0f, 0f));
			}
			break;
			case WEST:
			{
				aabb.add(new AxisAlignedBB(pos).contract(0.5f, 0f, 0f));
			}
			break;
		}

		return aabb;
	}
}
