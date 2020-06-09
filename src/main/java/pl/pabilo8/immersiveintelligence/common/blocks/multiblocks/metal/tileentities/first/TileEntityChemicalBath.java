package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ChemicalBath;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Pabilo8 on 28-06-2019.
 */
public class TileEntityChemicalBath extends TileEntityMultiblockMetal<TileEntityChemicalBath, BathingRecipe> implements IGuiTile, ISoundTile
{
	public FluidTank[] tanks = {new FluidTank(ChemicalBath.fluidCapacity)};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public ItemStack effect = ItemStack.EMPTY;
	public boolean active = false;

	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 0, true, false);

	public TileEntityChemicalBath()
	{
		super(MultiblockChemicalBath.instance, new int[]{2, 3, 5}, ChemicalBath.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));

		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("processTime"))
			this.processTime = message.getInteger("processTime");
		if(message.hasKey("processTimeMax"))
			this.processTimeMax = message.getInteger("processTimeMax");
		if(message.hasKey("active"))
			this.active = message.getBoolean("active");
		if(message.hasKey("inventory"))
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 4);
		if(message.hasKey("output"))
			effect = new ItemStack(message.getCompoundTag("output"));
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void update()
	{
		super.update();
		if(isDummy())
			return;

		if(world.isRemote)
		{
			if(active&&processTime < processTimeMax)
				processTime += 1;
			return;
		}

		boolean wasActive = active;
		boolean update = false;

		active = shouldRenderAsActive();

		if(energyStorage.getEnergyStored() > 0&&processQueue.size() < this.getProcessQueueMaxLength())
		{
			if(tanks[0].getFluidAmount() > 0)
			{
				BathingRecipe recipe = BathingRecipe.findRecipe(inventory.get(0), tanks[0].getFluid());
				if(recipe!=null)
				{
					MultiblockProcessInMachine<BathingRecipe> process = new MultiblockProcessInMachine(recipe, 0);
					process.setInputTanks(0);
					this.addProcessToQueue(process, false);
					update = true;
					processTime = 0;
					processTimeMax = recipe.getTotalProcessTime();
					effect = recipe.itemOutput;
				}
			}
		}

		if(inventory.get(2).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)&&inventory.get(2).getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).getTankProperties()[0].getContents()!=null)
		{

			int amount_prev = tanks[0].getFluidAmount();
			ItemStack emptyContainer = Utils.drainFluidContainer(tanks[0], inventory.get(2), inventory.get(3), null);
			if(amount_prev!=tanks[0].getFluidAmount())
			{
				if(!inventory.get(3).isEmpty()&&OreDictionary.itemMatches(inventory.get(3), emptyContainer, true))
					inventory.get(3).grow(emptyContainer.getCount());
				else if(inventory.get(3).isEmpty())
					inventory.set(3, emptyContainer.copy());
				inventory.get(2).shrink(1);
				if(inventory.get(2).getCount() <= 0)
					inventory.set(2, ItemStack.EMPTY);

				update = true;
			}
		}

		if(world.getTotalWorldTime()%20==0)
		{
			BlockPos pos = getBlockPosForPos(4).offset(facing.getOpposite(), 1);
			ItemStack output = inventory.get(1);
			TileEntity inventoryTile = this.world.getTileEntity(pos);
			if(inventoryTile!=null)
				output = Utils.insertStackIntoInventory(inventoryTile, output, facing.getOpposite());
			inventory.set(1, output);
		}

		if(update||wasActive!=active)
		{
			this.markDirty();
			this.markContainingBlockForUpdate(null);
			NBTTagCompound tag = new NBTTagCompound();

			tag.setInteger("processTime", processTime);
			tag.setInteger("processTimeMax", processTimeMax);
			tag.setBoolean("active", this.active);
			tag.setTag("inventory", Utils.writeInventory(inventory));
			NBTTagCompound itemTag = new NBTTagCompound();
			effect.writeToNBT(itemTag);
			tag.setTag("output", itemTag);
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), new TargetPoint(this.world.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 32));
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
		return new int[]{25};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{29};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<BathingRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getBlockPosForPos(4).offset(facing.getOpposite(), 1);
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, facing.getOpposite());
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing);
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{
	}

	@Override
	public void onProcessFinish(MultiblockProcess<BathingRecipe> process)
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
	public float getMinProcessDistance(MultiblockProcess<BathingRecipe> process)
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
		return true;
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
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		TileEntityChemicalBath master = this.master();
		if(master!=null)
		{
			if((pos==14&&side==facing))
				return new FluidTank[]{master.tanks[0]};
		}
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		if((pos==14&&side==facing))
		{
			TileEntityChemicalBath master = this.master();
			if(master==null||master.tanks[iTank].getFluidAmount() >= master.tanks[iTank].getCapacity())
				return false;
			if(master.tanks[0].getFluid()==null)
			{
				List<BathingRecipe> incompleteRecipes = BathingRecipe.findIncompleteBathingRecipe(ItemStack.EMPTY, resource);
				return incompleteRecipes!=null&&!incompleteRecipes.isEmpty();
			}
			else
			{
				List<BathingRecipe> incompleteRecipes = BathingRecipe.findIncompleteBathingRecipe(ItemStack.EMPTY, master.tanks[0].getFluid());
				return incompleteRecipes!=null&&!incompleteRecipes.isEmpty();
			}
		}
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		return (pos==14&&side==facing);
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public BathingRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected BathingRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return BathingRecipe.loadFromNBT(tag);
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_CHEMICAL_BATH.ordinal();
	}

	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return false;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(pos==0&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&facing==this.facing.getOpposite())
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&pos==0)
		{
			TileEntityChemicalBath master = master();
			return (T)master.insertionHandler;
		}

		return super.getCapability(capability, facing);
	}
}
