package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoCoreBrass extends AmmoCore
{
	public AmmoCoreBrass()
	{
		super("core_brass", 0.75f, PenetrationHardness.WOOD, 1.25f, 0.65f, 0xdaa84a);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetBrass");
	}
}