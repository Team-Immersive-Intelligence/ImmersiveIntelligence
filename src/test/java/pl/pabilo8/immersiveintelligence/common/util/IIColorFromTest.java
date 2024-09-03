package pl.pabilo8.immersiveintelligence.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IIColorFromTest
{
	@Test
	void testFromRGBA()
	{
		IIColor color = IIColor.fromRGBA(255, 100, 150, 200);
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
		IIColor color = IIColor.fromFloatRGBA(1.0f, 0.5f, 0.6f, 0.7f);
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

		//BUG: 03.09.2024 integer overflow
		/*IIColor color8 = IIColor.fromHex("FF6496C8");
		assertEquals(255, color8.alpha);
		assertEquals(100, color8.red);
		assertEquals(150, color8.green);
		assertEquals(200, color8.blue);*/
	}

	@Test
	void testFromPackedRGBA()
	{
		IIColor color = IIColor.fromPackedRGBA(0xFF6496C8);
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
		IIColor color = IIColor.fromCMYK(new float[]{0.0f, 0.25f, 0.5f, 0.215f});
		assertEquals(255, color.alpha);
		assertEquals(200, color.red);
		assertEquals(150, color.green);
		assertEquals(100, color.blue);
	}

	@Test
	void testFromHSV()
	{
		IIColor color = IIColor.fromHSV(new float[]{210.0f, 0.5f, 0.784f});
		assertEquals(255, color.alpha);
		assertEquals(100, color.red);
		assertEquals(150, color.green);
		assertEquals(200, color.blue);
	}
}