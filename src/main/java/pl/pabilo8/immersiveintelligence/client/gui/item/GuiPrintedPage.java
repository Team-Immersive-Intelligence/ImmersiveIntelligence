package pl.pabilo8.immersiveintelligence.client.gui.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.gui.deco.util.DecoColors;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.item.ItemIIPrintedPage.PageType;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 09.07.2019
 */
public class GuiPrintedPage extends GuiScreen
{
	private static final Pattern patternHighlight = Pattern.compile("\\[(.+?)]");
	private static final Pattern patternBold = Pattern.compile("\\*\\*(.+?)\\*\\*");
	private static final Pattern patternItalic = Pattern.compile("\\*(.+?)\\*");
	private static final Pattern patternUnderline = Pattern.compile("__(.+?)__");
	private static final Pattern patternStrikethrough = Pattern.compile("~~(.+?)~~");

	private static final String PAGE_TEXTURE_PAGE = ImmersiveIntelligence.MODID+":textures/gui/printed_page/page.png";
	private static final String PAGE_TEXTURE_BG = ImmersiveIntelligence.MODID+":textures/gui/printed_page/";
	private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	private int guiLeft = 0, guiTop = 0, topOffset = 0;
	private int currentPage = 0;
	private final int displayedPagesNum;
	private final String pageTexture;
	private FormattedTextLine[][] pages;
	private final PageType generalPageType;
	private PageType[] pageTypes;

	private NextPageButton buttonNextPage;
	private NextPageButton buttonPreviousPage;
	private GuiButton buttonDone;
	private final String author;
	private final String title;
	public final EnumHand initiatedFromHand;

	public GuiPrintedPage(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		this(player, heldStack, hand, false);
	}

	public GuiPrintedPage(EntityPlayer player, ItemStack heldStack, EnumHand hand, boolean onlyTitle)
	{
		generalPageType = PageType.fromStack(heldStack);
		pageTexture = PAGE_TEXTURE_BG+generalPageType.getUITextureName(heldStack);
		displayedPagesNum = generalPageType.getDisplayedPages();
		initiatedFromHand = hand;
		author = ItemNBTHelper.getString(heldStack, "author");
		title = ItemNBTHelper.getString(heldStack, "title");

		switch(generalPageType)
		{
			//No display; No text
			case BLANK:
			case LETTER:
				break;

			//Single page display (TODO: currently all of them display just text, need to adjust later)
			case TEXT:
			case CODE:
			case BLUEPRINT:
			{
				pages = new FormattedTextLine[1][];
				pages[0] = prepareLines(ItemNBTHelper.getString(heldStack, "text"));
				pageTypes = new PageType[]{generalPageType};
				break;
			}
			case LETTER_OPENED:
			{
				pages = new FormattedTextLine[1][];
				pages[0] = prepareLines(ItemNBTHelper.getString(heldStack, "text"));

				pageTypes = new PageType[]{
						PageType.valueOf(
								ItemNBTHelper.hasTag(heldStack)?
										ItemNBTHelper.getTag(heldStack).getString("type"):
										PageType.TEXT.toString()
						)
				};
				break;
			}

			//Multiple Pages display
			case BOUND_PAGES:
			case NEWSPAPER:
			case BOOK:
			{
				topOffset = -10;
				if(heldStack.hasTagCompound())
				{
					NBTTagCompound nbttagcompound = heldStack.getTagCompound();
					net.minecraft.nbt.NBTTagList pagesNBT = nbttagcompound.getTagList("pages", 10).copy();
					int pagesLength = pagesNBT.tagCount();

					//If the title only is needed, we don't need to load any additional pages
					if(onlyTitle) pagesLength = Math.min(pagesLength, displayedPagesNum);

					if(pagesLength < 1)
					{
						//Has no data, create single empty page
						pages = new FormattedTextLine[1][];
						pages[0] = new FormattedTextLine[]{};
						pageTypes = new PageType[]{PageType.TEXT};
					}
					else
					{
						//Create array of pages and page types
						pages = new FormattedTextLine[pagesLength][];
						pageTypes = new PageType[pagesLength];

						int index = 0;
						while(index < pagesLength)
						{
							try
							{
								NBTTagCompound pageNBT = pagesNBT.getCompoundTagAt(index);
								pageTypes[index] = PageType.valueOf(pageNBT.getString("type"));
								pages[index] = prepareLines(pageNBT.getString("text"));
							} catch(Exception e)
							{
								pages[index] = new FormattedTextLine[]{};
								pageTypes[index] = PageType.TEXT;
							} finally
							{
								index += 1;
							}
						}
					}
				}
				else
				{
					//Has no data, create single empty page
					pages = new FormattedTextLine[1][];
					pages[0] = new FormattedTextLine[]{};
					pageTypes = new PageType[]{PageType.TEXT};
				}
				break;
			}

		}
	}

	@Nonnull
	private FormattedTextLine[] prepareLines(String text)
	{
		text = text
				.replace("<br>", "\n")
				.replace("\\n", "\n");

		//markdown formatting
		text = matchReplaceSimple(patternHighlight, text, TextFormatting.BOLD, TextFormatting.GOLD); //highlight
		text = matchReplaceSimple(patternBold, text, TextFormatting.BOLD); //bold
		text = matchReplaceSimple(patternItalic, text, TextFormatting.ITALIC); //italic
		text = matchReplaceSimple(patternUnderline, text, TextFormatting.UNDERLINE); //underline
		text = matchReplaceSimple(patternStrikethrough, text, TextFormatting.STRIKETHROUGH); //strikethrough

		return Arrays.stream(text.split("\n"))
				.map(FormattedTextLine::new)
				.toArray(FormattedTextLine[]::new);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		guiLeft = (this.width-(149*displayedPagesNum))/2;
		guiTop = (this.height-196)/2;

		int buttonLeft = (this.width-188)/2;
		int buttonPagesOffset = 74*(displayedPagesNum-1);
		this.buttonNextPage = this.addButton(new NextPageButton(1, buttonLeft+buttonPagesOffset+141, guiTop+182, true));
		this.buttonPreviousPage = this.addButton(new NextPageButton(2, buttonLeft-buttonPagesOffset+20, guiTop+182, false));
		this.buttonDone = this.addButton(new GuiButton(0, this.width/2-100, guiTop+220, 200, 20, I18n.format("gui.done", new Object[0])));
		this.updateButtons();
	}

	private void updateButtons()
	{
		buttonNextPage.visible = (currentPage+displayedPagesNum) < pages.length;
		buttonPreviousPage.visible = currentPage > 0;
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawPageBackground();
		int leftOffset = 0;
		for(int renderedPage = currentPage; renderedPage < currentPage+displayedPagesNum; renderedPage += 1)
		{
			try
			{
				drawPageContent(pages[renderedPage], pageTypes[renderedPage], leftOffset);
			} catch(Exception r) {}
			leftOffset += 149;

		}
		drawPageForeground();
		drawPageIndicator();
		if(generalPageType==PageType.NEWSPAPER)
			drawPageHeader();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	private void drawPageHeader()
	{
		int leftLabel = (width-220)/2;
		int rightLabel = (width+220)/2;

		int titleWidthHalf = (this.fontRenderer.getStringWidth(title)/2);
		this.fontRenderer.drawString(title, leftLabel-titleWidthHalf, guiTop-10, 0);

		if(!StringUtils.isNullOrEmpty(author))
		{
			String authorLabel = net.minecraft.util.text.translation.I18n.translateToLocalFormatted("book.byAuthor", new Object[]{author});
			int labelWidthHalf = (this.fontRenderer.getStringWidth(authorLabel)/2);
			this.fontRenderer.drawString(authorLabel, rightLabel-labelWidthHalf, guiTop-10, 0);
		}
	}

	private void drawPageIndicator()
	{
		if(pages.length > 1)
		{
			int left = (width-149*(displayedPagesNum-1))/2;
			for(int labelledPage = currentPage; labelledPage < currentPage+displayedPagesNum; labelledPage++)
			{
				String pagesLabel = String.valueOf(labelledPage+1);
				int labelWidthHalf = (this.fontRenderer.getStringWidth(pagesLabel)/2);
				this.fontRenderer.drawString(pagesLabel, left-labelWidthHalf, guiTop+185, 0);
				left += 149;
			}
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		Minecraft mc = Minecraft.getMinecraft();

		if(buttonNextPage.mousePressed(mc, mouseX, mouseY))
		{
			currentPage += displayedPagesNum;
			updateButtons();
		}

		if(buttonPreviousPage.mousePressed(mc, mouseX, mouseY))
		{
			currentPage -= displayedPagesNum;
			updateButtons();
		}

		if(buttonDone.mousePressed(mc, mouseX, mouseY))
			mc.displayGuiScreen(null);
	}

	/**
	 * Draws the Handheld item, with fixed viewing setting to always be on the first pages
	 */
	public void drawTitlePage()
	{
		drawPageBackground();
		int leftOffset = 0;
		for(int renderedPage = 0; renderedPage < displayedPagesNum; renderedPage += 1)
		{
			try
			{
				drawPageContent(pages[renderedPage], pageTypes[renderedPage], leftOffset);
			} catch(Exception r) {}
			leftOffset += 149;

		}
		drawPageForeground();
	}

	public void drawPageBackground()
	{
		//Draw the initial background
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(pageTexture);
		switch(generalPageType)
		{
			case TEXT:
			case CODE:
			case BLUEPRINT:
			case LETTER_OPENED:
				this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 146, 196);
				break;
			case BOUND_PAGES:
				this.drawTexturedModalRect(guiLeft-8, guiTop, 0, 0, 155, 207);
				break;
			case NEWSPAPER:
				this.drawTexturedModalRect(guiLeft-5, guiTop-22, 0, 0, 218, 226);
				this.drawTexturedModalRect(guiLeft+213, guiTop-22, 20, 0, 31, 226);
				this.drawTexturedModalRect(guiLeft+244, guiTop-22, 20, 0, 221, 226);
				break;
			case BOOK:
				this.drawTexturedModalRect(guiLeft-6, guiTop-1, 0, 0, 166, 217);
				this.drawTexturedModalRectXMirrored(guiLeft+160, guiTop-1, 0, 0, 141, 217);
				break;
			default:
				break;
		}
	}

	private void drawTexturedModalRectXMirrored(int x, int y, int textureX, int textureY, int width, int height)
	{
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos((double)(x+0), (double)(y+height), (double)this.zLevel).tex((double)((float)(textureX+width)*0.00390625F), (double)((float)(textureY+height)*0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x+width), (double)(y+height), (double)this.zLevel).tex((double)((float)(textureX+0)*0.00390625F), (double)((float)(textureY+height)*0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x+width), (double)(y+0), (double)this.zLevel).tex((double)((float)(textureX+0)*0.00390625F), (double)((float)(textureY+0)*0.00390625F)).endVertex();
		bufferbuilder.pos((double)(x+0), (double)(y+0), (double)this.zLevel).tex((double)((float)(textureX+width)*0.00390625F), (double)((float)(textureY+0)*0.00390625F)).endVertex();
		tessellator.draw();
	}

	public void drawPageForeground()
	{
		//The overlay should always go over the text.
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(pageTexture);
		switch(generalPageType)
		{
			case TEXT:
			case CODE:
			case BLUEPRINT:
			case LETTER_OPENED:
				this.drawTexturedModalRect(guiLeft, guiTop, 0, 196, 21, 19);
				break;
			case BOUND_PAGES:
				this.drawTexturedModalRect(guiLeft-7, guiTop, 155, 0, 14, 207);
				break;
			case NEWSPAPER:
				break;
			case BOOK:
				break;
			default:
				break;
		}
	}

	public void drawPageContent(FormattedTextLine[] lines, PageType pageType, int leftOffset)
	{
		int pageOffset = -1;
		int bgHeight = 1;

		if(pageType==PageType.BLUEPRINT)
		{
			pageOffset = 0;
			bgHeight = 48;
		}
		if(pageType==PageType.CODE)
		{
			pageOffset = 48;
			bgHeight = 36;
		}

		//Draw additional Page Background
		if(pageOffset >= 0)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			ClientUtils.bindTexture(PAGE_TEXTURE_PAGE);
			GlStateManager.enableBlend();
			int cutoff_y = 179;
			int cutoff_x = 138;
			for(int grid_y = 0; grid_y < 48*4; grid_y += bgHeight)
			{
				for(int grid_x = 0; grid_x < 48*3; grid_x += 48)
				{
					this.drawTexturedModalRect(
							guiLeft+leftOffset+grid_x+4,
							guiTop+topOffset+grid_y+13,
							146, pageOffset,
							Math.min(48, cutoff_x-grid_x),
							Math.min(bgHeight, cutoff_y-grid_y)
					);
				}
			}
			GlStateManager.disableBlend();
		}

		int y = 20;
		for(FormattedTextLine line : lines)
		{
			int lineHeight = (int)((line.font.getWordWrappedHeight(line.text, (int)(133/line.size)))*line.size);
			if(y+lineHeight > 192) break;

			GlStateManager.pushMatrix();
			GlStateManager.translate(guiLeft+leftOffset+8, guiTop+topOffset+y, 0);
			GlStateManager.scale(line.size, line.size, line.size);
			line.font.drawSplitString(line.text, 0, 0, (int)(133/line.size), DecoColors.H1.getPackedRGB());
			y += lineHeight;
			GlStateManager.popMatrix();
		}
	}

	private String matchReplaceSimple(Pattern pattern, String text, TextFormatting... formats)
	{
		return matchReplace(pattern, text, (stringBuilder, matcher) ->
				{
					for(TextFormatting format : formats)
						stringBuilder.append(format);
					stringBuilder
							.append(matcher.group(1))
							.append(TextFormatting.RESET);
				}
		);
	}

	private String matchReplace(Pattern pattern, String text, BiConsumer<StringBuilder, Matcher> operation)
	{
		//iterate, skip fragments with no significance
		StringBuilder builder = new StringBuilder();
		Matcher matcher = pattern.matcher(text);
		int i = 0;
		while(matcher.find())
		{
			//replace from marker
			builder.append(text, i, matcher.start());
			operation.accept(builder, matcher);
			//move the marker
			i = matcher.end();
		}
		//build
		builder.append(text.substring(i));
		return builder.toString();
	}

	private static class FormattedTextLine
	{
		private final IIFontRenderer font;
		private final String text;
		private final float size;

		public FormattedTextLine(String text)
		{
			String[] split = text.split(" ");

			//font selection
			IIFontRenderer font = IIClientUtils.fontRegular;
			float size = 1;

			int removed = 0;
			for(String s : split)
			{
				if(s.startsWith("#"))
					switch(s.substring(1))
					{
						case "engineer_times":
						case "times":
							font = IIClientUtils.fontEngineerTimes;
							break;
						case "fraktur":
						case "kaiser":
						case "kaiser_fraktur":
						case "kaiserfraktur":
							font = IIClientUtils.fontKaiser;
							break;
						case "normung":
						case "bahnschrift":
							font = IIClientUtils.fontNormung;
							break;
						case "tinkerer":
							font = IIClientUtils.fontTinkerer;
							break;
					}
				else if(s.startsWith("@"))
					try
					{
						size = Float.parseFloat(s.substring(1));
					} catch(NumberFormatException ignored) {}
				else
					break;
			}

			if(font!=IIClientUtils.fontRegular)
				removed++;
			if(size!=1)
				removed++;

			this.font = font;
			if(removed==0)
				this.text = text;
			else
			{
				StringBuilder builder = new StringBuilder();
				for(int i = removed; i < split.length; i++)
					builder.append(split[i]).append(" ");
				this.text = builder.toString().trim();
			}
			this.size = size;
		}
	}

	@SideOnly(Side.CLIENT)
	static class NextPageButton extends GuiButton
	{
		private final boolean isForward;

		public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
		{
			super(buttonId, x, y, 23, 13, "");
			this.isForward = isForwardIn;
		}

		public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
		{
			if(this.visible)
			{
				boolean flag = mouseX >= this.x&&mouseY >= this.y&&mouseX < this.x+this.width&&mouseY < this.y+this.height;
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
				int i = 0;
				int j = 192;

				if(flag) i += 23;
				if(!this.isForward) j += 13;

				this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
			}

		}
	}
}
