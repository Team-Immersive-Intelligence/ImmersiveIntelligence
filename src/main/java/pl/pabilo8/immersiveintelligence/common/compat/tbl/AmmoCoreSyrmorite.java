package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @updated 14.04.2026
 */

public class AmmoCoreSyrmorite extends AmmoCore
{
	public AmmoCoreSyrmorite()
	{
		super("core_syrmorite", 0.5f, PenetrationHardness.IRON, 1f, 0.45f, IIColor.fromPackedRGB(0x4a59a6));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetSyrmorite");
	}
}
