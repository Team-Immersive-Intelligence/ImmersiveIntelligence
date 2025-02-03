package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIGuiList;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

/**
 * @author Pabilo8
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28-06-2019
 */
public class TileEntityDataInputMachine extends TileEntityMultiblockProductionSingle<TileEntityDataInputMachine, DataProgrammingRecipe> implements IBooleanAnimatedPartsBlock
{
	/**
	 * Used for GUI animations
	 */
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public MultiblockInteractablePart hatch, drawer;
	/**
	 * Will send stored packet if true
	 */
	public boolean sendPacketToggle = false;
	/**
	 * Stored data packet
	 */
	@SyncNBT(name = "variables")
	public DataPacket storedData = new DataPacket();

	public TileEntityDataInputMachine()
	{
		super(MultiblockDataInputMachine.INSTANCE);
		//Init basics
		energyStorage = new FluxStorageAdvanced(DataInputMachine.energyCapacity);
		inventory = NonNullList.withSize(26, ItemStack.EMPTY);

		//Init animated parts
		hatch = new MultiblockInteractablePart(0, 20, 1.25f);
		drawer = new MultiblockInteractablePart(1, 20, 0.85f);
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("variables"))
			storedData.fromNBT(message.getCompoundTag("variables"));
		if(message.hasKey("send_packet"))
			this.sendData(storedData, getDirection("sending"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
		drawer.update();
		hatch.update();

		if(!world.isRemote&&sendPacketToggle^getRedstoneAtPos(0))
		{
			sendPacketToggle = !sendPacketToggle;
			//Finally!
			if(sendPacketToggle)
				this.sendData(storedData, getDirection("sending"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
		}
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case DATA_INPUT:
				return getPOI("data");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case ENERGY_INPUT:
				return getPOI("energy");
			case MISC_HATCH:
				return getPOI("hatch");
			case MISC_CRATE:
				return getPOI("crate");
		}
		return new int[0];
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return DataProgrammingRecipe.RECIPE_LIST.stream().anyMatch(p -> p.input.matches(stack));
	}

	@Override
	public IIGuiList getGUI()
	{
		return IIGuiList.GUI_DATA_INPUT_MACHINE_STORAGE;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, drawer, hatch);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		MultiblockInteractablePart changed = MultiblockInteractablePart.setStates(state, part, drawer, hatch);
		if(changed==null)
			return;

		if(changed==drawer)
			world.playSound(null, getPos(), state?IISounds.drawerOpen: IISounds.drawerClose, SoundCategory.BLOCKS, 0.25F, 1f);
		else
			world.playSound(null, getPos(), state?IISounds.metalLockerOpen: IISounds.metalLockerClose, SoundCategory.BLOCKS, 0.25F, 1);
		IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(changed, this));
	}

	//TODO: 08.01.2024 reimplement programming data storage items

	@Override
	protected IIMultiblockProcess<DataProgrammingRecipe> findNewProductionProcess()
	{
		return null;
	}

	@Override
	protected IIMultiblockProcess<DataProgrammingRecipe> getProcessFromNBT(EasyNBT nbt)
	{
		return null;
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<DataProgrammingRecipe> process, boolean simulate)
	{
		return 0;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<DataProgrammingRecipe> process)
	{
		return false;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<DataProgrammingRecipe> process)
	{

	}
}
