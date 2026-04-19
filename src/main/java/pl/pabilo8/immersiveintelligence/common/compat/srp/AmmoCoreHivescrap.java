package pl.pabilo8.immersiveintelligence.common.compat.srp;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PenetrationHardness;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoCore;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoCoreHivescrap extends AmmoCore

{
	public AmmoCoreHivescrap()
	{
		super("core_hivescrap", 0.32f, PenetrationHardness.UBERCONCRETE, 1.20f, 2.0f, IIColor.fromPackedRGB(0x3d2323));
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("hivescrap");
	}
	//lategame resource. Obtained from meteors or Preeminent parasites or from biome. Notably hard and dangerous to obtain as it is either random RNG or having to fight Preeminents.
}
