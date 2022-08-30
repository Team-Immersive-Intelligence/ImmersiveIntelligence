package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoCoreTungsten implements IAmmoCore
{
	@Override
	public String getName()
	{
		return "core_tungsten";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("nuggetTungsten");
	}

	@Override
	public float getDensity()
	{
		return 0.35f;
	}

	@Override
	public float getDamageModifier()
	{
		return 1.45f;
	}

	@Override
	public float getExplosionModifier()
	{
		return 0.25f;
	}

	@Override
	public float getPenetrationHardness()
	{
		return 16;
	}

	@Override
	public int getColour()
	{
		return 0x2e3035;
	}
}
