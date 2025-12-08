package pl.pabilo8.immersiveintelligence.api.crafting.recipe;

import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.ComponentType;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIRecipeLayout.IOType;

import javax.annotation.Nullable;

/**
 * Unified layout component for recipe GUI layouts
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 06.12.2025
 */
public class LayoutComponent
{
	private final ComponentType type;
	private final IOType ioType;
	private final int x, y;
	private final int width, height;
	@Nullable
	private final Object data;
	private final String subtype;

	private LayoutComponent(LayoutComponentBuilder builder)
	{
		this.type = builder.type;
		this.ioType = builder.ioType;
		this.x = builder.x;
		this.y = builder.y;
		this.width = builder.width;
		this.height = builder.height;
		this.data = builder.data;
		this.subtype = builder.subtype;
	}

	public static LayoutComponentBuilder builder(ComponentType type, int x, int y)
	{
		return new LayoutComponentBuilder(type, x, y);
	}

	// Getters
	public ComponentType getType()
	{
		return type;
	}

	public IOType getIoType()
	{
		return ioType;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	@Nullable
	public Object getData()
	{
		return data;
	}

	public String getSubtype()
	{
		return subtype;
	}

	public static class LayoutComponentBuilder
	{
		private final ComponentType type;
		private IOType ioType = IOType.NEUTRAL;
		private final int x, y;
		private int width = 18;
		private int height = 18;
		@Nullable
		private Object data = null;
		private String subtype = "";

		private LayoutComponentBuilder(ComponentType type, int x, int y)
		{
			this.type = type;
			this.x = x;
			this.y = y;

			// Set defaults based on type
			switch(type)
			{
				case FLUID_TANK:
				case DUST_TANK:
					this.width = 24-6;
					this.height = 47;
					break;
				case INFO_DISPLAY:
					this.width = 54;
					this.height = 12;
					break;
				case MULTIBLOCK_MODEL:
					this.width = 80;
					this.height = 60;
					break;
			}
		}

		public LayoutComponentBuilder ioType(IOType ioType)
		{
			this.ioType = ioType;
			return this;
		}

		public LayoutComponentBuilder input()
		{
			this.ioType = IOType.INPUT;
			return this;
		}

		public LayoutComponentBuilder output()
		{
			this.ioType = IOType.OUTPUT;
			return this;
		}

		public LayoutComponentBuilder size(int width, int height)
		{
			this.width = width;
			this.height = height;
			return this;
		}

		public LayoutComponentBuilder data(Object data)
		{
			this.data = data;
			return this;
		}

		public LayoutComponentBuilder subtype(String subtype)
		{
			this.subtype = subtype;
			return this;
		}

		public LayoutComponent build()
		{
			return new LayoutComponent(this);
		}
	}
}
