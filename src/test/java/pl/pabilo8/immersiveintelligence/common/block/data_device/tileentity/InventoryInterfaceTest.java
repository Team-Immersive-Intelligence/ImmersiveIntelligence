package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.Test;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeFloat;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.common.IILogger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.Comparison;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.SendMode;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.Trigger;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.ValueType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventorySnapshot;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.test.GameTestBasic;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests inventory sample isolation, rule timing and callback values.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
public class InventoryInterfaceTest extends GameTestBasic
{
	@Test
	void samplesRetainCountsAndTagsAfterTheInventoryChanges()
	{
		ItemStackHandler handler = new ItemStackHandler(2);
		ItemStack source = new ItemStack(Items.IRON_INGOT, 128);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("batch", 1);
		source.setTagCompound(tag);
		handler.setStackInSlot(0, source);
		InventorySnapshot first = InventorySnapshot.capture(handler);
		source.setCount(384);
		tag.setInteger("batch", 2);
		InventorySnapshot second = InventorySnapshot.capture(handler);
		assertFalse(first.sameSlot(second, 0));
		source.setCount(128);
		assertFalse(first.sameSlot(InventorySnapshot.capture(handler), 0));
		assertEquals(128, first.getStackCount(0));
		NBTTagCompound capturedTag = first.getStack(0).getTagCompound();
		assertNotNull(capturedTag);
		assertEquals(1, capturedTag.getInteger("batch"));
		first.getStack(0).setCount(1);
		assertEquals(128, first.getStackCount(0));
		DataTypeItemStack payload = (DataTypeItemStack)InventoryInterfaceRule.readValue(
				ValueType.SLOT_STACK, first, 0, ItemStack.EMPTY, true);
		assertEquals(127, payload.value.getCount());
		assertEquals(128, first.getTotalItems());
		assertEquals(1, first.getEmptySlots());
		assertFalse(first.isFull());
	}

	@Test
	void repeatedConditionsSendOnEntryAndWaitBetweenRepeats()
	{
		InventorySnapshot empty = InventorySnapshot.capture(new ItemStackHandler(1));
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.trigger = Trigger.INVENTORY_EMPTY;
		rule.sendMode = SendMode.WHILE_TRUE;
		assertTrue(rule.shouldSend(empty, null, 0));
		assertFalse(rule.shouldSend(empty, empty, 10));
		assertTrue(rule.shouldSend(empty, empty, 10));
		assertTrue(rule.copy().shouldSend(empty, null, 0));
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT));
		InventorySnapshot filled = InventorySnapshot.capture(handler);
		assertFalse(rule.shouldSend(filled, empty, 10));
		assertTrue(rule.shouldSend(empty, filled, 10));
	}

	@Test
	void periodicTimersWaitAfterResetAndIgnoreNegativeElapsedTime()
	{
		InventorySnapshot empty = InventorySnapshot.capture(new ItemStackHandler(1));
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.trigger = Trigger.PERIODIC;
		assertFalse(rule.shouldSend(empty, null, 0));
		assertFalse(rule.shouldSend(empty, empty, 10));
		assertFalse(rule.shouldSend(empty, empty, -10));
		assertTrue(rule.shouldSend(empty, empty, 10));
		rule.resetRuntime();
		assertFalse(rule.shouldSend(empty, null, 0));
	}

	@Test
	void comparisonsKeepLargeIntegersAndFractionalFill()
	{
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT, 16777217));
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.trigger = Trigger.CONDITION;
		rule.comparison = Comparison.EQUAL;
		rule.threshold = 16777217;
		assertTrue(rule.shouldSend(InventorySnapshot.capture(handler), null, 0));
		handler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT, 1));
		InventorySnapshot sample = InventorySnapshot.capture(handler);
		rule.resetRuntime();
		rule.measuredValue = ValueType.FILL_PERCENT;
		rule.comparison = Comparison.ABOVE;
		rule.threshold = 1;
		assertTrue(rule.shouldSend(sample, null, 0));
		assertEquals(1.5625f, ((DataTypeFloat)InventoryInterfaceRule.readValue(
				ValueType.FILL_PERCENT, sample, 0, ItemStack.EMPTY, true)).value);
	}

	@Test
	void callbacksRejectInvalidSlotsAndReadTheCurrentCount()
	{
		ItemStackHandler handler = new ItemStackHandler(1);
		handler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT, 12));
		InventorySnapshot sample = InventorySnapshot.capture(handler);
		DataPacket request = new DataPacket();
		DataTypeInteger count = (DataTypeInteger)TileEntityInventoryInterface.queryInventory("get_slot_items:0", request, sample);
		assertNotNull(count);
		assertEquals(12, count.value);
		assertNull(TileEntityInventoryInterface.queryInventory("get_slot_items:1", request, sample));
		assertNull(TileEntityInventoryInterface.queryInventory("get_total_items:0", request, sample));
		assertNull(TileEntityInventoryInterface.queryInventory("get_total_items", request, null));
	}

	@Test
	void guiEventsIncludeRulesAndInheritedChannels()
	{
		IILogger.logger = LogManager.getLogger("inventory-interface-test");
		NBTSerialisation.preInit();
		NBTSerialisation.postInit();
		TileEntityInventoryInterface tile = new TileEntityInventoryInterface();
		tile.colorIn = 4;
		tile.colorOut = 8;
		tile.rules.add(new InventoryInterfaceRule());
		for(SyncEvents event : new SyncEvents[]{SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_GUI_OPENED})
		{
			NBTTagCompound message = new NBTTagCompound();
			NBTSerialisation.synchroniseFor(tile, (serializer, object) -> serializer.serializeForEvent(object, message, event));
			assertEquals(4, message.getInteger("colorIn"));
			assertEquals(8, message.getInteger("colorOut"));
			assertEquals(1, message.getTagList("rules", 10).tagCount());
		}
	}

	@Test
	void totalCountChangesIgnoreReorderingAndKeepLongPrecision()
	{
		ItemStackHandler handler = new ItemStackHandler(2);
		handler.setStackInSlot(0, new ItemStack(Items.IRON_INGOT, 2000000000));
		handler.setStackInSlot(1, new ItemStack(Items.GOLD_INGOT, 1000000000));
		InventorySnapshot first = InventorySnapshot.capture(handler);
		handler.setStackInSlot(0, new ItemStack(Items.GOLD_INGOT, 1000000000));
		handler.setStackInSlot(1, new ItemStack(Items.IRON_INGOT, 2000000000));
		InventorySnapshot reordered = InventorySnapshot.capture(handler);
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.trigger = Trigger.ITEM_COUNT_CHANGED;
		assertFalse(rule.shouldSend(reordered, first, 10));
		handler.getStackInSlot(0).grow(1);
		assertTrue(rule.shouldSend(InventorySnapshot.capture(handler), reordered, 10));
	}

	@Test
	void selectedItemCountChangesIgnoreOtherItemsAndTags()
	{
		ItemStackHandler handler = new ItemStackHandler(3);
		ItemStack selected = new ItemStack(Items.IRON_INGOT, 4);
		NBTTagCompound firstTag = new NBTTagCompound(), secondTag = new NBTTagCompound();
		firstTag.setInteger("batch", 1);
		secondTag.setInteger("batch", 2);
		selected.setTagCompound(firstTag);
		ItemStack otherBatch = selected.copy();
		otherBatch.setTagCompound(secondTag);
		handler.setStackInSlot(0, selected);
		handler.setStackInSlot(1, otherBatch);
		handler.setStackInSlot(2, new ItemStack(Items.GOLD_INGOT, 5));
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.trigger = Trigger.ITEM_COUNT_CHANGED;
		rule.filter = selected.copy();
		InventorySnapshot first = InventorySnapshot.capture(handler);
		handler.getStackInSlot(2).grow(1);
		InventorySnapshot otherItemChanged = InventorySnapshot.capture(handler);
		assertFalse(rule.shouldSend(otherItemChanged, first, 10));
		handler.getStackInSlot(1).grow(1);
		InventorySnapshot otherTagChanged = InventorySnapshot.capture(handler);
		assertFalse(rule.shouldSend(otherTagChanged, otherItemChanged, 10));
		rule.matchNBT = false;
		assertTrue(rule.shouldSend(otherTagChanged, otherItemChanged, 10));
		rule.matchNBT = true;
		handler.getStackInSlot(0).grow(1);
		assertTrue(rule.shouldSend(InventorySnapshot.capture(handler), otherTagChanged, 10));
	}

	@Test
	void presenceAndNumericConditionsUseTheSelectedItem()
	{
		ItemStackHandler handler = new ItemStackHandler(2);
		handler.setStackInSlot(0, new ItemStack(Items.GOLD_INGOT, 40));
		InventorySnapshot absent = InventorySnapshot.capture(handler);
		handler.setStackInSlot(1, new ItemStack(Items.IRON_INGOT, 3));
		InventorySnapshot present = InventorySnapshot.capture(handler);
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		rule.filter = new ItemStack(Items.IRON_INGOT);
		rule.trigger = Trigger.CONTAINS_ITEM;
		assertFalse(rule.shouldSend(absent, null, 0));
		assertTrue(rule.shouldSend(present, absent, 10));
		rule.resetRuntime();
		rule.trigger = Trigger.MISSING_ITEM;
		assertFalse(rule.shouldSend(present, null, 0));
		assertTrue(rule.shouldSend(absent, present, 10));
		rule.resetRuntime();
		rule.trigger = Trigger.CONDITION;
		rule.measuredValue = ValueType.ITEM_COUNT;
		rule.threshold = 2;
		assertFalse(rule.shouldSend(absent, null, 0));
		assertTrue(rule.shouldSend(present, absent, 10));
		assertEquals(43, ((DataTypeInteger)rule.createPacket(present).get('a')).value);
	}

	@Test
	void ruleItemValuesSurviveLoadingAndIndependentCopies()
	{
		InventoryInterfaceRule rule = new InventoryInterfaceRule();
		NBTTagCompound saved = rule.serializeNBT();
		ItemStack filter = new ItemStack(Items.IRON_INGOT);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("batch", 1);
		filter.setTagCompound(tag);
		saved.setInteger("packetAddress", 42);
		saved.setTag("filter", filter.serializeNBT());
		saved.setBoolean("matchNBT", false);
		saved.setString("trigger", "contains_item");
		rule.deserializeNBT(saved);
		assertEquals(Trigger.CONTAINS_ITEM, rule.trigger);
		assertFalse(rule.matchNBT);
		assertTrue(ItemStack.areItemStackTagsEqual(filter, rule.filter));
		InventoryInterfaceRule copy = rule.copy();
		copy.filter.setTagCompound(new NBTTagCompound());
		assertTrue(ItemStack.areItemStackTagsEqual(filter, rule.filter));
		assertFalse(rule.serializeNBT().hasKey("packetAddress"));
		saved.setString("trigger", "missing_item");
		rule.deserializeNBT(saved);
		assertEquals(Trigger.MISSING_ITEM, rule.trigger);
	}
}
