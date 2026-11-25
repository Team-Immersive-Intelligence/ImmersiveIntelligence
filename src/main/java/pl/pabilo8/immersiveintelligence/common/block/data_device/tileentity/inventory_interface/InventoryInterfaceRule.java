package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests inventory samples and builds data packets. Saves configuration only.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 28.10.2026
 * @since 27.10.2026
 */
public class InventoryInterfaceRule implements INBTSerializable<NBTTagCompound>
{
	public boolean enabled = true;
	public Trigger trigger = Trigger.INVENTORY_CHANGED;
	public ValueType measuredValue = ValueType.TOTAL_ITEMS;
	public Comparison comparison = Comparison.ABOVE;
	public int threshold = 0;
	public int slot = 0;
	//Sets the periodic and repeat interval in ticks, from 10 to 72000.
	public int interval = 20;
	public SendMode sendMode = SendMode.ON_ENTER;
	public ItemStack filter = ItemStack.EMPTY;
	public boolean matchNBT = true;
	//Stores packet outputs. The default output sends the total item count as variable a.
	public EasyCollection<Output, NBTTagCompound> outputs = new EasyCollection<>(Output::new);

	private transient boolean conditionActive;
	private transient int ticksSinceSend;

	public InventoryInterfaceRule()
	{
		outputs.add(new Output());
	}

	/**
	 * Returns a deep configuration copy with no runtime state.
	 */
	public InventoryInterfaceRule copy()
	{
		InventoryInterfaceRule copy = new InventoryInterfaceRule();
		copy.deserializeNBT(serializeNBT());
		return copy;
	}

	/**
	 * Clears the condition edge and cooldown. Conditions start false; periodic rules wait one interval.
	 */
	public void resetRuntime()
	{
		conditionActive = false;
		ticksSinceSend = 0;
	}

	/**
	 * Tests one sample. Pass ticks since the last sample, usually 10, as elapsedTicks.
	 * Call once per sample. Change triggers need a previous sample and ignore the send mode.
	 * Condition edges send at once. WHILE_TRUE sends at once on each entry, then waits between repeats.
	 */
	public boolean shouldSend(InventorySnapshot current, @Nullable InventorySnapshot previous, int elapsedTicks)
	{
		if(!enabled||current==null)
		{
			resetRuntime();
			return false;
		}
		int delay = MathHelper.clamp(interval, 10, 72000);
		ticksSinceSend = (int)Math.min(delay, (long)ticksSinceSend+Math.max(0, elapsedTicks));
		Trigger selected = orDefault(trigger, Trigger.INVENTORY_CHANGED);
		switch(selected)
		{
			case INVENTORY_CHANGED:
				return previous!=null&&!current.sameContents(previous);
			case SLOT_CHANGED:
				return previous!=null&&!current.sameSlot(previous, Math.max(0, slot));
			case ITEM_COUNT_CHANGED:
				return previous!=null&&current.countItems(filter, matchNBT)!=previous.countItems(filter, matchNBT);
			case PERIODIC:
				if(ticksSinceSend < delay)
					return false;
				ticksSinceSend = 0;
				return true;
			default:
				boolean active = testCondition(selected, current);
				boolean wasActive = conditionActive;
				conditionActive = active;
				switch(orDefault(sendMode, SendMode.ON_ENTER))
				{
					case ON_LEAVE:
						return wasActive&&!active;
					case ON_CHANGE:
						return wasActive!=active;
					case WHILE_TRUE:
						if(!active||(wasActive&&ticksSinceSend < delay))
							return false;
						ticksSinceSend = 0;
						return true;
					default:
						return !wasActive&&active;
				}
		}
	}

	private boolean testCondition(Trigger selected, InventorySnapshot snapshot)
	{
		switch(selected)
		{
			case INVENTORY_EMPTY:
				return snapshot.isEmpty();
			case INVENTORY_FULL:
				return snapshot.isFull();
			case CONTAINS_ITEM:
				return snapshot.getItemCount(filter, matchNBT) > 0;
			case MISSING_ITEM:
				return snapshot.getItemCount(filter, matchNBT)==0;
			default:
				double value = readNumber(numericValue(measuredValue), snapshot, slot, filter, matchNBT);
				int target = Math.max(0, threshold);
				return switch(orDefault(comparison, Comparison.ABOVE))
				{
					case EQUAL -> value==target;
					case NOT_EQUAL -> value!=target;
					case BELOW -> value < target;
					case AT_LEAST -> value >= target;
					case AT_MOST -> value <= target;
					default -> value > target;
				};
		}
	}

	/**
	 * Builds a packet from valid outputs. Duplicate variables use the first output.
	 */
	public DataPacket createPacket(InventorySnapshot snapshot)
	{
		DataPacket packet = new DataPacket();
		if(outputs!=null)
			for(Output output : outputs)
			{
				if(packet.size() >= DataPacket.VARIABLE_NAMES.length)
					break;
				if(output!=null&&DataPacket.isValidVariable(output.variable)&&!packet.has(output.variable))
					packet.set(output.variable, readValue(output.type, snapshot, output.slot, output.filter, output.matchNBT));
			}
		return packet;
	}

	/**
	 * Reads a value for a packet or callback. FILL_PERCENT is a float; other numbers are integers.
	 * State values are booleans. Stack payload counts are capped at 127 for Minecraft NBT.
	 * ITEM_STACKS returns up to 255 nonempty matching stacks in slot order, as required by DataTypeArray.
	 * An absent snapshot gives empty values. Returned stacks do not share snapshot state.
	 */
	public static DataType readValue(ValueType type, @Nullable InventorySnapshot snapshot, int slot, @Nullable ItemStack filter, boolean matchNBT)
	{
		return switch(orDefault(type, ValueType.TOTAL_ITEMS))
		{
			case SLOT_STACK -> stackValue(snapshot==null?ItemStack.EMPTY: snapshot.getStack(slot));
			case FILL_PERCENT -> new DataTypeFloat((float)readNumber(type, snapshot, slot, filter, matchNBT));
			case IS_EMPTY -> new DataTypeBoolean(snapshot==null||snapshot.isEmpty());
			case IS_FULL -> new DataTypeBoolean(snapshot!=null&&snapshot.isFull());
			case ITEM_STACKS ->
			{
				List<DataType> stacks = new ArrayList<>();
				if(snapshot!=null)
					for(int index = 0; index < snapshot.getTotalSlots()&&stacks.size() < 255; index++)
					{
						ItemStack stack = snapshot.getStack(index);
						if(InventorySnapshot.matches(stack, filter, matchNBT))
							stacks.add(stackValue(stack));
					}
				yield new DataTypeArray(stacks);
			}
			default -> new DataTypeInteger((int)readNumber(type, snapshot, slot, filter, matchNBT));
		};
	}

	private static double readNumber(ValueType type, @Nullable InventorySnapshot snapshot, int slot, @Nullable ItemStack filter, boolean matchNBT)
	{
		if(snapshot==null)
			return 0;
		return switch(orDefault(type, ValueType.TOTAL_ITEMS))
		{
			case TOTAL_STACKS -> snapshot.getOccupiedSlots();
			case ITEM_COUNT -> snapshot.getItemCount(filter, matchNBT);
			case TOTAL_SLOTS -> snapshot.getTotalSlots();
			case EMPTY_SLOTS -> snapshot.getEmptySlots();
			case SLOT_ITEMS -> snapshot.getStackCount(slot);
			case SLOT_LIMIT -> snapshot.getSlotLimit(slot);
			case COMPARATOR -> snapshot.getComparatorOutput();
			case FILL_PERCENT -> (double)snapshot.getFillPercent();
			default -> snapshot.getTotalItems();
		};
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagList entries = new NBTTagList();
		Set<Character> variables = new HashSet<>();
		if(outputs!=null)
			for(Output output : outputs)
			{
				if(entries.tagCount() >= DataPacket.VARIABLE_NAMES.length)
					break;
				if(output!=null&&DataPacket.isValidVariable(output.variable)&&variables.add(output.variable))
					entries.appendTag(output.serializeNBT());
			}
		return EasyNBT.newNBT()
				.withBoolean("enabled", enabled)
				.withEnum("trigger", orDefault(trigger, Trigger.INVENTORY_CHANGED))
				.withEnum("measuredValue", numericValue(measuredValue))
				.withEnum("comparison", orDefault(comparison, Comparison.ABOVE))
				.withInt("threshold", Math.max(0, threshold))
				.withInt("slot", Math.max(0, slot))
				.withInt("interval", MathHelper.clamp(interval, 10, 72000))
				.withEnum("sendMode", orDefault(sendMode, SendMode.ON_ENTER))
				.withTag("filter", writeStack(filter))
				.withBoolean("matchNBT", matchNBT)
				.withTag("outputs", entries).unwrap();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		enabled = readBoolean(nbt, "enabled", true);
		trigger = readEnum(nbt, "trigger", Trigger.INVENTORY_CHANGED);
		measuredValue = numericValue(readEnum(nbt, "measuredValue", ValueType.TOTAL_ITEMS));
		comparison = readEnum(nbt, "comparison", Comparison.ABOVE);
		threshold = Math.max(0, readInt(nbt, "threshold", 0));
		slot = Math.max(0, readInt(nbt, "slot", 0));
		interval = MathHelper.clamp(readInt(nbt, "interval", 20), 10, 72000);
		sendMode = readEnum(nbt, "sendMode", SendMode.ON_ENTER);
		filter = readStack(nbt, "filter");
		matchNBT = readBoolean(nbt, "matchNBT", true);
		if(outputs==null)
			outputs = new EasyCollection<>(Output::new);
		outputs.clear();
		if(nbt==null||!nbt.hasKey("outputs", 9))
			outputs.add(new Output());
		else
		{
			NBTTagList entries = nbt.getTagList("outputs", 10);
			Set<Character> variables = new HashSet<>();
			int count = Math.min(entries.tagCount(), DataPacket.VARIABLE_NAMES.length);
			for(int index = 0; index < count; index++)
			{
				Output output = new Output();
				output.deserializeNBT(entries.getCompoundTagAt(index));
				if(variables.add(output.variable))
					outputs.add(output);
			}
		}
		resetRuntime();
	}

	public boolean usesItemFilter()
	{
		return trigger==Trigger.ITEM_COUNT_CHANGED||trigger==Trigger.CONTAINS_ITEM||trigger==Trigger.MISSING_ITEM||
				(trigger==Trigger.CONDITION&&measuredValue==ValueType.ITEM_COUNT);
	}

	private static ValueType numericValue(@Nullable ValueType type)
	{
		return type!=null&&type.isNumeric()?type: ValueType.TOTAL_ITEMS;
	}

	private static <T> T orDefault(@Nullable T value, T fallback)
	{
		return value==null?fallback: value;
	}

	private static <T extends Enum<T>> T readEnum(@Nullable NBTTagCompound nbt, String key, T fallback)
	{
		if(nbt!=null&&nbt.hasKey(key, 8))
			for(T value : fallback.getDeclaringClass().getEnumConstants())
				if(value.name().equalsIgnoreCase(nbt.getString(key)))
					return value;
		return fallback;
	}

	private static int readInt(@Nullable NBTTagCompound nbt, String key, int fallback)
	{
		return nbt!=null&&nbt.hasKey(key, 3)?nbt.getInteger(key): fallback;
	}

	private static boolean readBoolean(@Nullable NBTTagCompound nbt, String key, boolean fallback)
	{
		return nbt!=null&&nbt.hasKey(key, 1)?nbt.getBoolean(key): fallback;
	}

	private static NBTTagCompound writeStack(@Nullable ItemStack stack)
	{
		ItemStack filter = stack==null?ItemStack.EMPTY: stack.copy();
		if(!filter.isEmpty())
			filter.setCount(1);
		return filter.writeToNBT(new NBTTagCompound());
	}

	private static DataTypeItemStack stackValue(ItemStack stack)
	{
		//Minecraft stores stack counts as signed bytes. Keep large-handler payloads valid.
		if(!stack.isEmpty())
			stack.setCount(Math.min(127, stack.getCount()));
		return new DataTypeItemStack(stack);
	}

	private static ItemStack readStack(@Nullable NBTTagCompound nbt, String key)
	{
		if(nbt==null||!nbt.hasKey(key, 10))
			return ItemStack.EMPTY;
		try
		{
			return new ItemStack(nbt.getCompoundTag(key).copy());
		} catch(RuntimeException ignored)
		{
			return ItemStack.EMPTY;
		}
	}

	/**
	 * Selects an inventory condition, change, or timer.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 27.10.2026
	 */
	public enum Trigger implements ILocalizedEnum
	{
		CONDITION, INVENTORY_EMPTY, INVENTORY_FULL,
		INVENTORY_CHANGED, SLOT_CHANGED, ITEM_COUNT_CHANGED, PERIODIC, CONTAINS_ITEM, MISSING_ITEM;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"inventory_interface.trigger.";
		}
	}

	/**
	 * Selects an inventory value.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 27.10.2026
	 */
	public enum ValueType implements ILocalizedEnum
	{
		TOTAL_ITEMS, TOTAL_STACKS, ITEM_COUNT, TOTAL_SLOTS, EMPTY_SLOTS, SLOT_ITEMS, SLOT_STACK,
		SLOT_LIMIT, COMPARATOR, FILL_PERCENT, IS_EMPTY, IS_FULL, ITEM_STACKS;

		public boolean isNumeric()
		{
			return this!=SLOT_STACK&&this!=IS_EMPTY&&this!=IS_FULL&&this!=ITEM_STACKS;
		}

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"inventory_interface.value.";
		}
	}

	/**
	 * Selects a numeric comparison.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 27.10.2026
	 */
	public enum Comparison implements ILocalizedEnum
	{
		EQUAL, NOT_EQUAL, ABOVE, BELOW, AT_LEAST, AT_MOST;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"inventory_interface.comparison.";
		}
	}

	/**
	 * Selects when a condition sends a packet.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 27.10.2026
	 */
	public enum SendMode implements ILocalizedEnum
	{
		ON_ENTER, ON_LEAVE, ON_CHANGE, WHILE_TRUE;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"inventory_interface.send_mode.";
		}
	}

	/**
	 * Stores the source and packet variable for one output.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 27.10.2026
	 */
	public static class Output implements INBTSerializable<NBTTagCompound>
	{
		public char variable = 'a';
		public ValueType type = ValueType.TOTAL_ITEMS;
		public int slot = 0;
		//An empty filter matches all items.
		public ItemStack filter = ItemStack.EMPTY;
		public boolean matchNBT = true;

		@Override
		public NBTTagCompound serializeNBT()
		{
			return EasyNBT.newNBT()
					.withString("variable", String.valueOf(DataPacket.isValidVariable(variable)?variable: 'a'))
					.withEnum("type", orDefault(type, ValueType.TOTAL_ITEMS))
					.withInt("slot", Math.max(0, slot))
					.withTag("filter", writeStack(filter))
					.withBoolean("matchNBT", matchNBT).unwrap();
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			variable = DataDeviceUtils.readLetter(nbt, "variable");
			type = readEnum(nbt, "type", ValueType.TOTAL_ITEMS);
			slot = Math.max(0, readInt(nbt, "slot", 0));
			filter = readStack(nbt, "filter");
			matchNBT = readBoolean(nbt, "matchNBT", true);
		}
	}
}
