package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.api.crafting.DataProgrammingRecipe;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradeStorageMachine;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.UpgradeStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.DataInputMachine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockDataInputMachine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionBase;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.production.TileEntityMultiblockProductionSingle;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import java.util.Optional;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityDataInputMachine extends TileEntityMultiblockProductionSingle<TileEntityDataInputMachine, DataProgrammingRecipe>
		implements IBooleanAnimatedPartsBlock, IUpgradeStorageMachine<TileEntityDataInputMachine>
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
	@SyncNBT(name = "variables", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_RECIPE_CHANGED, SyncEvents.TILE_CLIENT_MESSAGE})
	public DataPacket storedData = new DataPacket();
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public int selectedDataSlot;

	@SyncNBT
	public UpgradeStorage<TileEntityDataInputMachine> upgradeStorage;

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
		inputHandler = getSingleInventoryHandler(SLOT_INPUT);
		outputHandler = getSingleInventoryHandler(SLOT_OUTPUT);

		upgradeStorage = new UpgradeStorage<>(this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		storedData = null;
		drawer = hatch = null;
		inputHandler = outputHandler = null;
		upgradeStorage = null;
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		if(message.hasKey("send_packet"))
			this.sendData(storedData, getDirection("data"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
	}

	@Override
	protected void onUpdate()
	{
		super.onUpdate();
		drawer.update();
		hatch.update();
		upgradeStorage.update();

		if(world.isRemote)
			return;

		//Send packet on redstone
		if(sendPacketToggle^getRedstoneAtPos(0))
		{
			sendPacketToggle = !sendPacketToggle;
			//Finally!
			if(sendPacketToggle)
				this.sendData(storedData, getDirection("data"), getPOI(MultiblockPOI.DATA_OUTPUT)[0]);
		}
		//Check for item being taken out
		if(currentProcess!=null&&!currentProcess.recipe.input.matchesItemStack(inventory.get(SLOT_INPUT)))
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
				.filter(recipe -> recipe.input.matchesItemStack(inventory.get(SLOT_INPUT)))
				.findFirst();
		return found.map(IIMultiblockProcess::new).orElse(null);
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
			//Skip the recipe if input is invalid
			if(!recipe.input.matchesItemStack(inventory.get(SLOT_INPUT)))
				return true;
			//Proceed
			ItemStack output = recipe.operationFrom.apply(inventory.get(SLOT_INPUT), storedData, dataTypes -> storedData = dataTypes);
			if(outputHandler.insertItem(0, output, false).isEmpty())
			{
				inputHandler.extractItem(0, 1, false);
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onProductionFinish(IIMultiblockProcess<DataProgrammingRecipe> process)
	{

	}

	@Override
	public UpgradeStorage<TileEntityDataInputMachine> getUpgradeStorage()
	{
		return upgradeStorage;
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_ADVANCED_DATA;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)master();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{

	}
}
