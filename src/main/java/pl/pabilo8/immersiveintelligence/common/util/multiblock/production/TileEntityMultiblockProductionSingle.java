package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;

import javax.annotation.Nullable;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionSingle<T extends TileEntityMultiblockProductionSingle<T, R>, R extends IMultiblockRecipe>
		extends TileEntityMultiblockProductionBase<T, R>
		implements IGuiTile
{
	/**
	 * The current process
	 */
	@Nullable
	public IIMultiblockProcess<R> currentProcess;

	public TileEntityMultiblockProductionSingle(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	//--- Update Method ---//

	@Override
	protected void onUpdate()
	{
		IIMultiblockProcess<R> existingProcess = currentProcess;

		if(currentProcess!=null)
		{
			//Do process output
			if(currentProcess.ticks >= currentProcess.maxTicks)
			{
				if(attemptProductionOutput(currentProcess))
				{
					onProductionFinish(currentProcess);
					currentProcess = null;
				}
				else
					return;
			}
			else
			{
				float progress = getProductionStep(currentProcess, false);
				if(progress > 0)
					currentProcess.ticks += progress;
				return;
			}
		}

		//Add new process to the queue (no matter whether it's null)
		this.currentProcess = findNewProductionProcess();

		//Send block update on changes
		if(this.currentProcess!=existingProcess)
			forceTileUpdate();
	}

	//--- Production Abstracts Override ---//

	/**
	 * @return minimal offset between production processes
	 */
	public int getMinProductionOffset()
	{
		return 1;
	}

	/**
	 * @return max parallel processes in machine
	 */
	public int getMaxProductionQueue()
	{
		return 0;
	}

	/**
	 * @return the process's progress in upcoming partialTicks, divided by max and clamped to 0-1
	 */
	@Override
	public float getProductionProgress(IIMultiblockProcess<R> process, float partialTicks)
	{
		if(process==currentProcess&&process!=null&&process.maxTicks!=0)
			return (process.ticks+(getProductionStep(process, true)*partialTicks))/process.maxTicks;
		return 0;
	}
}
