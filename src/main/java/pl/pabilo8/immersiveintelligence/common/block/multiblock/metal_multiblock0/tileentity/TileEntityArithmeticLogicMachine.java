package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import org.apache.commons.lang3.ArrayUtils;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArithmeticLogicMachine;
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
public class TileEntityArithmeticLogicMachine extends TileEntityMultiblockIIGeneric<TileEntityArithmeticLogicMachine> implements IIIGuiMultiblockTile, IBooleanAnimatedPartsBlock
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

	public TileEntityArithmeticLogicMachine()
	{
		super(MultiblockArithmeticLogicMachine.INSTANCE);
		//basic machine properties
		energyStorage = new FluxStorageAdvanced(ArithmeticLogicMachine.energyCapacity);
		inventory = NonNullList.withSize(CIRCUITS_UPGRADED+STORAGE_SLOTS, ItemStack.EMPTY);
		memory = new DataPacket();

		//interactable parts
		door = new MultiblockInteractablePart(30);
		keyboard = new MultiblockInteractablePart(10);
		drawer = new MultiblockInteractablePart(20);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		door = null;
		memory = null;
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
		int circuitsAmount = CIRCUITS_BASE; //TODO: 08.01.2024 6 circuits with upgrade
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
		door.setState(state);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(door.setState(state))
		{
			world.playSound(null, getPos(), state?IISounds.metalLockerOpen: IISounds.metalLockerClose, SoundCategory.BLOCKS, 0.25F, 1f);
			IIPacketHandler.sendToClient(this, new MessageBooleanAnimatedPartsSync(0, state, getPos()));
		}
	}
}
