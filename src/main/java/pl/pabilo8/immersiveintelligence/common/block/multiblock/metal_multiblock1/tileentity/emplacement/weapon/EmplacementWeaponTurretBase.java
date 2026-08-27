package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Controls aiming, platform movement pose, setup, and firing for angle-based Emplacement weapons.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 20.08.2026
 * @since 01.01.2026
 */
public abstract class EmplacementWeaponTurretBase extends EmplacementWeapon
{
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_ROTATION)
	public GunAimCoordinate aim = new GunAimCoordinate();
	@Nullable
	@SyncNBT(time = 0, events = SyncEvents.WEAPON_MISC, nullable = true)
	public MultiblockInteractablePart setup = null;
	private int casingOutputTicker = 0;

	/**
	 * Initializes the weapon with the Emplacement facing as its local yaw center.
	 */
	@Override
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.aim.withCenterYaw(te.facing.getHorizontalAngle());
		if(!restoredFromNBT)
			this.aim.withCurrentAngles(this.aim.getCenterYaw(), this.aim.clampPitchToRange(90f));
	}

	@Override
	public void onPlatformUpdate(TileEntityEmplacement te)
	{
		boolean exposed = te.door.getState()&&te.door.isFullyOpened();
		boolean setupChanged = false;
		if(this.setup!=null)
		{
			setupChanged = this.setup.setState(exposed);
			this.setup.update();
		}

		//Only platform movement and the hidden state force the weapon into its hiding pose.
		if(!exposed)
			setAimTargetAngles(te, getHidingYaw(), getHidingPitch());
		updateAim(te);

		//The Base casing storage is stationary, so it can continue emptying while the platform operates.
		if(++casingOutputTicker >= Math.max(0, getItemTransferSpeed()))
		{
			casingOutputTicker = 0;
			ItemStack casing = extractBaseCasing(1);
			if(!casing.isEmpty())
			{
				te.outputItem(casing);
				if(getItemTransferSpeed() > 10||te.getWorld().getTotalWorldTime()%10==0)
					te.updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}
		}

		if(setupChanged)
			syncWithClient(te, SyncEvents.WEAPON_MISC);
	}

	@Override
	public void onClientUpdate(TileEntityEmplacement te)
	{
		//The client only advances state received from the server.
		if(this.setup!=null)
			this.setup.update();
		this.aim.update();
		super.onClientUpdate(te);
	}

	@Override
	protected boolean canChill(TileEntityEmplacement te)
	{
		return te.door.getState()&&te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened());
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		//onPlatformUpdate owns passive aim movement and the hiding pose.
		if(!te.door.getState()||!te.door.isFullyOpened())
			return EmplacementStateNeeds.WANTS_SURFACE;

		//Keep the current aim until setup is complete.
		if(setup!=null&&!setup.isFullyOpened())
			return EmplacementStateNeeds.WANTS_SURFACE;

		//Freeze an exposed idle weapon at its current angle. A gun can reserve the aim for loading.
		if(currentTarget==null||!currentTarget.shouldBeExecuted(te.getWorld()))
		{
			if(canTrackTarget(te))
				setAimTargetAngles(te, null, null);
			return EmplacementStateNeeds.WANTS_SURFACE;
		}
		if(!canTrackTarget(te))
			return EmplacementStateNeeds.WANTS_SURFACE;

		boolean rotationChanged = false;
		Vec3d target = currentTarget.supplyCoordinates();
		if(target!=null)
		{
			float previousTargetYaw = this.aim.getTargetYaw();
			float previousTargetPitch = this.aim.getTargetPitch();
			if(this.aim.setTarget(te.getWeaponCenter(), Vec3d.ZERO, target, currentTarget.supplyMotion()))
				rotationChanged = hasTargetAngleChanged(previousTargetYaw, previousTargetPitch);
		}

		boolean fired = te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened())&&aim.isAimed(1.5f)&&canShoot(te)
				&&shoot(te, currentTarget);
		if(fired)
			currentTarget.notifyAfterShot();

		if(rotationChanged)
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);
		if(fired)
			syncWithClient(te, SyncEvents.WEAPON_RELOAD);
		return EmplacementStateNeeds.WANTS_SURFACE;
	}

	/**
	 * Sets nullable local yaw and absolute pitch constraints without advancing the aim.
	 */
	protected final void setAimTargetAngles(TileEntityEmplacement te, @Nullable Float localYaw, @Nullable Float pitch)
	{
		if(te.getWorld().isRemote)
			return;

		float previousTargetYaw = aim.getTargetYaw();
		float previousTargetPitch = aim.getTargetPitch();
		float targetYaw = localYaw==null?aim.getYaw(0): MathHelper.wrapDegrees(aim.getCenterYaw()+localYaw);
		float targetPitch = pitch==null?aim.getPitch(0): pitch;
		aim.setTargetClamped(targetYaw, targetPitch);

		if(hasTargetAngleChanged(previousTargetYaw, previousTargetPitch))
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);
	}

	/**
	 * Checks nullable local yaw and absolute pitch constraints.
	 */
	protected final boolean isAtAngles(@Nullable Float localYaw, @Nullable Float pitch, float tolerance)
	{
		return (localYaw==null||Math.abs(MathHelper.wrapDegrees(aim.getRelativeYaw(0)-localYaw)) <= tolerance)
				&&(pitch==null||Math.abs(aim.getPitch(0)-aim.clampPitchToRange(pitch)) <= tolerance);
	}

	/**
	 * @return true when this weapon can track a fire mission target
	 */
	protected boolean canTrackTarget(TileEntityEmplacement te)
	{
		return true;
	}

	private void updateAim(TileEntityEmplacement te)
	{
		float previousYaw = aim.getYaw(0);
		float previousPitch = aim.getPitch(0);
		aim.update();
		if(hasCurrentAngleChanged(previousYaw, previousPitch))
			syncWithClient(te, SyncEvents.WEAPON_ROTATION);
	}

	/**
	 * Extracts one casing from stationary Base storage for the output port.
	 */
	@Nonnull
	protected ItemStack extractBaseCasing(int amount)
	{
		return ItemStack.EMPTY;
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

	public abstract boolean canShoot(TileEntityEmplacement te);

	protected boolean shoot(TileEntityEmplacement te, TargetCoordinateReference target)
	{
		if(baseEntity!=null)
			Utils.attractEnemies(baseEntity, 24);
		return false;
	}

	//--- Abstract Methods ---//

	public abstract int getShotDelay();

	public abstract int getReloadDelay();

	/**
	 * @return interval in ticks between Base casing output attempts
	 */
	protected int getItemTransferSpeed()
	{
		return Emplacement.itemTransferInterval;
	}

	/**
	 * @return local yaw used while the platform moves or stays hidden; null keeps the current yaw
	 */
	@Nullable
	protected Float getHidingYaw()
	{
		return 0f;
	}

	/**
	 * @return pitch used while the platform moves or stays hidden; null keeps the current pitch
	 */
	@Nullable
	protected Float getHidingPitch()
	{
		return aim.clampPitchToRange(-90f);
	}
}
