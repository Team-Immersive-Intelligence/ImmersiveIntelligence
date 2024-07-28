package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
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

	public static String getHexCol(int color, String text)
	{
		return getHexCol(Integer.toHexString(color), text);
	}

	public static String getHexCol(IIColor color, String text)
	{
		return getHexCol(color.getHexRGB(), text);
	}

	public static String getHexCol(String color, String text)
	{
		return String.format("<hexcol=%s:%s>", color, text);
	}

	public static int RGBAToRGB(int color)
	{
		return color-(color>>24&0xFF);
	}

	/**
	 * based on <a href="https://stackoverflow.com/a/2262117/9876980">https://stackoverflow.com/a/2262117/9876980</a>
	 *
	 * @param rgb color in rgbInt
	 * @return color in rgbFloats format
	 */
	public static float[] rgbIntToRGB(int rgb)
	{
		float r = ((rgb>>16)&0x0ff)*0.003921f;
		float g = ((rgb>>8)&0x0ff)*0.003921f;
		float b = (rgb&0x0ff)*0.003921f;
		return new float[]{r, g, b};
	}

	static double colorDistance(int a, int b)
	{
		float[] f1 = rgbIntToRGB(a);
		float[] f2 = rgbIntToRGB(b);
		return colorDistance(f1, f2);
	}

	static double colorDistance(float[] f1, float[] f2)
	{
		int deltaR = (int)(f1[0]*255-f2[0]*255);
		int deltaG = (int)(f1[1]*255-f2[1]*255);
		int deltaB = (int)(f1[2]*255-f2[2]*255);
		return Math.abs((deltaR*deltaR+deltaG*deltaG+deltaB*deltaB)/3.0);
	}

	public static int[] rgbToCmyk(int red, int green, int blue)
	{
		return new int[]{255-red, 255-green, 255-blue, 255-Math.min(red, Math.max(green, blue))};
	}

	/**
	 * @param r red amount (0-1)
	 * @param g green amount (0-1)
	 * @param b blue amount (0-1)
	 * @return float cmyk color array with values 0-1
	 */
	public static float[] rgbToCmyk(float r, float g, float b)
	{
		int[] cmyk = rgbToCmyk((int)(r*255), (int)(g*255), (int)(b*255));
		return new float[]{cmyk[0]/255f, cmyk[1]/255f, cmyk[2]/255f, cmyk[3]/255f};
	}

	public static float[] rgbToCmyk(float[] rgb)
	{
		return rgbToCmyk(rgb[0], rgb[1], rgb[2]);
	}

	/**
	 * @param cyan    cyan amount (0-255)
	 * @param magenta magenta amount (0-255)
	 * @param yellow  yellow amount (0-255)
	 * @param black   black amount (0-255)
	 * @return float cmyk color array with values 0-1
	 */
	public static int[] cmykToRgb(int cyan, int magenta, int yellow, int black)
	{
		return new int[]{Math.min(255-black, 255-cyan), Math.min(255-black, 255-magenta), Math.min(255-black, 255-yellow)};
	}

	public static float[] cmykToRgb(float c, float m, float y, float b)
	{
		int[] dec = cmykToRgb((int)(c*255), (int)(m*255), (int)(y*255), (int)(b*255));
		return new float[]{dec[0]/255f, dec[1]/255f, dec[2]/255f};
	}

	/**
	 * stolen from MathHelper class
	 *
	 * @param hue        hue amount (NOT DEGREES) in 0-1
	 * @param saturation saturation amount in 0-1
	 * @param value      value in 0-1
	 * @return float rgb color array with values 0-1
	 */
	public static float[] hsvToRgb(float hue, float saturation, float value)
	{
		int i = (int)(hue*6.0F)%6;
		float f = hue*6.0F-(float)i;
		float f1 = value*(1.0F-saturation);
		float f2 = value*(1.0F-f*saturation);
		float f3 = value*(1.0F-(1.0F-f)*saturation);
		float r, g, b;

		switch(i)
		{
			case 0:
				r = value;
				g = f3;
				b = f1;
				break;
			case 1:
				r = f2;
				g = value;
				b = f1;
				break;
			case 2:
				r = f1;
				g = value;
				b = f3;
				break;
			case 3:
				r = f1;
				g = f2;
				b = value;
				break;
			case 4:
				r = f3;
				g = f1;
				b = value;
				break;
			case 5:
				r = value;
				g = f1;
				b = f2;
				break;
			default:
				r = 0;
				g = 0;
				b = 0;
				break;
		}

		return new float[]{r, g, b};
	}

	/**
	 * based on formula from <a href="https://www.rapidtables.com/convert/color/rgb-to-hsv.html">https://www.rapidtables.com/convert/color/rgb-to-hsv.html</a>
	 *
	 * @param r red amount (0-1)
	 * @param g green amount (0-1)
	 * @param b blue amount (0-1)
	 * @return float hsv array with values 0-1
	 */
	public static float[] rgbToHsv(float r, float g, float b)
	{
		float cMax = Math.max(Math.max(r, g), b);
		float cMin = Math.min(Math.min(r, g), b);
		float d = cMax-cMin;

		float s = cMax!=0?d/cMax: 0;
		float h;
		if(d==0)
			h = 0;
		else if(cMax==r)
			h = (((g-b)/d)%6f)/6f;
		else if(cMax==g)
			h = (((b-r)/d)+2)/6f;
		else if(cMax==b)
			h = (((r-g)/d)+4)/6f;
		else
			h = 0;

		if(h < 0)
			h = 1f+h;

		return new float[]{h, s, cMax};
	}

	/**
	 * @param color color in rgbInt
	 * @return closest dye color
	 */
	public static EnumDyeColor getRGBTextFormatting(int color)
	{
		float[] cc = rgbIntToRGB(color);
		Optional<EnumDyeColor> min = Arrays.stream(EnumDyeColor.values()).min(Comparator.comparingDouble(value -> colorDistance(value.getColorComponentValues(), cc)));
		return min.orElse(EnumDyeColor.BLACK);
	}

	//--- RGB Int Methods ---//

	/**
	 * Makes an integer color from the given red, green, and blue float (0-1) values
	 * Stolen from MathHelper because of Side=Client annotation
	 */
	public static int rgb(float rIn, float gIn, float bIn)
	{
		return rgb(MathHelper.floor(rIn*255.0F), MathHelper.floor(gIn*255.0F), MathHelper.floor(bIn*255.0F));
	}

	/**
	 * Makes a single int color with the given red, green, and blue (0-255) values.
	 * Stolen from MathHelper because of Side=Client annotation
	 */
	public static int rgb(int rIn, int gIn, int bIn)
	{
		int lvt_3_1_ = (rIn<<8)+gIn;
		lvt_3_1_ = (lvt_3_1_<<8)+bIn;


		return lvt_3_1_;
	}

	/**
	 * @param colour1    in 3 float array format
	 * @param colour2    in 3 float array format
	 * @param proportion how much of second color is mixed to the first one
	 * @return color in between
	 */
	public static float[] medColour(float[] colour1, float[] colour2, float proportion)
	{
		float rev = 1f-proportion;
		return new float[]{
				(colour1[0]*rev+colour2[0]*proportion),
				(colour1[1]*rev+colour2[1]*proportion),
				(colour1[2]*rev+colour2[2]*proportion)
		};
	}

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
