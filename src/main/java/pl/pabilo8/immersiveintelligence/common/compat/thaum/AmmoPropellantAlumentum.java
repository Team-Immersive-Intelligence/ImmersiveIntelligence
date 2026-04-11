package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;


public class AmmoPropellantAlumentum extends AmmoPropellant
{
	public AmmoPropellantAlumentum()
	{
		super("alumentum propellant", 0.70f, IIColor.fromPackedRGB(0xe2ed68), PropellantType.SOLID, 0.9f, 0.20f, true, true);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("alumentum");
	}
}
