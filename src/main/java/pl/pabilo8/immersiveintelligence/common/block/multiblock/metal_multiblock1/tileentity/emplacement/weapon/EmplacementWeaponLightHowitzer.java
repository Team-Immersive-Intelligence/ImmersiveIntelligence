package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;

/**
 * Implements the single-round Light Howitzer Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 18.08.2026
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
		if(this.chillingState==null)
			this.chillingState = new ChillingState(160, 120, 80);
		this.ammoFactory.setAmmo(IIContent.itemAmmoLightArtillery);
		this.visionAABB = this.visionAABB.grow(0);
		this.attackAABB = this.attackAABB.grow(240);

		setupItemHandlers(te, 8, 8, 4, 4,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(3.5f, 3.5f);
//				.withPitchLimit(-89.5f, 22.5f);
		this.gunHandler.withShootSound(IISounds.howitzerShot, 55);
		this.ammoFactory.setUseArtilleryAngles(true);
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
	public int getShotDelay()
	{
		return 30;
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
