package pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.inserter;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.device.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataConnector;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Inserter;
import pl.pabilo8.immersiveintelligence.common.block.data_device.BlockIIDataDevice.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Pabilo8
 * @since 28.09.2020
 */
public abstract class TileEntityInserterBase extends TileEntityImmersiveConnectable implements IIEInventory, ITileDrop, IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IDataConnector
{
	public int energyStorage = 0;
	public int pickProgress = 0;
	public int takeAmount = 64;

	public EnumFacing defaultOutputFacing = EnumFacing.NORTH;
	public EnumFacing defaultInputFacing = EnumFacing.SOUTH;

	public int defaultOutputDistance = 2, defaultInputDistance = 2;

	@Nullable
	public InserterTask current = null;
	ArrayList<InserterTask> tasks = new ArrayList<>();
	NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY); //The currently held item
	public IItemHandler insertionHandler = new IEInventoryHandler(1, this);

	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;
	private WireType secondCable;
	public boolean nextTaskAfterFinish = true;

	@Override
	protected boolean canTakeLV()
	{
		return getAcceptedPowerWires().contains(WireType.LV_CATEGORY);
	}

	@Override
	protected boolean canTakeMV()
	{
		return getAcceptedPowerWires().contains(WireType.MV_CATEGORY);
	}

	@Override
	protected boolean canTakeHV()
	{
		return getAcceptedPowerWires().contains(WireType.HV_CATEGORY);
	}

	@Override
	public boolean canConnect()
	{
		return true;
	}

	@Override
	public boolean isEnergyOutput()
	{
		return true;
	}

	@Override
	public int outputEnergy(int amount, boolean simulate, int energyType)
	{
		if(amount > 0&&energyStorage < getEnergyCapacity())
		{
			if(!simulate)
			{
				int rec = Math.min(getEnergyCapacity()-energyStorage, getEnergyUsage());
				energyStorage += rec;
				return rec;
			}
			return Math.min(getEnergyCapacity()-energyStorage, getEnergyUsage());
		}
		return 0;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		int tc = getTargetedConnector(target);
		return canAttach(cableType, tc);
	}

	private boolean canAttach(WireType toAttach, int conn)
	{
		String attachCat = toAttach.getCategory();

		if(attachCat==null)
			return false;

		if(conn==0)
		{
			return attachCat.equals(IIDataWireType.DATA_CATEGORY)&&limitType==null;
		}
		else if(conn==1)
		{
			return getAcceptedPowerWires().contains(attachCat)&&secondCable==null;
		}

		return false;
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		switch(getTargetedConnector(target))
		{
			case 0:
				if(this.limitType==null)
				{
					DataWireNetwork.updateConnectors(pos, world, wireNetwork);
					this.limitType = cableType;
				}
				break;
			case 1:
				if(secondCable==null)
					this.secondCable = cableType;
				break;
		}
		this.markContainingBlockForUpdate(null);
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
		}
		return null;
	}

	@Override
	public void removeCable(Connection connection)
	{
		WireType type = connection!=null?connection.cableType: null;
		if(type==null)
		{
			limitType = null;
			secondCable = null;
		}
		if(type==limitType)
		{
			wireNetwork.removeFromNetwork(this);
			this.limitType = null;
		}
		if(type==secondCable)
			this.secondCable = null;
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		boolean right = con.cableType==limitType;
		return getConnectionOffset(right);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectionOffset(getTargetedConnector(target)==0);
	}

	private Vec3d getConnectionOffset(boolean data)
	{
		if(data)
			return new Vec3d(0.875f, 0.5f, 0.875f);
		else
			return new Vec3d(0.125f, 0.475f, 0.125f);
	}

	public int getTargetedConnector(TargetingInfo target)
	{
		if(target.hitX < 1&&target.hitX > 0.75&&target.hitZ < 1&&target.hitZ > 0.75)
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("pickProgress"))
			pickProgress = message.getInteger("pickProgress");
		if(message.hasKey("takeAmount"))
			takeAmount = Math.min(message.getInteger("takeAmount"), getMaxTakeAmount());
		if(message.hasKey("nextTaskAfterFinish"))
			nextTaskAfterFinish = message.getBoolean("nextTaskAfterFinish");

		if(message.hasKey("energyStorage"))
			energyStorage = message.getInteger("energyStorage");

		if(message.hasKey("outputFacing"))
			defaultOutputFacing = EnumFacing.getFront(message.getInteger("outputFacing"));
		if(message.hasKey("inputFacing"))
			defaultInputFacing = EnumFacing.getFront(message.getInteger("inputFacing"));

		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 1);

		if(message.hasKey("tasks"))
			readTasks(message.getTagList("tasks", 10));
		if(message.hasKey("current"))
		{
			NBTTagCompound tag = message.getCompoundTag("current");
			Function<NBTTagCompound, InserterTask> name = getAvailableTasks().get(tag.getString("name"));
			if(name!=null)
				current = name.apply(tag);
		}
		else current = null;

	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(nbt.hasKey("inventory"))
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 1);
		if(nbt.hasKey("pickProgress"))
			pickProgress = nbt.getInteger("pickProgress");
		if(nbt.hasKey("takeAmount"))
			takeAmount = Math.min(nbt.getInteger("takeAmount"), getMaxTakeAmount());
		if(nbt.hasKey("nextTaskAfterFinish"))
			nextTaskAfterFinish = nbt.getBoolean("nextTaskAfterFinish");

		if(nbt.hasKey("outputFacing"))
			defaultOutputFacing = EnumFacing.getFront(nbt.getInteger("outputFacing"));
		if(nbt.hasKey("inputFacing"))
			defaultInputFacing = EnumFacing.getFront(nbt.getInteger("inputFacing"));

		if(nbt.hasKey("secondCable"))
			secondCable = nbt.hasKey("secondCable")?ApiUtils.getWireTypeFromNBT(nbt, "secondCable"): null;
		if(nbt.hasKey("energyStorage"))
			energyStorage = nbt.getInteger("energyStorage");

		if(nbt.hasKey("tasks"))
			readTasks(nbt.getTagList("tasks", 10));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setInteger("pickProgress", pickProgress);
		nbt.setInteger("takeAmount", Math.min(takeAmount, getMaxTakeAmount()));
		nbt.setBoolean("nextTaskAfterFinish", nextTaskAfterFinish);

		nbt.setInteger("outputFacing", defaultOutputFacing.ordinal());
		nbt.setInteger("inputFacing", defaultInputFacing.ordinal());
		if(secondCable!=null)
			nbt.setString("secondCable", secondCable.getUniqueName());
		nbt.setTag("tasks", writeTasks());

		nbt.setInteger("energyStorage", energyStorage);
	}

	public NBTTagList writeTasks()
	{
		NBTTagList tagTasks = new NBTTagList();
		for(InserterTask task : tasks)
		{
			NBTTagCompound tag = task.toNBT();
			tag.setString("name", task.getName());
			tagTasks.appendTag(tag);
		}
		return tagTasks;
	}

	private void readTasks(NBTTagList tagTasks)
	{
		tasks.clear();
		for(NBTBase task : tagTasks)
		{
			if(task instanceof NBTTagCompound)
			{
				NBTTagCompound tag = (NBTTagCompound)task;
				Function<NBTTagCompound, InserterTask> name = getAvailableTasks().get(tag.getString("name"));
				if(name!=null)
					tasks.add(name.apply(tag));
			}
		}
	}

	@Override
	public void update()
	{
		if(world.isRemote)
			handleSounds();
		else if(!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(energyStorage > Inserter.energyUsage)
			performTasks();

	}

	protected void performTasks()
	{
		if(tasks.size()==0)
			return;

		if(this.current==null)
		{
			for(InserterTask task : tasks)
			{
				EnumFacing facingIn = task.facingIn==null?defaultInputFacing: task.facingIn;
				EnumFacing facingOut = task.facingOut==null?defaultOutputFacing: task.facingOut;

				BlockPos posIn = pos.offset(facingIn, task.distanceIn > 0?task.distanceIn: defaultInputDistance);
				BlockPos posOut = pos.offset(facingOut, task.distanceOut > 0?task.distanceOut: defaultOutputDistance);

				if(!task.canExecute(this, world, posIn, posOut, facingIn, facingOut, true))
					continue;
				if(!task.canExecute(this, world, posIn, posOut, facingIn, facingOut, false))
					continue;

				current = task;
				if(!world.isRemote)
					sendUpdate();
				break;
			}
		}
		//you should be, but who knows ^^
		if(this.current!=null)
		{
			EnumFacing facingIn = current.facingIn==null?defaultInputFacing: current.facingIn;
			EnumFacing facingOut = current.facingOut==null?defaultOutputFacing: current.facingOut;

			BlockPos posIn = pos.offset(facingIn, current.distanceIn > 0?current.distanceIn: defaultInputDistance);
			BlockPos posOut = pos.offset(facingOut, current.distanceOut > 0?current.distanceOut: defaultOutputDistance);

			int maxProgress = (int)(getPickupSpeed()*(1+current.getTimeModifier()));


			if(pickProgress==0)
			{
				if(!world.isRemote)
				{
					if(current.canExecute(this, world, posIn, posOut, facingIn, facingOut, true))
					{
						this.current.execute(this, world, posIn, posOut, facingIn, facingOut, true);
						pickProgress++;
					}
					else
						this.current = null;
					sendUpdate();
				}
				else
					pickProgress++;

				//better check

			}
			else if(pickProgress==maxProgress)
			{
				if(!world.isRemote)
				{
					if(this.current.execute(this, world, posIn, posOut, facingIn, facingOut, false)) //most of the time, if inventory.get(0).isEmpty()
					{
						int id = tasks.indexOf(current);
						if(!current.shouldContinue())
							tasks.remove(current);
						current = (tasks.size() > 0)?tasks.get((id+(nextTaskAfterFinish?1: 0))%tasks.size()): null;
						pickProgress = 0;
						sendUpdate();
					}
				}
				else if(this.current.canExecute(this, world, posIn, posOut, facingIn, facingOut, false))
				{
					pickProgress = 0;
				}

			}
			else if(pickProgress < maxProgress)
				pickProgress++;
		}

	}

	protected void sendUpdate()
	{
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
				.withTag("inventory", Utils.writeInventory(inventory))
				.withInt("pickProgress", pickProgress)
				.withInt("takeAmount", takeAmount)
				.withBoolean("nextTaskAfterFinish", nextTaskAfterFinish)
				.withInt("outputFacing", defaultOutputFacing.ordinal())
				.withInt("inputFacing", defaultInputFacing.ordinal())
				.withTag("tasks", writeTasks())
				.withInt("energyStorage", energyStorage)
				.conditionally(current!=null, e -> e.withTag("current",
						EasyNBT.wrapNBT(current.toNBT())
								.withString("name", current.getName())
				))
		));
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

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
		if(stack.getTagCompound()!=null)
			receiveMessageFromServer(stack.getTagCompound());

	}

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
		return (current!=null&&current.overrideTakeAmount!=-1)?current.overrideTakeAmount: takeAmount;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	@Override
	public boolean hammerUseSide(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;

		EnumFacing hitside = null;

		if(IIMath.isPointInRectangle(0.25, 0.75, 0.75, 1, hitX, hitZ))
			hitside = EnumFacing.SOUTH;
		else if(IIMath.isPointInRectangle(0.25, 0, 0.75, 0.25, hitX, hitZ))
			hitside = EnumFacing.NORTH;
		else if(IIMath.isPointInRectangle(0.75, 0.25, 1, 0.75, hitX, hitZ))
			hitside = EnumFacing.EAST;
		else if(IIMath.isPointInRectangle(0, 0.25, 0.25, 0.75, hitX, hitZ))
			hitside = EnumFacing.WEST;


		if(hitside!=null)
		{
			if(player.isSneaking())
			{
				if(defaultInputFacing==hitside)
					defaultInputFacing = EnumFacing.UP;
				defaultOutputFacing = hitside;
			}
			else
			{
				if(defaultOutputFacing==hitside)
					defaultOutputFacing = EnumFacing.UP;
				defaultInputFacing = hitside;
			}
		}

		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
				.withInt("inputFacing", defaultInputFacing.ordinal())
				.withInt("outputFacing", defaultOutputFacing.ordinal())
		));

		return true;
	}

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
		{
			markDirty();
			IBlockState stateHere = world.getBlockState(pos);
			markContainingBlockForUpdate(stateHere);
		}
	}

	@Override
	public World getConnectorWorld()
	{
		return getWorld();
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		// TODO: 21.12.2021 packet receiving
	}

	@Override
	public void sendPacket(DataPacket packet)
	{
		//Nope
	}

	@Override
	public boolean moveConnectionTo(Connection c, BlockPos newEnd)
	{
		return true;
	}

	@Nonnull
	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.INSERTER.getMeta());
		ItemNBTHelper.setInt(stack, "outputFacing", defaultOutputFacing.ordinal());
		ItemNBTHelper.setInt(stack, "inputFacing", defaultInputFacing.ordinal());
		return stack;
	}

	//Abstract variables

	/**
	 * @return names of all power wires able to connect to this inserter
	 */
	@Nonnull
	protected abstract Set<String> getAcceptedPowerWires();

	/**
	 * @return how many ticks does it take to perform a task
	 */
	public abstract int getPickupSpeed();

	/**
	 * @return how much IF this inserter uses per tick
	 */
	public abstract int getEnergyUsage();

	/**
	 * @return how much IF this inserter can store
	 */
	public abstract int getEnergyCapacity();

	/**
	 * @return max amount of items this inserter can take per operation
	 */
	public abstract int getMaxTakeAmount();

	/**
	 * @return all available tasks for this inserter
	 */
	@Nonnull
	protected abstract HashMap<String, Function<NBTTagCompound, InserterTask>> getAvailableTasks();

	/**
	 * Control the sounds played here.
	 */
	@SideOnly(Side.CLIENT)
	protected abstract void handleSounds();

	@ParametersAreNonnullByDefault
	public static abstract class InserterTask
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
		 * Inserter will only take the item if the item matches this
		 **/
		IngredientStack stack = new IngredientStack("*");
		/**
		 * Max number of items to be taken per operation, overrides {@link #takeAmount} if different from -1
		 **/
		public int overrideTakeAmount = -1;
		/**
		 * If true, the inserter won't take the items if the amount is lower
		 */
		public boolean strictAmount = false;
		/**
		 * Whether the task shouldn't end after items are taken
		 **/
		boolean isJob = true;

		public InserterTask(@Nullable EnumFacing facingIn, @Nullable EnumFacing facingOut)
		{
			this.facingIn = facingIn;
			this.facingOut = facingOut;
		}

		public InserterTask(NBTTagCompound nbt)
		{
			if(nbt.hasKey("facingIn"))
			{
				facingIn = EnumFacing.getFront(nbt.getInteger("facingIn"));
				if(nbt.hasKey("distanceIn"))
					distanceIn = nbt.getInteger("distanceIn");
			}
			if(nbt.hasKey("facingOut"))
			{
				facingOut = EnumFacing.getFront(nbt.getInteger("facingOut"));
				if(nbt.hasKey("distanceOut"))
					distanceOut = nbt.getInteger("distanceOut");
			}
			nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
			if(nbt.hasKey("isJob"))
				isJob = nbt.getBoolean("isJob");
			if(nbt.hasKey("strictAmount"))
				strictAmount = nbt.getBoolean("strictAmount");
			if(nbt.hasKey("overrideTakeAmount"))
				overrideTakeAmount = nbt.getInteger("overrideTakeAmount");
		}

		public NBTTagCompound toNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			if(facingIn!=null)
			{
				nbt.setInteger("facingIn", facingIn.getIndex());
				if(distanceIn!=-1)
					nbt.setInteger("distanceOut", distanceIn);
			}
			if(facingOut!=null)
			{
				nbt.setInteger("facingOut", facingOut.getIndex());
				if(distanceOut!=-1)
					nbt.setInteger("distanceOut", distanceOut);
			}
			IngredientStack.readFromNBT(nbt.getCompoundTag("stack"));

			nbt.setBoolean("isJob", isJob);
			nbt.setBoolean("strictAmount", strictAmount);
			if(overrideTakeAmount!=-1)
				nbt.setInteger("overrideTakeAmount", overrideTakeAmount);

			return nbt;
		}

		/**
		 * @return whether the task shouldn't be removed from task list, default true
		 */
		public boolean shouldContinue()
		{
			return true;
		}

		/**
		 * @return whether the inserter can execute or continue the task
		 */
		public abstract boolean canExecute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in);

		public abstract boolean execute(TileEntityInserterBase tile, World world, BlockPos posIn, BlockPos posOut, EnumFacing facingIn, EnumFacing facingOut, boolean in);

		abstract String getName();

		public abstract float getTimeModifier();
	}

	public final EnumFacing getCurrentInputFacing()
	{
		return current==null||current.facingIn==null?defaultInputFacing: current.facingIn;
	}

	public final EnumFacing getCurrentOutputFacing()
	{
		return current==null||current.facingOut==null?defaultOutputFacing: current.facingOut;
	}
}

