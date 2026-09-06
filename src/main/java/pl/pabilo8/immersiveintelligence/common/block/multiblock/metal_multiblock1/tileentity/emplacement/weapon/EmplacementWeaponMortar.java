package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;

import javax.annotation.Nullable;

/**
 * Implements the single-round Mortar Emplacement weapon without spent casings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
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
		this.ammoFactory.setAmmo(IIContent.itemAmmoMortar);
		this.visionAABB = this.visionAABB.grow(0);
		this.attackAABB = this.attackAABB.grow(160);
		setupItemHandlers(te, 12, 0, 4, 0, this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(4f, 2.5f)
				.withPitchLimit(-89.5f, 45f);
		this.ammoFactory.setUseArtilleryAngles(true);
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
	public int getShotDelay()
	{
		return 25;
	}

	@Override
	public int getReloadDelay()
	{
		return 120;
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
