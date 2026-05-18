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
		Vec3d target = currentTarget.supplyCoordinates();
		if(target!=null)
			this.aim.setTarget(te.getWeaponCenter(), Vec3d.ZERO,
					target, Vec3d.ZERO);
		this.aim.update();

		if(this.setup!=null)
		{
			this.setup.setState(true);
			this.setup.update();
		}

		return super.onUpdate(te, baseNeeds, currentTarget);
	}

	public abstract boolean canShoot(TileEntityEmplacement te);

	/**
	 * Used for shooting action
	 */
	public void shoot(TileEntityEmplacement te)
	{
		if(baseEntity!=null)
			Utils.attractEnemies(baseEntity, 24);
		this.shootDelay = getShotDelay();
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
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		super.deserializeNBT(nbt);
		aim.deserializeNBT(nbt.getCompoundTag("aim"));
		if(setup!=null)
			setup.deserializeNBT(nbt.getCompoundTag("setup"));
	}
}
