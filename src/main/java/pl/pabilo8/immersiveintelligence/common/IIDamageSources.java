package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityShrapnel;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class IIDamageSources
{
	public static final DamageSource SAWMILL_DAMAGE = (new DamageSource("iiSawmill"));

	public static DamageSource causeBulletDamage(EntityBullet shot, Entity shooter)
	{
		// TODO: 08.07.2020 integrate damage sources with bullet components
		if(shooter==null)
			return new DamageSource("iiBullet").setProjectile();
		return new IEDamageSource_Indirect("iiBullet", shot, shooter).setProjectile();
	}

	public static DamageSource causeShrapnelDamage(EntityShrapnel shot, Entity shooter)
	{
		if(shooter==null)
			return new DamageSource("iiShrapnel").setProjectile();
		return new IEDamageSource_Indirect("iiShrapnel", shot, shooter).setProjectile();
	}
}
