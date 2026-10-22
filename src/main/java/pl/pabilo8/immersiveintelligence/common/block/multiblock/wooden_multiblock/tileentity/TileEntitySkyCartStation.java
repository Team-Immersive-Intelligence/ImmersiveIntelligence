package pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.tileentity;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.wires.WireType;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartEmpty;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.rotary.*;
import pl.pabilo8.immersiveintelligence.api.utils.ISkyCrateConnector;
import pl.pabilo8.immersiveintelligence.api.utils.MinecartBlockHelper;
import pl.pabilo8.immersiveintelligence.api.utils.minecart.IMinecartBlockPickable;
import pl.pabilo8.immersiveintelligence.api.utils.tools.ISkycrateMount;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCartStation;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.SkyCrateStation;
import pl.pabilo8.immersiveintelligence.common.IIGUI;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.wooden_multiblock.multiblock.MultiblockSkyCartStation;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkyCrate;
import pl.pabilo8.immersiveintelligence.common.entity.EntitySkycrateInternal;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IIIGuiMultiblockTile;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIConnectable;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static blusunrize.immersiveengineering.api.energy.wires.WireType.STRUCTURE_CATEGORY;

/**
 * Transfers containers between minecarts and Skycrate mounts.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @author Avalon (avalon@iiteam.net)
 * @updated 08.10.2026
 * @since 28.06.2019
 * @since 22.10.2024
 */
public class TileEntitySkyCartStation extends TileEntityMultiblockIIConnectable<TileEntitySkyCartStation>
		implements ISkyCrateConnector, IPlayerInteraction, IIIGuiMultiblockTile
{
	public static final int GEAR_SLOTS = 3;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public boolean occupied = false;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public int animation = 0;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public float progress = 0;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public ItemStack banner = ItemStack.EMPTY;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public ItemStack crate = ItemStack.EMPTY;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public ItemStack mount = ItemStack.EMPTY;
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public NonNullList<ItemStack> inventory = NonNullList.withSize(GEAR_SLOTS, ItemStack.EMPTY);
	@SyncNBT(time = 0, events = {SyncEvents.TILE_CUSTOM1, SyncEvents.TILE_CUSTOM2, SyncEvents.TILE_GUI_OPENED})
	public RotaryStorage rotation = new RotaryStorage(0, 0)
	{
		@Override
		public RotationSide getSide(@Nullable EnumFacing side)
		{
			return side==transportDirection("rotary")?RotationSide.INPUT: RotationSide.NONE;
		}
	};
	public EntityMinecart cart;
	public EntitySkycrateInternal internalEntity;
	private int recoveryTicks;
	private float lastSyncedSpeed, lastSyncedTorque;

	public TileEntitySkyCartStation()
	{
		super(MultiblockSkyCartStation.INSTANCE);
	}

	@Override
	protected void dummyCleanup()
	{
		inventory = null;
		rotation = null;
		banner = crate = mount = ItemStack.EMPTY;
		cart = null;
		internalEntity = null;
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
		if(!handleRotation()||!recoverInternalEntity())
			return;
		if(cart!=null&&(!cart.isEntityAlive()||cart.getRidingEntity()!=internalEntity))
		{
			if(cart.isEntityAlive())
				cart.setCanUseRail(true);
			cart = null;
		}
		boolean hasCart = cart!=null;
		if(occupied!=hasCart)
		{
			occupied = hasCart;
			syncTransportState(false);
		}
		if(cart==null&&(animation==1||animation==2||animation==4))
			setAnimation(0);
		if(animation==0&&cart==null&&world.getTotalWorldTime()%4==0)
			captureCart();
		if(animation==1&&cart!=null)
		{
			if(shouldEject())
				setAnimation(3);
			else if(!crate.isEmpty()&&!mount.isEmpty()&&cart.getClass()==EntityMinecartEmpty.class)
				setAnimation(4);
			else if(crate.isEmpty()&&cart instanceof IMinecartBlockPickable)
			{
				if(mount.isEmpty()&&world.getTotalWorldTime()%4==0)
				{
					mount = extractMount();
					if(!mount.isEmpty())
						syncTransportState(true);
				}
				if(!mount.isEmpty())
				{
					Tuple<ItemStack, EntityMinecart> pickup = ((IMinecartBlockPickable)cart).getBlockForPickup();
					if(!pickup.getFirst().isEmpty()&&pickup.getSecond().isEntityAlive())
					{
						crate = pickup.getFirst();
						cart = pickup.getSecond();
						cart.startRiding(internalEntity, true);
						setAnimation(5);
					}
				}
			}
		}
		if(animation > 1)
		{
			progress = Math.min(getAnimationLength(), progress+getAnimationSpeed());
			markDirty();
			updateCartPosition();
			if(progress >= getAnimationLength())
				finishAnimation();
		}
		else
			updateCartPosition();
		if(world.getTotalWorldTime()%5==0&&(animation > 1||lastSyncedSpeed!=rotation.getRotationSpeed()||lastSyncedTorque!=rotation.getTorque()))
			syncTransportState(false);
	}

	private boolean recoverInternalEntity()
	{
		if(internalEntity!=null&&internalEntity.isEntityAlive())
			return true;
		internalEntity = null;
		List<EntitySkycrateInternal> helpers = world.getEntitiesWithinAABB(EntitySkycrateInternal.class,
				new AxisAlignedBB(getPos()).grow(1), helper -> helper.isEntityAlive()&&getPos().equals(helper.origin_pos));
		if(!helpers.isEmpty())
		{
			internalEntity = helpers.get(0);
			for(net.minecraft.entity.Entity passenger : internalEntity.getPassengers())
				if(passenger instanceof EntityMinecart&&passenger.isEntityAlive())
				{
					cart = (EntityMinecart)passenger;
					occupied = true;
					if(animation==0)
						setAnimation(2);
					break;
				}
			return true;
		}
		//Allow saved entities to load before the station creates a replacement.
		if(++recoveryTicks < 20)
			return false;
		EntitySkycrateInternal helper = new EntitySkycrateInternal(world, getPos());
		if(!world.spawnEntity(helper))
		{
			helper.setDead();
			return false;
		}
		internalEntity = helper;
		return true;
	}

	private void captureCart()
	{
		EnumFacing side = transportDirection("cart_io");
		Vec3i direction = side.getDirectionVec();
		List<EntityMinecart> carts = world.getEntitiesWithinAABB(EntityMinecart.class,
				new AxisAlignedBB(getPOIPos("cart_io").offset(side)),
				candidate -> candidate.isEntityAlive()&&!candidate.isRiding()
						&&candidate.motionX*direction.getX()+candidate.motionZ*direction.getZ() < -0.001);
		for(EntityMinecart candidate : carts)
			if(candidate.startRiding(internalEntity, true))
			{
				cart = candidate;
				occupied = true;
				cart.setCanUseRail(false);
				setAnimation(2);
				break;
			}
	}

	private void updateCartPosition()
	{
		if(cart==null||internalEntity==null)
			return;
		BlockPos cradle = getPOIPos("cart_cradle");
		Vec3i direction = transportDirection("cart_io").getDirectionVec();
		float offset = 0;
		if(animation==2)
			offset = 1-MathHelper.clamp(getAnimationProgress(0)*4, 0, 1);
		else if(animation==3)
			offset = MathHelper.clamp((getAnimationProgress(0)-0.45f)/0.15f, 0, 1);
		internalEntity.riding_x = cradle.getX()+.5f+direction.getX()*offset;
		internalEntity.riding_y = cradle.getY()+.125f;
		internalEntity.riding_z = cradle.getZ()+.5f+direction.getZ()*offset;
		internalEntity.updateValues();
		internalEntity.updatePassenger(cart);
		cart.rotationYaw = facing.getHorizontalAngle();
		cart.setCanUseRail(false);
		if(animation==3&&offset > .65f)
			dismountCart();
	}

	private void finishAnimation()
	{
		switch(animation)
		{
			case 2:
				setAnimation(1);
				break;
			case 3:
				if(crate.isEmpty()&&!returnMount())
					return;
				dismountCart();
				setAnimation(0);
				break;
			case 4:
				if(loadCart())
					setAnimation(3);
				break;
			case 5:
				if(launchSkycrate())
					setAnimation(3);
				break;
		}
	}

	private boolean loadCart()
	{
		if(cart==null||crate.isEmpty()||!MinecartBlockHelper.blocks.keySet().stream().anyMatch(predicate -> predicate.test(crate)))
			return false;
		BlockPos mountPos = getPOIPos("mount").offset(transportDirection("mount"));
		if(!world.isBlockLoaded(mountPos))
			return false;
		EntityMinecart loaded = MinecartBlockHelper.getMinecartFromBlockStack(crate, world);
		if(!(loaded instanceof IMinecartBlockPickable))
		{
			loaded.setDead();
			return false;
		}
		loaded.setPosition(cart.posX, cart.posY, cart.posZ);
		((IMinecartBlockPickable)loaded).setMinecartBlock(crate);
		if(!world.spawnEntity(loaded))
		{
			loaded.setDead();
			return false;
		}
		cart.dismountRidingEntity();
		cart.setDead();
		cart = loaded;
		cart.startRiding(internalEntity, true);
		crate = ItemStack.EMPTY;
		returnMount();
		return true;
	}

	private boolean returnMount()
	{
		BlockPos mountPos = getPOIPos("mount").offset(transportDirection("mount"));
		if(!world.isBlockLoaded(mountPos))
			return false;
		ItemStack remainder = Utils.insertStackIntoInventory(getMountInventory(), mount, transportDirection("mount").getOpposite());
		if(!remainder.isEmpty())
			Utils.dropStackAtPos(world, mountPos, remainder, transportDirection("mount"));
		mount = ItemStack.EMPTY;
		return true;
	}

	private boolean launchSkycrate()
	{
		if(crate.isEmpty()||!(mount.getItem() instanceof ISkycrateMount))
			return false;
		BlockPos wirePos = getPOIPos(MultiblockPOI.SKYCRATE_WIRE_MOUNT);
		Set<Connection> connections = ImmersiveNetHandler.INSTANCE.getConnections(world, wirePos);
		if(connections!=null)
			for(Connection connection : connections)
				if(connection!=null&&connection.length > 0&&isMatchingCable(connection.cableType)
						&&world.isBlockLoaded(connection.start)&&world.isBlockLoaded(connection.end))
				{
					EntitySkyCrate skycrate = new EntitySkyCrate(world, connection, mount, crate, wirePos);
					if(skycrate.isDead||skycrate.connection==null||!world.spawnEntity(skycrate))
					{
						skycrate.setDead();
						return false;
					}
					crate = mount = ItemStack.EMPTY;
					return true;
				}
		return false;
	}

	private void dismountCart()
	{
		if(cart==null)
			return;
		EnumFacing side = transportDirection("cart_io");
		BlockPos exit = getPOIPos("cart_io").offset(side, 2);
		cart.dismountRidingEntity();
		cart.setPosition(exit.getX()+.5, exit.getY(), exit.getZ()+.5);
		cart.setCanUseRail(true);
		Vec3i direction = side.getDirectionVec();
		cart.motionX = direction.getX();
		cart.motionZ = direction.getZ();
		cart = null;
		occupied = false;
		syncTransportState(false);
	}

	@Override
	public void disassemble()
	{
		TileEntitySkyCartStation master = master();
		if(world!=null&&!world.isRemote&&master!=null)
		{
			if(master.internalEntity==null)
			{
				List<EntitySkycrateInternal> helpers = world.getEntitiesWithinAABB(EntitySkycrateInternal.class,
						new AxisAlignedBB(master.getPos()).grow(1), helper -> master.getPos().equals(helper.origin_pos));
				if(!helpers.isEmpty())
					master.internalEntity = helpers.get(0);
			}
			if(master.internalEntity!=null)
			{
				for(net.minecraft.entity.Entity passenger : new ArrayList<>(master.internalEntity.getPassengers()))
				{
					passenger.dismountRidingEntity();
					if(passenger instanceof EntityMinecart)
						((EntityMinecart)passenger).setCanUseRail(true);
				}
				master.internalEntity.setDead();
			}
			if(master.cart!=null)
				master.cart.setCanUseRail(true);
		}
		super.disassemble();
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getDroppedItems()
	{
		TileEntitySkyCartStation master = master();
		NonNullList<ItemStack> drops = NonNullList.create();
		drops.addAll(getInventory());
		if(master!=null)
		{
			if(!master.crate.isEmpty())
				drops.add(master.crate);
			if(!master.mount.isEmpty())
				drops.add(master.mount);
			if(!master.banner.isEmpty())
				drops.add(master.banner);
		}
		return drops;
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return slot >= 0&&slot < GEAR_SLOTS&&stack.getItem() instanceof IMotorGear;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
	{
		TileEntitySkyCartStation master = master();
		if(master!=null&&master.isValid()&&master.formed&&isPOI(MultiblockPOI.ROTARY_INPUT)
				&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&side==transportDirection("rotary"))
			return true;
		return super.hasCapability(capability, side);
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
	{
		TileEntitySkyCartStation master = master();
		if(master!=null&&master.isValid()&&master.formed&&isPOI(MultiblockPOI.ROTARY_INPUT)
				&&capability==CapabilityRotaryEnergy.ROTARY_ENERGY&&side==transportDirection("rotary"))
			return (T)master.rotation;
		return super.getCapability(capability, side);
	}

	@Override
	public boolean interact(@Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull ItemStack heldItem, float hitX, float hitY, float hitZ)
	{
		TileEntitySkyCartStation master = master();
		if(!isPOI(MultiblockPOI.MISC_FLAGPOLE)||master==null||!master.isValid()||!master.formed)
			return false;
		if(master.banner.isEmpty()&&heldItem.getItem()==Items.BANNER)
		{
			if(!world.isRemote)
			{
				master.banner = heldItem.copy();
				master.banner.setCount(1);
				heldItem.shrink(1);
				master.syncTransportState(true);
			}
			return true;
		}
		if(!master.banner.isEmpty()&&Utils.isWirecutter(heldItem))
		{
			if(!world.isRemote)
			{
				ItemStack removed = master.banner.copy();
				if(!player.inventory.addItemStackToInventory(removed))
					player.dropItem(removed, false);
				master.banner = ItemStack.EMPTY;
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
				return SkyCartStation.minecartInTime;
			case 3:
				return SkyCartStation.minecartOutTime;
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
		TileEntitySkyCartStation master = master();
		if(world==null||world.isRemote||master==null||!master.isValid()||!master.formed)
			return false;
		if(!master.crate.isEmpty()||!master.mount.isEmpty()||master.animation==4||master.animation==5)
			return true;
		master.crate = skycrate.crate.copy();
		master.mount = skycrate.mount.copy();
		master.syncTransportState(true);
		skycrate.crate = skycrate.mount = ItemStack.EMPTY;
		skycrate.setDead();
		return false;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		TileEntitySkyCartStation master = master();
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
		TileEntitySkyCartStation master = master();
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
		TileEntitySkyCartStation master = master();
		if(master==null||!master.isValid()||!master.formed)
			return false;
		if(!world.isRemote)
			master.updateTileForEvent(SyncEvents.TILE_GUI_OPENED);
		return true;
	}

	@Override
	public IIGUI getGUI()
	{
		return IIGUI.SKYCART_STATION;
	}
}
