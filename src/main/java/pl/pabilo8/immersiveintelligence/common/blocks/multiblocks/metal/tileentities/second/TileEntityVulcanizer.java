package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.api.crafting.VulcanizerRecipe;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityVulcanizer extends TileEntityMultiblockMetal<TileEntityVulcanizer, VulcanizerRecipe> implements IPlayerInteraction
{
	public ItemStack mold = ItemStack.EMPTY;
	public NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

	public TileEntityVulcanizer()
	{
		super(MultiblockVulcanizer.instance, new int[]{4, 4, 6}, Emplacement.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();
		if(isDummy()||isRSDisabled()||world.isRemote)
			return;
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		mold = new ItemStack(nbt.getCompoundTag("mold"));
		if(!descPacket)
			inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 4);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(!this.mold.isEmpty())
			nbt.setTag("mold", this.mold.writeToNBT(new NBTTagCompound()));
		if(!descPacket)
			nbt.setTag("inventory", Utils.writeInventory(inventory));
	}

	@Override
	public boolean interact(EnumFacing side, EntityPlayer player, EnumHand hand, ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntityVulcanizer master = master();
		if(master!=null)
			if(player.isSneaking()&&!master.mold.isEmpty())
			{
				if(heldItem.isEmpty())
					player.setHeldItem(hand, master.mold.copy());
				else if(!world.isRemote)
					player.entityDropItem(master.mold.copy(), 0);
				master.mold = ItemStack.EMPTY;
				this.updateMasterBlock(null, true);
				return true;
			}
			else if(VulcanizerRecipe.isValidMold(heldItem))
			{
				ItemStack tempMold = !master.mold.isEmpty()?master.mold.copy(): ItemStack.EMPTY;
				master.mold = Utils.copyStackWithAmount(heldItem, 1);
				heldItem.shrink(1);
				if(heldItem.getCount() <= 0)
					heldItem = ItemStack.EMPTY;
				else
					player.setHeldItem(hand, heldItem);
				if(!tempMold.isEmpty())
					if(heldItem.isEmpty())
						player.setHeldItem(hand, tempMold);
					else if(!world.isRemote)
						player.entityDropItem(tempMold, 0);
				this.updateMasterBlock(null, true);
				return true;
			}
		return false;
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
	}

	@Override
	protected VulcanizerRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return VulcanizerRecipe.loadFromNBT(tag);
	}

	@Override
	public int[] getEnergyPos()
	{
		return new int[]{21};
	}

	@Override
	public int[] getRedstonePos()
	{
		return new int[0];
	}

	@Override
	public IFluidTank[] getInternalTanks()
	{
		return null;
	}

	@Override
	public VulcanizerRecipe findRecipeForInsertion(ItemStack inserting)
	{
		return VulcanizerRecipe.findRecipe(mold, inserting);
	}

	@Override
	public int[] getOutputSlots()
	{
		return null;
	}

	@Override
	public int[] getOutputTanks()
	{
		return null;
	}

	@Override
	public boolean additionalCanProcessCheck(MultiblockProcess<VulcanizerRecipe> process)
	{
		return true;
	}

	@Override
	public void doProcessOutput(ItemStack output)
	{
		BlockPos pos = getBlockPosForPos(11).offset(getOutFacing());
		TileEntity inventoryTile = this.world.getTileEntity(pos);
		if(inventoryTile!=null)
			output = Utils.insertStackIntoInventory(inventoryTile, output, getOutFacing().getOpposite());
		if(!output.isEmpty())
			Utils.dropStackAtPos(world, pos, output, facing);
	}

	@Override
	public void doProcessFluidOutput(FluidStack output)
	{

	}

	@Override
	public void onProcessFinish(MultiblockProcess<VulcanizerRecipe> process)
	{

	}

	@Override
	public int getMaxProcessPerTick()
	{
		return 3;
	}

	@Override
	public int getProcessQueueMaxLength()
	{
		return 3;
	}

	@Override
	public float getMinProcessDistance(MultiblockProcess<VulcanizerRecipe> process)
	{
		return 63.75f/120f;
	}

	@Override
	public boolean isInWorldProcessingMachine()
	{
		return true;
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
		return new float[]{0, 0, 0, 1, 1, 1};
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return ApiUtils.compareToOreName(itemStack,i==0?"rubberRaw":"dustSulfur");
	}

	@Override
	public int getSlotLimit(int i)
	{
		return 64;
	}

	@Override
	public void doGraphicalUpdates(int i)
	{

	}

	@Override
	public void replaceStructureBlock(BlockPos pos, IBlockState state, ItemStack stack, int h, int l, int w)
	{
		super.replaceStructureBlock(pos, state, stack, h, l, w);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityConveyorBelt)
			((TileEntityConveyorBelt)tile).setFacing(getOutFacing());
	}

	private EnumFacing getOutFacing()
	{
		return this.mirrored?this.facing.rotateYCCW(): this.facing.rotateY();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityVulcanizer master = master();
			if(master==null)
				return false;
			return pos==9&&facing==getOutFacing().getOpposite();
		}
		return super.hasCapability(capability, facing);
	}

	IItemHandler insertionHandler = new MultiblockInventoryHandler_DirectProcessing(this);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			TileEntityVulcanizer master = master();
			if(master==null)
				return null;
			if(pos==9&&facing==getOutFacing().getOpposite())
				return (T)master.insertionHandler;
			return null;
		}
		return super.getCapability(capability, facing);
	}
}
