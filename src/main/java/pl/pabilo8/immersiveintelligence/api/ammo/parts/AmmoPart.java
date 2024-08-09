package pl.pabilo8.immersiveintelligence.api.ammo.parts;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

/**
 * @author Pabilo8
 * @ii-approved 0.3.1
 * @since 27.05.2024
 */
public abstract class AmmoPart
{
	protected final String name;
	protected final float density;
	protected final IIColor color;

	public AmmoPart(String name, float density, IIColor color)
	{
		this.name = name;
		this.density = density;
		this.color = color;
	}

	/**
	 * @return the name of the ammo ingredient
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the density of ammo ingredient (density * amount = mass)
	 */
	public float getDensity()
	{
		return density;
	}

	/**
	 * @return the color of the ammo ingredient
	 */
	public IIColor getColor()
	{
		return color;
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
