package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import lombok.Getter;
import net.minecraft.block.state.IBlockState;
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
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.NumericDataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.DataDeviceUtils;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIDirectional;

import javax.annotation.Nonnull;

/**
 * Executes repeating jobs and finite requests as timed or constant redstone sequence steps.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 24.10.2026
 */
public class TileEntityRedstoneSequencer extends TileEntityIIDirectional implements IDataDevice, ITickable, IRedstoneOutput, IBlockBounds, IIIGuiMultiblockTile, IIIInventory
{
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public EasyCollection<SequenceTask, NBTTagCompound> tasks = new EasyCollection<>(SequenceTask::new);
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public boolean running = true;
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public int revision = 0;
	@SyncNBT
	public int currentTask = 0, remainingTicks = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public int signal = 0;
	@SyncNBT
	public boolean active = false;

	@Override
	public void update()
	{
		if(world==null||world.isRemote)
			return;
		if(!running||tasks.isEmpty())
		{
			setSignal(0);
			return;
		}
		currentTask = MathHelper.clamp(currentTask, 0, tasks.size()-1);
		SequenceTask task = tasks.get(currentTask);
		if(!active)
		{
			remainingTicks = task.duration;
			active = true;
			setSignal(task.strength);
			markDirty();
		}
		if(!task.constant&&--remainingTicks <= 0)
			finishStep();
		else
			markDirty();
	}

	public void finishStep()
	{
		if(active&&!tasks.isEmpty())
		{
			currentTask = MathHelper.clamp(currentTask, 0, tasks.size()-1);
			SequenceTask task = tasks.get(currentTask);
			boolean remove = !task.isJob&&--task.repeats <= 0;
			if(remove)
				tasks.remove(currentTask);
			else
				currentTask++;
			if(currentTask >= tasks.size())
				currentTask = 0;
			if(!task.isJob)
			{
				revision++;
				updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
			}
		}
		active = false;
		remainingTicks = 0;
		//The next tick begins the next step. Zero strength steps provide timed gaps.
		markDirty();
	}

	private void setSignal(int strength)
	{
		strength = MathHelper.clamp(strength, 0, 15);
		if(signal==strength)
			return;
		signal = strength;
		if(world!=null&&!world.isRemote)
		{
			world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
			world.notifyNeighborsOfStateChange(pos.offset(facing), getBlockType(), false);
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
		}
		markDirty();
	}

	private void restart()
	{
		currentTask = remainingTicks = 0;
		active = false;
		setSignal(0);
		markDirty();
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(world==null||world.isRemote||packet==null||side!=facing.getOpposite())
			return;
		IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
			switch(command)
			{
				case "add":
					if(tasks.size() >= DataDeviceUtils.MAX_RULES)
						return;
					SequenceTask task = new SequenceTask();
					if(packet.get('s') instanceof NumericDataType)
						task.strength = MathHelper.clamp(((NumericDataType)packet.get('s')).intValue(), 0, 15);
					if(packet.get('t') instanceof NumericDataType)
						task.duration = MathHelper.clamp(((NumericDataType)packet.get('t')).intValue(), 1, DataDeviceUtils.MAX_TICKS);
					if(packet.get('h') instanceof DataTypeBoolean)
						task.constant = ((DataTypeBoolean)packet.get('h')).value;
					if(packet.get('j') instanceof DataTypeBoolean)
						task.isJob = ((DataTypeBoolean)packet.get('j')).value;
					if(packet.get('e') instanceof NumericDataType)
					{
						task.isJob = false;
						task.repeats = MathHelper.clamp(((NumericDataType)packet.get('e')).intValue(), 1, DataDeviceUtils.MAX_TICKS);
					}
					tasks.add(task);
					break;
				case "remove":
					if(!(packet.get('i') instanceof NumericDataType))
						return;
					int index = ((NumericDataType)packet.get('i')).intValue();
					if(index < 0||index >= tasks.size())
						return;
					tasks.remove(index);
					restart();
					break;
				case "clear":
					tasks.clear();
					restart();
					break;
				case "start":
					running = true;
					restart();
					break;
				case "stop":
					running = false;
					restart();
					break;
				case "next":
					finishStep();
					setSignal(0);
					break;
				default:
					return;
			}
			markDirty();
			revision++;
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		});
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		boolean changed = false;
		if(message.hasKey("tasks", 9)&&message.hasKey("expectedRevision", 3)&&message.getInteger("expectedRevision")==revision)
		{
			tasks.deserializeNBT(DataDeviceUtils.boundedList(message, "tasks"));
			changed = true;
		}
		if(message.hasKey("running", 1)&&running!=message.getBoolean("running"))
		{
			running = message.getBoolean("running");
			changed = true;
		}
		if(changed)
		{
			revision++;
			restart();
		}
		updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
	}

	@Override
	public int getWeakRSOutput(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return side==facing.getOpposite()?signal: 0;
	}

	@Override
	public int getStrongRSOutput(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return getWeakRSOutput(state, side);
	}

	@Override
	public boolean canConnectRedstone(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return side==facing.getOpposite();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.REDSTONE_SEQUENCER;
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

	/**
	 * Stores one repeating or finite redstone sequence step.
	 *
	 * @author Pabilo8 (pabilo@iiteam.net)
	 * @since 24.10.2026
	 */
	public static class SequenceTask implements INBTSerializable<NBTTagCompound>
	{
		@Getter
		public boolean isJob = true;
		public boolean constant = false;
		public int strength = 15, duration = 20, repeats = 1;

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setBoolean("isJob", isJob);
			nbt.setBoolean("constant", constant);
			nbt.setInteger("strength", strength);
			nbt.setInteger("duration", duration);
			nbt.setInteger("repeats", repeats);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			isJob = nbt.getBoolean("isJob");
			constant = nbt.getBoolean("constant");
			strength = MathHelper.clamp(nbt.getInteger("strength"), 0, 15);
			duration = MathHelper.clamp(nbt.getInteger("duration"), 1, DataDeviceUtils.MAX_TICKS);
			repeats = MathHelper.clamp(nbt.getInteger("repeats"), 1, DataDeviceUtils.MAX_TICKS);
		}
	}
}
