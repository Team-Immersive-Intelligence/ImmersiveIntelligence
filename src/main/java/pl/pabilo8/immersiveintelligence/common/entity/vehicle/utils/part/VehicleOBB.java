package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Lightweight oriented bounding box used by II vehicles.
 * <p>
 * Minecraft still needs AABBs for broad-phase lookups, rendering selection boxes and vanilla entity plumbing.
 * This class provides the narrow-phase test used by vehicle parts after the broad-phase AABB has found
 * candidate blocks or entities.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 05.06.2026
 */
public class VehicleOBB
{
	private static final double EPSILON = 1.0E-7;

	public final Vec3d center;
	public final Vec3d axisX;
	public final Vec3d axisY;
	public final Vec3d axisZ;
	public final double halfX;
	public final double halfY;
	public final double halfZ;

	public VehicleOBB(Vec3d center, Vec3d axisX, Vec3d axisY, Vec3d axisZ, double halfX, double halfY, double halfZ)
	{
		this.center = center;
		this.axisX = safeNormalize(axisX, new Vec3d(1, 0, 0));
		this.axisY = safeNormalize(axisY, new Vec3d(0, 1, 0));
		this.axisZ = safeNormalize(axisZ, new Vec3d(0, 0, 1));
		this.halfX = Math.max(0, halfX);
		this.halfY = Math.max(0, halfY);
		this.halfZ = Math.max(0, halfZ);
	}

	/**
	 * Creates an OBB from a part-local AABB. The resulting box is centred at {@code partWorldPosition}
	 * plus the rotated local AABB centre.
	 */
	public static VehicleOBB fromLocalAABB(AxisAlignedBB localBox, Vec3d partWorldPosition, float yaw, float pitch, float roll)
	{
		AxisAlignedBB box = normalize(localBox);
		Vec3d localCenter = new Vec3d(
				(box.minX+box.maxX)*0.5,
				(box.minY+box.maxY)*0.5,
				(box.minZ+box.maxZ)*0.5
		);
		Vec3d center = partWorldPosition.add(rotateLocal(localCenter, yaw, pitch, roll));

		return new VehicleOBB(center,
				rotateLocal(new Vec3d(1, 0, 0), yaw, pitch, roll),
				rotateLocal(new Vec3d(0, 1, 0), yaw, pitch, roll),
				rotateLocal(new Vec3d(0, 0, 1), yaw, pitch, roll),
				(box.maxX-box.minX)*0.5,
				(box.maxY-box.minY)*0.5,
				(box.maxZ-box.minZ)*0.5
		);
	}

	/**
	 * Treats a vanilla AABB as an OBB aligned to world axes.
	 */
	public static VehicleOBB fromAABB(AxisAlignedBB box)
	{
		AxisAlignedBB normalized = normalize(box);
		return new VehicleOBB(new Vec3d(
				(normalized.minX+normalized.maxX)*0.5,
				(normalized.minY+normalized.maxY)*0.5,
				(normalized.minZ+normalized.maxZ)*0.5),
				new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), new Vec3d(0, 0, 1),
				(normalized.maxX-normalized.minX)*0.5,
				(normalized.maxY-normalized.minY)*0.5,
				(normalized.maxZ-normalized.minZ)*0.5
		);
	}

	public VehicleOBB offset(double x, double y, double z)
	{
		return offset(new Vec3d(x, y, z));
	}

	public VehicleOBB offset(Vec3d offset)
	{
		return new VehicleOBB(center.add(offset), axisX, axisY, axisZ, halfX, halfY, halfZ);
	}

	public Vec3d[] getAxes()
	{
		return new Vec3d[]{axisX, axisY, axisZ};
	}

	public double[] getHalfSizes()
	{
		return new double[]{halfX, halfY, halfZ};
	}

	public Vec3d[] getCorners()
	{
		Vec3d x = axisX.scale(halfX);
		Vec3d y = axisY.scale(halfY);
		Vec3d z = axisZ.scale(halfZ);
		return new Vec3d[]{
				center.subtract(x).subtract(y).subtract(z),
				center.add(x).subtract(y).subtract(z),
				center.add(x).add(y).subtract(z),
				center.subtract(x).add(y).subtract(z),
				center.subtract(x).subtract(y).add(z),
				center.add(x).subtract(y).add(z),
				center.add(x).add(y).add(z),
				center.subtract(x).add(y).add(z)
		};
	}

	public AxisAlignedBB getEnclosingAABB()
	{
		Vec3d[] corners = getCorners();
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
		double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;
		for(Vec3d corner : corners)
		{
			minX = Math.min(minX, corner.x);
			minY = Math.min(minY, corner.y);
			minZ = Math.min(minZ, corner.z);
			maxX = Math.max(maxX, corner.x);
			maxY = Math.max(maxY, corner.y);
			maxZ = Math.max(maxZ, corner.z);
		}
		return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
	}

	/**
	 * Ray-traces this OBB using a finite segment.
	 *
	 * @param start ray start in world coordinates
	 * @param end   ray end in world coordinates
	 * @return the closest hit point on the segment, or null when the segment misses this OBB
	 */
	@Nullable
	public Vec3d rayTrace(Vec3d start, Vec3d end)
	{
		Vec3d direction = end.subtract(start);
		Vec3d relativeStart = start.subtract(center);
		Vec3d[] axes = getAxes();
		double[] halves = getHalfSizes();

		double tMin = 0.0;
		double tMax = 1.0;
		for(int i = 0; i < 3; i++)
		{
			double origin = relativeStart.dotProduct(axes[i]);
			double dir = direction.dotProduct(axes[i]);
			double min = -halves[i];
			double max = halves[i];

			if(Math.abs(dir) < EPSILON)
			{
				if(origin < min||origin > max)
					return null;
				continue;
			}

			double t1 = (min-origin)/dir;
			double t2 = (max-origin)/dir;
			if(t1 > t2)
			{
				double swap = t1;
				t1 = t2;
				t2 = swap;
			}

			tMin = Math.max(tMin, t1);
			tMax = Math.min(tMax, t2);
			if(tMin-tMax > EPSILON)
				return null;
		}

		if(tMin < -EPSILON||tMin > 1.0+EPSILON)
			return null;
		double hitT = Math.max(0.0, Math.min(1.0, tMin));
		return start.add(direction.scale(hitT));
	}

	public boolean intersects(AxisAlignedBB box)
	{
		return calculateCollision(box)!=null;
	}

	public boolean intersects(VehicleOBB other)
	{
		return calculateCollision(other)!=null;
	}

	@Nullable
	public CollisionResult calculateCollision(AxisAlignedBB box)
	{
		return calculateCollision(fromAABB(box));
	}

	/**
	 * Separating Axis Theorem narrow-phase test. The returned normal points from this OBB toward the other box.
	 */
	@Nullable
	public CollisionResult calculateCollision(VehicleOBB other)
	{
		List<Vec3d> axes = new ArrayList<>(15);
		addAxis(axes, axisX);
		addAxis(axes, axisY);
		addAxis(axes, axisZ);
		addAxis(axes, other.axisX);
		addAxis(axes, other.axisY);
		addAxis(axes, other.axisZ);

		Vec3d[] localAxes = getAxes();
		Vec3d[] otherAxes = other.getAxes();
		for(Vec3d a : localAxes)
			for(Vec3d b : otherAxes)
				addAxis(axes, a.crossProduct(b));

		double smallestOverlap = Double.MAX_VALUE;
		Vec3d smallestAxis = Vec3d.ZERO;
		for(Vec3d axis : axes)
		{
			Projection p1 = project(axis);
			Projection p2 = other.project(axis);
			double overlap = Math.min(p1.max, p2.max)-Math.max(p1.min, p2.min);
			if(overlap <= EPSILON)
				return null;
			if(overlap < smallestOverlap)
			{
				smallestOverlap = overlap;
				smallestAxis = axis;
			}
		}

		Vec3d direction = other.center.subtract(this.center);
		if(direction.dotProduct(smallestAxis) < 0)
			smallestAxis = smallestAxis.scale(-1);
		return new CollisionResult(smallestAxis, smallestOverlap);
	}

	private Projection project(Vec3d axis)
	{
		double centerProjection = center.dotProduct(axis);
		double radius = halfX*Math.abs(axisX.dotProduct(axis))+
				halfY*Math.abs(axisY.dotProduct(axis))+
				halfZ*Math.abs(axisZ.dotProduct(axis));
		return new Projection(centerProjection-radius, centerProjection+radius);
	}

	private static void addAxis(List<Vec3d> axes, Vec3d axis)
	{
		if(axis.lengthSquared() > EPSILON)
			axes.add(axis.normalize());
	}

	private static Vec3d safeNormalize(Vec3d vec, Vec3d fallback)
	{
		return vec.lengthSquared() > EPSILON?vec.normalize(): fallback;
	}

	/**
	 * Transforms a vector from II vehicle-local coordinates to world coordinates.
	 * <p>
	 * Vehicle-local convention, matching the model editor:
	 * <ul>
	 * 	<li>+X: right side</li>
	 * 	<li>-X: left side</li>
	 * 	<li>+Y: up</li>
	 * 	<li>-Y: down</li>
	 * 	<li>-Z: front / nose</li>
	 * 	<li>+Z: rear / back</li>
	 * </ul>
	 * <p>
	 * Yaw zero keeps the vehicle pointing toward world +Z, preserving the old spawn-facing behaviour,
	 * but the local forward axis is now consistently {@code -Z}.
	 */
	public static Vec3d transformLocal(Vec3d vec, float yaw, float pitch, float roll)
	{
		Basis basis = createBasis(yaw, pitch, roll);
		return basis.axisX.scale(vec.x)
				.add(basis.axisY.scale(vec.y))
				.add(basis.axisZ.scale(vec.z));
	}

	public static Vec3d getForwardVector(float yaw)
	{
		float radians = yaw*0.017453292F;
		return new Vec3d(-Math.sin(radians), 0, Math.cos(radians));
	}

	public static Vec3d getRightVector(float yaw)
	{
		float radians = yaw*0.017453292F;
		return new Vec3d(Math.cos(radians), 0, Math.sin(radians));
	}

	private static Vec3d rotateLocal(Vec3d vec, float yaw, float pitch, float roll)
	{
		return transformLocal(vec, yaw, pitch, roll);
	}

	private static Basis createBasis(float yaw, float pitch, float roll)
	{
		Vec3d axisX = getRightVector(yaw);
		Vec3d axisY = new Vec3d(0, 1, 0);
		Vec3d axisZ = getForwardVector(yaw).scale(-1); // local +Z is the vehicle rear

		double pitchRadians = -pitch*0.017453292519943295D;
		if(Math.abs(pitchRadians) > EPSILON)
		{
			axisY = rotateAroundAxis(axisY, axisX, pitchRadians);
			axisZ = rotateAroundAxis(axisZ, axisX, pitchRadians);
		}

		double rollRadians = roll*0.017453292519943295D;
		if(Math.abs(rollRadians) > EPSILON)
		{
			axisX = rotateAroundAxis(axisX, axisZ, rollRadians);
			axisY = rotateAroundAxis(axisY, axisZ, rollRadians);
		}

		return new Basis(axisX.normalize(), axisY.normalize(), axisZ.normalize());
	}

	private static Vec3d rotateAroundAxis(Vec3d vec, Vec3d axis, double angle)
	{
		Vec3d n = safeNormalize(axis, new Vec3d(0, 1, 0));
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		return vec.scale(cos)
				.add(n.crossProduct(vec).scale(sin))
				.add(n.scale(n.dotProduct(vec)*(1.0D-cos)));
	}

	private static class Basis
	{
		private final Vec3d axisX;
		private final Vec3d axisY;
		private final Vec3d axisZ;

		private Basis(Vec3d axisX, Vec3d axisY, Vec3d axisZ)
		{
			this.axisX = axisX;
			this.axisY = axisY;
			this.axisZ = axisZ;
		}
	}

	private static AxisAlignedBB normalize(AxisAlignedBB box)
	{
		return new AxisAlignedBB(
				Math.min(box.minX, box.maxX), Math.min(box.minY, box.maxY), Math.min(box.minZ, box.maxZ),
				Math.max(box.minX, box.maxX), Math.max(box.minY, box.maxY), Math.max(box.minZ, box.maxZ)
		);
	}

	private static class Projection
	{
		private final double min;
		private final double max;

		private Projection(double min, double max)
		{
			this.min = min;
			this.max = max;
		}
	}

	public static class CollisionResult
	{
		/**
		 * Normal pointing from the first tested OBB toward the other collider.
		 */
		public final Vec3d normal;
		public final double depth;

		public CollisionResult(Vec3d normal, double depth)
		{
			this.normal = normal;
			this.depth = depth;
		}

		/**
		 * Pushes the first tested OBB out of the other collider.
		 */
		public Vec3d getPushOut()
		{
			return normal.scale(-depth);
		}
	}
}
