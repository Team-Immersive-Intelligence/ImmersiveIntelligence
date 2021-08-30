package pl.pabilo8.immersiveintelligence.common.ammunition_system.cores;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumComponentRole;
import pl.pabilo8.immersiveintelligence.api.bullets.IBulletCore;

/**
 * @author Pabilo8
 * @since 30-08-2019
 */
public class BulletCoreLead implements IBulletCore
{
	@Override
	public String getName()
	{
		return "core_lead";
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("plateLead");
	}

	@Override
	public float getDensity()
	{
		return 1.5f;
	}

	@Override
	public float getDamageModifier()
	{
		return 1.1f;
	}

	@Override
	public float getExplosionModifier()
	{
		return 1f;
	}

	@Override
	public float getPenetrationHardness()
	{
		return 8;
	}

	@Override
	public int getColour()
	{
		return 0x3a3e44;
	}
}
