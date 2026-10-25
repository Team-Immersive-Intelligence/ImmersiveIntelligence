package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeNull;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataBuffer;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ILocalizedEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;
import java.util.Iterator;

/**
 * Queues packets until configurable conditions permit output or their lifetime expires.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class TileEntityDataBuffer extends TileEntityIIDirectional implements IDataDevice, ITickable, IBlockBounds, IIIGuiMultiblockTile, IIIInventory
{
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public EasyCollection<BufferCondition, NBTTagCompound> conditions = new EasyCollection<>(BufferCondition::new);
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public boolean matchAll = true, queueUnmatched = true;
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public int terminationTime = 1200;
	@SyncNBT
	public EasyCollection<QueuedPacket, NBTTagCompound> queue = new EasyCollection<>(QueuedPacket::new);
	@SyncNBT
	public long lastReceived = -1;

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world==null||world.isRemote||packet==null||side!=facing.getOpposite())
			return;
		long now = world.getTotalWorldTime();
		long receivedDelay = lastReceived < 0?0: Math.max(0, now-lastReceived);
		lastReceived = now;
		markDirty();
		if(queue.size() >= MathHelper.clamp(DataBuffer.packetCapacity, 1, 4096))
			return;
		QueuedPacket entry = new QueuedPacket();
		entry.packet = packet;
		entry.receivedAt = now;
		entry.receivedDelay = receivedDelay;
		if(queueUnmatched||matches(entry, now, false))
		{
			entry.packet = new DataPacket(packet.serializeNBT());
			queue.add(entry);
		}
	}

	@Override
	public void update()
	{
		if(world==null||world.isRemote||queue.isEmpty())
			return;
		long now = world.getTotalWorldTime();
		int redstone = world.isBlockIndirectlyGettingPowered(pos);
		//Remove entries before dispatch. A receiver can return a packet to this device.
		QueuedPacket outgoing = null;
		Iterator<QueuedPacket> iterator = queue.iterator();
		while(iterator.hasNext())
		{
			QueuedPacket entry = iterator.next();
			if(terminationTime > 0&&now-entry.receivedAt >= terminationTime)
			{
				iterator.remove();
				markDirty();
			}
			else if(outgoing==null&&matches(entry, now, true, redstone))
			{
				outgoing = entry;
				iterator.remove();
			}
		}
		if(outgoing!=null)
		{
			if(!IIDataHandlingUtils.sendPacketAdjacently(outgoing.packet, world, pos, facing))
				queue.add(0, outgoing);
			markDirty();
		}
	}

	public boolean matches(QueuedPacket entry, long now, boolean includeTime)
	{
		return matches(entry, now, includeTime, world==null?0: world.isBlockIndirectlyGettingPowered(pos));
	}

	private boolean matches(QueuedPacket entry, long now, boolean includeTime, int redstone)
	{
		boolean found = false;
		for(BufferCondition condition : conditions)
		{
			if(!includeTime&&condition.type==ConditionType.TIME_DELAY)
				continue;
			found = true;
			boolean matches = condition.matches(entry, now, redstone);
			if(matchAll&&!matches)
				return false;
			if(!matchAll&&matches)
				return true;
		}
		return !found||matchAll;
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		//Only configuration fields are accepted from a screen.
		if(message.hasKey("conditions", 9))
			conditions.deserializeNBT(DataDeviceUtils.boundedList(message, "conditions"));
		if(message.hasKey("match_all", 1))
			matchAll = message.getBoolean("match_all");
		if(message.hasKey("queue_unmatched", 1))
			queueUnmatched = message.getBoolean("queue_unmatched");
		if(message.hasKey("termination_time", 3))
			terminationTime = MathHelper.clamp(message.getInteger("termination_time"), 0, DataDeviceUtils.MAX_TICKS);
		markDirty();
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.DATA_BUFFER;
	}

	private static final FacingSettings FACING_SETTINGS = new FacingSettings(FacingLimitation.HORIZONTAL).withRotation(true);

	@Nonnull
	@Override
	protected FacingSettings getFacingSettings()
	{
		return FACING_SETTINGS;
	}

	@Override
	@Nonnull
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, .875f, 1};
	}

	@Override
	public boolean canOpenGui()
	{
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		return true;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	public enum ConditionType implements ILocalizedEnum
	{
		REDSTONE_SIGNAL, LAST_RECEIVED_DELAY, TIME_DELAY, VARIABLE_PRESENT, VARIABLE_MATCHING;

		@Override
		public String geLocaleKey()
		{
			return IIReference.GUI_LABEL_KEY+"data_buffer.condition.";
		}
	}

	public enum Comparison
	{
		GREATER_OR_EQUAL(">="), GREATER(">"), EQUAL("="), NOT_EQUAL("!="), LESS("<"), LESS_OR_EQUAL("<=");

		private final String sign;

		Comparison(String sign)
		{
			this.sign = sign;
		}

		public boolean test(int actual, int expected)
		{
			return switch(this)
			{
				case GREATER -> actual > expected;
				case EQUAL -> actual==expected;
				case NOT_EQUAL -> actual!=expected;
				case LESS -> actual < expected;
				case LESS_OR_EQUAL -> actual <= expected;
				default -> actual >= expected;
			};
		}

		@Override
		public String toString()
		{
			return sign;
		}
	}

	/**
	 * Stores one packet admission or release condition.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class BufferCondition implements INBTSerializable<NBTTagCompound>
	{
		public ConditionType type = ConditionType.REDSTONE_SIGNAL;
		public Comparison comparison = Comparison.GREATER_OR_EQUAL;
		public int ticks = 20, signal = 1;
		public char variable = 'a';
		public boolean useAccessor = false;
		public DataType expected = new DataTypeNull();

		public boolean matches(QueuedPacket entry, long now, int redstone)
		{
			switch(type)
			{
				case REDSTONE_SIGNAL:
					return comparison.test(redstone, signal);
				case LAST_RECEIVED_DELAY:
					return entry.receivedDelay >= ticks;
				case TIME_DELAY:
					if(!useAccessor)
						return now-entry.receivedAt >= ticks;
					DataType delay = entry.packet.get(variable);
					return delay instanceof NumericDataType&&now-entry.receivedAt >= MathHelper.clamp(((NumericDataType)delay).intValue(), 0, DataDeviceUtils.MAX_TICKS);
				case VARIABLE_PRESENT:
					return entry.packet.has(variable);
				case VARIABLE_MATCHING:
					return entry.packet.has(variable)&&DataDeviceUtils.matches(entry.packet.get(variable), expected);
				default:
					return false;
			}
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("type", type.ordinal());
			nbt.setInteger("comparison", comparison.ordinal());
			nbt.setInteger("ticks", ticks);
			nbt.setInteger("signal", signal);
			nbt.setString("variable", String.valueOf(variable));
			nbt.setBoolean("useAccessor", useAccessor);
			nbt.setTag("expected", expected.valueToNBT());
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			type = ConditionType.values()[MathHelper.clamp(nbt.getInteger("type"), 0, ConditionType.values().length-1)];
			comparison = Comparison.values()[MathHelper.clamp(nbt.getInteger("comparison"), 0, Comparison.values().length-1)];
			ticks = MathHelper.clamp(nbt.getInteger("ticks"), 0, DataDeviceUtils.MAX_TICKS);
			signal = MathHelper.clamp(nbt.getInteger("signal"), 0, 16);
			variable = DataDeviceUtils.readLetter(nbt, "variable");
			useAccessor = nbt.getBoolean("useAccessor");
			expected = DataDeviceUtils.readValue(nbt, "expected");
		}
	}

	/**
	 * Stores an accepted packet and its arrival timing.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class QueuedPacket implements INBTSerializable<NBTTagCompound>
	{
		public DataPacket packet = new DataPacket();
		public long receivedAt, receivedDelay;

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("packet", packet.serializeNBT());
			nbt.setLong("receivedAt", receivedAt);
			nbt.setLong("receivedDelay", receivedDelay);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			packet = new DataPacket(nbt.getCompoundTag("packet"));
			receivedAt = nbt.getLong("receivedAt");
			receivedDelay = Math.max(0, nbt.getLong("receivedDelay"));
		}
	}
}
