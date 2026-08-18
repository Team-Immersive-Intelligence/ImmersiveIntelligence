package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNullableSyncMechanism;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.MultiblockStuctureBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

import javax.annotation.Nullable;


/**
 * A standard II production multiblock.<br>
 * Counterpart to {@link TileEntityMultiblockMetal}<br>
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.04.2023
 */

public abstract class TileEntityMultiblockProductionSingle<T extends TileEntityMultiblockProductionSingle<T, R>, R extends IIIMultiblockRecipe>
		extends TileEntityMultiblockProductionBase<T, R>
{
	/**
	 * The current process
	 */
	@Nullable
	public IIMultiblockProcess<R> currentProcess;
	@SyncNBT(name = "current_process", events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public EasyNullableSyncMechanism<IIMultiblockProcess<R>, NBTTagCompound> currentProcessSync = new EasyNullableSyncMechanism<>(
			() -> currentProcess, p -> this.currentProcess = p, nbt ->
			getProcessByName(nbt.getString("recipe")).withNBT(easyNBT -> easyNBT.mergeWith(nbt))
	);

	public TileEntityMultiblockProductionSingle(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		this.currentProcess = null;
		this.currentProcessSync = null;
	}

	//--- Update Method ---//

	@Override
	protected void onUpdate()
	{
		IIMultiblockProcess<R> existingProcess = currentProcess;
		boolean updateProcess = false;
		if(currentProcess!=null)
		{
			//Do process output
			if(currentProcess.ticks >= currentProcess.maxTicks)
			{
				if(attemptProductionOutput(currentProcess))
				{
					onProductionFinish(currentProcess);
					currentProcess = existingProcess = null;
					updateProcess = true;
				}
			}
			else
			{
				float progress = getProductionStep(currentProcess, false);
				if(progress > 0)
				{
					currentProcess.ticks += progress;
					if(Machines.recipeUpdateInterval > 0&&currentProcess.ticks%Machines.recipeUpdateInterval==0)
						updateProcess = true;
				}
			}
		}

		if(world.isRemote)
			return;
		//Add new process to the queue (no matter whether it's null)
		if(this.currentProcess==null)
		{
			this.currentProcess = findNewProductionProcess();
			if(this.currentProcess!=existingProcess)
				updateProcess = true;
		}

		//Send nbt update on changes
		if(updateProcess)
			updateTileForEvent(SyncEvents.TILE_RECIPE_CHANGED);
	}

	//--- Production Abstracts Override ---//

	/**
	 * @return minimal offset between production processes
	 */
	public float getMinProductionOffset()
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
