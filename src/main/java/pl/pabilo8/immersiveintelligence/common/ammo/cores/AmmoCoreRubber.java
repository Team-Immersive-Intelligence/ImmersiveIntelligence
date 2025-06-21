package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30.08.2019
 */
public class AmmoCoreRubber extends AmmoCore
{
	public AmmoCoreRubber()
	{
		super("core_rubber", 0.25f, PenetrationHardness.FRAGILE, 0.125f, 0.125f, IIColor.fromPackedRGB(0x242424));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetRubber");
	}
}