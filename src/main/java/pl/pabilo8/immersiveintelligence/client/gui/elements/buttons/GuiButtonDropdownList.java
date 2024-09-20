package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 17.09.2021
 */
public class GuiButtonDropdownList extends GuiButton
{
	private boolean needsSlider;
	public boolean dropped = false;
	private final int perPage;
	private int offset;
	private int maxOffset;

	private Function<String, String> translationFunction;
	private final String[] entries;

	public int selectedEntry = -1;
	private int hoveredEntry = -1;
	private int hoverTimer = 0;
	private long prevWheelNano = 0;

	public GuiButtonDropdownList(int buttonId, int x, int y, int w, int h, int perPage, String... entries)
	{
		super(buttonId, x, y, w, h, "");
		this.perPage = perPage;
		this.entries = entries;
		recalculateEntries();
	}

	public GuiButtonDropdownList setTranslationFunc(Function<String, String> func)
	{
		this.translationFunction = func;
		return this;
	}

	public void recalculateEntries()
	{
		if(perPage < entries.length)
		{
			needsSlider = true;
			maxOffset = entries.length-perPage;
		}
		else
			needsSlider = false;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		FontRenderer fr = IIClientUtils.fontRegular;
		if(!this.visible)
			return;

		if(!this.enabled)
			GlStateManager.color(0.5f, 0.5f, 0.5f, 1);
		else
			GlStateManager.color(1, 1, 1, 1);
		ClientUtils.bindTexture("immersiveintelligence:textures/gui/emplacement_icons.png");

		final int firstX = Math.min(width, 12);
		this.drawTexturedModalRect(x-1, y-1, 163, 103, firstX, 12);
		this.drawTexturedModalRect(x+1+Math.max(width-12, 0), y-1, 215, 103, Math.min(width, 12), 12);
		for(int i = 0; i < Math.max(width+2-24, 0); i += 16)
			this.drawTexturedModalRect(x-1+firstX+i, y-1, (i%32) > 16?207: 175, 103, MathHelper.clamp(width+2-24-i, 0, 16), 12);

		GlStateManager.pushMatrix();
		GlStateManager.enableDepth();
		GlStateManager.translate(0, 0, 1);

		if(dropped)
		{
			int yDropDown = y+2+fr.FONT_HEIGHT;
			int mmY = my-yDropDown;
			int strWidth = width-(needsSlider?6: 0);
			int hh = Math.min(perPage, entries.length)*fr.FONT_HEIGHT;
			//drawRect(x, yDropDown, x+width, yDropDown+hh, 0xff000000);

			for(int hpos = 12; hpos < hh+12; hpos += fr.FONT_HEIGHT)
			{
				this.drawTexturedModalRect(x-1, y-1+hpos, 163, 127, firstX, fr.FONT_HEIGHT);
				this.drawTexturedModalRect(x+1+Math.max(width-12, 0), y-1+hpos, 215, 127, Math.min(width, 12), fr.FONT_HEIGHT);
				for(int i = 0; i < Math.max(width+2-24, 0); i += 16)
					this.drawTexturedModalRect(x-1+firstX+i, y-1+hpos, (i%32) > 16?207: 175, 127, MathHelper.clamp(width+2-24-i, 0, 16), fr.FONT_HEIGHT);
			}

			this.drawTexturedModalRect(x-1, y-1+12+hh, 163, 139, firstX, 3);
			this.drawTexturedModalRect(x+1+Math.max(width-12, 0), y-1+12+hh, 215, 139, Math.min(width, 12), 3);
			for(int i = 0; i < Math.max(width+2-24, 0); i += 16)
				this.drawTexturedModalRect(x-1+firstX+i, y-1+12+hh, (i%32) > 16?207: 175, 139, MathHelper.clamp(width+2-24-i, 0, 16), 3);

			//slider
			if(needsSlider)
			{
				//136 - 103
				this.drawTexturedModalRect(x+width-6, yDropDown, 227, 103, 6, 4);
				this.drawTexturedModalRect(x+width-6, yDropDown+hh-4, 227, 111, 6, 4);
				for(int i = 0; i < hh-8; i += 2)
					this.drawTexturedModalRect(x+width-6, yDropDown+4+i, 227, 108, 6, 2);

				int sliderSize = Math.max(6, hh-maxOffset*fr.FONT_HEIGHT);
				float silderShift = (hh-sliderSize)/(float)maxOffset*offset;

				this.drawTexturedModalRect(x+width-5, yDropDown+silderShift+1, 227, 115, 4, 2);
				this.drawTexturedModalRect(x+width-5, yDropDown+silderShift+sliderSize-4, 227, 118, 4, 3);
				for(int i = 0; i < sliderSize-7; i++)
					this.drawTexturedModalRect(x+width-5, yDropDown+silderShift+3+i, 227, 117, 4, 1);
			}

			this.hovered = mx >= x&&mx < x+width&&my >= yDropDown&&my < yDropDown+hh;
			boolean hasTarget = false;
			for(int i = 0; i < Math.min(perPage, entries.length); i++)
			{
				int j = offset+i;
				int col = 0xE0E0E0;
				boolean selectionHover = hovered&&mmY >= i*fr.FONT_HEIGHT&&mmY < (i+1)*fr.FONT_HEIGHT;
				if(selectionHover)
				{
					hasTarget = true;
					if(hoveredEntry!=j)
					{
						hoveredEntry = j;
						hoverTimer = 0;
					}
					else
						hoverTimer++;
					col = Lib.COLOUR_I_ImmersiveOrange;
				}
				if(j > entries.length-1)
					j = entries.length-1;
				String s = translationFunction!=null?translationFunction.apply(entries[j]): entries[j];
				//Thanks, Blu!
				int overLength = s.length()-fr.sizeStringToWidth(s, strWidth);
				if(overLength > 0)//String is too long
				{
					if(selectionHover&&hoverTimer > 20)
					{
						int textOffset = (hoverTimer/10)%(s.length());
						s = s.substring(textOffset)+" "+s.substring(0, textOffset);
					}
					s = fr.trimStringToWidth(s, strWidth);
				}
				float tx = x;
				float ty = yDropDown+(fr.FONT_HEIGHT*i)+1;
				GlStateManager.translate(tx, ty, 0);
				fr.drawString(s, 0, 0, col, false);
				GlStateManager.translate(-tx, -ty, 0);
			}
			GlStateManager.scale(1, 1, 1);
			if(!hasTarget)
			{
				hoveredEntry = -1;
				hoverTimer = 0;
			}

			if(IIMath.isPointInRectangle(x, yDropDown, x+width, yDropDown+hh, mx, my))
				handleWheel();
		}

		if(selectedEntry!=-1)
		{
			String text = translationFunction!=null?translationFunction.apply(entries[selectedEntry]): entries[selectedEntry];
			int maxW = width-2-12;

			fr.drawString(fr.trimStringToWidth(text, maxW), x+1, y+1, dropped?Lib.COLOUR_I_ImmersiveOrange: (enabled?0xE0E0E0: 0x202020), false);
		}
		boolean flag = fr.getUnicodeFlag();
		fr.setUnicodeFlag(true);
		fr.drawString(dropped?"▼": "▶", x+0.5f+width-7, y+1, dropped?Lib.COLOUR_I_ImmersiveOrange: (enabled?0xE0E0E0: 0x202020), false);
		fr.setUnicodeFlag(flag);
		GlStateManager.popMatrix();

	}

	private void handleWheel()
	{
		int mouseWheel = Mouse.getEventDWheel();
		if(mouseWheel!=0&&maxOffset > 0&&Mouse.getEventNanoseconds()!=prevWheelNano)
		{
			prevWheelNano = Mouse.getEventNanoseconds();
			if(mouseWheel < 0&&offset < maxOffset)
				offset++;
			if(mouseWheel > 0&&offset > 0)
				offset--;
		}
	}

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mx, int my)
	{
		if(!(this.enabled&&this.visible))
			return false;

		if(dropped)
		{
			FontRenderer fr = ClientUtils.mc().fontRenderer;
			int yDropDown = y+2+fr.FONT_HEIGHT;
			int hh = Math.min(perPage, entries.length)*fr.FONT_HEIGHT;
			boolean b = IIMath.isPointInRectangle(x, yDropDown, x+width, yDropDown+hh, mx, my);
			if(b)
			{
				int mmY = my-yDropDown;
				for(int i = 0; i < Math.min(perPage, entries.length); i++)
					if(mmY >= i*fr.FONT_HEIGHT&&mmY < (i+1)*fr.FONT_HEIGHT)
					{
						selectedEntry = offset+i;
						dropped = false;
					}
			}
			else
				dropped = false;
			return selectedEntry!=-1;
		}
		else
		{
			return this.dropped = IIMath.isPointInRectangle(x, y, x+width, y+height, mx, my);
		}
	}

	public String getEntry(int selectedEntry)
	{
		return entries[MathHelper.clamp(selectedEntry, 0, entries.length-1)];
	}
}
