package pl.pabilo8.immersiveintelligence.common.compat.thaumaugment;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @since 12.04.2026
 */

public class AmmoPropellantImpetus extends AmmoPropellant
{
	public AmmoPropellantImpetus()
{
	super("impetus propellant", 0.01f, IIColor.fromPackedRGB(0x000b10), PropellantType.SOLID, 3.0f, 0.05f, true, true);
}

	@Override
	public IngredientStack getMaterial()
{
	return new IngredientStack("impetus");
}
}
