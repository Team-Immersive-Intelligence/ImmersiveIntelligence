package pl.pabilo8.immersiveintelligence.api.data.radio;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Checks radio routing, range, cooldown, and relay behavior
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 24.09.2026
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
		assertFalse(radioNetwork.addDevice(mockDevice1)); //Adding the same device again should return false
	}

	@Test
	public void testRemoveDevice()
	{
		radioNetwork.addDevice(mockDevice1);
		assertTrue(radioNetwork.removeDevice(mockDevice1));
		assertFalse(radioNetwork.removeDevice(mockDevice1)); //Removing the same device again should return false
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
		when(mockDevice1.isRadioAvailable()).thenReturn(true);
		when(mockDevice2.isRadioAvailable()).thenReturn(true);
		when(mockDevice1.canRelayRadio()).thenReturn(true);
		when(mockDevice1.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 50, 0));
		when(mockDevice2.onRadioReceive(mockPacket, mockDevice1)).thenReturn(true);

		//Create radio network
		createRadioNetwork();

		//Same frequency
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1).onRadioSend(mockPacket);
		verify(mockDevice2).onRadioReceive(mockPacket, mockDevice1);
		clearInvocations(mockDevice1, mockDevice2);

		//Different frequency
		when(mockDevice1.getFrequency()).thenReturn(5);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1).onRadioSend(mockPacket);
		verify(mockDevice2, never()).onRadioReceive(mockPacket, mockDevice1);
	}

	@Test
	public void testRelayPermissionAndOriginalSender()
	{
		IRadioDevice destination = mock(IRadioDevice.class);
		when(mockDevice1.getFrequency()).thenReturn(1);
		when(mockDevice2.getFrequency()).thenReturn(1);
		when(destination.getFrequency()).thenReturn(1);
		when(mockDevice1.getRange()).thenReturn(60.0f);
		when(mockDevice2.getRange()).thenReturn(60.0f);
		when(mockDevice1.isRadioAvailable()).thenReturn(true);
		when(mockDevice2.isRadioAvailable()).thenReturn(true);
		when(destination.isRadioAvailable()).thenReturn(true);
		when(mockDevice1.canRelayRadio()).then(InvocationOnMock::callRealMethod);
		when(mockDevice2.canRelayRadio()).thenReturn(false);
		when(mockDevice1.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 50, 0));
		when(destination.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 100, 0));
		when(mockDevice2.onRadioReceive(mockPacket, mockDevice1)).thenReturn(true);
		when(destination.onRadioReceive(mockPacket, mockDevice1)).thenReturn(true);

		assertTrue(mockDevice1.canRelayRadio());
		createRadioNetwork();
		radioNetwork.addDevice(destination);

		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice2).onRadioReceive(mockPacket, mockDevice1);
		verify(destination, never()).onRadioReceive(mockPacket, mockDevice1);

		clearInvocations(mockDevice1, mockDevice2, destination);
		when(mockDevice2.canRelayRadio()).thenReturn(true);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(destination).onRadioReceive(mockPacket, mockDevice1);

		clearInvocations(mockDevice1, mockDevice2, destination);
		when(mockDevice1.canRelayRadio()).thenReturn(false);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1, never()).onRadioSend(mockPacket);
		verify(mockDevice2, never()).onRadioReceive(mockPacket, mockDevice1);
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

	@Test
	public void testCooldownCheck()
	{
		when(mockDevice1.getFrequency()).thenReturn(1);
		when(mockDevice2.getFrequency()).thenReturn(1);
		when(mockDevice1.getRange()).thenReturn(100.0f);
		when(mockDevice1.isRadioAvailable()).then(InvocationOnMock::callRealMethod);
		when(mockDevice2.isRadioAvailable()).then(InvocationOnMock::callRealMethod);
		when(mockDevice1.canRelayRadio()).thenReturn(true);
		when(mockDevice1.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(mockDevice2.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 50, 0));
		when(mockDevice2.onRadioReceive(mockPacket, mockDevice1)).thenReturn(true);

		createRadioNetwork();

		//No jamming
		when(mockDevice1.getRadioCooldown()).thenReturn(0);
		when(mockDevice2.getRadioCooldown()).thenReturn(0);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1).onRadioSend(mockPacket);
		verify(mockDevice2).onRadioReceive(mockPacket, mockDevice1);
		clearInvocations(mockDevice1, mockDevice2);

		//Receiver is being jammed
		when(mockDevice1.getRadioCooldown()).thenReturn(0);
		when(mockDevice2.getRadioCooldown()).thenReturn(100);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1).onRadioSend(mockPacket);
		verify(mockDevice2, never()).onRadioReceive(mockPacket, mockDevice1);
		clearInvocations(mockDevice1, mockDevice2);

		//Sender is being jammed
		when(mockDevice1.getRadioCooldown()).thenReturn(100);
		when(mockDevice2.getRadioCooldown()).thenReturn(0);
		radioNetwork.sendPacket(mockPacket, mockDevice1, new ArrayList<>());
		verify(mockDevice1, never()).onRadioSend(mockPacket);
		verify(mockDevice2, never()).onRadioReceive(mockPacket, mockDevice1);
	}

	//--- Utils ---//

	private void createRadioNetwork()
	{
		radioNetwork.addDevice(mockDevice1);
		radioNetwork.addDevice(mockDevice2);
	}

}
