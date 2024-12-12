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
 * @author Prism
 * @updated 07.02.2024
 * @ii-approved 0.3.1
 * @since 30.11.2020
 */
@IIAmmoProjectile
@GeneratedItemModels(itemName = "bullet_pistol_1bcal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
@IIItemProperties(category = IICategory.WARFARE)
public class ItemIIAmmoPistol extends ItemIIAmmoBase<EntityAmmoProjectile>
{
	public ItemIIAmmoPistol()
	{
		super("pistol_1bCal", Casing.SMG_1BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.085f;
	}

	@Override
	public float getPenetrationDepth() { return super.getPenetrationDepth(); }

	@Override
	public int getPropellantNeeded()
	{
		return 15;
	}

	@Override
	public PropellantType getAllowedPropellants()
	{
		return PropellantType.SOLID;
	}

	@Override
	public int getCoreMaterialNeeded()
	{
		return 1;
	}

	@Override
	public float getCasingMass()
	{
		return 0.0625f;
	}

	@Override
	public float getVelocity()
	{
		return Ammunition.stgVelocity;
	}

	@Override
	public int getCaliber()
	{
		return 1;
	}

	@Override
	public float getDamage()
	{
		return 3;
	}

	@Override
	public CoreType[] getAllowedCoreTypes()
	{
		return new CoreType[]{CoreType.SOFTPOINT, CoreType.PIERCING};
	}

	@Nonnull
	@SideOnly(Side.CLIENT)
	public Function<ItemIIAmmoBase<EntityAmmoProjectile>, IAmmoModel<ItemIIAmmoBase<EntityAmmoProjectile>, EntityAmmoProjectile>> get3DModel()
	{
		return ModelAmmoProjectile::createProjectileModel;
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
}
