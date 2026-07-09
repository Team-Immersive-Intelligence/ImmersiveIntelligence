package pl.pabilo8.immersiveintelligence.common.util.gun.ammoprovider;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAmmoProvider;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 26.05.2026
 */
public class GunAmmoProviderAmmoCrate extends GunAmmoProvider
{
	public GunAmmoProviderAmmoCrate(Entity gun, Supplier<Entity> operatorSupplier)
	{
		super(gun, operatorSupplier);
	}
	// For belt‑fed, no loading/unloading; always returns a bullet if crate has any.
	// This is a placeholder – implement according to your crate logic.

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
		// TODO: Query the crate inventory at gunPosition.down()
		return ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getAmmoList()
	{
		return NonNullList.create();
	}

	@Override
	protected boolean canFindAmmo()
	{
		return false;
	}
}
