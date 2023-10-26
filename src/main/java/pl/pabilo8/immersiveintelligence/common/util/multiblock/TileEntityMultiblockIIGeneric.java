package pl.pabilo8.immersiveintelligence.common.util.multiblock;

import blusunrize.immersiveengineering.api.IEEnums.SideConfig;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorage;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IComparatorOverride;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHammerInteraction;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IRedstoneOutput;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.ChatUtils;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IEForgeEnergyWrapper;
import blusunrize.immersiveengineering.common.util.EnergyHelper.IIEInternalFluxHandler;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IAdvancedBounds;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * A standard II "medium-high tier" multiblock.<br>
 * Uses power, handles fluids, items, redstone, data, <i>does the dishes</i>. <b>Multi-Function Multi-Tool!</b>
 * <hr>
 * Closer to {@link TileEntityMultiblockMetal} functionally, but doesn't feature recipes.<br>
 *
 * @author Pabilo8
 * @since 04.08.2022
 */
@SuppressWarnings("unused")
public abstract class TileEntityMultiblockIIGeneric<T extends TileEntityMultiblockIIGeneric<T>> extends TileEntityMultiblockIIBase<T>
		implements IIIInventory, IIEInternalFluxHandler, IHammerInteraction, IRedstoneOutput, IDataDevice, IComparatorOverride, IAdvancedBounds
{
	private List<AxisAlignedBB> aabb = null;

	protected boolean redstoneControlInverted = false;
	public NonNullList<ItemStack> inventory;
	IEForgeEnergyWrapper wrapper = new IEForgeEnergyWrapper(this, null);
	public FluxStorageAdvanced energyStorage;

	//--- Reference Variables ---//

	public static final String KEY_SYNC_AABB = "_sync_aabb";
	public static final String KEY_INVENTORY = "inventory";
	public static final String KEY_ENERGY = "ifluxEnergy";
	public static final String KEY_REDSTONE_CONTROL = "redstone_control";

	//--- Constructor, Initialization ---//

	public TileEntityMultiblockIIGeneric(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
	}

	//--- NBT ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

//		if(!descPacket)
		{
			if(energyStorage.getMaxEnergyStored()!=0)
				energyStorage.readFromNBT(nbt);
			if(inventory.size()!=0)
				inventory = Utils.readInventory(nbt.getTagList(KEY_INVENTORY, NBT.TAG_COMPOUND), inventory.size());
			redstoneControlInverted = nbt.getBoolean(KEY_REDSTONE_CONTROL);
		}

	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

//		if(!descPacket)
		{
			if(energyStorage.getMaxEnergyStored()!=0)
				energyStorage.writeToNBT(nbt);
			if(inventory.size()!=0)
				nbt.setTag(KEY_INVENTORY, Utils.writeInventory(inventory));
			nbt.setBoolean(KEY_REDSTONE_CONTROL, redstoneControlInverted);
		}
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy()||isFullSyncMessage(message))
			return;

		if(message.hasKey(KEY_SYNC_AABB))
			forMultiblockBlocks(TileEntityMultiblockIIGeneric::forceReCacheAABB);

		if(message.hasKey(KEY_INVENTORY))
			inventory = Utils.readInventory(message.getTagList(KEY_INVENTORY, 10), inventory.size());
		if(message.hasKey(KEY_ENERGY))
			energyStorage.readFromNBT(message);
		if(message.hasKey(KEY_REDSTONE_CONTROL))
			redstoneControlInverted = message.getBoolean(KEY_REDSTONE_CONTROL);

	}

	//--- Redstone ---//

	//REFACTOR: 23.10.2023 use POI instead of getRedstonePos, getEnergyPos, etc.
	public abstract int[] getRedstonePos(boolean input);

	public final boolean isRedstonePos(boolean input)
	{
		return Arrays.binarySearch(getRedstonePos(input), pos) >= 0;
	}

	public boolean getRedstoneAtPos(int id)
	{
		return (world.isBlockPowered(getBlockPosForPos(getRedstonePos(true)[id])))^redstoneControlInverted;
	}

	@Override
	public final boolean canConnectRedstone(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return this.isRedstonePos(false)||this.isRedstonePos(true);
	}

	@Override
	public boolean hammerUseSide(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(this.isRedstonePos(true))
		{
			T master = master();
			if(master!=null)
			{
				//Send the "Redstone Control Inverted" message
				master.redstoneControlInverted = !master.redstoneControlInverted;
				ChatUtils.sendServerNoSpamMessages(player,
						new TextComponentTranslation(Lib.CHAT_INFO+"rsControl."+(master.redstoneControlInverted?"invertedOn": "invertedOff")));
				forceTileUpdate();
			}
			return true;
		}
		return false;
	}

	@Override
	public int getStrongRSOutput(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return 0;
	}

	//--- Data ---//

	@Nonnull
	public abstract int[] getDataPos(boolean input);

	public final boolean isDataPos(boolean input)
	{
		return Arrays.binarySearch(getDataPos(input), pos) >= 0;
	}

	@Override
	public final void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		T master = master();
		if(master!=null&&isDataPos(true))
			master.receiveData(packet, pos);
	}


	/**
	 * Called on master when the TE receives data.
	 *
	 * @param packet data received
	 */
	public void receiveData(DataPacket packet, int pos)
	{

	}

	/**
	 * Used to send data easily.
	 *
	 * @param packet data received
	 */
	public void sendData(DataPacket packet, EnumFacing facing, int pos)
	{
		IDataConnector conn = IIUtils.findConnectorFacing(getBlockPosForPos(pos), world, facing);
		if(conn!=null)
			conn.sendPacket(packet);
	}

	//--- Inventory ---//
	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public int getComparatorInputOverride()
	{
		if(!this.isRedstonePos(false))
			return 0;
		T master = master();
		if(master==null)
			return 0;
		return Utils.calcRedstoneFromInventory(master);
	}

	protected final IEInventoryHandler getSingleInventoryHandler(int slotID)
	{
		return getSingleInventoryHandler(slotID, true, true);
	}

	protected final IEInventoryHandler getSingleInventoryHandler(int slotID, boolean canInput, boolean canOutput)
	{
		return new IEInventoryHandler(1, this, slotID, canInput, canOutput);
	}

	@Nonnull
	public abstract int[] getEnergyPos(EnergyType type);

	public boolean isEnergyPos()
	{
		return Arrays.binarySearch(getEnergyPos(EnergyType.ELECTRIC), pos) >= 0;
	}

	@Nonnull
	@Override
	public final FluxStorage getFluxStorage()
	{
		T master = this.master();
		if(master!=null)
			return master.energyStorage;
		return energyStorage;
	}

	@Nonnull
	@Override
	public SideConfig getEnergySideConfig(EnumFacing facing)
	{
		return this.formed&&this.isEnergyPos()?SideConfig.INPUT: SideConfig.NONE;
	}

	@Override
	public IEForgeEnergyWrapper getCapabilityWrapper(EnumFacing facing)
	{
		if(this.formed&&this.isEnergyPos())
			return wrapper;
		return null;
	}

	@Override
	public void postEnergyTransferUpdate(int energy, boolean simulate)
	{
		if(!simulate&&!world.isRemote)
		{
			T master = master();
			if(master!=null)
				master.sendNBTMessageClient(master.energyStorage.writeToNBT(new NBTTagCompound()));
		}
	}

	//--- Fluids ---//


	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		T master = master();
		if(master!=null&&(isFluidPos(true)||isFluidPos(false)))
			return master.getFluidTanks(pos, side);

		return super.getAccessibleFluidTanks(side);
	}

	/**
	 * Returns master tanks
	 *
	 * @param pos  position inside the multiblock
	 * @param side side accessed
	 * @return array of tanks available
	 */
	protected IFluidTank[] getFluidTanks(int pos, EnumFacing side)
	{
		return new IFluidTank[0];
	}

	@Override
	protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
	{
		if(isFluidPos(true))
			return master().isTankAvailable(pos, iTank);
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		if(isFluidPos(false))
			return master().isTankAvailable(pos, iTank);
		return false;
	}

	@Nonnull
	public int[] getFluidPos(boolean input)
	{
		return new int[0];
	}

	public boolean isFluidPos(boolean input)
	{
		return Arrays.binarySearch(getFluidPos(input), pos) >= 0;
	}

	protected boolean isTankAvailable(int pos, int tank)
	{
		return false;
	}


	//--- IAdvancedBounds ---//

	public void forceReCacheAABB()
	{
		this.aabb = null;
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		//Use or create AABB cache
		if(pos!=-1&&aabb==null)
			aabb = multiblock.getAABB(pos, getPos(), facing, mirrored);
		return aabb;
	}

	/**
	 * Represents an animated part of the multiblock, like a drawer
	 */
	public static class MultiblockInteractablePart
	{
		boolean opened = false;
		float progress = 0;
		final float maxProgress;

		public MultiblockInteractablePart(float maxProgress)
		{
			this.maxProgress = maxProgress;
		}

		public float getProgress(float partialTicks)
		{
			return MathHelper.clamp(progress+(opened?partialTicks: -partialTicks), 0, maxProgress)/maxProgress;
		}

		public void update()
		{
			this.progress = MathHelper.clamp(progress+(opened?1: -1), 0, maxProgress);
		}

		public void setState(boolean state)
		{
			this.opened = state;
		}
	}

	public BlockPos getPosForPOI(String name)
	{
		return getBlockPosForPos(multiblock.getPointOfInterest(name));
	}

	public enum EnergyType
	{
		ELECTRIC,
		ROTARY,
		HEAT
	}
}
