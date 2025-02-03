package pl.pabilo8.immersiveintelligence.common.util.multiblock.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Represents an animated part of the multiblock, like a drawer
 */
public class MultiblockInteractablePart implements INBTSerializable<NBTTagCompound>
{
	/**
	 * The ID of the part, used for syncing
	 */
	final int id;
	/**
	 * The maximum progress of the part
	 */
	final float maxProgress;
	/**
	 * The speed at which the part closes, opening is always 1
	 */
	final float closingSpeed;
	/**
	 * Whether the part is opened
	 */
	boolean opened = false;
	/**
	 * The current progress of the part
	 */
	float progress = 0;

	public MultiblockInteractablePart(float maxProgress)
	{
		this.maxProgress = maxProgress;
		this.id = -1;
		this.closingSpeed = 1;
	}

	public MultiblockInteractablePart(int id, float maxProgress, float closingSpeed)
	{
		this.id = id;
		this.maxProgress = maxProgress;
		this.closingSpeed = closingSpeed;
	}

	public float getProgress(float partialTicks)
	{
		return MathHelper.clamp(progress+(opened?partialTicks: -partialTicks*closingSpeed), 0, maxProgress)/maxProgress;
	}

	public void update()
	{
		this.progress = MathHelper.clamp(progress+(opened?1: -closingSpeed), 0, maxProgress);
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

	/**
	 * @param state true if opened
	 * @param part  the part ID
	 * @param parts the parts to check
	 * @return the part that changed state, or null if none did
	 */
	public static MultiblockInteractablePart setStates(boolean state, int part, MultiblockInteractablePart... parts)
	{
		for(MultiblockInteractablePart p : parts)
			if(p.id==part)
				return p.setState(state)?p: null;
		return null;
	}

	public int getID()
	{
		return id;
	}
}
