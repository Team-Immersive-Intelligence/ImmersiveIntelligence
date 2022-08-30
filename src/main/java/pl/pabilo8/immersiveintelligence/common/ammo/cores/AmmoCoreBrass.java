package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoCoreBrass implements IAmmoCore
{
	@Override
	public String getName()
	{
		return "core_brass";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetBrass");
	}

	@Override
	public float getDensity()
	{
		return 0.75f;
	}

	@Override
	public float getDamageModifier()
	{
		return 0.65f;
	}

	@Override
	public float getExplosionModifier()
	{
		return 1.25f;
	}

	@Override
	public float getPenetrationHardness()
	{
		return 4;
	}

	@Override
	public int getColour()
	{
		return 0xdaa84a;
	}
}
