package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreBolsterShell extends AmmoCore
{
	public AmmoCoreBolsterShell()
	{
		super("core_bolster", 0.55f, PenetrationHardness.GROUND, 1.50f, 0.45f, IIColor.fromPackedRGB(0x33353a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("bolstershell");
	}
}
