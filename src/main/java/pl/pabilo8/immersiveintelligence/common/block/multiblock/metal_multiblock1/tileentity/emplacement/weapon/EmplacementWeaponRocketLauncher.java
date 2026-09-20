package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.RocketLauncher;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

/**
 * Implements the two-stage, six-round Rocket Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponRocketLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(RocketLauncher.minimumIdleTime,
				RocketLauncher.idleAnimationInterval, RocketLauncher.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemAmmoRocketLight);
		this.visionAABB = this.visionAABB.grow(RocketLauncher.detectionRadius);
		this.attackAABB = this.attackAABB.grow(RocketLauncher.attackRadius);
		setupItemHandlers(te, 24, 0, 16, 0,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(RocketLauncher.yawRotateSpeed, RocketLauncher.pitchRotateSpeed)
				.withPitchLimit(RocketLauncher.minPitch, RocketLauncher.maxPitch);
		this.rotateAfterFiring = false;
		this.gunHandler.withShootSound(IISounds.missileShot, 68);
	}

	@Override
	protected boolean isSpentCasing(ItemStack stack)
	{
		return false;
	}

	@Override
	protected boolean storesSpentCasings()
	{
		return false;
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{4, 4};
	}

	@Override
	protected Float getLoadingYaw()
	{
		return 180f;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}

	@Override
	protected Float getHidingYaw()
	{
		return 0f;
	}

	@Override
	protected Float getHidingPitch()
	{
		return 0f;
	}

	@Override
	public String getName()
	{
		return "rocket_launcher";
	}

	@Override
	public boolean isArtilleryWeapon()
	{
		return true;
	}

	@Override
	public boolean usesBallisticFireByDefault()
	{
		return true;
	}

	@Override
	public int getShotDelay()
	{
		return RocketLauncher.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return RocketLauncher.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return RocketLauncher.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return RocketLauncher.maxHealth;
	}
}
