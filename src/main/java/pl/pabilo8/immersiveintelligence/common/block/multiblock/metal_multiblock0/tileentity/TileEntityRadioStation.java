package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.ImmersiveEngineering;
import blusunrize.immersiveengineering.api.DimensionBlockPos;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.ISoundTile;
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
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.IRadioDevice;
import pl.pabilo8.immersiveintelligence.api.data.radio.RadioNetwork;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.RadioStation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockRadioStation;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageIITileSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedMultiblockTileEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8
 * @since 20-06-2019
 */
public class TileEntityRadioStation extends TileEntityMultiblockMetal<TileEntityRadioStation, IMultiblockRecipe> implements IDataDevice, IAdvancedCollisionBounds, IAdvancedSelectionBounds, IRadioDevice, IAdvancedMultiblockTileEntity, ISoundTile
{
	public static int PART_AMOUNT = 1;

	public NonNullList<ItemStack> inventory = NonNullList.withSize(0, ItemStack.EMPTY);
	public int frequency, construction = 0, clientConstruction = 0;
	private int soundDelay = 0;
	private boolean sountIn = false;

	public TileEntityRadioStation()
	{
		super(MultiblockRadioStation.INSTANCE, new int[]{8, 3, 3}, RadioStation.energyCapacity, true);
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
			getConstructionNBT(nbt);
			clientConstruction = construction;
		}
	}

	@Override
	public void update()
	{
		super.update();
		if(!isDummy()&&world.isRemote)
		{
			if(getTileForPos(9)!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.radio_noise, getTileForPos(9), this.soundDelay > 0, 0.125f/4f, 1);
			if(getTileForPos(0)!=null)
				ImmersiveEngineering.proxy.handleTileSound(IISounds.radio_beep, getTileForPos(0), this.soundDelay > 0, 0.5f/4f, sountIn?1f: 0.5f);

			if(soundDelay > 0)
				soundDelay--;

			float maxConstruction = IIUtils.getMaxClientProgress(construction, getConstructionCost(), PART_AMOUNT);
			if(clientConstruction < maxConstruction)
				clientConstruction = (int)Math.min(clientConstruction+(Tools.electricHammerEnergyPerUseConstruction/4.25f), maxConstruction);
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
			setConstructionNBT(nbt);
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
		else if(message.hasKey("beep"))
		{
			this.sountIn = message.getBoolean("beep");
			this.soundDelay = sountIn?20: 25;
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
		IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT().withBoolean("beep", false)));
	}

	@Override
	public boolean onRadioReceive(DataPacket packet)
	{
		//Added because of getting double (and fake (with pos -1 and facing north) tile entities) when using world.getTileEntity
		if(this.formed&&!this.isDummy()&&isConstructionFinished())
		{
			if(IIDataHandlingUtils.sendPacketAdjacently(packet, this.world, this.getPos(), facing.getOpposite()))
				IIPacketHandler.sendToClient(this, new MessageIITileSync(this, EasyNBT.newNBT().withBoolean("beep", true)));
			return true;
		}
		return false;
	}

	@Override
	public void onReceive(DataPacket packet, EnumFacing side)
	{
		if(this.pos==9&&energyStorage.getEnergyStored() >= RadioStation.energyUsage)
		{
			energyStorage.extractEnergy(RadioStation.energyUsage, false);
			RadioNetwork.INSTANCE.sendPacket(packet, this, new ArrayList<>());
		}
	}

	@Override
	public boolean isBasicRadio()
	{
		return false;
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
		float factor = world.isRainingAt(getPos())?(float)RadioStation.weatherHarshness: 1f;
		return RadioStation.radioRange*factor;
	}

	@Override
	public DimensionBlockPos getDevicePosition()
	{
		//Should exist, but who knows
		if(getTileForPos(67)!=null)
			return new DimensionBlockPos(getTileForPos(67));
		else
			return new DimensionBlockPos(this);
	}

	@Override
	public List<AxisAlignedBB> getAdvancedSelectionBounds()
	{
		ArrayList<AxisAlignedBB> list = new ArrayList<>();

		if(pos==0||pos==9||pos==7)
			list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		else
		{
			EnumFacing ff = mirrored?facing.rotateY(): facing;
			double off = pos < 20?-0.125: (pos < 35?0.125: (pos < 45?0.375: 0.5));
			if(offset[1]==-1)
			{
				list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				if(pos==2)
					list.add(new AxisAlignedBB(0.3125, 0.5, 0.3125, 0.6875, 1, 0.6875)
							.offset(ff.rotateY().getFrontOffsetX()*0.1875f, 0, ff.rotateY().getFrontOffsetZ()*0.1875f)
							.offset(ff.getOpposite().getFrontOffsetX()*0.1875f, 0, ff.getOpposite().getFrontOffsetZ()*0.1875f)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				else if(pos==6)
					list.add(new AxisAlignedBB(0.3125, 0.5, 0.3125, 0.6875, 1, 0.6875)
							.offset(ff.getOpposite().rotateY().getFrontOffsetX()*0.1875f, 0, ff.getOpposite().rotateY().getFrontOffsetZ()*0.1875f)
							.offset(ff.getFrontOffsetX()*0.1875f, 0, ff.getFrontOffsetZ()*0.1875f)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				else if(pos==8)
					list.add(new AxisAlignedBB(0.3125, 0.5, 0.3125, 0.6875, 1, 0.6875)
							.offset(ff.rotateY().getFrontOffsetX()*0.1875f, 0, ff.rotateY().getFrontOffsetZ()*0.1875f)
							.offset(ff.getFrontOffsetX()*0.1875f, 0, ff.getFrontOffsetZ()*0.1875f)
							.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==67)
			{
				list.add(new AxisAlignedBB(0.25, 0.5+0.0625, 0.25, 0.75, 1.0625, 0.75).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.5+0.0625, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==58)
			{
				list.add(new AxisAlignedBB(0, 0, 0, 0.425, 1, 0.425).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.575, 0, 0, 1, 1, 0.425).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0, 0, 0.575, 0.425, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				list.add(new AxisAlignedBB(0.575, 0, 0.575, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==26||pos==24||pos==18||pos==20)
			{
				list.add(new AxisAlignedBB(0.3125, 0, 0.3125, 0.6875, 1, 0.6875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				if(pos==18)
					list.add(new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==45||pos==36||pos==27)
			{
				list.add(new AxisAlignedBB(0.5-0.2125, 0, 0.5-0.2125, 0.5+0.2125, 1, 0.5+0.2125)
						.offset(ff.rotateY().getFrontOffsetX()*off, 0, ff.rotateY().getFrontOffsetZ()*off)
						.offset(ff.getFrontOffsetX()*off, 0, ff.getFrontOffsetZ()*off)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==47||pos==38||pos==29||pos==11)
			{
				list.add(new AxisAlignedBB(0.5-0.2125, 0, 0.5-0.2125, 0.5+0.2125, 1, 0.5+0.2125)
						.offset(ff.rotateYCCW().getFrontOffsetX()*off, 0, ff.rotateYCCW().getFrontOffsetZ()*off)
						.offset(ff.getFrontOffsetX()*off, 0, ff.getFrontOffsetZ()*off)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==51||pos==42||pos==33||pos==15)
			{
				list.add(new AxisAlignedBB(0.5-0.2125, 0, 0.5-0.2125, 0.5+0.2125, 1, 0.5+0.2125)
						.offset(ff.rotateY().getFrontOffsetX()*off, 0, ff.rotateY().getFrontOffsetZ()*off)
						.offset(ff.getOpposite().getFrontOffsetX()*off, 0, ff.getOpposite().getFrontOffsetZ()*off)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==53||pos==44||pos==35||pos==17)
			{
				list.add(new AxisAlignedBB(0.5-0.2125, 0, 0.5-0.2125, 0.5+0.2125, 1, 0.5+0.2125)
						.offset(ff.rotateYCCW().getFrontOffsetX()*off, 0, ff.rotateYCCW().getFrontOffsetZ()*off)
						.offset(ff.getOpposite().getFrontOffsetX()*off, 0, ff.getOpposite().getFrontOffsetZ()*off)
						.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
			}
			else if(pos==28||pos==34)
			{
				EnumFacing g = pos==34?facing.getOpposite(): facing;
				list.add(new AxisAlignedBB(0, -0.1875, 0, 0, 0, 0)
						.grow(facing.rotateY().getFrontOffsetX(), 0, facing.rotateY().getFrontOffsetZ())
						.grow(facing.getFrontOffsetX()*0.125, 0, facing.getFrontOffsetZ()*0.125)
						.offset(g.getFrontOffsetX()*0.1875, 0, g.getFrontOffsetZ()*0.1875)
						.offset(getPos().getX()+0.5, getPos().getY(), getPos().getZ()+0.5));
			}
			else if(pos==30||pos==32)
			{
				EnumFacing g = (pos==32?facing.getOpposite(): facing).rotateY();
				list.add(new AxisAlignedBB(0, -0.1875, 0, 0, 0, 0)
						.grow(facing.getFrontOffsetX(), 0, facing.getFrontOffsetZ())
						.grow(facing.rotateY().getFrontOffsetX()*0.125, 0, facing.rotateY().getFrontOffsetZ()*0.125)
						.offset(g.getFrontOffsetX()*0.1875, 0, g.getFrontOffsetZ()*0.1875)
						.offset(getPos().getX()+0.5, getPos().getY(), getPos().getZ()+0.5));
			}
			else
				list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
		}

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
	public int getConstructionCost()
	{
		return RadioStation.constructionEnergy;
	}

	@Override
	public int getCurrentConstruction()
	{
		return master().construction;
	}

	@Override
	public void setCurrentConstruction(int construction)
	{
		master().construction = construction;
	}

	@Override
	public void onConstructionFinish()
	{

	}

	@Override
	public boolean shoudlPlaySound(String sound)
	{
		return true;
	}
}
