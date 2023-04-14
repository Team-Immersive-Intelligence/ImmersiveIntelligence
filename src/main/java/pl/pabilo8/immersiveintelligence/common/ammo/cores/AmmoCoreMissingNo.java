package pl.pabilo8.immersiveintelligence.common.ammo.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmoCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class AmmoCoreMissingNo implements IAmmoCore
{
	@Override
	public String getName()
	{
		return "";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("");
	}

	@Override
	public float getDensity()
	{
		return 0;
	}

	@Override
	public float getDamageModifier()
	{
		return 0;
	}

	@Override
	public float getExplosionModifier()
	{
		return 0;
	}

	@Override
	public float getPenetrationHardness()
	{
		return -1;
	}

	@Override
	public int getColour()
	{
		return 0xff00ff;
	}
}
