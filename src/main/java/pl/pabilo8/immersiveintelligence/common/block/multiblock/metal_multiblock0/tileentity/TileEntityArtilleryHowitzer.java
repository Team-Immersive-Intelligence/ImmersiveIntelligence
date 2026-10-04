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
import pl.pabilo8.immersiveintelligence.client.fx.utils.IIParticleUtils;
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
import pl.pabilo8.immersiveintelligence.common.util.IIReference;
import pl.pabilo8.immersiveintelligence.common.util.ISerializableEnum;
import pl.pabilo8.immersiveintelligence.common.util.ResLoc;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IManagedDamageResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.MultiblockHealth;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockInteractablePart;
import pl.pabilo8.immersiveintelligence.common.util.sound.IISoundAnimation;
import pl.pabilo8.immersiveintelligence.common.util.sound.SoundHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A strategic artillery gun multiblock.
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @updated 09.08.2026
 * @ii-approved 0.3.1
 * @since 28.06.2019
 */
public class TileEntityArtilleryHowitzer extends TileEntityMultiblockIIGeneric<TileEntityArtilleryHowitzer>
		implements IBooleanAnimatedPartsBlock, IConveyorAttachable, ILadderMultiblock, IManagedDamageResistantMultiblock, ITactileListener
{
	//--- Variables ---//

	//currently performed action
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ArtilleryHowitzerAction action = ArtilleryHowitzerAction.STOP;
	public ArrayList<HowitzerOrder> orderList = new ArrayList<>();
	@SyncNBT
	public MultiblockInteractablePart door, platform;
	@SyncNBT(events = SyncEvents.TILE_DAMAGED)

	public MultiblockHealth health;
	//animation related variables
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public int animationTime = 0, animationTimeMax = 0;
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public int shellConveyorTime = 0;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public GunAimCoordinate aim = new GunAimCoordinate();
	//The requested firing pitch is retained while the breech requires horizontal loading/ejection.
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public float firingPitch = -90f;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public boolean aimMoving = false, animationRunning = false, actionExecuted = false;
	@SyncNBT(events = SyncEvents.TILE_CUSTOM1)
	public ItemStack cyclingShell = ItemStack.EMPTY;
	private boolean clientActionExecuted = false;

	//Markers shared by all four firing animations: breech closed, then recoil finished before ejection.
	private static final float BREECH_LOADED = 0.56f, BREECH_EJECTION = 0.6125f;

	//shells loaded into the rack
	@SyncNBT(events = SyncEvents.TILE_RECIPE_CHANGED)
	public NonNullList<ItemStack> loadedShells;
	public IItemHandler inventoryHandler, insertionHandler;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound<TileEntityArtilleryHowitzer> soundRotationV, soundRotationH, soundDoorOpen, soundDoorClose, soundPlatformLower, soundPlatformRaise;
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
		aim.withAimSpeed(ArtilleryHowitzer.rotateSpeed, ArtilleryHowitzer.rotateSpeed)
				.withPitchLimit(-90f, 15f)
				.withCurrentAngles(0, -90f)
				.withAimSpeedMultiplier(0);
	}

	@Override
	public void onBeforeFirstTick()
	{
		super.onBeforeFirstTick();
		if(world.isRemote)
		{
			sounds = new SoundHandler(this);
			clientActionExecuted = actionExecuted;
		}
		else
		{
			tactileManager = new TactileManager(multiblock, this);
			tactileManager.defaultize();
		}
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
		boolean passivePower = energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive;
		boolean activePower = energyStorage.getEnergyStored() >=
				ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive;
		if(world.isRemote)
		{
			if(passivePower)
			{
				door.update();
				if(activePower)
					platform.update();
				shellConveyorTime = (shellConveyorTime+1)%(ArtilleryHowitzer.conveyorTime+1);
				aim.withAimSpeedMultiplier(activePower&&aimMoving?1f: 0f);
				if(activePower&&aimMoving)
					aim.update();
				if(activePower&&animationRunning&&animationTime < animationTimeMax)
				{
					animationTime++;
					if(action.alias!=null&&action.alias.equals("FIRE")
							&&(animationTime==(int)(animationTimeMax*BREECH_LOADED)
							||animationTime==(int)Math.ceil(animationTimeMax*BREECH_EJECTION)))
						animationRunning = false;
					handleAnimationSounds();
				}
			}
			else
				aim.withAimSpeedMultiplier(0);
			if(actionExecuted&&!clientActionExecuted&&action.alias!=null&&action.alias.equals("FIRE"))
			{
				Vec3d direction = aim.getTarget(0);
				ParticleRegistry.spawnParticle("ammo/gunfire_howitzer", getGunPosition().add(direction.scale(4.5)),
						Vec3d.ZERO, IIParticleUtils.toVector2f(direction));
			}
			clientActionExecuted = actionExecuted;
			handleSounds();
			return;
		}

		if(tactileManager==null)
			tactileManager = new TactileManager(multiblock, this);
		if(door.setState(getRedstoneAtPos(0)))
			IIPacketHandler.sendToClient(new MessageBooleanAnimatedPartsSync(door, this));

		boolean wasMoving = aimMoving, wasAnimating = animationRunning;
		float previousTargetYaw = aim.getTargetYaw(), previousTargetPitch = aim.getTargetPitch();
		aimMoving = animationRunning = false;
		aim.withCenterYaw(facing.getHorizontalAngle()).withAimSpeedMultiplier(0);
		if(passivePower)
		{
			door.update();
			if(activePower)
				platform.update();
			updateShellConveyor();
			updateAction(activePower);
			if(action!=ArtilleryHowitzerAction.STOP)
				energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePassive, false);
		}
		if(wasMoving||aimMoving||wasAnimating!=animationRunning
				||previousTargetYaw!=aim.getTargetYaw()||previousTargetPitch!=aim.getTargetPitch())
			syncForEvent(SyncEvents.TILE_CUSTOM1);
		animateGunTactiles();
	}

	private void updateShellConveyor()
	{
		//shell conveyor action
		if(shellConveyorTime < ArtilleryHowitzer.conveyorTime)
			shellConveyorTime += 1;
		else
		{
			boolean inventoryChanged = false;

			//push up
			//input 0->5
			for(int i = 5; i > 0; i--)
				if(inventoryHandler.getStackInSlot(i).isEmpty())
				{
					ItemStack moved = inventoryHandler.extractItem(i-1, 1, false);
					if(!moved.isEmpty())
					{
						inventory.set(i, moved);
						inventoryChanged = true;
					}
				}
			//output 6->11
			for(int i = 11; i > 6; i--)
				if(inventoryHandler.getStackInSlot(i).isEmpty())
				{
					ItemStack moved = inventoryHandler.extractItem(i-1, 1, false);
					if(!moved.isEmpty())
					{
						inventory.set(i, moved);
						inventoryChanged = true;
					}
				}

			//output shell into TileEntity or drop as item
			if(!world.isRemote&&!inventoryHandler.getStackInSlot(11).isEmpty())
			{
				BlockPos outPos = getBlockPosForPos(multiblock.getPointOfInterest("item_output"))
						.offset(facing.getOpposite())
						.offset(EnumFacing.UP);
				ItemStack casing = inventoryHandler.extractItem(11, 1, false);
				inventoryChanged = true;

				if(world.getTileEntity(outPos)!=null)
					casing = Utils.insertStackIntoInventory(world.getTileEntity(outPos), casing, facing);

				if(!casing.isEmpty())
					Utils.dropStackAtPos(world, outPos, casing);
			}

			shellConveyorTime = 0;

			if(inventoryChanged)
				syncForEvent(SyncEvents.TILE_RECIPE_CHANGED);
		}

	}

	private void updateAction(boolean canOperate)
	{
		if(!door.getState()&&action!=ArtilleryHowitzerAction.HIDE
				&&(action!=ArtilleryHowitzerAction.STOP||!platform.isFullyClosed()
				||aim.getPitch(0)!=-90f||aim.getRelativeYaw(0)!=0f))
		{
			action = ArtilleryHowitzerAction.HIDE;
			animationTime = animationTimeMax = 0;
			syncForEvent(SyncEvents.TILE_CUSTOM1);
		}
		if(action==ArtilleryHowitzerAction.STOP)
		{
			if(orderList.isEmpty()||!door.getState())
				return;
			HowitzerOrder order = orderList.get(0);
			if(order.animation.isFulfilled(this))
			{
				orderList.remove(0);
				markDirty();
				return;
			}
			if(!order.animation.matchesRequirements(this))
				return;
			orderList.remove(0);
			startAction(order.animation, order.yaw, order.pitch);
		}

		boolean firing = action.alias!=null&&action.alias.equals("FIRE");
		boolean loadingPosition = action.gunPosition==GunPosition.LOADING;
		int loadTick = (int)(animationTimeMax*BREECH_LOADED);
		int ejectionTick = (int)Math.ceil(animationTimeMax*BREECH_EJECTION);
		if(loadingPosition)
			aim.setTargetClamped(facing.getHorizontalAngle(), -90f);
		else if(firing)
		{
			aim.setTargetClamped(aim.getTargetYaw(), animationTime < loadTick||animationTime >= ejectionTick?0f: firingPitch);
		}

		//No aiming while the platform moves. Stow before descending, rise before targeting.
		boolean platformReady;
		if(loadingPosition)
		{
			if(aim.isAimed()||!platform.isFullyOpened())
				onAnimationChangeServer(false, 1);
			platformReady = platform.isFullyClosed()||platform.isFullyOpened();
		}
		else
		{
			if(door.isFullyOpened())
				onAnimationChangeServer(true, 1);
			platformReady = platform.isFullyOpened()&&door.isFullyOpened();
		}
		aimMoving = canOperate&&platformReady&&!aim.isAimed();
		aim.withAimSpeedMultiplier(aimMoving?1f: 0f);
		if(aimMoving)
			aim.update();

		boolean ready = canOperate&&aim.isAimed()&&(loadingPosition?platform.isFullyClosed(): platformReady);
		if(!ready)
			return;
		if(animationTime < animationTimeMax)
		{
			animationRunning = true;
			animationTime++;
			//Publish the exact freeze point before interpolation could run past it.
			if(firing&&(animationTime==loadTick||animationTime==ejectionTick))
			{
				animationRunning = false;
				syncForEvent(SyncEvents.TILE_CUSTOM1);
			}
		}
		else
		{
			action = ArtilleryHowitzerAction.STOP;
			animationTime = animationTimeMax = 0;
			syncForEvent(SyncEvents.TILE_CUSTOM1);
			return;
		}

		int executeTick = Math.max(1, (int)Math.ceil(animationTimeMax*action.executeTime));
		//Even custom shot timings must remain between breech loading and casing ejection.
		if(firing)
			executeTick = MathHelper.clamp(executeTick, loadTick+1, ejectionTick-1);
		if(!actionExecuted&&animationTime >= executeTick)
		{
			switch(action)
			{
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
					loadedShells.set(action.ordinal()-ArtilleryHowitzerAction.LOAD1.ordinal(), inventoryHandler.extractItem(5, 1, false));
					break;
				case UNLOAD1:
				case UNLOAD2:
				case UNLOAD3:
				case UNLOAD4:
					int slot = action.ordinal()-ArtilleryHowitzerAction.UNLOAD1.ordinal();
					inventory.set(6, loadedShells.get(slot).copy());
					loadedShells.set(slot, ItemStack.EMPTY);
					break;
				default:
					return;
			}
			actionExecuted = true;
			syncForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			syncForEvent(SyncEvents.TILE_CUSTOM1);
		}
	}

	private void startAction(ArtilleryHowitzerAction requested, float yaw, float pitch)
	{
		action = requested;
		animationTime = 0;
		animationTimeMax = requested.animationTime;
		firingPitch = aim.clampPitchToRange(pitch);
		aim.setTargetClamped(yaw, firingPitch);
		aim.withAimSpeedMultiplier(0);
		aimMoving = animationRunning = actionExecuted = false;
		cyclingShell = switch(requested)
		{
			case FIRE1, FIRE2, FIRE3, FIRE4 -> loadedShells.get(requested.ordinal()-ArtilleryHowitzerAction.FIRE1.ordinal()).copy();
			case UNLOAD1, UNLOAD2, UNLOAD3, UNLOAD4 -> loadedShells.get(requested.ordinal()-ArtilleryHowitzerAction.UNLOAD1.ordinal()).copy();
			case LOAD1, LOAD2, LOAD3, LOAD4 -> inventory.get(5).copy();
			default -> ItemStack.EMPTY;
		};
		syncForEvent(SyncEvents.TILE_CUSTOM1);
	}

	public float getAnimationProgress(float partialTicks)
	{
		return animationTimeMax <= 0?0f: MathHelper.clamp(
				(animationTime+(animationRunning?partialTicks: 0f))/animationTimeMax, 0f, 1f);
	}

	private void syncForEvent(SyncEvents event)
	{
		if(world.isRemote)
			return;

		markDirty();
		updateTileForEvent(event);
	}

	private void animateGunTactiles()
	{
		ResLoc actionAnimation = null;
		if(action.alias!=null)
		{
			int slot = switch(action.alias)
			{
				case "FIRE" -> action.ordinal()-ArtilleryHowitzerAction.FIRE1.ordinal();
				case "LOAD" -> action.ordinal()-ArtilleryHowitzerAction.LOAD1.ordinal();
				default -> action.ordinal()-ArtilleryHowitzerAction.UNLOAD1.ordinal();
			};
			actionAnimation = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_",
					action.alias.equals("FIRE")?"fire": action.alias.equals("LOAD")?"loading": "unloading", Integer.toString(slot+1));
		}
		tactileManager.update(new ResLoc[]{MultiblockArtilleryHowitzer.INSTANCE.animationOpen,
						MultiblockArtilleryHowitzer.INSTANCE.animationPlatform, actionAnimation,
						MultiblockArtilleryHowitzer.INSTANCE.animationPitch, MultiblockArtilleryHowitzer.INSTANCE.animationYaw},
				new float[]{door.getProgress(0), platform.getProgress(0), getAnimationProgress(0),
						(aim.getPitch(0)+90f)/105f,
						((720-aim.getYaw(0)-facing.getHorizontalAngle()-90)%360)/360f});
	}

	private void fireGun(int slot)
	{
		//The same unit direction and default ammo velocity used by AmmoBallisticsCache/Emplacement.
		Vec3d direction = aim.getTarget(0);
		Vec3d muzzle = getGunPosition().add(direction.scale(3));
		new AmmoFactory<EntityAmmoArtilleryProjectile>(world)
				.setPosition(muzzle)
				.setDirection(direction)
				.setIgnoredEntities(tactileManager.getEntities())
				.setIgnoredBlocks(getMultiblockBlocks())
				.setStack(loadedShells.get(slot))
				.create();
		IIPacketHandler.playRangedSound(world, muzzle, IISounds.howitzerShot, SoundCategory.PLAYERS,
				155, 1.5f, 1f+(float)(Utils.RAND.nextGaussian()*0.02));
		loadedShells.set(slot, IIContent.itemAmmoHeavyArtillery.getCasingStack(1));
	}

	//--- NBT Handling ---//

	@Override
	public void readCustomNBT(@Nonnull NBTTagCompound nbt, boolean descPacket)
	{
		super.readCustomNBT(nbt, descPacket);

		if(isDummy())
			return;

		if(!nbt.hasKey("aim")&&nbt.hasKey("turretYaw"))
		{
			aim.withCurrentAngles(nbt.getFloat("turretYaw"), nbt.getFloat("turretPitch")-90f);
			firingPitch = nbt.getFloat("plannedPitch")-90f;
			aim.setTargetClamped(nbt.getFloat("plannedYaw"), firingPitch);
			actionExecuted = action.alias!=null&&action.alias.equals("FIRE")
					&&animationTime >= (int)(animationTimeMax*action.executeTime);
			if(action.alias!=null)
			{
				int slot = action.ordinal()-(action.alias.equals("FIRE")?ArtilleryHowitzerAction.FIRE1.ordinal():
						action.alias.equals("LOAD")?ArtilleryHowitzerAction.LOAD1.ordinal(): ArtilleryHowitzerAction.UNLOAD1.ordinal());
				cyclingShell = action.alias.equals("LOAD")?inventory.get(5).copy(): loadedShells.get(slot).copy();
			}
		}
		if(!IIMath.isNumberFinite(aim.getYaw(0), aim.getPitch(0), aim.getTargetYaw(), aim.getTargetPitch(), firingPitch))
		{
			aim.withCurrentAngles(facing.getHorizontalAngle(), -90f);
			firingPitch = -90f;
		}
		aim.withAimSpeedMultiplier(aimMoving?1f: 0f);
		if(!nbt.hasKey("order_queue", NBT.TAG_LIST))
			return;
		orderList.clear();
		for(NBTBase order : nbt.getTagList("order_queue", NBT.TAG_COMPOUND))
			if(order instanceof NBTTagCompound)
			{
				NBTTagCompound compound = (NBTTagCompound)order;
				orderList.add(
						new HowitzerOrder(ArtilleryHowitzerAction.values()[compound.getInteger("order")],
								compound.getFloat("pitch")-(nbt.getInteger("aim_format") >= 1?0f: 90f),
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

		nbt.setInteger("aim_format", 1);
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
		Vec3d posDoor = getGunPosition();
		soundDoorOpen = ConditionCompoundSound.updateLoopSound(soundDoorOpen, IISounds.slidingDoorOpenLoop,
				posDoor, this, TileEntityArtilleryHowitzer::isDoorOpening);

		soundDoorClose = ConditionCompoundSound.updateLoopSound(soundDoorClose, IISounds.slidingDoorCloseLoop,
				posDoor.addVector(1, 0, 0), this, TileEntityArtilleryHowitzer::isDoorClosing);

		soundRotationH = ConditionCompoundSound.updateLoopSound(soundRotationH, IISounds.turntableHeavyForwardLoop,
				posDoor.addVector(0, 0, 1), this, TileEntityArtilleryHowitzer::isTurningYaw);

		soundRotationV = ConditionCompoundSound.updateLoopSound(soundRotationV, IISounds.electricMotorHeavyForwardLoop, posDoor.
				addVector(-1, 0, 0), this, TileEntityArtilleryHowitzer::isTurningPitch);

		soundPlatformLower = ConditionCompoundSound.updateLoopSound(soundPlatformLower, IISounds.platformLowerLoop,
				posDoor, this, TileEntityArtilleryHowitzer::isPlatformLowering);

		soundPlatformRaise = ConditionCompoundSound.updateLoopSound(soundPlatformRaise, IISounds.platformRaiseLoop,
				posDoor, this, TileEntityArtilleryHowitzer::isPlatformRaising);
	}

	public boolean isDoorOpening()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive
				&&door.getState()&&!door.isFullyOpened();
	}

	private boolean isDoorClosing()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive
				&&!door.getState()&&!door.isFullyClosed();
	}

	private boolean isTurningYaw()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >=
				ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive&&aimMoving&&Math.abs(MathHelper.wrapDegrees(aim.getTargetYaw()-aim.getYaw(0))) > 0.05f;
	}

	private boolean isTurningPitch()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >=
				ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive&&aimMoving&&Math.abs(aim.getTargetPitch()-aim.getPitch(0)) > 0.05f;
	}

	private boolean isPlatformLowering()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive
				&&platform.getState()&&!platform.isFullyClosed();
	}

	private boolean isPlatformRaising()
	{
		return !tileEntityInvalid&&energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive
				&&platform.getState()&&!platform.isFullyOpened();
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
	public void receiveData(DataPacket packet, int pos)
	{
		float[] requestedAngles = {aim.getTargetYaw(), firingPitch};
		//Ballistic Computer emits zenith angle p = 90 - elevation; GunAimCoordinate uses -elevation.
		IIDataHandlingUtils.expectingNumericParam('y', packet, f -> {
			if(Float.isFinite(f)) requestedAngles[0] = MathHelper.wrapDegrees(f);
		});
		IIDataHandlingUtils.expectingNumericParam('p', packet, f -> {
			if(Float.isFinite(f)) requestedAngles[1] = MathHelper.clamp(Math.abs(f%360), 0f, 105f)-90f;
		});

		//Control
		if(animationTime==0||IIDataHandlingUtils.isCallbackPacket(packet))
		{
			IIDataHandlingUtils.expectingStringParam('c', packet, command -> {
				switch(command)
				{
					//Batched Commands
					case "fire_all":
					{
						float vOffset = IIDataHandlingUtils.asFloat('v', packet);
						float hOffset = IIDataHandlingUtils.asFloat('h', packet);

						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE1, requestedAngles[1]-vOffset, requestedAngles[0]-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE2, requestedAngles[1]+vOffset, requestedAngles[0]+hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE3, requestedAngles[1]-vOffset, requestedAngles[0]-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAction.FIRE4, requestedAngles[1]+vOffset, requestedAngles[0]+hOffset));
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
									return new DataTypeFloat(getAnimationProgress(0));
								case "get_yaw":
									return new DataTypeFloat(aim.getYaw(0));
								case "get_pitch":
									return new DataTypeFloat(aim.getPitch(0)+90f);
								case "get_planned_yaw":
									return new DataTypeFloat(aim.getTargetYaw());
								case "get_planned_pitch":
									return new DataTypeFloat(firingPitch+90f);
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
											MathHelper.clamp(IIDataHandlingUtils.asInt('i', packet), 0, 3)
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
						if(callback!=null)
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
								startAction(anim, requestedAngles[0], requestedAngles[1]);
							}
						}
					}
					break;
				}
			});
		}

		if(action==ArtilleryHowitzerAction.STOP)
		{
			firingPitch = requestedAngles[1];
			aim.setTargetClamped(requestedAngles[0], firingPitch);
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
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY&&isPOI("item_input"))
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
		if(!world.isRemote&&isPOI("item_input")&&entity instanceof EntityItem)
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
			{
				entityItem.setItem(ItemStack.EMPTY);
				master.syncForEvent(SyncEvents.TILE_RECIPE_CHANGED);
			}
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
			return master!=null&&master.door.isFullyClosed()?2000.0F: 0;
		}
		return 0;
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

	@Override
	public boolean damageHealth(float damage)
	{
		float previousHealth = health.getHealth();
		boolean destroyed = health.damageHealth(damage);
		if(Float.compare(previousHealth, health.getHealth())!=0)
			syncForEvent(SyncEvents.TILE_DAMAGED);
		return destroyed;
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

		UNLOAD1(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(0).isEmpty()&&t.inventory.get(6).isEmpty(),
				t -> t.loadedShells.get(0).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD2(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(1).isEmpty()&&t.inventory.get(6).isEmpty(),
				t -> t.loadedShells.get(1).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD3(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(2).isEmpty()&&t.inventory.get(6).isEmpty(),
				t -> t.loadedShells.get(2).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD4(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(3).isEmpty()&&t.inventory.get(6).isEmpty(),
				t -> t.loadedShells.get(3).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),

		FIRE1(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(0).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(0).getItem()!=IIContent.itemAmmoHeavyArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE2(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(1).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(1).getItem()!=IIContent.itemAmmoHeavyArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE3(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(2).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(2).getItem()!=IIContent.itemAmmoHeavyArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE4(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(3).getItem()==IIContent.itemAmmoHeavyArtillery,
				t -> t.loadedShells.get(3).getItem()!=IIContent.itemAmmoHeavyArtillery,
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
