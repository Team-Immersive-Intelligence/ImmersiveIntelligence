package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreValonite extends AmmoCore
/**
 * @author Carver (carver@iiteam.net)
 * @updated 14.04.2026
 */

{
	public AmmoCoreValonite()
	{
		super("core_valonite", 0.4f, PenetrationHardness.STEEL, 0.80f, 1.10f, IIColor.fromPackedRGB(0xa98da9));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetValonite");
	}
}
