package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @since 11.04.2026
 */
public class AmmoCoreThaumium extends AmmoCore
{
	public AmmoCoreThaumium()
	{
		super("core_thaumium", 0.5f, PenetrationHardness.IRON, 0.70f, 1.10f, IIColor.fromPackedRGB(0x423250));
	}

	@Override

	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetThaumium");
	}
}
