package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantRocketFuelStable extends AmmoPropellant
{
	public AmmoPropellantRocketFuelStable()
	{
		super("stable_rocket_fuel", 0.93f, IIColor.fromPackedRGB(0x687d99), PropellantType.FLUID, 0.9f, 0f, true, true);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustRDX");
	}
}
