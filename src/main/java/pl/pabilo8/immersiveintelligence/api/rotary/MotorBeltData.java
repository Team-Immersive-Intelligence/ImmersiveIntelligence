package pl.pabilo8.immersiveintelligence.api.rotary;

import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.client.render.MechanicalConnectorRenderer;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityMechanicalConnectable;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 03-01-2020
 */
public class MotorBeltData
{
	public final IModelMotorBelt model;
	public final Vec3d[] origins;
	public final float[] pitch, yaw, lengths, radii;
	public final int[] sidePoints;

	public final int beltThickness, beltWidth;
	public final ResourceLocation texture;

	public MotorBeltData(IModelMotorBelt model, Vec3d[] origins, float[] pitch, float[] yaw, float[] lengths, float[] radii, int[] sidePoints, ResourceLocation texture, int beltThickness, int beltWidth)
	{
		this.model = model;
		this.origins = origins;
		this.pitch = pitch;
		this.yaw = yaw;
		this.lengths = lengths;
		this.radii = radii;
		this.sidePoints = sidePoints;
		this.texture = texture;
		this.beltThickness = beltThickness;
		this.beltWidth = beltWidth;
	}

	@Nullable
	public static MotorBeltData createBeltData(Vec3d[] points, float[] radii, MotorBeltType belt)
	{
		if(points.length < 2)
			return null;

		int beltThickness = belt.getThickness();
		float[] angles = new float[points.length];
		float[] lengths = new float[points.length];
		float[] yaws = new float[points.length];
		int[] sidePoints = new int[points.length];

		for(int i = 0; i < points.length; i++)
		{
			sidePoints[i] = (int)Math.ceil((Math.PI*2/points.length)*radii[i]/belt.getWidth());
			lengths[i] = (float)points[i].distanceTo(points[(i+1)%points.length])*16;
			Vec3d vv = points[i].subtract(points[(i+1)%points.length]).normalize();
			yaws[i] = (float)MathHelper.wrapDegrees((Math.atan2(vv.x, vv.z)*180D)/3.1415927410125732D);
			angles[i] = (float)MathHelper.wrapDegrees(Math.toDegrees((Math.atan2(vv.y, vv.distanceTo(new Vec3d(0, vv.y, 0))))));
		}

		return new MotorBeltData(belt.getModel(), points, angles, yaws, lengths, radii, sidePoints, new ResourceLocation(belt.getTexture()+".png"), beltThickness, belt.getWidth());
	}

	@Nullable
	public static MotorBeltData createBeltData(Connection connection, TileEntityMechanicalConnectable start, TileEntityMechanicalConnectable end)
	{
		if(!(connection.cableType instanceof MotorBeltType)||start==null||end==null)
			return null;

		Vec3d v1 = Vec3d.ZERO;
		Vec3d v2 = new Vec3d(end.getConnectionPos()).add(end.getConnectionOffset(connection))
				.subtract(new Vec3d(start.getConnectionPos()).add(start.getConnectionOffset(connection)));

		MotorBeltData data = createBeltData(new Vec3d[]{v1, v2}, new float[]{start.getRadius(), end.getRadius()}, (MotorBeltType)connection.cableType);

		MechanicalConnectorRenderer.cache.put(connection, data);
		MechanicalConnectorRenderer.cache.put(ImmersiveNetHandler.INSTANCE.getReverseConnection(start.getWorld().provider.getDimension(), connection), null);
		return data;
	}
}
