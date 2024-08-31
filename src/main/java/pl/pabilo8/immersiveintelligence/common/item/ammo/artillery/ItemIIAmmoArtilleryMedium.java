package pl.pabilo8.immersiveintelligence.common.item.ammo.artillery;

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
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoBase.AmmoParts;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIAmmoCasing.Casing;
import pl.pabilo8.immersiveintelligence.common.util.item.IICategory;
import pl.pabilo8.immersiveintelligence.common.util.item.IIItemEnum.IIItemProperties;
import pl.pabilo8.modworks.annotations.item.GeneratedItemModels;
import pl.pabilo8.modworks.annotations.item.ItemModelType;

import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 05.01.2024
 */
@IIAmmoProjectile(artillery = true)
@GeneratedItemModels(itemName = "bullet_artillery_6bcal_long", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoArtilleryMedium extends ItemIIAmmoBase<EntityAmmoArtilleryProjectile>
{
	public ItemIIAmmoArtilleryMedium()
	{
		super("artillery_6bCal_long", Casing.MEDIUM_ARTILLERY_6BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getPropellantNeeded()
	{
		return 650;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 3;
	}

	@Override
	public float getCasingMass()
	{
		return 0.6f;
	}

	@Override
	public float getVelocity()
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
		return 5;
	}

	@SideOnly(Side.CLIENT)
	@Nonnull
	@Override
	public Function<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>,
			IAmmoModel<ItemIIAmmoBase<EntityAmmoArtilleryProjectile>, EntityAmmoArtilleryProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
	}

	@Override
	public float getDamage()
	{
		return 150;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.PIERCING, CoreType.PIERCING_SABOT, CoreType.SHAPED, CoreType.SHAPED_SABOT, CoreType.CANISTER};
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT, FuseType.TIMED, FuseType.PROXIMITY};
	}

	@Override
	public float getSupressionRadius()
	{
		return 6;
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
