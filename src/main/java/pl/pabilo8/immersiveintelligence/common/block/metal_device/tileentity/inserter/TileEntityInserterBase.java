package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.crafting.IngredientReference;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyMultiTypeCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.ITypeNBTSerializable;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.tile.TileEntityIIConnectable;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Provides common wire, task, inventory, synchronization, and animation logic for Inserters.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 12.08.2026
 * @since 28.09.2020
 */
public abstract class TileEntityInserterBase extends TileEntityIIConnectable implements IIEInventory, ITileDrop,
		IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IDataConnector, IIIGuiMultiblockTile
{
	private static final int TASK_RETRY_DELAY = 10;

	@SyncNBT(name = "energyStorage", events = {SyncEvents.TILE_ENERGY_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public int energyStorage = 0;
	@SyncNBT(name = "pickProgress", events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public int pickProgress = 0;
	@SyncNBT(name = "takeAmount", events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public int takeAmount = getMaxTakeAmount();

	@SyncNBT(name = "outputFacing", events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_DROP_AS_ITEM})
	public EnumFacing defaultOutputFacing = EnumFacing.NORTH;
	@SyncNBT(name = "inputFacing", events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_DROP_AS_ITEM})
	public EnumFacing defaultInputFacing = EnumFacing.SOUTH;

	public int defaultOutputDistance = 2, defaultInputDistance = 2;

	@Nullable
	public InserterTask current = null;
	@SyncNBT(name = "currentTaskIndex", events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public int currentTaskIndex = -1;
	@SyncNBT(name = "inventory", events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	@SyncNBT(name = "nextTaskAfterFinish", events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public boolean nextTaskAfterFinish = true;
	@SyncNBT(name = "tasks", events = {SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_GUI_OPENED})
	public EasyMultiTypeCollection<InserterTask> tasks = new EasyMultiTypeCollection<>(InserterTask.class);
	@SyncNBT(name = "taskRetryDelay")
	public int taskRetryDelay = 0;
	@SyncNBT(name = "secondCable", events = SyncEvents.TILE_CUSTOM2, nullable = true)
	public WireType secondCable;

	public final IItemHandler insertionHandler = new IEInventoryHandler(1, this);
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityInserterBase> pitchSound;
	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityInserterBase> yawSound;

	//--- Wire system ---//

	@Override
	public boolean acceptsWireType(WireType wireType)
	{
		String category = wireType.getCategory();
		return IIDataWireType.DATA_CATEGORY.equals(category)||getAcceptedPowerWires().contains(category);
	}

	@Override
	public boolean isRelay()
	{
		return false;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		if(amount <= 0||energyStorage >= getEnergyCapacity())
			return 0;

		int received = Math.min(amount, Math.min(getEnergyCapacity()-energyStorage, getEnergyUsage()));
		if(!simulate)
		{
			boolean wasPowered = hasTaskEnergy();
			energyStorage += received;
			markDirty();
			if(!wasPowered&&hasTaskEnergy())
				updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
		}
		return received;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		return canAttach(cableType, getTargetedConnector(target));
	}

	private boolean canAttach(WireType toAttach, int connector)
	{
		String category = toAttach.getCategory();
		if(category==null)
			return false;

		if(connector==0)
			return IIDataWireType.DATA_CATEGORY.equals(category)&&limitType==null;
		if(connector==1)
			return getAcceptedPowerWires().contains(category)&&secondCable==null;
		return false;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		switch(getTargetedConnector(target))
		{
			case 0:
				if(limitType==null)
				{
					DataWireNetwork.updateConnectors(pos, world, wireNetwork);
					limitType = cableType;
				}
				break;
			case 1:
				if(secondCable==null)
					secondCable = cableType;
				break;
		}
		markDirty();
		if(!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	@Override
	public WireType getCableLimiter(TargetingInfo target)
	{
		switch(getTargetedConnector(target))
		{
			case 0:
				return limitType;
			case 1:
				return secondCable;
			default:
				return null;
		}
	}

	@Override
	public void removeCable(@Nullable Connection connection)
	{
		WireType type = connection==null?null: connection.cableType;
		if(type==null||type==limitType)
		{
			wireNetwork.removeFromNetwork(this);
			limitType = null;
		}
		if(type==null||type==secondCable)
			secondCable = null;

		markDirty();
		if(world!=null&&!world.isRemote)
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
	}

	@Override
	public void onConnectivityUpdate(BlockPos pos, int dimension)
	{
		super.onConnectivityUpdate(pos, dimension);
		refreshWireNetwork = false;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return getConnectionOffset(con.cableType==limitType);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectionOffset(getTargetedConnector(target)==0);
	}

	private Vec3d getConnectionOffset(boolean data)
	{
		return data?new Vec3d(0.875f, 0.5f, 0.875f): new Vec3d(0.125f, 0.475f, 0.125f);
	}

	/**
	 * Gets the connector selected by the wire hit position.
	 *
	 * @param target wire target data
	 * @return 0 for data or 1 for power
	 */
	public int getTargetedConnector(TargetingInfo target)
	{
		return target.hitX < 1&&target.hitX > 0.75&&target.hitZ < 1&&target.hitZ > 0.75?0: 1;
	}

	//--- Tick and task handling ---//

	@Override
	public void update()
	{
		refreshCurrentTask();
		if(world.isRemote)
		{
			handleSounds();
			if(current!=null&&hasTaskEnergy())
				performTasks();
			return;
		}

		if(!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(taskRetryDelay > 0)
			taskRetryDelay--;

		if(!hasTaskEnergy())
			return;

		if(performTasks())
		{
			energyStorage = Math.max(0, energyStorage-getEnergyUsage());
			markDirty();
			if(!hasTaskEnergy())
				updateTileForEvent(SyncEvents.TILE_ENERGY_CHANGED);
		}
	}

	protected boolean performTasks()
	{
		if(tasks.isEmpty())
		{
			setCurrentTask(null);
			return false;
		}

		if(current==null)
		{
			if(world.isRemote||taskRetryDelay > 0)
				return false;

			for(InserterTask task : tasks)
			{
				task.clearCachedTargets();
				EnumFacing facingIn = task.facingIn==null?defaultInputFacing: task.facingIn;
				EnumFacing facingOut = task.facingOut==null?defaultOutputFacing: task.facingOut;
				BlockPos posIn = pos.offset(facingIn, task.distanceIn > 0?task.distanceIn: defaultInputDistance);
				BlockPos posOut = pos.offset(facingOut, task.distanceOut > 0?task.distanceOut: defaultOutputDistance);

				if(task.canExecute(this, world, posIn, posOut, facingIn, facingOut, true)
						&&task.canExecute(this, world, posIn, posOut, facingIn, facingOut, false))
				{
					setCurrentTask(task);
					taskRetryDelay = 0;
					break;
				}
				task.clearCachedTargets();
			}

			if(current==null)
			{
				taskRetryDelay = TASK_RETRY_DELAY;
				return false;
			}
		}

		EnumFacing facingIn = current.facingIn==null?defaultInputFacing: current.facingIn;
		EnumFacing facingOut = current.facingOut==null?defaultOutputFacing: current.facingOut;
		BlockPos posIn = pos.offset(facingIn, current.distanceIn > 0?current.distanceIn: defaultInputDistance);
		BlockPos posOut = pos.offset(facingOut, current.distanceOut > 0?current.distanceOut: defaultOutputDistance);
		int maxProgress = getTaskDuration(current);

		if(pickProgress==0)
		{
			if(world.isRemote)
			{
				pickProgress++;
				return true;
			}

			boolean canStart = current.canExecute(this, world, posIn, posOut, facingIn, facingOut, true)
					&&current.canExecute(this, world, posIn, posOut, facingIn, facingOut, false);
			boolean started = canStart&&current.execute(this, world, posIn, posOut, facingIn, facingOut, true);
			current.clearCachedTargets();
			if(started)
			{
				pickProgress++;
				updateTileForEvent(SyncEvents.TILE_CUSTOM1);
				return true;
			}

			setCurrentTask(null);
			updateTileForEvent(SyncEvents.TILE_CUSTOM1);
			return false;
		}

		if(pickProgress >= maxProgress)
		{
			if(world.isRemote)
				return false;

			boolean completed = current.canExecute(this, world, posIn, posOut, facingIn, facingOut, false)
					&&current.execute(this, world, posIn, posOut, facingIn, facingOut, false);
			current.clearCachedTargets();
			if(completed)
			{
				int id = tasks.indexOf(current);
				boolean removed = !current.shouldContinue();
				if(removed)
					tasks.remove(current);

				if(tasks.isEmpty())
					setCurrentTask(null);
				else
				{
					int next = Math.max(0, id)+(removed?0: nextTaskAfterFinish?1: 0);
					setCurrentTask(tasks.get(next%tasks.size()));
				}
				pickProgress = 0;
				updateTileForEvent(removed?SyncEvents.TILE_RECIPE_CHANGED: SyncEvents.TILE_CUSTOM1);
				return true;
			}
			return false;
		}

		pickProgress++;
		return true;
	}

	private boolean hasTaskEnergy()
	{
		return energyStorage >= getEnergyUsage();
	}

	private int getTaskDuration(InserterTask task)
	{
		return Math.max(1, (int)(getPickupSpeed()*(1+task.getTimeModifier())));
	}

	private void setCurrentTask(@Nullable InserterTask task)
	{
		current = task;
		currentTaskIndex = task==null?-1: tasks.indexOf(task);
		if(currentTaskIndex < 0)
			current = null;
	}

	protected final void refreshCurrentTask()
	{
		if(current!=null)
		{
			int actualIndex = tasks.indexOf(current);
			if(actualIndex >= 0)
			{
				currentTaskIndex = actualIndex;
				return;
			}
		}

		if(currentTaskIndex >= 0&&currentTaskIndex < tasks.size())
			current = tasks.get(currentTaskIndex);
		else
		{
			current = null;
			currentTaskIndex = -1;
		}
	}

	//--- Repeated sounds ---//

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		if(isArmMoving()&&(pitchSound==null||pitchSound.isDonePlaying()))
		{
			pitchSound = new ConditionCompoundSound<>(IISounds.inserterPitchM,
					new Vec3d(pos).addVector(0.5, 0.5, 0.5), this, TileEntityInserterBase::isArmMoving);
			pitchSound.setVolume(0.25f);
		}
		if(isArmRotating()&&(yawSound==null||yawSound.isDonePlaying()))
		{
			yawSound = new ConditionCompoundSound<>(IISounds.inserterYawM,
					new Vec3d(pos).addVector(0.5, 0.5, 0.5), this, TileEntityInserterBase::isArmRotating);
			yawSound.setVolume(0.25f);
		}
	}

	private boolean isArmMoving()
	{
		return !isInvalid()&&current!=null&&hasTaskEnergy()
				&&pickProgress > 0&&pickProgress < getTaskDuration(current);
	}

	private boolean isArmRotating()
	{
		if(!isArmMoving())
			return false;
		EnumFacing input = getCurrentInputFacing();
		EnumFacing output = getCurrentOutputFacing();
		return input.getAxis()!=EnumFacing.Axis.Y&&output.getAxis()!=EnumFacing.Axis.Y&&input!=output;
	}

	//--- Capability lookup ---//

	/**
	 * Finds a capability on the target side or on the top face of a tile or entity.
	 *
	 * @param world      target world
	 * @param pos        target block position
	 * @param capability capability to find
	 * @param side       preferred side
	 * @param <T>        capability type
	 * @return capability instance or null
	 */
	@Nullable
	public static <T> T findTargetCapability(World world, BlockPos pos, Capability<T> capability, EnumFacing side)
	{
		TileEntity tile = world.getTileEntity(pos);
		T found = getCapability(tile, capability, side);
		if(found!=null)
			return found;

		for(Entity entity : world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos)))
		{
			found = getCapability(entity, capability, side);
			if(found!=null)
				return found;
		}
		return null;
	}

	@Nullable
	private static <T> T getCapability(@Nullable ICapabilityProvider provider, Capability<T> capability, EnumFacing side)
	{
		if(provider==null)
			return null;
		T found = provider.getCapability(capability, side);
		if(found==null&&side!=EnumFacing.UP)
			found = provider.getCapability(capability, EnumFacing.UP);
		return found;
	}

	private static class CachedCapability<T>
	{
		private final BlockPos pos;
		private final EnumFacing side;
		private final Capability<T> capability;
		@Nullable
		private final T value;

		private CachedCapability(World world, BlockPos pos, Capability<T> capability, EnumFacing side)
		{
			this.pos = pos;
			this.side = side;
			this.capability = capability;
			this.value = findTargetCapability(world, pos, capability, side);
		}

		private boolean matches(BlockPos pos, Capability<?> capability, EnumFacing side)
		{
			return this.capability==capability&&this.side==side&&this.pos.equals(pos);
		}
	}

	//--- Inventory ---//

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		return capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY||super.hasCapability(capability, facing);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return true;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return current!=null&&current.overrideTakeAmount!=-1?current.overrideTakeAmount: takeAmount;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
	}

	//--- Block and placement ---//

	@Nonnull
	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0f, 0, 0f, 1f, 0.1875f, 1f};
	}

	@Override
	public int getComparatorInputOverride()
	{
		return 0;
	}

	@Override
	public void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack)
	{
		//noinspection unchecked
		if(stack.hasTagCompound())
			NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.deserializeAll(tile, stack.getTagCompound(), true));
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		return withDropData(new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.INSERTER.getMeta()));
	}

	protected ItemStack withDropData(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		//noinspection unchecked
		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeForEvent(tile, nbt, SyncEvents.TILE_DROP_AS_ITEM));
		if(!nbt.hasNoTags())
			stack.setTagCompound(nbt);
		return stack;
	}

	@Override
	public boolean hammerUseSide(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;

		EnumFacing hitSide = null;
		if(IIMath.isPointInRectangle(0.25, 0.75, 0.75, 1, hitX, hitZ))
			hitSide = EnumFacing.SOUTH;
		else if(IIMath.isPointInRectangle(0.25, 0, 0.75, 0.25, hitX, hitZ))
			hitSide = EnumFacing.NORTH;
		else if(IIMath.isPointInRectangle(0.75, 0.25, 1, 0.75, hitX, hitZ))
			hitSide = EnumFacing.EAST;
		else if(IIMath.isPointInRectangle(0, 0.25, 0.25, 0.75, hitX, hitZ))
			hitSide = EnumFacing.WEST;

		if(hitSide!=null)
		{
			if(player.isSneaking())
			{
				if(defaultInputFacing==hitSide)
					defaultInputFacing = EnumFacing.UP;
				defaultOutputFacing = hitSide;
			}
			else
			{
				if(defaultOutputFacing==hitSide)
					defaultOutputFacing = EnumFacing.UP;
				defaultInputFacing = hitSide;
			}
			markDirty();
			updateTileForEvent(SyncEvents.TILE_CUSTOM2);
		}
		return true;
	}

	//--- Data network ---//

	@Override
	public DataWireNetwork getDataNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void setDataNetwork(DataWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public void onDataChange()
	{
		if(!isInvalid())
			markDirty();
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		// Implemented by concrete Inserters.
	}

	@Override
	public void sendPacket(DataPacket packet)
	{
	}

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	/**
	 * Gets all tasks assigned to this Inserter.
	 *
	 * @return task collection
	 */
	@Nonnull
	public final EasyMultiTypeCollection<InserterTask> getTasks()
	{
		return tasks;
	}

	//--- Abstract variables ---//

	/**
	 * @return names of all power wires that can connect to this Inserter
	 */
	@Nonnull
	protected abstract Set<String> getAcceptedPowerWires();

	/**
	 * @return task duration in ticks
	 */
	public abstract int getPickupSpeed();

	/**
	 * @return IF used for each active task tick
	 */
	public abstract int getEnergyUsage();

	/**
	 * @return maximum stored IF
	 */
	public abstract int getEnergyCapacity();

	/**
	 * @return maximum amount moved per operation
	 */
	public abstract int getMaxTakeAmount();

	/**
	 * @return registered tasks available to this Inserter
	 */
	@Nonnull
	public abstract LinkedHashMap<String, Supplier<InserterTask>> getAvailableTasks();

	public final EnumFacing getCurrentInputFacing()
	{
		return current==null||current.facingIn==null?defaultInputFacing: current.facingIn;
	}

	public final EnumFacing getCurrentOutputFacing()
	{
		return current==null||current.facingOut==null?defaultOutputFacing: current.facingOut;
	}

	//--- IIIGuiMultiblockTile ---//

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
	public IIGUI getGUI()
	{
		return IIGUI.INSERTER;
	}

	@ParametersAreNonnullByDefault
	public static abstract class InserterTask implements ITypeNBTSerializable
	{
		/**
		 * Overrides facing if different from null
		 */
		@Nullable
		public EnumFacing facingIn = null, facingOut = null;
		/**
		 * Overrides distance if different from -1
		 */
		public int distanceIn = -1, distanceOut = -1;
		/**
		 * Max number of items to be taken per operation, overrides {@link #takeAmount} if different from -1
		 **/
		public int overrideTakeAmount = -1;
		/**
		 * If true, the inserter won't take the items if the amount is lower
		 */
		public boolean strictAmount = false;
		/**
		 * Inserter will only take the item if the item matches this
		 **/
		public IngredientReference stack = new IngredientReference();
		/**
		 * Whether the task shouldn't end after items are taken
		 **/
		public boolean isJob = true;

		@Nullable
		private transient CachedCapability<?> cachedInputCapability;
		@Nullable
		private transient CachedCapability<?> cachedOutputCapability;

		public InserterTask()
		{

		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("name", getName());
			if(facingIn!=null)
				nbt.setInteger("facingIn", facingIn.getIndex());
			if(distanceIn!=-1)
				nbt.setInteger("distanceIn", MathHelper.clamp(distanceIn, -1, 2));
			if(facingOut!=null)
				nbt.setInteger("facingOut", facingOut.getIndex());
			if(distanceOut!=-1)
				nbt.setInteger("distanceOut", MathHelper.clamp(distanceOut, -1, 2));
			nbt.setTag("stack", stack.serializeNBT());

			nbt.setBoolean("isJob", isJob);
			nbt.setBoolean("strictAmount", strictAmount);
			if(overrideTakeAmount!=-1)
				nbt.setInteger("overrideTakeAmount", overrideTakeAmount);

			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			if(nbt.hasKey("facingIn"))
				facingIn = EnumFacing.getFront(nbt.getInteger("facingIn"));
			if(nbt.hasKey("distanceIn"))
				distanceIn = MathHelper.clamp(nbt.getInteger("distanceIn"), -1, 2);
			if(nbt.hasKey("facingOut"))
				facingOut = EnumFacing.getFront(nbt.getInteger("facingOut"));
			if(nbt.hasKey("distanceOut"))
				distanceOut = MathHelper.clamp(nbt.getInteger("distanceOut"), -1, 2);
			if(nbt.hasKey("stack"))
				stack = IngredientReference.readFromNBT(nbt.getCompoundTag("stack"));
			if(nbt.hasKey("isJob"))
				isJob = nbt.getBoolean("isJob");
			if(nbt.hasKey("strictAmount"))
				strictAmount = nbt.getBoolean("strictAmount");
			if(nbt.hasKey("overrideTakeAmount"))
				overrideTakeAmount = nbt.getInteger("overrideTakeAmount");
		}

		/**
		 * Gets and caches a capability for one task endpoint.
		 *
		 * @param world      target world
		 * @param pos        target position
		 * @param capability capability to get
		 * @param side       preferred capability side
		 * @param input      true for the input endpoint
		 * @param <T>        capability type
		 * @return capability instance or null
		 */
		@Nullable
		@SuppressWarnings("unchecked")
		protected final <T> T getTargetCapability(World world, BlockPos pos, Capability<T> capability, EnumFacing side, boolean input)
		{
			CachedCapability<?> cached = input?cachedInputCapability: cachedOutputCapability;
			if(cached==null||!cached.matches(pos, capability, side))
			{
				cached = new CachedCapability<>(world, pos, capability, side);
				if(input)
					cachedInputCapability = cached;
				else
					cachedOutputCapability = cached;
			}
			return (T)cached.value;
		}

		/**
		 * Clears endpoint caches after one task phase.
		 */
		protected void clearCachedTargets()
		{
			cachedInputCapability = null;
			cachedOutputCapability = null;
		}

		/**
		 * @return whether the task shouldn't be removed from task list, default true
		 */
		public boolean shouldContinue()
		{
			return stack.inputSize > 0;
		}

		/**
		 * @return whether the inserter can execute or continue the task
		 */
		public abstract boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in);

		public abstract boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in);

		public abstract String getName();

		public abstract float getTimeModifier();

		/**
		 * GUI/support accessor
		 */
		public final boolean isJob()
		{
			return isJob;
		}

		/**
		 * Whether things like stack, overrideTakeAmount and strictAmount are editable in the GUI
		 */
		public boolean areDetailsEditable()
		{
			return true;
		}

		/**
		 * GUI/support accessor
		 */
		public final IngredientReference getIngredient()
		{
			return stack;
		}

		protected int getAmountToBeTaken(TileEntityInserterBase tile)
		{
			int perOp = Math.min(overrideTakeAmount!=-1?overrideTakeAmount: tile.takeAmount, tile.getMaxTakeAmount());
			return isJob?perOp: Math.min(perOp, stack.inputSize);
		}
	}
}

