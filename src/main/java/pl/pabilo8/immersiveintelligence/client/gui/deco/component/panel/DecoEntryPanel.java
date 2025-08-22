package pl.pabilo8.immersiveintelligence.client.gui.deco.component.panel;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.resources.I18n;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoScrolledCollection;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;

import java.util.Collections;
import java.util.function.Function;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 10.02.2025
 **/
public abstract class DecoEntryPanel<T> extends DecoPanel implements DecoElementDisplay<T>
{
	private DecoButton addButton;
	private DecoScrolledCollection<?, T> list;
	private T element;

	public DecoEntryPanel()
	{
		super(0, 0);
		withBackgroundMask(DecoTextures.RES_TEXTURES_DECO_TEMPLATE_PAPER);
		withBackground(DecoTextures.GUI_BG_PAPER);
		addButton = new DecoButton(0, 0)
				.withSize(16, 16)
				.withPadding(0, 0, 0, 0)
				.withIcon(DecoTextures.RES_TEXTURES_DECO_ICON_ACTION_ADD)
				.withBackground(DecoTextures.RES_TEXTURES_DECO_BUTTON_HANGING)
		;
	}

	@Override
	protected boolean initialize()
	{
		if(!initialized)
		{
			cleanup();
			initializeChildren();
		}
		return super.initialize();
	}

	public DecoScrolledCollection<?, T> getCurrentList()
	{
		return list;
	}

	public T getCurrentElement()
	{
		return element;
	}

	/**
	 * Initializes the panel, add child components here
	 */
	protected abstract void initializeChildren();

	/**
	 * Displays an element field of a listing component.
	 *
	 * @param t            The element to display
	 * @param width        The width of the element
	 * @param font         The font renderer to use
	 * @param partialTicks Partial render ticks (used for animations)
	 * @param heightProbe  If true, the method should only return the height of the element, otherwise it should draw it
	 * @return height of the element
	 */
	@Override
	public int displayElement(T t, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks, boolean heightProbe)
	{
		//If the width has changed, reinitialize the panel
		if(this.width!=width)
		{
			//Readjust the width
			this.width = width;
			if(!initialize())
				return 0;

			//Readjust the height
			int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
			for(GuiComponentDecoBase<?> child : children)
			{
				minY = Math.min(minY, child.y);
				maxY = Math.max(maxY, child.y+child.height);
			}
			this.height = 2+maxY-minY;
		}

		//If the height is being probed, return the height
		if(heightProbe)
			return height;

		//Apply the element to the panel and draw it
		applyElement(t);
		drawButton(ClientUtils.mc(), mouseX, mouseY, partialTicks);
		return height;
	}

	@Override
	public void drawCreateOption(int width, int height, IIFontRenderer font, int mouseX, int mouseY)
	{
		addButton.x = DecoAlignment.CENTER.getAlignX(x, addButton.width, width);
		addButton.y = y;
		addButton.drawButton(ClientUtils.mc(), mouseX, mouseY, 0f);
	}

	@Override
	public boolean isMouseOverCreateOption(int mouseX, int mouseY, int width, int height)
	{
		return addButton.isMouseOver();
	}

	@Override
	public void bindCollection(DecoScrolledCollection<?, T> collection)
	{
		this.list = collection;
	}

	/**
	 * Applies the element to the panel
	 *
	 * @param t The element to apply
	 */
	public final void applyElement(T t)
	{
		this.element = t;
		applyElementToChildren(t);
	}

	/**
	 * Applies the element to the panel
	 *
	 * @param t The element to apply
	 */
	protected abstract void applyElementToChildren(T t);


	/**
	 * Sets the tooltip function for this panel using the current element.
	 *
	 * @param onTooltip Function that takes the current element and returns a tooltip string.
	 * @return this
	 */
	public DecoEntryPanel<T> withElementTooltip(Function<T, String> onTooltip)
	{
		withOnTooltip(decoPanel -> Collections.singleton(I18n.format(onTooltip.apply(element))));
		return this;
	}
}
