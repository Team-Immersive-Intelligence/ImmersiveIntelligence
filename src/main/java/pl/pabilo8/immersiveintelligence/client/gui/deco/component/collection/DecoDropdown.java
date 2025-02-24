package pl.pabilo8.immersiveintelligence.client.gui.deco.component.collection;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Keyboard;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoGuiUtils;
import pl.pabilo8.immersiveintelligence.client.util.IIDrawUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.List;

/**
 * @author Pabilo8
 * @since 17.09.2021
 */
public class DecoDropdown<T> extends DecoScrolledCollection<DecoDropdown<T>, T>
{
	private final ResLoc dropdownSymbolLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_DROPDOWN;

	public int selectedEntry = -1;
	private int blinkTime = 0;
	private int maxDropHeight = 32;
	private boolean dropped = false;

	public DecoDropdown(int x, int y)
	{
		super(x, y);
		this.backgroundLocation = IIReference.RES_TEXTURES_DECO_COMPONENT_BUTTON;
		withSize(120, 12);
		withOnPressed((gui, mouseX, mouseY) -> {
			if(dropped)
			{
				Tuple<Integer, Integer> clicked = getClickedEntryIndex(gui.x+2, gui.y-scroll+height+2, mouseX, mouseY);
				if(clicked!=null)
				{
					if(clicked.getFirst()==ON_CREATE_OPTION)
						gui.runCreateAction();
					else
						selectedEntry = clicked.getFirst();
					text = "";
					dropped = false;
					return true;
				}
			}
			dropped = !dropped;
			pressed = false;
			return true;
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

	@Override
	protected int getAddButtonHeight()
	{
		return 16;
	}

	@Deprecated
	public DecoDropdown(int buttonId, int x, int y, int w, int h, int perPage, T... entries)
	{
		this(x, y);
		withSize(w, h);
		withEntries(entries);

	}

	public DecoDropdown<T> withSelectedEntry(int selectedEntry)
	{
		this.selectedEntry = selectedEntry;
		return this;
	}

	public DecoDropdown<T> withSelectedEntry(T selectedEntry)
	{
		this.selectedEntry = entries.indexOf(selectedEntry);
		return this;
	}

	public DecoDropdown<T> withMaxDropHeight(int maxDropHeight)
	{
		this.maxDropHeight = maxDropHeight;
		return this;
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
		if(dropped)
			drawList(x, y+height, mouseX, mouseY, partialTicks);
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
	protected boolean shouldAlwaysHaveScrollbar()
	{
		return false;
	}

	@Override
	protected int getListHeight()
	{
		return maxDropHeight;
	}

	@Override
	protected boolean canBeClicked(int mouseX, int mouseY)
	{
		return IIMath.isPointInRectangle(x, y, x+width, y+height+(dropped?32: 0), mouseX, mouseY);
	}
}
