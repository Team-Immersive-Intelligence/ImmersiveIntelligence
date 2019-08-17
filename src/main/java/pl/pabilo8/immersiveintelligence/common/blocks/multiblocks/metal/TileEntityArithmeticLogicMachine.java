package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataPacketTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.IDataType;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.items.ItemFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.arithmeticLogicMachine;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class TileEntityArithmeticLogicMachine extends TileEntityMultiblockMetal<TileEntityArithmeticLogicMachine, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IGuiTile, IBooleanAnimatedPartsBlock
{

	public boolean isDoorOpened = false;
	public float doorAngle = 0;
	public String renderCircuit1 = "", renderCircuit2 = "", renderCircuit3 = "", renderCircuit4 = "";
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

	public TileEntityArithmeticLogicMachine()
	{
		super(MultiblockArithmeticLogicMachine.instance, new int[]{3, 3, 2}, arithmeticLogicMachine.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket&&!isDummy())
		{
			if(nbt.hasKey("inventory"))
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		ImmersiveIntelligence.logger.info("got it");
		if(message.hasKey("expressions"))
		{
			ImmersiveIntelligence.logger.info("got it twice");
			NBTTagCompound expressions = message.getCompoundTag("expressions");

			ItemStack stack = inventoryHandler.extractItem(expressions.getInteger("page"), 1, false);
			DataPacket packet = new DataPacket();
			packet.fromNBT(expressions.getCompoundTag("list"));
			((ItemFunctionalCircuit)stack.getItem()).writeDataToItem(packet, stack);
			inventoryHandler.insertItem(expressions.getInteger("page"), stack, false);

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
		if(!descPacket&&!isDummy())
		{
			nbt.setTag("inventory", Utils.writeInventory(inventory));
		}
	}

	@Override
	public void update()
	{
		super.update();

		if(world.isRemote&&pos==8)
		{
			if(isDoorOpened&&doorAngle < 135f)
				doorAngle = Math.min(doorAngle+6.5f, 135f);
			else if(!isDoorOpened&&doorAngle > 0f)
				doorAngle = Math.max(doorAngle-3f, 0f);
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 0, 0, 0};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{16};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{};
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
		return stack.getItem() instanceof ItemFunctionalCircuit;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 4;
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
		if(world.isRemote)
		{
			if(slot==0)
				renderCircuit1 = inventoryHandler.getStackInSlot(slot).isEmpty()?"": ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(slot).getItem()).getTESRRenderTexture(inventoryHandler.getStackInSlot(slot));
			if(slot==1)
				renderCircuit2 = inventoryHandler.getStackInSlot(slot).isEmpty()?"": ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(slot).getItem()).getTESRRenderTexture(inventoryHandler.getStackInSlot(slot));
			if(slot==2)
				renderCircuit3 = inventoryHandler.getStackInSlot(slot).isEmpty()?"": ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(slot).getItem()).getTESRRenderTexture(inventoryHandler.getStackInSlot(slot));
			if(slot==3)
				renderCircuit4 = inventoryHandler.getStackInSlot(slot).isEmpty()?"": ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(slot).getItem()).getTESRRenderTexture(inventoryHandler.getStackInSlot(slot));

		}
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
	public void onSend()
	{

	}

	@Override
	public void onReceive(DataPacket packet)
	{
		if(pos==2)
		{
			master().onReceive(packet);
		}

		if(this.pos==8&&energyStorage.getEnergyStored() >= arithmeticLogicMachine.energyUsage)
		{
			energyStorage.extractEnergy(arithmeticLogicMachine.energyUsage, false);

			boolean c1 = false, c2 = false, c3 = false, c4 = false;
			DataPacket circuit1 = null, circuit2 = null, circuit3 = null, circuit4 = null;
			if(!inventoryHandler.getStackInSlot(0).isEmpty())
			{
				c1 = true;
				circuit1 = ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(0).getItem()).getStoredData(inventoryHandler.getStackInSlot(0));
			}
			if(!inventoryHandler.getStackInSlot(1).isEmpty())
			{
				c2 = true;
				circuit2 = ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(1).getItem()).getStoredData(inventoryHandler.getStackInSlot(1));
			}
			if(!inventoryHandler.getStackInSlot(2).isEmpty())
			{
				c3 = true;
				circuit3 = ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(2).getItem()).getStoredData(inventoryHandler.getStackInSlot(2));
			}
			if(!inventoryHandler.getStackInSlot(3).isEmpty())
			{
				c4 = true;
				circuit4 = ((ItemFunctionalCircuit)inventoryHandler.getStackInSlot(3).getItem()).getStoredData(inventoryHandler.getStackInSlot(3));
			}


			if(c1)
			{
				for(char c : DataPacket.varCharacters)
				{
					if(packet.variables.containsKey(c))
					{
						IDataType type = packet.getPacketVariable(c);

						if(circuit1.variables.containsKey(c))
						{
							packet.setVariable(c, ((DataPacketTypeExpression)circuit1.getPacketVariable(c)).getValue(packet));
						}
					}
				}
			}

			if(c2&&energyStorage.getEnergyStored() >= arithmeticLogicMachine.energyUsage)
			{
				for(char c : DataPacket.varCharacters)
				{
					if(packet.variables.containsKey(c))
					{
						IDataType type = packet.getPacketVariable(c);

						if(circuit2.variables.containsKey(c))
						{
							packet.setVariable(c, ((DataPacketTypeExpression)circuit2.getPacketVariable(c)).getValue(packet));
						}
					}
				}
				energyStorage.extractEnergy(arithmeticLogicMachine.energyUsage, false);
			}

			if(c3&&energyStorage.getEnergyStored() >= arithmeticLogicMachine.energyUsage)
			{
				for(char c : DataPacket.varCharacters)
				{
					if(packet.variables.containsKey(c))
					{
						IDataType type = packet.getPacketVariable(c);

						if(circuit3.variables.containsKey(c))
						{
							packet.setVariable(c, ((DataPacketTypeExpression)circuit3.getPacketVariable(c)).getValue(packet));
						}
					}
				}
				energyStorage.extractEnergy(arithmeticLogicMachine.energyUsage, false);
			}

			if(c4&&energyStorage.getEnergyStored() >= arithmeticLogicMachine.energyUsage)
			{
				for(char c : DataPacket.varCharacters)
				{
					if(packet.variables.containsKey(c))
					{
						IDataType type = packet.getPacketVariable(c);

						if(circuit4.variables.containsKey(c))
						{
							packet.setVariable(c, ((DataPacketTypeExpression)circuit4.getPacketVariable(c)).getValue(packet));
						}
					}
				}
				energyStorage.extractEnergy(arithmeticLogicMachine.energyUsage, false);
			}

			IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorAround(getBlockPosForPos(3), this.world);
			if(conn!=null)
			{
				conn.sendPacket(packet);
			}

		}
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List list = new ArrayList<AxisAlignedBB>();

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
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE;
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return master()!=null;
		return super.hasCapability(capability, facing);
	}

	IItemHandler inventoryHandler = new IEInventoryHandler(4, this, 0, true, true);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityArithmeticLogicMachine master = master();
			if(master==null)
				return null;
			return (T)this.inventoryHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void onGuiOpened(EntityPlayer player, boolean clientside)
	{
		if(!clientside)
		{
			NBTTagCompound tag = new NBTTagCompound();
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 32));
		}
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		ImmersiveIntelligence.logger.info(state);
		if(part==0)
			isDoorOpened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
			isDoorOpened = state;

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));
	}
}
