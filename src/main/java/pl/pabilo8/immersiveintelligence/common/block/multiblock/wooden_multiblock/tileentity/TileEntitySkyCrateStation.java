package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityConveyorBelt;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Set;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.STRUCTURE_CATEGORY;

/**
 * Transfers containers between conveyors and Skycrate mounts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon
 * @updated 08.10.2026
 * @since 28.06.2019
 * @since 27.08.2025
 */
public class TileEntitySkyCrateStation extends TileEntityMultiblockIIConnectable<TileEntitySkyCrateStation>
		implements ISkyCrateConnector, IPlayerInteraction, IIIGuiMultiblockTile
{
	public static final int GEAR_SLOTS = 3;
	//Idle, holding, conveyor input, conveyor output, zipline input, zipline output.
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public int animation = 0;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public float progress = 0;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing side)
		{
			return side==transportDirection("rotary")?RotationSide.INPUT: RotationSide.NONE;
		}
	};
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public NonNullList<ItemStack> inventory = NonNullList.withSize(6, ItemStack.EMPTY);
	private float lastSyncedSpeed, lastSyncedTorque;
	private IItemHandler insertionHandler = new IItemHandler()
	{
		private void validateSlotIndex(int slot)
		{
			if(slot!=0)
				throw new IndexOutOfBoundsException("Invalid cargo slot: "+slot);
		}

		@Override
		public int getSlots()
		{
			return 1;
		}

		@Override
		@Nonnull
		public ItemStack getStackInSlot(int slot)
		{
			validateSlotIndex(slot);
			return inventory!=null?inventory.get(3): ItemStack.EMPTY;
		}

		@Override
		@Nonnull
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
		{
			validateSlotIndex(slot);
			if(!isValid()||!formed||world.isRemote||animation!=0||!inventory.get(3).isEmpty()||!isStackValid(3, stack))
				return stack;
			ItemStack remainder = stack.copy();
			remainder.shrink(1);
			if(!simulate)
			{
				inventory.set(3, stack.copy());
				inventory.get(3).setCount(1);
				syncTransportState(true);
			}
			return remainder;
		}

		@Override
		@Nonnull
		public ItemStack extractItem(int slot, int amount, boolean simulate)
		{
			validateSlotIndex(slot);
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot)
		{
			return 1;
		}
	};

	public TileEntitySkyCrateStation()
	{
		super(MultiblockSkyCrateStation.INSTANCE);
	}

	@Override
	protected void dummyCleanup()
	{
		inventory = null;
		rotation = null;
		insertionHandler = null;
	}

	@Override
	protected void onUpdate()
	{
		if(!formed)
			return;
		if(world.isRemote)
		{
			if(animation > 1)
				progress = Math.min(getAnimationLength(), progress+getAnimationSpeed());
			return;
		}
		if(!handleRotation())
			return;
		if(animation==0&&!inventory.get(3).isEmpty())
			setAnimation(2);
		else if(animation==1)
		{
			if(inventory.get(4).isEmpty()&&world.getTotalWorldTime()%4==0)
			{
				ItemStack extracted = extractMount();
				if(!extracted.isEmpty())
				{
					inventory.set(4, extracted);
					syncTransportState(true);
				}
			}
			if(shouldEject())
				setAnimation(3);
			else if(!inventory.get(3).isEmpty()&&!inventory.get(4).isEmpty())
				setAnimation(5);
		}
		if(animation > 1)
		{
			progress = Math.min(getAnimationLength(), progress+getAnimationSpeed());
			markDirty();
			if(progress >= getAnimationLength())
				finishAnimation();
		}
		if(world.getTotalWorldTime()%5==0&&(animation > 1||lastSyncedSpeed!=rotation.getRotationSpeed()||lastSyncedTorque!=rotation.getTorque()))
			syncTransportState(false);
	}

	private void finishAnimation()
	{
		switch(animation)
		{
			case 2:
				setAnimation(1);
				break;
			case 3:
				if(!returnMount())
					return;
				EnumFacing outputSide = transportDirection("cargo_output");
				BlockPos outputPos = getPOIPos(MultiblockPOI.ITEM_OUTPUT).offset(outputSide);
				if(!world.isBlockLoaded(outputPos))
					return;
				ItemStack remainder = Utils.insertStackIntoInventory(world.getTileEntity(outputPos), inventory.get(3), outputSide.getOpposite());
				if(!remainder.isEmpty())
					Utils.dropStackAtPos(world, outputPos, remainder, outputSide);
				inventory.set(3, ItemStack.EMPTY);
				setAnimation(0);
				break;
			case 4:
				if(!returnMount())
					return;
				setAnimation(3);
				break;
			case 5:
				if(launchSkycrate())
					setAnimation(0);
				break;
		}
	}

	private boolean returnMount()
	{
		BlockPos mountPos = getPOIPos("mount").offset(transportDirection("mount"));
		if(!world.isBlockLoaded(mountPos))
			return false;
		ItemStack remainder = Utils.insertStackIntoInventory(getMountInventory(), inventory.get(4), transportDirection("mount").getOpposite());
		if(!remainder.isEmpty())
			Utils.dropStackAtPos(world, mountPos, remainder, transportDirection("mount"));
		inventory.set(4, ItemStack.EMPTY);
		return true;
	}

	private boolean launchSkycrate()
	{
		if(inventory.get(3).isEmpty()||!(inventory.get(4).getItem() instanceof ISkycrateMount))
			return false;
		BlockPos wirePos = getPOIPos(MultiblockPOI.SKYCRATE_WIRE_MOUNT);
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, wirePos);
		if(connections!=null)
			for(Connection connection : connections)
				if(connection!=null&&connection.length > 0&&isMatchingCable(connection.cableType)
						&&world.isBlockLoaded(connection.start)&&world.isBlockLoaded(connection.end))
				{
					EntitySkyCrate skycrate = new EntitySkyCrate(world, connection, inventory.get(4), inventory.get(3), wirePos);
					if(skycrate.isDead||skycrate.connection==null||!world.spawnEntity(skycrate))
					{
						skycrate.setDead();
						return false;
					}
					inventory.set(3, ItemStack.EMPTY);
					inventory.set(4, ItemStack.EMPTY);
					return true;
				}
		return false;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		if(stack.isEmpty())
			return false;
		if(slot >= 0&&slot < GEAR_SLOTS)
			return stack.getItem() instanceof IMotorGear;
		return slot==3&&MinecartBlockHelper.blocks.keySet().stream().anyMatch(predicate -> predicate.test(stack));
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
	{
		TileEntitySkyCrateStation master = master();
		if(master!=null&&master.isValid()&&master.formed)
		{
			if(isPOI(MultiblockPOI.ROTARY_INPUT)&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&side==transportDirection("rotary"))
				return true;
			if(isPOI(MultiblockPOI.ITEM_INPUT)&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&side==transportDirection("cargo_input"))
				return true;
		}
		return super.hasCapability(capability, side);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
	{
		TileEntitySkyCrateStation master = master();
		if(master!=null&&master.isValid()&&master.formed)
		{
			if(isPOI(MultiblockPOI.ROTARY_INPUT)&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&side==transportDirection("rotary"))
				return (T)master.rotation;
			if(isPOI(MultiblockPOI.ITEM_INPUT)&&capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&side==transportDirection("cargo_input"))
				return (T)master.insertionHandler;
		}
		return super.getCapability(capability, side);
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntitySkyCrateStation master = master();
		if(!isPOI(MultiblockPOI.MISC_FLAGPOLE)||master==null||!master.isValid()||!master.formed)
			return false;
		if(master.inventory.get(5).isEmpty()&&heldItem.getItem()==Items.BANNER)
		{
			if(!world.isRemote)
			{
				master.inventory.set(5, heldItem.copy());
				master.inventory.get(5).setCount(1);
				heldItem.shrink(1);
				master.syncTransportState(true);
			}
			return true;
		}
		if(!master.inventory.get(5).isEmpty()&&Utils.isWirecutter(heldItem))
		{
			if(!world.isRemote)
			{
				ItemStack banner = master.inventory.get(5).copy();
				if(!player.inventory.addItemStackToInventory(banner))
					player.dropItem(banner, false);
				master.inventory.set(5, ItemStack.EMPTY);
				master.syncTransportState(true);
			}
			return true;
		}
		return false;
	}

	public int getAnimationLength()
	{
		switch(animation)
		{
			case 2:
				return SkyCrateStation.crateInTime;
			case 3:
				return SkyCrateStation.crateOutTime;
			case 4:
				return SkyCrateStation.inputTime;
			case 5:
				return SkyCrateStation.outputTime;
			default:
				return 0;
		}
	}

	@Override
	public boolean onSkycrateMeeting(EntitySkyCrate skycrate)
	{
		TileEntitySkyCrateStation master = master();
		if(world==null||world.isRemote||master==null||!master.isValid()||!master.formed)
			return false;
		if(master.animation!=0||!master.inventory.get(3).isEmpty()||!master.inventory.get(4).isEmpty())
			return true;
		master.inventory.set(3, skycrate.crate.copy());
		master.inventory.set(4, skycrate.mount.copy());
		master.setAnimation(4);
		skycrate.crate = ItemStack.EMPTY;
		skycrate.mount = ItemStack.EMPTY;
		skycrate.setDead();
		return false;
	}

	@Override
	public void replaceStructureBlock(@Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull ItemStack stack, int h, int l, int w)
	{
		super.replaceStructureBlock(pos, state, stack, h, l, w);
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof TileEntityConveyorBelt)
			((TileEntityConveyorBelt)tile).setFacing(transportDirection("cargo_output"));
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		TileEntitySkyCrateStation master = master();
		return master!=null&&master.isValid()&&master.inventory!=null?master.inventory: NonNullList.create();
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	public void doGraphicalUpdates(int slot)
	{
		TileEntitySkyCrateStation master = master();
		if(master!=null&&master.isValid()&&!world.isRemote)
			master.syncTransportState(true);
	}

	void syncTransportState(boolean payloadChanged)
	{
		markDirty();
		updateTileForEvent(payloadChanged?SyncEvents.TILE_CUSTOM2: SyncEvents.TILE_CUSTOM1);
		lastSyncedSpeed = rotation.getRotationSpeed();
		lastSyncedTorque = rotation.getTorque();
	}

	@Nonnull
	private EnumFacing transportDirection(String name)
	{
		return java.util.Objects.requireNonNull(getDirection(name));
	}

	private boolean handleRotation()
	{
		EnumFacing inputSide = transportDirection("rotary");
		BlockPos inputPos = getPOIPos(MultiblockPOI.ROTARY_INPUT).offset(inputSide);
		TileEntity neighbour = world.isBlockLoaded(inputPos)?world.getTileEntity(inputPos): null;
		IRotaryEnergy input = neighbour==null?null: neighbour.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY, inputSide.getOpposite());
		if(input!=null&&input.getSide(inputSide.getOpposite()).canOutput())
			rotation.handleRotation(input, inputSide.getOpposite());
		else
			rotation.grow(0, 0, 0.98f);
		return !IIRotaryUtils.destroyIfOverloaded(this, rotation, SkyCrateStation.speedBreaking, SkyCrateStation.torqueBreaking);
	}

	public float getAnimationSpeed()
	{
		return IIRotaryUtils.getEffectiveEnergy(rotation, SkyCrateStation.speedMin, SkyCrateStation.speedEfficient,
				SkyCrateStation.torqueMin, SkyCrateStation.torqueEfficient)*IIRotaryUtils.getGearEfficiency(inventory, 0, GEAR_SLOTS);
	}

	public float getAnimationProgress(float partialTicks)
	{
		int duration = getAnimationLength();
		return duration > 0?MathHelper.clamp((progress+partialTicks*getAnimationSpeed())/duration, 0, 1): 0;
	}

	private void setAnimation(int next)
	{
		animation = next;
		progress = 0;
		syncTransportState(true);
	}

	private boolean shouldEject()
	{
		EnumFacing side = transportDirection("redstone");
		return world.getRedstonePower(getPOIPos(MultiblockPOI.REDSTONE_INPUT).offset(side), side.getOpposite()) > 0;
	}

	private TileEntity getMountInventory()
	{
		BlockPos mountPos = getPOIPos("mount").offset(transportDirection("mount"));
		return world.isBlockLoaded(mountPos)?world.getTileEntity(mountPos): null;
	}

	private ItemStack extractMount()
	{
		TileEntity neighbour = getMountInventory();
		IItemHandler handler = neighbour==null?null: neighbour.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, transportDirection("mount").getOpposite());
		if(handler!=null)
			for(int slot = 0; slot < handler.getSlots(); slot++)
				if(handler.getStackInSlot(slot).getItem() instanceof ISkycrateMount)
				{
					ItemStack extracted = handler.extractItem(slot, 1, false);
					if(!extracted.isEmpty())
						return extracted;
				}
		return ItemStack.EMPTY;
	}

	@Override
	protected boolean isMatchingCable(WireType cableType)
	{
		return STRUCTURE_CATEGORY.equals(cableType.getCategory());
	}

	@Override
	public boolean canConnectCable(WireType cableType, TargetingInfo target, Vec3i offset)
	{
		Set<Connection> connections = world==null?null: ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
		return formed&&canConnect()&&isMatchingCable(cableType)&&(connections==null||connections.isEmpty())
				&&(limitType==null||limitType==cableType);
	}

	@Override
	public void connectCable(WireType cableType, TargetingInfo target, IImmersiveConnectable other)
	{
		super.connectCable(cableType, target, other);
		markDirty();
		if(!world.isRemote&&!(other instanceof ISkyCrateConnector))
		{
			Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, getPos());
			if(connections!=null)
				for(Connection connection : new ArrayList<>(connections))
					if(connection.cableType==cableType&&connection.end.equals(other.getConnectionMaster(cableType, target)))
						ImmersiveNetHandler.INSTANCE.removeConnectionAndDrop(connection, world, getPos());
		}
	}

	@Override
	public Vec3d getConnectionOffset(Connection connection)
	{
		return new Vec3d(.5, .625, .5);
	}

	@Override
	public Set<BlockPos> getIgnored(IImmersiveConnectable other)
	{
		return ImmutableSet.of(getPos(), getPos().offset(facing.getOpposite()));
	}

	@Override
	public void receiveMessageFromClient(NBTTagCompound message)
	{
		//Transport state is controlled by the server.
	}

	@Override
	public boolean canOpenGui()
	{
		TileEntitySkyCrateStation master = master();
		if(master==null||!master.isValid()||!master.formed)
			return false;
		if(!world.isRemote)
			master.updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SKYCRATE_STATION;
	}
}
