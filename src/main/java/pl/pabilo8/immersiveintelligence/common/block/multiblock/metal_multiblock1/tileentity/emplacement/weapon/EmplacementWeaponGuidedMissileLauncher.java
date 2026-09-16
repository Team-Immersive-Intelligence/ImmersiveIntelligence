package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.GuidedMissileLauncher;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoGuidedMissile;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;

import javax.annotation.Nullable;

/**
 * Implements the single-round Guided Missile Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponGuidedMissileLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	public EmplacementWeaponGuidedMissileLauncher()
	{
	}

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.ammoFactory.setAmmo(IIContent.itemAmmoGuidedMissile).setUseArtilleryAngles(false);
		this.visionAABB = this.visionAABB.grow(GuidedMissileLauncher.detectionRadius);
		this.attackAABB = this.attackAABB.grow(GuidedMissileLauncher.attackRadius);
		setupItemHandlers(te, 8, 0, 4, 0,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(GuidedMissileLauncher.yawRotateSpeed, GuidedMissileLauncher.pitchRotateSpeed)
				.withPitchLimit(GuidedMissileLauncher.minPitch, GuidedMissileLauncher.maxPitch);
	}

	@Override
	protected void configureProjectile(EntityAmmoProjectile projectile, TargetCoordinateReference target)
	{
		if(projectile instanceof EntityAmmoGuidedMissile)
			((EntityAmmoGuidedMissile)projectile).setHomingTarget(target.getEntity());
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
		return new int[]{1};
	}

	@Override
	public String getName()
	{
		return "guided_missile_launcher";
	}

	@Override
	public int getShotDelay()
	{
		return GuidedMissileLauncher.shotFireTime;
	}

	@Override
	public int getReloadDelay()
	{
		return GuidedMissileLauncher.reloadTime;
	}

	@Override
	public int getEnergyUpkeepCost()
	{
		return GuidedMissileLauncher.energyUpkeepCost;
	}

	@Override
	public int getMaxHealth()
	{
		return GuidedMissileLauncher.maxHealth;
	}

	@Nullable
	@Override
	protected Float getHidingPitch()
	{
		return 0f;
	}

	@Nullable
	@Override
	protected Float getLoadingPitch()
	{
		return 0f;
	}
}
