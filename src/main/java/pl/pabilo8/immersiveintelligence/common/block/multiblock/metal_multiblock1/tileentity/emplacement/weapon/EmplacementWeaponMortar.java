package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Mortar;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

import javax.annotation.Nullable;

/**
 * Implements the single-round Mortar Emplacement weapon without spent casings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponMortar extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponMortar()
	{

	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.chillingState = new ChillingState(200, 240, 80);
		this.ammoFactory.setAmmo(IIContent.itemAmmoMortar);
		this.visionAABB = this.visionAABB.grow(Mortar.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Mortar.attackRadius);
		setupItemHandlers(te, 12, 0, 4, 0, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(Mortar.yawRotateSpeed, Mortar.pitchRotateSpeed)
				.withPitchLimit(Mortar.minPitch, Mortar.maxPitch);
		this.rotateAfterFiring = false;
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{1};
	}

	@Override
	protected boolean storesSpentCasings()
	{
		return false;
	}

	@Nullable
	@Override
	protected Float getLoadingYaw()
	{
		return 180f;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return -57f;
	}

	@Nullable
	@Override
	protected Float getHidingYaw()
	{
		return 180f;
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return -90f;
	}

	@Override
	public String getName()
	{
		return "mortar";
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
		return Mortar.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return Mortar.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return Mortar.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return Mortar.maxHealth;
	}
}
