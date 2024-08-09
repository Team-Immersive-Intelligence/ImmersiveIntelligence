package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantRDX extends AmmoPropellant
{
	public AmmoPropellantRDX()
	{
		super("rdx", 1.1f, IIColor.fromPackedRGB(0xd2c294), PropellantType.SOLID, 1.2f, 0.02f, true, false);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustRDX");
	}
}
