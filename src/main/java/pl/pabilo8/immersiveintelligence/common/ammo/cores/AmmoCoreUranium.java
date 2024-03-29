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
public class AmmoCoreUranium extends AmmoCore
{
	public AmmoCoreUranium()
	{
		super("core_uranium", 0.45f, PenetrationHardness.UBERCONCRETE, 0.75f, 1.35f, 0x659269);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetUranium");
	}
}
