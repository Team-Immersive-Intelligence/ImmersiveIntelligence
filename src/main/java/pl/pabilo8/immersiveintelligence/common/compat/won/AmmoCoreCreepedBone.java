package pl.pabilo8.immersiveintelligence.common.compat.won;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreCreepedBone extends AmmoCore

/**
 * @author Carver (carver@iiteam.net)
 * @since 13.04.2026
 */
{
	public AmmoCoreCreepedBone()
	{
		super("core_creepedbone", 1.2f, PenetrationHardness.ROCK, 0.050f, 0.50f, IIColor.fromPackedRGB(0xd7c192));
	}

	@Override

	public IngredientStack getMaterial()
	{
		return new IngredientStack("creepedbone");
	}
}
