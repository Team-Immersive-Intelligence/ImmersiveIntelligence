package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.entity.EntityShrapnel;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class IIDamageSources
{
	public static DamageSource causeBulletDamage(EntityBullet shot, Entity shooter)
	{
		if(shooter==null)
			return new DamageSource("iiBullet");
		return new IEDamageSource_Indirect("iiBullet", shot, shooter);
	}

	public static DamageSource causeShrapnelDamage(EntityShrapnel shot, Entity shooter)
	{
		if(shooter==null)
			return new DamageSource("iiShrapnel");
		return new IEDamageSource_Indirect("iiShrapnel", shot, shooter);
	}
}
