package pl.pabilo8.immersiveintelligence.common.item.ammo;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.client.model.IBulletModel;
import pl.pabilo8.immersiveintelligence.client.model.bullet.ModelBullet3bCal;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_autocannon_3bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoAutocannon extends ItemIIAmmoBase
{
	public ItemIIAmmoAutocannon()
	{
		super("autocannon_3bCal", Casings.AUTOCANNON_3BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.125f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 7;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 6;
	}

	@Override
	public float getInitialMass()
	{
		return 0.185f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.autocannonVelocity;
	}

	@Override
	public float getCaliber()
	{
		return 3f;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public @Nonnull Class<? extends IBulletModel> getModel()
	{
		return ModelBullet3bCal.class;
	}

	@Override
	public float getDamage()
	{
		return 10;
	}

	@Override
	public EnumCoreTypes[] getAllowedCoreTypes()
	{
		return new EnumCoreTypes[]{EnumCoreTypes.SOFTPOINT, EnumCoreTypes.PIERCING};
	}

	@Override
	public EnumFuseTypes[] getAllowedFuseTypes()
	{
		return new EnumFuseTypes[]{EnumFuseTypes.CONTACT, EnumFuseTypes.TIMED, EnumFuseTypes.PROXIMITY};
	}

	@Override
	public float getSupressionRadius()
	{
		return 2.5f;
	}

	@Override
	public int getSuppressionPower()
	{
		return 2;
	}
}
