package pl.pabilo8.immersiveintelligence.api.data;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.08.2024
 */
public class DataPacketTest
{
	private DataPacket dataPacket;

	@BeforeEach
	public void setUp()
	{
		dataPacket = new DataPacket();
		assertFalse(dataPacket.isEmpty());
		IIDataTypeUtils.registerDataTypes();
		IIDataOperationUtils.registerDataOperations();
	}

	@Test
	public void testSetAndGetVariable()
	{
		DataType booleanType = new DataTypeBoolean(true);
		assertTrue(dataPacket.set('a', booleanType));
		assertEquals(booleanType, dataPacket.get('a'));
	}

	@Test
	public void testRemove()
	{
		DataType integerType = new DataTypeInteger(42);
		dataPacket.set('b', integerType);
		assertTrue(dataPacket.remove('b'));
		assertFalse(dataPacket.has('b'));
	}

	@Test
	public void testSerializeNBT()
	{
		DataType booleanType = new DataTypeBoolean(true);
		dataPacket.set('a', booleanType);
		NBTTagCompound nbt = dataPacket.serializeNBT();
		assertTrue(nbt.hasKey("a"));
	}

	@Test
	public void testDeserializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound booleanNBT = new NBTTagCompound();
		booleanNBT.setString("Type", "boolean");
		booleanNBT.setBoolean("Value", true);
		nbt.setTag("a", booleanNBT);

		dataPacket.deserializeNBT(nbt);
		assertTrue(dataPacket.has('a'));
		assertTrue(((DataTypeBoolean)dataPacket.get('a')).value);
	}

	@Test
	public void testWithPacketColor()
	{
		dataPacket.withPacketColor(EnumDyeColor.RED);
		assertTrue(dataPacket.matchesConnector(EnumDyeColor.RED, -1));
	}

	@Test
	public void testWithPacketAddress()
	{
		dataPacket.withPacketAddress(123);
		assertTrue(dataPacket.matchesConnector(EnumDyeColor.WHITE, 123));
	}

	@Test
	public void testClone()
	{
		DataType booleanType = new DataTypeBoolean(true);
		dataPacket.set('a', booleanType);
		DataPacket clonedPacket = dataPacket.clone();
		assertEquals(dataPacket, clonedPacket);
	}
}
