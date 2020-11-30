package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet1bCalRevolver;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletBase;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoStormRifle extends ItemIIBulletBase
{
	public ItemIIAmmoStormRifle()
	{
		super("stg_1bCal", 32);
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.085f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 5;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getInitialMass()
	{
		return 0.0625f;
	}

	@Override
	public float getCaliber()
	{
		return 0.0625f;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBullet1bCalRevolver.class;
	}

	@Override
	public float getDamage()
	{
		return 4;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}
}
