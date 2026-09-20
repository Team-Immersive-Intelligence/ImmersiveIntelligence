package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 27.05.2024
 */
@Getter
@ToString
@EqualsAndHashCode
public abstract class AmmoPart
{
	/**
	 * The name of the ammo ingredient
	 */
	protected final String name;
	/**
	 * The density of ammo ingredient (density * amount = mass)
	 */
	protected final float density;
	/**
	 * The color of the ammo ingredient
	 */
	protected final IIColor color;

	public AmmoPart(String name, float density, IIColor color)
	{
		this.name = name;
		this.density = density;
		this.color = color;
	}

	/**
	 * @return if the ammo ingredient should glow in the dark
	 */
	public boolean isGlowing()
	{
		return false;
	}

	/**
	 * @return the ingredient used to create this ammo core
	 */
	public abstract IngredientStack getMaterial();

	/**
	 * @return if this component should be shown in the manual
	 */
	public boolean showInManual()
	{
		return true;
	}
}
