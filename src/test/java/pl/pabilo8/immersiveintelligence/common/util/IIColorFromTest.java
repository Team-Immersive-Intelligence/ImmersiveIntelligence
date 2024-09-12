package pl.pabilo8.immersiveintelligence.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IIColorFromTest
{
	//--- From (Constructors) ---//

	@Test
	void testFromARGB()
	{
		IIColor color = IIColor.fromARGB(255, 100, 150, 200);
		assertEquals(255, color.alpha);
		assertEquals(100, color.red);
		assertEquals(150, color.green);
		assertEquals(200, color.blue);
	}

	@Test
	void testFromRGB()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		assertEquals(255, color.alpha);
		assertEquals(100, color.red);
		assertEquals(150, color.green);
		assertEquals(200, color.blue);
	}

	@Test
	void testFromFloatRGBA()
	{
		IIColor color = IIColor.fromFloatARGB(1.0f, 0.5f, 0.6f, 0.7f);
		assertEquals(255, color.alpha);
		assertEquals(127, color.red);
		assertEquals(153, color.green);
		assertEquals(178, color.blue);
	}

	@Test
	void testFromFloatRGB()
	{
		IIColor color = IIColor.fromFloatRGB(0.5f, 0.6f, 0.7f);
		assertEquals(255, color.alpha);
		assertEquals(127, color.red);
		assertEquals(153, color.green);
		assertEquals(178, color.blue);
	}

	@Test
	void testFromHex()
	{
		IIColor color6 = IIColor.fromHex("6496C8");
		assertEquals(255, color6.alpha);
		assertEquals(100, color6.red);
		assertEquals(150, color6.green);
		assertEquals(200, color6.blue);

		IIColor color8 = IIColor.fromHex("FF6496C8");
		assertEquals(255, color8.alpha);
		assertEquals(100, color8.red);
		assertEquals(150, color8.green);
		assertEquals(200, color8.blue);
	}

	@Test
	void testFromPackedARGB()
	{
		IIColor color = IIColor.fromPackedARGB(0xFF6496C8);
		assertEquals(255, color.alpha);
		assertEquals(100, color.red);
		assertEquals(150, color.green);
		assertEquals(200, color.blue);
	}

	@Test
	void testFromPackedRGB()
	{
		IIColor color = IIColor.fromPackedRGB(0x6496C8);
		assertEquals(255, color.alpha);
		assertEquals(100, color.red);
		assertEquals(150, color.green);
		assertEquals(200, color.blue);
	}

	@Test
	void testFromCMYK()
	{
		IIColor color = IIColor.fromCMYK(0.0f, 0.25f, 0.5f, 0.215f);
		assertEquals(255, color.alpha);
		assertEquals(200, color.red);
		assertEquals(150, color.green);
		assertEquals(100, color.blue);
	}

	@Test
	void testFromHSV()
	{
		IIColor color = IIColor.fromHSV(0.5833333f, 0.5f, 0.784f);
		assertEquals(255, color.alpha);
		assertEquals(100, color.red, 1);
		assertEquals(150, color.green, 1);
		assertEquals(200, color.blue, 1);
	}

	//--- With (Modifiers) ---//

	@Test
	void testWithRed()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		IIColor newColor = color.withRed(50);
		assertEquals(50, newColor.red);
		assertEquals(150, newColor.green);
		assertEquals(200, newColor.blue);
	}

	@Test
	void testWithGreen()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		IIColor newColor = color.withGreen(50);
		assertEquals(100, newColor.red);
		assertEquals(50, newColor.green);
		assertEquals(200, newColor.blue);
	}

	@Test
	void testWithBlue()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		IIColor newColor = color.withBlue(50);
		assertEquals(100, newColor.red);
		assertEquals(150, newColor.green);
		assertEquals(50, newColor.blue);
	}

	@Test
	void testWithBrightness()
	{
		IIColor color = IIColor.fromRGB(100, 150, 200);
		IIColor newColor = color.withBrightness(0.5f);
		int brightness = newColor.getBrightness();
		assertEquals(brightness, 128, 1);
	}
}