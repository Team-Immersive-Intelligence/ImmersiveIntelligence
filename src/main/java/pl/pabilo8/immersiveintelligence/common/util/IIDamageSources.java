package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityMotorbike;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleSeat;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityShrapnel;

import java.util.Arrays;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class IIDamageSources
{
	public static final DamageSource SAWMILL_DAMAGE = new DamageSource("iiSawmill");
	public static final DamageSource RADIATION_DAMAGE = new DamageSource("iiRadiation").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource NUCLEAR_HEAT_DAMAGE = new DamageSource("iiNuclearHeat").setDamageBypassesArmor().setDamageIsAbsolute();

	public static DamageSource causeMotorbikeDamage(EntityMotorbike motorbike)
	{
		EntityVehicleSeat seat = EntityVehicleSeat.getOrCreateSeat(motorbike, 0);
		Entity rider = seat.getPassengers().get(0);
		return new IEDamageSource_Indirect(rider!=null?"iiMotorbike": "iiMotorbikeNoRider", motorbike, rider);
	}

	public static DamageSource causeMotorbikeDamageGetOut(EntityMotorbike motorbike)
	{
		return new IEDamageSource_Indirect("iiMotorbikeSuicide", motorbike, null);
	}

	public static DamageSource causeBulletDamage(EntityBullet shot, Entity shooter, Entity attacked)
	{
		if(shooter==null)
		{
			ResourceLocation key = EntityList.getKey(attacked instanceof MultiPartEntityPart?(Entity)((MultiPartEntityPart)attacked).parent: attacked);
			if(key!=null&&Arrays.asList(IIConfig.bulletFakeplayerWhitelist).contains(key.toString()))
				return new IEDamageSource_Indirect("iiBulletNoShooter", shot, FakePlayerUtil.getFakePlayer(shot.getEntityWorld())).setProjectile().setDamageBypassesArmor();
		}
		return new IEDamageSource_Indirect(shooter!=null?"iiBullet": "iiBulletNoShooter", shot, shooter).setProjectile().setDamageBypassesArmor();
	}

	public static DamageSource causeShrapnelDamage(EntityShrapnel shot, Entity shooter, Entity attacked)
	{
		if(shooter==null)
		{
			ResourceLocation key = EntityList.getKey(attacked instanceof MultiPartEntityPart?(Entity)((MultiPartEntityPart)attacked).parent: attacked);
			if(key!=null&&Arrays.asList(IIConfig.bulletFakeplayerWhitelist).contains(key.toString()))
				return new IEDamageSource_Indirect("iiShrapnelNoShooter", shot, FakePlayerUtil.getFakePlayer(shot.getEntityWorld())).setProjectile().setDamageBypassesArmor();
		}
		return new IEDamageSource_Indirect(shooter!=null?"iiShrapnel": "iiShrapnelNoShooter", shot, shooter).setProjectile().setDamageBypassesArmor();
	}
}
