package pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeBoolean;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeInteger;
import pl.pabilo8.immersiveintelligence.api.data.types.DataTypeItemStack;
import pl.pabilo8.immersiveintelligence.api.data.types.generic.DataType;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventoryInterfaceRule.ValueType;
import pl.pabilo8.immersiveintelligence.common.block.data_device.tileentity.inventory_interface.InventorySnapshot;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.EasyCollection;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIInventory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

/**
 * Reports the attached inventory through configured rules and data callbacks.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 27.10.2026
 */
public class TileEntityInventoryInterface extends TileEntityDataCallbackConnector implements IIIGuiMultiblockTile, IIIInventory
{
	public static final int MAX_RULES = 32;
	public static final int SAMPLE_INTERVAL = 10;

	@SyncNBT(events = {SyncEvents.TILE_CLIENT_MESSAGE, SyncEvents.TILE_GUI_OPENED})
	public EasyCollection<InventoryInterfaceRule, NBTTagCompound> rules = new EasyCollection<>(InventoryInterfaceRule::new);

	@Nullable
	private InventorySnapshot previous;
	@Nullable
	private TileEntity previousInventory;
	@Nullable
	private EnumFacing previousFacing;
	private int sampleTicks;

	@Override
	public void update()
	{
		super.update();
		if(!hasWorld()||world.isRemote||rules.isEmpty()||++sampleTicks < SAMPLE_INTERVAL)
			return;
		sampleTicks = 0;

		BlockPos inventoryPos = pos.offset(facing);
		TileEntity inventory = world.isBlockLoaded(inventoryPos)?world.getTileEntity(inventoryPos): null;
		IItemHandler handler = getInventoryHandler(inventory);
		if(handler==null)
		{
			resetTracking();
			return;
		}
		boolean newInventory = inventory!=previousInventory||facing!=previousFacing;
		if(newInventory)
			resetTracking();
		previousInventory = inventory;
		previousFacing = facing;

		InventorySnapshot current = InventorySnapshot.capture(handler);
		for(InventoryInterfaceRule rule : rules)
			if(rule.shouldSend(current, previous, newInventory?0: SAMPLE_INTERVAL))
			{
				DataPacket packet = rule.createPacket(current);
				if(!packet.isEmpty())
					sendPacket(packet);
			}
		previous = current;
	}

	@Nullable
	private IItemHandler getInventoryHandler(@Nullable TileEntity inventory)
	{
		if(inventory==null||inventory.isInvalid())
			return null;
		EnumFacing side = facing.getOpposite();
		if(inventory.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side))
			return inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
		//Do not bypass a capability that is exposed through other faces only.
		if(inventory.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			return null;
		for(EnumFacing otherSide : EnumFacing.VALUES)
			if(otherSide!=side&&inventory.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, otherSide))
				return null;
		if(inventory instanceof ISidedInventory)
			return new SidedInvWrapper((ISidedInventory)inventory, side);
		if(inventory instanceof IInventory)
			return new InvWrapper((IInventory)inventory);
		return null;
	}

	@Override
	public void onPacketReceive(DataPacket packet)
	{
		if(!hasWorld()||world.isRemote||packet==null)
			return;
		lastReceived = packet.clone();
		compatReceived = false;
		if(!packet.matchesConnector(EnumDyeColor.byMetadata(colorIn), -1)||!IIDataHandlingUtils.isCallbackPacket(packet))
			return;

		BlockPos inventoryPos = pos.offset(facing);
		IItemHandler handler = world.isBlockLoaded(inventoryPos)?getInventoryHandler(world.getTileEntity(inventoryPos)): null;
		InventorySnapshot snapshot = handler==null?null: InventorySnapshot.capture(handler);
		DataPacket response = IIDataHandlingUtils.handleCallback(packet, query -> queryInventory(query, packet, snapshot));
		if(response!=null)
			sendPacket(response);
	}

	/**
	 * Resolves an inventory callback without changing the inventory.
	 */
	@Nullable
	public static DataType queryInventory(String query, DataPacket request, @Nullable InventorySnapshot snapshot)
	{
		String[] arguments = query.trim().toLowerCase(Locale.ROOT).split(":", -1);
		String command = arguments[0];
		if("is_connected".equals(command)&&arguments.length==1)
			return new DataTypeBoolean(snapshot!=null);
		if(snapshot==null||arguments.length > 2)
			return null;

		ValueType type = switch(command)
		{
			case "get_total", "get_total_items" -> ValueType.TOTAL_ITEMS;
			case "get_total_stacks" -> ValueType.TOTAL_STACKS;
			case "get_total_slots" -> ValueType.TOTAL_SLOTS;
			case "get_empty_slots" -> ValueType.EMPTY_SLOTS;
			case "get_slot_items" -> ValueType.SLOT_ITEMS;
			case "get_slot_stack" -> ValueType.SLOT_STACK;
			case "get_slot_limit" -> ValueType.SLOT_LIMIT;
			case "get_item_count" -> ValueType.ITEM_COUNT;
			case "get_comparator" -> ValueType.COMPARATOR;
			case "get_fill" -> ValueType.FILL_PERCENT;
			case "is_empty" -> ValueType.IS_EMPTY;
			case "is_full" -> ValueType.IS_FULL;
			case "get_items" -> ValueType.ITEM_STACKS;
			default -> null;
		};
		if(type==null)
			return null;

		boolean slotQuery = type==ValueType.SLOT_ITEMS||type==ValueType.SLOT_STACK||type==ValueType.SLOT_LIMIT;
		int slot = 0;
		if(arguments.length==2)
		{
			if(!slotQuery)
				return null;
			try
			{
				slot = Integer.parseInt(arguments[1]);
			} catch(NumberFormatException ignored)
			{
				return null;
			}
		}
		else if(slotQuery&&request.has('s'))
		{
			DataType slotValue = request.get('s');
			if(!(slotValue instanceof DataTypeInteger))
				return null;
			slot = ((DataTypeInteger)slotValue).value;
		}
		if(slotQuery&&(slot < 0||slot >= snapshot.getTotalSlots()))
			return null;

		ItemStack filter = ItemStack.EMPTY;
		boolean matchNBT = true;
		if(type==ValueType.ITEM_COUNT)
		{
			DataType item = request.get('i');
			if(!(item instanceof DataTypeItemStack)||((DataTypeItemStack)item).value.isEmpty())
				return null;
			filter = ((DataTypeItemStack)item).value;
			if(request.get('n') instanceof DataTypeBoolean)
				matchNBT = ((DataTypeBoolean)request.get('n')).value;
		}
		return InventoryInterfaceRule.readValue(type, snapshot, slot, filter, matchNBT);
	}

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		//Bound nested rule entries before the generic reader creates or copies them.
		NBTTagCompound bounded = new NBTTagCompound();
		for(String key : nbt.getKeySet())
			bounded.setTag(key, "rules".equals(key)?boundedRules(nbt.getTagList(key, 10)): nbt.getTag(key));
		super.readCustomNBT(bounded, descPacket);
		validateChannels();
		resetTracking();
	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{
		if(!hasWorld()||world.isRemote)
			return;
		int oldColorIn = colorIn, oldColorOut = colorOut;
		boolean rulesChanged = false;
		if(message.hasKey("rules", 9))
		{
			EasyCollection<InventoryInterfaceRule, NBTTagCompound> incoming = new EasyCollection<>(InventoryInterfaceRule::new);
			incoming.deserializeNBT(boundedRules(message.getTagList("rules", 10)));
			//GUI rebuilds can resend the same configuration. Keep active rule edges in that case.
			if(!rules.serializeNBT().equals(incoming.serializeNBT()))
			{
				rules = incoming;
				resetTracking();
				rulesChanged = true;
			}
		}
		if(message.hasKey("colorIn", 3))
			colorIn = message.getInteger("colorIn");
		if(message.hasKey("colorOut", 3))
			colorOut = message.getInteger("colorOut");
		validateChannels();
		boolean channelsChanged = oldColorIn!=colorIn||oldColorOut!=colorOut;
		if(!rulesChanged&&!channelsChanged)
			return;
		if(channelsChanged)
			world.addBlockEvent(pos, getBlockType(), 254, 0);
		updateTileForEvent(SyncEvents.TILE_CLIENT_MESSAGE);
	}

	private static NBTTagList boundedRules(NBTTagList input)
	{
		NBTTagList result = new NBTTagList();
		for(int i = 0; i < Math.min(input.tagCount(), MAX_RULES); i++)
			result.appendTag(input.getCompoundTagAt(i));
		return result;
	}

	private void validateChannels()
	{
		colorIn = MathHelper.clamp(colorIn, 0, 15);
		colorOut = MathHelper.clamp(colorOut, 0, 15);
		if(colorIn==colorOut)
			colorOut = (colorOut+1)%16;
	}

	private void resetTracking()
	{
		sampleTicks = 0;
		previous = null;
		previousInventory = null;
		previousFacing = null;
		for(InventoryInterfaceRule rule : rules)
			rule.resetRuntime();
	}

	@Override
	public boolean canOpenGui()
	{
		updateTileForAll();
		return true;
	}

	@Override
	public TileEntity master()
	{
		return this;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.INVENTORY_INTERFACE;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		return NonNullList.create();
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return false;
	}
}
