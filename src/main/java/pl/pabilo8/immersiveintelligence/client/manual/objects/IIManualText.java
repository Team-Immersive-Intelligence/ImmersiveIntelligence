package pl.pabilo8.immersiveintelligence.client.manual.objects;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualObject;
import pl.pabilo8.immersiveintelligence.client.manual.IIManualPage;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import java.util.Collections;
import java.util.List;

/**
 * A text object, can display an item name, fluid name or (translated) text.<br>
 * Can be positioned freely and allows formatting through NBT.
 *
 * @author Pabilo8
 * @since 22.05.2022
 */
public class IIManualText extends IIManualObject
{
	private FontRenderer fontRenderer;
	private String text, tooltip;
	private int color;
	private boolean unicode, textBlock;

	//--- Setup ---//

	public IIManualText(ManualObjectInfo info, EasyNBT nbt)
	{
		super(info, nbt);
	}

	@Override
	public void postInit(IIManualPage page)
	{
		super.postInit(page);

		//--- Style and Font ---//

		this.unicode = false;
		dataSource.checkSetBoolean("text_block", b -> textBlock = b, false);
		dataSource.checkSetString("tooltip", b -> tooltip = b, "");
		dataSource.checkSetInt("color", b -> color = b, 0xff000000);
		switch(dataSource.getString("font"))
		{
			case "engineer_times":
			case "times":
				fontRenderer = IIClientUtils.fontEngineerTimes;
				break;
			case "normung":
			case "bahnhof":
				fontRenderer = IIClientUtils.fontNormung;
				break;
			case "kaiser":
			case "fraktur":
				fontRenderer = IIClientUtils.fontKaiser;
				break;
			case "tinkerer":
				fontRenderer = IIClientUtils.fontTinkerer;
				break;
			default:
				fontRenderer = manual.fontRenderer;
				break;
		}
		if(dataSource.getBoolean("unicode"))
			this.unicode = true;

		//--- Formatting ---//

		if(dataSource.getBoolean("bold"))
			this.text = TextFormatting.BOLD+text;
		if(dataSource.getBoolean("italic"))
			this.text = TextFormatting.ITALIC+text;
		if(dataSource.getBoolean("underline"))
			this.text = TextFormatting.UNDERLINE+text;
		if(dataSource.getBoolean("strike"))
			this.text = TextFormatting.STRIKETHROUGH+text;
		if(dataSource.getBoolean("obfuscated"))
			this.text = TextFormatting.OBFUSCATED+text;

		//--- Text Source ---//

		this.text = getText(this.dataSource);

		//Calculate the height of the text
		boolean unicodeFlag = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(unicode);
		this.height = fontRenderer.getWordWrappedHeight(this.text, width);
		fontRenderer.setUnicodeFlag(unicodeFlag);

	}

	//--- Rendering, Reaction ---//

	@Override
	public void drawButton(Minecraft mc, int mx, int my, float partialTicks)
	{
		super.drawButton(mc, mx, my, partialTicks);
		if(textBlock)
			ClientUtils.drawColouredRect(x-2, y, width+2, height, 0xaa000000);

		boolean unicodeFlag = fontRenderer.getUnicodeFlag();
		fontRenderer.setUnicodeFlag(unicode);
		fontRenderer.drawSplitString(text, x, y, width, color);
		fontRenderer.setUnicodeFlag(unicodeFlag);
	}

	@Override
	protected int getDefaultHeight()
	{
		return height;
	}

	@Override
	public void mouseDragged(int x, int y, int clickX, int clickY, int mx, int my, int lastX, int lastY, int button)
	{

	}

	@Override
	public List<String> getTooltip(Minecraft mc, int mx, int my)
	{
		if(hovered&&!tooltip.isEmpty())
			return Collections.singletonList(tooltip);
		return null;
	}
}
