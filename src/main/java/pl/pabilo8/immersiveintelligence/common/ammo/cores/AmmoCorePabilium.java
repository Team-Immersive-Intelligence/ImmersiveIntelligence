package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoCorePabilium extends AmmoCore
{
	public AmmoCorePabilium()
	{
		super("core_pabilium", 1.5f, 8, 1f, 1.1f, 0x3a3e44);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetPabilium");
	}
}
