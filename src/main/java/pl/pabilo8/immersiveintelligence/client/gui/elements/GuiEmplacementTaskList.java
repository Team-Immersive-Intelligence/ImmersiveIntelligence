package pl.pabilo8.immersiveintelligence.client.gui.elements;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.gui.block.emplacement.GuiEmplacementPageTasks.TaskFilter;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

/**
 * @author Pabilo8
 * @since 19.07.2021
 */
public class GuiEmplacementTaskList extends GuiButton
{
	private final TaskFilter[] entries;
	private final int[] padding = {0, 0, 0, 0};
	private boolean needsSlider = false;
	private int perPage;
	private float textScale = 1;
	private boolean unicode = false;

	private int offset;
	private int maxOffset;

	private long prevWheelNano = 0;
	private int targetEntry = -1;
	private int hoverTimer = 0;

	public GuiEmplacementTaskList(int id, int x, int y, int w, int h, TaskFilter[] entries)
	{
		super(id, x, y, w, h, "");
		this.entries = entries;
		recalculateEntries();
	}

	private void recalculateEntries()
	{
		perPage = (int)((this.height-padding[0]-padding[1])/(ClientUtils.mc().fontRenderer.FONT_HEIGHT*textScale));
		if(perPage < entries.length)
		{
			needsSlider = true;
			maxOffset = entries.length-perPage;
		}
		else
			needsSlider = false;
	}

	public GuiEmplacementTaskList setPadding(int up, int down, int left, int right)
	{
		this.padding[0] = up;
		this.padding[1] = down;
		this.padding[2] = left;
		this.padding[3] = right;
		recalculateEntries();
		return this;
	}

	public GuiEmplacementTaskList setFormatting(float textScale, boolean unicode)
	{
		this.textScale = textScale;
		this.unicode = unicode;
		this.recalculateEntries();
		return this;
	}

	public int getOffset()
	{
		return this.offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getMaxOffset()
	{
		return this.maxOffset;
	}

	/**
	 * Draws this button to the screen.
	 */
	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		FontRenderer fr = ClientUtils.mc().fontRenderer;
		boolean uni = fr.getUnicodeFlag();
		fr.setUnicodeFlag(unicode);

		int mmY = my-this.y;
		int strWidth = width-padding[2]-padding[3]-(needsSlider?6: 0);
		GlStateManager.color(1, 1, 1);

		ClientUtils.bindTexture(ImmersiveIntelligence.MODID+":textures/gui/emplacement_icons.png");
		float silderShift = (height-14)/(float)maxOffset*offset;
		this.drawTexturedModalRect(x+width+2, y+silderShift-1, 153, 101, 9, 14);

		GlStateManager.scale(textScale, textScale, 1);
		this.hovered = mx >= x&&mx < x+width&&my >= y&&my < y+height;
		boolean hasTarget = false;
		for(int i = 0; i < Math.min(perPage, entries.length); i++)
		{
			int j = offset+i;
			int col = selectedOption==i?Lib.COLOUR_I_ImmersiveOrange:0xE0E0E0;
			boolean selectionHover = hovered&&mmY >= i*fr.FONT_HEIGHT&&mmY < (i+1)*fr.FONT_HEIGHT;
			if(selectionHover)
			{
				hasTarget = true;
				if(targetEntry!=j)
				{
					targetEntry = j;
					hoverTimer = 0;
				}
				else
					hoverTimer++;
			}

			if(j > entries.length-1)
				j = entries.length-1;
			String s = I18n.format(IIReference.DESCRIPTION_KEY+"metal_multiblock1.emplacement.target."+entries[j].type.getName());
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
			float tx = ((x+padding[2])/textScale);
			float ty = ((y+padding[0]+(fr.FONT_HEIGHT*i))/textScale);
			GlStateManager.translate(tx, ty, 0);
			fr.drawString(s, 0, 0, col, false);
			GlStateManager.translate(-tx, -ty, 0);
		}
		GlStateManager.scale(1/textScale, 1/textScale, 1);
		if(!hasTarget)
		{
			targetEntry = -1;
			hoverTimer = 0;
		}

		fr.setUnicodeFlag(uni);

		//Handle DWheel
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

	public int selectedOption = -1;

	/**
	 * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent e).
	 */
	@Override
	public boolean mousePressed(Minecraft mc, int mx, int my)
	{
		boolean b = super.mousePressed(mc, mx, my);
		FontRenderer fr = ClientUtils.mc().fontRenderer;
		if(isMouseOver())
			selectedOption = -1;

		if(b)
		{
			int mmY = my-this.y;
			for(int i = 0; i < Math.min(perPage, entries.length); i++)
				if(mmY >= i*fr.FONT_HEIGHT&&mmY < (i+1)*fr.FONT_HEIGHT)
					selectedOption = offset+i;
		}
		return b;
	}
}
