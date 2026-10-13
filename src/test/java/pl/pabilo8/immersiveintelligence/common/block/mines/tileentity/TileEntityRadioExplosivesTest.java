package pl.pabilo8.immersiveintelligence.common.block.mines.tileentity;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 13.10.2026
 */
public class TileEntityRadioExplosivesTest extends GameTestBasic
{
	private RadioNetwork previousNetwork;
	private TestExplosives explosives;

	@BeforeEach
	void setUp()
	{
		previousNetwork = RadioNetwork.INSTANCE;
		RadioNetwork.INSTANCE = new RadioNetwork();
		World world = mock(World.class);
		explosives = new TestExplosives();
		explosives.setWorld(world);
		explosives.setPos(BlockPos.ORIGIN);
	}

	@AfterEach
	void tearDown()
	{
		RadioNetwork.INSTANCE = previousNetwork;
	}

	@Test
	void packetDeliveryFinishesBeforeExplosionChangesNetwork()
	{
		DataPacket packet = new DataPacket();
		IRadioDevice sender = mock(IRadioDevice.class);
		IRadioDevice receiver = mock(IRadioDevice.class);
		when(sender.isRadioAvailable()).thenReturn(true);
		when(sender.canRelayRadio()).thenReturn(true);
		when(sender.getRange()).thenReturn(100f);
		when(sender.getDevicePosition()).thenReturn(new DimensionBlockPos(0, 0, 0, 0));
		when(receiver.isRadioAvailable()).thenReturn(true);
		when(receiver.getDevicePosition()).thenReturn(new DimensionBlockPos(1, 0, 0, 0));
		RadioNetwork.INSTANCE.addDevice(sender);
		RadioNetwork.INSTANCE.addDevice(explosives);
		RadioNetwork.INSTANCE.addDevice(receiver);

		assertDoesNotThrow(() -> RadioNetwork.INSTANCE.sendPacket(packet, sender, new ArrayList<>()));
		assertEquals(0, explosives.explosions);
		verify(receiver).onRadioReceive(packet, sender);

		explosives.onRadioReceive(packet);
		explosives.update();
		assertEquals(1, explosives.explosions);
		assertFalse(RadioNetwork.INSTANCE.getDevices().contains(explosives));
		explosives.update();
		assertEquals(1, explosives.explosions);
	}

	@Test
	void cooldownAndDisarmingPreventDetonation()
	{
		explosives.setRadioCooldown(2);
		explosives.onRadioReceive(new DataPacket());
		explosives.update();
		assertEquals(0, explosives.explosions);

		explosives.setRadioCooldown(0);
		explosives.onRadioReceive(new DataPacket());
		explosives.armed = false;
		explosives.update();
		assertEquals(0, explosives.explosions);
	}

	@Test
	void missingWorldDoesNotPrimeOrExplode()
	{
		explosives = new TestExplosives();
		assertFalse(explosives.onRadioReceive(new DataPacket()));
		assertDoesNotThrow(explosives::update);
		assertEquals(0, explosives.explosions);
	}

	private static class TestExplosives extends TileEntityRadioExplosives
	{
		private int explosions;

		private TestExplosives()
		{
			wireNetwork = mock(RedstoneWireNetwork.class);
		}

		@Override
		public void explode()
		{
			if(!armed)
				return;
			explosions++;
			RadioNetwork.INSTANCE.removeDevice(this);
			RadioNetwork.INSTANCE.sendPacket(new DataPacket(), this, new ArrayList<>());
		}

		@Override
		public DimensionBlockPos getDevicePosition()
		{
			return new DimensionBlockPos(0, 0, 0, 0);
		}
	}
}
