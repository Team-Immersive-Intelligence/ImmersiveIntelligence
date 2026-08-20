package pl.pabilo8.immersiveintelligence.api.protection.protection;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.protection.protection.capability.IRadiationEmitter;

/**
 * Radiation emitter implementation for tile entities and entities.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.07.2026
 */
public class RadiationEmitter implements IRadiationEmitter, INBTSerializable<NBTTagCompound>
{
	private float radius;
	private float strength;
	private boolean active = true;

	public RadiationEmitter()
	{
	}

	public RadiationEmitter(float radius, float strength)
	{
		this.radius = Math.max(0, radius);
		this.strength = Math.max(0, strength);
	}

	public RadiationEmitter withRadius(float radius)
	{
		this.radius = Math.max(0, radius);
		return this;
	}

	public RadiationEmitter withStrength(float strength)
	{
		this.strength = Math.max(0, strength);
		return this;
	}

	public RadiationEmitter withActive(boolean active)
	{
		this.active = active;
		return this;
	}

	@Override
	public float getRadiationRadius()
	{
		return radius;
	}

	@Override
	public float getRadiationStrength()
	{
		return strength;
	}

	@Override
	public boolean isRadiationActive()
	{
		return active;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("radius", radius);
		nbt.setFloat("strength", strength);
		nbt.setBoolean("active", active);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		radius = Math.max(0, nbt.getFloat("radius"));
		strength = Math.max(0, nbt.getFloat("strength"));
		active = !nbt.hasKey("active")||nbt.getBoolean("active");
	}
}
