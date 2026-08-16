package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.INBTSerializable;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeExpression;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.api.upgrade.IManagedUpgradableDevice;
import pl.pabilo8.immersiveintelligence.api.upgrade.Upgrade;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeManager;
import pl.pabilo8.immersiveintelligence.api.upgrade.UpgradeUtils.MachineStyle;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArithmeticLogicMachine;
import pl.pabilo8.immersiveintelligence.common.item.data.ItemIIFunctionalCircuit;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 08.01.2024
 * @ii-approved 0.3.1
 * @updated 03.30.2026
 * @since 28.06.2019
 */
public class TileEntityArithmeticLogicMachine extends TileEntityMultiblockIIGeneric<TileEntityArithmeticLogicMachine>
		implements IIIGuiMultiblockTile, IBooleanAnimatedPartsBlock, IManagedUpgradableDevice<TileEntityArithmeticLogicMachine>
{
	/**
	 * Used for GUI animations
	 */
	@SyncNBT(events = SyncEvents.TILE_GUI_OPENED)
	public MultiblockInteractablePart door, keyboard, drawer;
	@SyncNBT(events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public DataPacket memory;
	@SyncNBT(name = "memory_load_rules", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<MemoryTransferRule, NBTTagCompound> memoryLoadRules;
	@SyncNBT(name = "memory_save_rules", events = {SyncEvents.TILE_GUI_OPENED, SyncEvents.TILE_CLIENT_MESSAGE})
	public EasyCollection<MemoryTransferRule, NBTTagCompound> memorySaveRules;
	@SyncNBT(name = "upgrades", events = SyncEvents.TILE_UPGRADES_MODIFIED)
	public UpgradeManager<TileEntityArithmeticLogicMachine> upgradeManager;

	public TileEntityArithmeticLogicMachine()
	{
		super(MultiblockArithmeticLogicMachine.INSTANCE);
		//basic machine properties
		this.energyStorage = new FluxStorageAdvanced(ArithmeticLogicMachine.energyCapacity);
		this.inventory = NonNullList.withSize(MultiblockArithmeticLogicMachine.CIRCUITS_UPGRADED+MultiblockArithmeticLogicMachine.STORAGE_SLOTS, ItemStack.EMPTY);
		this.upgradeManager = new UpgradeManager<>(this);
		this.memory = new DataPacket();
		this.memoryLoadRules = new EasyCollection<>(MemoryTransferRule::new);
		this.memorySaveRules = new EasyCollection<>(MemoryTransferRule::new);

		//interactable parts
		this.door = new MultiblockInteractablePart(0, 30, 1);
		this.keyboard = new MultiblockInteractablePart(1, 10, 1);
		this.drawer = new MultiblockInteractablePart(2, 20, 1);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		door = null;
		memory = null;
		memoryLoadRules = null;
		memorySaveRules = null;
		keyboard = null;
		upgradeManager = null;
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromClient(message);
		//Receive expression edits from the circuit editor GUI
		if(message.hasKey("expressions"))
		{
			NBTTagCompound expressions = message.getCompoundTag("expressions");
			int page = expressions.getInteger("page");

			if(page < 0||page >= inventory.size())
				return;

			//Update circuit stack
			ItemStack stack = inventory.get(page);
			if(stack.isEmpty()||!(stack.getItem() instanceof ItemIIFunctionalCircuit))
				return;

			DataPacket packet = expressions.hasKey("list")?
					new DataPacket(expressions.getCompoundTag("list")):
					new DataPacket(expressions);
			((ItemIIFunctionalCircuit)stack.getItem()).writeDataToItem(stack, packet);
			inventory.set(page, stack);
			markDirty();
			forceTileUpdate();
		}

		if(message.hasKey("memory_load_rules", NBT.TAG_LIST))
		{
			memoryLoadRules.deserializeNBT(message.getTagList("memory_load_rules", NBT.TAG_COMPOUND));
			markDirty();
			forceTileUpdate();
		}
		if(message.hasKey("memory_save_rules", NBT.TAG_LIST))
		{
			memorySaveRules.deserializeNBT(message.getTagList("memory_save_rules", NBT.TAG_COMPOUND));
			markDirty();
			forceTileUpdate();
		}
	}

	@Override
	protected void onUpdate()
	{
		door.update();
		drawer.update();
		keyboard.update();
		upgradeManager.update();
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		//Prepare reply packet
		DataPacket newPacket = packet.clone();

		//Optional memory input stage: copy configured variables from internal memory into the working packet.
		if(isUpgradeInstalled(IIContent.UPGRADE_MEMORY)&&applyMemoryTransfers(memoryLoadRules, memory, newPacket))
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		}

		//Process received packet with circuits
		int circuitsAmount = isUpgradeInstalled(IIContent.UPGRADE_CIRCUIT_RACKS)?MultiblockArithmeticLogicMachine.CIRCUITS_UPGRADED:
				MultiblockArithmeticLogicMachine.CIRCUITS_BASE;
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

					//Respect condition, if set: expressions with a required variable only run when that variable is present in the input packet.
					if(condition==' '||(newPacket.has(condition)&&
							//In case the variable is a boolean, it must be true
							IIDataHandlingUtils.optionalBoolean(condition, newPacket).orElse(true)))
						newPacket.set(c, exp.getValue(newPacket));
				}
			}

		//Optional memory output stage: copy configured variables from the processed packet into internal memory.
		if(isUpgradeInstalled(IIContent.UPGRADE_MEMORY)&&applyMemoryTransfers(memorySaveRules, newPacket, memory))
		{
			markDirty();
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		}

		//Send reply to opposite side
		sendData(newPacket, getDirection("data_out"), getPOI("data_out")[0]);
	}

	private boolean applyMemoryTransfers(EasyCollection<MemoryTransferRule, NBTTagCompound> rules, DataPacket source, DataPacket target)
	{
		if(rules==null||rules.isEmpty()||source==null||target==null)
			return false;

		boolean changed = false;
		List<MemoryTransferRule> expiredRules = new ArrayList<>();
		for(MemoryTransferRule rule : rules)
			if(rule.applyTo(source, target))
			{
				changed = true;
				rule.consumeUse();
				if(rule.isExpired())
					expiredRules.add(rule);
			}

		if(!expiredRules.isEmpty())
		{
			rules.removeAll(expiredRules);
			changed = true;
		}
		return changed;
	}

	@Override
	public boolean canOpenGui()
	{
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.ARITHMETIC_LOGIC_MACHINE_STORAGE;
	}

	@Override
	public void onGuiOpened(@Nullable EntityPlayer player, boolean clientside)
	{
		if(!clientside)
			updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
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

	@Nonnull
	@Override
	public UpgradeManager<TileEntityArithmeticLogicMachine> getUpgradeManager()
	{
		return upgradeManager;
	}

	@Override
	public MachineStyle getUpgradableMachineStyle()
	{
		return MachineStyle.STEEL;
	}

	@Override
	public boolean removeUpgrade(Upgrade upgrade)
	{
		if(IManagedUpgradableDevice.super.removeUpgrade(upgrade))
		{
			if(upgrade==IIContent.UPGRADE_CIRCUIT_RACKS)
			{
				for(int i = MultiblockArithmeticLogicMachine.CIRCUITS_BASE; i < MultiblockArithmeticLogicMachine.CIRCUITS_UPGRADED; i++)
					if(!inventory.get(i).isEmpty())
					{
						ItemStack stack = inventory.get(i);
						Utils.dropStackAtPos(world, getPos().offset(facing), stack, facing.getOpposite());
						inventory.set(i, ItemStack.EMPTY);
					}
			}
			else if(upgrade==IIContent.UPGRADE_MEMORY)
			{
				memory = new DataPacket();
				memoryLoadRules.clear();
				memorySaveRules.clear();
			}
			updateTileForEvent(SyncEvents.TILE_UPGRADES_MODIFIED);
			return true;
		}
		return false;
	}

	public static class MemoryTransferRule implements INBTSerializable<NBTTagCompound>
	{
		public char sourceVariable = 'a';
		public char targetVariable = 'a';
		public boolean overwriteExisting = true;
		public int usageLimit = -1;

		public boolean isJob()
		{
			return usageLimit < 0;
		}

		public void consumeUse()
		{
			if(usageLimit > 0)
				usageLimit--;
		}

		public boolean isExpired()
		{
			return usageLimit==0;
		}

		public boolean applyTo(DataPacket source, DataPacket target)
		{
			if(source==null||target==null||!source.has(sourceVariable))
				return false;
			if(target.has(targetVariable)&&!overwriteExisting)
				return false;

			DataType value = source.get(sourceVariable);
			if(value==null)
				return false;

			target.set(targetVariable, value.clone());
			return true;
		}

		public MemoryTransferRule copy()
		{
			MemoryTransferRule copy = new MemoryTransferRule();
			copy.deserializeNBT(serializeNBT());
			return copy;
		}

		@Nonnull
		@Override
		public NBTTagCompound serializeNBT()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setString("source", String.valueOf(sourceVariable));
			nbt.setString("target", String.valueOf(targetVariable));
			nbt.setBoolean("overwrite_existing", overwriteExisting);
			nbt.setInteger("usage_limit", usageLimit);
			return nbt;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			String sourceString = nbt.getString("source");
			String targetString = nbt.getString("target");
			sourceVariable = sourceString.isEmpty()?'a': sourceString.charAt(0);
			targetVariable = targetString.isEmpty()?sourceVariable: targetString.charAt(0);
			overwriteExisting = !nbt.hasKey("overwrite_existing")||nbt.getBoolean("overwrite_existing");
			usageLimit = nbt.hasKey("usage_limit")?nbt.getInteger("usage_limit"): -1;
		}
	}

}
