package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.ParticleUtils;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet2bCal;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletBase;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoMachinegun extends ItemIIBulletBase
{
	public ItemIIAmmoMachinegun()
	{
		super("mg_2bCal", 24);
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.125f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 6;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 4;
	}

	@Override
	public float getInitialMass()
	{
		return 0.125f;
	}

	@Override
	public float getCaliber()
	{
		return 0.125f;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBullet2bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}

	@Override
	public void doPuff(EntityBullet bullet)
	{
		for(int i = 1; i < 5; i++)
			ParticleUtils.spawnGunfireFX(bullet.posX, bullet.posY, bullet.posZ, bullet.motionX/i, bullet.motionY/i, bullet.motionZ/i, 8*bullet.bulletCasing.getCaliber()*(i/2.5f));
	}

	@Override
	public float getSupressionRadius()
	{
		return 2;
	}

	@Override
	public int getSuppressionPower()
	{
		return 2;
	}
}
