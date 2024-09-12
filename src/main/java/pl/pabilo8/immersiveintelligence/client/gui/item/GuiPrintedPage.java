package pl.pabilo8.immersiveintelligence.client.gui.item;

import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.client.util.font.IIFontRenderer;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8
 * @since 09-07-2019
 */
public class GuiPrintedPage extends GuiScreen
{
	private static final Pattern patternHighlight = Pattern.compile("\\[(.+?)]");
	private static final Pattern patternBold = Pattern.compile("\\*\\*(.+?)\\*\\*");
	private static final Pattern patternItalic = Pattern.compile("\\*(.+?)\\*");
	private static final Pattern patternUnderline = Pattern.compile("__(.+?)__");
	private static final Pattern patternStrikethrough = Pattern.compile("~~(.+?)~~");

	private static final String PAGE_TEXTURE = ImmersiveIntelligence.MODID+":textures/gui/printed_page.png";
	private int guiLeft = 0, guiTop = 0;

	private final FormattedTextLine[] lines;

	public GuiPrintedPage(EntityPlayer player, ItemStack heldStack, EnumHand hand)
	{
		String text = ItemNBTHelper.getString(heldStack, "text")
				.replace("<br>", "\n")
				.replace("\\n", "\n");

		//markdown formatting
		text = matchReplaceSimple(patternHighlight, text, TextFormatting.BOLD, TextFormatting.GOLD); //highlight
		text = matchReplaceSimple(patternBold, text, TextFormatting.BOLD); //bold
		text = matchReplaceSimple(patternItalic, text, TextFormatting.ITALIC); //italic
		text = matchReplaceSimple(patternUnderline, text, TextFormatting.UNDERLINE); //underline
		text = matchReplaceSimple(patternStrikethrough, text, TextFormatting.STRIKETHROUGH); //strikethrough

		//create lines with font specified
		lines = Arrays.stream(text.split("\n"))
				.map(FormattedTextLine::new)
				.toArray(FormattedTextLine[]::new);
	}

	@Override
	public void initGui()
	{
		super.initGui();

		guiLeft = (this.width-149)/2;
		guiTop = (this.height-196)/2;
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
		super.drawScreen(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		drawPage();

		/*GlStateManager.pushMatrix();
		GlStateManager.translate(i+8, j+24, 0);
		GlStateManager.scale(2f, 2f, 1f);
		IIClientUtils.fontKaiser.drawSplitString(TextFormatting.UNDERLINE+"Minecraft at War!", 0, 0, pageImageWidth-8, IILib.COLOR_H1);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(i+8, j+44, 0);
		GlStateManager.scale(1.25f, 1.25f, 1f);
		IIClientUtils.fontNormung.drawSplitString("Engineers' Ministry of War announces: \nTHERE IS NO STEP BACK.", 0, 0, pageImageWidth-18, IILib.COLOR_H1);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate(i+8, j+76, 0);
		IIClientUtils.fontEngineerTimes.drawSplitString("Hanses unwarned attack at the "+TextFormatting.ITALIC+"Vanilla Compound"+TextFormatting.RESET+". Unknown armored vehicles broke through the main defence line. Vanilla High Command flees to allied Bedrock Republic.", 0, 0, pageImageWidth-18, IILib.COLOR_H1);
		GlStateManager.popMatrix();*/
	}

	public void drawPage()
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		ClientUtils.bindTexture(PAGE_TEXTURE);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 146, 196);

		int y = 24;
		for(FormattedTextLine line : lines)
		{
			GlStateManager.pushMatrix();
			GlStateManager.translate(guiLeft+8, guiTop+y, 0);
			GlStateManager.scale(line.size, line.size, line.size);
			line.font.drawSplitString(line.text, 0, 0, (int)(141/line.size), IIReference.COLOR_H1.getPackedRGB());
			y += (line.font.getWordWrappedHeight(line.text, (int)(141/line.size)))*line.size;
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
}
