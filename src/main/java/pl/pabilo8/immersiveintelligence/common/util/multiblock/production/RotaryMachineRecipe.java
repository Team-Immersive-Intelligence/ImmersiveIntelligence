package pl.pabilo8.immersiveintelligence.common.util.multiblock.production;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;

public interface RotaryMachineRecipe extends IMultiblockRecipe
{
	int getTorque();

	default int getTotalProcessTorque()
	{
		return getTorque()*getTotalProcessTime();
	}
}
