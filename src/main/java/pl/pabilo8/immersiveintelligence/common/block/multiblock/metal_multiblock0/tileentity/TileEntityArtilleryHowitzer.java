package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.api.ammo.utils.AmmoFactory;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IIDataHandlingUtils;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.client.fx.utils.ParticleRegistry;
import pl.pabilo8.immersiveintelligence.client.util.carversound.ConditionCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileManager.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoArtilleryHeavy;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IIMath;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Pabilo8 (pabilo@iiteam.net)
 * @since 28.06.2019
 */
public class TileEntityArtilleryHowitzer extends TileEntityMultiblockIIGeneric<TileEntityArtilleryHowitzer>
		implements IBooleanAnimatedPartsBlock, IConveyorAttachable, ILadderMultiblock, IManagedDamageResistantMultiblock, ITactileListener
{
	//--- Variables ---//

	//currently performed action
	@SyncNBT
	public ArtilleryHowitzerAction action = ArtilleryHowitzerAction.STOP;
	public ArrayList<HowitzerOrder> orderList = new ArrayList<>();
	@SyncNBT
	public MultiblockInteractablePart door, platform;
	@SyncNBT
	public MultiblockHealth health;
	//animation related variables
	@SyncNBT
	public int animationTime = 0, animationTimeMax = 0, shellConveyorTime = 0;
	@SyncNBT
	public float turretYaw = 0, turretPitch = 0, plannedYaw = 0, plannedPitch = 0;

	//shells loaded into the rack
	@SyncNBT
	public NonNullList<ItemStack> loadedShells;
	public IItemHandler inventoryHandler, insertionHandler;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityArtilleryHowitzer> soundRotationV, soundRotationH, soundDoorOpen, soundDoorClose;
	@SideOnly(Side.CLIENT)
	private SoundHandler sounds;

	private TactileManager tactileManager = null;

	public TileEntityArtilleryHowitzer()
	{
		super(MultiblockArtilleryHowitzer.INSTANCE);
		this.energyStorage = new FluxStorageAdvanced(ArtilleryHowitzer.energyCapacity);

		//shell queue: 0-5 in, 5-11 out
		inventory = NonNullList.withSize(12, ItemStack.EMPTY);
		loadedShells = NonNullList.withSize(4, ItemStack.EMPTY);
		inventoryHandler = new IEInventoryHandler(inventory.size(), this, 0, true, true);
		insertionHandler = new IEInventoryHandler(1, this, 0, true, false);
		health = new MultiblockHealth(this, ArtilleryHowitzer.baseHealth);
		door = new MultiblockInteractablePart(0, ArtilleryHowitzer.doorTime, 1);
		platform = new MultiblockInteractablePart(1, ArtilleryHowitzer.platformTime, 1);
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(world.isRemote)
			sounds = new SoundHandler(this);
	}

	@Override
	protected void dummyCleanup()
	{
		super.dummyCleanup();
		loadedShells = null;
		inventoryHandler = insertionHandler = null;
		door = platform = null;
		health = null;
	}

	@Override
	public void onUpdate()
	{
		//handles looped sounds
		if(world.isRemote)
			handleSounds();

		//Handle Tactile AMT on server side
		if(tactileManager==null)
			tactileManager = new TactileManager(multiblock, this);
		tactileManager.defaultize();

		boolean rs = getRedstoneAtPos(0);
		if(door.setState(rs)&&!world.isRemote)
			IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(door, this));

		//operate only if energy is sufficient
		if(energyStorage.getEnergyStored() < ArtilleryHowitzer.energyUsagePassive)
			return;
		//Update door and platform
		door.update();
		platform.update();
		tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationOpen, door.getProgress(0));
		tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationPlatform, platform.getProgress(0));


		//hide howitzer if door is closed
		if(!door.getState())
			action = ArtilleryHowitzerAction.HIDE;

		//shell conveyor action
		if(shellConveyorTime < ArtilleryHowitzer.conveyorTime)
			shellConveyorTime += 1;
		else
		{
			//push up
			//input 0->5
			for(int i = 5; i > 0; i--)
				if(inventoryHandler.getStackInSlot(i).isEmpty())
					inventory.set(i, inventoryHandler.extractItem(i-1, 1, false));
			//output 6->11
			for(int i = 11; i > 6; i--)
				if(inventoryHandler.getStackInSlot(i).isEmpty())
					inventory.set(i, inventoryHandler.extractItem(i-1, 1, false));

			//output shell into TileEntity or drop as item
			if(!world.isRemote&&!inventoryHandler.getStackInSlot(11).isEmpty())
			{
				BlockPos outPos = getBlockPosForPos(multiblock.getPointOfInterest("item_output"))
						.offset(facing.getOpposite())
						.offset(EnumFacing.UP);
				ItemStack casing = inventoryHandler.extractItem(11, 1, false);

				if(world.getTileEntity(outPos)!=null)
					casing = Utils.insertStackIntoInventory(world.getTileEntity(outPos), casing, facing);

				if(!casing.isEmpty())
					Utils.dropStackAtPos(world, outPos, casing);
			}

			shellConveyorTime = 0;

			if(!world.isRemote)
				forceTileUpdate();
		}

		/*if(!animation.matchesRequirements(this))
		{
			animation = ArtilleryHowitzerAnimation.STOP;
			animationTime = animationTimeMax = 0;
		}*/

		//animate gun tactiles
		animateGunTactiles();

		//S T O P
		if(action==ArtilleryHowitzerAction.STOP)
		{
			if(world.isRemote||orderList.isEmpty())
				return;
			HowitzerOrder newOrder = orderList.get(0);
			//only apply when valid

			if(newOrder.animation.isFulfilled(this)) //already fulfilled
				orderList.remove(0);
			else if(newOrder.animation.matchesRequirements(this)) //can be done
			{
				action = newOrder.animation;
				animationTime = 0;
				animationTimeMax = action.animationTime;
				plannedPitch = newOrder.pitch;
				plannedYaw = newOrder.yaw;

				forceTileUpdate();
				orderList.remove(0);
			}
			return;
		}


		boolean canContinue = true;
		switch(action.gunPosition)
		{
			case NEUTRAL: //doesn't need anything
				break;
			case LOADING: //platform lowered, gun yaw,pitch=0
			{
				platform.setState(false);
				plannedYaw = facing.getHorizontalAngle();
				plannedPitch = 0;
				canContinue = platform.isFullyClosed();
			}
			break;
			case ON_TARGET: //platform up, gun aimed
			{
				platform.setState(true);
				canContinue = platform.isFullyOpened()&&isAimed();
			}
			break;
		}

		//gun aiming
		turnToTarget();

		//passed above check
		if(canContinue)
		{
			if(animationTime < animationTimeMax)
				animationTime++;
			else if(action!=ArtilleryHowitzerAction.STOP)
			{
				action = ArtilleryHowitzerAction.STOP;
				animationTimeMax = 0;
				animationTime = 0;
				forceTileUpdate();
			}

			if(world.isRemote)
				handleAnimationSounds();

			//update tactile animation
			switch(action)
			{
				case AIM:
					break;
				case FIRE1:
				case FIRE2:
				case FIRE3:
				case FIRE4:
					tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationFire, (float)animationTime/animationTimeMax);
					break;
				case LOAD1:
				case LOAD2:
				case LOAD3:
				case LOAD4:
					tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationLoading, (float)animationTime/animationTimeMax);
					break;
				case UNLOAD1:
				case UNLOAD2:
				case UNLOAD3:
				case UNLOAD4:
					tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationUnloading, (float)animationTime/animationTimeMax);
					break;
			}

			//update animations
			if(animationTime==(int)(animationTimeMax*action.executeTime))
			{
				switch(action)
				{
					case AIM:
						break;
					case FIRE1:
					case FIRE2:
					case FIRE3:
					case FIRE4:
						fireGun(action.ordinal()-ArtilleryHowitzerAction.FIRE1.ordinal());
						break;
					case LOAD1:
					case LOAD2:
					case LOAD3:
					case LOAD4:
					{
						int slot = action.ordinal()-ArtilleryHowitzerAction.LOAD1.ordinal();
						loadedShells.set(slot, inventoryHandler.extractItem(5, 1, false));
					}
					break;
					case UNLOAD1:
					case UNLOAD2:
					case UNLOAD3:
					case UNLOAD4:
					{
						int slot = action.ordinal()-ArtilleryHowitzerAction.UNLOAD1.ordinal();
						inventory.set(6, loadedShells.get(slot).copy());
						loadedShells.set(slot, ItemStack.EMPTY);
					}
					break;
				}
			}
		}

		//in case an animation overrides the gun yaw and pitch or of an early return
		animateGunTactiles();
		energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePassive, false);
	}

	private void animateGunTactiles()
	{
		tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationPitch, turretPitch/105f);
		tactileManager.update(MultiblockArtilleryHowitzer.INSTANCE.animationYaw, ((720-turretYaw-facing.getHorizontalAngle()-90)%360)/360f);
	}

	private void fireGun(int i)
	{
		double yawFireAngle = Math.toRadians(-turretYaw > 180?180f+turretYaw: 180f-turretYaw);
		double yawPitchAngle = Math.toRadians(turretPitch+90);

		Vec3d gunEnd = IIMath.offsetPosDirection(3, yawFireAngle, yawPitchAngle);
		Vec3d gunVec = gunEnd.normalize();
		if(world.isRemote)
		{
			Vec3d gun_end_particle = gunVec.scale(4.5);
			ParticleRegistry.spawnGunfireFX(getGunPosition().add(gun_end_particle), gunVec, 8f);
		}

		IIPacketHandler.playRangedSound(world, gunEnd,
				IISounds.howitzerShot, SoundCategory.PLAYERS, 155, 1.5f,
				1.25f+(float)(Utils.RAND.nextGaussian()*0.02)
		);

		if(!world.isRemote)
		{
			new AmmoFactory<EntityAmmoArtilleryProjectile>(world)
					.setPosition(getGunPosition().add(gunEnd))
					.setDirection(gunVec.scale(1.5))
					.setIgnoredEntities(tactileManager!=null?tactileManager.getEntities(): null)
					.setIgnoredBlocks(getMultiblockBlocks())
					.setStack(loadedShells.get(i))
					.create();
		}

		loadedShells.set(i, IIContent.itemAmmoHeavyArtillery.getCasingStack(1));
	}

	private boolean isAimed()
	{
		return plannedYaw==turretYaw&&plannedPitch==turretPitch;
	}

	//--- NBT Handling ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		orderList.clear();
		for(NBTBase order : nbt.getTagList("order_queue", NBT.TAG_COMPOUND))
			if(order instanceof NBTTagCompound)
			{
				NBTTagCompound compound = (NBTTagCompound)order;
				orderList.add(
						new HowitzerOrder(ArtilleryHowitzerAction.values()[compound.getInteger("order")],
								compound.getFloat("pitch"),
								compound.getFloat("yaw")
						));
			}
	}

	@Override
	public void writeCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.writeCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		nbt.setTag("order_queue",
				orderList.stream()
						.map(order -> {
							NBTTagCompound tag = new NBTTagCompound();
							tag.setInteger("order", order.animation.ordinal());
							tag.setFloat("pitch", order.pitch);
							tag.setFloat("yaw", order.yaw);
							return tag;
						})
						.collect(NBTTagCollector.collect())
		);

	}

	//--- Utility Methods ---//

	private void turnToTarget()
	{
		//fixes a bug
		if(Double.isNaN(turretPitch))
			turretPitch = 0;
		if(Double.isNaN(turretYaw))
			turretYaw = 0;

		if(isAimed())
			return;

		//normalize to 0-360 degrees
		this.plannedYaw = MathHelper.wrapDegrees(plannedYaw);
		float p = plannedPitch-this.turretPitch;
		this.turretPitch += Math.signum(p)*MathHelper.clamp(Math.abs(p), 0, ArtilleryHowitzer.rotateSpeed);
		float y = MathHelper.wrapDegrees(360+plannedYaw-this.turretYaw);

		//if angle to target is smaller than rotation speed, set angle directly to target
		if(Math.abs(p) < ArtilleryHowitzer.rotateSpeed*0.5f)
			this.turretPitch = this.plannedPitch;
		if(Math.abs(y) < ArtilleryHowitzer.rotateSpeed*0.5f)
			this.turretYaw = this.plannedYaw;
		else
			this.turretYaw = MathHelper.wrapDegrees(this.turretYaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, ArtilleryHowitzer.rotateSpeed)));
	}

	//for handling single sounds
	@SideOnly(Side.CLIENT)
	private void handleAnimationSounds()
	{
		IISoundAnimation current = null;
		switch(action)
		{
			case FIRE1:
			case FIRE2:
			case FIRE3:
			case FIRE4:
				current = MultiblockArtilleryHowitzer.INSTANCE.firingSoundAnimation;
				break;
			case LOAD1:
			case LOAD2:
			case LOAD3:
			case LOAD4:
				current = MultiblockArtilleryHowitzer.INSTANCE.loadingSoundAnimation;
				break;
			case UNLOAD1:
			case UNLOAD2:
			case UNLOAD3:
			case UNLOAD4:
				current = MultiblockArtilleryHowitzer.INSTANCE.unloadingSoundAnimation;
				break;
			default:
				break;
		}

		if(current!=null)
			current.handleSounds(sounds, animationTime, .75f);
	}

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		//Load the sounds only once
		if(soundDoorOpen!=null&&soundDoorClose!=null&&soundRotationH!=null&&soundRotationV!=null)
			return;

		Supplier<Boolean> hasEnergy = () -> energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive;
		Supplier<Boolean> hasActiveEnergy = () -> energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive;
		Supplier<Boolean> platformOK = () -> !platform.isFullyClosed()&&!platform.isFullyOpened();
		Supplier<Boolean> yawOK = () -> turretYaw==MathHelper.wrapDegrees(plannedYaw);
		Supplier<Boolean> pitchOK = () -> turretPitch==plannedPitch;

		Vec3d posDoor = new Vec3d(getBlockPosForPos(525));

		soundDoorOpen = new ConditionCompoundSound<>(IISounds.slidingDoorOpenLoop, posDoor, this,
				te -> hasEnergy.get()&&door.getState()&&!door.isFullyOpened());

		soundDoorClose = new ConditionCompoundSound<>(IISounds.slidingDoorCloseLoop, posDoor, this,
				te -> hasEnergy.get()&&!door.getState()&&!door.isFullyClosed());

		soundRotationH = new ConditionCompoundSound<>(IISounds.turntableHeavyForwardLoop, posDoor, this,
				te -> hasActiveEnergy.get()&&platformOK.get()&&!yawOK.get());

		soundRotationV = new ConditionCompoundSound<>(IISounds.electricMotorHeavyForwardLoop, posDoor, this,
				te -> hasActiveEnergy.get()&&platformOK.get()&&!pitchOK.get());
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof ItemIIAmmoArtilleryHeavy;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Override
	protected int[] listAllPOI(MultiblockPOI poi)
	{
		switch(poi)
		{
			case ENERGY_INPUT:
				return getPOI("energy");
			case ITEM_INPUT:
				return getPOI("item_input");
			case ITEM_OUTPUT:
				return getPOI("item_output");
			case REDSTONE_INPUT:
				return getPOI("redstone");
			case DATA_INPUT:
				return getPOI("data");
			case MISC_DOOR:
				return getPOI("bunker_door");
			case MISC_WEAPON:
				return getPOI("gun");
		}
		return new int[0];
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		IIDataHandlingUtils.expectingNumericParam('y', packet, f -> plannedYaw = f);
		IIDataHandlingUtils.expectingNumericParam('p', packet,
				f -> plannedPitch = Math.abs(Math.min(Math.max(-Math.abs(f%360), -105), 0)));

		//Control
		if(animationTime==0)
		{
			IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
				switch(command)
				{
					//Batched Commands
					case "fire_all":
					{
						float vOffset = IIDataHandlingUtils.asFloat('v', packet);
						float hOffset = IIDataHandlingUtils.asFloat('h', packet);

						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE1, plannedPitch-vOffset, plannedYaw-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE2, plannedPitch+vOffset, plannedYaw+hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE3, plannedPitch-vOffset, plannedYaw-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE4, plannedPitch+vOffset, plannedYaw+hOffset));
					}
					break;
					case "load_all":
					{
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.LOAD1));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.LOAD2));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.LOAD3));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.LOAD4));
					}
					break;
					case "unload_all":
					{
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.UNLOAD1));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.UNLOAD2));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.UNLOAD3));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.UNLOAD4));
					}
					break;
					case "callback":
					{
						DataPacket callback = IIDataHandlingUtils.handleCallback(packet, var -> {
							switch(var)
							{
								case "get_energy":
									return new DataTypeInteger(energyStorage.getEnergyStored());
								case "get_state_progress":
									return new DataTypeFloat(animationTime/(float)animationTimeMax);
								case "get_yaw":
									return new DataTypeFloat(turretYaw);
								case "get_pitch":
									return new DataTypeFloat(turretPitch);
								case "get_planned_yaw":
									return new DataTypeFloat(plannedYaw);
								case "get_planned_pitch":
									return new DataTypeFloat(plannedPitch);
								case "get_platform_height":
									return new DataTypeFloat(platform.getProgress(0));

								case "get_door_opened":
									return new DataTypeBoolean(platform.isFullyOpened());
								case "get_door_closed":
									return new DataTypeBoolean(door.isFullyClosed());
								case "get_door_opening":
									return new DataTypeBoolean(door.isFullyOpened()==door.isFullyClosed());

								case "get_loaded_shell":
								{
									return new DataTypeItemStack(loadedShells.get(
											IIDataHandlingUtils.asInt('i', packet)
									));
								}
								case "get_stored_shell":
								{
									return new DataTypeItemStack(inventory.get(
											MathHelper.clamp(IIDataHandlingUtils.asInt('i', packet), 0, 5)
									));
								}
								case "get_state":
									return new DataTypeString(action.getName());
								case "get_state_num":
									return new DataTypeInteger(action.ordinal());
							}
							return null;
						});
						sendData(callback, EnumFacing.UP, 441);
					}
					break;
					//Single Commands
					default:
					{
						ArtilleryHowitzerAction anim = ArtilleryHowitzerAction.v(command, this);
						if(anim!=null)
						{
							if(anim==ArtilleryHowitzerAction.STOP)
								orderList.clear();

							if(anim.matchesRequirements(this))
							{
								action = anim;
								animationTime = 0;
								animationTimeMax = anim.animationTime;
								forceTileUpdate();
							}
						}
					}
					break;
				}
			});
		}

		forceTileUpdate();
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return multiblock.isPointOfInterest(this.pos, "item_input");
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&pos==410)
		{
			TileEntityArtilleryHowitzer master = master();
			if(master==null)
				return null;
			return (T)master.insertionHandler;
		}
		return super.getCapability(capability, facing);
	}

	@Override
	public void onAnimationChangeClient(boolean state, int part)
	{
		MultiblockInteractablePart.setStates(state, part, door, platform);
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		MultiblockInteractablePart changed = MultiblockInteractablePart.setStates(state, part, door, platform);
		if(changed!=null)
			IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(changed, this));
	}

	@Override
	public EnumFacing[] sigOutputDirections()
	{
		if(pos==2)
			return new EnumFacing[]{mirrored?facing.rotateYCCW(): facing.rotateY()};
		return new EnumFacing[0];
	}

	@Override
	public void onEntityCollision(@Nonnull World world, @Nonnull Entity entity)
	{
		if(!world.isRemote&&pos==410&&entity instanceof EntityItem)
		{
			//perform on master TE, check if insertion slot is empty
			TileEntityArtilleryHowitzer master = master();
			if(master==null||!master.inventory.get(0).isEmpty())
				return;

			//check if artillery shell
			EntityItem entityItem = (EntityItem)entity;
			if(entityItem.getItem().getItem()!=IIContent.itemAmmoHeavyArtillery)
				return;

			//insert copied stack to inventory
			ItemStack stack = master.inventoryHandler.insertItem(0, entityItem.getItem().copy(), false);
			if(stack.isEmpty())
				entityItem.setItem(ItemStack.EMPTY);
		}
	}

	private Vec3d getGunPosition()
	{
		BlockPos shoot_pos = getBlockPosForPos(multiblock.getPointOfInterest("gun")).offset(EnumFacing.UP, 1);
		return new Vec3d(shoot_pos.getX()+.5, shoot_pos.getY()+1.5, shoot_pos.getZ()+.5);
	}

	@Override
	public boolean isLadder()
	{
		return multiblock.isPointOfInterest(pos, "ladder");
	}

	@Override
	public float getExplosionResistance()
	{
		if(multiblock.isPointOfInterest(pos, "bunker_door"))
		{
			TileEntityArtilleryHowitzer master = master();
			return master!=null&&master.doorTime > 1?2000.0F: -1;
		}
		return -1;
	}

	@Override
	@Nullable
	public TactileManager getTactileHandler()
	{
		return tactileManager;
	}

	@Override
	public MultiblockHealth getHealthManager()
	{
		return health;
	}

	public enum ArtilleryHowitzerAction implements ISerializableEnum
	{
		STOP(false, false, GunPosition.NEUTRAL, t -> true, t -> false, 0, null, 1f), //stops current action
		HIDE(false, false, GunPosition.LOADING, t -> true, t -> t.platform.isFullyClosed(), 0, null, 1f), //makes howitzer go down

		LOAD1(true, false, GunPosition.LOADING, t -> t.loadedShells.get(0).isEmpty()&&!t.inventory.get(5).isEmpty(),
				t -> !t.loadedShells.get(0).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD2(true, false, GunPosition.LOADING, t -> t.loadedShells.get(1).isEmpty()&&!t.inventory.get(5).isEmpty(),
				t -> !t.loadedShells.get(1).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD3(true, false, GunPosition.LOADING, t -> t.loadedShells.get(2).isEmpty()&&!t.inventory.get(5).isEmpty(),
				t -> !t.loadedShells.get(2).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD4(true, false, GunPosition.LOADING, t -> t.loadedShells.get(3).isEmpty()&&!t.inventory.get(5).isEmpty(),
				t -> !t.loadedShells.get(3).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),

		UNLOAD1(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(0).isEmpty(),
				t -> t.loadedShells.get(0).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD2(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(1).isEmpty(),
				t -> t.loadedShells.get(1).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD3(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(2).isEmpty(),
				t -> t.loadedShells.get(2).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD4(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(3).isEmpty(),
				t -> t.loadedShells.get(3).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),

		FIRE1(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(0).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(0).isEmpty(),
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE2(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(1).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(1).isEmpty(),
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE3(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(2).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(2).isEmpty(),
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE4(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(3).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(3).isEmpty(),
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),

		AIM(true, true, GunPosition.ON_TARGET, t -> true, t -> false, 0, null, 1f);

		//whether the platform is required to be in a position
		//the platform position: true - up, false - down
		//whether requires yaw & pitch to be 0
		final boolean requiresPlatform, platformUp;
		final GunPosition gunPosition;
		@Nullable
		final String alias;
		final Predicate<TileEntityArtilleryHowitzer> requirements, fulfilled;
		final int animationTime;
		final float executeTime;

		ArtilleryHowitzerAction(boolean requiresPlatform, boolean platformUp, GunPosition gunPosition,
								Predicate<TileEntityArtilleryHowitzer> requirements,
								Predicate<TileEntityArtilleryHowitzer> fulfilled,
								int animationTime, @Nullable String alias, float executeTime)
		{
			this.requiresPlatform = requiresPlatform;
			this.platformUp = platformUp;
			this.gunPosition = gunPosition;
			this.requirements = requirements;
			this.fulfilled = fulfilled;
			this.animationTime = animationTime;
			this.alias = alias;
			this.executeTime = executeTime;
		}

		@Nullable
		public static ArtilleryHowitzerAction v(String s, TileEntityArtilleryHowitzer te)
		{
			String ss = s.toUpperCase();
			Optional<ArtilleryHowitzerAction> found = Arrays.stream(values())
					.filter(e -> e.alias!=null&&e.alias.toLowerCase().equals(s))
					.filter(a -> a.matchesRequirements(te))
					.findFirst();
			return found.orElseGet(() -> Arrays.stream(values())
					.filter(e -> e.name().equals(ss))
					.findFirst()
					.orElse(null));

		}

		public boolean matchesRequirements(TileEntityArtilleryHowitzer te)
		{
			return requirements.test(te);
		}

		public boolean isFulfilled(TileEntityArtilleryHowitzer te)
		{
			return fulfilled.test(te);
		}
	}

	public enum GunPosition
	{
		ON_TARGET,
		NEUTRAL,
		LOADING
	}

	public static class HowitzerOrder
	{
		final ArtilleryHowitzerAction animation;
		final float pitch, yaw;

		public HowitzerOrder(ArtilleryHowitzerAction animation, float pitch, float yaw)
		{
			this.animation = animation;
			this.pitch = pitch;
			this.yaw = yaw;
		}

		public HowitzerOrder(ArtilleryHowitzerAction animation)
		{
			this(animation, 0, 0);
		}
	}
}
