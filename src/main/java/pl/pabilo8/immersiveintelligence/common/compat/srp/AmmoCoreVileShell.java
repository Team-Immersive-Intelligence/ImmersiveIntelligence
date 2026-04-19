package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreVileShell extends AmmoCore
{
	public AmmoCoreVileShell()
	{
		super("core_vileshell", 0.2f, PenetrationHardness.TUNGSTEN, 0.50f, 1.5f, IIColor.fromPackedRGB(0x6f5549));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("vileshell");
	}
}
