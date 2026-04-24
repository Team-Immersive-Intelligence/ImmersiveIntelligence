package pl.pabilo8.immersiveintelligence.common.compat.thaum;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.PropellantType;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPropellant;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Carver (carver@iiteam.net)
 * @since 11.04.2026
 */
public class AmmoPropellantAlumentum extends AmmoPropellant
{
	private final ItemStack alumentum;

	public AmmoPropellantAlumentum()
	{
		super("propellant_alumentum", 0.70f, IIColor.fromPackedRGB(0xe2ed68), PropellantType.SOLID, 0.9f, 0.20f, true, true);
		//noinspection DataFlowIssue
		this.alumentum = new ItemStack(Item.REGISTRY.getObject(ThaumcraftHelper.RES_TC.with("alumentum")), 1, 0);
	}

	@Override
	public IngredientStack getMaterial()
	{
		return new IngredientStack(alumentum);
	}
}
