package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.lib.manual.ManualPages;
import blusunrize.lib.manual.ManualUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.List;

/**
 * A text object used instead of rendering a wall of text, like in default IE manual.
 *
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManuaRegularText extends IIManualObject
{
	private String text;

	//--- Setup ---//

	public IIManuaRegularText(ManualObjectInfo info, String text, List<GuiButton> linkCallback)
	{
		super(info, EasyNBT.newNBT());
		this.text = ManualPages.addLinks(gui.getManual(), gui, manual.formatText(text), x, y, width, linkCallback);
		if(this.text.endsWith("\n"))
			this.text = this.text.substring(0, this.text.length()-1);

		FontRenderer fontRenderer = manual.fontRenderer;
		boolean unicodeFlag = fontRenderer.getUnicodeFlag();

		fontRenderer.setUnicodeFlag(true);
		this.height = fontRenderer.getWordWrappedHeight(this.text, width);
		fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		FontRenderer fontRenderer = manual.fontRenderer;
		boolean unicodeFlag = manual.fontRenderer.getUnicodeFlag();

		fontRenderer.setUnicodeFlag(true);
		ManualUtils.drawSplitString(manual.fontRenderer, text, x, y, width, manual.getTextColour());
		fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return null;
	}
}
