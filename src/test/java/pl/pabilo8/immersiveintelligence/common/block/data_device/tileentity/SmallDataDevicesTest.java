package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataTypeUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeString;
import pl.pabilo8.immersiveintelligence.client.gui.block.data_device.GuiDataBuffer;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataAppender.AppendRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.BufferCondition;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.ConditionType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataBuffer.QueuedPacket;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.MappingRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityDataMapper.ValueMapping;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.TileEntityRedstoneSequencer.SequenceTask;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests packet isolation, queue timing and redstone task execution for small data devices.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class SmallDataDevicesTest extends GameTestBasic
{
	private World world;
	private long now;
	private int redstone;
	private TestBuffer buffer;
	private TestSequencer sequencer;
	private Receiver receiver;
	private final BlockPos position = new BlockPos(8, 64, 8);

	@BeforeEach
	void setUp()
	{
		IILogger.logger = LogManager.getLogger("small-data-devices-test");
		IIDataTypeUtils.registerDataTypes();
		NBTSerialisation.preInit();
		NBTSerialisation.postInit();
		now = 100;
		world = mock(World.class);
		when(world.getTotalWorldTime()).thenAnswer(invocation -> now);
		when(world.isBlockIndirectlyGettingPowered(any(BlockPos.class))).thenAnswer(invocation -> redstone);
		when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(true);
		receiver = new Receiver();
		when(world.getTileEntity(position.north())).thenReturn(receiver);
		buffer = new TestBuffer();
		buffer.setWorld(world);
		buffer.setPos(position);
		sequencer = new TestSequencer();
		sequencer.setWorld(world);
		sequencer.setPos(position);
	}

	@Test
	void delayAdmissionIgnoresTimeAndCopiesIncomingPacket()
	{
		BufferCondition delay = condition(ConditionType.TIME_DELAY);
		delay.ticks = 10;
		buffer.queueUnmatched = false;
		buffer.conditions.add(delay);
		DataPacket source = new DataPacket().with('a', new DataTypeInteger(1));
		buffer.onReceive(source, EnumFacing.SOUTH);
		((DataTypeInteger)source.get('a')).value = 9;
		assertEquals(1, buffer.queue.size());
		now = 109;
		buffer.update();
		assertTrue(receiver.packets.isEmpty());
		now = 110;
		buffer.update();
		assertEquals(1, ((DataTypeInteger)receiver.packets.get(0).get('a')).value);
		assertTrue(buffer.queue.isEmpty());
	}

	@Test
	void unmatchedQueueRechecksRedstoneAndDiscardModeRejects()
	{
		buffer.conditions.add(condition(ConditionType.REDSTONE_SIGNAL));
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		buffer.update();
		assertEquals(1, buffer.queue.size());
		buffer.queueUnmatched = false;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		assertEquals(1, buffer.queue.size());
		redstone = 1;
		buffer.update();
		assertEquals(1, receiver.packets.size());
	}

	@Test
	void andOrAndAccessorUseTheFullConditionSet()
	{
		BufferCondition present = condition(ConditionType.VARIABLE_PRESENT);
		BufferCondition delay = condition(ConditionType.TIME_DELAY);
		delay.useAccessor = true;
		delay.variable = 't';
		buffer.conditions.add(present);
		buffer.conditions.add(delay);
		QueuedPacket entry = new QueuedPacket();
		entry.receivedAt = 100;
		entry.packet.with('a', new DataTypeBoolean(true)).with('t', new DataTypeInteger(20));
		assertFalse(buffer.matches(entry, 119, true));
		assertTrue(buffer.matches(entry, 120, true));
		entry.packet.remove('t');
		assertFalse(buffer.matches(entry, 200, true));
		buffer.matchAll = false;
		assertTrue(buffer.matches(entry, 100, true));
		entry.packet.remove('a');
		assertFalse(buffer.matches(entry, 200, true));
	}

	@Test
	void arrivalIntervalUsesThePreviousArrivalEvenWhenDiscarded()
	{
		BufferCondition interval = condition(ConditionType.LAST_RECEIVED_DELAY);
		interval.ticks = 10;
		buffer.conditions.add(interval);
		buffer.queueUnmatched = false;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		now = 105;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		now = 115;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		assertEquals(1, buffer.queue.size());
		assertEquals(10, buffer.queue.get(0).receivedDelay);
		buffer.onReceive(new DataPacket(), EnumFacing.NORTH);
		assertEquals(1, buffer.queue.size());
	}

	@Test
	void expiryWinsAtTheBoundaryAndMissingOutputRetainsPackets()
	{
		when(world.getTileEntity(position.north())).thenReturn(null);
		buffer.terminationTime = 10;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		now = 109;
		buffer.update();
		assertEquals(1, buffer.queue.size());
		now = 110;
		buffer.update();
		assertTrue(buffer.queue.isEmpty());
		buffer.terminationTime = 0;
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		now = 100000;
		buffer.update();
		assertEquals(1, buffer.queue.size());
	}

	@Test
	void readyPacketsBypassUnreadyEntriesAndDispatchCanReenter()
	{
		BufferCondition present = condition(ConditionType.VARIABLE_PRESENT);
		buffer.conditions.add(present);
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		buffer.onReceive(new DataPacket().with('a', new DataTypeInteger(1)), EnumFacing.SOUTH);
		receiver.onReceive = () -> buffer.onReceive(new DataPacket().with('a', new DataTypeInteger(2)), EnumFacing.SOUTH);
		assertDoesNotThrow(buffer::update);
		assertEquals(1, receiver.packets.size());
		assertEquals(2, buffer.queue.size());
	}

	@Test
	void mapperUsesExactTypesOriginalInputsAndPreservesMetadata()
	{
		TileEntityDataMapper mapper = new TileEntityDataMapper();
		MappingRule rule = new MappingRule();
		rule.output = 's';
		for(int number : new int[]{1, 2, 5})
		{
			ValueMapping pair = new ValueMapping();
			pair.expected = new DataTypeInteger(number);
			pair.output = new DataTypeString("word"+number);
			rule.values.add(pair);
		}
		mapper.mappings.add(rule);
		MappingRule cascading = new MappingRule();
		cascading.input = 's';
		cascading.output = 't';
		cascading.inputType = new DataTypeString();
		ValueMapping pair = new ValueMapping();
		pair.expected = new DataTypeString("word1");
		pair.output = new DataTypeString("unexpected");
		cascading.values.add(pair);
		mapper.mappings.add(cascading);
		for(int number : new int[]{1, 2, 5})
		{
			DataPacket source = new DataPacket().with('a', new DataTypeInteger(number))
					.withPacketAddress(42).withPacketColor(EnumDyeColor.BLUE);
			DataPacket result = mapper.map(source);
			assertFalse(result.has('a'));
			assertEquals("word"+number, ((DataTypeString)result.get('s')).value);
			assertFalse(result.has('t'));
			assertTrue(source.has('a'));
			assertTrue(result.matchesConnector(EnumDyeColor.BLUE, 42));
		}
		assertTrue(mapper.map(new DataPacket().with('a', new DataTypeFloat(1))).has('a'));
		assertTrue(mapper.map(new DataPacket().with('a', new DataTypeInteger(3))).has('a'));
	}

	@Test
	void appenderHonoursOrderOverwriteAndIndependentValues()
	{
		TileEntityDataAppender appender = new TileEntityDataAppender();
		AppendRule add = new AppendRule();
		add.value = new DataTypeInteger(5);
		appender.entries.add(add);
		DataPacket source = new DataPacket().with('a', new DataTypeInteger(1));
		assertEquals(1, ((DataTypeInteger)appender.append(source).get('a')).value);
		add.overwrite = true;
		DataPacket replaced = appender.append(source);
		assertEquals(5, ((DataTypeInteger)replaced.get('a')).value);
		((DataTypeInteger)replaced.get('a')).value = 7;
		assertEquals(5, ((DataTypeInteger)add.value).value);
		assertEquals(1, ((DataTypeInteger)source.get('a')).value);
		AppendRule remove = new AppendRule();
		remove.remove = true;
		appender.entries.add(remove);
		assertFalse(appender.append(source).has('a'));
		appender.entries.add(appender.entries.copyEntry(add));
		assertEquals(5, ((DataTypeInteger)appender.append(source).get('a')).value);
	}

	@Test
	void sequencerProducesTimedGapsAndHoldsConstantSteps()
	{
		SequenceTask pulse = new SequenceTask();
		pulse.duration = 2;
		SequenceTask gap = new SequenceTask();
		gap.strength = 0;
		gap.duration = 1;
		SequenceTask hold = new SequenceTask();
		hold.strength = 7;
		hold.constant = true;
		sequencer.tasks.add(pulse);
		sequencer.tasks.add(gap);
		sequencer.tasks.add(hold);
		sequencer.update();
		assertEquals(15, sequencer.signal);
		sequencer.update();
		assertEquals(15, sequencer.signal);
		sequencer.update();
		assertEquals(0, sequencer.signal);
		sequencer.update();
		assertEquals(7, sequencer.signal);
		for(int i = 0; i < 30; i++)
			sequencer.update();
		assertEquals(2, sequencer.currentTask);
		sequencer.onReceive(command("next"), EnumFacing.SOUTH);
		sequencer.update();
		assertEquals(15, sequencer.signal);
		assertEquals(15, sequencer.getWeakRSOutput(Blocks.STONE.getDefaultState(), EnumFacing.SOUTH));
		assertEquals(0, sequencer.getWeakRSOutput(Blocks.STONE.getDefaultState(), EnumFacing.NORTH));
	}

	@Test
	void finiteRequestsExpireAndOldGuiCannotRestoreThem()
	{
		sequencer.onReceive(command("add").with('e', new DataTypeInteger(2)).with('t', new DataTypeInteger(1)), EnumFacing.SOUTH);
		NBTTagCompound stale = new NBTTagCompound();
		stale.setInteger("expectedRevision", sequencer.revision);
		stale.setTag("tasks", sequencer.tasks.serializeNBT());
		sequencer.update();
		assertEquals(1, sequencer.tasks.get(0).repeats);
		sequencer.receiveMessageFromClient(stale);
		assertEquals(1, sequencer.tasks.get(0).repeats);
		sequencer.update();
		assertTrue(sequencer.tasks.isEmpty());
		sequencer.update();
		assertEquals(0, sequencer.signal);
		sequencer.receiveMessageFromClient(stale);
		assertTrue(sequencer.tasks.isEmpty());
	}

	@Test
	void commandsClampValuesAndRejectTheWrongInputSide()
	{
		DataPacket add = command("add").with('s', new DataTypeInteger(100)).with('t', new DataTypeInteger(-1));
		sequencer.onReceive(add, EnumFacing.NORTH);
		assertTrue(sequencer.tasks.isEmpty());
		sequencer.onReceive(add, EnumFacing.SOUTH);
		assertEquals(15, sequencer.tasks.get(0).strength);
		assertEquals(1, sequencer.tasks.get(0).duration);
		sequencer.onReceive(command("stop"), EnumFacing.SOUTH);
		assertFalse(sequencer.running);
		sequencer.onReceive(command("clear"), EnumFacing.SOUTH);
		assertTrue(sequencer.tasks.isEmpty());
	}

	@Test
	void configurationCannotReplaceRuntimeQueueFacingOrSignal()
	{
		NBTTagCompound hostile = new NBTTagCompound();
		hostile.setTag("queue", new NBTTagList());
		hostile.setInteger("facing", EnumFacing.EAST.ordinal());
		hostile.setInteger("signal", 100);
		hostile.setInteger("termination_time", -20);
		buffer.onReceive(new DataPacket(), EnumFacing.SOUTH);
		buffer.receiveMessageFromClient(hostile);
		assertEquals(1, buffer.queue.size());
		assertEquals(EnumFacing.NORTH, buffer.facing);
		assertEquals(0, buffer.terminationTime);
		hostile.setInteger("expectedRevision", sequencer.revision);
		sequencer.receiveMessageFromClient(hostile);
		assertEquals(0, sequencer.signal);
		assertEquals(EnumFacing.NORTH, sequencer.facing);
	}

	@Test
	void saveReloadRetainsQueueAndActiveSequenceProgress()
	{
		buffer.onReceive(new DataPacket().with('a', new DataTypeInteger(4)), EnumFacing.SOUTH);
		NBTTagCompound saved = new NBTTagCompound();
		buffer.writeCustomNBT(saved, false);
		TestBuffer restored = new TestBuffer();
		restored.readCustomNBT(saved, false);
		assertEquals(100, restored.queue.get(0).receivedAt);
		assertEquals(4, ((DataTypeInteger)restored.queue.get(0).packet.get('a')).value);
		sequencer.tasks.add(new SequenceTask());
		sequencer.update();
		sequencer.writeCustomNBT(saved = new NBTTagCompound(), false);
		TestSequencer restoredSequence = new TestSequencer();
		restoredSequence.readCustomNBT(saved, false);
		assertTrue(restoredSequence.active);
		assertEquals(19, restoredSequence.remainingTicks);
		assertEquals(15, restoredSequence.signal);
	}

	@Test
	void boundedConfigurationRejectsInvalidLettersAndLimitsLists()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("variable", "!");
		nbt.setInteger("signal", 999);
		BufferCondition condition = new BufferCondition();
		condition.deserializeNBT(nbt);
		assertEquals('a', condition.variable);
		assertEquals(16, condition.signal);
		NBTTagList list = new NBTTagList();
		for(int i = 0; i < 100; i++)
			list.appendTag(condition.serializeNBT());
		nbt.setTag("conditions", list);
		buffer.receiveMessageFromClient(nbt);
		assertEquals(DataDeviceUtils.MAX_RULES, buffer.conditions.size());
	}

	@Test
	void guiSyncEventUsesTheServerConfigurationContract()
	{
		GuiDataBuffer gui = new GuiDataBuffer(null, null);
		gui.matchAll = false;
		gui.queueUnmatched = false;
		gui.terminationTime = 345;
		gui.conditions.add(condition(ConditionType.VARIABLE_PRESENT));
		NBTTagCompound message = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(gui, (serializer, object) ->
				serializer.serializeForEvent(object, message, SyncEvents.TILE_CLIENT_MESSAGE));
		buffer.receiveMessageFromClient(message);
		assertFalse(buffer.matchAll);
		assertFalse(buffer.queueUnmatched);
		assertEquals(345, buffer.terminationTime);
		assertEquals(ConditionType.VARIABLE_PRESENT, buffer.conditions.get(0).type);
	}

	private static DataPacket command(String command)
	{
		return new DataPacket().with('c', new DataTypeString(command));
	}

	@Test
	void staleGuiCanStopOutputWithoutRestoringConsumedRequests()
	{
		sequencer.onReceive(command("add").with('e', new DataTypeInteger(2)).with('t', new DataTypeInteger(1)), EnumFacing.SOUTH);
		NBTTagCompound message = new NBTTagCompound();
		message.setInteger("expectedRevision", sequencer.revision);
		message.setTag("tasks", sequencer.tasks.serializeNBT());
		message.setBoolean("running", false);
		sequencer.update();
		sequencer.receiveMessageFromClient(message);
		assertFalse(sequencer.running);
		assertEquals(0, sequencer.signal);
		assertEquals(1, sequencer.tasks.get(0).repeats);
	}

	private static BufferCondition condition(ConditionType type)
	{
		BufferCondition condition = new BufferCondition();
		condition.type = type;
		return condition;
	}

	private static class TestBuffer extends TileEntityDataBuffer
	{
		@Override
		public void markDirty()
		{
		}

		@Override
		public void updateTileForEvent(SyncEvents event)
		{
		}
	}

	private static class TestSequencer extends TileEntityRedstoneSequencer
	{
		@Override
		public void markDirty()
		{
		}

		@Override
		public void updateTileForEvent(SyncEvents event)
		{
		}

		@Override
		public Block getBlockType()
		{
			return Blocks.STONE;
		}
	}

	private static class Receiver extends TileEntity implements IDataDevice
	{
		private final List<DataPacket> packets = new ArrayList<>();
		private Runnable onReceive;

		@Override
		public void onReceive(DataPacket packet, EnumFacing side)
		{
			packets.add(packet);
			if(onReceive!=null)
				onReceive.run();
		}
	}
}
