package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet8bCal;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletBase;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoArtillery extends ItemIIBulletBase
{
	public ItemIIAmmoArtillery()
	{
		super("artillery_8bCal", 1);
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.5f;
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
		return 1f;
	}

	@Override
	public float getCaliber()
	{
		return 0.5f;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBullet8bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 30;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING, EnumCoreTypes.SHAPED, EnumCoreTypes.CANISTER, EnumCoreTypes.DOUBLE_CANISTER};
	}

	@Override
	public float getSupressionRadius()
	{
		return 4;
	}

	@Override
	public int getSuppressionPower()
	{
		return 20;
	}

	@Override
	public boolean shouldLoadChunks()
	{
		return true;
	}
}
