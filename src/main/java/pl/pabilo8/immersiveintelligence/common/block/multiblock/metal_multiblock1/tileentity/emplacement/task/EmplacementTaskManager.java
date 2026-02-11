package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;

import javax.annotation.Nullable;
import java.util.ArrayList;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 14.09.2025
 */
public class EmplacementTaskManager implements INBTSerializable<NBTTagList>
{
	public ArrayList<EmplacementFireMission> tasks = new ArrayList<>();
	public boolean paused = false;

	static
	{
		NBTSerialisation.registerPolimorphicTypeClass(EmplacementFireMission.class);
	}

	public void addTask(EmplacementFireMission task)
	{
		this.tasks.add(task);
		paused = false;
	}

	public void skipTask(int taskID)
	{
		tasks.remove(taskID);
		paused = false;
	}

	@Nullable
	public EmplacementFireMission getCurrentTask()
	{
		return tasks.get(0);
	}

	public void stopTask(boolean switchToDefault)
	{
		paused = true;
	}

	public void resumeTask(boolean switchToDefault)
	{
		paused = false;
	}

	@Override
	public NBTTagList serializeNBT()
	{
		NBTTagList nbt = new NBTTagList();
		for(EmplacementFireMission task : tasks)
		{
			NBTTagCompound tag = new NBTTagCompound();
			//noinspection unchecked
			NBTSerialisation.synchroniseFor(task, (nbtSerializer, emplacementFireMission) -> nbtSerializer.serializeAll(emplacementFireMission, tag));
			nbt.appendTag(tag);
		}
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagList nbt)
	{

	}
}
