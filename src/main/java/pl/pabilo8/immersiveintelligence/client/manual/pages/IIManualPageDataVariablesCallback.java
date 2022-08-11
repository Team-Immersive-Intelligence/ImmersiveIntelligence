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
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.client.IDataMachineGui;
import pl.pabilo8.immersiveintelligence.client.gui.elements.GuiWidgetManualWrapper;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 07.08.2021
 */
public class IIManualPageDataVariablesCallback extends ManualPages
{
	List<CallbackEntry> entries = new ArrayList<>();
	String title;
	int titleShift;

	public IIManualPageDataVariablesCallback(ManualInstance manual, String name)
	{
		super(manual, name+".vars_callback");
	}

	public IIManualPageDataVariablesCallback addEntry(IDataType type, String... c)
	{
		if(c.length > 0)
			entries.add(new CallbackEntry(type, c, this.text+"."+c[0]));
		return this;
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		highlighted = ItemStack.EMPTY;

		boolean uni = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);

		title = I18n.format("ie.manual.entry.variables_callback");
		titleShift = 55-(manual.fontRenderer.getStringWidth(title)/2);

		int spacing = 15;
		for(CallbackEntry entry : entries)
		{
			entry.init(manual, gui, x, y+spacing, pageButtons);
			pageButtons.add(new GuiButtonDataCallback(pageButtons.size(), x-4, y+spacing-3, entry));
			spacing += entry.getSpacing();
		}

		manual.fontRenderer.setUnicodeFlag(uni);

	}

	@Override
	public void buttonPressed(GuiManual gui, GuiButton button)
	{
		super.buttonPressed(gui, button);
		if(button instanceof GuiButtonDataCallback)
		{
			CallbackEntry entry = ((GuiButtonDataCallback)button).entry;
			if(Minecraft.getMinecraft().currentScreen instanceof IDataMachineGui)
			{
				((IDataMachineGui)Minecraft.getMinecraft().currentScreen).editVariable('c', new DataTypeString(entry.n[0]));
			}
		}
	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{
		ManualUtils.drawSplitString(manual.fontRenderer, String.valueOf(TextFormatting.BOLD)+TextFormatting.UNDERLINE+title, x+titleShift, y, 120, manual.getTextColour());

		CallbackEntry tooltip = null;
		int down = 15;
		for(CallbackEntry entry : entries)
		{
			GlStateManager.enableBlend();
			GlStateManager.color(0.9f, 0.9f, 0.85f, 0.85f);
			ClientUtils.bindTexture(entry.dataType.textureLocation());
			Gui.drawModalRectWithCustomSizedTexture(x-4, y+down-3, 0, 0, 16, 16, 16, 16);

			if(Utils.isPointInRectangle(x-4, y+down-3, x+12, y+down+13, mx, my))
				tooltip = entry;

			String toDraw = entry.n[(int)Math.ceil((gui.mc.world.getTotalWorldTime()%entry.prezTimeTotal)/20)];

			GlStateManager.pushMatrix();
			GlStateManager.scale(1.25, 1.25, 1.25);
			ManualUtils.drawSplitString(manual.fontRenderer, TextFormatting.BOLD+toDraw, (int)((x+15)/1.25f), (int)((y+down-1)/1.25f), 100, entry.dataType.getTypeColour());
			GlStateManager.popMatrix();

			GlStateManager.disableBlend();
			down += manual.fontRenderer.FONT_HEIGHT;
			ManualUtils.drawSplitString(manual.fontRenderer, entry.localizedText, x+24, y+down, 100, manual.getTextColour());
			down += entry.spacingMain;
		}

		manual.fontRenderer.setUnicodeFlag(false);
		if(tooltip!=null)
		{
			ArrayList<String> toDraw = new ArrayList<>();
			toDraw.add(I18n.format(CommonProxy.DATA_KEY+"datatype."+tooltip.dataType.getName()));
			ClientUtils.drawHoveringText(toDraw, mx, my, manual.fontRenderer, -1, -1);
		}
		manual.fontRenderer.setUnicodeFlag(true);

	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		return false;
	}


	public static class CallbackEntry
	{
		IDataType dataType;
		String[] n;
		int prezTimeTotal;
		String text;
		String localizedText;
		int spacingMain;

		public CallbackEntry(IDataType dataType, String[] n, String text)
		{
			this.dataType = dataType;
			this.n = n;
			this.text = text;
		}

		public void init(ManualInstance manual, GuiManual gui, int x, int y, List<GuiButton> pageButtons)
		{
			this.prezTimeTotal = n.length*20;
			this.localizedText = manual.formatText(text);
			this.localizedText = addLinks(manual, gui, this.localizedText, x+24, y, 80, pageButtons);
			this.spacingMain = manual.fontRenderer.getWordWrappedHeight(this.localizedText, 120)+4;

		}

		public int getSpacing()
		{
			return spacingMain;
		}
	}

	public static class GuiButtonDataCallback extends GuiButtonIE
	{
		@Nonnull
		public final CallbackEntry entry;

		public GuiButtonDataCallback(int buttonId, int x, int y, CallbackEntry entry)
		{
			super(100+buttonId, x, y, 16, 16, "",
					String.format("immersiveintelligence:textures/gui/data_types/%s.png", entry.dataType.getName())
					, 0, 0);
			this.entry = entry;
			this.enabled = ManualHelper.getManual().getGui() instanceof GuiWidgetManualWrapper;
		}
	}
}
