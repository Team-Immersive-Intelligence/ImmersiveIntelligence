package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.Autocannon;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

/**
 * Implements the single-round Light Howitzer Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
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
		this.ammoFactory.setAmmo(IIContent.itemAmmoLightArtillery);
		this.visionAABB = this.visionAABB.grow(Autocannon.detectionRadius);
		this.attackAABB = this.attackAABB.grow(Autocannon.attackRadius);

		setupItemHandlers(te, 8, 12, 4, 4, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(3.5f, 3.5f);
		this.ammoFactory.setUseArtilleryAngles(false);
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
	public int getShotDelay()
	{
		return 10;
	}

	@Override
	public int getReloadDelay()
	{
		return 56;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return 512;
	}

	@Override
	public int getMaxHealth()
	{
		return 350;
	}
}
