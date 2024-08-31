package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import net.minecraft.nbt.NBTTagCompound;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase.IIIMultiblockRecipe;

/**
 * Utility class combining II's {@link IIMultiblockRecipe} recipes with IE's {@link MultiblockRecipe} recipes
 */
public abstract class IIMultiblockRecipe extends MultiblockRecipe implements IIIMultiblockRecipe
{
	protected int totalProcessTime;
	protected int totalProcessEnergy;

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		return writeToNBT().unwrap();
	}

	public int getTotalProcessTime()
	{
		return this.totalProcessTime;
	}

	public int getTotalProcessEnergy()
	{
		return this.totalProcessEnergy;
	}
}
