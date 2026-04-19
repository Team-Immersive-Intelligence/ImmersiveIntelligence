package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreDispatcherShell extends AmmoCore

{
	public AmmoCoreDispatcherShell()
	{
		super("core_dispatcher", 0.55f, PenetrationHardness.GROUND, 1.50f, 0.45f, IIColor.fromPackedRGB(0x752d33));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dispatchershell");
	}
}
