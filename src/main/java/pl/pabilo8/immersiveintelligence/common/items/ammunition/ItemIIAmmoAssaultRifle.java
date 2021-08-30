package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet1bCal;
import pl.pabilo8.immersiveintelligence.common.IIContent;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoAssaultRifle extends ItemIIBulletBase
{
	public ItemIIAmmoAssaultRifle()
	{
		super("stg_1bCal", 32);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.085f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 15;
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
	public float getDefaultVelocity()
	{
		return 6.5f;
	}

	@Override
	public float getCaliber()
	{
		return 1f;
	}

	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelBullet1bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 4;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return Utils.getStackWithMetaName(IIContent.itemAmmoCasing,"stg_1bcal",amount);
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}
}
