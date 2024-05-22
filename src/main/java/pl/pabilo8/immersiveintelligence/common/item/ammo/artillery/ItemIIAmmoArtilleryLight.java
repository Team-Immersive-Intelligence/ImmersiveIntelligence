package pl.pabilo8.immersiveintelligence.common.item.ammo.artillery;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Ammunition;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casings;
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
//TODO: 08.03.2024 update values from notes
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_artillery_6bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoArtilleryLight extends ItemIIAmmoBase<EntityAmmoArtilleryProjectile>
{
	public ItemIIAmmoArtilleryLight()
	{
		super("artillery_6bCal", Casings.LIGHT_ARTILLERY_6BCAL);
	}

	@Override
	public float getComponentAmount()
	{
		return 0.65f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 500;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getInitialMass()
	{
		return 0.85f;
	}

	@Override
	public float getDefaultVelocity()
	{
		return Ammunition.lightHowiVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 6;
	}

	@Override
	public float getPenetrationDepth()
	{
		return 4;
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>,
			IAmmoModel<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>, EntityAmmoArtilleryProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
	}

	@Override
	public float getDamage()
	{
		return 30;
	}

	@Override
	public CoreTypes[] getAllowedCoreTypes()
	{
		return new CoreTypes[]{CoreTypes.PIERCING, CoreTypes.SHAPED, CoreTypes.CANISTER};
	}

	@Override
	public FuseTypes[] getAllowedFuseTypes()
	{
		return new FuseTypes[]{FuseTypes.CONTACT, FuseTypes.TIMED, FuseTypes.PROXIMITY};
	}

	@Override
	public float getSupressionRadius()
	{
		return 3;
	}

	@Override
	public int getSuppressionPower()
	{
		return 20;
	}

	@Nonnull
	@Override
	public EntityAmmoArtilleryProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoArtilleryProjectile(world);
	}
}
