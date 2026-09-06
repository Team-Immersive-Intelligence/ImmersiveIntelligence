package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.api.ammocrate.AmmunitionCrateHandler;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Supplies a belt-fed mounted weapon directly from an Ammunition Crate.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.05.2026
 */
public class GunAmmoProviderAmmoCrate extends GunAmmoProvider
{
	public GunAmmoProviderAmmoCrate(Entity gun, Supplier<Entity> operatorSupplier)
	{
		super(gun, operatorSupplier);
		this.maxReload = 1;
	}

	@Override
	protected void onLoadFinished()
	{
	}

	@Override
	protected void onUnloadFinished()
	{
	}

	@Nonnull
	@Override
	protected ItemStack provideNextBullet()
	{
		return AmmunitionCrateHandler.extractMountedBullet(gun);
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		return AmmunitionCrateHandler.getMountedAmmunition(gun);
	}

	@Override
	protected boolean canFindAmmo()
	{
		return AmmunitionCrateHandler.hasMountedBullet(gun);
	}
}
