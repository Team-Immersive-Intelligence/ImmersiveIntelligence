package pl.pabilo8.immersiveintelligence.common.items.bullet_casings;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelBullet;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class ItemIICasingSubmachinegun extends ItemIIBase implements IBulletCasingType
{
	public ItemIICasingSubmachinegun()
	{
		super("casing_submachinegun", 48);
	}

	@Override
	public String getName()
	{
		return "submachinegun_1bCal";
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
	public float getSize()
	{
		return 0.0625f;
	}

	@Override
	public int getStackSize()
	{
		return 48;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBullet.class;
	}

	@Override
	public float getPenetration()
	{
		return 0.25f;
	}

	@Override
	public float getDamage()
	{
		return 6;
	}

	@Override
	public ItemStack getStack(int amount)
	{
		return new ItemStack(CommonProxy.item_casing_submachinegun, amount);
	}
}
