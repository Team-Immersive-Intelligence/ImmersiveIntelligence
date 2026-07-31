package pl.pabilo8.immersiveintelligence.api.api.protection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

/**
 * A persistent point source of radiation. Strength falls off linearly to zero at the radius edge.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.07.2026
 */
public class RadiationCenter implements INBTSerializable<NBTTagCompound>
{
	private int dimension;
	@Nonnull
	private BlockPos position = BlockPos.ORIGIN;
	private float radius;
	private float strength;

	public RadiationCenter()
	{
	}

	public RadiationCenter(int dimension, @Nonnull BlockPos position, float radius, float strength)
	{
		this.dimension = dimension;
		this.position = position.toImmutable();
		this.radius = Math.max(0, radius);
		this.strength = Math.max(0, strength);
	}

	public int getDimension()
	{
		return dimension;
	}

	@Nonnull
	public BlockPos getPosition()
	{
		return position;
	}

	public float getRadius()
	{
		return radius;
	}

	public float getStrength()
	{
		return strength;
	}

	void setRadius(float radius)
	{
		this.radius = Math.max(0, radius);
	}

	void setStrength(float strength)
	{
		this.strength = Math.max(0, strength);
	}

	public float getRadiationAt(@Nonnull Vec3d point)
	{
		if(radius <= 0||strength <= 0)
			return 0;

		Vec3d center = new Vec3d(position).addVector(0.5, 0.5, 0.5);
		double distanceSq = center.squareDistanceTo(point);
		double radiusSq = radius*radius;
		if(distanceSq >= radiusSq)
			return 0;
		return strength*(1f-(float)(Math.sqrt(distanceSq)/radius));
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("dimension", dimension);
		nbt.setLong("position", position.toLong());
		nbt.setFloat("radius", radius);
		nbt.setFloat("strength", strength);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		dimension = nbt.getInteger("dimension");
		position = BlockPos.fromLong(nbt.getLong("position"));
		radius = Math.max(0, nbt.getFloat("radius"));
		strength = Math.max(0, nbt.getFloat("strength"));
	}
}
