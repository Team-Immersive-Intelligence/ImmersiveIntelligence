package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoCoreCopper extends AmmoCore
{
	public AmmoCoreCopper()
	{
		super("core_copper", 0.55f, PenetrationHardness.FRAGILE, 1f, 0.35f, IIColor.fromPackedRGB(0xb47a4c));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetCopper");
	}
}