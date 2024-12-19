package pl.pabilo8.immersiveintelligence.client.gui.elements.buttons;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * @author Pabilo8
 * @since 18.09.2021
 * @author Avalon
 * @since 19.12.2024
 */
public class GuiButtonDataLetterListLow extends GuiButton
{
	private final ArrowsAlignment arrows;
	@Nullable
	private final GuiButtonIE buttonNext, buttonPrev;

	public boolean dropped = false;
	public final boolean hasEmpty;

	public char selectedEntry;
	private char hoveredEntry;
	@Nullable
	private Supplier<DataPacket> avoidGetter;

	public GuiButtonDataLetterListLow(int buttonId, int x, int y, boolean hasEmpty, char currentValue, ArrowsAlignment arrows)
	{
		super(buttonId, x, y, arrows==ArrowsAlignment.NONE?18: 28, 18, "");
		this.hasEmpty = hasEmpty;
		selectedEntry = currentValue;
		hoveredEntry = currentValue;
		this.arrows = arrows;

		switch(arrows)
		{
			case LEFT:
			{
				this.buttonNext = new GuiButtonIE(0, x+1, y+2, 8, 6, "",
						ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77)
						.setHoverOffset(8, 0);
				this.buttonPrev = new GuiButtonIE(1, x+1, y+10, 8, 6, "",
						ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77+6)
						.setHoverOffset(8, 0);
			}
			break;
			case RIGHT:
			{
				this.buttonNext = new GuiButtonIE(0, x+width-9, y+2, 8, 6, "",
						ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77)
						.setHoverOffset(8, 0);
				this.buttonPrev = new GuiButtonIE(1, x+width-9, y+10, 8, 6, "",
						ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png", 128, 77+6)
						.setHoverOffset(8, 0);
			}
			break;
			default:
			case NONE:
			{
				buttonNext = null;
				buttonPrev = null;
				break;
			}
		}

	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
	{
		FontRenderer fr = ClientUtils.mc().fontRenderer;
		if(!this.visible)
			return;
		if(!this.enabled)
			GlStateManager.color(0.5f, 0.5f, 0.5f, 1);
		else
			GlStateManager.color(1, 1, 1, 1);
		ClientUtils.bindTexture("immersiveintelligence:textures/gui/emplacement_icons.png");

		if(buttonNext!=null)
			buttonNext.drawButton(mc, mouseX, mouseY, partialTicks);
		if(buttonPrev!=null)
			buttonPrev.drawButton(mc, mouseX, mouseY, partialTicks);

		int xx = arrows==ArrowsAlignment.LEFT?x+10: x;

		//frame
		this.drawTexturedModalRect(xx, y, 163, 142, 18, 18);

		if(dropped)
		{
			GlStateManager.pushMatrix();
			GlStateManager.enableDepth();
			GlStateManager.translate(0,0,1);

			this.drawTexturedModalRect(xx, y+height, 56, 115, 96, 32);
			this.drawTexturedModalRect(xx, y+height+32, 56, 195-4, 96, 20);

			if(IIMath.isPointInRectangle(xx+4, y+height+4, xx+4+10*fr.FONT_HEIGHT, y+height+52+4, mouseX, mouseY))
			{
				int hlX = ((int)Math.floor((mouseX-(xx+4))/(float)fr.FONT_HEIGHT));
				int hlY = ((int)Math.floor((mouseY-(y+height+4))/(float)12));

				int i = hlX+(10*hlY);
				if(i >= 0&&i < DataPacket.varCharacters.length)
					hoveredEntry = DataPacket.varCharacters[i];
				else
					hoveredEntry = '/';
			}
			else
			{
				if(buttonNext!=null&&buttonNext.isMouseOver())
					hoveredEntry = avoidGetter!=null?
							IIUtils.cyclePacketCharsAvoiding(selectedEntry, true, hasEmpty, avoidGetter.get()):
							IIUtils.cycleDataPacketChars(selectedEntry, true, hasEmpty);
				else if(buttonPrev!=null&&buttonPrev.isMouseOver())
					hoveredEntry = avoidGetter!=null?
							IIUtils.cyclePacketCharsAvoiding(selectedEntry, false, hasEmpty, avoidGetter.get()):
							IIUtils.cycleDataPacketChars(selectedEntry, false, hasEmpty);
				else
					hoveredEntry = '/';
			}

			int cx = 0, cy = 0;
			for(char c : hasEmpty?ArrayUtils.add(DataPacket.varCharacters, ' '): DataPacket.varCharacters)
			{
				this.drawTexturedModalRect(xx+1+cx*fr.FONT_HEIGHT, y+height+2+cy*12, 144, 89, 12, 12);
				cx++;
				if(cx > 9)
				{
					cy++;
					cx = 0;
				}
			}

			cx = 0;
			cy = 0;
			DataPacket pack = avoidGetter!=null?avoidGetter.get():null;
			for(char c : hasEmpty?ArrayUtils.add(DataPacket.varCharacters, ' '): DataPacket.varCharacters)
			{
				fr.drawString(String.valueOf(c==' '?'_': c), xx+4+cx*fr.FONT_HEIGHT, y+height+4+cy*12,
						c==selectedEntry?Lib.COLOUR_I_ImmersiveOrange: ((pack!=null&&pack.hasVariable(c))?0x202020:(c==hoveredEntry?Lib.colour_nixieTubeText: 0xE0E0E0))
				);
				cx++;
				if(cx > 9)
				{
					cy++;
					cx = 0;
				}
			}
			GlStateManager.popMatrix();
		}

		fr.drawString(String.valueOf(selectedEntry==' '?'_': selectedEntry), xx+9-(int)Math.floor(fr.getCharWidth(selectedEntry==' '?'_': selectedEntry)/2f), y+5, Lib.COLOUR_I_ImmersiveOrange, true);

	}

	public boolean keyTyped(char typedChar, int keyCode)
	{
		if(dropped)
		{
			if((hasEmpty&&typedChar==' ')||ArrayUtils.contains(DataPacket.varCharacters, typedChar))
			{
				this.selectedEntry = typedChar;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mx, int my)
	{
		if(!(this.enabled&&this.visible))
			return false;

		if(buttonNext!=null&&buttonNext.mousePressed(mc, mx, my))
		{
			selectedEntry = avoidGetter!=null?
					IIUtils.cyclePacketCharsAvoiding(selectedEntry, true, hasEmpty, avoidGetter.get()):
					IIUtils.cycleDataPacketChars(selectedEntry, true, hasEmpty);
			return true;
		}
		else if(buttonPrev!=null&&buttonPrev.mousePressed(mc, mx, my))
		{
			selectedEntry = avoidGetter!=null?
					IIUtils.cyclePacketCharsAvoiding(selectedEntry, false, hasEmpty, avoidGetter.get()):
					IIUtils.cycleDataPacketChars(selectedEntry, false, hasEmpty);
			return true;
		}
		else if(dropped)
		{
			FontRenderer fr = ClientUtils.mc().fontRenderer;
			int xx = arrows==ArrowsAlignment.LEFT?x+10: x;
			if(IIMath.isPointInRectangle(xx+4, y+height+4, xx+4+10*fr.FONT_HEIGHT, y+height+52, mx, my))
			{
				if(hoveredEntry!='/')
				{
					selectedEntry = hoveredEntry;
					dropped = false;
				}
			}
			else
				dropped = false;

			int yDropDown = y+2+fr.FONT_HEIGHT;
			/*
			int hh = Math.min(perPage, entries.length)*fr.FONT_HEIGHT;
			boolean b = Utils.isPointInRectangle(x, yDropDown, x+width, yDropDown+hh, mx, my);
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
			 */
			return true;
		}
		else
		{
			return this.dropped = IIMath.isPointInRectangle(x, y, x+width, y+height, mx, my);
		}
	}

	public void setAvoidGetter(Supplier<DataPacket> avoidGetter)
	{
		this.avoidGetter = avoidGetter;
	}

	/**
	 * Determines on which side the arrows will be placed (if NONE, no arrows will be displayed)
	 */
	public enum ArrowsAlignment
	{
		LEFT,
		RIGHT,
		NONE
	}
}
