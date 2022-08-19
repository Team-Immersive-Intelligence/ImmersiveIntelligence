package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Electrolyzer;
import pl.pabilo8.immersiveintelligence.api.crafting.ElectrolyzerRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;

import static pl.pabilo8.immersiveintelligence.api.Utils.handleBucketTankInteraction;
import static pl.pabilo8.immersiveintelligence.api.Utils.outputFluidToTank;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityElectrolyzer extends TileEntityMultiblockMetal<TileEntityElectrolyzer, ElectrolyzerRecipe> implements IGuiTile, ISoundTile
{
	public FluidTank[] tanks = {new FluidTank(Electrolyzer.fluidCapacity), new FluidTank(Electrolyzer.fluidCapacity), new FluidTank(Electrolyzer.fluidCapacity)};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public boolean active = false;


	public TileEntityElectrolyzer()
	{
		super(MultiblockElectrolyzer.instance, new int[]{2, 3, 3}, Electrolyzer.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		tanks[0].readFromNBT(nbt.getCompoundTag("tank0"));
		tanks[1].readFromNBT(nbt.getCompoundTag("tank1"));
		tanks[2].readFromNBT(nbt.getCompoundTag("tank2"));
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 6);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		nbt.setTag("tank0", tanks[0].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank1", tanks[1].writeToNBT(new NBTTagCompound()));
		nbt.setTag("tank2", tanks[2].writeToNBT(new NBTTagCompound()));

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
			inventory = Utils.readInventory(message.getTagList("inventory", 10), 6);
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
				ElectrolyzerRecipe recipe = ElectrolyzerRecipe.findRecipe(tanks[0].getFluid());
				if(recipe!=null)
				{
					MultiblockProcessInMachine<ElectrolyzerRecipe> process = new MultiblockProcessInMachine<>(recipe);
					process.setInputTanks(0);
					processTime = 0;
					processTimeMax = recipe.getTotalProcessTime();
					if(this.addProcessToQueue(process, true))
					{
						this.addProcessToQueue(process, false);
						update = true;
					}
				}
			}
		}

		if(outputFluidToTank(tanks[1], 80, getBlockPosForPos(0), this.world, this.facing.getOpposite()))
			update = true;
		if(outputFluidToTank(tanks[2], 80, getBlockPosForPos(2), this.world, this.facing.getOpposite()))
			update = true;

		if(world.getTotalWorldTime()%8==0)
		{
			if(handleBucketTankInteraction(tanks, inventory, 0, 1, 0,false))
				update = true;
			//No, it's not a mistake - you can only put the fluid in into the first tank and get the output from the next
			if(handleBucketTankInteraction(tanks, inventory, 2, 4, 1,true))
				update = true;
			if(handleBucketTankInteraction(tanks, inventory, 3, 5, 2,true))
				update = true;
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
		return new int[]{8};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[]{8};
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<ElectrolyzerRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getPos().offset(facing, 2);
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
	public void onProcessFinish(MultiblockProcess<ElectrolyzerRecipe> process)
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
	public float getMinProcessDistance(MultiblockProcess<ElectrolyzerRecipe> process)
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
		return new int[]{};
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[]{1, 2};
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		TileEntityElectrolyzer master = this.master();
		if(master!=null)
		{
			if((pos==6&&side==facing.getOpposite().rotateY()))
				return new FluidTank[]{master.tanks[0]};
			if((pos==0&&side==facing.getOpposite()))
				return new FluidTank[]{master.tanks[1]};
			if((pos==2&&side==facing.getOpposite()))
				return new FluidTank[]{master.tanks[2]};
		}
		return new FluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		if(side==null)
			return true;
		if((pos==6&&side==facing.getOpposite().rotateY()))
		{
			TileEntityElectrolyzer master = this.master();
			if(master==null||master.tanks[iTank].getFluidAmount() >= master.tanks[iTank].getCapacity())
				return false;
			ElectrolyzerRecipe incompleteRecipes;
			if(master.tanks[0].getFluid()==null)
				incompleteRecipes = ElectrolyzerRecipe.findIncompleteRecipe(resource);
			else
				incompleteRecipes = ElectrolyzerRecipe.findIncompleteRecipe(master.tanks[0].getFluid());
			return incompleteRecipes!=null;
		}
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		if(pos==0&&side==facing.getOpposite()&&iTank==1)
			return true;
		else return pos==2&&side==facing.getOpposite()&&iTank==2;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		this.markDirty();
		this.markContainingBlockForUpdate(null);
	}

	@Override
	public ElectrolyzerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	protected ElectrolyzerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return ElectrolyzerRecipe.loadFromNBT(tag);
	}

	@Override
	public boolean canOpenGui()
	{
		return formed;
	}

	@Override
	public int getGuiID()
	{
		return IIGuiList.GUI_ELECTROLYZER.ordinal();
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
}
