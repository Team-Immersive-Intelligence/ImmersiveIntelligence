package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement.EmplacementStateNeeds;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.TargetCoordinateReference;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.01.2026
 */
public abstract class EmplacementWeaponTurretBase extends EmplacementWeapon
{
	public GunAimCoordinate aim = new GunAimCoordinate();
	@Nullable
	public MultiblockInteractablePart setup = null;
	public int shootDelay = 0, reloadDelay = 0;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	protected void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.aim.withCurrentAngles(te.facing.getHorizontalAngle(), 0);
	}

	@Override
	public EmplacementStateNeeds onUpdate(TileEntityEmplacement te, EmplacementStateNeeds baseNeeds, TargetCoordinateReference currentTarget)
	{
		if(shootDelay > 0)
			shootDelay--;

		if(currentTarget==null||!currentTarget.shouldBeExecuted(te.getWorld()))
		{
			if(this.setup!=null)
			{
				this.setup.setState(false);
				this.setup.update();
			}
			return super.onUpdate(te, baseNeeds, currentTarget);
		}

		Vec3d target = currentTarget.supplyCoordinates();
		if(target!=null)
			this.aim.setTarget(te.getWeaponCenter(), Vec3d.ZERO, target, getTargetMotion(currentTarget));
		this.aim.update();

		if(this.setup!=null)
		{
			this.setup.setState(true);
			this.setup.update();
		}

		if(isReadyToShoot(te)&&shoot(te, currentTarget))
			currentTarget.notifyAfterShot();

		return EmplacementStateNeeds.WANTS_SURFACE;
	}

	protected Vec3d getTargetMotion(TargetCoordinateReference target)
	{
		if(target.getEntity()!=null)
			return new Vec3d(target.getEntity().motionX, target.getEntity().motionY, target.getEntity().motionZ);
		return Vec3d.ZERO;
	}

	protected boolean isReadyToShoot(TileEntityEmplacement te)
	{
		return shootDelay <= 0&&te.door.isFullyOpened()&&(setup==null||setup.isFullyOpened())&&aim.isAimed(1.5f)&&canShoot(te);
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
		this.shootDelay = getShotDelay();
		return false;
	}

	public boolean requiresZeroingBeforeReload()
	{
		return true;
	}

	public abstract int getShotDelay();

	public abstract int getReloadDelay();

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setTag("aim", aim.serializeNBT());
		if(setup!=null)
			nbt.setTag("setup", setup.serializeNBT());
		nbt.setInteger("shootDelay", shootDelay);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		aim.deserializeNBT(nbt.getCompoundTag("aim"));
		if(setup!=null)
			setup.deserializeNBT(nbt.getCompoundTag("setup"));
		shootDelay = nbt.getInteger("shootDelay");
	}
}
