package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet1bCalRevolver;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoSubmachinegun extends ItemIIBulletBase
{
	public ItemIIAmmoSubmachinegun()
	{
		super("smg_1bCal", 48);
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.0625f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 3;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
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
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelBullet1bCalRevolver.class;
	}

	@Override
	public float getDamage()
	{
		return 3;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}
}
