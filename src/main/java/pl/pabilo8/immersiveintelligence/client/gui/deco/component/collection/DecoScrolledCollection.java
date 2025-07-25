package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 31.01.2025
 **/
public abstract class DecoScrolledCollection<E extends DecoScrolledCollection<? super E, T>, T> extends GuiComponentDecoTextBase<E>
{
	protected static final int ON_CREATE_OPTION = -10;
	protected ResLoc listBackgroundLocation = IIReference.GUI_BG_DARK;
	protected ResLoc scrollBarLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_SLIDER;

	protected Queue<T> toBeAdded = new ArrayDeque<>();
	protected Queue<T> toBeRemoved = new ArrayDeque<>();
	protected Runnable onCreate = null;

	protected List<T> entries = new ArrayList<>();
	protected int scroll = 0, maxScroll = 0, scrollStep = fontRenderer.FONT_HEIGHT;
	protected int entriesInGrid = 1;
	protected int entryMaxWidth;
	protected DecoElementDisplays.DecoElementDisplay<T> display = DecoElementDisplays.getDefaultDisplay();
	protected DecoElementDisplays.DecoElementSorter<T> sorter = DecoElementDisplays.getDefaultSorter();

	public DecoScrolledCollection(int x, int y)
	{
		super(x, y);
		withOnScroll((gui, scroll, mouseX, mouseY) -> {
			if(hovered&&maxScroll > 0)
			{
				this.scroll = MathHelper.clamp(this.scroll-(scroll/scrollStep), 0, maxScroll);
				return true;
			}
			return false;
		});
	}

	/**
	 * Sets the background texture of the list
	 *
	 * @param listBackgroundLocation The background location
	 * @return this
	 */
	public DecoScrolledCollection<E, T> withListBackgroundLocation(ResLoc listBackgroundLocation)
	{
		this.listBackgroundLocation = listBackgroundLocation;
		return this;
	}

	/**
	 * Sets the texture of the scrollbar
	 *
	 * @param scrollBarLocation The scrollbar location
	 * @return this
	 */
	public DecoScrolledCollection<E, T> withScrollBarLocation(ResLoc scrollBarLocation)
	{
		this.scrollBarLocation = scrollBarLocation;
		return this;
	}

	/**
	 * Sets the display function for the elements
	 *
	 * @param display The display function
	 * @return this
	 */
	public E withDisplayFunction(DecoElementDisplays.DecoElementDisplay<T> display)
	{
		this.display = display;
		display.bindCollection(this);
		return (E)this;
	}

	/**
	 * Sets the sort function for the elements
	 *
	 * @param sorter The sort function
	 * @return this
	 */
	public E withSortFunction(DecoElementDisplays.DecoElementSorter<T> sorter)
	{
		this.sorter = sorter;
		return (E)this;
	}

	/**
	 * Sets the entries of the collection
	 *
	 * @param entries The entries
	 * @return this
	 */
	public E withEntries(Collection<T> entries)
	{
		this.entries = new ArrayList<>(entries);
		this.entries = sorter.sort(this.entries);
		calculateSlideLength();
		return (E)this;
	}

	/**
	 * Sets the entries of the collection
	 *
	 * @param entries The entries
	 * @return this
	 * @see #withEntries(Collection)
	 */
	@SafeVarargs
	public final E withEntries(@Nonnull T... entries)
	{
		return withEntries(new ArrayList<>(Arrays.asList(entries)));
	}

	/**
	 * Sets the number of entries in one row
	 *
	 * @param entriesInGrid The number of entries in one row
	 * @return this
	 */
	public E withEntriesInGrid(int entriesInGrid)
	{
		this.entriesInGrid = entriesInGrid;
		calculateSlideLength();
		return (E)this;
	}

	/**
	 * Sets the action to be executed when the "create" option is clicked
	 *
	 * @param onCreate The action
	 * @return this
	 */
	public E withCreateAction(Supplier<T> onCreate)
	{
		this.onCreate = () -> addEntry(onCreate.get());
		return (E)this;
	}

	/**
	 * Sets the action to be executed when the "create" option is clicked. This can be used to set another GUI where this new "entry" is edited.
	 *
	 * @param onCreate The action
	 * @return this
	 * @see #withCreateAction(Supplier)
	 */
	public E withCreateLaterAction(Runnable onCreate)
	{
		this.onCreate = onCreate;
		return (E)this;
	}

	/**
	 * Queues an entry to be added to the list
	 *
	 * @param entry The entry
	 * @return true if the entry will be added
	 */
	public boolean addEntry(@Nonnull T entry)
	{
		return toBeAdded.add(entry);
	}

	/**
	 * Queues an entry to be removed from the list
	 *
	 * @param entry The entry
	 * @return true if the entry will be removed
	 */
	public boolean removeEntry(@Nonnull T entry)
	{
		return toBeRemoved.add(entry);
	}

	/**
	 * Sets the current scroll value of the list
	 *
	 * @param scroll The scroll
	 * @return this
	 */
	public E withScroll(int scroll)
	{
		this.scroll = scroll;
		return (E)this;
	}

	/**
	 * @return the current scroll value of the list
	 */
	public int getScroll()
	{
		return scroll;
	}

	/**
	 * Calculates the list parameters: maximum scroll value, scroll step, entry width
	 * Clamps the current scroll value.
	 *
	 * @return the maximum scroll value of the list
	 */
	protected int calculateSlideLength()
	{
		List<T> filteredEntries = autocomplete();
		int alreadyDrawnHeight = 0;
		for(int i = 0; i < filteredEntries.size(); i += entriesInGrid)
			alreadyDrawnHeight += display.displayElement(filteredEntries.get(i), width-12, fontRenderer, true);
		if(onCreate!=null)
			alreadyDrawnHeight += getAddButtonHeight();

		this.entryMaxWidth = ((shouldAlwaysHaveScrollbar()||alreadyDrawnHeight > height)?(width-12): width)/entriesInGrid;
		this.maxScroll = Math.max(0, alreadyDrawnHeight-getListHeight());
		this.scrollStep = !filteredEntries.isEmpty()?Math.max(1, alreadyDrawnHeight/filteredEntries.size()/entriesInGrid): 1;
		this.scroll = MathHelper.clamp(this.scroll, 0, maxScroll);
		return alreadyDrawnHeight;
	}

	/**
	 * @return the height of the "create" option
	 */
	protected abstract int getAddButtonHeight();

	/**
	 * Filters the entries based on the search input
	 *
	 * @return The filtered entries
	 */
	protected List<T> autocomplete()
	{
		if(!text.isEmpty())
			return sorter.autocomplete(entries, text);
		return entries;
	}

	protected final void drawList(int x, int y, int listWidth, int mouseX, int mouseY, float partialTicks)
	{
		//Apply queued changes to the list
		if(!toBeAdded.isEmpty()||!toBeRemoved.isEmpty())
		{
			while(!toBeAdded.isEmpty())
			{
				entries.add(toBeAdded.poll());
				entries = sorter.sort(entries);
			}
			while(!toBeRemoved.isEmpty())
			{
				entries.remove(toBeRemoved.poll());
				entries = sorter.sort(entries);
			}
			calculateSlideLength();
		}

		//Draw list
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Background
		int listHeight = getListHeight();
		draw.drawConnectedColorRect(x, y, listWidth, listHeight, IIColor.WHITE, listBackgroundLocation, 64, 64, 8, 8);
		//Scrollbar
		if(shouldAlwaysHaveScrollbar()||maxScroll > 0)
		{
			TextureAtlasSprite scrollbarSprite = ClientUtils.getSprite(scrollBarLocation);
			//Scrollbar background
			draw.drawConnectedColorRect(x+listWidth-11, y,
					10, listHeight,
					IIColor.WHITE, 10, 32, 0, 4,
					scrollbarSprite.getMinU(), scrollbarSprite.getInterpolatedU(5),
					scrollbarSprite.getMinV(), scrollbarSprite.getMaxV()
			);
			//Scrollbar
			if(maxScroll > 0)
			{
				int scrollBarHeight = Math.max(10, (int)((listHeight/(float)(maxScroll+listHeight))*listHeight));
				int scrollbarOffset = (int)((scroll/(float)maxScroll)*(listHeight-scrollBarHeight));
				draw.drawConnectedColorRect(
						x+listWidth-11, y+1+scrollbarOffset, 10, scrollBarHeight,
						IIColor.WHITE, 10, 32, 2, 8,
						scrollbarSprite.getInterpolatedU(5), scrollbarSprite.getInterpolatedU(10),
						scrollbarSprite.getMinV(), scrollbarSprite.getMaxV()
				);
			}
		}
		draw.finish();

		//Draw only a cutout of the elements
		GlStateManager.pushMatrix();
		scissor(x, y, listWidth, listHeight);
		GlStateManager.translate(0, -scroll, 0);

		//Filter entries based on search input
		List<T> filteredEntries = autocomplete();
		int alreadyDrawnHeight = 0;
		int currentColumn = 0;
		for(T filteredEntry : filteredEntries)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+(currentColumn*entryMaxWidth), y+1+alreadyDrawnHeight, 0);
			int offset = display.displayElement(filteredEntry, entryMaxWidth, fontRenderer, mouseX-(x+(currentColumn*entryMaxWidth)), mouseY+scroll-y-alreadyDrawnHeight, partialTicks, false);
			GlStateManager.popMatrix();

			currentColumn++;
			if(currentColumn >= entriesInGrid)
			{
				currentColumn = 0;
				alreadyDrawnHeight += offset;
			}
		}

		//Draw the "create" option
		if(onCreate!=null)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y+alreadyDrawnHeight, 0);
			display.drawCreateOption(entryMaxWidth, getAddButtonHeight(), fontRenderer, mouseX-x, mouseY+scroll-y-alreadyDrawnHeight);
			GlStateManager.popMatrix();
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}

	@Override
	protected boolean initialize()
	{
		calculateSlideLength();
		return true;
	}

	/**
	 * @return true if the list should always have a scrollbar
	 */
	protected abstract boolean shouldAlwaysHaveScrollbar();

	/**
	 * @return the height of the drawn list (not necessarily the height of the GUI element)
	 */
	protected int getListHeight()
	{
		return height;
	}

	/**
	 * Calculates the index and offset of the clicked entry
	 *
	 * @param xx     x position of the list
	 * @param yy     y position of the list
	 * @param mouseX x position of the mouse
	 * @param mouseY y position of the mouse
	 * @return Tuple containing the index and offset of the clicked entry
	 */
	@Nullable
	protected Tuple<Integer, Integer> getClickedEntryIndex(int xx, int yy, int mouseX, int mouseY)
	{
		int drawOffset = 0;
		int currentColumn = 0;
		//Filter entries based on search input
		List<T> entries = autocomplete();

		//Iterate over all entries
		for(T entry : entries)
		{
			//Skip non-selectable (clickable) entries
			if(!display.isSelectable(entry))
				continue;
			int elementHeight = display.displayElement(entry, entryMaxWidth, fontRenderer, true);

			//Check if the mouse is over the current entry
			if(IIMath.isPointInRectangle(xx+(currentColumn*entryMaxWidth), yy+drawOffset,
					xx+(currentColumn*entryMaxWidth)+entryMaxWidth,
					yy+drawOffset+display.displayElement(entry, entryMaxWidth, fontRenderer, true),
					mouseX, mouseY))
				return new Tuple<>(
						this.entries.indexOf(entry),
						yy+drawOffset
				);

			currentColumn++;
			//There can be multiple entries in one row
			if(currentColumn >= entriesInGrid)
			{
				currentColumn = 0;
				drawOffset += elementHeight;
			}

		}
		if(onCreate!=null&&IIMath.isPointInRectangle(xx, yy+drawOffset, xx+width, yy+drawOffset+getAddButtonHeight(), mouseX, mouseY))
			return new Tuple<>(ON_CREATE_OPTION, yy+drawOffset);

		return null;
	}

	/**
	 * Runs the "create" action
	 *
	 * @return true if the action was executed
	 * @see #withCreateAction(Supplier)
	 */
	protected boolean runCreateAction()
	{
		if(onCreate!=null)
			onCreate.run();
		return onCreate!=null;
	}
}