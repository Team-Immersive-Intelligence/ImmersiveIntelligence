package pl.pabilo8.immersiveintelligence.common.compat.ee;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreCordium extends AmmoCore
{
	public AmmoCoreCordium()
	{
		super("core_cordium", 0.7f, PenetrationHardness.STEEL, 0.70f, 1.05f, IIColor.fromPackedRGB(0x98680f));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("ingotCordium");
	}
}
