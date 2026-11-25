package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
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
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.nixie.TileEntityNixieDisplayBase.DisplayMode;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests Nixie display chaining, packet isolation, input sides and saved settings.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 26.10.2026
 */
class NixieDisplayTest extends GameTestBasic
{
	@BeforeEach
	void setUp()
	{
		IILogger.logger = LogManager.getLogger("nixie-display-test");
		IIDataTypeUtils.registerDataTypes();
		NBTSerialisation.preInit();
		NBTSerialisation.postInit();
	}

	@Test
	void mixedDisplaysConsumeTextAndClearOldCharacters()
	{
		TileEntityDoubleNixieDisplay first = new TileEntityDoubleNixieDisplay();
		TileEntityQuadNixieDisplay second = new TileEntityQuadNixieDisplay();
		DataPacket source = new DataPacket().with('a', new DataTypeString("01234567"));
		DataPacket remainder = second.display(first.display(source));
		assertEquals("01", first.displayedText);
		assertEquals("2345", second.displayedText);
		assertEquals("67", remainder.get('a').toString());
		assertEquals("01234567", source.get('a').toString());

		second.display(first.display(new DataPacket().with('a', new DataTypeString("8"))));
		assertEquals("8 ", first.displayedText);
		assertEquals("    ", second.displayedText);
	}

	@Test
	void numericRemaindersPreserveZeroesSignsAndDecimalPoints()
	{
		TileEntityDoubleNixieDisplay first = new TileEntityDoubleNixieDisplay();
		TileEntityQuadNixieDisplay second = new TileEntityQuadNixieDisplay();
		DataPacket remainder = first.display(new DataPacket().with('a', new DataTypeInteger(120034)));
		assertEquals("12", first.displayedText);
		assertInstanceOf(DataTypeString.class, remainder.get('a'));
		second.display(remainder);
		assertEquals("0034", second.displayedText);

		second.display(first.display(new DataPacket().with('a', new DataTypeFloat(-12.5f))));
		assertEquals("-1", first.displayedText);
		assertEquals("2.5 ", second.displayedText);
	}

	@Test
	void digitModeConsumesIndependentVariablesAndSupportsRepeatedLetters()
	{
		TileEntityQuadNixieDisplay tile = new TileEntityQuadNixieDisplay();
		tile.mode = DisplayMode.DIGITS;
		tile.variables = "abca";
		DataPacket source = new DataPacket()
				.with('a', new DataTypeString("12"))
				.with('b', new DataTypeInteger(7))
				.with('c', new DataTypeString("AB"));
		DataPacket remainder = tile.display(source);
		assertEquals("17A2", tile.displayedText);
		assertEquals("", remainder.get('a').toString());
		assertEquals("", remainder.get('b').toString());
		assertEquals("B", remainder.get('c').toString());
		assertEquals("12", source.get('a').toString());
	}

	@Test
	void missingAndUnsupportedValuesLeaveBlankTubesWithoutChangingOtherData()
	{
		TileEntityDoubleNixieDisplay tile = new TileEntityDoubleNixieDisplay();
		DataPacket source = new DataPacket().with('a', new DataTypeBoolean(true)).with('z', new DataTypeInteger(9));
		NBTTagCompound before = source.serializeNBT();
		before.setInteger("color", EnumDyeColor.ORANGE.getMetadata());
		before.setInteger("address", 42);
		source = new DataPacket(before);
		assertEquals(before, tile.display(source).serializeNBT());
		assertEquals("  ", tile.displayedText);
		tile.display(new DataPacket());
		assertEquals("  ", tile.displayedText);
		assertEquals(before, source.serializeNBT());
	}

	@Test
	void settingsFacingAndDisplayedTextSurviveNbtRoundTrip()
	{
		TileEntityQuadNixieDisplay tile = new TileEntityQuadNixieDisplay();
		tile.mode = DisplayMode.DIGITS;
		tile.variables = "wxyz";
		tile.facing = EnumFacing.EAST;
		tile.displayedText = "TEST";
		NBTTagCompound saved = new NBTTagCompound();
		tile.writeCustomNBT(saved, false);
		TileEntityQuadNixieDisplay loaded = new TileEntityQuadNixieDisplay();
		loaded.readCustomNBT(saved, false);
		assertEquals(tile.mode, loaded.mode);
		assertEquals(tile.variables, loaded.variables);
		assertEquals(tile.facing, loaded.facing);
		assertEquals(tile.displayedText, loaded.displayedText);
	}

	@Test
	void clientMessagesCannotSetDisplayTextFacingOrInvalidSettings()
	{
		TileEntityDoubleNixieDisplay tile = new TileEntityDoubleNixieDisplay();
		NBTTagCompound message = new NBTTagCompound();
		message.setString("displayedText", "XX");
		message.setInteger("facing", EnumFacing.UP.getIndex());
		message.setString("mode", "invalid");
		message.setString("variables", "A");
		tile.receiveMessageFromClient(message);
		assertEquals("", tile.displayedText);
		assertEquals(EnumFacing.NORTH, tile.facing);
		assertEquals(DisplayMode.NUMBER_TEXT, tile.mode);
		assertEquals("abcd", tile.variables);
	}

	@Test
	void onlyBottomAndLeftInputsForwardToTheRightForEveryFacing() throws ReflectiveOperationException
	{
		Field remote = World.class.getField("isRemote");
		remote.setAccessible(true);
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			World world = mock(World.class);
			BlockPos pos = new BlockPos(8, 64, 8);
			when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(true);
			Receiver receiver = new Receiver();
			when(world.getTileEntity(pos.offset(facing.rotateYCCW()))).thenReturn(receiver);
			TestDisplay tile = new TestDisplay();
			tile.setWorld(world);
			tile.setPos(pos);
			tile.facing = facing;
			DataPacket packet = new DataPacket().with('a', new DataTypeString("1234"));
			for(EnumFacing side : EnumFacing.values())
			{
				receiver.packet = null;
				tile.onReceive(packet, side);
				if(side==EnumFacing.DOWN||side==facing.rotateY())
				{
					assertNotNull(receiver.packet);
					assertEquals("34", receiver.packet.get('a').toString());
					assertEquals(facing.rotateY(), receiver.side);
				}
				else
					assertNull(receiver.packet);
			}
			remote.setBoolean(world, true);
			receiver.packet = null;
			tile.onReceive(packet, EnumFacing.DOWN);
			assertNull(receiver.packet);
			remote.setBoolean(world, false);
			when(world.isBlockLoaded(any(BlockPos.class))).thenReturn(false);
			tile.onReceive(packet, EnumFacing.DOWN);
			assertNull(receiver.packet);
		}
	}

	private static class TestDisplay extends TileEntityDoubleNixieDisplay
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

	private static class Receiver extends TileEntity implements IDataDevice
	{
		private DataPacket packet;
		private EnumFacing side;

		@Override
		public void onReceive(DataPacket packet, EnumFacing side)
		{
			this.packet = packet;
			this.side = side;
		}
	}
}
