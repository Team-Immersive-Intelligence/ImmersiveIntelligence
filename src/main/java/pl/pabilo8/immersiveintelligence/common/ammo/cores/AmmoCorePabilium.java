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
public class AmmoCorePabilium extends AmmoCore
{
	public AmmoCorePabilium()
	{
		super("core_pabilium", 1.5f, PenetrationHardness.PABILIUM, 3f, 8.0f, IIColor.fromPackedRGB(0x2E4242));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetPabilium");
	}

	@Override
	public boolean showInManual()
	{
		return false;
	}
}
