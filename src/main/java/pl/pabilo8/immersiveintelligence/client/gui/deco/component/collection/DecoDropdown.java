package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoAlignment;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoTextures;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.02.2025
 * @ii-approved 0.3.1
 * @since 17.09.2021
 */
public class DecoDropdown<T> extends DecoScrolledCollection<DecoDropdown<T>, T>
{
	public int selectedEntry = -1;
	protected int blinkTime = 0;
	protected int maxDropHeight = 32, maxPossibleDropHeight = 32;
	protected int dropdownWidth;
	protected boolean dropped = false;
	protected BiConsumer<T, T> onSelectedEntry;
	private ResLoc dropdownSymbolLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_DROPDOWN_SYMBOL;

	public DecoDropdown(int x, int y)
	{
		super(x, y);
		this.backgroundLocation = DecoTextures.RES_TEXTURES_DECO_COMPONENT_BUTTON;
		withSize(120, 12);
		withOnPressed((gui, mouseButton, mouseX, mouseY) -> {
			if(mouseButton==MouseButton.LEFT)
			{
				if(dropped&&!IIMath.isPointInRectangle(x, y, x+width, y+height, mouseX, mouseY))
				{
					Tuple<Integer, Integer> clicked = getClickedEntryIndex(gui.x+2, gui.y-scroll+height+2, mouseX, mouseY);
					if(clicked!=null)
					{
						if(clicked.getFirst()==ON_CREATE_OPTION)
							gui.runCreateAction();
						else
							changeSelectedEntry(clicked.getFirst());
						text = "";
						dropped = false;
						return true;
					}
				}
				dropped = !dropped;
				pressed = false;
				return true;
			}
			return false;
		});
		withOnKeyTyped((gui, typedChar, keyCode) -> {
			//Should only work when dropped
			if(!dropped)
				return false;
			//Text entering and removal
			if(keyCode==Keyboard.KEY_RETURN)
			{
				List<T> completions = sorter.autocomplete(entries, text);
				if(completions!=null&&!completions.isEmpty())
					selectedEntry = entries.indexOf(completions.get(0));
				text = "";
				dropped = false;
				return true;
			}
			else if(keyCode==Keyboard.KEY_BACK)
			{
				if(!text.isEmpty())
					text = text.substring(0, text.length()-1);
				return true;
			}

			if(Character.isLetterOrDigit(typedChar)||Character.isSpaceChar(typedChar))
				text += typedChar;
			return true;
		});
	}

	@SafeVarargs
	@Deprecated
	public DecoDropdown(int buttonId, int x, int y, int w, int h, int perPage, T... entries)
	{
		this(x, y);
		withSize(w, h);
		withEntries(entries);

	}

	@Override
	public void setFocused(boolean focused)
	{
		if(!focused)
		{
			text = "";
			dropped = false;
		}
		super.setFocused(focused);
	}

	@Override
	public DecoDropdown<T> withSize(int width, int height)
	{
		//Change only if width is the same as dropdown width
		dropdownWidth = this.width==width?dropdownWidth: width-12;
		return super.withSize(width, height);
	}

	@Override
	protected int getAddButtonHeight()
	{
		return 16;
	}

	public DecoDropdown<T> withDropdownSymbol(ResLoc dropdownSymbolLocation)
	{
		this.dropdownSymbolLocation = dropdownSymbolLocation;
		return this;
	}

	/**
	 * Sets the selected entry to a given index. Runs the {@link #onSelectedEntry} event.
	 *
	 * @param selectedEntry the index of the selected entry, -1 for no selection
	 * @return this
	 */
	public DecoDropdown<T> withSelectedEntry(int selectedEntry)
	{
		changeSelectedEntry(selectedEntry);
		return this;
	}

	/**
	 * Sets the selected entry to a given index. Runs the {@link #onSelectedEntry} event.
	 *
	 * @param newSelectedEntry new selected entry index, -1 for no selection
	 */
	private void changeSelectedEntry(int newSelectedEntry)
	{
		if(this.onSelectedEntry!=null)
			this.onSelectedEntry.accept(getSelectedEntry(), getEntry(newSelectedEntry));
		this.selectedEntry = newSelectedEntry;
	}

	/**
	 * Sets the selected entry to a given entry. Runs the {@link #onSelectedEntry} event.
	 *
	 * @param selectedEntry new selected entry, null for no selection
	 */
	public DecoDropdown<T> withSelectedEntry(T selectedEntry)
	{
		return withSelectedEntry(entries.indexOf(selectedEntry));
	}

	/**
	 * Sets the onSelectedEntry event, which is called when the selected entry changes.
	 *
	 * @param onSelectedEntry the event to run, with the previous and new selected entry as parameters
	 **/
	public DecoDropdown<T> withOnSelectedEntry(BiConsumer<T, T> onSelectedEntry)
	{
		this.onSelectedEntry = onSelectedEntry;
		return this;
	}

	/**
	 * Sets the maximum height of the dropdown list.
	 * If the list exceeds this height, a scrollbar will be shown.
	 *
	 * @param maxDropHeight the maximum height of the dropdown list
	 */
	public DecoDropdown<T> withMaxDropHeight(int maxDropHeight)
	{
		this.maxDropHeight = maxDropHeight;
		return this;
	}

	/**
	 * Sets the width of the dropdown list.
	 * This is the width of the list when it is dropped down.
	 *
	 * @param dropdownWidth the width of the dropdown list
	 */
	public DecoDropdown<T> withDropdownWidth(int dropdownWidth)
	{
		this.dropdownWidth = dropdownWidth;
		this.calculateSlideLength();
		return this;
	}

	@Override
	protected int calculateSlideLength()
	{
		List<T> filteredEntries = autocomplete();
		int alreadyDrawnHeight = 0;
		for(int i = 0; i < filteredEntries.size(); i += entriesInGrid)
			alreadyDrawnHeight += display.displayElement(filteredEntries.get(i), dropdownWidth-12, fontRenderer, true);
		if(onCreate!=null)
			alreadyDrawnHeight += getAddButtonHeight();

		this.entryMaxWidth = ((shouldAlwaysHaveScrollbar()||alreadyDrawnHeight > maxDropHeight)?(dropdownWidth-12): dropdownWidth)/entriesInGrid;
		this.maxPossibleDropHeight = Math.min(alreadyDrawnHeight, maxDropHeight);
		this.maxScroll = Math.max(0, alreadyDrawnHeight-getListHeight());
		this.scrollStep = !filteredEntries.isEmpty()?Math.max(1, alreadyDrawnHeight/filteredEntries.size()/entriesInGrid): 1;
		this.scroll = MathHelper.clamp(this.scroll, 0, maxScroll);
		return alreadyDrawnHeight;
	}

	@Override
	public void onGuiEvent(DecoGuiEvent event)
	{
		switch(event)
		{
			case COPY:
			{
				GuiScreen.setClipboardString(EasyNBT.newNBT()
						.withString("type", getSelectedEntry().getClass().toString())
						.withString("selected", getSelectedEntry().toString())
						.toString());
			}
			break;
			case PASTE:
			{
				EasyNBT nbt = EasyNBT.parseEasyNBT(GuiScreen.getClipboardString());
				if(nbt.hasKey("type", "selected"))
					for(T entry : this.entries)
						if(entry.toString().equals(nbt.getString("selected")))
						{
							selectedEntry = this.entries.indexOf(entry);
							break;
						}
			}
			break;
			default:
				super.onGuiEvent(event);
				break;
		}


	}

	@Override
	protected void draw(int mouseX, int mouseY, float partialTicks)
	{
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();
		IIColor buttonColor = enabled?(pressed?BACKGROUND_PRESSED: (hovered?BACKGROUND_HOVERED: BACKGROUND)): BACKGROUND_DISABLED;

		//Blinking search text cursor
		blinkTime = (blinkTime+1)%40;

		//Background
		DecoGuiUtils.drawRepeatedRect(draw, x, y, width, height, backgroundLocation, buttonColor, 32, 8);
		//Dropdown Symbol Background
		DecoGuiUtils.drawRepeatedRect(draw, x+width-12, y, 12, height, backgroundLocation, buttonColor, 32, 8);

		//Dropdown Symbol
		TextureAtlasSprite dropdownSymbol = ClientUtils.getSprite(dropdownSymbolLocation);
		int alignY = DecoAlignment.CENTER.getAlignY(y, 9, height);
		if(dropped)
			draw.drawTexColorRect(x+width-11, alignY, 9, 9, getTextColor(false),
					dropdownSymbol.getMinU(), dropdownSymbol.getInterpolatedU(9),
					dropdownSymbol.getInterpolatedV(7), dropdownSymbol.getMaxV());
		else
			draw.drawTexColorRect(x+width-11, alignY, 9, 9, getTextColor(false),
					dropdownSymbol.getInterpolatedU(7), dropdownSymbol.getMaxU(),
					dropdownSymbol.getMinV(), dropdownSymbol.getInterpolatedV(9));
		draw.finish();

		//Selected entry or search text
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		if(dropped&&!text.isEmpty())
		{
			String drawn = blinkTime > 20?(text+"_"): text;
			fontRenderer.drawString(drawn, 0, 0, getTextColor(false).getPackedRGB());
		}
		else
		{
			T entry = getSelectedEntry();
			if(entry!=null)
				display.displayElement(entry, width-12, fontRenderer, false);
		}
		GlStateManager.popMatrix();
	}

	@Override
	public void drawUpperLayer(int mouseX, int mouseY, float partialTicks)
	{
		if(dropped)
			drawList(x, y+height, dropdownWidth, mouseX, mouseY, partialTicks);
	}

	@Override
	public void cleanup()
	{

	}

	@Nullable
	public T getSelectedEntry()
	{
		return getEntry(selectedEntry);
	}

	@Nullable
	private T getEntry(int index)
	{
		if(index==-1)
			return null;
		return entries.get(MathHelper.clamp(index, 0, entries.size()-1));
	}

	@Deprecated
	public String getSelectedEntry(int selectedEntry)
	{
		return entries.get(MathHelper.clamp(selectedEntry, 0, entries.size()-1)).toString();
	}

	@Deprecated
	public boolean isDropped()
	{
		return dropped;
	}

	@Override
	protected boolean shouldAlwaysHaveScrollbar()
	{
		return false;
	}

	@Override
	protected int getListHeight()
	{
		return maxPossibleDropHeight;
	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+width, y+height+(dropped?maxDropHeight: 0), mouseX, mouseY);
	}
}
