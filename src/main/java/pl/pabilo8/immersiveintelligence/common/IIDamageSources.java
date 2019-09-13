package pl.pabilo8.immersiveintelligence.common;

import blusunrize.immersiveengineering.common.util.IEDamageSources.IEDamageSource_Indirect;
import blusunrize.immersiveengineering.common.util.IEDamageSources.TurretDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import pl.pabilo8.immersiveintelligence.common.entity.EntityBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class IIDamageSources
{
	public static DamageSource causeBulletDamage(EntityBullet shot, Entity shooter)
	{
		if(shooter==null)
			return new TurretDamageSource("ieBullet");
		return new IEDamageSource_Indirect("ieBullet", shot, shooter);
	}
}
