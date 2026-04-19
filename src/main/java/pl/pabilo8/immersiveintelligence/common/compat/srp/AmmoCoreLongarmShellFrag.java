package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreLongarmShellFrag extends AmmoCore

{
	public AmmoCoreLongarmShellFrag()
	{
		super("core_longarmsfrag", 0.1f, PenetrationHardness.STEEL, 0.25f, 1f, IIColor.fromPackedRGB(0x33353a));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("longarmshell");
	}
}
