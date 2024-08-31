package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @updated 06.03.2024
 * @ii-approved 0.3.1
 * @since 30-08-2019
 */
public class AmmoCoreMissingNo extends AmmoCore
{
	public AmmoCoreMissingNo()
	{
		super("", 0, PenetrationHardness.FOLIAGE, 0, 0, IIColor.fromPackedRGB(0xff00ff));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("");
	}

	@Override
	public boolean showInManual()
	{
		return false;
	}
}
