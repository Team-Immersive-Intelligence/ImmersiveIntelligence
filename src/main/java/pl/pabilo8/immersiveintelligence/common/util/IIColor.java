package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * Class made for easy handling of RGB hexadecimal, int array, float array point and packed format colors, as well as CMYK and HSV.
 *
 * @author GabrielV (gabriel@iiteam.net)
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 23.04.2024
 * @ii-approved 0.3.1
 * @since 20/04/2024 - 10:47 AM
 */
public class IIColor implements Comparable<IIColor>, ToIntFunction<IIColor>
{
	//--- Constants ---//
	public static final IIColor WHITE = IIColor.fromRGBA(255, 255, 255, 255);
	public static final IIColor BLACK = IIColor.fromRGBA(0, 0, 0, 255);
	public static final IIColor ALPHA = IIColor.fromRGBA(255, 255, 255, 0);

	//--- Fields ---//

	/**
	 * Alpha, Red, Green and Blue components of the color in 0-255 range.
	 */
	public final int alpha, red, green, blue;

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
	}

	//--- Creation Methods ---//

	/**
	 * Creates a new color with 0-255 ARGB int values.
	 *
	 * @param argb Alpha, Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromRGBA(int... argb)
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
		return fromRGBA(255, rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * Creates a new color with 0-1 ARGB float values.
	 *
	 * @param argb Alpha, Red, Green and Blue components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromFloatRGBA(float... argb)
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
		return fromFloatRGBA(1, rgb[0], rgb[1], rgb[2]);
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
			return IIColor.fromPackedRGBA(Integer.parseInt(hex, 16));

		return new IIColor(0, 0, 0, 0);
	}

	/**
	 * Creates a new color with a packed RGB integer.
	 *
	 * @param hex Packed RGB integer.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromPackedRGBA(int hex)
	{
		return new IIColor((hex>>24)&0xFF, (hex>>16)&0xFF, (hex>>8)&0xFF, hex&0xFF);
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
	public static IIColor fromCMYK(float[] cmyk)
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
	 * @param hsv Hue, Saturation and Value components of the color.
	 * @return A new IIColor object with the specified values.
	 */
	public static IIColor fromHSV(float[] hsv)
	{
		float h = hsv[0];
		float s = hsv[1];
		float v = hsv[2];

		float c = v*s;
		float x = c*(1-Math.abs((h/60)%2-1));
		float m = v-c;

		float r = 0;
		float g = 0;
		float b = 0;

		if(h < 60)
		{
			r = c;
			g = x;
		}
		else if(h < 120)
		{
			r = x;
			g = c;
		}
		else if(h < 180)
		{
			g = c;
			b = x;
		}
		else if(h < 240)
		{
			g = x;
			b = c;
		}
		else if(h < 300)
		{
			r = x;
			b = c;
		}
		else
		{
			r = c;
			b = x;
		}

		return new IIColor(255, (int)((r+m)*255), (int)((g+m)*255), (int)((b+m)*255));
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
		return (red<<16)|(green<<8)|blue;
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
		return new float[]{alpha/255f, red/255f, green/255f, blue/255f};
	}

	/**
	 * @return An array of RGB floating point values with values 0.0-1.0.
	 */
	public float[] getFloatRGB()
	{
		return new float[]{red/255f, green/255f, blue/255f};
	}

	//--- RGB Hex Methods ---//

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

	//--- CMYK methods ---//

	public float[] getCMYK()
	{
		float r = red/255f;
		float g = green/255f;
		float b = blue/255f;

		float k = 1-Math.max(r, Math.max(g, b));
		float c = (1-r-k)/(1-k);
		float m = (1-g-k)/(1-k);
		float y = (1-b-k)/(1-k);

		return new float[]{c, m, y, k};
	}

	//--- HSV Methods ---//

	public float[] getHSV()
	{
		float r = red/255f, g = green/255f, b = blue/255f;

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

		return new float[]{h, s, v};
	}

	//--- Dyes and TextFormatting ---//

	/**
	 * @return Closest text formatting color to this color.
	 */
	TextFormatting getTextFormatting()
	{
		return getDyeColor().chatColor;
	}

	/**
	 * @return Closest dye color to this color.
	 */
	EnumDyeColor getDyeColor()
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

	//--- Internal Utils ---//

	@Override
	public int compareTo(IIColor o)
	{
		int deltaA = alpha-o.alpha;
		int deltaR = red-o.red;
		int deltaG = green-o.green;
		int deltaB = blue-o.blue;

		return Math.abs((deltaA*deltaA+deltaR*deltaR+deltaG*deltaG+deltaB*deltaB));
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