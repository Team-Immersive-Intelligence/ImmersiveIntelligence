package pl.pabilo8.immersiveintelligence.client.gui.deco.component.button;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoElementDisplays;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoElementDisplays.DecoElementDisplay;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.DecoElementDisplays.DecoElementSorter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.GuiComponentDecoTextBase;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Pabilo8
 * @since 17.09.2021
 */
public class DecoDropdown<T> extends GuiComponentDecoTextBase<DecoDropdown<T>>
{
	private ResLoc dropdownSymbolLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_DROPDOWN;
	private ResLoc dropdownBackgroundLocation = IIReference.GUI_BG_DARK;
	private ResLoc dropdownScrollBarLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_SLIDER;

	public int selectedEntry = -1;
	private int selectingScrollOffset = 0;
	private int blinkTime = 0;
	private List<T> entries;
	private DecoElementDisplay<T> display = DecoElementDisplays.getDefaultDisplay();
	private DecoElementSorter<T> sorter = DecoElementDisplays.getDefaultSorter();
	private int entriesInGrid = 1;
	private int entryMaxWidth;
	private int maxDropHeight = 32, maxScroll = 0, averageScroll = fontRenderer.FONT_HEIGHT;
	private boolean dropped = false;

	public DecoDropdown(int x, int y)
	{
		super(x, y);
		this.backgroundLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_BUTTON;
		withSize(120, 12);
		withOnScroll((gui, scroll, mouseX, mouseY) -> {
			if(dropped)
			{
				selectingScrollOffset = MathHelper.clamp(selectingScrollOffset-(scroll*averageScroll), 0, maxScroll);
				return true;
			}
			return false;
		});
		withOnPressed((gui, mouseX, mouseY) -> {
			if(dropped)
			{
				int drawOffset = 0;
				int currentColumn = 0;
				List<T> entries = autocomplete();
				for(int entryID = 0; entryID < entries.size(); entryID++)
				{
					if(currentColumn >= entriesInGrid)
					{
						currentColumn = 0;
						drawOffset += display.displayElement(entries.get(entryID-entriesInGrid), entryMaxWidth, fontRenderer, true);
					}

					if(!display.isSelectable(entries.get(entryID)))
						continue;

					if(IIMath.isPointInRectangle(gui.x+2+(currentColumn*entryMaxWidth), gui.y-selectingScrollOffset+height+2+drawOffset,
							gui.x+2+(currentColumn*entryMaxWidth)+entryMaxWidth,
							gui.y-selectingScrollOffset+height+2+drawOffset+display.displayElement(entries.get(entryID), entryMaxWidth, fontRenderer, true),
							mouseX, mouseY))
					{
						selectedEntry = this.entries.indexOf(entries.get(entryID));
						text = "";
						dropped = false;
						return true;
					}

					currentColumn++;
				}
			}
			dropped = !dropped;
			pressed = false;
			return true;
		});
		//TODO: 01.02.2025 combine text field and other components typing logic
		withOnKeyTyped((gui, typedChar, keyCode) -> {
			//Should only work when dropped
			if(!dropped)
				return false;

			//Clipboard shortcuts
			if(keyCode==Keyboard.KEY_C&&Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			{
				GuiScreen.setClipboardString(EasyNBT.newNBT()
						.withString("type", getSelectedEntry().getClass().toString())
						.withString("selected", getSelectedEntry().toString())
						.toString());
				return true;
			}
			else if(keyCode==Keyboard.KEY_V&&Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			{
				EasyNBT nbt = EasyNBT.parseEasyNBT(GuiScreen.getClipboardString());
				if(nbt.hasKey("type", "selected"))
					for(T entry : this.entries)
						if(entry.toString().equals(nbt.getString("selected")))
						{
							selectedEntry = this.entries.indexOf(entry);
							return true;
						}
				return true;
			}

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
			else if(keyCode==Keyboard.KEY_ESCAPE)
			{
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

	@Deprecated
	public DecoDropdown(int buttonId, int x, int y, int w, int h, int perPage, T... entries)
	{
		this(x, y);
		withSize(w, h);
		withEntries(entries);

	}

	public DecoDropdown<T> withDisplayFunction(DecoElementDisplay<T> display)
	{
		this.display = display;
		return this;
	}

	public DecoDropdown<T> withSortFunction(DecoElementSorter<T> sorter)
	{
		this.sorter = sorter;
		return this;
	}

	public DecoDropdown<T> withEntries(ArrayList<T> entries)
	{
		this.entries = entries;
		this.entries = sorter.sort(this.entries);
		calculateSlideLength();
		return this;
	}

	public DecoDropdown<T> withEntries(T... entries)
	{
		return withEntries(new ArrayList<>(Arrays.asList(entries)));
	}

	public DecoDropdown<T> withEntriesInGrid(int entriesInGrid)
	{
		this.entriesInGrid = entriesInGrid;
		calculateSlideLength();
		return this;
	}

	public DecoDropdown<T> withSelectedEntry(int selectedEntry)
	{
		this.selectedEntry = selectedEntry;
		return this;
	}

	public DecoDropdown<T> withMaxDropHeight(int maxDropHeight)
	{
		this.maxDropHeight = maxDropHeight;
		return this;
	}

	@Override
	protected boolean initialize()
	{
		calculateSlideLength();
		return true;
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
		if(dropped)
			draw.drawTexColorRect(x+width-11, y+1, 9, 9, getTextColor(false),
					dropdownSymbol.getMinU(), dropdownSymbol.getInterpolatedU(9),
					dropdownSymbol.getInterpolatedV(7), dropdownSymbol.getMaxV());
		else
			draw.drawTexColorRect(x+width-11, y+1, 9, 9, getTextColor(false),
					dropdownSymbol.getInterpolatedU(7), dropdownSymbol.getMaxU(),
					dropdownSymbol.getMinV(), dropdownSymbol.getInterpolatedV(9));
		draw.finish();

		//Selected entry or search text
		GlStateManager.pushMatrix();
		GlStateManager.translate(x+2, y+2, 0);
		if(dropped&&!text.isEmpty())
		{
			String drawn = blinkTime > 20?(text+"_"): text;
			fontRenderer.drawString(drawn, 0, 0, getTextColor(false).getPackedRGB());
		}
		else
			display.displayElement(getSelectedEntry(), width-12, fontRenderer, false);
		GlStateManager.popMatrix();
	}

	@Override
	public void drawUpperLayer(int mouseX, int mouseY, float partialTicks)
	{
		if(!dropped)
			return;

		//Draw dropdown
		bindAtlas();
		IIDrawUtils draw = IIDrawUtils.startTexturedColored();

		//Background
		DecoGuiUtils.drawRepeatedRect(draw, x, y+height, width, maxDropHeight, dropdownBackgroundLocation, IIColor.WHITE, 32, 8);
		//Scrollbar
		if(maxScroll > 0)
		{
			TextureAtlasSprite scrollbarSprite = ClientUtils.getSprite(dropdownScrollBarLocation);
			//Scrollbar background
			draw.drawRepeatedColorRect(x+width-11, y+height,
					10, maxDropHeight,
					IIColor.WHITE, 10, 32,
					scrollbarSprite.getMinU(), scrollbarSprite.getInterpolatedU(5),
					scrollbarSprite.getMinV(), scrollbarSprite.getMaxV()
			);
			//Scrollbar
			int scrollBarHeight = Math.max(10, (int)((maxDropHeight/(float)(maxScroll+maxDropHeight))*maxDropHeight));
			int scrollbarOffset = (int)((selectingScrollOffset/(float)maxScroll)*(maxDropHeight-scrollBarHeight));
			draw.drawRepeatedColorRect(x+width-11, y+height+1+scrollbarOffset,
					10, scrollBarHeight,
					IIColor.WHITE, 10, 32,
					scrollbarSprite.getInterpolatedU(5), scrollbarSprite.getInterpolatedU(10),
					scrollbarSprite.getMinV(), scrollbarSprite.getMaxV()
			);
		}
		draw.finish();

		//Draw only a cutout of the elements
		GlStateManager.pushMatrix();
		scissor(x, y+height, width, maxDropHeight);
		GlStateManager.translate(0, -selectingScrollOffset, 0);

		//Filter entries based on search input
		List<T> filteredEntries = autocomplete();
		int alreadyDrawnHeight = 0;
		int currentColumn = 0;
		for(int i = 0; i < filteredEntries.size(); i++)
		{
			if(currentColumn >= entriesInGrid)
			{
				currentColumn = 0;
				alreadyDrawnHeight += display.displayElement(filteredEntries.get(i-entriesInGrid), width-12, fontRenderer, true);
			}

			GlStateManager.pushMatrix();
			GlStateManager.translate(x+2+(currentColumn*entryMaxWidth), y+height+2+alreadyDrawnHeight, 0);
			display.displayElement(filteredEntries.get(i), entryMaxWidth, fontRenderer, false);
			GlStateManager.popMatrix();

			currentColumn++;
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.popMatrix();
	}

	private void scissor(int x, int y, int xSize, int ySize)
	{
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		ScaledResolution res = new ScaledResolution(ClientUtils.mc());
		x = x*res.getScaleFactor();
		ySize = ySize*res.getScaleFactor();
		y = ClientUtils.mc().displayHeight-(y*res.getScaleFactor())-ySize;
		xSize = xSize*res.getScaleFactor();
		GL11.glScissor(x, y, xSize, ySize);
	}

	@Override
	public void cleanup()
	{

	}

	public T getSelectedEntry()
	{
		return entries.get(MathHelper.clamp(selectedEntry, 0, entries.size()-1));
	}

	@Deprecated
	public String getSelectedEntry(int selectedEntry)
	{
		return entries.get(MathHelper.clamp(selectedEntry, 0, entries.size()-1)).toString();
	}

	public boolean isDropped()
	{
		return dropped;
	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+width, y+height+(dropped?32: 0), mouseX, mouseY);
	}

	private List<T> autocomplete()
	{
		if(!text.isEmpty())
			return sorter.autocomplete(entries, text);
		return entries;
	}


	private int calculateSlideLength()
	{
		List<T> filteredEntries = autocomplete();
		int alreadyDrawnHeight = 0;
		//Calculate the height of the dropdown
		for(int i = 0; i < filteredEntries.size(); i += entriesInGrid)
			alreadyDrawnHeight += display.displayElement(filteredEntries.get(i), width-12, fontRenderer, true);

		//Calculate the width of the dropdown
		//If the height is more than the visible dropdown, account in space for a scrollbar
		this.entryMaxWidth = ((alreadyDrawnHeight > height)?(width-12): width)/entriesInGrid;
		this.maxScroll = Math.max(0, alreadyDrawnHeight-maxDropHeight);
		//Calculate the px scrolled per one unit of mouse scroll
		this.averageScroll = !filteredEntries.isEmpty()?Math.max(1, alreadyDrawnHeight/filteredEntries.size()): 1;
		return alreadyDrawnHeight;
	}
}
