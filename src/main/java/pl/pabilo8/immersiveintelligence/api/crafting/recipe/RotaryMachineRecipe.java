package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;

import javax.annotation.Nullable;

public interface RotaryMachineRecipe extends IMultiblockRecipe
{
	int getTorque();

	int getMinSpeed();

	int getMaxSpeed();

	@Nullable
	default Integer getTotalProcessTorque()
	{
		return getTorque()*getTotalProcessTime();
	}
}
