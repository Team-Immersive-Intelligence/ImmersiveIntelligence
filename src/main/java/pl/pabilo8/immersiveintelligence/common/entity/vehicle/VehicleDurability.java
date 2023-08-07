package pl.pabilo8.immersiveintelligence.common.entity.vehicle;

import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;

/**
 * @author Pabilo8
 * @since 23.12.2022
 */
public class VehicleDurability
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
}
