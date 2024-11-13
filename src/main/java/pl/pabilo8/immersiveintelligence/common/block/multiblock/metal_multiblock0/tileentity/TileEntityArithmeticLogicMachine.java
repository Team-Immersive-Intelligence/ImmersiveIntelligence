package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityArithmeticLogicMachine extends TileEntityMultiblockMetal<TileEntityArithmeticLogicMachine, IMultiblockRecipe> implements IDataDevice, IAdvancedBounds, IGuiTile, IBooleanAnimatedPartsBlock
{

	public boolean isDoorOpened = false;
	public float doorAngle = 0;
	public String[] renderCircuit = new String[]{"", "", "", ""};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	IItemHandler inventoryHandler = new IEInventoryHandler(4, this, 0, true, true);

	public TileEntityArithmeticLogicMachine()
	{
		super(MultiblockArithmeticLogicMachine.INSTANCE, new int[]{3, 3, 2}, ArithmeticLogicMachine.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket&&!isDummy())
			if(nbt.hasKey("inventory"))
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("expressions"))
		{
			NBTTagCompound expressions = message.getCompoundTag("expressions");

			ItemStack stack = inventoryHandler.extractItem(expressions.getInteger("page"), 1, false);
			DataPacket packet = new DataPacket();
			packet.fromNBT(expressions.getCompoundTag("list"));
			((ItemIIFunctionalCircuit)stack.getItem()).writeDataToItem(packet, stack);
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
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public void update()
	{
		super.update();

		if(isDummy())
			return;

		doorAngle = MathHelper.clamp(doorAngle+(isDoorOpened?5f: -6.5f), 0, 135f);
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
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
		return stack.getItem() instanceof ItemIIFunctionalCircuit;
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
		if(world.isRemote&&slot >= 0&&slot < renderCircuit.length)
		{
			ItemStack stack = inventoryHandler.getStackInSlot(slot);
			renderCircuit[slot] = stack.isEmpty()?"": ((ItemIIFunctionalCircuit)stack.getItem()).getTESRRenderTexture(stack);
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
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		TileEntityArithmeticLogicMachine master = master();
		if(master==null)
			return;

		if(pos==2)
			master.onReceive(packet, EnumFacing.DOWN);
		else if(pos==3)
			master.onReceive(packet, EnumFacing.UP);

		if(!isDummy()&&energyStorage.getEnergyStored() >= ArithmeticLogicMachine.energyUsage)
		{
			DataPacket newPacket = packet.clone();
			energyStorage.extractEnergy(ArithmeticLogicMachine.energyUsage, false);

			boolean[] circuit = new boolean[4];
			DataPacket[] cPacket = new DataPacket[4];

			for(int i = 0; i < 4; i++)
			{
				circuit[i] = !inventoryHandler.getStackInSlot(i).isEmpty();
				cPacket[i] = circuit[i]?
						((ItemIIFunctionalCircuit)inventoryHandler.getStackInSlot(i).getItem()).getStoredData(inventoryHandler.getStackInSlot(i)):
						null;
			}

			for(int i = 0; i < 4; i++)
				if(circuit[i])
				{
					if(energyStorage.extractEnergy(ArithmeticLogicMachine.energyUsage, false) < ArithmeticLogicMachine.energyUsage)
						break;
					for(char c : DataPacket.varCharacters)
					{
						DataType var = cPacket[i].getPacketVariable(c);
						if(var instanceof DataTypeExpression)
						{
							DataTypeExpression exp = ((DataTypeExpression)var);
							char condition = exp.getRequiredVariable();

							if(condition==' '||(newPacket.getPacketVariable(condition) instanceof DataTypeBoolean
									&&((DataTypeBoolean)newPacket.getPacketVariable(condition)).value))
								newPacket.setVariable(c, exp.getValue(newPacket));
						}
					}
				}
			newPacket.trimNulls();

			TileEntityArithmeticLogicMachine tile1 = getTileForPos(3);
			TileEntityArithmeticLogicMachine tile2 = getTileForPos(2);

			//TODO: 09.10.2024 use sendData after conversion
			if(side==EnumFacing.DOWN&&tile1!=null)
				IIDataHandlingUtils.sendPacketAdjacently(newPacket, world, tile1.getPos(), mirrored?facing.rotateYCCW(): facing.rotateY());
			else if(tile2!=null)
				IIDataHandlingUtils.sendPacketAdjacently(newPacket, world, tile2.getPos(), mirrored?facing.rotateY(): facing.rotateYCCW());

		}
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		List<AxisAlignedBB> list = new ArrayList<>();

		list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

		return list;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_ARITHMETIC_LOGIC_MACHINE_STORAGE.ordinal();
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
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.accept(tag -> writeCustomNBT(tag, false))
			));
		}
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==0)
			isDoorOpened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
		{
			if(state!=isDoorOpened)
				world.playSound(null, getPos(), state?IISounds.metalLockerOpen: IISounds.metalLockerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			isDoorOpened = state;
		}

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
	}
}
