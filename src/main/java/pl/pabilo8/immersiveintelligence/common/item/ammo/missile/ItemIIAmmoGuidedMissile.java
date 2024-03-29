package pl.pabilo8.immersiveintelligence.common.item.ammo.missile;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.FuseTypes;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.IAmmoTypeItem.IIAmmoProjectile;
import pl.pabilo8.immersiveintelligence.client.model.builtin.IAmmoModel;
import pl.pabilo8.immersiveintelligence.client.model.builtin.ModelAmmoMissile;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoGuidedMissile;
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
//TODO: 08.03.2024 update values from notes
@IIAmmoProjectile
@GeneratedItemModels(itemName = "missile_guided_6bCal", type = ItemModelType.ITEM_SIMPLE_AUTOREPLACED, valueSet = AmmoParts.class)
public class ItemIIAmmoGuidedMissile extends ItemIIAmmoBase<EntityAmmoGuidedMissile>
{
	public ItemIIAmmoGuidedMissile()
	{
		super("missile_guided_6bCal", Casings.LIGHT_ARTILLERY_6BCAL);
	}

	@Override
	public float getComponentMultiplier()
	{
		return 0.65f;
	}

	@Override
	public int getGunpowderNeeded()
	{
		return 2000;
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
		return 4f;
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
	public Function<ItemIIAmmoBase<EntityAmmoGuidedMissile>,
			IAmmoModel<ItemIIAmmoBase<EntityAmmoGuidedMissile>, EntityAmmoGuidedMissile>> get3DModel()
	{
		return ModelAmmoMissile::createMissileModel;
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
	public EntityAmmoGuidedMissile getAmmoEntity(World world)
	{
		return new EntityAmmoGuidedMissile(world);
	}
}
