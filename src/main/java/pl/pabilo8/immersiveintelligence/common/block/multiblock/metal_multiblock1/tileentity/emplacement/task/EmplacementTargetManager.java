package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class EmplacementTargetManager implements INBTSerializable<NBTTagCompound>
{
	public boolean paused = false;

	public void skipTask(int taskID)
	{
		paused = false;
	}

	public void stopTask(boolean switchToDefault)
	{
		paused = true;
	}

	public void resumeTask(boolean switchToDefault)
	{
		paused = false;
	}

	public EmplacementTarget provideNextTask()
	{
		return null;
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
