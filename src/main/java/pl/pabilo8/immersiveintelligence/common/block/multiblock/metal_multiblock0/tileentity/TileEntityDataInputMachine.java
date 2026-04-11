package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;
import pl.pabilo8.immersiveintelligence.api.crafting.recipe.IIMultiblockRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 11.04.2026
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityDataInputMachine extends TileEntityMultiblockProductionSingle<TileEntityDataInputMachine, DataProgrammingRecipe>
		implements IBooleanAnimatedPartsBlock, IManagedUpgradableDevice<TileEntityDataInputMachine>
{
	/**
	 * Used for GUI animations
	 */
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public MultiblockInteractablePart drawer, hatch;

	/**
	 * Will send stored packet if true, then switch back to false
	 */
	@SyncNBT(name = "send_packet", events = {SyncEvents.TILE_CLIENT_MESSAGE})
	public boolean sendPacket = false;

	/**
	 * A temporary value for calculating the falling & rising edge.
	 */
	boolean prevSignal = false;
	/**
	 * Stored data packet
	 */
	@SyncNBT(name = "variables", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CLIENT_MESSAGE})
	public DataPacket storedData = new DataPacket();
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public int selectedDataSlot = 0;

	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityDataInputMachine> upgradeManager;

	private IEInventoryHandler inputHandler, outputHandler;


	public TileEntityDataInputMachine()
	{
		super(MultiblockDataInputMachine.INSTANCE);
		//Init basics
		this.energyStorage = new FluxStorageAdvanced(DataInputMachine.energyCapacity);
		this.inventory = NonNullList.withSize(26, ItemStack.EMPTY);
		this.upgradeManager = new UpgradeManager<>(this);

		//Init animated parts
		this.drawer = new MultiblockInteractablePart(0, 15, 0.85f);
		this.hatch = new MultiblockInteractablePart(1, 24, 1.25f);
		this.inputHandler = getSingleInventoryHandler(MultiblockDataInputMachine.SLOT_INPUT);
		this.outputHandler = getSingleInventoryHandler(MultiblockDataInputMachine.SLOT_OUTPUT);

	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		storedData = null;
		drawer = hatch = null;
		inputHandler = outputHandler = null;
		upgradeManager = null;
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
		drawer.update();
		hatch.update();
		upgradeManager.update();

		if(world.isRemote)
			return;

		boolean currentSignal = getRedstoneAtPos(0);

		//Send packet on rising edge redstone signal or ui button press
		if(sendPacket || ((prevSignal ^ currentSignal) & currentSignal))
		{
			this.sendData(storedData, getDirection("data"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
			sendPacket = false;
		}

		prevSignal = currentSignal;

		//Check for item being taken out
		if(currentProcess!=null&&!currentProcess.recipe.input.matchesItemStack(inventory.get(MultiblockDataInputMachine.SLOT_INPUT)))
			this.currentProcess.ticks = this.currentProcess.maxTicks;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case DATA_OUTPUT:
				return getPOI("data");
			case MISC_HATCH:
				return getPOI("hatch");
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
	public IIGUI getGUI()
	{
		return IIGUI.DATA_INPUT_MACHINE_STORAGE;
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
				.filter(recipe -> recipe.input.matchesItemStack(inventory.get(MultiblockDataInputMachine.SLOT_INPUT)))
				.findFirst();
		return found.map(IIMultiblockProcess::new).orElse(null);
	}

	@Override
	protected IIMultiblockProcess<DataProgrammingRecipe> getProcessByName(String name)
	{
		DataProgrammingRecipe recipe = IIMultiblockRecipe.getRecipe(DataProgrammingRecipe.class, name);
		return recipe==null?null: new IIMultiblockProcess<>(recipe);
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
			//Skip the recipe if input is invalid
			if(!recipe.input.matchesItemStack(inventory.get(MultiblockDataInputMachine.SLOT_INPUT)))
				return true;

			//Take a copy of the original item and apply recipe
			ItemStack output = recipe.operationFrom.apply(inputHandler.extractItem(MultiblockDataInputMachine.SLOT_INPUT, 1, true),
					storedData, dataTypes -> storedData = dataTypes);
			//Try to output
			return outputHandler.insertItem(0, output, false).isEmpty()&&
					!inputHandler.extractItem(0, 1, false).isEmpty();
		}
		return false;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<DataProgrammingRecipe> process)
	{

	}

	@Nonnull
	@Override
	public UpgradeManager<TileEntityDataInputMachine> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
	}
}
