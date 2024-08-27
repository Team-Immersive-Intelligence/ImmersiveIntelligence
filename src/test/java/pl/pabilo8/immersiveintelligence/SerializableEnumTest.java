package pl.pabilo8.immersiveintelligence;

import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SerializableEnumTest
{
	private enum TestEnum implements ISerializableEnum
	{
		ADIN,
		dwA,
		Tr1
	}

	@Test
	public void testGetName()
	{
		assertEquals("adin", TestEnum.ADIN.getName());
		assertEquals("dwa", TestEnum.dwA.getName());
		assertEquals("tr1", TestEnum.Tr1.getName());
	}
}
