package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 13.04.2026
 */

//Values subject to change, as properties of this material are not fully explored.

public class AmmoCoreBorgiplate extends AmmoCore
{
	public AmmoCoreBorgiplate()
	{
		super("core_borgiplate", 0.4f, PenetrationHardness.STEEL, 0.80f, 1.10f, IIColor.fromPackedRGB(0x1f0f4e));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("borgiplate");
	}
}
