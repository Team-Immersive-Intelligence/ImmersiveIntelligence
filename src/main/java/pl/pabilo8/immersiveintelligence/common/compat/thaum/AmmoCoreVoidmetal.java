package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreVoidmetal extends AmmoCore

/**
 * @author Carver (carver@iiteam.net)
 * @since 11.04.2026
 */
{
	public AmmoCoreVoidmetal()
	{
		super("core_voidmetal", 0.20f, PenetrationHardness.TUNGSTEN, 0.50f, 2.00f, IIColor.fromPackedRGB(0x10081a));
	}

	@Override

	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetVoid");
	}
}
