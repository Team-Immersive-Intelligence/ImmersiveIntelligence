package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
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
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.mines.BlockIIRadioExplosives.ItemBlockRadioExplosives;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIPunchtape;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityDataInputMachine extends TileEntityMultiblockMetal<TileEntityDataInputMachine, IMultiblockRecipe> implements IDataDevice, IAdvancedBounds, IGuiTile, IBooleanAnimatedPartsBlock
{
	public static HashMap<Predicate<ItemStack>, BiFunction<TileEntityDataInputMachine, ItemStack, ItemStack>> dataOperations = new HashMap<>();

	static
	{
		dataOperations.put(stack -> Utils.compareToOreName(stack, "punchtapeEmpty"),
				(tile, stack) -> {
					ItemStack output = new ItemStack(IIContent.itemPunchtape, 1, 0);
					((ItemIIPunchtape)output.getItem()).writeDataToItem(tile.storedData, output);
					return output;
				});

		dataOperations.put(stack -> stack.getItem() instanceof ItemIIPunchtape,
				(tile, stack) -> {
					tile.storedData = ((ItemIIPunchtape)stack.getItem()).getStoredData(stack);
					return stack;
				});

		dataOperations.put(stack -> stack.getItem() instanceof ItemBlockRadioExplosives,
				(tile, stack) -> {
					ItemNBTHelper.setTagCompound(stack, "programmed_data", tile.storedData.clone().toNBT());
					return stack;
				});

		// TODO: 20.08.2021 radio explosives
	}

	public boolean toggle = false;
	public float productionProgress = 0f;
	public DataPacket storedData = new DataPacket();
	public boolean isDrawerOpened = false, isDoorOpened = false;
	public float drawerAngle = 0, doorAngle = 0;
	public NonNullList<ItemStack> inventory = NonNullList.withSize(26, ItemStack.EMPTY);

	public TileEntityDataInputMachine()
	{
		super(MultiblockDataInputMachine.INSTANCE, new int[]{3, 2, 2}, DataInputMachine.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy())
		{
			if(!descPacket)
				inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 26);

			storedData = new DataPacket().fromNBT(nbt.getCompoundTag("variables"));
			productionProgress = nbt.getFloat("production_progress");
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
				nbt.setTag("inventory", Utils.writeInventory(inventory));
			nbt.setTag("variables", storedData.toNBT());
			nbt.setFloat("production_progress", productionProgress);
		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("variables"))
			storedData.fromNBT(message.getCompoundTag("variables"));
		if(message.hasKey("send_packet"))
			this.sendPacket();
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("variables"))
			storedData.fromNBT(message.getCompoundTag("variables"));
		if(message.hasKey("production_progress"))
			productionProgress = message.getFloat("production_progress");
	}

	@Override
	public void update()
	{
		super.update();

		if(world.isRemote&&!isDummy())
		{
			drawerAngle = MathHelper.clamp(drawerAngle+(isDrawerOpened?0.4f: -0.5f), 0f, 5f);
			doorAngle = MathHelper.clamp(doorAngle+(isDoorOpened?3f: -5f), 0f, 135f);

			if(this.productionProgress > 0&&energyStorage.getEnergyStored() > DataInputMachine.energyUsagePunchtape&&productionProgress < DataInputMachine.timePunchtapeProduction)
				this.productionProgress += 1;
		}

		if(!world.isRemote&&!isDummy())
		{
			if(toggle^world.isBlockPowered(getBlockPosForPos(getRedstonePos()[0])))
			{
				toggle = !toggle;

				//Finally!
				if(toggle)
					this.sendPacket();
			}

			if(dataOperations.keySet().stream().anyMatch(itemStackPredicate -> itemStackPredicate.test(inventoryHandler.getStackInSlot(0)))
					&&energyStorage.getEnergyStored() >= DataInputMachine.energyUsagePunchtape)
				if(productionProgress >= DataInputMachine.timePunchtapeProduction)
				{
					productionProgress = 0f;
					Optional<Predicate<ItemStack>> first = dataOperations.keySet().stream().filter(itemStackPredicate -> itemStackPredicate.test(inventoryHandler.getStackInSlot(0))).findFirst();
					if(first.isPresent())
					{
						ItemStack input = inventoryHandler.extractItem(0, 1, true);
						ItemStack written = dataOperations.get(first.get()).apply(this, input);
						if(inventoryHandler.insertItem(1, written, true).isEmpty())
						{
							inventoryHandler.extractItem(0, 1, false);
							inventoryHandler.insertItem(1, written, false);

							IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
									.withTag("inventory", Utils.writeInventory(inventory))
									.withTag("variables", storedData.toNBT())
							));
						}
					}
				}
				else
				{
					energyStorage.extractEnergy(DataInputMachine.energyUsagePunchtape, false);
					productionProgress += 1;
				}
			if(productionProgress%4==0)
				IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
						.withFloat("production_progress", productionProgress)));

		}
	}

	public void sendPacket()
	{
		if(energyStorage.getEnergyStored() >= DataInputMachine.energyUsage)
		{
			energyStorage.extractEnergy(DataInputMachine.energyUsage, false);
			IIDataHandlingUtils.sendPacketAdjacently(storedData, world, getBlockPosForPos(3),
					this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY()
			);
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{10};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{2};
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
		return dataOperations.keySet().stream().anyMatch(p -> p.test(stack));
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 64;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[]{1};
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
		//this.markContainingBlockForUpdate(null);
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

	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();

		if(pos==0||pos==1)
		{
			list.add(new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			switch(mirrored^pos==1?facing.getOpposite(): facing)
			{
				case NORTH:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case SOUTH:
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 0.9375, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 0.9375, 0.8125, 1-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case EAST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 0.0625+0.1875, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.0625, 1-0.0625, 0.8125, 0.0625+0.1875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
				case WEST:
					list.add(new AxisAlignedBB(0.0625, 0, 0.75, 0.0625+0.1875, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					list.add(new AxisAlignedBB(0.75, 0, 0.75, 1-0.0625, 0.8125, 0.9375).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
					break;
			}
		}
		else if(pos==4)
			list.add(new AxisAlignedBB(0.0625, 0, 0.0625, 1-0.0625, 0.25, 1-0.0625)
					//.shrink()
					.offset(new Vec3d((mirrored?facing.rotateY(): facing.rotateYCCW()).getDirectionVec()).scale(0.0625f))
					.expand(facing.getFrontOffsetX()*0.0625, 0, facing.getFrontOffsetZ()*0.0625)
					.contract(facing.rotateYCCW().getFrontOffsetX()*0.0625, 0, facing.rotateYCCW().getFrontOffsetZ()*0.0625)
					.contract(facing.rotateY().getFrontOffsetX()*0.0625, 0, facing.rotateY().getFrontOffsetZ()*0.0625)
					//.offset(new Vec3d(facing.getDirectionVec()).scale(0.0625f))
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else if(pos==5)
			list.add(new AxisAlignedBB(0.25, 0, 0.25, 1-0.25, 0.25, 1-0.25)
					.offset(new Vec3d((mirrored?facing.rotateYCCW(): facing.rotateY()).getDirectionVec()).scale(0.125f))
					.expand(facing.getFrontOffsetX()*-0.125, 0, facing.getFrontOffsetZ()*-0.125)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else
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
		return IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE.ordinal();
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

	IItemHandler inventoryHandler = new IEInventoryHandler(26, this, 0, true, true);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityDataInputMachine master = master();
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
			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withTag("variables", storedData.toNBT())));
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		if(part==0)
			isDrawerOpened = state;
		else if(part==1)
			isDoorOpened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
		{
			if(state!=isDrawerOpened)
				world.playSound(null, getPos(), state?IISounds.drawerOpen: IISounds.drawerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			isDrawerOpened = state;
		}
		else if(part==1)
		{
			if(state!=isDoorOpened)
				world.playSound(null, getPos(), state?IISounds.metalLockerOpen: IISounds.metalLockerClose, SoundCategory.BLOCKS, 0.25F, 1);
			isDoorOpened = state;
		}

		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDrawerOpened, 0, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));

	}
}
