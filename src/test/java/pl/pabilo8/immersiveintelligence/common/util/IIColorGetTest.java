package pl.pabilo8.immersiveintelligence.common.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IIColorGetTest
{
	@Test
	void testGetRed()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals(100, color.red);
	}

	@Test
	void testGetGreen()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals(150, color.green);
	}

	@Test
	void testGetBlue()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals(200, color.blue);
	}

	@Test
	void testGetAlpha()
	{
		IIColor color = IIColor.fromARGB(255, 100, 150, 200);
		assertEquals(255, color.alpha);
	}

	@Test
	void testGetPackedRGB()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals(0x6496C8, color.getPackedRGB());
	}

	@Test
	void testGetPackedARGB()
	{
		IIColor color = IIColor.fromARGB(255, 100, 150, 200);
		assertEquals(0xFF6496C8, color.getPackedARGB());
	}

	@Test
	void testGetHexRGB()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals("6496C8", color.getHexRGB());
	}

	@Test
	void testGetHexARGB()
	{
		IIColor color = IIColor.fromARGB(255, 100, 150, 200);
		assertEquals("FF6496C8", color.getHexARGB());
	}

	@Test
	void testGetCMYK()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		float[] cmyk = color.getCMYK();
		assertEquals(0.5f, cmyk[0], 0.01f); // Cyan
		assertEquals(0.25f, cmyk[1], 0.01f); // Magenta
		assertEquals(0.0f, cmyk[2], 0.01f); // Yellow
		assertEquals(0.215f, cmyk[3], 0.01f); // Black
	}

	@Test
	void testGetHSV()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		float[] hsv = color.getHSV();
		assertEquals(0.5833333f, hsv[0], 0.01f); // Hue
		assertEquals(0.5f, hsv[1], 0.01f); // Saturation
		assertEquals(0.784f, hsv[2], 0.01f); // Value
	}

	@Test
	void testGetTextFormatting()
	{
		IIColor color = IIColor.MC_RED;
		//An oddity, but it is correct; TextFormatting and Dye Colors are named inconsistently
		assertEquals(TextFormatting.DARK_RED, color.getTextFormatting());
	}

	@Test
	void testGetDyeColor()
	{
		IIColor color = IIColor.MC_RED;
		assertEquals(EnumDyeColor.RED, color.getDyeColor());
	}

	@Test
	void testGetHexCol()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		String text = "example text";
		assertEquals("<hexcol=6496C8:example text>", color.getHexCol(text));
	}
}