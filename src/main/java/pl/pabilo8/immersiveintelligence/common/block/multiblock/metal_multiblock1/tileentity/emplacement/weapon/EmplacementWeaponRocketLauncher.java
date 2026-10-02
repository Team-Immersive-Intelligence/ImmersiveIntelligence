package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.EmplacementWeapons.RocketLauncher;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoProjectile;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.ChillingState;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;

/**
 * Implements the two-stage, eight-round Rocket Launcher Emplacement weapon.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 27.09.2026
 * @since 01.01.2026
 */
public class EmplacementWeaponRocketLauncher extends EmplacementWeaponGunBase<EntityAmmoProjectile>
{
	private static final int UPPER_ROCKET_COUNT = 4;
	private transient ResLoc loadUpperAnimation, loadLowerAnimation;

	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.loadUpperAnimation = ResLoc.of(IIReference.RES_II, "emplacement/weapon/", getName(), "/load_upper");
		this.loadLowerAnimation = ResLoc.of(IIReference.RES_II, "emplacement/weapon/", getName(), "/load_lower");
		this.chillingState = new ChillingState(RocketLauncher.minimumIdleTime,
				RocketLauncher.idleAnimationInterval, RocketLauncher.idleAnimationDuration);
		this.ammoFactory.setAmmo(IIContent.itemAmmoRocketLight);
		this.visionAABB = this.visionAABB.grow(RocketLauncher.detectionRadius);
		this.attackAABB = this.attackAABB.grow(RocketLauncher.attackRadius);
		setupItemHandlers(te, 24, 0, 16, 0,
				this.ammoFactory::isValidAmmo, this.ammoFactory::isValidAmmo);
		this.aim.withAimSpeed(RocketLauncher.yawRotateSpeed, RocketLauncher.pitchRotateSpeed)
				.withYawLimit(RocketLauncher.minYaw, RocketLauncher.maxYaw)
				.withPitchLimit(RocketLauncher.minPitch, RocketLauncher.maxPitch);
		this.rotateAfterFiring = false;
		this.gunHandler.withShootSound(IISounds.missileShot, 68);
	}

	@Override
	public void applyTactileAnimations(TileEntityEmplacement te)
	{
		float loading = getReloadProgress(0);
		boolean lowerStage = getReloadStage() > 0;
		applyTurretTactileAnimationSet(te,
				new ResLoc[]{loadUpperAnimation, loadLowerAnimation},
				new float[]{lowerStage?1f: loading, lowerStage?loading: 0f});
	}

	@Override
	protected void configureProjectile(TileEntityEmplacement te, EntityAmmoProjectile projectile, TargetCoordinateReference target)
	{
		//Rounds disappear from the end of the rendered rack: after the lower bank is empty,
		//a post-consumption count below four identifies a shot from the upper bank.
		if(platformAmmoProvider!=null&&platformAmmoProvider.getLoadedRoundCount() < UPPER_ROCKET_COUNT)
			projectile.setPosition(projectile.posX, projectile.posY+0.5, projectile.posZ);
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
		return new int[]{UPPER_ROCKET_COUNT, 4};
	}

	@Override
	protected IISoundAnimation[] createLoadingSoundAnimations()
	{
		//The sound ranges follow the loader, winch, and chain movement in load_upper/lower.json.
		return new IISoundAnimation[]{
				new IISoundAnimation(1d)
						.withRepeatedSound(0.07500, 0.90000, IISounds.electricMotorForwardLoop)
						.withRepeatedSound(0.17500, 0.75125, IISounds.chainLoop),
				new IISoundAnimation(1d)
						.withRepeatedSound(0.02500, 0.99167, IISounds.electricMotorForwardLoop)
						.withRepeatedSound(0.22500, 0.81458, IISounds.chainLoop)
		};
	}

	@Override
	public Vec3d getWeaponOffset()
	{
		return new Vec3d(0, 1, 0);
	}

	@Override
	protected Float getLoadingYaw()
	{
		return RocketLauncher.loadingYaw;
	}

	@Override
	protected Float getLoadingPitch()
	{
		return RocketLauncher.loadingPitch;
	}

	@Override
	protected Float getHidingYaw()
	{
		return RocketLauncher.hidingYaw;
	}

	@Override
	protected Float getHidingPitch()
	{
		return RocketLauncher.hidingPitch;
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
