package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public class VehicleDurability implements INBTSerializable<NBTTagInt>
{
	private int durability;
	public final int maxDurability, armor;

	public VehicleDurability(int maxDurability, int armor)
	{
		this.maxDurability = maxDurability;
		this.durability = maxDurability;
		this.armor = armor;
	}

	public void attackFrom(DamageSource source, float amount)
	{
		this.durability -= MathHelper.clamp(amount-armor, 0, this.durability);
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
