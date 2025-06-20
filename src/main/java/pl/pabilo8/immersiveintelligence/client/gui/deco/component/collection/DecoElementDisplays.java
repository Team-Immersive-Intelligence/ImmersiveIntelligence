package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import net.minecraft.client.renderer.GlStateManager;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;

import javax.annotation.Nullable;
import java.util.List;
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
				font.drawString(t.toString(), 0, 0, IIColor.fromHex("afafaf").getPackedRGB());
			return font.FONT_HEIGHT;
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

		default boolean isSelectable(T t)
		{
			return true;
		}

		default void drawCreateOption(int width, int height, IIFontRenderer font, int mouseX, int mouseY)
		{
			font.drawString("+ New Entry", 0, 0, IIColor.fromHex("afafaf").getPackedRGB());
		}

		default void bindCollection(DecoScrolledCollection<?, T> collection)
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
					.filter(e -> e.toString().toLowerCase().startsWith(input.toLowerCase()))
					.collect(Collectors.toList());
		}
	}
}
