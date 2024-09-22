package pl.pabilo8.immersiveintelligence.common.item.ammo;

import blusunrize.immersiveengineering.common.IEContent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseType;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
@GeneratedItemModels(itemName = "bullet_railgun_grenade_4bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIAmmoProjectile(artillery = true)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoRailgunGrenade extends ItemIIAmmoBase<EntityAmmoProjectile>
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
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.NONE;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 2;
	}

	@Override
	public float getCasingMass()
	{
		return 0.35f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.railgunGrenadeVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 4;
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoBase<EntityAmmoProjectile>, IAmmoModel<ItemIIAmmoBase<EntityAmmoProjectile>, EntityAmmoProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createEncasedProjectileModel;
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
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.PIERCING, CoreType.PIERCING_SABOT, CoreType.SHAPED, CoreType.SOFTPOINT, CoreType.CANISTER};
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT, FuseType.TIMED, FuseType.PROXIMITY};
	}

	@Override
	public float getPenetrationDepth()
	{
		return 3;
	}

	@Nonnull
	@Override
	public EntityAmmoProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoProjectile(world);
	}
}
