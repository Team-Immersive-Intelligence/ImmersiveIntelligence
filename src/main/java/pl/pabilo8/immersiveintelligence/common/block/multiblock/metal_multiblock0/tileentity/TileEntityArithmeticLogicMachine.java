package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradableMachine;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.IUpgradeStorageMachine;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.MachineUpgrade;
import pl.pabilo8.immersiveintelligence.api.utils.upgrade_system.UpgradeStorage;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityArithmeticLogicMachine extends TileEntityMultiblockIIGeneric<TileEntityArithmeticLogicMachine>
		implements IIIGuiMultiblockTile, IBooleanAnimatedPartsBlock, IUpgradeStorageMachine<TileEntityArithmeticLogicMachine>
{
	/**
	 * ALM has 4 circuits by default, 6 with upgrade<br>
	 * and 16 storage slots for additional circuits
	 */
	public static final int CIRCUITS_BASE = 4, CIRCUITS_UPGRADED = 6, STORAGE_SLOTS = 16;
	/**
	 * Used for GUI animations
	 */
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public MultiblockInteractablePart door, keyboard, drawer;
	@SyncNBT
	public DataPacket memory;
	@SyncNBT
	public UpgradeStorage<TileEntityArithmeticLogicMachine> upgradeStorage;

	public TileEntityArithmeticLogicMachine()
	{
		super(MultiblockArithmeticLogicMachine.INSTANCE);
		//basic machine properties
		energyStorage = new FluxStorageAdvanced(ArithmeticLogicMachine.energyCapacity);
		inventory = NonNullList.withSize(CIRCUITS_UPGRADED+STORAGE_SLOTS, ItemStack.EMPTY);
		upgradeStorage = new UpgradeStorage<>(this);
		memory = new DataPacket();

		//interactable parts
		door = new MultiblockInteractablePart(0, 30, 1);
		keyboard = new MultiblockInteractablePart(1, 10, 1);
		drawer = new MultiblockInteractablePart(2, 20, 1);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		door = null;
		memory = null;
		keyboard = null;
		upgradeStorage = null;
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		//Receive edits from GUI
		if(message.hasKey("expressions"))
		{
			//Get updated expressions
			NBTTagCompound expressions = message.getCompoundTag("expressions");
			int page = expressions.getInteger("page");

			//Update circuit stack
			ItemStack stack = inventory.get(page);
			DataPacket packet = new DataPacket(expressions.getCompoundTag("list"));
			((ItemIIFunctionalCircuit)stack.getItem()).writeDataToItem(stack, packet);
			inventory.set(page, stack);
		}
	}

	@Override
	protected void onUpdate()
	{
		door.update();
		drawer.update();
		keyboard.update();
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY:
				return getPOI("energy");
			case DATA:
				return getPOI("data");
			case MISC_CONTROL_PANEL:
				return getPOI("front_panel");
			case MISC_CRATE:
				return getPOI("crates");
			case MISC_DOOR:
				return getPOI("door");
		}
		return new int[0];
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		//Prepare reply packet
		boolean fromLeft = ArrayUtils.contains(getPOI("data_left"), pos);
		DataPacket newPacket = packet.clone();

		//Process received packet with circuits
		int circuitsAmount = hasUpgrade(IIContent.UPGRADE_CIRCUIT_RACKS)?CIRCUITS_UPGRADED: CIRCUITS_BASE;
		boolean[] circuit = new boolean[circuitsAmount];
		DataPacket[] cPacket = new DataPacket[circuitsAmount];

		//Find if there are any circuits, get operations stored in them
		for(int i = 0; i < circuitsAmount; i++)
		{

			circuit[i] = !inventory.get(i).isEmpty();
			cPacket[i] = circuit[i]?
					((ItemIIFunctionalCircuit)inventory.get(i).getItem()).getStoredData(inventory.get(i)):
					null;
		}

		//Use every circuit, as long as there is energy
		for(int i = 0; i < circuitsAmount; i++)
			if(circuit[i])
			{
				//Nothing is free
				if(energyStorage.extractEnergy(ArithmeticLogicMachine.energyUsage, false) < ArithmeticLogicMachine.energyUsage)
					break;

				//Perform operations
				for(char c : DataPacket.VARIABLE_NAMES)
				{
					DataType var = cPacket[i].get(c);
					if(!(var instanceof DataTypeExpression))
						continue;

					DataTypeExpression exp = ((DataTypeExpression)var);
					char condition = exp.getRequiredVariable();

					//Respect condition, if set
					if(condition==' '||IIDataHandlingUtils.asBoolean(condition, packet))
						newPacket.set(c, exp.getValue(newPacket));
				}
			}

		//Send reply to opposite side
		sendData(newPacket,
				fromLeft?facing.rotateY(): facing.rotateYCCW(),
				(fromLeft?getPOI("data_right"): getPOI("data_left"))[0]
		);
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Nullable
	@Override
	public TileEntity getGuiMaster()
	{
		return master();
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof ItemIIFunctionalCircuit;
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, door, keyboard, drawer);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		MultiblockInteractablePart result = MultiblockInteractablePart.setStates(state, part, door, keyboard, drawer);
		if(result!=null)
		{
			if(result==door)
				world.playSound(null, getPos(), state?IISounds.metalLockerOpen: IISounds.metalLockerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			else //keyboard and drawer make the same sound
				world.playSound(null, getPos(), state?IISounds.drawerOpen: IISounds.drawerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(result, this));
		}
	}

	//--- IUpgradeStorageMachine ---//

	@Override
	public UpgradeStorage<TileEntityArithmeticLogicMachine> getUpgradeStorage()
	{
		return upgradeStorage;
	}

	@Override
	public boolean upgradeMatches(MachineUpgrade upgrade)
	{
		return upgrade==IIContent.UPGRADE_MEMORY||upgrade==IIContent.UPGRADE_CIRCUIT_RACKS;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends TileEntity & IUpgradableMachine> T getUpgradeMaster()
	{
		return (T)master();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderWithUpgrades(MachineUpgrade... upgrades)
	{

	}

	@Override
	public void removeUpgrade(MachineUpgrade upgrade)
	{
		IUpgradeStorageMachine.super.removeUpgrade(upgrade);
		if(upgrade==IIContent.UPGRADE_CIRCUIT_RACKS)
		{
			for(int i = CIRCUITS_BASE; i < CIRCUITS_UPGRADED; i++)
				if(!inventory.get(i).isEmpty())
				{
					ItemStack stack = inventory.get(i);
					Utils.dropStackAtPos(world, getPos().offset(facing), stack, facing.getOpposite());
					inventory.set(i, ItemStack.EMPTY);
				}
		}
		else if(upgrade==IIContent.UPGRADE_MEMORY)
			memory = new DataPacket();
		updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
	}
}
