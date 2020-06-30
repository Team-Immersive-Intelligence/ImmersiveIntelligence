package pl.pabilo8.immersiveintelligence.api.utils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Pabilo8
 * @since 26-10-2019
 */
public interface IAdvancedMultiblockTileEntity
{
	int getConstructionCost();

	int getCurrentConstruction();

	void setCurrentConstruction(int construction);

	void onConstructionFinish();

	default boolean isConstructionFinished()
	{
		return getCurrentConstruction() >= getConstructionCost();
	}

	default void setConstructionNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("construction", getCurrentConstruction());
	}

	default void getConstructionNBT(NBTTagCompound nbt)
	{
		setCurrentConstruction(nbt.getInteger("construction"));
	}
}
