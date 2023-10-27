package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.client.gui.elements.GuiButtonIE;
import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.gui.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiWidgetManualWrapper;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageDataVariables extends ManualPages
{
	List<DisplayEntry> entries = new ArrayList<>();
	boolean input;
	String title;
	int titleShift;

	public IIManualPageDataVariables(ManualInstance manual, String name, boolean input)
	{
		super(manual, name+(input?".vars_in": ".vars_out"));
		this.input = input;
	}

	public IIManualPageDataVariables addEntry(IDataType type, char... c)
	{
		if(c.length > 0)
			entries.add(new DisplayEntry(type, c, this.text+"."+c[0]+".main", this.text+"."+c[0]+".sub"));
		return this;
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		highlighted = ItemStack.EMPTY;

		boolean uni = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);

		title = I18n.format("ie.manual.entry."+(input?"variables_input": "variables_output"));
		titleShift = 55-(manual.fontRenderer.getStringWidth(title)/2);

		int spacing = 15;
		for(DisplayEntry entry : entries)
		{
			entry.init(manual, gui, x, y+spacing, pageButtons);
			pageButtons.add(new GuiButtonDatatype(pageButtons.size(), x-4, y+spacing-3, entry));
			spacing += entry.getSpacing();
		}

		manual.fontRenderer.setUnicodeFlag(uni);

	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{
		ManualUtils.drawSplitString(manual.fontRenderer, String.valueOf(TextFormatting.BOLD)+TextFormatting.UNDERLINE+title, x+titleShift, y, 120, manual.getTextColour());

		DisplayEntry tooltip = null;
		int down = 15;
		for(DisplayEntry entry : entries)
		{
			GlStateManager.enableBlend();
			GlStateManager.color(0.9f, 0.9f, 0.85f, 0.85f);
			ClientUtils.bindTexture(entry.dataType.textureLocation());
			Gui.drawModalRectWithCustomSizedTexture(x-4, y+down-3, 0, 0, 16, 16, 16, 16);

			if(IIUtils.isPointInRectangle(x-4, y+down-3, x+12, y+down+13, mx, my))
				tooltip = entry;

			char toDraw = entry.c[(int)Math.ceil((gui.mc.world.getTotalWorldTime()%entry.prezTimeTotal)/20)];

			GlStateManager.pushMatrix();
			GlStateManager.scale(1.25, 1.25, 1.25);
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+String.valueOf(toDraw), (int)((x+15)/1.25f), (int)((y+down-1)/1.25f), 100, entry.dataType.getTypeColour());
			GlStateManager.popMatrix();

			GlStateManager.disableBlend();
			ManualUtils.drawSplitString(manual.fontRenderer, entry.localizedText, x+24, y+down, 100, manual.getTextColour());
			down += entry.spacingMain;
			ManualUtils.drawSplitString(manual.fontRenderer, entry.localizedSubText, x, y+down, 120, manual.getTextColour());
			down += entry.spacingSub;

		}

		manual.fontRenderer.setUnicodeFlag(false);
		if(tooltip!=null)
		{
			ArrayList<String> toDraw = new ArrayList<>();
			toDraw.add(I18n.format(IIReference.DATA_KEY+"datatype."+tooltip.dataType.getName()));
			ClientUtils.drawHoveringText(toDraw, mx, my, manual.fontRenderer, -1, -1);
		}
		manual.fontRenderer.setUnicodeFlag(true);

	}

	@Override
	public void buttonPressed(GuiManual gui, GuiButton button)
	{
		super.buttonPressed(gui, button);
		if(button instanceof GuiButtonDatatype)
		{
			DisplayEntry entry = ((GuiButtonDatatype)button).entry;
			if(Minecraft.getMinecraft().currentScreen instanceof IDataMachineGui)
			{
				((IDataMachineGui)Minecraft.getMinecraft().currentScreen).editVariable(entry.c[0], new DataPacket().getVarInType(entry.dataType.getClass(), new DataTypeNull()));
			}
		}
	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		return false;
	}


	public static class DisplayEntry
	{
		IDataType dataType;
		char[] c;
		int prezTimeTotal;
		String text, subtext;
		String localizedText, localizedSubText;
		int spacingMain;
		int spacingSub;

		public DisplayEntry(IDataType dataType, char[] c, String text, String subtext)
		{
			this.dataType = dataType;
			this.c = c;
			this.text = text;
			this.subtext = subtext;
		}

		public void init(ManualInstance manual, GuiManual gui, int x, int y, List<GuiButton> pageButtons)
		{
			this.prezTimeTotal = c.length*20;

			this.localizedText = manual.formatText(text);
			this.localizedSubText = TextFormatting.ITALIC+manual.formatText(subtext);

			this.localizedText = addLinks(manual, gui, this.localizedText, x+24, y, 80, pageButtons);
			this.spacingMain = manual.fontRenderer.getWordWrappedHeight(this.localizedText, 120)+4;
			if(this.localizedText==null)
				this.localizedText = "";

			this.localizedSubText = addLinks(manual, gui, this.localizedSubText, x, y+spacingMain, 100, pageButtons);
			this.spacingSub = manual.fontRenderer.getWordWrappedHeight(this.localizedSubText, 160);
			if(this.localizedSubText==null)
				this.localizedSubText = "";

			if(this.localizedSubText.equals(TextFormatting.ITALIC.toString()))
				this.spacingSub = 4;
			else
				this.spacingSub += 10;
		}

		public int getSpacing()
		{
			return spacingMain+spacingSub;
		}
	}

	public static class GuiButtonDatatype extends GuiButtonIE
	{
		@Nonnull
		public final DisplayEntry entry;

		public GuiButtonDatatype(int buttonId, int x, int y, DisplayEntry entry)
		{
			super(100+buttonId, x, y, 16, 16, "",
					String.format("immersiveintelligence:textures/gui/data_types/%s.png", entry.dataType.getName())
					, 0, 0);
			this.entry = entry;
			this.enabled = ManualHelper.getManual().getGui() instanceof GuiWidgetManualWrapper;
		}
	}
}
