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
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;

import java.util.ArrayList;
import java.util.List;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.radioStation;

/**
 * Created by Pabilo8 on 20-06-2019.
 */
public class TileEntityRadioStation extends TileEntityMultiblockMetal<TileEntityRadioStation, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IRadioDevice
{
	public NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
	public int frequency;

	public TileEntityRadioStation()
	{
		super(MultiblockRadioStation.instance, new int[]{8, 3, 3}, radioStation.energyCapacity, true);
	}

	@Override
	public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(!descPacket&&!isDummy())
		{
			inventory = blusunrize.immersiveengineering.common.util.Utils.readInventory(nbt.getTagList("inventory", 10), 0);
			frequency = 0;
			if(nbt.hasKey("frequency"))
				frequency = nbt.getInteger("frequency");

			ImmersiveIntelligence.logger.info(frequency);
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
				nbt.setTag("inventory", blusunrize.immersiveengineering.common.util.Utils.writeInventory(inventory));
			nbt.setInteger("frequency", frequency);
			RadioNetwork.INSTANCE.addDevice(this);
		}
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("frequency"))
		{
			frequency = message.getInteger("frequency");
		}
	}

	@Override
	public void receiveMessageFromServer(NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);
		if(message.hasKey("frequency"))
		{
			frequency = message.getInteger("frequency");
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
		return inventory;
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
	public void onRadioSend(DataPacket packet)
	{

	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		//Added because of getting double (and fake (with pos -1 and facing north) tile entities) when using world.getTileEntity
		if(this.pos!=-1&&!this.isDummy())
		{
			IDataConnector conn = Utils.findConnectorAround(this.getPos(), this.world);
			if(conn!=null)
			{
				conn.sendPacket(packet);
				ImmersiveIntelligence.logger.info(this.getPos()+" - Transmitted data to conns");
			}
			return true;
		}
		return false;
	}

	@Override
	public void onSend()
	{

	}

	@Override
	public void onReceive(DataPacket packet)
	{
		if(this.pos==9&&energyStorage.getEnergyStored() >= radioStation.energyUsage)
		{
			energyStorage.extractEnergy(radioStation.energyUsage, false);
			RadioNetwork.INSTANCE.sendPacket(packet, this, new ArrayList<>());
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
			this.frequency = value;
			markDirty();
			markContainingBlockForUpdate(null);
		}
		else
			master().setFrequency(value);
	}

	@Override
	public float getRange()
	{
		return radioStation.radioRange;
	}

	@Override
	public float getWeatherRangeDecrease()
	{
		return world.isRainingAt(getPos())?radioStation.weatherHarshness: 1f;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		//Should exist, but who knows ¯\_(ツ)_/¯
		if(getTileForPos(67)!=null)
			return new DimensionBlockPos(getTileForPos(67));
		else
			return new DimensionBlockPos(this);
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
		return getAdvancedSelectionBounds();
	}

}
