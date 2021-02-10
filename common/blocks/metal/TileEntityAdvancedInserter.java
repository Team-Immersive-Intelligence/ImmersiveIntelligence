package pl.pabilo8.immersiveintelligence.common.blocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IBlockBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ITileDrop;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.AdvancedInserter;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.api.utils.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 15-07-2019
 */
public class TileEntityAdvancedInserter extends TileEntityImmersiveConnectable implements IIEInventory, ITileDrop, IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IDataConnector
{
	public static ItemStack conn_data, conn_mv;
	public int energyStorage = 0;
	public EnumFacing outputFacing = EnumFacing.NORTH;
	public EnumFacing inputFacing = EnumFacing.SOUTH;
	public int pickProgress = 100, armDirection = 0;
	public int nextPickProgress = 0, nextDirection = 0;
	public IItemHandler insertionHandler = new IEInventoryHandler(1, this);
	protected Set<String> acceptablePowerWires = ImmutableSet.of(WireType.LV_CATEGORY, WireType.MV_CATEGORY);
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	boolean canPickBlocks = false;
	WireType secondCable;
	//The held item
	NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	//Item filters
	NonNullList<ItemStack> filters = NonNullList.withSize(10, ItemStack.EMPTY);
	String filterMode = "none";
	String filterItemMode = "item";
	int itemsToTake = 0;
	String itemTakeMode = "set";
	//place / take
	String minecartMode = "place";
	private boolean refreshWireNetwork = false;

	@Override
	protected boolean canTakeLV()
	{
		return true;
	}

	@Override
	protected boolean canTakeMV()
	{
		return true;
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
		if(amount > 0&&energyStorage < AdvancedInserter.energyCapacity)
		{
			if(!simulate)
			{
				int rec = Math.min(AdvancedInserter.energyCapacity-energyStorage, AdvancedInserter.energyUsage);
				energyStorage += rec;
				return rec;
			}
			return Math.min(AdvancedInserter.energyCapacity-energyStorage, AdvancedInserter.energyUsage);
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
			return acceptablePowerWires.contains(attachCat)&&secondCable==null;
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
		return getConnectionOffset(con, right);
	}

	@Override
	public Vec3d getConnectionOffset(Connection con, TargetingInfo target, Vec3i offsetLink)
	{
		return getConnectionOffset(con, getTargetedConnector(target)==0);
	}

	private Vec3d getConnectionOffset(Connection con, boolean data)
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

	public WireType getLimiter(int side)
	{
		if(side==0)
			return limitType;
		return secondCable;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("pickProgress"))
			pickProgress = message.getInteger("pickProgress");
		if(message.hasKey("nextPickProgress"))
			nextPickProgress = message.getInteger("nextPickProgress");

		if(message.hasKey("nextPickProgress"))
			nextPickProgress = message.getInteger("nextPickProgress");
		if(message.hasKey("nextDirection"))
			nextDirection = message.getInteger("nextDirection");

		if(message.hasKey("energyStorage"))
			energyStorage = message.getInteger("energyStorage");

		if(message.hasKey("outputFacing"))
			outputFacing = EnumFacing.getFront(message.getInteger("outputFacing"));
		if(message.hasKey("inputFacing"))
			inputFacing = EnumFacing.getFront(message.getInteger("inputFacing"));

		if(message.hasKey("canPickBlocks"))
			canPickBlocks = message.getBoolean("canPickBlocks");

		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 1);

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 1);
		filters = Utils.readInventory(nbt.getTagList("filters", 10), 10);

		filterMode = nbt.getString("filterMode");
		filterItemMode = nbt.getString("filterItemMode");

		itemsToTake = nbt.getInteger("itemsToTake");
		itemTakeMode = nbt.getString("itemTakeMode");

		pickProgress = nbt.getInteger("pickProgress");
		armDirection = nbt.getInteger("armDirection");

		nextPickProgress = nbt.getInteger("nextPickProgress");
		nextDirection = nbt.getInteger("nextDirection");

		outputFacing = EnumFacing.getFront(nbt.getInteger("outputFacing"));
		inputFacing = EnumFacing.getFront(nbt.getInteger("inputFacing"));
		if(nbt.hasKey("secondCable"))
			secondCable = ApiUtils.getWireTypeFromNBT(nbt, "secondCable");
		else
			secondCable = null;
		energyStorage = nbt.getInteger("energyStorage");
		canPickBlocks = nbt.getBoolean("canPickBlocks");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("inventory", Utils.writeInventory(inventory));
		nbt.setTag("filters", Utils.writeInventory(filters));

		nbt.setString("filterMode", filterMode);
		nbt.setString("filterItemMode", filterItemMode);

		nbt.setInteger("itemsToTake", itemsToTake);
		nbt.setString("itemTakeMode", itemTakeMode);

		nbt.setInteger("pickProgress", pickProgress);
		nbt.setInteger("armDirection", armDirection);

		nbt.setInteger("nextPickProgress", nextPickProgress);
		nbt.setInteger("nextDirection", nextDirection);

		nbt.setInteger("outputFacing", outputFacing.ordinal());
		nbt.setInteger("inputFacing", inputFacing.ordinal());
		if(secondCable!=null)
			nbt.setString("secondCable", secondCable.getUniqueName());
		nbt.setInteger("energyStorage", energyStorage);
		nbt.setBoolean("canPickBlocks", canPickBlocks);


	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)insertionHandler;
		return super.getCapability(capability, facing);
	}

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

		if(stack.hasTagCompound())
		{
			receiveMessageFromServer(stack.getTagCompound());
		}

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
		return 64;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{

	}

	@Override
	public void update()
	{
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}

		if(armDirection > 360)
			armDirection = 0;

		if(pickProgress > 99)
			pickProgress = 100;
		if(pickProgress < 1)
			pickProgress = 0;

		if(world.isRemote)
			handleSounds();

		if(energyStorage > AdvancedInserter.energyUsage&&armDirection!=nextDirection||pickProgress!=nextPickProgress)
		{
			if(pickProgress!=nextPickProgress)
			{
				if(pickProgress < nextPickProgress)
				{
					pickProgress += 100f/AdvancedInserter.grabTime;
				}
				else if(pickProgress > nextPickProgress)
				{
					pickProgress -= 100f/AdvancedInserter.grabTime;
				}

				if(Math.round(pickProgress/10f)*10f==Math.round(nextPickProgress/10f)*10f)
				{
					pickProgress = nextPickProgress;
				}

			}
			else if(energyStorage > AdvancedInserter.energyUsage&&armDirection!=nextDirection)
			{
				armDirection += Math.round(90f/AdvancedInserter.rotateTime);
				if(Math.round(armDirection/10f)*10f==Math.round(nextDirection/10f)*10f)
				{
					armDirection = nextDirection;
				}
			}
		}
		else
		{
			int prevDirection = nextDirection, prevPickProgress = nextPickProgress;

			if(hasWorld()&&!world.isRemote&&itemsToTake > 0&&outputFacing!=EnumFacing.UP&&inputFacing!=EnumFacing.UP&&energyStorage >= AdvancedInserter.energyUsage)
			{
				if(!inventory.get(0).isEmpty())
				{
					if(nextDirection!=armDirection&&armDirection!=Math.round(inputFacing.getHorizontalAngle()))
					{
						nextPickProgress = 100;
						nextDirection = Math.round(inputFacing.getHorizontalAngle());
					}
					else
					{
						if(world.isAirBlock(getPos().offset(outputFacing))&&world.getTileEntity(getPos().offset(outputFacing, 2))!=null&&world.getTileEntity(getPos().offset(outputFacing, 2)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
						{
							if(nextPickProgress==0)
							{
								IItemHandler cap = world.getTileEntity(getPos().offset(outputFacing, 2)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, outputFacing.getOpposite());
								ItemStack stack = insertionHandler.extractItem(0, 1, false);
								for(int i = 0; i < cap.getSlots(); i += 1)
								{
									if(stack.isEmpty())
										continue;
									stack = cap.insertItem(i, stack, false);
									if(stack.isEmpty())
									{
										System.out.println(itemTakeMode);
										itemsToTake -= 1;
										nextPickProgress = 100;
										nextDirection = Math.round(inputFacing.getHorizontalAngle());
									}
									else
									{
										nextPickProgress = 0;
										pickProgress = 0;
									}
								}
								energyStorage -= AdvancedInserter.energyUsage;
							}
							else
							{
								nextPickProgress = 0;
							}

						}
					}
				}
				else if(world.isAirBlock(getPos().offset(inputFacing))&&!world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(this.pos).offset(new BlockPos(0, 0, 0).offset(inputFacing, 2))).isEmpty())
				{
					if(nextDirection!=armDirection&&armDirection!=Math.round(outputFacing.getHorizontalAngle()))
					{
						nextPickProgress = 100;
						nextDirection = Math.round(outputFacing.getHorizontalAngle());
					}
					else
					{
						if(canPickBlocks&&!world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(this.pos).offset(new BlockPos(0, 0, 0).offset(inputFacing, 2))).isEmpty())
						{
							List<Entity> entities = world.getEntitiesWithinAABB(EntityMinecart.class, new AxisAlignedBB(this.pos).offset(new BlockPos(0, 0, 0).offset(inputFacing, 2)));
							IMinecartBlockPickable cart = null;
							boolean right = false;

							for(Entity item : entities)
							{
								if(item instanceof IMinecartBlockPickable)
								{
									right = true;
									cart = (IMinecartBlockPickable)item;
									break;
								}
							}

							if(right&&nextPickProgress==0&&cart!=null)
							{
								ItemStack stack = cart.getBlockForPickup().getFirst();
								insertionHandler.insertItem(0, stack, false);
								nextPickProgress = 100;
								nextDirection = Math.round(outputFacing.getHorizontalAngle());
								energyStorage -= AdvancedInserter.energyUsage;
							}
							else
							{
								nextPickProgress = 0;
							}
						}
					}
				}
				else if(world.isAirBlock(getPos().offset(inputFacing))&&world.getTileEntity(getPos().offset(inputFacing, 2))!=null&&world.getTileEntity(getPos().offset(inputFacing, 2)).hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputFacing.getOpposite()))
				{
					if(nextPickProgress==0)
					{
						IItemHandler cap = world.getTileEntity(getPos().offset(inputFacing, 2)).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, inputFacing.getOpposite());
						boolean done = false;
						for(int i = 0; i < cap.getSlots(); i += 1)
						{
							if(done)
								continue;
							if(cap.getStackInSlot(i).isEmpty())
								continue;

							if(!canTakeItem(cap.getStackInSlot(i)))
								continue;

							ItemStack stack = cap.extractItem(i, 1, false);
							insertionHandler.insertItem(0, stack, false);
							nextPickProgress = 100;
							nextDirection = Math.round(outputFacing.getHorizontalAngle());
							energyStorage -= AdvancedInserter.energyUsage;
							done = true;
						}
					}
					else
					{
						nextPickProgress = 0;
					}

				}

			}

			if(prevDirection!=nextDirection||prevPickProgress!=nextPickProgress)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("nextDirection", nextDirection);
				nbt.setInteger("nextPickProgress", nextPickProgress);
				nbt.setInteger("armDirection", armDirection);
				nbt.setInteger("pickProgress", pickProgress);
				nbt.setInteger("energyStorage", energyStorage);
				nbt.setTag("inventory", Utils.writeInventory(inventory));
				ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, nbt), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 24));
			}
		}


	}

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		if(world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			if(armDirection!=nextDirection)
			{
				world.playSound(ClientUtils.mc().player, getPos(), IISounds.inserter_backward, SoundCategory.BLOCKS, .25f, 1);
			}
			else if(pickProgress!=nextPickProgress)
			{
				world.playSound(ClientUtils.mc().player, getPos(), IISounds.inserter_forward, SoundCategory.BLOCKS, .25f, 1);
			}
		}
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;

		/*if (side==EnumFacing.UP || side==EnumFacing.DOWN)
			side=player.getHorizontalFacing().getOpposite();*/

		EnumFacing hitside = null;

		if(pl.pabilo8.immersiveintelligence.api.Utils.isPointInRectangle(0.25, 0.75, 0.75, 1, hitX, hitZ))
			hitside = EnumFacing.SOUTH;
		else if(pl.pabilo8.immersiveintelligence.api.Utils.isPointInRectangle(0.25, 0, 0.75, 0.25, hitX, hitZ))
			hitside = EnumFacing.NORTH;
		else if(pl.pabilo8.immersiveintelligence.api.Utils.isPointInRectangle(0.75, 0.25, 1, 0.75, hitX, hitZ))
			hitside = EnumFacing.EAST;
		else if(pl.pabilo8.immersiveintelligence.api.Utils.isPointInRectangle(0, 0.25, 0.25, 0.75, hitX, hitZ))
			hitside = EnumFacing.WEST;


		if(hitside!=null)
		{
			if(player.isSneaking())
			{
				if(inputFacing==hitside)
					inputFacing = EnumFacing.UP;
				outputFacing = hitside;
			}
			else
			{
				if(outputFacing==hitside)
					outputFacing = EnumFacing.UP;
				inputFacing = hitside;
			}
		}

		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("inputFacing", inputFacing.ordinal());
		nbt.setInteger("outputFacing", outputFacing.ordinal());

		ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, nbt), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 24));

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
		//[m stands for mode]
		if(packet.getPacketVariable('m').getName().equals("string"))
		{
			itemTakeMode = ((DataPacketTypeString)packet.getPacketVariable('m')).value;
		}

		//[b stands for blocks]
		if(packet.getPacketVariable('b').getName().equals("boolean"))
		{
			canPickBlocks = ((DataPacketTypeBoolean)packet.getPacketVariable('b')).value;
		}

		//[c stands for count]
		if(packet.getPacketVariable('c').getName().equals("integer"))
		{
			int items = ((DataPacketTypeInteger)packet.getPacketVariable('c')).value;
			itemsToTake = (itemTakeMode.equals("add"))?itemsToTake+items: items;
		}

		//whitelist, blacklist, none [f stands for filter]
		if(packet.getPacketVariable('f').getName().equals("string"))
		{
			filterMode = ((DataPacketTypeString)packet.getPacketVariable('f')).value;
		}
		//exact, item, oredict [g is after f]
		if(packet.getPacketVariable('g').getName().equals("string"))
		{
			filterItemMode = ((DataPacketTypeString)packet.getPacketVariable('g')).value;
		}

		//items / block [W stands for waggon]
		if(packet.getPacketVariable('w').getName().equals("string"))
		{
			minecartMode = ((DataPacketTypeString)packet.getPacketVariable('w')).value;
		}

		int i = 0;
		for(char c : new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'})
		{
			if(packet.getPacketVariable(c).getName().equals("itemstack"))
			{
				filters.set(i, ((DataPacketTypeItemStack)packet.getPacketVariable(c)).value);
			}
			i += 1;
		}

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

	@Override
	public ItemStack getTileDrop(@Nullable EntityPlayer player, IBlockState state)
	{
		ItemStack stack = new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.ADVANCED_INSERTER.getMeta());
		ItemNBTHelper.setInt(stack, "outputFacing", outputFacing.ordinal());
		ItemNBTHelper.setInt(stack, "inputFacing", inputFacing.ordinal());
		return stack;
	}

	//filterItemMode can have exact, item, oredict, nbt as values
	//filterMode can have none, whitelist, blacklist as values

	public boolean canTakeItem(ItemStack stack)
	{
		if(filterMode.equals("none"))
			return true;
		else
		{
			boolean equal = false;
			for(ItemStack s : filters)
			{
				switch(filterItemMode)
				{
					case "exact":
						equal = stack.isItemEqual(s);
						break;
					case "item":
						equal = stack.isItemEqualIgnoreDurability(s);
						break;
					case "oredict":
						for(String name : OreDictionary.getOreNames())
							if(Utils.compareToOreName(stack, name)&&Utils.compareToOreName(s, name))
							{
								equal = true;
								break;
							}
						break;
					case "nbt":
						equal = Utils.compareItemNBT(s, stack);
						break;
				}

				if(equal)
				{
					if(filterMode.equals("whitelist"))
						return true;
					else if(filterMode.equals("blacklist"))
						return false;
				}

			}

		}

		return false;
	}
}
