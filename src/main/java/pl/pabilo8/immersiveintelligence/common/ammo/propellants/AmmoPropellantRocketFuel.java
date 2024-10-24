package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantRocketFuel extends AmmoPropellant
{
	public AmmoPropellantRocketFuel()
	{
		super("rocket_fuel", 1f, IIColor.fromPackedRGB(0x9b9b9b), PropellantType.FLUID, 1f, 0.03f, true, true);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustRDX");
	}
}
