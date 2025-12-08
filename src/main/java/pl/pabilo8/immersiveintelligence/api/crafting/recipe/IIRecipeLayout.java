package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import java.util.List;

/**
 * Recipe layout with components
 */
public class IIRecipeLayout
{
	private final List<LayoutComponent> components, bottomBarComponents;
	private final int gridWidth;
	private final int gridHeight;
	private final boolean earlyGame;

	public IIRecipeLayout(List<LayoutComponent> components, List<LayoutComponent> bottomBarComponents, int gridWidth, int gridHeight, boolean earlyGame)
	{
		this.components = components;
		this.bottomBarComponents = bottomBarComponents;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.earlyGame = earlyGame;
	}

	public List<LayoutComponent> getComponents()
	{
		return components;
	}

	public List<LayoutComponent> getBottomBarComponents()
	{
		return bottomBarComponents;
	}

	public int getGridWidth()
	{
		return gridWidth;
	}

	public int getGridHeight()
	{
		return gridHeight;
	}

	public boolean isEarlyGame()
	{
		return earlyGame;
	}

	public enum ComponentType
	{
		SLOT,
		FLUID_TANK,
		DUST_TANK,
		INFO_DISPLAY,
		MULTIBLOCK_MODEL
	}

	public enum IOType
	{
		INPUT,
		OUTPUT,
		NEUTRAL
	}
}
