package pl.pabilo8.immersiveintelligence.common.item.ammo.gun;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_smg_1bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoSubmachinegun extends ItemIIAmmoBase<EntityAmmoProjectile>
{
	public ItemIIAmmoSubmachinegun()
	{
		super("smg_1bCal", Casings.SMG_1BCAL);
	}

	@Override
	public float getComponentAmount()
	{
		return 0.0625f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 10;
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
	public float getDefaultVelocity()
	{
		return Ammunition.smgVelocity;
	}

	@Override
	public float getPenetrationDepth()
	{
		return 1;
	}

	@Override
	public int getCaliber()
	{
		return 1;
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoBase<EntityAmmoProjectile>, IAmmoModel<ItemIIAmmoBase<EntityAmmoProjectile>, EntityAmmoProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
	}

	@Override
	public float getDamage()
	{
		return 3;
	}

	@Override
	public CoreTypes[] getAllowedCoreTypes()
	{
		return new CoreTypes[]{CoreTypes.SOFTPOINT, CoreTypes.PIERCING};
	}

	@Override
	public FuseTypes[] getAllowedFuseTypes()
	{
		return new FuseTypes[]{FuseTypes.CONTACT};
	}

	@Nonnull
	@Override
	public EntityAmmoProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoProjectile(world);
	}
}
