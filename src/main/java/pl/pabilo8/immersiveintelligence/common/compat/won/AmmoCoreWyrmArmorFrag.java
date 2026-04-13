package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreWyrmArmorFrag extends AmmoCore
/**
 * @author Carver (carver@iiteam.net)
 * @since 13.04.2026
 */
{
	public AmmoCoreWyrmArmorFrag()
	{
		super("core_wyrmshell", 0.35f, PenetrationHardness.STEEL, 0.65f, 1.625f, IIColor.fromPackedRGB(0xe77e28));
	}

	@Override

	public IngredientStack getMaterial()
	{
		return new IngredientStack("wyrmarmorfrag");
	}
}
