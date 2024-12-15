package pl.pabilo8.immersiveintelligence.common.item.ammo.gun;

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
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoShotgunProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author Prism
 * @ii-approved 0.3.1
 * @since 12.10.2024
 */
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_gun_3bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoShotgun extends ItemIIAmmoBase<EntityAmmoProjectile>
{
	public ItemIIAmmoShotgun()
	{
		super("gun_3bCal", Casing.SHOTGUN_3BCAL);
	}

	@Override
	public int getPropellantNeeded()
	{
		return 450;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 8;
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[] {FuseType.CONTACT};
	}

	@Override
	public float getCasingMass()
	{
		return 0.3f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.lightHowiVelocity;
	}

	@Override
	public float getDamage()
	{
		return 8;
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.35f;
	}

	@Override
	public float getPenetrationDepth()
	{
		return 2.5f;
	}

	@Override
	public int getCaliber()
	{
		return 3;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[] {CoreType.PIERCING, CoreType.CLUSTER, CoreType.CANISTER, CoreType.PIERCING_SABOT, CoreType.SHAPED};
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoBase<EntityAmmoProjectile>,
			IAmmoModel<ItemIIAmmoBase<EntityAmmoProjectile>, EntityAmmoProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
	}

	@Nonnull
	@Override
	public EntityAmmoShotgunProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoShotgunProjectile(world);
	}
}
