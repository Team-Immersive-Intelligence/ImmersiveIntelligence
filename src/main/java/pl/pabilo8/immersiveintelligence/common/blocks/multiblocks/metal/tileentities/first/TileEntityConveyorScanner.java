package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ConveyorScanner;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityConveyorScanner extends TileEntityMultiblockMetal<TileEntityConveyorScanner, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IConveyorAttachable
{
	public NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);
	public int processTime, processTimeMax;
	public boolean active = false;
	IItemHandler inventoryHandler = new IEInventoryHandler(1, this, 0, true, true);

	public TileEntityConveyorScanner()
	{
		super(MultiblockConveyorScanner.instance, new int[]{3, 1, 1}, ConveyorScanner.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket&&!isDummy())
		{
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 1);
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
	}

	@Override
	public void update()
	{
		super.update();

		if(world.isRemote)
		{
			if(pos==1&&active&&processTime < processTimeMax)
				processTime += 1;
			return;
		}

		boolean wasActive = active;
		boolean update = false;

		if(pos==1&&energyStorage.getEnergyStored() > 0&&!inventory.get(0).isEmpty())
		{
			active = true;
			if(processTime >= 20)
			{

				energyStorage.extractEnergy(ConveyorScanner.energyUsage, false);
				IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(getBlockPosForPos(0), this.world, this.facing.rotateY());
				if(conn!=null)
				{
					DataPacket packet = new DataPacket();
					packet.setVariable('s', new DataTypeItemStack(inventoryHandler.extractItem(0, 64, true)));
					conn.sendPacket(packet);
				}
				Utils.dropStackAtPos(world, this.getPos().offset(facing), inventoryHandler.extractItem(0, 64, false));
				processTime = 0;
				processTimeMax = 20;
			}
			else if(processTime < 20)
			{
				processTime += 1;
				energyStorage.extractEnergy(ConveyorScanner.energyUsage, false);
			}
		}
		else
			active = false;

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
		return new int[]{2};
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
		/*if (this.pos==3 && energyStorage.getEnergyStored()>=dataInputMachine.energyUsage)
		{
			energyStorage.extractEnergy(dataInputMachine.energyUsage,false);
		}*/
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		if(pos==2)
		{
			list.add(new AxisAlignedBB(0.1875, -0.125, 0.1875, 0.8125, 0.375, 0.8125)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			if(facing.getAxis()==Axis.Z)
			{
				list.add(new AxisAlignedBB(0, 0.375, 0.1875, 1, 1, 0.8125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

				list.add(new AxisAlignedBB(0, -0.0625, 0.5-0.125, 0.125, 0.375, 0.5+0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(1f-0.125, -0.0625, 0.5-0.125, 1, 0.375, 0.5+0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else
			{
				list.add(new AxisAlignedBB(0.1875, 0.375, 0, 0.8125, 1, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

				list.add(new AxisAlignedBB(0.5-0.125, -0.0625, 0, 0.5+0.125, 0.375, 0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.5-0.125, -0.0625, 1f-0.125, 0.5+0.125, 0.375, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
		}
		else if(pos==1)
		{
			list.add(new AxisAlignedBB(0, 0, 0, 1, 0.125, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

			if(facing.getAxis()==Axis.Z)
			{
				list.add(new AxisAlignedBB(0, 0.125, 0, 0.0625, 0.25, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(1f-0.0625, 0.125, 0, 1, 0.25, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

				list.add(new AxisAlignedBB(0, 0.25, 0.5-0.125, 0.0625, 1, 0.5+0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(1f-0.0625, 0.25, 0.5-0.125, 1, 1, 0.5+0.125)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else
			{
				list.add(new AxisAlignedBB(0, 0.125, 0, 1, 0.25, 0.0625)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0, 0.125, 1f-0.0625, 1, 0.25, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));

				list.add(new AxisAlignedBB(0.5-0.125, 0.25, 0, 0.5+0.125, 1, 0.0625)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.5-0.125, 0.25, 1f-0.0625, 0.5+0.125, 1, 1)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
		}
		else
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1)
					.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
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
			TileEntityConveyorScanner master = master();
			if(master==null)
				return null;
			return (T)this.inventoryHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==1)
			return new EnumFacing[]{facing};
		return new EnumFacing[0];
	}

	@Override
	public void onEntityCollision(World world, Entity entity)
	{
		/*if(pos==1&&!world.isRemote&&entity!=null&&!entity.isDead&&entity instanceof EntityItem&&!((EntityItem)entity).getItem().isEmpty() &&pl.pabilo8.immersiveintelligence.api.Utils.getDistanceBetweenPos(entity.getPosition(),this.getPos().offset(facing.getOpposite()),false)==0f)
		{
			ItemStack stack = ((EntityItem)entity).getItem();
			if(stack.isEmpty())
				return;
			if(inventory.get(0).isEmpty() && inventoryHandler.insertItem(0, stack, false).isEmpty())
			{
				((EntityItem)entity).setItem(ItemStack.EMPTY);
				entity.setDead();
				processTime=0;
				processTimeMax=60;
			}

		}*/
	}

	@Override
	public void replaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w)
	{
		if(state.getBlock()==IEContent.blockConveyor)
		{
			state = state.withProperty(IEProperties.FACING_ALL, facing);
		}
		super.replaceStructureBlock(pos, state, stack, h, l, w);
	}
}
