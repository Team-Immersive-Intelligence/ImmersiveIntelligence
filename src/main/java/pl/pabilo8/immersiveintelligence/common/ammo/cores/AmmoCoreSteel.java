package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoCoreSteel extends AmmoCore
{
	public AmmoCoreSteel()
	{
		super("core_steel", 0.5f, 12, 0.65f, 1.25f, 0x64676c);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetSteel");
	}
}