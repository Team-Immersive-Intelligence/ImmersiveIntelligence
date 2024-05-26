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
public class AmmoCoreTungsten extends AmmoCore
{
	public AmmoCoreTungsten()
	{
		super("core_tungsten", 0.35f, PenetrationHardness.TUNGSTEN, 0.25f, 1.45f, 0x2e3035);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetTungsten");
	}
}
