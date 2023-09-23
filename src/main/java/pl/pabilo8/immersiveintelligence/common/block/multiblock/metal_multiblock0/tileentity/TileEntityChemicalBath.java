package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler;
import blusunrize.immersiveengineering.api.tool.ChemthrowerHandler.ChemthrowerEffect;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ChemicalBath;
import pl.pabilo8.immersiveintelligence.api.crafting.BathingRecipe;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockChemicalBath;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
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
public class TileEntityChemicalBath extends TileEntityMultiblockMetal<TileEntityChemicalBath, BathingRecipe> implements IGuiTile, ISoundTile, IPlayerInteraction, IAdvancedBounds
{
	public FluidTank[] tanks = {new FluidTank(ChemicalBath.fluidCapacity)};
	public NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public ItemStack effect = ItemStack.EMPTY;
	public boolean active = false;

	IItemHandler insertionHandler = new IEInventoryHandler(1, this, 0, true, false);

	public TileEntityChemicalBath()
	{
		super(MultiblockChemicalBath.INSTANCE, new int[]{2, 3, 5}, ChemicalBath.energyCapacity, true);
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
		if(message.hasKey("tank"))
			tanks[0].readFromNBT(message.getCompoundTag("tank"));
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

		if(world.getTotalWorldTime()%4==0&&tanks[0].getFluid()!=null)
		{
			final ChemthrowerEffect effect = ChemthrowerHandler.getEffect(tanks[0].getFluid().getFluid());
			if(effect!=null)
			{
				AxisAlignedBB aabb = new AxisAlignedBB(getBlockPosForPos(7)).grow(1, 0, 1).contract(0, 1-(tanks[0].getFluidAmount()/(float)tanks[0].getCapacity()), 0);
				world.getEntitiesWithinAABB(EntityLivingBase.class, aabb).forEach(entityLivingBase -> effect.applyToEntity(entityLivingBase, null, ItemStack.EMPTY, tanks[0].getFluid()));
			}
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
					MultiblockProcessInMachine<BathingRecipe> process = new MultiblockProcessInMachine<>(recipe, 0);
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

			IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
					.withInt("processTime", processTime)
					.withInt("processTimeMax", processTimeMax)
					.withBoolean("active", this.active)
					.withTag("inventory", Utils.writeInventory(inventory))
					.withItemStack("output", effect)
					.withTag("tank", EasyNBT.newNBT().accept(tanks[0]::writeToNBT))
			));
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		if(pos > 20&&pos < 24)
		{
			switch(facing)
			{
				case NORTH:
					return new float[]{0, 0.625f, 0.0625f, 1, 0.875f, 0.625f};
				case SOUTH:
					return new float[]{0, 0.625f, 0.375f, 1, 0.875f, 0.9375f};
				case EAST:
					return new float[]{0.375f, 0.625f, 0, 0.9375f, 0.875f, 1};
				case WEST:
					return new float[]{0.0625f, 0.625f, 0, 0.625f, 0.875f, 1};
			}
			//return new float[]{0, 0.875f-0.25f, 0, 1, 0.875f, 1};
		}
		else if(pos==20||pos==24)
		{
			switch(facing)
			{
				case NORTH:
					return new float[]{0, 0.875f-0.25f, 0, 1, 0.875f, 1-0.25f};
				case SOUTH:
					return new float[]{0, 0.875f-0.25f, 0.25f, 1, 0.875f, 1};
				case EAST:
					return new float[]{0.25f, 0.875f-0.25f, 0, 1, 0.875f, 1};
				case WEST:
					return new float[]{0, 0.875f-0.25f, 0, 1-0.25f, 0.875f, 1};
			}
			//return new float[]{0, 0.875f-0.25f, 0, 1, 0.875f, 1};
		}
		if(pos==7||pos==2)
			return new float[]{0, 0, 0, 1, 0.3125f, 1};
		if(pos==5||pos==9)
			return new float[]{0, 0, 0, 1, 0.5f, 1};
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

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityChemicalBath master = master();
		if(offset[1]==-1&&offset[2] >= -1&&offset[2] <= 1&&master!=null&&!world.isRemote)
		{
			if(heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
			{
				if(FluidUtil.interactWithFluidHandler(player, hand, master.tanks[0]))
				{
					IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT()
							.withTag("tank", tanks[0].writeToNBT(new NBTTagCompound()))
					));
				}
			}
		}

		return false;
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		if(offset[1]==-1&&(facing.getAxis()==Axis.X?(offset[2]==0||offset[2]==-1||offset[2]==1): (offset[0]==0||offset[0]==-1||offset[0]==1)))
		{
			ArrayList<AxisAlignedBB> aabb = new ArrayList<>();
			aabb.add(new AxisAlignedBB(0, 0, 0, 1, 0.3125f, 1).offset(getPos()));
			if((pos > 0&&pos < 4)||(pos > 10&&pos < 14))
				switch(pos > 10?this.facing.getOpposite(): this.facing)
				{
					case NORTH:
						aabb.add(new AxisAlignedBB(0, 0.75, 1-0.0625, 1, 1.25, 1).offset(getPos()));
						aabb.add(new AxisAlignedBB(0, 0, 1-0.375, 1, 0.75, 1-0.0625).offset(getPos()));
						break;
					case SOUTH:
						aabb.add(new AxisAlignedBB(0, 0.75, 0, 1, 1.25, 0.0625).offset(getPos()));
						aabb.add(new AxisAlignedBB(0, 0, 0.0625, 1, 0.75, 0.375).offset(getPos()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0, 0.75, 0, 0.0625, 1.25, 1).offset(getPos()));
						aabb.add(new AxisAlignedBB(0.0625, 0, 0, 0.375, 0.75, 1).offset(getPos()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(1-0.0625, 0.75, 0, 1, 1.25, 1).offset(getPos()));
						aabb.add(new AxisAlignedBB(1-0.375, 0, 0, 1-0.0625, 0.75, 1).offset(getPos()));
						break;
				}
			boolean pb = pos==3||pos==8||pos==13;
			if(pb||(pos==1||pos==6||pos==11))
			{
				switch(pb^mirrored?this.facing: this.facing.getOpposite())
				{
					case NORTH:
						aabb.add(new AxisAlignedBB(1-0.0625, 0.3125f, 0.0625, 1, 1.25, 1).offset(getPos()));
						break;
					case SOUTH:
						aabb.add(new AxisAlignedBB(0, 0.3125f, 0.0625, 0.0625, 1.25, 1).offset(getPos()));
						break;
					case EAST:
						aabb.add(new AxisAlignedBB(0.0625, 0.3125f, 1-0.0625, 1, 1.25, 1).offset(getPos()));
						break;
					case WEST:
						aabb.add(new AxisAlignedBB(0.0625, 0.3125f, 0, 1, 1.25, 0.0625).offset(getPos()));
						break;
				}
			}
			return aabb;
		}
		return new ArrayList<>();
	}
}
