package pl.pabilo8.immersiveintelligence.api.data.device;

import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataConnector;
import pl.pabilo8.immersiveintelligence.test.GameTestWorld;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

import static org.junit.jupiter.api.Assertions.*;

class DataWireNetworkTest extends GameTestWorld
{
	private DataWireNetwork dataWireNetwork;
	private IDataConnector connector1;
	private IDataConnector connector2;
	private IDataDevice receiver;
	private DataPacket dataPacket;
	private World world;

	@Override
	@BeforeEach
	public void setUp()
	{
		super.setUp();

		world = testManager.getWorld(0);
		dataPacket = new DataPacket();

		world.setTileEntity(new BlockPos(0, 1, 0), (TileEntity)(connector1 = new TestConnector()));
		world.setTileEntity(new BlockPos(2, 1, 0), (TileEntity)(connector2 = new TestConnector()));
		((TestConnector)connector2).setFacing(EnumFacing.DOWN);
		world.setTileEntity(new BlockPos(2, 0, 0), (TileEntity)(receiver = new TestDevice()));

		dataWireNetwork = connector1.getDataNetwork();
	}

	@Test
	void testAddConnector()
	{
		dataWireNetwork.add(connector2);
		assertEquals(2, dataWireNetwork.connectors.size());
	}

	@Test
	void testRemoveFromNetwork()
	{
		dataWireNetwork.removeFromNetwork(connector1);

		assertFalse(dataWireNetwork.connectors.contains(new WeakReference<>(connector1)));
	}

	@Test
	void testSendPacket()
	{
		//Setup
		dataWireNetwork.add(connector1);
		dataWireNetwork.add(connector2);

		//Send
		assertFalse(((TestConnector)connector1).packetSent);
		connector1.sendPacket(dataPacket);

		//Receive
		assertTrue(((TestConnector)connector1).packetSent);
		assertTrue(((TestConnector)connector2).packetReceived);
	}

	@Test
	void testSendDifferentColorPacket()
	{
		//Setup
		dataWireNetwork.add(connector1);
		dataWireNetwork.add(connector2);

		//Send, mismatched colors
		assertFalse(((TestConnector)connector1).packetSent);
		connector1.sendPacket(dataPacket.setPacketColor(EnumDyeColor.BLACK));

		//Receive, mismatched colors
		assertTrue(((TestConnector)connector1).packetSent);
		assertTrue(((TestConnector)connector2).packetReceived);
		assertNull(((TestDevice)receiver).received);

		//Send, matching colors
		((TestConnector)connector1).setColor(EnumDyeColor.ORANGE);
		((TestConnector)connector2).setColor(EnumDyeColor.ORANGE);
		connector1.sendPacket(dataPacket.setPacketColor(EnumDyeColor.ORANGE));

		//Receive, matching colors
		assertTrue(((TestConnector)connector1).packetSent);
		assertTrue(((TestConnector)connector2).packetReceived);
		//TODO: 13.12.2024 proper world tile entities
//		assertNotNull(((TestDevice)receiver).received);
	}

	private static class TestConnector extends TileEntityDataConnector
	{
		public boolean packetReceived, packetSent;

		@Override
		public void onPacketReceive(DataPacket packet)
		{
			super.onPacketReceive(packet);
			packetReceived = true;
		}

		@Override
		public void sendPacket(DataPacket packet)
		{
			super.sendPacket(packet);
			packetSent = true;
		}
	}

	private static class TestDevice extends TileEntityIEBase implements IDataDevice
	{
		public DataPacket received = null;

		@Override
		public void onReceive(DataPacket packet, @Nullable EnumFacing side)
		{
			if(side==EnumFacing.UP)
				received = packet;
		}

		@Override
		public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
		{

		}

		@Override
		public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
		{

		}
	}
}