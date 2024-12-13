package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.12.2024
 */
public class RadioNetworkTest
{
	private RadioNetwork radioNetwork;
	private IRadioDevice mockDevice1;
	private IRadioDevice mockDevice2;
	private DataPacket mockPacket;

	@BeforeEach
	public void setUp()
	{
		radioNetwork = new RadioNetwork();
		mockDevice1 = mock(IRadioDevice.class);
		mockDevice2 = mock(IRadioDevice.class);
		mockPacket = mock(DataPacket.class);
	}

	@Test
	public void testAddDevice()
	{
		assertTrue(radioNetwork.addDevice(mockDevice1));
		assertFalse(radioNetwork.addDevice(mockDevice1)); // Adding the same device again should return false
	}

	@Test
	public void testRemoveDevice()
	{
		radioNetwork.addDevice(mockDevice1);
		assertTrue(radioNetwork.removeDevice(mockDevice1));
		assertFalse(radioNetwork.removeDevice(mockDevice1)); // Removing the same device again should return false
	}

	@Test
	public void testClearDevices()
	{
		radioNetwork.addDevice(mockDevice1);
		radioNetwork.addDevice(mockDevice2);
		radioNetwork.clearDevices();
		assertTrue(radioNetwork.getDevices().isEmpty());
	}

	@Test
	public void testSendPacket()
	{
		when(mockDevice1.getFrequency()).thenReturn(1);
		when(mockDevice2.getFrequency()).thenReturn(1);
		when(mockDevice1.getRange()).thenReturn(100.0f);
		when(mockDevice1.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 50, 0));
		when(mockDevice2.onRadioReceive(mockPacket)).thenReturn(true);

		radioNetwork.addDevice(mockDevice1);
		radioNetwork.addDevice(mockDevice2);

		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());

		verify(mockDevice1).onRadioSend(mockPacket);
		verify(mockDevice2).onRadioReceive(mockPacket);
	}

	@Test
	public void testDistanceCheck()
	{
		when(mockDevice1.getRange()).thenReturn(100.0f);
		when(mockDevice1.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 50, 0));

		assertTrue(radioNetwork.distanceCheck(mockDevice1, mockDevice2));

		//Same dimension, too far
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 150, 0));
		assertFalse(radioNetwork.distanceCheck(mockDevice1, mockDevice2));

		//Different dimension
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 1));
		assertFalse(radioNetwork.distanceCheck(mockDevice1, mockDevice2));
	}
}