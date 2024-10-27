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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.device.IDataDevice;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.NBTSerialisation;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
		implements IIIInventory, IIEInternalFluxHandler, IHammerInteraction, IRedstoneOutput, IDataDevice, IComparatorOverride
{
	@SyncNBT(name = "redstone_control")
	protected boolean redstoneControlInverted = false;
	@SyncNBT(name = "inventory", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_GUI_CLOSED, SyncEvents.TILE_RECIPE_CHANGED})
	public NonNullList<ItemStack> inventory;
	@SyncNBT(name = "ifluxEnergy")
	public FluxStorageAdvanced energyStorage;
	private IEForgeEnergyWrapper wrapper = new IEForgeEnergyWrapper(this, null);


	//--- Constructor, Initialization ---//

	public TileEntityMultiblockIIGeneric(MultiblockStuctureBase<T> multiblock)
	{
		super(multiblock);
		inventory = NonNullList.create();
		energyStorage = new FluxStorageAdvanced(1);
	}

	@Override
	protected void dummyCleanup()
	{
		inventory = null;
		energyStorage = null;
		wrapper = null;
	}

	//--- NBT ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.deserializeAll(tile, nbt, false));
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);
		if(isDummy())
			return;

		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeAll(tile, nbt));
	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isDummy()||isFullSyncMessage(message))
			return;

		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.deserializeAll(tile, message, true));
	}

	protected void updateTileForTime()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeForTime(tile, nbt, (int)(world.getTotalWorldTime()%1000)));
		sendNBTMessageClient(nbt);
	}

	protected void updateTileForEvent(SyncNBT.SyncEvents event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTSerialisation.synchroniseFor(this, (tag, tile) -> tag.serializeForEvent(tile, nbt, event));
		sendNBTMessageClient(nbt);
	}

	//--- Redstone ---//

	public boolean getRedstoneAtPos(int id)
	{
		return (world.isBlockPowered(getBlockPosForPos(
				getPOI(MultiblockPOI.REDSTONE_INPUT)[id]))
		)^redstoneControlInverted;
	}

	@Override
	public final boolean canConnectRedstone(@Nonnull IBlockState state, @Nonnull EnumFacing side)
	{
		return this.isPOI(MultiblockPOI.REDSTONE);
	}

	@Override
	public boolean hammerUseSide(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, float hitX, float hitY, float hitZ)
	{
		if(this.isPOI(MultiblockPOI.REDSTONE_INPUT))
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

	@Override
	public final void onReceive(DataPacket packet, @Nullable EnumFacing side)
	{
		T master = master();
		if(master!=null&&isPOI(MultiblockPOI.DATA_INPUT))
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
		IIDataHandlingUtils.sendPacketAdjacently(packet, world, getBlockPosForPos(pos), facing);
	}

	//--- Inventory ---//
	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return inventory;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&isPOI(MultiblockPOI.ITEM_INPUT))
			return true;
		return super.hasCapability(capability, facing);
	}

	@Override
	public int getComparatorInputOverride()
	{
		if(!this.isPOI(MultiblockPOI.REDSTONE_OUTPUT))
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
		if(this.formed&&this.isPOI(MultiblockPOI.ENERGY))
			return this.isPOI(MultiblockPOI.ENERGY_OUTPUT)?SideConfig.OUTPUT: SideConfig.INPUT;
		return SideConfig.NONE;
	}

	@Override
	public IEForgeEnergyWrapper getCapabilityWrapper(EnumFacing facing)
	{
		if(this.formed&&this.isPOI(MultiblockPOI.ENERGY))
			return wrapper;
		return null;
	}

	@Override
	public void postEnergyTransferUpdate(int energy, boolean simulate)
	{
		if(!simulate&&!world.isRemote&&energy!=0)
		{
			T master = master();
			if(master!=null&&world.getTotalWorldTime()%8==0)
				master.sendNBTMessageClient(master.energyStorage.writeToNBT(new NBTTagCompound()));
		}
	}

	//--- Fluids ---//


	@Nonnull
	@Override
	protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
	{
		T master = master();
		if(master!=null&&(isPOI(MultiblockPOI.FLUID)))
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
		if(isPOI(MultiblockPOI.FLUID_INPUT))
			return master().isTankAvailable(pos, iTank);
		return false;
	}

	@Override
	protected boolean canDrainTankFrom(int iTank, EnumFacing side)
	{
		if(isPOI(MultiblockPOI.FLUID_OUTPUT))
			return master().isTankAvailable(pos, iTank);
		return false;
	}

	protected boolean isTankAvailable(int pos, int tank)
	{
		return false;
	}
}
