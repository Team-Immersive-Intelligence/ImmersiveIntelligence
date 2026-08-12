package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoComponent;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stores renderer classes for Deco standard lists (DecoDropdown, DecoList) elements.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 31.01.2025
 **/
public class DecoElementDisplays
{
	/**
	 * Returns a default display method for elements, which is rendering the element's toString() method.
	 *
	 * @param <T> The type of the element
	 * @return Default display method
	 */
	public static <T> DecoElementDisplay<T> getDefaultDisplay()
	{
		return (t, width, font, mouseX, mouseY, partialTicks, heightProbe) -> {
			if(!heightProbe)
			{
				String localizedText;
				if(t instanceof EnumFacing)
					localizedText = I18n.format(IIReference.DESCRIPTION_KEY+"side."+((EnumFacing)t).getName());
				else if(t instanceof ILocalizedEnum)
					localizedText = ((ILocalizedEnum)t).getLocalizedName();
				else
					localizedText = t.toString();

				font.drawString(localizedText, 2, 2, IIColor.fromHex("afafaf").getPackedRGB());
			}
			return font.FONT_HEIGHT+1;
		};
	}

	/**
	 * Similar to {@link #getDefaultDisplay()}, but allows for custom text based on the element.
	 *
	 * @param text Function that returns the text to display for the given element
	 * @param <T>  The type of the element
	 * @return Display method
	 */
	public static <T> DecoElementDisplay<T> getSimpleTextDisplay(Function<T, String> text)
	{
		return (t, width, font, mouseX, mouseY, partialTicks, heightProbe) -> {
			if(!heightProbe)
			{
				font.drawString(text.apply(t), 2, 2, IIColor.fromHex("afafaf").getPackedRGB());
			}
			return font.FONT_HEIGHT+1;
		};
	}

	/**
	 * Returns a default sorter for elements, which is returning the elements in the same order as they were given.
	 *
	 * @param <T> The type of the element
	 * @return Default sorter
	 */
	public static <T> DecoElementSorter<T> getDefaultSorter()
	{
		return elements -> elements;
	}

	@FunctionalInterface
	public interface DecoElementDisplay<T>
	{
		/**
		 * Displays an element field of a listing component.
		 *
		 * @param t            The element to display
		 * @param width        The width of the element
		 * @param font         The font renderer to use
		 * @param mouseX       The x position of the mouse
		 * @param mouseY       The y position of the mouse
		 * @param partialTicks Partial render ticks (used for animations)
		 * @param heightProbe  If true, the method should only return the height of the element, otherwise it should draw it
		 * @return The height of the element
		 * @implNote Top-Left corner is at (0,0).
		 * The draw action is wrapped in {@link GlStateManager#pushMatrix()} and {@link GlStateManager#popMatrix()}, so there is no need to add your own.
		 */
		int displayElement(T t, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks, boolean heightProbe);

		default int displayElement(T t, int width, IIFontRenderer font, boolean heightProbe)
		{
			return displayElement(t, width, font, 0, 0, 0, heightProbe);
		}

		/**
		 * Draws overlays owned by an element after the collection has finished drawing
		 * and released its scissor. Stateful panel displays use this for dropdown lists
		 * and other content that must appear above neighbouring entries.
		 */
		default void drawElementUpperLayer(T t, int width, IIFontRenderer font, int mouseX, int mouseY, float partialTicks)
		{

		}

		/**
		 * Returns the tooltip of the currently hovered virtual element, if any.
		 */
		default List<String> getTooltip()
		{
			return Collections.emptyList();
		}

		/**
		 * Returns whether a dynamically generated component is currently owned by this display.
		 * Used to validate focus captures after caches or component trees are rebuilt.
		 */
		default boolean ownsComponent(DecoComponent<?> component)
		{
			return false;
		}

		default boolean isSelectable(T t)
		{
			return true;
		}

		default void drawCreateOption(int width, int height, IIFontRenderer font, int mouseX, int mouseY)
		{
			font.drawString("+ New Entry", 0, 0, IIColor.fromHex("afafaf").getPackedRGB());
		}

		default boolean isMouseOverCreateOption(int mouseX, int mouseY, int width, int height)
		{
			return mouseX >= 0&&mouseX < width&&mouseY >= 0&&mouseY < height;
		}

		default void bindCollection(DecoScrolledCollection<?, T> collection)
		{

		}

		/**
		 * Called once for each collection display pass.
		 *
		 * @return true when the display changed in a way that requires list layout recalculation
		 */
		default boolean onDisplayTick()
		{
			return false;
		}

		/**
		 * Called when the collection's entry set changes.
		 */
		default void onEntriesChanged(Collection<T> entries)
		{

		}

		/**
		 * Releases resources owned by this display.
		 */
		default void cleanupDisplay()
		{

		}
	}

	@FunctionalInterface
	public interface DecoElementSorter<T>
	{
		/**
		 * Sorts a list of elements.
		 *
		 * @param elements The elements to sort
		 * @return The sorted list
		 */
		List<T> sort(List<T> elements);

		/**
		 * Autocompletes an element from a list.
		 *
		 * @param elements The elements to autocomplete from
		 * @param input    The input to autocomplete
		 * @return The autocompleted element
		 */
		@Nullable
		default List<T> autocomplete(List<T> elements, String input)
		{
			return elements.stream()
					.filter(element -> {
						String localizedText;
						if(element instanceof EnumFacing)
							localizedText = I18n.format(IIReference.DESCRIPTION_KEY+"side."+((EnumFacing)element).getName());
						else if(element instanceof ILocalizedEnum)
							localizedText = ((ILocalizedEnum)element).getLocalizedName();
						else
							localizedText = element.toString();
						return localizedText.toLowerCase().startsWith(input.toLowerCase());
					})
					.collect(Collectors.toList());

		}
	}
}
