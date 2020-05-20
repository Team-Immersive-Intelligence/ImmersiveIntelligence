package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.render.MechanicalConnectorRenderer;
import pl.pabilo8.immersiveintelligence.common.blocks.rotary.TileEntityMechanicalConnectable;

import javax.annotation.Nullable;

/**
 * Created by Pabilo8 on 03-01-2020.
 */
public class MotorBeltData
{
	public IModelMotorBelt model;
	public int sidePointsFirst, sidePointsSecond, totalPoints, yaw, bt, radius;
	public float angleFirst, angleSecond, bs;
	public Vec3d[] points;
	public Vec3d offset;
	public float[] slopes;
	public String texture;

	public MotorBeltData(IModelMotorBelt model, int sidePointsFirst, int sidePointsSecond, int totalPoints, float angleFirst, float angleSecond, int yaw, Vec3d[] points, float[] slopes, Vec3d offset, String texture, int bt, int radius, float bs)
	{
		this.model = model;
		this.sidePointsFirst = sidePointsFirst;
		this.sidePointsSecond = sidePointsSecond;
		this.totalPoints = totalPoints;
		this.angleFirst = angleFirst;
		this.angleSecond = angleSecond;
		this.yaw = yaw;
		this.points = points;
		this.slopes = slopes;
		this.offset = offset;
		this.texture = texture;
		this.bt = bt;
		this.radius = radius;
		this.bs = bs;
	}

	@Nullable
	public static MotorBeltData createBeltData(Connection connection, TileEntityMechanicalConnectable start, TileEntityMechanicalConnectable end)
	{
		if(!(connection.cableType instanceof MotorBeltType)||start==null||end==null)
			return null;

		Vec3d offset = start.getConnectionOffset(connection);

		MotorBeltType type = (MotorBeltType)connection.cableType;
		IModelMotorBelt model = type.getModel();
		String texture = type.getTexture()+".png";
		float bs = type.getWidth();

		int bt = type.getThickness();
		int radius = start.getRadius();

		if(connection.length <= 0)
			return null;

		int sidePointsFirst = (int)Math.ceil(Math.PI*start.getRadius()/type.getWidth());
		int sidePointsSecond = (int)Math.ceil(Math.PI*end.getRadius()/type.getWidth());
		int totalPoints = (int)Math.ceil((connection.horizontalLength*16)/type.getWidth());
		float angleFirst = 0, angleSecond = 0;
		int yaw = 0;

		if(start.getConnectionAxis()==Axis.X)
			yaw = start.getPos().getX() > end.getPos().getX()?-180: 0;
		else if(start.getConnectionAxis()==Axis.Z)
			yaw = start.getPos().getZ() > end.getPos().getZ()?90: -90;
		if(sidePointsFirst > 0)
			angleFirst = 180/sidePointsFirst;
		if(sidePointsSecond > 0)
			angleSecond = 180/sidePointsSecond;

		Vec3d[] points = new Vec3d[totalPoints+1];
		float[] slopes = new float[totalPoints+1];

		for(int i = 0; i < (totalPoints)+1; i += 1)
		{
			//Errors sometimes, better be safe
			try
			{
				points[i] = connection.getVecAt((double)i/(double)(connection.length*16)*type.getWidth());
				slopes[i] = (float)connection.getSlopeAt((double)i/(double)(connection.length*16)*type.getWidth());
			} catch(NullPointerException n)
			{
				return null;
			}
		}

		MotorBeltData data = new MotorBeltData(model, sidePointsFirst, sidePointsSecond, totalPoints, angleFirst, angleSecond, yaw, points, slopes, offset, texture, bt, radius, bs);
		MechanicalConnectorRenderer.cache.put(connection, data);
		MechanicalConnectorRenderer.cache.put(ImmersiveNetHandler.INSTANCE.getReverseConnection(start.getWorld().provider.getDimension(), connection), null);
		return data;
	}
}
