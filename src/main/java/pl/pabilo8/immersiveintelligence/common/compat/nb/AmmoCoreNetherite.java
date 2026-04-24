package pl.pabilo8.immersiveintelligence.common.compat.nb;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @since 11.04.2026
 */
public class AmmoCoreNetherite extends AmmoCore
{
	public AmmoCoreNetherite()
	{
		super("core_netherite", 0.7f, PenetrationHardness.UBERCONCRETE, 0.45f, 1.50f, IIColor.fromPackedRGB(0x31292a));
	}

	@Override

	public IngredientStack getMaterial()
	{
		return new IngredientStack("ingotNetherite");
	}
}
