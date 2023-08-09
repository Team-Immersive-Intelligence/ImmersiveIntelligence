package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public class VehicleDurability
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
		this.durability -= MathHelper.clamp(amount-armor, 0, this.durability);
	}

	public double getDamageFactor()
	{
		return durability/(double)maxDurability;
	}
}
