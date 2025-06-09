package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
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
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.Optional;

/**
 * @author Pabilo8
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28-06-2019
 */
public class TileEntityDataInputMachine extends TileEntityMultiblockProductionSingle<TileEntityDataInputMachine, DataProgrammingRecipe> implements IBooleanAnimatedPartsBlock
{
	private static final int SLOT_INPUT = 0, SLOT_OUTPUT = 1;

	/**
	 * Used for GUI animations
	 */
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public MultiblockInteractablePart drawer, hatch;
	/**
	 * Will send stored packet if true, then switch back to false
	 */
	public boolean sendPacketToggle = false;
	/**
	 * Stored data packet
	 */
	@SyncNBT(name = "variables")
	public DataPacket storedData = new DataPacket();

	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public int selectedDataSlot;
	private IEInventoryHandler inputHandler, outputHandler;


	public TileEntityDataInputMachine()
	{
		super(MultiblockDataInputMachine.INSTANCE);
		//Init basics
		energyStorage = new FluxStorageAdvanced(DataInputMachine.energyCapacity);
		inventory = NonNullList.withSize(26, ItemStack.EMPTY);

		//Init animated parts
		drawer = new MultiblockInteractablePart(0, 15, 0.85f);
		hatch = new MultiblockInteractablePart(1, 24, 1.25f);
		inputHandler = getSingleInventoryHandler(SLOT_INPUT, true, false);
		outputHandler = getSingleInventoryHandler(SLOT_OUTPUT, false, true);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		storedData = null;
		drawer = hatch = null;
		inputHandler = outputHandler = null;
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
		return DataProgrammingRecipe.streamRecipes(DataProgrammingRecipe.class)
				.anyMatch(p -> p.input.matches(stack));
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

	@Override
	protected IIMultiblockProcess<DataProgrammingRecipe> findNewProductionProcess()
	{
		Optional<DataProgrammingRecipe> found = DataProgrammingRecipe.streamRecipes(DataProgrammingRecipe.class)
				.filter(recipe -> recipe.input.matches(inventory.get(SLOT_INPUT)))
				.findFirst();
		if(found.isPresent())
			return new IIMultiblockProcess<>(found.get())
					.withNBT(easyNBT -> easyNBT.mergeWith(EasyNBT.wrapNBT(inventory.get(SLOT_INPUT))));
		return null;
	}

	@Override
	protected IIMultiblockProcess<DataProgrammingRecipe> getProcessByName(String name)
	{
		return TileEntityMultiblockProductionBase.findRecipeFromList(DataProgrammingRecipe.class, name);
	}

	@Override
	public float getProductionStep(IIMultiblockProcess<DataProgrammingRecipe> process, boolean simulate)
	{
		int perTick = process.recipe.getTotalProcessEnergy()/process.maxTicks;
		if(energyStorage.extractEnergy(perTick, simulate) < perTick)
			return 0;
		return 1;
	}

	@Override
	protected boolean attemptProductionOutput(IIMultiblockProcess<DataProgrammingRecipe> process)
	{
		if(!world.isRemote)
		{
			DataProgrammingRecipe recipe = process.recipe;
			ItemStack output = recipe.operationFrom.apply(inventory.get(SLOT_INPUT), storedData, dataTypes -> storedData = dataTypes);
			if(outputHandler.insertItem(0, output, false).isEmpty())
			{
				inventory.get(SLOT_INPUT).shrink(1);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<DataProgrammingRecipe> process)
	{

	}
}
