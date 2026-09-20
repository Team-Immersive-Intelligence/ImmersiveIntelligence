package pl.pabilo8.immersiveintelligence.common.util.amt;

import blusunrize.immersiveengineering.common.util.chickenbones.Matrix4;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;

/**
 * Resolved model-to-world transform for one tactile AABB source.
 * <p>
 * Transform properties are selected in the following order: {@code all}, horizontal facing,
 * {@code normal}/{@code mirrored}, and finally {@code <facing>_normal}/{@code <facing>_mirrored}.
 * Later selectors replace only the properties they declare.
 * </p>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 */
public final class TactileTransform
{
	private static final Vec3d BLOCK_CENTRE = new Vec3d(0.5, 0.5, 0.5);

	private final Vec3d offset;
	private final Vec3d rotation;
	private final boolean flipXZSize;
	private final boolean mirrored;
	private final Matrix4 rotationMatrix;

	private TactileTransform(Vec3d offset, Vec3d rotation, boolean flipXZSize, boolean mirrored)
	{
		this.offset = offset;
		this.rotation = rotation;
		this.flipXZSize = flipXZSize;
		this.mirrored = mirrored;
		this.rotationMatrix = new Matrix4().setIdentity()
				.rotate(Math.toRadians(-rotation.y), 0, 1, 0)
				.rotate(Math.toRadians(rotation.z), 0, 0, 1)
				.rotate(Math.toRadians(-rotation.x), 1, 0, 0);
	}

	@Nonnull
	public static TactileTransform resolve(JsonObject transforms, EnumFacing facing, boolean mirrored)
	{
		Builder builder = new Builder(facing, mirrored);
		if(transforms!=null)
		{
			String direction = facing.getName();
			builder.apply(transforms, "all");
			builder.apply(transforms, direction);
			builder.apply(transforms, mirrored?"mirrored": "normal");
			builder.apply(transforms, direction+"_"+(mirrored?"mirrored": "normal"));
		}
		return builder.build();
	}

	/**
	 * Transforms an AMT/header point into the block-local coordinate system used by tactiles.
	 */
	public Vec3d transformPosition(Vec3d modelPosition)
	{
		Vec3d local = modelPosition.add(offset);
		local = new Vec3d(-local.x, local.y, 1-local.z);
		if(mirrored)
			local = new Vec3d(1-local.x, local.y, local.z);
		return rotateDirection(local.subtract(BLOCK_CENTRE)).add(BLOCK_CENTRE);
	}

	/**
	 * Transforms an AMT animation translation. AMT applies the X channel inverted.
	 */
	public Vec3d transformAnimationTranslation(Vec3d translation)
	{
		Vec3d local = new Vec3d(-translation.x, translation.y, translation.z);
		if(mirrored)
			local = new Vec3d(-local.x, local.y, local.z);
		return rotateDirection(local);
	}

	/**
	 * Converts AMT Euler axes to the tactile hierarchy and preserves axial-vector handedness.
	 */
	public Vec3d transformAnimationRotation(Vec3d animationRotation)
	{
		Vec3d local = new Vec3d(-animationRotation.x, -animationRotation.y, animationRotation.z);
		if(mirrored)
			local = new Vec3d(local.x, -local.y, -local.z);
		return rotateDirection(local);
	}

	/**
	 * Transforms a local AABB centre and applies the explicitly selected horizontal size basis.
	 */
	public AxisAlignedBB transformBounds(AxisAlignedBB bounds)
	{
		Vec3d centre = new Vec3d(
				(bounds.minX+bounds.maxX)*0.5,
				(bounds.minY+bounds.maxY)*0.5,
				(bounds.minZ+bounds.maxZ)*0.5
		);
		centre = new Vec3d(-centre.x, centre.y, -centre.z);
		if(mirrored)
			centre = new Vec3d(-centre.x, centre.y, centre.z);
		centre = rotateDirection(centre);

		double width = bounds.maxX-bounds.minX;
		double height = bounds.maxY-bounds.minY;
		double depth = bounds.maxZ-bounds.minZ;
		if(flipXZSize)
		{
			double swap = width;
			width = depth;
			depth = swap;
		}
		return new AxisAlignedBB(
				centre.x-width*0.5, centre.y-height*0.5, centre.z-depth*0.5,
				centre.x+width*0.5, centre.y+height*0.5, centre.z+depth*0.5
		);
	}

	public Vec3d rotateDirection(Vec3d direction)
	{
		Vec3d rotated = rotationMatrix.apply(direction);
		return new Vec3d(cleanComponent(rotated.x), cleanComponent(rotated.y), cleanComponent(rotated.z));
	}

	private static double cleanComponent(double value)
	{
		return Math.abs(value) < 1e-12?0: value;
	}

	public Vec3d getOffset()
	{
		return offset;
	}

	public Vec3d getRotation()
	{
		return rotation;
	}

	public boolean isFlipXZSize()
	{
		return flipXZSize;
	}

	private static Vec3d readVector(JsonObject object, String key, Vec3d fallback)
	{
		if(!object.has(key)||!object.get(key).isJsonArray())
			return fallback;
		JsonArray array = object.getAsJsonArray(key);
		if(array.size()!=3)
			return fallback;
		return new Vec3d(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
	}

	private static class Builder
	{
		private Vec3d offset = Vec3d.ZERO;
		private Vec3d rotation;
		private boolean flipXZSize;
		private final boolean mirrored;

		private Builder(EnumFacing facing, boolean mirrored)
		{
			this.rotation = new Vec3d(0, facing.getHorizontalAngle(), 0);
			this.flipXZSize = facing.getAxis()==EnumFacing.Axis.X;
			this.mirrored = mirrored;
		}

		private void apply(JsonObject transforms, String selector)
		{
			if(!transforms.has(selector)||!transforms.get(selector).isJsonObject())
				return;
			JsonObject transform = transforms.getAsJsonObject(selector);
			offset = readVector(transform, "offset", offset);
			rotation = readVector(transform, "rotation", rotation);
			if(transform.has("flip_xz_size")&&transform.get("flip_xz_size").isJsonPrimitive())
				flipXZSize = transform.get("flip_xz_size").getAsBoolean();
		}

		private TactileTransform build()
		{
			return new TactileTransform(offset, rotation, flipXZSize, mirrored);
		}
	}
}
