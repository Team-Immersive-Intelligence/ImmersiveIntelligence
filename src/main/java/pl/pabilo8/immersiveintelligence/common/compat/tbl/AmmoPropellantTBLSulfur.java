package pl.pabilo8.immersiveintelligence.common.compat.tbl;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

public class AmmoPropellantTBLSulfur extends AmmoPropellant
/**
 * @author Carver (carver@iiteam.net)
 * @since 15.04.2026
 */
//quasi-lore: lore-wise that it's mistakenly called a "sulfur" while actually being a compound that works as, essentially, a worse gunpowder, without the need to process it.

{
	public AmmoPropellantTBLSulfur()
	{
		super("Thebetweenlands sulfur propellant", 1.4f, IIColor.fromPackedRGB(0xe0e018), PropellantType.SOLID, 0.60f, 0.10f, true, false);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack("tsulfur");
	}
}
