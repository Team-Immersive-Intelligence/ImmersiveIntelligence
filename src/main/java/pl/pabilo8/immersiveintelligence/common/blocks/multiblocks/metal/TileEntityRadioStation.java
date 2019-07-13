package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal;

import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.RadioStation;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class TileEntityRadioStation extends TileEntityMultiblockMetal<TileEntityRadioStation, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IRadioDevice
{
	int frequency = 0;
	int ticker = 0;

	public TileEntityRadioStation()
	{
		super(MultiblockRadioStation.instance, new int[]{8, 3, 3}, RadioStation.energyCapacity, false);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!isDummy()&&nbt.hasKey("Frequency"))
			frequency = nbt.getInteger("Frequency");
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
	}

	private boolean firstTime = true;

	@Override
	public void update()
	{
		super.update();
		if(firstTime)
		{
			firstTime = false;
			if(!isDummy())
			{
				RadioNetwork.INSTANCE.addDevice(new DimensionBlockPos(this));
			}
		}
	}

	@Override
	public float[] getBlockBounds()
	{
		return new float[]{0, 0, 0, 0, 0, 0};
	}


	@Override
	public int[] getEnergyPos()
	{
		return new int[]{7};
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
		return NonNullList.withSize(0, ItemStack.EMPTY);
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 0;
	}

	@Override
	public int[] getOutputSlots()
	{
		return null;
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
		this.markContainingBlockForUpdate(null);
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
	public void onRadioSend(DataPacket packet)
	{

	}

	@Override
	public void onRadioReceive(DataPacket packet)
	{
		//I don't know why, but when i get the facing of the tile, it always returns north
		//So i made a way around it (literally)

		IDataConnector conn = Utils.findConnectorAround(this.getPos(), this.world);
		if(conn!=null)
		{
			conn.sendPacket(packet);
		}
	}

	@Override
	public void onSend()
	{

	}

	@Override
	public void onReceive(DataPacket packet)
	{
		if(this.pos==9&&energyStorage.getEnergyStored() >= RadioStation.energyUsage)
		{
			energyStorage.extractEnergy(RadioStation.energyUsage, false);
			RadioNetwork.INSTANCE.sendPacket(packet, new DimensionBlockPos(master()), master().world, new ArrayList<>());
		}
	}

	@Override
	public boolean isBasicRadio()
	{
		return true;
	}

	@Override
	public int getFrequency()
	{
		if(!isDummy())
		{
			markDirty();
			return frequency;
		}
		else
			return master().getFrequency();
	}

	@Override
	public void setFrequency(int value)
	{
		if(!isDummy())
		{
			ImmersiveIntelligence.logger.info(this.getPos());
			this.frequency = value;
			markDirty();
		}
		else
			master().setFrequency(value);
	}

	@Override
	public float getRange()
	{
		return RadioStation.radioRange;
	}

	@Override
	public float getWeatherRangeDecrease()
	{
		return RadioStation.weatherHarshness;
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		List list = new ArrayList<AxisAlignedBB>();
		list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
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
		List list = new ArrayList<AxisAlignedBB>();
		return getAdvancedSelectionBounds();
	}
}
