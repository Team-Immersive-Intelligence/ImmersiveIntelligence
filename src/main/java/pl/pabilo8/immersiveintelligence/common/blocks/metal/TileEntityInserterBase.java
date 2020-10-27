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
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Inserter;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.DataWireNetwork;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeString;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_Connector;
import pl.pabilo8.immersiveintelligence.common.wire.IIDataWireType;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Pabilo8
 * @since 28.09.2020
 */
public abstract class TileEntityInserterBase extends TileEntityImmersiveConnectable implements IIEInventory, ITileDrop, IComparatorOverride, IHammerInteraction, ITickable, IBlockBounds, IDataConnector
{
	public static ItemStack conn_data, conn_mv;

	public int energyStorage = 0;
	public EnumFacing outputFacing = EnumFacing.NORTH;
	public EnumFacing inputFacing = EnumFacing.SOUTH;

	public int pickProgress = 100, armDirection = 0;
	public int nextPickProgress = 0, nextDirection = 0;
	public IItemHandler insertionHandler = new IEInventoryHandler(1, this);
	protected DataWireNetwork wireNetwork = new DataWireNetwork().add(this);
	private boolean refreshWireNetwork = false;
	WireType secondCable;

	//The held item
	NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

	//how many items will the inserter take
	int itemsToTake = 0;
	//mode of taking items, add to already requested or set
	String itemTakeMode = "add";

	//current operation, grab_inventory, grab_block
	String operation = "grab_inventory";


	protected abstract Set<String> getAcceptedPowerWires();

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
		if(amount > 0&&energyStorage < Inserter.energyCapacity)
		{
			if(!simulate)
			{
				int rec = Math.min(Inserter.energyCapacity-energyStorage, Inserter.energyUsage);
				energyStorage += rec;
				return rec;
			}
			return Math.min(Inserter.energyCapacity-energyStorage, Inserter.energyUsage);
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

		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 1);

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 1);

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
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("inventory", Utils.writeInventory(inventory));

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

		if(energyStorage > Inserter.energyUsage&&armDirection!=nextDirection||pickProgress!=nextPickProgress)
		{
			performRotation();
		}
		else
		{
			boolean update = false;

			if(update)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("nextDirection", nextDirection);
				nbt.setInteger("nextPickProgress", nextPickProgress);
				nbt.setInteger("armDirection", armDirection);
				nbt.setInteger("pickProgress", pickProgress);
				nbt.setInteger("energyStorage", energyStorage);
				nbt.setInteger("outputFacing", outputFacing.ordinal());
				nbt.setInteger("inputFacing", inputFacing.ordinal());
				nbt.setTag("inventory", Utils.writeInventory(inventory));
				ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, nbt), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 24));
			}
		}


	}

	protected void performRotation()
	{
		if(pickProgress!=nextPickProgress)
		{
			if(pickProgress < nextPickProgress)
			{
				pickProgress += 100f/Inserter.grabTime;
			}
			else if(pickProgress > nextPickProgress)
			{
				pickProgress -= 100f/Inserter.grabTime;

			}

			if(Math.round(pickProgress/10f)*10f==Math.round(nextPickProgress/10f)*10f)
			{
				pickProgress = nextPickProgress;
			}

		}
		else if(energyStorage > Inserter.energyUsage&&armDirection!=nextDirection)
		{
			armDirection += Math.round(90f/Inserter.rotateTime);
			if(Math.round(armDirection/10f)*10f==Math.round(nextDirection/10f)*10f)
			{
				armDirection = nextDirection;
			}
		}
	}

	@Override
	public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(world.isRemote)
			return true;

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
		if(packet.getPacketVariable('m').getName().equals("string"))
		{
			this.itemTakeMode = ((DataPacketTypeString)packet.getPacketVariable('m')).value;
		}

		if(packet.getPacketVariable('c').getName().equals("integer"))
		{
			// TODO: 29.09.2020 modes
			int items = ((DataPacketTypeInteger)packet.getPacketVariable('c')).value;
			this.itemsToTake = (itemTakeMode.equals("add"))?itemsToTake+items: items;
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
		ItemStack stack = new ItemStack(state.getBlock(), 1, IIBlockTypes_Connector.INSERTER.getMeta());
		ItemNBTHelper.setInt(stack, "outputFacing", outputFacing.ordinal());
		ItemNBTHelper.setInt(stack, "inputFacing", inputFacing.ordinal());
		return stack;
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
}

