package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * Class made for easy handling of RGB in hexadecimal, int array, float array point and packed format colors, as well as CMYK and HSV.
 *
 * @author GabrielV (gabriel@iiteam.net)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 23.04.2024
 * @ii-approved 0.3.1
 * @implNote 0.003921f is an approximation of 1/255 used for ease of calculation
 * @since 20.04.2024
 */
public class IIColor implements Comparable<IIColor>, ToIntFunction<IIColor>
{
	//--- Constants ---//
	public static final IIColor WHITE = IIColor.fromARGB(255, 255, 255, 255);
	public static final IIColor BLACK = IIColor.fromARGB(0, 0, 0, 255);
	public static final IIColor ALPHA = IIColor.fromARGB(255, 255, 255, 0);

	//--- Minecraft Colors ---//
	public static final IIColor MC_BLACK = IIColor.fromHex("000000");
	public static final IIColor MC_DARK_BLUE = IIColor.fromHex("0000AA");
	public static final IIColor MC_DARK_GREEN = IIColor.fromHex("00AA00");
	public static final IIColor MC_DARK_AQUA = IIColor.fromHex("00AAAA");
	public static final IIColor MC_DARK_RED = IIColor.fromHex("AA0000");
	public static final IIColor MC_DARK_PURPLE = IIColor.fromHex("AA00AA");
	public static final IIColor MC_GOLD = IIColor.fromHex("FFAA00");
	public static final IIColor MC_GRAY = IIColor.fromHex("AAAAAA");
	public static final IIColor MC_DARK_GRAY = IIColor.fromHex("555555");
	public static final IIColor MC_BLUE = IIColor.fromHex("5555FF");
	public static final IIColor MC_GREEN = IIColor.fromHex("55FF55");
	public static final IIColor MC_AQUA = IIColor.fromHex("55FFFF");
	public static final IIColor MC_RED = IIColor.fromHex("FF5555");
	public static final IIColor MC_LIGHT_PURPLE = IIColor.fromHex("FF55FF");
	public static final IIColor MC_YELLOW = IIColor.fromHex("FFFF55");
	public static final IIColor MC_WHITE = IIColor.fromHex("FFFFFF");

	/**
	 * Array of all 16 Minecraft colors.
	 */
	public static final IIColor[] MC_COLORS = new IIColor[]{
			MC_BLACK, MC_DARK_BLUE, MC_DARK_GREEN, MC_DARK_AQUA,
			MC_DARK_RED, MC_DARK_PURPLE, MC_GOLD, MC_GRAY,
			MC_DARK_GRAY, MC_BLUE, MC_GREEN, MC_AQUA,
			MC_RED, MC_LIGHT_PURPLE, MC_YELLOW, MC_WHITE
	};

	//--- Fields ---//

	/**
	 * Alpha, component of the color in 0-255 range.
	 */
	public final int alpha;
	/**
	 * Red component of the color in 0-255 range.
	 */
	public final int red;
	/**
	 * Green component of the color in 0-255 range.
	 */
	public final int green;
	/**
	 * Blue component of the color in 0-255 range.
	 */
	public final int blue;
	/**
	 * Packed ARGB int value of the color.
	 */
	public final int rgb;

	//--- Private Constructor ---//

	/**
	 * Creates a new color with 0-255 ARGB int values.
	 *
	 * @param alpha Alpha component of the color.
	 * @param red   Red component of the color.
	 * @param green Green component of the color.
	 * @param blue  Blue component of the color.
	 */
	private IIColor(int alpha, int red, int green, int blue)
	{
		this.red = red%256;
		this.green = green%256;
		this.blue = blue%256;
		this.alpha = alpha%256;
		this.rgb = (red<<16)|(green<<8)|blue;
	}

	//--- Creation Methods ---//

	/**
	 * Creates a new color with 0-255 ARGB int values.
	 *
	 * @param argb Alpha, Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromARGB(int... argb)
	{
		return new IIColor(argb[0], argb[1], argb[2], argb[3]);
	}

	/**
	 * Creates a new color with 0-255 RGB int values.
	 *
	 * @param rgb Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromRGB(int... rgb)
	{
		return fromARGB(255, rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Creates a new color with 0-1 ARGB float values.
	 *
	 * @param argb Alpha, Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromFloatARGB(float... argb)
	{
		return new IIColor((int)(argb[0]*255), (int)(argb[1]*255), (int)(argb[2]*255), (int)(argb[3]*255));
	}

	/**
	 * Creates a new color with 0-1 RGB float values.
	 *
	 * @param rgb Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromFloatRGB(float... rgb)
	{
		return fromFloatARGB(1, rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Creates a new color with a hexadecimal string.
	 *
	 * @param hex Hexadecimal string with the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromHex(String hex)
	{
		if(hex.length()==6)
			return IIColor.fromPackedRGB(Integer.parseInt(hex, 16));
		else if(hex.length()==8)
			return IIColor.fromPackedARGB(Long.parseLong(hex, 16));

		return new IIColor(0, 0, 0, 0);
	}

	/**
	 * Creates a new color with a packed RGB integer.
	 *
	 * @param hex Packed RGB integer.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromPackedARGB(long hex)
	{
		return new IIColor((int)(hex>>24)&0xFF, (int)(hex>>16)&0xFF, (int)(hex>>8)&0xFF, (int)hex&0xFF);
	}

	/**
	 * Creates a new color with a packed RGB integer.
	 *
	 * @param hex Packed RGB integer.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromPackedRGB(int hex)
	{
		return new IIColor(255, (hex>>16)&0xFF, (hex>>8)&0xFF, hex&0xFF);
	}

	/**
	 * Creates a new color with 0.0-1.0 CMYK float values.
	 *
	 * @param cmyk Cyan, Magenta, Yellow and Black components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromCMYK(float... cmyk)
	{
		float c = cmyk[0];
		float m = cmyk[1];
		float y = cmyk[2];
		float k = cmyk[3];

		int r = (int)(255*(1-c)*(1-k));
		int g = (int)(255*(1-m)*(1-k));
		int b = (int)(255*(1-y)*(1-k));

		return new IIColor(255, r, g, b);
	}

	/**
	 * Creates a new color with 0.0-1.0 HSV float values.
	 *
	 * @param hsv Hue, Saturation, and Value components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromHSV(float... hsv)
	{
		//
		float c = hsv[2]*hsv[1];
		//
		float x = c*(1-Math.abs((hsv[0]*6)%2-1));
		//
		float m = hsv[2]-c;
		float r = 0, g = 0, b = 0;

		//use colors based on hue range
		if(hsv[0] < 0.16666667f)
		{
			r = c;
			g = x;
		}
		else if(hsv[0] < 0.33333334f)
		{
			r = x;
			g = c;
		}
		else if(hsv[0] < 0.5f)
		{
			g = c;
			b = x;
		}
		else if(hsv[0] < 0.6666667f)
		{
			g = x;
			b = c;
		}
		else if(hsv[0] < 0.8333333f)
		{
			r = x;
			b = c;
		}
		else
		{
			r = c;
			b = x;
		}

		return new IIColor(255, (int)((r+m)*255)+1, (int)((g+m)*255)+1, (int)((b+m)*255)+1);
	}

	/**
	 * @param dyeColor dye color enum
	 * @return A new IIColor object with the dye's color values
	 */
	public static IIColor fromDye(EnumDyeColor dyeColor)
	{
		return fromFloatRGB(dyeColor.getColorComponentValues());
	}

	/**
	 * @param formatting text formatting enum
	 * @return A new IIColor object with the text formatting's color values
	 */
	public static IIColor fromTextFormatting(TextFormatting formatting)
	{
		if(!formatting.isColor())
			return MC_WHITE;
		return fromRGB(formatting.getColorIndex());
	}

	/**
	 * based on <a href="https://stackoverflow.com/a/2262117/9876980">https://stackoverflow.com/a/2262117/9876980</a>
	 *
	 * @param rgb color in rgbInt
	 * @return color in rgbFloats format
	 */
	@Deprecated
	public static float[] rgbIntToRGB(int rgb)
	{
		float r = ((rgb>>16)&0x0ff)*0.003921f;
		float g = ((rgb>>8)&0x0ff)*0.003921f;
		float b = (rgb&0x0ff)*0.003921f;
		return new float[]{r, g, b};
	}

	//--- RGB Int Methods ---//

	/**
	 * @return An integer with the color in ARGBInt format.
	 */
	public int getPackedARGB()
	{
		return (alpha<<24)|getPackedRGB();
	}

	/**
	 * @return An integer with the color in RGBInt format.
	 */
	public int getPackedRGB()
	{
		return rgb;
	}

	//--- RGB Float Methods ---//

	/**
	 * @return An array of ARGB integers with values 0-255.
	 */
	public int[] getARGB()
	{
		return new int[]{alpha, red, green, blue};
	}

	/**
	 * @return An array of RGB integers with values 0-255.
	 */
	public int[] getRGB()
	{
		return new int[]{red, green, blue};
	}

	/**
	 * @return An array of ARGB floating point values with values 0.0-1.0.
	 */
	public float[] getFloatARGB()
	{
		return new float[]{alpha*0.003921f, red*0.003921f, green*0.003921f};
	}

	/**
	 * @return An array of RGB floating point values with values 0.0-1.0.
	 */
	public float[] getFloatRGB()
	{
		return new float[]{red*0.003921f, green*0.003921f, blue*0.003921f};
	}

	//--- RGB Hex String Methods ---//

	/**
	 * @return A string with the hex representation of the color in ARGB format.
	 */
	public String getHexARGB()
	{
		return String.format("%02X%02X%02X%02X", alpha, red, green, blue);
	}

	/**
	 * @return A string with the hex representation of the color in RGB format.
	 */
	public String getHexRGB()
	{
		return String.format("%02X%02X%02X", red, green, blue);
	}

	//--- CMYK and HSV methods ---//

	/**
	 * @return An array of CMYK floating point values with values 0.0-1.0.
	 */
	public float[] getCMYK()
	{
		float r = red*0.003921f;
		float g = green*0.003921f;
		float b = blue*0.003921f;

		float k = 1-Math.max(r, Math.max(g, b));
		float c = (1-r-k)/(1-k);
		float m = (1-g-k)/(1-k);
		float y = (1-b-k)/(1-k);

		return new float[]{c, m, y, k};
	}

	/**
	 * @return An array of HSV floating point values with values 0.0-1.0.
	 */
	public float[] getHSV()
	{
		float r = red*0.003921f, g = green*0.003921f, b = blue*0.003921f;

		float v = Math.max(r, Math.max(g, b));
		float min = Math.min(r, Math.min(g, b));
		float delta = v-min;

		float h = 0;
		if(delta!=0)
		{
			if(v==r)
				h = (g-b)/delta;
			else if(v==g)
				h = 2+(b-r)/delta;
			else
				h = 4+(r-g)/delta;
		}
		h *= 60;
		if(h < 0)
			h += 360;

		float s = v==0?0: delta/v;

		return new float[]{h/360f, s, v};
	}

	public int getBrightness()
	{
		return Math.max(red, Math.max(green, blue));
	}

	//--- Dyes and TextFormatting ---//

	/**
	 * @return Closest text formatting color to this color.
	 */
	public TextFormatting getTextFormatting()
	{
		return getDyeColor().chatColor;
	}

	public String getHexCol(String text)
	{
		return String.format("<hexcol=%s:%s>", getHexRGB(), text);
	}

	/**
	 * @return Closest dye color to this color.
	 */
	public EnumDyeColor getDyeColor()
	{
		Optional<EnumDyeColor> min = Arrays.stream(EnumDyeColor.values()).min(Comparator.comparingInt(value ->
				IIColor.fromFloatRGB(value.getColorComponentValues()).compareTo(this)));
		return min.orElse(EnumDyeColor.BLACK);
	}

	//--- "With" Type Methods ---//

	/**
	 * @param alpha Alpha component of the color in range 0.0-1.0.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withAlpha(float alpha)
	{
		return new IIColor((int)(alpha*255), red, green, blue);
	}

	/**
	 * @param alpha Alpha component of the color in range 0-255.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withAlpha(int alpha)
	{
		return new IIColor(alpha, red, green, blue);
	}

	/**
	 * @param red Red component of the color in range 0.0-1.0.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withRed(float red)
	{
		return new IIColor(alpha, (int)(red*255), green, blue);
	}

	/**
	 * @param red Red component of the color in range 0-255.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withRed(int red)
	{
		return new IIColor(alpha, red, green, blue);
	}

	/**
	 * @param green Green component of the color in range 0.0-1.0.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withGreen(float green)
	{
		return new IIColor(alpha, red, (int)(green*255), blue);
	}

	/**
	 * @param green Green component of the color in range 0-255.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withGreen(int green)
	{
		return new IIColor(alpha, red, green, blue);
	}

	/**
	 * @param blue Blue component of the color in range 0.0-1.0.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withBlue(float blue)
	{
		return new IIColor(alpha, red, green, (int)(blue*255));
	}

	/**
	 * @param blue Blue component of the color in range 0-255.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withBlue(int blue)
	{
		return new IIColor(alpha, red, green, blue);
	}

	/**
	 * @param factor Brightness of the color in range 0.0-1.0.
	 * @return A new IIColor object with the specified values.
	 */
	public IIColor withBrightness(float factor)
	{
		int maxComponent = Math.max(red, Math.max(green, blue));
		if(maxComponent==0)
			return new IIColor(alpha, 0, 0, 0);

		float scale = factor*255/maxComponent;
		int newRed = MathHelper.clamp((int)(red*scale), 0, 255);
		int newGreen = MathHelper.clamp((int)(green*scale), 0, 255);
		int newBlue = MathHelper.clamp((int)(blue*scale), 0, 255);
		return new IIColor(alpha, newRed, newGreen, newBlue);
	}

	//--- Color Mixing Utilities ---//

	/**
	 * Mixes two colors with a given ratio.
	 *
	 * @param color The color to mix with.
	 * @param ratio The ratio of this to the other color.
	 * @return A new color with the mixed values.
	 */
	public IIColor mixedWith(IIColor color, float ratio)
	{
		return new IIColor(
				(int)(alpha*(1-ratio)+(color.alpha*ratio)),
				(int)(red*(1-ratio)+(color.red*ratio)),
				(int)(green*(1-ratio)+(color.green*ratio)),
				(int)(blue*(1-ratio)+(color.blue*ratio))
		);
	}

	//--- OpenGL Utilities ---//

	@SideOnly(Side.CLIENT)
	public void glColor()
	{
		GlStateManager.color(red*0.003921f, green*0.003921f, blue*0.003921f, alpha*0.003921f);
	}

	//--- Internal Utils ---//

	@Override
	public int compareTo(IIColor o)
	{
		float[] hsv1 = getHSV();
		float[] hsv2 = o.getHSV();

		float deltaA = (alpha-o.alpha)*100;
		float deltaH = (hsv1[0]-hsv2[0])*100;
		float deltaL = (hsv1[1]-hsv2[1]+hsv1[2]-hsv2[2])*25;

		return (int)Math.abs((deltaA*deltaA+deltaH*deltaH+deltaL*deltaL));
	}

	@Override
	public int applyAsInt(IIColor value)
	{
		return value.getPackedRGB();
	}

	@Override
	public boolean equals(Object o)
	{
		if(this==o)
			return true;
		if(!(o instanceof IIColor))
			return false;

		IIColor iiColor = (IIColor)o;

		if(alpha!=iiColor.alpha) return false;
		if(red!=iiColor.red) return false;
		if(green!=iiColor.green) return false;
		return blue==iiColor.blue;
	}

	@Override
	public int hashCode()
	{
		return getPackedARGB();
	}
}
