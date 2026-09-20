package pl.pabilo8.immersiveintelligence.client.manual.pages;

import blusunrize.lib.manual.ManualInstance;
import blusunrize.lib.manual.ManualUtils;
import blusunrize.lib.manual.gui.GuiManual;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.api.ammo.parts.AmmoPart;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Base manual page for {@link AmmoPart ammunition parts}.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 05.09.2026
 */
public abstract class IIManualPageAmmoPart<T extends AmmoPart> extends IIManualPages
{
	private static final int PAGE_WIDTH = 120;
	private static final int HEADER_X = 40;
	private static final int HEADER_WIDTH = 90;
	private static final int INFO_X = 8;
	private static final int INFO_WIDTH = PAGE_WIDTH;
	private static final int MIN_INFO_Y = 40;
	private static final int HEADER_GAP = 1;
	private static final int SECTION_GAP = 1;
	private static final int FRAME_Y = 4;

	protected final T part;
	protected final ItemStack stack;
	protected String localizedName;
	protected String localizedLore;

	private List<String> infoLines = Collections.emptyList();
	private int loreY;
	private int infoY;
	private int descriptionY;

	protected IIManualPageAmmoPart(ManualInstance manual, T part, String localizationPrefix)
	{
		super(manual, localizationPrefix+"."+part.getName());
		this.part = part;
		this.stack = part.getMaterial().getExampleStack();
	}

	@Override
	public void initPage(GuiManual gui, int x, int y, List<GuiButton> pageButtons)
	{
		highlighted = ItemStack.EMPTY;
		if(text==null||text.isEmpty())
			return;

		boolean unicode = manual.fontRenderer.getUnicodeFlag();
		manual.fontRenderer.setUnicodeFlag(true);

		localizedName = getLocalizedPartName();
		localizedLore = manual.formatText(text+".lore");
		localizedText = manual.formatText(text+".desc");
		infoLines = getInfoLines();

		calculateLayout();
		localizedText = addLinks(manual, gui, localizedText, x, y+descriptionY, PAGE_WIDTH, pageButtons);
		if(localizedText==null)
			localizedText = "";

		manual.fontRenderer.setUnicodeFlag(unicode);
	}

	@Override
	public void renderPage(GuiManual gui, int x, int y, int mx, int my)
	{
		drawWrappedText(formatName(), x+HEADER_X, y, HEADER_WIDTH);
		drawWrappedText(formatLore(), x+HEADER_X, y+loreY, HEADER_WIDTH);
		renderInfo(x+INFO_X, y+infoY);

		if(localizedText!=null&&!localizedText.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, localizedText, x, y+descriptionY, PAGE_WIDTH, manual.getTextColour());

		drawPartDisplay(gui, x, y+FRAME_Y, mx, my);
	}

	/**
	 * Gets the localized display name of the ammunition part.
	 */
	protected abstract String getLocalizedPartName();

	/**
	 * Gets the statistic lines displayed below the header.
	 */
	protected abstract List<String> getInfoLines();

	/**
	 * Gets the minimum Y offset of the description.
	 */
	protected int getMinimumDescriptionY()
	{
		return 70;
	}

	/**
	 * Draws the fixed ornate frame around the item preview.
	 */
	protected void drawOrnamentalFrame(int x, int y)
	{
		GlStateManager.pushMatrix();
		GlStateManager.color(0.25f, 0.25f, 0.25f, 0.5f);
		ManualUtils.bindTexture(texture);
		ManualUtils.drawTexturedRect(x-5, y-5, 15, 15, 0, 15/255f, 0, 15/255f);
		ManualUtils.drawTexturedRect(x+22, y-5, 15, 15, 15/255f, 30/255f, 0, 15/255f);
		ManualUtils.drawTexturedRect(x-5, y+20, 15, 15, 0, 15/255f, 15/255f, 30/255f);
		ManualUtils.drawTexturedRect(x+22, y+20, 15, 15, 15/255f, 30/255f, 15/255f, 30/255f);
		GlStateManager.popMatrix();
	}

	/**
	 * Gets the rendered height of wrapped text.
	 */
	protected int getWrappedTextHeight(String text, int width)
	{
		if(text==null||text.isEmpty())
			return 0;
		return manual.fontRenderer.listFormattedStringToWidth(text, width).size()*manual.fontRenderer.FONT_HEIGHT;
	}

	private String formatName()
	{
		return localizedName==null||localizedName.isEmpty()?"": TextFormatting.BOLD.toString()+TextFormatting.UNDERLINE+localizedName;
	}

	private String formatLore()
	{
		return localizedLore==null||localizedLore.isEmpty()?"": TextFormatting.ITALIC+localizedLore;
	}

	private void calculateLayout()
	{
		String formattedName = formatName();
		String formattedLore = formatLore();
		int nameHeight = getWrappedTextHeight(formattedName, HEADER_WIDTH);
		loreY = nameHeight+(nameHeight > 0&&!formattedLore.isEmpty()?HEADER_GAP: 0);
		int loreHeight = getWrappedTextHeight(formattedLore, HEADER_WIDTH);
		int headerBottom = loreY+loreHeight;
		infoY = Math.max(MIN_INFO_Y, headerBottom+SECTION_GAP);

		int infoHeight = 0;
		for(int i = 0; i < infoLines.size(); i++)
		{
			infoHeight += getWrappedTextHeight(TextFormatting.BOLD+infoLines.get(i), INFO_WIDTH);
			if(i < infoLines.size()-1)
				infoHeight += SECTION_GAP;
		}
		descriptionY = Math.max(getMinimumDescriptionY(), infoY+infoHeight+SECTION_GAP);
	}

	private void renderInfo(int x, int y)
	{
		int offset = 0;
		for(String line : infoLines)
		{
			String formatted = TextFormatting.BOLD+line;
			ManualUtils.drawSplitString(manual.fontRenderer, formatted, x, y+offset, INFO_WIDTH, manual.getTextColour());
			offset += getWrappedTextHeight(formatted, INFO_WIDTH)+SECTION_GAP;
		}
	}

	private void drawWrappedText(String text, int x, int y, int width)
	{
		if(text!=null&&!text.isEmpty())
			ManualUtils.drawSplitString(manual.fontRenderer, text, x, y, width, manual.getTextColour());
	}

	private void drawPartDisplay(GuiManual gui, int x, int y, int mx, int my)
	{
		drawOrnamentalFrame(x, y);
		GlStateManager.enableBlend();
		GlStateManager.pushMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.scale(2, 2, 2);
		ManualUtils.renderItem().renderItemAndEffectIntoGUI(stack, x/2, y/2);
		GlStateManager.popMatrix();

		if(IIMath.isPointInRectangle(x+4, y-4, x+36, y+28, mx, my))
			gui.renderToolTip(stack, mx, my);
	}

	@Override
	public boolean listForSearch(String searchTag)
	{
		return stack.getDisplayName().toLowerCase(Locale.ENGLISH).contains(searchTag);
	}
}
