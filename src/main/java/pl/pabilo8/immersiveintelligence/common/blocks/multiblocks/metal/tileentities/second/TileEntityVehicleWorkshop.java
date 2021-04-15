package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.second;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.network.MessageTileSync;
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
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.Emplacement;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;

/**
 * @author Pabilo8
 * @since 04.03.2021
 */
public class TileEntityVehicleWorkshop extends TileEntityMultiblockMetal<TileEntityVehicleWorkshop, MultiblockRecipe>
{
	public TileEntityVehicleWorkshop()
	{
		super(MultiblockVehicleWorkshop.instance, new int[]{4, 4, 5}, Emplacement.energyCapacity, true);
	}

	@Override
	public void validate()
	{
		super.validate();
	}

	@Override
	public void update()
	{
		super.update();

	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
	}

	@Override
	public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
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
		if(pos==20||pos==21||pos==25||pos==26||pos==30||pos==31||pos==35||pos==36)
			return new float[]{0, 0, 0, 1, 0.25f, 1};
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

	public void sendUpdate(int id)
	{
		NBTTagCompound tag = new NBTTagCompound();
		if(id==0)
		{

		}
		if(!tag.hasNoTags())
			ImmersiveEngineering.packetHandler.sendToAllAround(new MessageTileSync(this, tag), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 32));
	}

	@Override
	public void disassemble()
	{
		super.disassemble();
	}
}
