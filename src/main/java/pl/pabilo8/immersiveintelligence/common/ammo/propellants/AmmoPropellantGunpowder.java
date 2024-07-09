package pl.pabilo8.immersiveintelligence.common.ammo.propellants;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @since 28.05.2024
 */
public class AmmoPropellantGunpowder extends AmmoPropellant
{
	public AmmoPropellantGunpowder()
	{
		super("gunpowder", 1.25f, IIColor.fromPackedRGB(0x1d1d1d), PropellantType.SOLID, 0.75f, 0.02f, true, false);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("gunpowder");
	}
}
