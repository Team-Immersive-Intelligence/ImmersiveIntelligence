package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Groups matching recipe examples without registering production recipes.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 06.10.2026
 * @since 05.10.2026
 */
public final class IIRecipeDisplay extends IIMultiblockRecipe
{
	private final List<IIRecipeLayout> layouts;

	public IIRecipeDisplay(String name, int time, int energy, IIRecipeLayout layout)
	{
		this(name, time, energy, Collections.singletonList(layout));
	}

	/**
	 * Variants must have the same component positions and ingredient slot order.
	 */
	public IIRecipeDisplay(String name, int time, int energy, List<IIRecipeLayout> layouts)
	{
		super(name, new Object[0], false);
		if(layouts.isEmpty())
			throw new IllegalArgumentException("A recipe display needs at least one layout");
		this.layouts = Collections.unmodifiableList(new ArrayList<>(layouts));
		setTimeAndEnergy(time, energy);
	}

	public List<IIRecipeLayout> getLayouts()
	{
		return layouts;
	}

	@Override
	protected IIRecipeLayout initRecipeLayout()
	{
		return layouts.get(0);
	}
}
