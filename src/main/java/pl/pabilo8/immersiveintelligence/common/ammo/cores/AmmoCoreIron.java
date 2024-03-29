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
public class AmmoCoreIron extends AmmoCore
{
	public AmmoCoreIron()
	{
		super("core_iron", 0.5f, PenetrationHardness.IRON, 1f, 0.45f, 0x9d9fa4);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetIron");
	}
}