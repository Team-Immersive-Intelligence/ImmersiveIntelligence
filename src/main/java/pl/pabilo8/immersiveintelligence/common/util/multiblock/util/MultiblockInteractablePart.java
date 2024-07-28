package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Represents an animated part of the multiblock, like a drawer
 */
public class MultiblockInteractablePart implements INBTSerializable<NBTTagCompound>
{
	boolean opened = false;
	float progress = 0;
	final float maxProgress;

	public MultiblockInteractablePart(float maxProgress)
	{
		this.maxProgress = maxProgress;
	}

	public float getProgress(float partialTicks)
	{
		return MathHelper.clamp(progress+(opened?partialTicks: -partialTicks), 0, maxProgress)/maxProgress;
	}

	public void update()
	{
		this.progress = MathHelper.clamp(progress+(opened?1: -1), 0, maxProgress);
	}

	/**
	 * @param state whether the part is opened
	 * @return true if the state changed
	 */
	public boolean setState(boolean state)
	{
		if(state!=opened)
		{
			this.opened = state;
			return true;
		}
		return false;
	}


	/**
	 * Switches the state between opened/closed
	 */
	public void toggleState()
	{
		setState(!opened);
	}

	/**
	 * @return true if the part is opened
	 */
	public boolean getState()
	{
		return opened;
	}

	/**
	 * Reads the state from NBT
	 *
	 * @param nbt the NBT to read from
	 */
	public void readFromNBT(NBTTagCompound nbt)
	{
		opened = nbt.getBoolean("opened");
		progress = nbt.getFloat("progress");
	}

	/**
	 * Writes the state to NBT
	 *
	 * @return the NBT to write to
	 */
	public NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("opened", opened);
		nbt.setFloat("progress", progress);
		return nbt;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return writeToNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		readFromNBT(nbt);
	}
}
