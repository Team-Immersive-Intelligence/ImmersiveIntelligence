package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockFlagpole;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityFlagpole extends TileEntityMultiblockMetal<TileEntityFlagpole, MultiblockRecipe> implements IPlayerInteraction
{
	private static final float[] BOUNDS_POLE = new float[]{0.375f, 0f, 0.375f, 0.625f, 1f, 0.625f};
	private static final float[] BOUNDS_FLAT = new float[]{0f, 0f, 0f, 1f, 0.125f, 1f};
	public ItemStack flag = ItemStack.EMPTY;
	Ticket ticket = null;

	public TileEntityFlagpole()
	{
		super(MultiblockFlagpole.INSTANCE, new int[]{8, 3, 3}, Emplacement.energyCapacity, true);
	}

	@Override
	public void validate()
	{
		super.validate();
		if(!isDummy())
		{
			if(!world.isRemote)
			{
				// TODO: 05.03.2021 make it work 

				Ticket ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getWorld(), Type.NORMAL);
				if(ticket!=null)
					ticket.getChunkList();
			}
		}
	}

	@Override
	public void update()
	{
		super.update();
		if(!isDummy())
		{
			if(ticket!=null)
			{
				ForgeChunkManager.forceChunk(ticket, this.world.getChunkFromBlockCoords(getPos()).getPos());
			}
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		if(nbt.hasKey("flag"))
			flag = new ItemStack(nbt.getCompoundTag("flag"));
		super.readCustomNBT(nbt, descPacket);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		nbt.setTag("flag", flag.serializeNBT());
		super.writeCustomNBT(nbt, descPacket);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		if(message.hasKey("flag"))
			flag = new ItemStack(message.getCompoundTag("flag"));
		super.receiveMessageFromServer(message);
	}

	@Override
	protected MultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[0];
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[0];
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return new IFluidTank[0];
	}

	@Override
	public MultiblockRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return null;
	}

	@Override
	public int[] getOutputSlots()
	{
		return new int[0];
	}

	@Override
	public int[] getOutputTanks()
	{
		return new int[0];
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<MultiblockRecipe> process)
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
	public void onProcessFinish(MultiblockProcess<MultiblockRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 0;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 0;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<MultiblockRecipe> process)
	{
		return 0;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return false;
	}

	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing enumFacing)
	{
		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack)
	{
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int i, EnumFacing enumFacing)
	{
		return false;
	}

	@Override
	public float[] getBlockBounds()
	{
		if(pos==40||pos==49||pos==58||pos==67)
			return BOUNDS_POLE;
		else if(offset[1]==0)
			return BOUNDS_FLAT;
		return new float[0];
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return null;
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 0;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{

	}

	public void sendFlagUpdate()
	{
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT().withItemStack("flag", flag)));
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityFlagpole master = master();
		if(
				(pos==40||pos==49||pos==58||pos==67)
						&&master!=null&&!world.isRemote)
		{
			if(master.flag.isEmpty()&&heldItem.getItem()==Items.BANNER)
			{
				master.flag = heldItem.copy();
				master.flag.setCount(1);
				heldItem.shrink(1);
				master.sendFlagUpdate();
				return true;
			}
			else if(!master.flag.isEmpty()&&Utils.isWirecutter(heldItem))
			{
				player.inventory.addItemStackToInventory(master.flag.copy());
				master.flag = ItemStack.EMPTY;
				master.sendFlagUpdate();
				return true;
			}
		}

		return false;
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
		if(!isDummy()&&!flag.isEmpty())
		{
			Utils.dropStackAtPos(world, getBlockPosForPos(67), flag.copy());
			flag = ItemStack.EMPTY;
			ForgeChunkManager.releaseTicket(ticket);
		}
	}
}
