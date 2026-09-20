package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.LightHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

/**
 * Implements the single-round Light Howitzer Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponLightHowitzer extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponLightHowitzer()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(LightHowitzer.minimumIdleTime,
				LightHowitzer.idleAnimationInterval, LightHowitzer.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemAmmoLightArtillery);
		this.visionAABB = this.visionAABB.grow(LightHowitzer.detectionRadius);
		this.attackAABB = this.attackAABB.grow(LightHowitzer.attackRadius);

		setupItemHandlers(te, 8, 8, 4, 4,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(LightHowitzer.yawRotateSpeed, LightHowitzer.pitchRotateSpeed)
				.withPitchLimit(-90f, 22.5f);
		this.gunHandler.withShootSound(IISounds.howitzerShot, 55);
		this.rotateAfterFiring = false;
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1};
	}

	@Override
	protected Float getLoadingYaw()
	{
		return 0f;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}

	@Override
	public String getName()
	{
		return "light_howitzer";
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
		return LightHowitzer.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return LightHowitzer.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return LightHowitzer.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return LightHowitzer.maxHealth;
	}
}
