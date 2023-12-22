package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionMulti<T extends TileEntityMultiblockProductionMulti<T, R>, R extends IIIMultiblockRecipe>
		extends TileEntityMultiblockProductionBase<T, R>
		implements IGuiTile
{
	/**
	 * List of all current processes
	 */
	public List<IIMultiblockProcess<R>> processQueue = new ArrayList<>();

	public TileEntityMultiblockProductionMulti(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	//--- Update Method ---//

	@Override
	protected void onUpdate()
	{
		//Iterate existing processes and try to progress them
		if(!processQueue.isEmpty())
		{
			Iterator<IIMultiblockProcess<R>> iterator = processQueue.iterator();
			while(iterator.hasNext())
			{
				IIMultiblockProcess<R> process = iterator.next();

				//Do process output
				if(process.ticks >= process.maxTicks)
				{
					if(attemptProductionOutput(process))
					{
						//Remove the process from the queue
						onProductionFinish(process);
						iterator.remove();
					}
					break;
				}

				//Else, try to progress this process
				float progress = getProductionStep(process, false);
				if(progress > 0)
					process.ticks += progress;
			}
		}

		//Add new process to the queue
		int processes = processQueue.size();
		int maxProcesses = getMaxProductionQueue();
		if(processes <= maxProcesses)
		{
			boolean canAdd = true;
			if(maxProcesses > 1&&processes > 0)
			{
				IIMultiblockProcess<R> lastProcess = processQueue.get(processQueue.size()-1);
				canAdd = lastProcess.ticks/lastProcess.maxTicks >= getMinProductionOffset();
			}

			if(canAdd)
			{
				IIMultiblockProcess<R> process = findNewProductionProcess();
				if(process!=null)
					processQueue.add(process);
			}
		}
	}

	@Override
	public float getProductionProgress(IIMultiblockProcess<R> process, float partialTicks)
	{
		if(process!=null&&this.processQueue.contains(process)&&process.maxTicks!=0)
			return (process.ticks+getProductionStep(process, true)*partialTicks)/process.maxTicks;
		return 0;
	}
}
