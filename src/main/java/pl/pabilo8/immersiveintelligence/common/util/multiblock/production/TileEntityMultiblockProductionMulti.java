package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import java.util.Iterator;


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
	@SyncNBT(events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public ProcessQueue<T, R> processQueue = new ProcessQueue<>(this);

	public TileEntityMultiblockProductionMulti(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.processQueue = null;
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
				{
					processQueue.add(process);
					updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
				}
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
