package pl.pabilo8.immersiveintelligence.common;

/**
 * @author GabrielV (gabriel@iiteam.net)
 * @since 20/04/2024 - 10:47 AM
 * Class made for easy handling of hexadecimal, normal, floating point and packed format colors.<br>
 * Methods <code>r(), g(), b(), a()</code> return normal 0-255 rgba format components while<br>
 * <code>fr(), fg(), fb(), fa()</code> return floating point 0-1 rgba format components.
 */
public class IIColor
{
	// RGBA 0-255
	public int r, g, b, a;

	public IIColor(int r, int g, int b, int a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public IIColor(float r, float g, float b, float a)
	{
		this.r = Math.round(r * 255);
		this.g = Math.round(g * 255);
		this.b = Math.round(b * 255);
		this.a = Math.round(a * 255);
	}

	public IIColor(int r, int g, int b)
	{
		this(r, g, b, 255);
	}

	public IIColor(float r, float g, float b)
	{
		this(r, g, b, 1f);
	}

	/* 0-255 RGBA/RGB format components */

	public int[] rgba()
	{
		return new int[] {r, g, b, a};
	}

	public int[] rgb()
	{
		return new int[] {r, g, b};
	}

	/* 0-1 RGBA/RGB format components */

	public float[] frgba()
	{
		return new float[] {(float)(r / 255), (float)(g / 255), (float)(b / 255), (float)(a / 255)};
	}

	public float[] frgb()
	{
		return new float[] {(float)(r / 255), (float)(g / 255), (float)(b / 255)};
	}

	/* Hexadecimal RGBA/RGB format */
	public String RGBAHex()
	{
		return String.format("%02X%02X%02X%02X", r, g, b, a);
	}

	public String RGBHex()
	{
		return String.format("%02X%02X%02X", r, g, b);
	}
}
