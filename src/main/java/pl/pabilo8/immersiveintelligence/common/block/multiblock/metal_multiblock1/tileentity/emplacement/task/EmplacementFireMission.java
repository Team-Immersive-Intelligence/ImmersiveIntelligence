package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.task;

import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity.emplacement.TileEntityEmplacement;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 15.02.2024
 */
public abstract class EmplacementFireMission implements ITypeNBTSerializable
{
	/**
	 * @return whether the task should continue execution, should be always true if it is a permanent detection task like {@link EmplacementFireMissionEntities}
	 */
	public abstract boolean shouldContinue();

	/**
	 * @return target name/id for loading from nbt and init
	 */
	public abstract String getName();

	/**
	 * Reduces lag.
	 * Used to update visible targets list on target seeking tasks, like {@link EmplacementFireMissionEntities}
	 * Leave empty if it is a single target and you're not doing any detection
	 *
	 * @param emplacement to which the task belongs
	 */
	public abstract void updateTargets(TileEntityEmplacement emplacement);

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{

	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		return new NBTTagCompound();
	}
}
