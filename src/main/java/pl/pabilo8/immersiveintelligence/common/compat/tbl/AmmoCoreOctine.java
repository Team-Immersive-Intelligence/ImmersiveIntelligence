package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreOctine extends AmmoCore
/**
 * @author Carver (carver@iiteam.net)
 * @updated 14.04.2026
 */

//TODO: Make this material have glow and set impact target on fire.


{
	public AmmoCoreOctine()
	{
		super("core_octine", 0.5f, PenetrationHardness.IRON, 0.90f, 0.65f, IIColor.fromPackedRGB(0xd73900));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetOctine");
	}
}
