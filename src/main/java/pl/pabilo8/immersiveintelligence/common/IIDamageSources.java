package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import pl.pabilo8.immersiveintelligence.common.entity.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class IIDamageSources
{
	public static final DamageSource SAWMILL_DAMAGE = (new DamageSource("iiSawmill"));

	public static DamageSource causeMotorbikeDamage(EntityMotorbike motorbike)
	{
		EntityVehicleSeat seat = EntityVehicleSeat.getOrCreateSeat(motorbike, 0);
		Entity rider= seat.getPassengers().get(0);
		return new IEDamageSource_Indirect(rider!=null?"iiMotorbike":"iiMotorbikeNoRider", motorbike, rider);
	}

	public static DamageSource causeMotorbikeDamageGetOut(EntityMotorbike motorbike)
	{
		return new IEDamageSource_Indirect("iiMotorbikeSuicide", motorbike,null);
	}

	public static DamageSource causeBulletDamage(EntityBullet shot, Entity shooter)
	{
		return new IEDamageSource_Indirect(shooter!=null?"iiBullet":"iiBulletNoShooter", shot, shooter).setProjectile().setDamageBypassesArmor();
	}

	public static DamageSource causeShrapnelDamage(EntityShrapnel shot, Entity shooter)
	{
		return new IEDamageSource_Indirect(shooter!=null?"iiShrapnel":"iiShrapnelNoShooter", shot, shooter).setProjectile().setDamageBypassesArmor();
	}
}
