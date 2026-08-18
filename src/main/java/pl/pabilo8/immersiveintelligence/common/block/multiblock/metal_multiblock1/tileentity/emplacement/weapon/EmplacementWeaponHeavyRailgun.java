package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.HeavyRailgun;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.item.weapons.ItemIIRailgunOverride;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

/**
 * Implements the four-round Heavy Railgun Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 17.08.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponHeavyRailgun extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponHeavyRailgun()
	{
		this.setup = new MultiblockInteractablePart(HeavyRailgun.setupTime);
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemRailgunGrenade);
		this.visionAABB = this.visionAABB.grow(HeavyRailgun.detectionRadius);
		this.attackAABB = this.attackAABB.grow(HeavyRailgun.attackRadius);
		setupItemHandlers(te, 3, 2, 8, 8, ItemIIRailgunOverride::isAmmo, ItemIIRailgunOverride::isAmmo);
		this.aim.withAimSpeed(HeavyRailgun.yawRotateSpeed, HeavyRailgun.pitchRotateSpeed);
	}

	@Override
	protected int[] getReloadStages()
	{
		return new int[]{4};
	}

	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}

	@Override
	public String getName()
	{
		return "heavy_railgun";
	}

	@Override
	public int getShotDelay()
	{
		return HeavyRailgun.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return HeavyRailgun.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return HeavyRailgun.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return HeavyRailgun.maxHealth;
	}
}
