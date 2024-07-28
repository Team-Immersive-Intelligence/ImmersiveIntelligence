package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantRocketFuelExperimental extends AmmoPropellant
{
	public AmmoPropellantRocketFuelExperimental()
	{
		super("experimental_rocket_fuel", 0.93f, IIColor.fromPackedRGB(0x998c66), PropellantType.FLUID, 1.35f, 0.15f, true, true);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustRDX");
	}
}
