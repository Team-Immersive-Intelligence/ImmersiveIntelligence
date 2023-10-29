package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import java.util.ArrayList;
import java.util.List;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionMulti<T extends TileEntityMultiblockProductionMulti<T, R>, R extends IMultiblockRecipe>
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
		int processes = processQueue.size();
		if(processes > 0)
		{
			for(IIMultiblockProcess<R> process : processQueue)
			{
				//Do process output
				if(process.ticks >= process.maxTicks)
				{
					if(attemptProductionOutput(process))
						onProductionFinish(process);
					break;
				}

				//Else, try to progress this process
				float progress = getProductionStep(process, false);
				if(progress > 0)
					process.ticks += progress;
			}
		}

		//Add new process to the queue
		int maxProcesses = getMaxProductionQueue();
		if(processes <= maxProcesses)
		{
			boolean canAdd = true;
			if(maxProcesses > 1&&processes > 0)
			{
				IIMultiblockProcess<R> lastProcess = processQueue.get(processQueue.size()-1);
				canAdd = lastProcess.ticks/lastProcess.maxTicks >= getMinProductionOffset();
			}

			//Whether it's actually added, depends on the implementation
			if(canAdd)
				findNewProductionProcess();
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
