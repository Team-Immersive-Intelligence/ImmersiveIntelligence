package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

/**
 * Implements the two-stage, six-round Rocket Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponRocketLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoRocketLight);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);
		setupItemHandlers(te, 24, 12, 12, 12, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(2f, 1f);
		this.ammoFactory.setUseArtilleryAngles(true);
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{3, 3};
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
	public int getShotDelay()
	{
		return 20;
	}

	@Override
	public int getReloadDelay()
	{
		return 280;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return 1024;
	}

	@Override
	public int getMaxHealth()
	{
		return 300;
	}
}
