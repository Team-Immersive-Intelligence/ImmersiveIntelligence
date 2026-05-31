package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.POLHighlighter;
import pl.pabilo8.immersiveintelligence.client.gui.deco.component.text.highlight.TextHighlighter.Segment;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.IIColor;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A manual object that displays a POL code block with syntax highlighting.
 * Based on the code display  from IIManualDataOperation, and using the colors from POLHighlighter
 * @author Avalon (avalon@iiteam.net)
 * @since 26.03.2026
 */
public class IIManualPOLCode extends IIManualObject
{
	private static final IIColor COLOR_PLAIN = IIColor.fromPackedRGB(0xA9B7C6);
	private static final POLHighlighter HIGHLIGHTER = new POLHighlighter();

	private String[] codeLines;


	public IIManualPOLCode(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//reading code
		String codeRaw = dataSource.getString("code");
		codeLines = codeRaw.isEmpty()?new String[0]: codeRaw.split("\\\\n");
	}

	@Override
	protected int getDefaultHeight()
	{
		return 16;
	}

	//font stuff

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);

		boolean unicode = mc.fontRenderer.getUnicodeFlag();
		int fontHeight = manual.fontRenderer.FONT_HEIGHT;
		int yOffset = y;

		mc.fontRenderer.setUnicodeFlag(true);
		GlStateManager.pushMatrix();

		boolean codeFontUnicode = IIClientUtils.fontRegular.getUnicodeFlag();
		IIClientUtils.fontRegular.setUnicodeFlag(true);

		//background
		int codeHeight = codeLines.length*fontHeight;
		ClientUtils.drawColouredRect(x-2, yOffset-2, width+4, codeHeight+4, 0xaa000000);

		//highlighting
		for(String line : codeLines)
		{
			List<Segment> segments = HIGHLIGHTER.highlight(line);
			int xPos = x;
			for(Segment seg : segments)
			{
				String text = seg.text;
				if(seg.bold)
					text = TextFormatting.BOLD+text;
				if(seg.italic)
					text = TextFormatting.ITALIC+text;
				IIClientUtils.fontRegular.drawString(text, xPos, yOffset, seg.color.getPackedRGB());
				xPos += IIClientUtils.fontRegular.getStringWidth(text);
			}
			yOffset += fontHeight;
		}

		IIClientUtils.fontRegular.setUnicodeFlag(codeFontUnicode);

		GlStateManager.popMatrix();
		mc.fontRenderer.setUnicodeFlag(unicode);
	}

	@Override
	public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
	{
		return false;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{
	}

	@Nullable
	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		return null;
	}
}
