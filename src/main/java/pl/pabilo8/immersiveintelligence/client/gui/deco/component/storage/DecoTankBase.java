package pl.pabilo8.immersiveintelligence.client.gui.deco.component.storage;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @implNote RESOURCE must implement {@link #hashCode()} method properly, see {@link FluidStack#hashCode()}
 * @since 16.06.2025
 */
public abstract class DecoTankBase<TYPE extends DecoTankBase<TYPE, RESOURCE>, RESOURCE> extends GuiComponentDecoBase<TYPE>
{
	private final String STRING_TANK_EMPTY = I18n.format("gui.immersiveengineering.empty");
	private final Map<RESOURCE, Float> displayedAmounts = new HashMap<>();

	protected ResLoc tankBackgroundLocation = IIReference.GUI_BG_DARK_TANK;
	protected ResLoc tankOverlayLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_TANK;

	protected int lastMouseY = 0;
	protected int borderSize = 0;
	protected float animationSpeed = 0.1f;

	public DecoTankBase(int x, int y)
	{
		super(x, y);
	}

	public TYPE withBorderSize(int borderSize)
	{
		this.borderSize = borderSize;
		//noinspection unchecked
		return (TYPE)this;
	}

	public TYPE withTankBackgroundLocation(ResLoc tankBackgroundLocation)
	{
		this.tankBackgroundLocation = tankBackgroundLocation;
		//noinspection unchecked
		return (TYPE)this;
	}

	public TYPE withTankOverlayLocation(ResLoc tankOverlayLocation)
	{
		this.tankOverlayLocation = tankOverlayLocation;
		//noinspection unchecked
		return (TYPE)this;
	}

	@Override
	protected boolean initialize()
	{
		List<RESOURCE> contents = getContents();
		if(contents!=null&&!contents.isEmpty())
			for(RESOURCE resource : contents)
				displayedAmounts.put(resource, 0f); //(float)getResourceAmount(resource)
		return true;
	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		//Draw background
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		draw.drawConnectedColorRect(x, y, width, height, IIColor.WHITE, tankBackgroundLocation, 64, 64, 8, 8);

		//Draw contents
		List<RESOURCE> contents = getContents();
		if(contents!=null&&!contents.isEmpty())
		{
			//Save the mouse position for tooltip and ingredient handling
			this.lastMouseY = mouseY-y;

			//Calculate the tank bounds
			int fullWidth = width-2*borderSize;
			int fullHeight = height-2*borderSize;
			int yOffset = y+fullHeight;
			float capacity = getResourceCapacity();

			//For compatibility with MultiFluidTanks, DecoTankBase supports multiple resources
			for(RESOURCE resource : contents)
			{
				int resourceAmount = getResourceAmount(resource);
				if(resourceAmount <= 0)
					continue;
				float displayedAmount = displayedAmounts.getOrDefault(resource, 0f);

				//Smoothly interpolate the displayed amount towards the actual amount
				displayedAmount = (int)Math.ceil(displayedAmount+(resourceAmount-displayedAmount)*animationSpeed);
				int resourceHeight = (int)(fullHeight*(displayedAmount/capacity));
				displayedAmounts.put(resource, displayedAmount);

				//Draw the layer, offset upwards by the height of it
				yOffset -= resourceHeight;
				draw.drawRepeatedColorRect(x+borderSize, yOffset, fullWidth, resourceHeight, getResourceColor(resource),
						getResourceTexture(resource), 16);
			}
		}

		//Draw overlay
		draw.drawConnectedColorRect(x, y, width, height, IIColor.WHITE, tankOverlayLocation, 64, 64, 8, 8)
				.finish();
	}

	@Override
	public void cleanup()
	{

	}

	/**
	 * @return the maximum capacity of this tank, in the number of resources it can hold
	 * @implNote MUST be greater than 0 or the render method will throw an exception
	 */
	protected abstract int getResourceCapacity();

	/**
	 * @return the list of resources contained in this tank, or null if it is empty
	 */
	@Nullable
	protected abstract List<RESOURCE> getContents();

	/**
	 * Returns the amount of the given resource in this tank.
	 *
	 * @param resource the resource to check
	 * @return the amount of the resource in this tank, or 0 if it is not present
	 */
	public abstract int getResourceAmount(@Nonnull RESOURCE resource);

	/**
	 * Returns the color for the given resource.
	 *
	 * @param resource the resource to get the color for
	 * @return the color for the resource, or IIColor.WHITE if it does not have a specific color
	 */
	@Nonnull
	protected abstract IIColor getResourceColor(RESOURCE resource);

	/**
	 * Returns the resource texture for the given resource.
	 *
	 * @param resource the resource to get the texture for
	 * @return the resource texture, or null if it does not have one
	 */
	public abstract ResourceLocation getResourceTexture(@Nonnull RESOURCE resource);

	/**
	 * Adds a tooltip for the given resource to the provided tooltip list.
	 *
	 * @param tooltip      the tooltip list to add to
	 * @param resource     the resource to add the tooltip for
	 * @param tankCapacity the capacity of the tank
	 */
	protected abstract void addResourceTooltip(List<String> tooltip, RESOURCE resource, int tankCapacity);

	@Override
	public List<String> getTooltip()
	{
		List<String> tooltip = super.getTooltip();

		List<RESOURCE> contents = getContents();
		if(contents==null||contents.isEmpty())
			tooltip.add(STRING_TANK_EMPTY);
		else
		{
			int yy = height-2*borderSize, fullHeight = yy;
			float capacity = getResourceCapacity();
			for(RESOURCE resource : contents)
			{
				//Check if the resource is present in the tank
				int resourceAmount = getResourceAmount(resource);
				if(resourceAmount <= 0)
					continue;

				int fluidHeight = (int)Math.ceil(fullHeight*(resourceAmount/capacity));
				yy -= fluidHeight;
				//Thanks Flaxbeard!
				if(lastMouseY >= yy&&lastMouseY < yy+fluidHeight)
					addResourceTooltip(tooltip, resource, (int)capacity);
			}
		}

		return tooltip;
	}

	@Nullable
	@Override
	public RESOURCE getProvidedIngredient()
	{
		List<RESOURCE> contents = getContents();
		if(contents==null||contents.isEmpty())
			return null;
		else
		{
			int yy = height-2*borderSize, fullHeight = yy;
			float capacity = getResourceCapacity();
			for(RESOURCE resource : contents)
			{
				//Check if the resource is present in the tank
				int resourceAmount = getResourceAmount(resource);
				if(resourceAmount <= 0)
					continue;

				int fluidHeight = (int)Math.ceil(fullHeight*(resourceAmount/capacity));
				yy -= fluidHeight;
				//Thanks Flaxbeard!
				if(lastMouseY >= yy&&lastMouseY < yy+fluidHeight)
					return resource;
			}
		}
		return null;
	}
}
