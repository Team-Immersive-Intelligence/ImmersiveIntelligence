package pl.pabilo8.immersiveintelligence.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.ammo.enums.CoreType;
import pl.pabilo8.immersiveintelligence.api.data.DataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.*;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.08.2024
 */
public class DataHandlingUtilsTest
{
	private DataPacket dataPacket;

	@BeforeEach
	public void setUp()
	{
		dataPacket = new DataPacket();
	}

	@Test
	public void testAsInt()
	{
		dataPacket.setVariable('a', new DataTypeInteger(42));
		assertEquals(42, DataHandlingUtils.asInt('a', dataPacket));
	}

	@Test
	public void testAsFloat()
	{
		dataPacket.setVariable('a', new DataTypeFloat(3.14f));
		assertEquals(3.14f, DataHandlingUtils.asFloat('a', dataPacket));
	}

	@Test
	public void testAsString()
	{
		dataPacket.setVariable('a', new DataTypeString("test"));
		assertEquals("test", DataHandlingUtils.asString('a', dataPacket));
	}

	@Test
	public void testExpectingNumericParam()
	{
		dataPacket.setVariable('a', new DataTypeFloat(3.14f));
		Consumer<Float> consumer = value -> assertEquals(3.14f, value);
		assertTrue(DataHandlingUtils.expectingNumericParam('a', dataPacket, consumer));
	}

	@Test
	public void testExpectingBooleanParam()
	{
		dataPacket.setVariable('a', new DataTypeBoolean(true));
		Consumer<Boolean> consumer = value -> assertTrue(value);
		assertTrue(DataHandlingUtils.expectingBooleanParam('a', dataPacket, consumer));
	}

	@Test
	public void testExpectingStringParam()
	{
		dataPacket.setVariable('a', new DataTypeString("test"));
		Consumer<String> consumer = value -> assertEquals("test", value);
		assertTrue(DataHandlingUtils.expectingStringParam('a', dataPacket, consumer));
	}

	@Test
	public void testExpectingEnumParam()
	{
		dataPacket.setVariable('a', new DataTypeString("piercing_sabot"));
		Consumer<CoreType> consumer = value -> assertEquals(CoreType.PIERCING_SABOT, value);
		assertTrue(DataHandlingUtils.expectingEnumParam('a', dataPacket, CoreType.class, consumer));
	}

	@Test
	public void testHandleCallback()
	{
		dataPacket.setVariable('a', new DataTypeString("ab"));
		dataPacket.setVariable('b', new DataTypeString("cd"));
		dataPacket.setVariable('c', new DataTypeString("cb"));
		DataPacket responsePacket = DataHandlingUtils.handleCallback(dataPacket, s -> {
			switch(s)
			{
				case "ab":
					return new DataTypeString("response");
				case "cd":
					return new DataTypeString("response2");
				default:
					return new DataTypeNull();
			}
		});
		assertNotNull(responsePacket);
		assertEquals("response", ((DataTypeString)responsePacket.getPacketVariable('a')).value);
		assertEquals("response2", ((DataTypeString)responsePacket.getPacketVariable('b')).value);
		assertEquals(new DataTypeNull(), responsePacket.getPacketVariable('c'));
	}
}
