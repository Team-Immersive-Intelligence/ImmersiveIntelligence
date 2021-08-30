package pl.pabilo8.immersiveintelligence.common.ammunition_system.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletCoreSteel implements IBulletCore
{
	@Override
	public String getName()
	{
		return "core_steel";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("plateSteel");
	}

	@Override
	public float getDensity()
	{
		return 0.5f;
	}

	@Override
	public float getDamageModifier()
	{
		return 1.25f;
	}

	@Override
	public float getExplosionModifier()
	{
		return 0.65f;
	}

	@Override
	public float getPenetrationHardness()
	{
		return 12;
	}

	@Override
	public int getColour()
	{
		return 0x64676c;
	}
}
