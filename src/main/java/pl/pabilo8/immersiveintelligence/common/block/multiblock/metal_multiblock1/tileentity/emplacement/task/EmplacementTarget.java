package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 22.02.2026
 */
public class EmplacementTarget implements INBTSerializable<NBTTagCompound>
{
	@Nullable
	private final BlockPos position;
	@Nullable
	private final Entity entity;

	private boolean shotsAreFinite = false;
	private int shotsRemaining = 0;

	public EmplacementTarget(@Nullable BlockPos position)
	{
		this.position = position;
		this.entity = null;
	}

	public EmplacementTarget(@Nullable Entity entity)
	{
		this.entity = entity;
		this.position = null;
	}

	public EmplacementTarget withShotLimit(int shotsRemaining)
	{
		this.shotsAreFinite = true;
		this.shotsRemaining = shotsRemaining;
		return this;
	}

	/**
	 * @return whether the task should be executed or removed from memory
	 */
	public boolean shouldBeExecuted()
	{
		return !shotsAreFinite||shotsRemaining > 0;
	}

	@Nonnull
	public Vec3d supplyCoordinates()
	{
		if(entity!=null)
			return new Vec3d(entity.posX, entity.posY, entity.posZ);
		assert position!=null;
		return new Vec3d(position);
	}

	/**
	 * Called by the weapon after a shot has been made to lower the counter
	 */
	public void notifyAfterShot()
	{
		if(shotsAreFinite)
			shotsRemaining--;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}
}
