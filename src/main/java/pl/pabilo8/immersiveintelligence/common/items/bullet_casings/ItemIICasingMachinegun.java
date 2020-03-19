package pl.pabilo8.immersiveintelligence.common.items.bullet_casings;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCasingType;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.misc.ModelBullet;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBase;

/**
 * Created by Pabilo8 on 30-08-2019.
 */
public class ItemIICasingMachinegun extends ItemIIBase implements IBulletCasingType
{
	public ItemIICasingMachinegun()
	{
		super("casing_machinegun", 24);
	}

	@Override
	public String getName()
	{
		return "machinegun_2bCal";
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
	public float getSize()
	{
		return 0.125f;
	}

	@Override
	public int getStackSize()
	{
		return 24;
	}

	@Override
	public Class<? extends IBulletModel> getModel()
	{
		return ModelBullet.class;
	}

	@Override
	public float getPenetration()
	{
		return 0.5f;
	}

	@Override
	public float getDamage()
	{
		return 10;
	}

	@Override
	public ItemStack getStack(int amount)
	{
		return new ItemStack(CommonProxy.item_casing_machinegun, amount);
	}

	@Override
	public boolean isThrowable()
	{
		return false;
	}

	@Override
	public void doPuff(EntityBullet bullet)
	{
		ImmersiveIntelligence.proxy.spawnGunfireFX(bullet.world, bullet.posX, bullet.posY, bullet.posZ, bullet.motionX/2f, bullet.motionY/2f, bullet.motionZ/2f, 2.5f);
		ImmersiveEngineering.proxy.spawnRedstoneFX(bullet.world, bullet.posX, bullet.posY, bullet.posZ, 0, 0, 0, 1.5f, 0.75f, 0.75f, 0.75f);
	}
}
