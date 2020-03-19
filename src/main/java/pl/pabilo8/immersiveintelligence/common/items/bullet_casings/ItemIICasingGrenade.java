package pl.pabilo8.immersiveintelligence.common.items.bullet_casings;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelGrenade;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class ItemIICasingGrenade extends ItemIIBase implements IBulletCasingType
{
	public ItemIICasingGrenade()
	{
		super("casing_grenade", 1);
	}

	@Override
	public String getName()
	{
		return "grenade_4bCal";
	}

	@Override
	public float getComponentCapacity()
	{
		return 0.15f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 0;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getInitialMass()
	{
		return 0.25f;
	}

	@Override
	public float getSize()
	{
		return 0.5f;
	}

	@Override
	public int getStackSize()
	{
		return 8;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelGrenade.class;
	}

	@Override
	public float getPenetration()
	{
		return 0.125f;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public ItemStack getStack(int amount)
	{
		return new ItemStack(CommonProxy.item_casing_grenade, amount);
	}

	@Override
	public boolean isThrowable()
	{
		return true;
	}
}
