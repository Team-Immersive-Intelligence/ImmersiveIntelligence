package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import pl.pabilo8.immersiveintelligence.client.gui.deco.DecoGui;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoTextBasedComponent;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.button.DecoButton;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection.DecoElementDisplays.DecoElementSorter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Displays a scrolled collection with a row of create actions.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @updated 04.10.2026
 * @since 31.01.2025
 **/
public abstract class DecoScrolledCollection<E extends DecoScrolledCollection<? super E, T>, T> extends DecoTextBasedComponent<E>
{
	protected static final int ON_CREATE_OPTION = -10;
	protected IIColor listBackgroundColor = IIColor.WHITE;
	protected ResLoc listBackgroundLocation = DecoTextures.BG_DARK;
	protected ResLoc scrollBarLocation = DecoTextures.COMPONENT_SLIDER;

	protected Queue<T> toBeAdded = new ArrayDeque<>();
	protected Queue<T> toBeRemoved = new ArrayDeque<>();
	protected Runnable onCreate = null;
	private final List<CreateAction> createActions = new ArrayList<>();
	private boolean layoutDirty;

	protected List<T> entries = new ArrayList<>();
	protected int scroll = 0, maxScroll = 0, scrollStep = fontRenderer.FONT_HEIGHT;
	protected int entriesInGrid = 1;
	protected int entryMaxWidth;
	protected DecoElementDisplay<T> display = DecoElementDisplays.getDefaultDisplay();
	protected DecoElementSorter<T> sorter = DecoElementDisplays.getDefaultSorter();

	public DecoScrolledCollection(int x, int y)
	{
		super(x, y);
		withOnScroll((gui, scroll, mouseX, mouseY) -> {
			if(hovered&&maxScroll > 0)
			{
				this.scroll = MathHelper.clamp(this.scroll-(scroll*scrollStep), 0, maxScroll);
				return true;
			}
			return false;
		});
	}

	@Override
	public <GUI, C extends Container> void setParentGUI(DecoGui<GUI, C> parent)
	{
		super.setParentGUI(parent);
		//Displays may have created entry panels during an earlier height probe, before
		//the collection was attached to its GUI. Rebinding discards those contextless
		//instances so interactive children receive the proper focus owner.
		display.bindCollection(this);
	}

	@Override
	public List<String> getTooltip()
	{
		List<String> tooltip = super.getTooltip();
		if(tooltip.isEmpty()&&hovered)
			for(CreateAction action : createActions)
				if(action.button.isMouseOver())
					return action.button.getTooltip();
		return (tooltip.isEmpty()&&display!=this)?display.getTooltip(): tooltip;
	}

	/**
	 * Sets the background texture of the list
	 *
	 * @param listBackgroundLocation The background location
	 * @return this
	 */
	public E withListBackground(ResLoc listBackgroundLocation)
	{
		this.listBackgroundLocation = listBackgroundLocation;
		//noinspection unchecked
		return (E)this;
	}

	/**
	 * Sets the background color of the list
	 *
	 * @param listBackgroundColor The background color
	 * @return this
	 */
	public E withListBackgroundColor(IIColor listBackgroundColor)
	{
		this.listBackgroundColor = listBackgroundColor;
		//noinspection unchecked
		return (E)this;
	}

	/**
	 * Sets the texture of the scrollbar
	 *
	 * @param scrollBarLocation The scrollbar location
	 * @return this
	 */
	public E withScrollBarBackground(ResLoc scrollBarLocation)
	{
		this.scrollBarLocation = scrollBarLocation;
		//noinspection unchecked
		return (E)this;
	}

	/**
	 * Sets the display function for the elements
	 *
	 * @param display The display function
	 * @return this
	 */
	public E withDisplayFunction(DecoElementDisplay<T> display)
	{
		this.display.cleanupDisplay();
		this.display = display;
		display.bindCollection(this);
		display.onEntriesChanged(entries);
		calculateSlideLength();
		//noinspection unchecked
		return (E)this;
	}

	/**
	 * Sets the sort function for the elements
	 *
	 * @param sorter The sort function
	 * @return this
	 */
	public E withSortFunction(DecoElementSorter<T> sorter)
	{
		this.sorter = sorter;
		this.entries = sorter.sort(this.entries);
		display.onEntriesChanged(entries);
		calculateSlideLength();
		//noinspection unchecked
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
		if(entries instanceof EasyCollection)
			this.entries = (List<T>)entries;
		else
			this.entries = new ArrayList<>(entries);
		this.entries = sorter.sort(this.entries);
		display.onEntriesChanged(this.entries);
		calculateSlideLength();
		//noinspection unchecked
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
		this.entriesInGrid = Math.max(1, entriesInGrid);
		calculateSlideLength();
		//noinspection unchecked
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
		return withCreateLaterAction(() -> {
			T entry = onCreate.get();
			if(entry!=null)
				addEntry(entry);
		});
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
		requestLayout();
		//noinspection unchecked
		return (E)this;
	}

	/**
	 * Adds a create button after the default action in the same row.
	 */
	public E addCreateAction(ResourceLocation icon, String tooltip, Supplier<T> onCreate)
	{
		return addCreateLaterAction(icon, tooltip, () -> {
			T entry = onCreate.get();
			if(entry!=null)
				addEntry(entry);
		});
	}

	/**
	 * Adds a button for an action that creates or edits an entry later.
	 */
	@SuppressWarnings("unchecked")
	public E addCreateLaterAction(ResourceLocation icon, String tooltip, Runnable onCreate)
	{
		DecoButton button = new DecoButton(0, 0).withSize(16, getAddButtonHeight())
				.withPadding(0, 0, 0, 0).withIcon(icon, 12)
				.withBackground(DecoTextures.COMPONENT_BUTTON_HANGING).withTranslatedTooltip(tooltip);
		createActions.add(new CreateAction(button, onCreate));
		requestLayout();
		return (E)this;
	}

	/**
	 * Requests a layout update on the next draw pass.
	 */
	public void requestLayout()
	{
		layoutDirty = true;
	}

	protected final boolean hasCreateActions()
	{
		return onCreate!=null||!createActions.isEmpty();
	}

	private int getCreateActionCount()
	{
		return createActions.size()+(onCreate==null?0: 1);
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
		//noinspection unchecked
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
		int alreadyDrawnHeight = 1;
		for(int i = 0; i < filteredEntries.size(); i += entriesInGrid)
		{
			int rowHeight = 0;
			for(int j = i; j < Math.min(i+entriesInGrid, filteredEntries.size()); j++)
				rowHeight = Math.max(rowHeight, display.displayElement(filteredEntries.get(j), Math.max(1, (width-12)/entriesInGrid), fontRenderer, true));
			alreadyDrawnHeight += rowHeight;
		}
		if(hasCreateActions())
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
		boolean entriesChanged = !toBeAdded.isEmpty()||!toBeRemoved.isEmpty();
		if(entriesChanged)
		{
			while(!toBeAdded.isEmpty())
			{
				entries.add(toBeAdded.poll());
			}
			while(!toBeRemoved.isEmpty())
			{
				entries.remove(toBeRemoved.poll());
			}
			entries = sorter.sort(entries);
			display.onEntriesChanged(entries);
		}

		//Allow stateful displays to invalidate their cached layout on a controlled cadence.
		boolean displayChanged = display.onDisplayTick();
		if(entriesChanged||layoutDirty||displayChanged)
		{
			layoutDirty = false;
			calculateSlideLength();
		}

		//Draw list
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Background
		int listHeight = getListHeight();
		draw.drawConnectedTexColorRect(x, y, listWidth, listHeight, listBackgroundColor, listBackgroundLocation, 64, 64, 8, 8);
		//Scrollbar
		if(shouldAlwaysHaveScrollbar()||maxScroll > 0)
		{
			TextureAtlasSprite scrollbarSprite = ClientUtils.getSprite(scrollBarLocation);
			//Scrollbar background
			draw.drawConnectedTexColorRect(x+listWidth-11, y,
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
				draw.drawConnectedTexColorRect(
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
		if(parentGui!=null)
			parentGui.scissorStart(x, y, listWidth, listHeight);
		GlStateManager.translate(0, -scroll, 0);

		//Filter entries based on search input
		List<T> filteredEntries = autocomplete();
		List<DisplayedElement<T>> displayedElements = new ArrayList<>(filteredEntries.size());
		int alreadyDrawnHeight = 0;
		int currentColumn = 0, rowHeight = 0;
		for(T filteredEntry : filteredEntries)
		{
			int elementX = x+(currentColumn*entryMaxWidth);
			int elementY = y+1+alreadyDrawnHeight;
			if(parentGui!=null)
				parentGui.pushScissorOffset(elementX, elementY-scroll);
			GlStateManager.pushMatrix();
			int offset;
			try
			{
				GlStateManager.translate(elementX, elementY, 0);
				offset = display.displayElement(filteredEntry, entryMaxWidth, fontRenderer,
						mouseX-elementX, mouseY+scroll-elementY, partialTicks, false);
			} finally
			{
				GlStateManager.popMatrix();
				if(parentGui!=null)
					parentGui.popScissorOffset();
			}
			displayedElements.add(new DisplayedElement<>(filteredEntry, elementX, elementY-scroll, offset));

			rowHeight = Math.max(rowHeight, offset);
			currentColumn++;
			if(currentColumn >= entriesInGrid)
			{
				currentColumn = 0;
				alreadyDrawnHeight += rowHeight;
				rowHeight = 0;
			}
		}

		alreadyDrawnHeight += rowHeight;
		// Draw all create actions in one row.
		if(hasCreateActions())
		{
			int rowWidth = entryMaxWidth*entriesInGrid;
			int count = getCreateActionCount();
			for(int i = 0; i < count; i++)
			{
				int startX = i*rowWidth/count;
				int slotWidth = (i+1)*rowWidth/count-startX;
				GlStateManager.pushMatrix();
				GlStateManager.translate(x+startX, y+1+alreadyDrawnHeight, 0);
				int localX = mouseX-x-startX, localY = mouseY+scroll-y-1-alreadyDrawnHeight;
				if(i==0&&onCreate!=null)
					display.drawCreateOption(slotWidth, getAddButtonHeight(), fontRenderer, localX, localY);
				else
				{
					DecoButton button = createActions.get(i-(onCreate==null?0: 1)).button;
					int buttonX = (slotWidth-button.width)/2;
					if(button.x!=buttonX)
					{
						button.x = buttonX;
						button.initialize();
					}
					button.drawButton(ClientUtils.mc(), localX, localY, partialTicks);
				}
				GlStateManager.popMatrix();
			}
		}
		if(parentGui!=null)
			parentGui.scissorEnd();
		GlStateManager.popMatrix();

		//Element overlays must be drawn after neighbouring rows and outside the parent
		//collection scissor. Otherwise nested dropdown lists are clipped or overdrawn.
		for(DisplayedElement<T> element : displayedElements)
		{
			if(element.y+element.height < y||element.y > y+listHeight)
				continue;

			//The element panel is positioned using the model-view matrix. glScissor does
			//not observe that translation, so nested collections need the same origin
			//supplied separately in window-coordinate calculations.
			if(parentGui!=null)
				parentGui.pushScissorOffset(element.x, element.y);
			GlStateManager.pushMatrix();
			try
			{
				GlStateManager.translate(element.x, element.y, 0);
				display.drawElementUpperLayer(element.entry, entryMaxWidth, fontRenderer,
						mouseX-element.x, mouseY-element.y, partialTicks);
			} finally
			{
				GlStateManager.popMatrix();
				if(parentGui!=null)
					parentGui.popScissorOffset();
			}
		}
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
		int currentColumn = 0, rowHeight = 0;
		//Filter entries based on search input
		List<T> entries = autocomplete();

		//Iterate over all entries
		for(T entry : entries)
		{
			//Measure element height for layout for all entries
			int elementHeight = display.displayElement(entry, entryMaxWidth, fontRenderer, true);

			//Check if the mouse is over the current entry
			if(IIMath.isPointInRectangle(xx+(currentColumn*entryMaxWidth), yy+drawOffset,
					xx+(currentColumn*entryMaxWidth)+entryMaxWidth,
					yy+drawOffset+elementHeight,
					mouseX, mouseY))
			{
				//Only return selectable entries
				if(display.isSelectable(entry))
					return new Tuple<>(
							this.entries.indexOf(entry),
							yy+drawOffset
					);
			}

			rowHeight = Math.max(rowHeight, elementHeight);
			currentColumn++;
			//There can be multiple entries in one row
			if(currentColumn >= entriesInGrid)
			{
				currentColumn = 0;
				drawOffset += rowHeight;
				rowHeight = 0;
			}

		}
		drawOffset += rowHeight;
		if(hasCreateActions())
		{
			int rowWidth = entryMaxWidth*entriesInGrid, count = getCreateActionCount();
			int localY = mouseY-yy-drawOffset;
			for(int i = 0; i < count; i++)
			{
				int startX = i*rowWidth/count, slotWidth = (i+1)*rowWidth/count-startX;
				int localX = mouseX-xx-startX;
				boolean over;
				if(i==0&&onCreate!=null)
					over = display.isMouseOverCreateOption(localX, localY, slotWidth, getAddButtonHeight());
				else
				{
					DecoButton button = createActions.get(i-(onCreate==null?0: 1)).button;
					int buttonX = (slotWidth-button.width)/2;
					over = localX >= buttonX&&localX < buttonX+button.width&&localY >= 0&&localY < button.height;
				}
				if(over)
					return new Tuple<>(ON_CREATE_OPTION-i, yy+drawOffset);
			}
		}
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
		return runCreateAction(ON_CREATE_OPTION);
	}

	protected boolean runCreateAction(int option)
	{
		int index = ON_CREATE_OPTION-option;
		if(index < 0||index >= getCreateActionCount())
			return false;
		if(index==0&&onCreate!=null)
			onCreate.run();
		else
			createActions.get(index-(onCreate==null?0: 1)).action.run();
		return true;
	}

	private static class CreateAction
	{
		private final DecoButton button;
		private final Runnable action;

		private CreateAction(DecoButton button, Runnable action)
		{
			this.button = button;
			this.action = action;
		}
	}

	private static class DisplayedElement<T>
	{
		private final T entry;
		private final int x, y, height;

		private DisplayedElement(T entry, int x, int y, int height)
		{
			this.entry = entry;
			this.x = x;
			this.y = y;
			this.height = height;
		}
	}
}
