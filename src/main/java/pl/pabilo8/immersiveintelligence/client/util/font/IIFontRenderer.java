package pl.pabilo8.immersiveintelligence.client.util.font;

import blusunrize.immersiveengineering.client.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.client.IIClientUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Pabilo8
 * @since 24.09.2022
 */
public class IIFontRenderer extends FontRenderer
{
	static HashMap<Character, CharReplacement> unicodeReplacements = new HashMap<>();
	private final Pattern hexColPattern = Pattern.compile("<hexcol=(......):(.*)>");
	private final int hexColWidth = getStringWidth("<hexcol=012345:>");
	private final int hexColLength = "<hexcol=012345:>".length();

	static
	{
		//IE
		unicodeReplacements.put((char)Integer.parseInt("260E", 16),
				new CharReplacement(new ResourceLocation("immersiveengineering:textures/gui/hud_elements.png"), .5f, .75f, .5625f, .8125f));

		//II
		//Basic symbols
		unicodeReplacements.put('\u29c1', new CharReplacement(0, 0)); //speed
		unicodeReplacements.put('\u2296', new CharReplacement(1, 0)); //torque
		unicodeReplacements.put('\u2607', new CharReplacement(2, 0)); //energy
		unicodeReplacements.put('\u2622', new CharReplacement(3, 0)); //nuclear / radiation

		//Descriptions
		unicodeReplacements.put('\u2023', new CharReplacement(0, 1)); //bullet
		unicodeReplacements.put('\u29b3', new CharReplacement(1, 1)); //bullet contents
		unicodeReplacements.put('\u29b4', new CharReplacement(2, 1)); //penetration
		unicodeReplacements.put('\u2295', new CharReplacement(3, 1)); //damage / skull

		//Ammunition Fuse Types
		unicodeReplacements.put('\u29b0', new CharReplacement(0, 2)); //contact
		unicodeReplacements.put('\u29b1', new CharReplacement(1, 2)); //proximity
		unicodeReplacements.put('\u29b2', new CharReplacement(2, 2)); //timed

		//Manual Directory Symbol
		unicodeReplacements.put('\u2348', new CharReplacement(2, 3)); //folder

		//Weapon Classes
		unicodeReplacements.put('\u24b6', new CharReplacement(0, 3)); //mg
		unicodeReplacements.put('\u24b7', new CharReplacement(1, 3)); //smg
		unicodeReplacements.put('\u24b8', new CharReplacement(2, 3)); //railgun
		unicodeReplacements.put('\u24b9', new CharReplacement(3, 3)); //revolver
		unicodeReplacements.put('\u24ba', new CharReplacement(0, 4)); //autorevolver
		unicodeReplacements.put('\u24bb', new CharReplacement(1, 4)); //stg
		unicodeReplacements.put('\u24bc', new CharReplacement(2, 4)); //spigot mortar
		unicodeReplacements.put('\u24bd', new CharReplacement(3, 4)); //rifle

		//Armor Classes
		unicodeReplacements.put('\u24be', new CharReplacement(0, 5)); //helmet
		unicodeReplacements.put('\u24bf', new CharReplacement(1, 5)); //chestplate
		unicodeReplacements.put('\u24c0', new CharReplacement(2, 5)); //leggings
		unicodeReplacements.put('\u24c1', new CharReplacement(3, 5)); //boots
	}

	int[] backupColours;
	String colourFormattingKeys = "0123456789abcdef";
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
		createColourBackup();
	}

	/**
	 * This should be called again if the colour array was modified after instantiation
	 */
	public void createColourBackup()
	{
		this.backupColours = Arrays.copyOf(this.colorCode, 32);
	}

	@Override
	public int getStringWidth(@Nullable String text)
	{
		if(text==null)
			return 0;

		//width - occurences of hexcol
		int stringWidth = getDefaultStringWidth(text);
		Matcher matcher = hexColPattern.matcher(text);

		while(matcher.find())
			stringWidth -= hexColWidth;
		return stringWidth;
	}

	private int getDefaultStringWidth(@Nullable String text)
	{
		if(text==null)
			return 0;
		else
		{
			float i = 0;
			boolean flag = false;
			for(int j = 0; j < text.length(); ++j)
			{
				char c0 = text.charAt(j);
				float k = this.getCharWidthIEFloat(c0);
				if(k < 0&&j < text.length()-1)
				{
					++j;
					c0 = text.charAt(j);

					if(c0!=108&&c0!=76)
					{
						if(c0==114||c0==82)
							flag = false;
					}
					else
						flag = true;
					k = 0;
				}

				i += k;
				if(flag&&k > 0)
					++i;
			}
			return (int)i;
		}
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
						int hexColour = Integer.parseInt(hex, 16);
						int formatting = 0;
						if(formattingReplacements.containsKey(hexColour))
							formatting = formattingReplacements.get(hexColour);
						else
							while(formatting < 16&&text.contains("\u00A7"+colourFormattingKeys.charAt(formatting)))
								formatting++;
						if(formatting < 16)
						{
							rep = "\u00A7"+colourFormattingKeys.charAt(formatting)+rep+"\u00A7r";
							this.colorCode[formatting] = hexColour;
							this.colorCode[16+formatting] = ClientUtils.getDarkenedTextColour(hexColour);
						}
						formattingReplacements.put(hexColour, formatting);
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

		this.colorCode = Arrays.copyOf(backupColours, 32);
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

		int i = str.length();
		float j = 0;
		int k = 0;
		int l = -1;

		for(boolean flag = false; k < i; ++k)
		{
			char c0 = str.charAt(k);
			switch(c0)
			{
				case '\n':
					--k;
					break;
				case ' ':
					l = k;
				default:
					j += this.getCharWidthIEFloat(c0);
					if(flag)
						++j;
					break;
				case '\u00a7':
					if(k < i-1)
					{
						++k;
						char c1 = str.charAt(k);

						if(c1!=108&&c1!=76)
						{
							if(c1==114||c1==82||(c1 >= 48&&c1 <= 57||c1 >= 97&&c1 <= 102||c1 >= 65&&c1 <= 70))
								flag = false;
						}
						else
							flag = true;
					}
			}
			if(c0==10)
			{
				++k;
				l = k;
				break;
			}
			if(j > wrapWidth)
				break;
		}

		return (k!=i&&l!=-1&&l < k?l: k)+m*hexColLength;
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