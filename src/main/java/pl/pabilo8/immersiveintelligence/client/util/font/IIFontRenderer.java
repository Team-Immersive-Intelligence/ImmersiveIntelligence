package pl.pabilo8.immersiveintelligence.client.util.font;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8
 * @since 24.09.2022
 */
public class IIFontRenderer extends FontRenderer
{
	static HashMap<Character, CharReplacement> unicodeReplacements = new HashMap<>();
	private final Pattern hexColPattern = Pattern.compile("<hexcol=(......):([^>]*)>");
	private final int hexColLength = "<hexcol=012345:>".length();
	private int hexColWidth;

	static
	{
		//IE
		unicodeReplacements.put((char)Integer.parseInt("260E", 16),
				new CharReplacement(new ResourceLocation("immersiveengineering:textures/gui/hud_elements.png"), .5f, .75f, .5625f, .8125f));

		//II
		//Basic symbols
		unicodeReplacements.put(IIReference.CHARICON_SPEED, new CharReplacement(0, 0)); //speed
		unicodeReplacements.put(IIReference.CHARICON_TORQUE, new CharReplacement(1, 0)); //torque
		unicodeReplacements.put(IIReference.CHARICON_ENERGY, new CharReplacement(2, 0)); //energy
		unicodeReplacements.put(IIReference.CHARICON_RADIATION, new CharReplacement(3, 0)); //nuclear / radiation

		//Descriptions
		unicodeReplacements.put(IIReference.CHARICON_BULLET, new CharReplacement(0, 1)); //bullet
		unicodeReplacements.put(IIReference.CHARICON_BULLET_CONTENTS, new CharReplacement(1, 1)); //bullet contents
		unicodeReplacements.put(IIReference.CHARICON_PENETRATION, new CharReplacement(2, 1)); //penetration
		unicodeReplacements.put(IIReference.CHARICON_SKULL, new CharReplacement(3, 1)); //damage / skull

		//Ammunition Fuse Types
		unicodeReplacements.put(IIReference.CHARICON_CONTACT, new CharReplacement(0, 2)); //contact
		unicodeReplacements.put(IIReference.CHARICON_TIMED, new CharReplacement(1, 2)); //timed
		unicodeReplacements.put(IIReference.CHARICON_PROXIMITY, new CharReplacement(2, 2)); //proximity

		//Manual Directory Symbol
		unicodeReplacements.put(IIReference.CHARICON_FOLDER, new CharReplacement(2, 3)); //folder

		//Weapon Classes
		unicodeReplacements.put(IIReference.CHARICON_MG, new CharReplacement(0, 3)); //mg
		unicodeReplacements.put(IIReference.CHARICON_SMG, new CharReplacement(1, 3)); //smg
		unicodeReplacements.put(IIReference.CHARICON_RAILGUN, new CharReplacement(2, 3)); //railgun
		unicodeReplacements.put(IIReference.CHARICON_REVOLVER, new CharReplacement(3, 3)); //revolver
		unicodeReplacements.put(IIReference.CHARICON_AUTOREVOLVER, new CharReplacement(0, 4)); //autorevolver
		unicodeReplacements.put(IIReference.CHARICON_STG, new CharReplacement(1, 4)); //stg
		unicodeReplacements.put(IIReference.CHARICON_SPIGOT_MORTAR, new CharReplacement(2, 4)); //spigot mortar
		unicodeReplacements.put(IIReference.CHARICON_RIFLE, new CharReplacement(3, 4)); //rifle

		//Armor Classes
		unicodeReplacements.put(IIReference.CHARICON_HELMET, new CharReplacement(0, 5)); //helmet
		unicodeReplacements.put(IIReference.CHARICON_CHESTPLATE, new CharReplacement(1, 5)); //chestplate
		unicodeReplacements.put(IIReference.CHARICON_LEGGINGS, new CharReplacement(2, 5)); //leggings
		unicodeReplacements.put(IIReference.CHARICON_BOOTS, new CharReplacement(3, 5)); //boots
	}

	int[] backupColors;
	String colorFormattingKeys = "0123456789abcdef";
	public float customSpaceWidth = 4f;
	public float spacingModifier = 0f;
	public boolean verticalBoldness = false;

	public IIFontRenderer(ResourceLocation res)
	{
		super(ClientUtils.mc().gameSettings, res, ClientUtils.mc().renderEngine, false);
		if(Minecraft.getMinecraft().gameSettings.language!=null)
		{
			this.setUnicodeFlag(ClientUtils.mc().getLanguageManager().isCurrentLocaleUnicode());
			this.setBidiFlag(ClientUtils.mc().getLanguageManager().isCurrentLanguageBidirectional());
		}
		((IReloadableResourceManager)ClientUtils.mc().getResourceManager()).registerReloadListener(this);
		createColorBackup();
	}

	@Override
	protected void readGlyphSizes()
	{
		super.readGlyphSizes();
		this.hexColWidth = super.getStringWidth("<hexcol=012345:>");

	}

	/**
	 * This should be called again if the color array was modified after instantiation
	 */
	public void createColorBackup()
	{
		this.backupColors = Arrays.copyOf(this.colorCode, 32);
	}

	@Override
	public int getStringWidth(@Nullable String text)
	{
		if(text==null)
			return 0;

		//width - occurences of hexcol
		int stringWidth = super.getStringWidth(text);
		Matcher matcher = hexColPattern.matcher(text);

		while(matcher.find())
			stringWidth -= hexColWidth;
		return stringWidth;
	}


	/**
	 * Inserts newline and formatting into a string to wrap it within the specified width.
	 */
	private String wrapFormattedStringToWidth(String str, int wrapWidth)
	{
		int i = this.sizeStringToWidth(str, wrapWidth);

		if(str.length() <= i)
			return str;
		else
		{
			Matcher matcher = hexColPattern.matcher(str);
			String current, remains;
			while(matcher.find())
			{
				int start = matcher.start();
				int end = matcher.end();
				//the hexcol pattern is always counted as being "0" width, so it will never be wrapped aside the text inside it
				if(end >= i)
				{
					int charsRemaining = Math.min(i-start, matcher.group(2).length());
					current = str.substring(0, start)+String.format("<hexcol=(%s):(%s)>",
							matcher.group(1),
							matcher.group(2).substring(0, charsRemaining)
					);
					remains = String.format("<hexcol=(%s):(%s)>",
							matcher.group(1),
							matcher.group(2).substring(charsRemaining)
					)+str.substring(end);
					return current+"\n"+this.wrapFormattedStringToWidth(remains, wrapWidth);
				}
			}

			current = str.substring(0, i);
			char c0 = str.charAt(i);
			boolean flag = c0==' '||c0=='\n';
			remains = getFormatFromString(current)+str.substring(i+(flag?1: 0));
			return current+"\n"+this.wrapFormattedStringToWidth(remains, wrapWidth);
		}
	}

	@Override
	public List<String> listFormattedStringToWidth(String str, int wrapWidth)
	{
		return Arrays.asList(this.wrapFormattedStringToWidth(str, wrapWidth).split("\n"));
	}

	/**
	 * Render a single line string at the current (posX,posY) and update posX
	 */
	@Override
	public void renderStringAtPos(String text, boolean shadow)
	{
		int idx;
		int loop = 0;
		HashMap<Integer, Integer> formattingReplacements = new HashMap<>();

		while((idx = text.indexOf("<hexcol=")) >= 0&&loop++ < 20)
		{
			int end = text.indexOf(">", idx);
			if(end >= 0)
			{
				String rep = "ERROR";
				String s = text.substring(idx, end+1);
				int formatEnd = s.indexOf(":");
				if(formatEnd >= 0)
				{
					rep = s.substring(formatEnd+1, s.length()-1);
					String hex = s.substring("<hexcol=".length(), formatEnd);
					try
					{
						int hexColor = Integer.parseInt(hex, 16);
						int formatting = 0;
						if(formattingReplacements.containsKey(hexColor))
							formatting = formattingReplacements.get(hexColor);
						else
							while(formatting < 16&&text.contains("\u00A7"+colorFormattingKeys.charAt(formatting)))
								formatting++;
						if(formatting < 16)
						{
							rep = "\u00A7"+colorFormattingKeys.charAt(formatting)+rep+"\u00A7r";
							this.colorCode[formatting] = hexColor;
							this.colorCode[16+formatting] = ClientUtils.getDarkenedTextColour(hexColor);
						}
						formattingReplacements.put(hexColor, formatting);
					} catch(Exception ignored)
					{
					}
				}
				text = text.replace(s, rep);
			}
		}
		if(verticalBoldness)
		{
			float startX = this.posX;
			float startY = this.posY;
			float yOffset = this.getUnicodeFlag()?.5f: 1;

			super.renderStringAtPos(text, shadow);
			this.posY = startY+yOffset;
			this.posX = startX;
			super.renderStringAtPos(text, shadow);
			this.posY -= yOffset;
		}
		else
			super.renderStringAtPos(text, shadow);

		this.colorCode = Arrays.copyOf(backupColors, 32);
	}

	/**
	 * Render a single Unicode character at current (posX,posY) location using one of the /font/glyph_XX.png files...
	 */
	@Override
	protected float renderUnicodeChar(char ch, boolean italic)
	{
		CharReplacement cr = unicodeReplacements.get(ch);
		if(cr!=null)
			return cr.replaceChar(posX, posY);
		return super.renderUnicodeChar(ch, italic);
	}

	/**
	 * Render a single character with the default.png font at current (posX,posY) location...
	 */
	@Override
	protected float renderDefaultChar(int ch, boolean italic)
	{
		if(ch==32)
			return customSpaceWidth;
		return super.renderDefaultChar(ch, italic)+spacingModifier;
	}

	public float getCharWidthIEFloat(char character)
	{
		if(character==32)
			return customSpaceWidth;
		return super.getCharWidth(character)+spacingModifier;
	}

	/**
	 * Returns the width of this character as rendered.
	 */
	@Override
	public int getCharWidth(char character)
	{
		return (int)this.getCharWidthIEFloat(character);
	}

	/**
	 * Determines how many characters from the string will fit into the specified width.
	 */
	@Override
	public int sizeStringToWidth(String str, int wrapWidth)
	{
		StringBuilder builder = new StringBuilder(str);
		Matcher matcher = hexColPattern.matcher(str);
		int m = 0;
		while(matcher.find())
		{
			builder.replace(matcher.start(), matcher.end(), matcher.group(2));
			m++;
		}
		str = builder.toString();
		return super.sizeStringToWidth(str, wrapWidth)+m*hexColLength;
	}


	static class CharReplacement
	{
		private final ResourceLocation textureSheet;
		private final float uMin;
		private final float vMin;
		private final float uMax;
		private final float vMax;

		public CharReplacement(int x, int y)
		{
			this(new ResourceLocation(ImmersiveIntelligence.MODID+":textures/gui/hud_elements.png"),
					(192+(x*16))/256f, (160+(y*16))/256f, (192+16+(x*16))/256f, (160+16+(y*16))/256f);
		}

		public CharReplacement(ResourceLocation textureSheet, float uMin, float vMin, float uMax, float vMax)
		{
			this.textureSheet = textureSheet;
			this.uMin = uMin;
			this.vMin = vMin;
			this.uMax = uMax;
			this.vMax = vMax;
		}

		float replaceChar(float posX, float posY)
		{
			IIClientUtils.bindTexture(textureSheet);
			GlStateManager.glBegin(5);
			GlStateManager.glTexCoord2f(uMin, vMin);
			GlStateManager.glVertex3f(posX, posY, 0.0F);
			GlStateManager.glTexCoord2f(uMin, vMax);
			GlStateManager.glVertex3f(posX, posY+7.99F, 0.0F);
			GlStateManager.glTexCoord2f(uMax, vMin);
			GlStateManager.glVertex3f(posX+7.99f, posY, 0.0F);
			GlStateManager.glTexCoord2f(uMax, vMax);
			GlStateManager.glVertex3f(posX+7.99f, posY+7.99F, 0.0F);
			GlStateManager.glEnd();
			return 8.02f;
		}
	}
}