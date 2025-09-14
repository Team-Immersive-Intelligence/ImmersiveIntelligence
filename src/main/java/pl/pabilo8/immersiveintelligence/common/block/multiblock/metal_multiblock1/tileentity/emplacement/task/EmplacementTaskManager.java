package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class EmplacementTaskManager implements INBTSerializable<NBTTagCompound>
{
	public EmplacementTask[] tasks = new EmplacementTask[]{
			new EmplacementTaskCustom(createDefaultTask()),
			new EmplacementTaskCustom(createDefaultTask()),
			new EmplacementTaskCustom(createDefaultTask()),
			new EmplacementTaskCustom(createDefaultTask())
	};
	public EmplacementTask temporaryTask = null;
	public int currentTask = 0, defaultTask = 0;
	public boolean paused = false;

	public void setCurrentTask(EmplacementTask task)
	{
		temporaryTask = task;
		currentTask = 4;
		paused = false;
	}

	public void setCurrentTask(int taskID)
	{
		currentTask = MathHelper.clamp(taskID, 0, tasks.length-1);
		paused = false;
	}

	public EmplacementTask getCurrentTask()
	{
		return tasks[defaultTask];
	}

	public void stopTask(boolean switchToDefault)
	{
		paused = true;
		if(switchToDefault)
			currentTask = defaultTask;
	}

	public void resumeTask(boolean switchToDefault)
	{
		paused = false;
		if(switchToDefault)
			currentTask = defaultTask;
	}

	private NBTTagCompound createDefaultTask()
	{
		NBTTagCompound compound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		NBTTagCompound taskCompound = new NBTTagCompound();

		taskCompound.setString("type", "mobs");
		taskCompound.setBoolean("negation", false);

		list.appendTag(taskCompound);
		compound.setTag("filters", list);
		return compound;
	}

	//TODO: 14.09.2025 implement

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
