package pl.pabilo8.immersiveintelligence.common.items.ammunition;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Bullets;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelRailgunGrenade;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class ItemIIAmmoRailgunGrenade extends ItemIIBulletBase
{
	public ItemIIAmmoRailgunGrenade()
	{
		super("railgun_grenade_4bCal", 8);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.3f;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getInitialMass()
	{
		return 0.35f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Bullets.railgunGrenadeVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 4f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelRailgunGrenade.class;
	}

	@Override
	public float getDamage()
	{
		return 5;
	}

	@Override
	public ItemStack getCasingStack(int amount)
	{
		return new ItemStack(IEContent.itemMaterial, amount, 2);
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.PIERCING, EnumCoreTypes.PIERCING_SABOT, EnumCoreTypes.SHAPED, EnumCoreTypes.SOFTPOINT, EnumCoreTypes.CANISTER};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT,EnumFuseTypes.TIMED,EnumFuseTypes.PROXIMITY};
	}
}
