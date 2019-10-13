package pl.pabilo8.immersiveintelligence.common.items;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelBullet;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class ItemIICasingArtillery extends ItemIIBase implements IBulletCasingType
{
	public ItemIICasingArtillery()
	{
		super("casing_artillery", 1);
	}

	@Override
	public String getName()
	{
		return "artillery_8bCal";
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
	public float getSize()
	{
		return 1f;
	}

	@Override
	public int getStackSize()
	{
		return 1;
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
		return 30;
	}

	@Override
	public ItemStack getStack(int amount)
	{
		return new ItemStack(ImmersiveIntelligence.proxy.item_casing_artillery, amount);
	}

	@Override
	public boolean isThrowable()
	{
		return false;
	}
}
