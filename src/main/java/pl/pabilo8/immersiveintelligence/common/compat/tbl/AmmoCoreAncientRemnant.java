package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreAncientRemnant extends AmmoCore
/**
 * @author Carver (carver@iiteam.net)
 * @updated 15.04.2026
 */

{
	public AmmoCoreAncientRemnant()
	{
		super("core_ancient_remnant", 0.3f, PenetrationHardness.TUNGSTEN, 0.50f, 1.10f, IIColor.fromPackedRGB(0xafe5e1));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("ancient_remnant");
	}
}

