package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelRailgunGrenade;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_railgun_grenade_4bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoRailgunGrenade extends ItemIIAmmoBase
{
	public ItemIIAmmoRailgunGrenade()
	{
		super("railgun_grenade_4bCal", null);
		setMaxStackSize(8);
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
		return Ammunition.railgunGrenadeVelocity;
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
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.TIMED, EnumFuseTypes.PROXIMITY};
	}
}
