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
		assertFalse(dataPacket.hasAnyVariables());
		IIDataTypeUtils.registerDataTypes();
		IIDataOperationUtils.registerDataOperations();
	}

	@Test
	public void testSetAndGetVariable()
	{
		DataType booleanType = new DataTypeBoolean(true);
		assertTrue(dataPacket.setVariable('a', booleanType));
		assertEquals(booleanType, dataPacket.getPacketVariable('a'));
	}

	@Test
	public void testRemoveVariable()
	{
		DataType integerType = new DataTypeInteger(42);
		dataPacket.setVariable('b', integerType);
		assertTrue(dataPacket.removeVariable('b'));
		assertFalse(dataPacket.hasVariable('b'));
	}

	@Test
	public void testToNBT()
	{
		DataType booleanType = new DataTypeBoolean(true);
		dataPacket.setVariable('a', booleanType);
		NBTTagCompound nbt = dataPacket.toNBT();
		assertTrue(nbt.hasKey("a"));
	}

	@Test
	public void testFromNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound booleanNBT = new NBTTagCompound();
		booleanNBT.setString("Type", "boolean");
		booleanNBT.setBoolean("Value", true);
		nbt.setTag("a", booleanNBT);

		dataPacket.fromNBT(nbt);
		assertTrue(dataPacket.hasVariable('a'));
		assertTrue(((DataTypeBoolean)dataPacket.getPacketVariable('a')).value);
	}

	@Test
	public void testSetPacketColor()
	{
		dataPacket.setPacketColor(EnumDyeColor.RED);
		assertTrue(dataPacket.matchesConnector(EnumDyeColor.RED, -1));
	}

	@Test
	public void testSetPacketAddress()
	{
		dataPacket.setPacketAddress(123);
		assertTrue(dataPacket.matchesConnector(EnumDyeColor.WHITE, 123));
	}

	@Test
	public void testClone()
	{
		DataType booleanType = new DataTypeBoolean(true);
		dataPacket.setVariable('a', booleanType);
		DataPacket clonedPacket = dataPacket.clone();
		assertEquals(dataPacket, clonedPacket);
	}
}
