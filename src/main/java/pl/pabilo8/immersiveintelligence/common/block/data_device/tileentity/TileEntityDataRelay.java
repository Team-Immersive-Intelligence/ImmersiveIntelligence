package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingLimitation;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional.FacingSettings;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectionalConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 20.07.2026
 * @ii-approved 0.3.1
 * @since 31.05.2019
 */
public class TileEntityDataRelay extends TileEntityIIDirectionalConnectable implements ITickable, IBlockBounds
{
	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.SIDE_CLICKED)
			.withMirroringOnPlacement(true);
	boolean firstTick = true;

	@Override
	public void update()
	{
		if(!world.isRemote&&firstTick)
		{
			Set<Connection> conns = ImmersiveNetHandler.INSTANCE.getConnections(world, pos);
			if(conns!=null)
				for(Connection conn : conns)
					if(pos.compareTo(conn.end) < 0&&world.isBlockLoaded(conn.end))
						this.markContainingBlockForUpdate(null);
			firstTick = false;
		}
	}

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		EnumFacing side = facing.getOpposite();
		double conRadius = con.cableType.getRenderDiameter()/2;
		return new Vec3d(.5+side.getFrontOffsetX()*(.5-conRadius), 0.5+side.getFrontOffsetY()*(.5-conRadius), .5+side.getFrontOffsetZ()*(.5-conRadius));
	}

	@Override
	public float[] getBlockBounds()
	{
		float length = 1f;
		float wMin = .25f;
		float wMax = .75f;
		switch(facing.getOpposite())
		{
			case UP:
				return new float[]{wMin, 0, wMin, wMax, length, wMax};
			case DOWN:
				return new float[]{wMin, 1-length, wMin, wMax, 1, wMax};
			case SOUTH:
				return new float[]{wMin, wMin, 0, wMax, wMax, length};
			case NORTH:
				return new float[]{wMin, wMin, 1-length, wMax, wMax, 1};
			case EAST:
				return new float[]{0, wMin, wMin, length, wMax, wMax};
			case WEST:
				return new float[]{1-length, wMin, wMin, 1, wMax, wMax};
		}
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public boolean acceptsWireType(WireType category)
	{
		return IIDataWireType.DATA_CATEGORY.equals(category.getCategory());
	}

	@Override
	public boolean isRelay()
	{
		return true;
	}
}
