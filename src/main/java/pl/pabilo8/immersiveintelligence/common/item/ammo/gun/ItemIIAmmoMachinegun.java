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
 * @since 30-08-2019
 */
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_mg_2bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoMachinegun extends ItemIIAmmoBase<EntityAmmoProjectile>
{
	public ItemIIAmmoMachinegun()
	{
		super("mg_2bCal", Casing.MG_2BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.125f;
	}

	@Override
	public int getPropellantNeeded()
	{
		return 25;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}


	@Override
	public int getCoreMaterialNeeded()
	{
		return 4;
	}

	@Override
	public float getCasingMass()
	{
		return 0.125f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.mgVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 2;
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
		return 5;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.SOFTPOINT, CoreType.PIERCING};
	}

	@Override
	public float getSupressionRadius()
	{
		return 2;
	}

	@Override
	public int getSuppressionPower()
	{
		return 2;
	}

	@Nonnull
	@Override
	public EntityAmmoProjectile getAmmoEntity(World world)
	{
		return new EntityAmmoProjectile(world);
	}

	@Override
	public FuseType[] getAllowedFuseTypes()
	{
		return new FuseType[]{FuseType.CONTACT};
	}

	@Override
	public float getPenetrationDepth()
	{
		return 2;
	}
}
