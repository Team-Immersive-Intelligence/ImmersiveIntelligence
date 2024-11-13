package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.api.energy.wires.redstone.IRedstoneConnector;
import blusunrize.immersiveengineering.api.energy.wires.redstone.RedstoneWireNetwork;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataStorageItem;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockRedstoneInterface;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockConnectable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.REDSTONE_CATEGORY;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityRedstoneInterface extends TileEntityMultiblockConnectable<TileEntityRedstoneInterface, IMultiblockRecipe> implements IAdvancedCollisionBounds, IAdvancedSelectionBounds, IGuiTile, IDataDevice, IRedstoneConnector
{
	public boolean rsDirty = false;
	public DataPacket storedData = new DataPacket(), storedRedstone = new DataPacket();
	//false - storedRedstone, true - storedData
	public boolean copySide = false;
	public int productionProgress = 0;
	protected RedstoneWireNetwork wireNetwork = new RedstoneWireNetwork().add(this);
	NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);
	byte[] redstoneOutput = new byte[16];
	private boolean refreshWireNetwork = false;
	private boolean redstoneChanged = false;

	public TileEntityRedstoneInterface()
	{
		super(MultiblockRedstoneInterface.INSTANCE, new int[]{1, 3, 2}, 0, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket&&nbt.hasKey("inventory"))
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 2);
			if(nbt.hasKey("copySide"))
				copySide = nbt.getBoolean("copySide");
			if(nbt.hasKey("storedData"))
				storedData.fromNBT(nbt.getCompoundTag("storedData"));
			if(nbt.hasKey("storedRedstone"))
				storedRedstone.fromNBT(nbt.getCompoundTag("storedRedstone"));
			if(nbt.hasKey("productionProgress"))
				productionProgress = nbt.getInteger("productionProgress");
		}
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				nbt.setTag("inventory", Utils.writeInventory(getInventory()));
			nbt.setBoolean("copySide", copySide);
			nbt.setTag("storedData", storedData.toNBT());
			nbt.setTag("storedRedstone", storedRedstone.toNBT());
			nbt.setInteger("productionProgress", productionProgress);
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 2);
		if(message.hasKey("copySide"))
			copySide = message.getBoolean("copySide");
		if(message.hasKey("storedData"))
			storedData.fromNBT(message.getCompoundTag("storedData"));
		if(message.hasKey("storedRedstone"))
			storedRedstone.fromNBT(message.getCompoundTag("storedRedstone"));
		if(message.hasKey("productionProgress"))
			productionProgress = message.getInteger("productionProgress");

		super.receiveMessageFromServer(message);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("copySide"))
			copySide = message.getBoolean("copySide");
		if(message.hasKey("storedData"))
			storedData.fromNBT(message.getCompoundTag("storedData"));
		if(message.hasKey("storedRedstone"))
			storedRedstone.fromNBT(message.getCompoundTag("storedRedstone"));
	}

	IItemHandler inventoryHandler = new IEInventoryHandler(2, this, 0, true, true);


	@Override
	public void update()
	{
		super.update();
		if(hasWorld()&&!world.isRemote&&!refreshWireNetwork)
		{
			refreshWireNetwork = true;
			wireNetwork.removeFromNetwork(null);
		}
		if(hasWorld()&&!world.isRemote&&rsDirty)
			wireNetwork.updateValues();
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
	}

	@Override
	public int[] getConnectionPos()

	{
		return new int[]{4};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{

	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 1;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 1;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof IDataStorageItem||stack.isEmpty();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{};
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
	{
		return false;
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[]{};
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return false;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List<AxisAlignedBB> list = new ArrayList<>();

		if(pos==4)
		{
			switch(facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0.3125, 0.3125, 0.35, 0.6875, 0.6875, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.3125, 0.3125, 0, 0.6875, 0.6875, 0.65).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0, 0.3125, 0.3125, 0.65, 0.6875, 0.6875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0.35, 0.3125, 0.3125, 1, 0.6875, 0.6875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

		return list;
	}

	@Override
	public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
	{
		return false;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedColisionBounds()
	{
		return getAdvancedSelectionBounds();
	}

	@Override
	protected boolean canTakeMV()
	{
		return false;
	}

	@Override
	protected boolean canTakeLV()
	{
		return false;
	}

	@Override
	protected boolean canTakeHV()
	{
		return false;
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		if(!REDSTONE_CATEGORY.equals(cableType.getCategory()))
			return false;
		return limitType==null;
	}

	@Override
	public Vec3d getConnectionOffset(Connection con)
	{
		return new Vec3d(.5, .625, .5);
	}

	@Override
	public Set<BlockPos> getIgnored(IImmersiveConnectable other)
	{
		return ImmutableSet.of(getPos(), getPos().offset(facing.getOpposite(), 1));
	}

	@Override
	public boolean isEnergyOutput()
	{
		return false;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_DATA_REDSTONE_INTERFACE_DATA.ordinal();
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		TileEntityRedstoneInterface master = master();
		if(master==null)
			return;

		if(pos==0&&side==facing.getOpposite())
		{
			for(char c : packet.variables.keySet())
			{
				if(storedData.variables.containsKey(c))
				{
					if(storedData.variables.get(c) instanceof DataTypeArray)
					{
						DataTypeArray a = (DataTypeArray)storedData.variables.get(c);
						DataTypeInteger int1 = (DataTypeInteger)a.value[0];
						DataTypeInteger int2 = (DataTypeInteger)a.value[1];
						redstoneOutput[int1.value] = getRedstoneFromPacket(int2.value, packet, c);

					}
					else
						storedData.removeVariable(c);
				}
			}
			master.redstoneChanged = true;
			if(getTileForPos(4)!=null)
				getTileForPos(4).getNetwork().updateValues();
			master.redstoneChanged = false;
		}
	}

	private byte getRedstoneFromPacket(int value, DataPacket packet, char c)
	{
		switch(value)
		{
			case 0:
			{
				return 0;
			}
			case 1:
			{
				if(packet.getPacketVariable(c) instanceof DataTypeBoolean)
					return (byte)(((DataTypeBoolean)packet.getPacketVariable(c)).value?15: 0);
				else
					return 0;
			}
			case 2:
			{
				if(packet.getPacketVariable(c) instanceof DataTypeInteger)
				{
					int i = ((DataTypeInteger)packet.getPacketVariable(c)).value;
					return (byte)MathHelper.clamp(i, 0, 15);
				}
				else
					return 0;
			}
			case 3:
			{
				if(packet.getPacketVariable(c) instanceof DataTypeInteger)
				{
					int i = ((DataTypeInteger)packet.getPacketVariable(c)).value;
					return (byte)MathHelper.clamp(((float)i/255f)*15, 0, 15);
				}
				else
					return 0;
			}
			case 4:
			{
				if(packet.getPacketVariable(c) instanceof DataTypeInteger)
				{
					int i = ((DataTypeInteger)packet.getPacketVariable(c)).value;
					return (byte)MathHelper.clamp(((float)i/100f)*15, 0, 15);
				}
				else
					return 0;
			}
			case 5:
			{
				if(packet.getPacketVariable(c) instanceof DataTypeString)
				{
					String s = ((DataTypeString)packet.getPacketVariable(c)).value;
					switch(s)
					{
						case "on":
							return 15;
						case "off":
							return 0;
						case "low":
							return 4;
						case "high":
							return 12;
						case "med":
							return 8;
					}
					return 0;
				}
				else
					return 0;
			}

		}
		return 0;
	}

	private DataType getTypeFromRedstone(byte value, int type)
	{
		switch(type)
		{
			case 0:
			{
				return new DataTypeNull();
			}
			case 1:
			{
				return new DataTypeBoolean(value > 0);
			}
			case 2:
			{
				return new DataTypeInteger(value);
			}

			case 3:
			{
				return new DataTypeInteger((int)((float)value/15f*255));
			}
			case 4:
			{
				return new DataTypeInteger((int)((float)value/15f*100));
			}
			case 5:
			{
				String s;
				if(value==15)
					s = "on";
				else if(value >= 12)
					s = "high";
				else if(value >= 8)
					s = "med";
				else if(value >= 4)
					s = "low";
				else
					s = "off";

				return new DataTypeString(s);

			}
		}
		return new DataTypeNull();
	}

	@Override
	public RedstoneWireNetwork getNetwork()
	{
		return wireNetwork;
	}

	@Override
	public void setNetwork(RedstoneWireNetwork net)
	{
		wireNetwork = net;
	}

	@Override
	public void onChange()
	{
		TileEntityRedstoneInterface master = master();
		if(master!=null&&!master.redstoneChanged)
		{
			dataToRedstone();
		}
	}

	@Override
	public World getConnectorWorld()
	{
		return world;
	}

	@Override
	public void updateInput(byte[] signals)
	{
		TileEntityRedstoneInterface m = master();
		if(m!=null)
			for(int i = 0; i < 16; i += 1)
			{
				if(signals[i] < m.redstoneOutput[i])
					signals[i] = m.redstoneOutput[i];
			}

	}

	private void dataToRedstone()
	{
		TileEntityRedstoneInterface m = master();
		if(m==null||!m.storedRedstone.hasAnyVariables())
			return;

		DataPacket out = new DataPacket();
		for(char c : DataPacket.varCharacters)
		{
			if(m.storedRedstone.variables.containsKey(c)&&m.storedRedstone.variables.get(c) instanceof DataTypeArray)
			{
				DataTypeArray a = (DataTypeArray)m.storedRedstone.variables.get(c);
				int i1 = ((DataTypeInteger)a.value[0]).value;
				int i2 = ((DataTypeInteger)a.value[1]).value;

				TileEntityRedstoneInterface m4 = getTileForPos(4);
				if(m4!=null)
					out.setVariable(c, getTypeFromRedstone((byte)m4.getNetwork().getPowerOutput(i1), i2));
			}
		}

		if(!out.hasAnyVariables())
			return;
		IIDataHandlingUtils.sendPacketAdjacently(out, world, m.getPos(), facing.getOpposite());
	}

}
