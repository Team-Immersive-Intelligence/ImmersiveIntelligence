package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantHMX extends AmmoPropellant
{
	public AmmoPropellantHMX()
	{
		super("rdx", 1.3f, IIColor.fromPackedRGB(0xfbfbfb), PropellantType.SOLID, 1.5f, 0.05f, true, false);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustHMX");
	}
}
