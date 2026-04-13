package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreHivesteel extends AmmoCore
{
	public AmmoCoreHivesteel()
	{
		super("core_hivesteel", 0.4f, PenetrationHardness.UBERCONCRETE, 0.90f, 1.80f, IIColor.fromPackedRGB(0xc16d28));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("ingotHivesteel");
	}
}
