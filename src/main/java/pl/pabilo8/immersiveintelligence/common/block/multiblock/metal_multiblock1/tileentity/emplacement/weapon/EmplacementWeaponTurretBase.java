package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 16.08.2026
 * @since 01.01.2026
 */
public abstract class EmplacementWeaponTurretBase extends EmplacementWeapon
{
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_ROTATION)
	public GunAimCoordinate aim = new GunAimCoordinate();
	@Nullable
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC, nullable = true)
	public MultiblockInteractablePart setup = null;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		if(!restoredFromNBT)
			this.aim.withCurrentAngles(te.facing.getHorizontalAngle(), 0);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		boolean remote = te.getWorld().isRemote;
		if(currentTarget==null||!currentTarget.shouldBeExecuted(te.getWorld()))
		{
			boolean setupChanged = false;
			if(this.setup!=null)
			{
				if(!remote)
					setupChanged = this.setup.setState(false);
				this.setup.update();
			}

			//The server owns target selection, but the client must keep advancing the last synced aim.
			float previousYaw = this.aim.getYaw(0);
			float previousPitch = this.aim.getPitch(0);
			this.aim.update();

			if(!remote&&hasCurrentAngleChanged(previousYaw, previousPitch))
				syncWithClient(te, SyncEvents.WEAPON_ROTATION);
			if(setupChanged)
				syncWithClient(te, SyncEvents.WEAPON_MISC);
			return super.onUpdate(te, baseNeeds, currentTarget);
		}

		boolean rotationChanged = false;
		float previousYaw = this.aim.getYaw(0);
		float previousPitch = this.aim.getPitch(0);
		if(!remote)
		{
			Vec3d target = currentTarget.supplyCoordinates();
			if(target!=null)
			{
				float previousTargetYaw = this.aim.getTargetYaw();
				float previousTargetPitch = this.aim.getTargetPitch();
				if(this.aim.setTarget(te.getWeaponCenter(), Vec3d.ZERO, target, getTargetMotion(currentTarget)))
					rotationChanged = hasTargetAngleChanged(previousTargetYaw, previousTargetPitch);
			}
		}
		//Do not calculate targets on the client. It only interpolates the server-owned aim state.
		this.aim.update();
		if(!remote)
			rotationChanged |= hasCurrentAngleChanged(previousYaw, previousPitch);

		boolean setupChanged = false;
		if(this.setup!=null)
		{
			if(!remote)
				setupChanged = this.setup.setState(true);
			this.setup.update();
		}

		boolean fired = !remote&&isReadyToShoot(te)&&shoot(te, currentTarget);
		if(fired)
			currentTarget.notifyAfterShot();

		if(!remote)
		{
			if(rotationChanged)
				syncWithClient(te, SyncEvents.WEAPON_ROTATION);
			if(setupChanged)
				syncWithClient(te, SyncEvents.WEAPON_MISC);
			if(fired)
				syncWithClient(te, SyncEvents.WEAPON_RELOAD);
		}
		return EmplacementStateNeeds.WANTS_SURFACE;
	}

	private boolean hasTargetAngleChanged(float previousYaw, float previousPitch)
	{
		return Math.abs(MathHelper.wrapDegrees(this.aim.getTargetYaw()-previousYaw)) > 0.001f
				||Math.abs(this.aim.getTargetPitch()-previousPitch) > 0.001f;
	}

	private boolean hasCurrentAngleChanged(float previousYaw, float previousPitch)
	{
		return Math.abs(MathHelper.wrapDegrees(this.aim.getYaw(0)-previousYaw)) > 0.001f
				||Math.abs(this.aim.getPitch(0)-previousPitch) > 0.001f;
	}

	protected Vec3d getTargetMotion(TargetCoordinateReference target)
	{
		if(target.getEntity()!=null)
			return new Vec3d(target.getEntity().motionX, target.getEntity().motionY, target.getEntity().motionZ);
		return Vec3d.ZERO;
	}

	protected boolean isReadyToShoot(TileEntityEmplacement te)
	{
		return te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened())&&aim.isAimed(1.5f)&&canShoot(te);
	}

	public abstract boolean canShoot(TileEntityEmplacement te);

	/**
	 * Used for shooting action. Base turret logic only handles attract/noise bookkeeping;
	 * concrete weapons should return true only after actually spawning a projectile/effect.
	 */
	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		if(baseEntity!=null)
			Utils.attractEnemies(baseEntity, 24);
		return false;
	}

	public boolean requiresZeroingBeforeReload()
	{
		return true;
	}

	public abstract int getShotDelay();

	public abstract int getReloadDelay();

}
