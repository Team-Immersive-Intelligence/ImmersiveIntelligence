package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantCordite extends AmmoPropellant
{
	public AmmoPropellantCordite()
	{
		super("cordite", 1f, IIColor.fromPackedRGB(0x9b9b9b), PropellantType.SOLID, 1f, 0f, true, false);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("dustCordite");
	}
}
