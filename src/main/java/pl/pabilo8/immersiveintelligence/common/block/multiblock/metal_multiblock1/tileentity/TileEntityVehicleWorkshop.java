package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.tileentity;

import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.VehicleWorkshop;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock1.multiblock.MultiblockVehicleWorkshop;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityVehicleWorkshop extends TileEntityMultiblockMetal<TileEntityVehicleWorkshop, MultiblockRecipe>
{
	public NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
	public FluidTank[] tanks = new FluidTank[]{
			new FluidTank(VehicleWorkshop.dieselCapacity)
	};
	public int progress=0, maxProgress=0;

	public TileEntityVehicleWorkshop()
	{
		super(MultiblockVehicleWorkshop.INSTANCE, new int[]{4, 4, 5}, VehicleWorkshop.energyCapacity, true);
	}

	@Override
	public void update()
	{
		super.update();

		// TODO: 19.10.2021 main loop
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		nbt.setTag("inventory", Utils.writeInventory(inventory));
		tanks[0] = tanks[0].readFromNBT(nbt.getCompoundTag("tank"));
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		inventory = Utils.readInventory(nbt.getTagList("inventory", 10), inventory.size());
		nbt.setTag("tank", tanks[0].writeToNBT(new NBTTagCompound()));
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		// TODO: 19.10.2021 Prints le Bleu
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		// TODO: 19.10.2021 update
	}

	@Override
	protected MultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
	{
		return null;
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
	public IFluidTank[] getInternalTanks()
	{
		return tanks;
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

	// TODO: 19.10.2021 tank
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
		if(pos==20||pos==21||pos==25||pos==26||pos==30||pos==31||pos==35||pos==36)
			return new float[]{0, 0, 0, 1, 0.25f, 1};
		return new float[0];
	}

	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean isStackValid(int i, ItemStack itemStack)
	{
		return true;
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
	public void disassemble()
	{
		super.disassemble();
	}
}
