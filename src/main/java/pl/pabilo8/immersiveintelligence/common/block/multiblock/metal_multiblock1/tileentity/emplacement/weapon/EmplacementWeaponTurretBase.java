package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.weapon;

import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.entity.EntityEmplacementWeapon;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 01.01.2026
 */
public abstract class EmplacementWeaponTurretBase extends EmplacementWeapon implements IAimedEmplacementWeapon
{
	public float pitch = 0, yaw = 0;
	protected float nextPitch = 0, nextYaw = 0;
	protected Vec3d aimVector = Vec3d.ZERO;

	protected int shootDelay = 0, reloadDelay = 0, setupDelay = 0;

	/**
	 * Called after the weapon is installed or loaded from NBT
	 * Initialize sight AABB here
	 */
	public void onInit(TileEntityEmplacement te)
	{
		super.onInit(te);
		this.nextPitch = this.pitch = -90;
		this.nextYaw = this.yaw = te.facing.getHorizontalAngle();
	}

	@Override
	public void syncWithEntity(EntityEmplacementWeapon entity)
	{
		super.syncWithEntity(entity);
		entity.rotationYaw = yaw;
		entity.rotationPitch = pitch;
	}

	public abstract boolean canShoot(TileEntityEmplacement te);

	/**
	 * Used for shooting action
	 */
	public void shoot(TileEntityEmplacement te)
	{
		if(entity!=null)
			Utils.attractEnemies(entity, 24);
	}

	@Override
	public float getYaw()
	{
		return yaw;
	}

	@Override
	public float getPitch()
	{
		return pitch;
	}

	public abstract float getYawTurnSpeed();

	public abstract float getPitchTurnSpeed();

	public float getPitchUpperLimit()
	{
		return 90;
	}

	public float getPitchLowerLimit()
	{
		return -45;
	}

	public boolean requiresZeroingBeforeReload()
	{
		return true;
	}

	public abstract float getShotDelay();

	public abstract float getReloadDelay();

	public abstract float getSetupDelay();

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = super.serializeNBT();
		nbt.setFloat("pitch", pitch);
		nbt.setFloat("yaw", yaw);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		pitch = nbt.getFloat("pitch");
		yaw = nbt.getFloat("yaw");
	}
}
