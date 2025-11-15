package pl.pabilo8.immersiveintelligence.common.util;

import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.component.EntityShrapnel;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.EntityVehicleBase;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.part.EntityVehicleSeat;

import java.util.Arrays;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 30.08.2019
 */
public class IIDamageSources
{
	public static final DamageSource SAWMILL_DAMAGE = new DamageSource("iiSawmill");
	public static final DamageSource PRINTING_PRESS_DAMAGE = new DamageSource("iiPrintingPress");
	public static final DamageSource RADIATION_DAMAGE = new DamageSource("iiRadiation").setDamageBypassesArmor().setDamageIsAbsolute();
	public static final DamageSource NUCLEAR_HEAT_DAMAGE = new DamageSource("iiNuclearHeat").setDamageBypassesArmor().setDamageIsAbsolute();


	public static DamageSource causeVehicleDamage(EntityVehicleBase<?> vehicle)
	{
		//Find the first seat with an EntityLivingBase passenger
		for(Entity seat : vehicle.getPassengers())
			if(seat instanceof EntityVehicleSeat)
			{
				if(seat.getPassengers().isEmpty())
					continue;
				Entity passenger = seat.getPassengers().get(1);
				if(passenger instanceof EntityLivingBase)
					return new IEDamageSource_Indirect("iiVehicleNoRider", vehicle, passenger);
			}
		//Return a generic vehicle damage source if no passenger could be found
		return new IEDamageSource_Indirect("iiVehicle", vehicle, null);
	}

	public static DamageSource causeVehicleDamageGetOut(EntityVehicleBase<?> vehicle)
	{
		return new IEDamageSource_Indirect("iiVehicleSuicide", vehicle, null);
	}

	public static DamageSource causeBulletDamage(EntityAmmoProjectile shot, Entity attacked)
	{
		Entity shooter = shot.getOwner();

		if(shooter==null)
		{
			//skip blacklisted entities
			ResourceLocation key = EntityList.getKey(attacked instanceof MultiPartEntityPart?(Entity)((MultiPartEntityPart)attacked).parent: attacked);
			if(key!=null&&Arrays.asList(IIConfig.bulletFakeplayerWhitelist).contains(key.toString()))
				//owner is a FakePlayer
				return new IEDamageSource_Indirect("iiBulletNoShooter", shot, FakePlayerUtil.getFakePlayer(shot.getEntityWorld())).setProjectile().setDamageBypassesArmor();
		}
		//owner (shooter) is a normal player or undefined
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
