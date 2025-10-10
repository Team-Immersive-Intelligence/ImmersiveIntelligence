package pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 23.12.2022
 */
public class VehicleDurability implements INBTSerializable<NBTTagInt>
{
	public final int maxDurability, armor;
	private int durability;

	public VehicleDurability(int maxDurability, int armor)
	{
		this.maxDurability = maxDurability;
		this.durability = maxDurability;
		this.armor = armor;
	}

	public void attackFrom(DamageSource source, float amount)
	{
		this.durability = MathHelper.clamp(durability+(int)amount, 0, maxDurability);
	}

	public boolean isDead()
	{
		return this.durability <= 0;
	}

	public boolean canRepair()
	{
		return durability < maxDurability;
	}

	public boolean repair(float amount)
	{
		if(!canRepair())
			return false;
		this.durability = MathHelper.clamp(durability+(int)amount, 0, maxDurability);
		return true;
	}

	public double getDamageFactor()
	{
		return durability/(double)maxDurability;
	}


	@Override
	public NBTTagInt serializeNBT()
	{
		return new NBTTagInt(durability);
	}

	@Override
	public void deserializeNBT(NBTTagInt nbt)
	{
		this.durability = nbt.getInt();
	}
}
