package pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.tileentity;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
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
import pl.pabilo8.immersiveintelligence.client.util.carversound.TimedCompoundSound;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.block.multiblock.metal_multiblock0.multiblock.MultiblockArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.common.entity.ammo.types.EntityAmmoArtilleryProjectile;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler;
import pl.pabilo8.immersiveintelligence.common.entity.tactile.TactileHandler.ITactileListener;
import pl.pabilo8.immersiveintelligence.common.item.ammo.artillery.ItemIIAmmoArtilleryHeavy;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.MultiSound;
import pl.pabilo8.immersiveintelligence.common.util.*;
import pl.pabilo8.immersiveintelligence.common.util.lambda.NBTTagCollector;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.IExplosionResistantMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.util.multiblock.util.MultiblockPOI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Pabilo8
 * @since 28-06-2019
 */
public class TileEntityArtilleryHowitzer extends TileEntityMultiblockIIGeneric<TileEntityArtilleryHowitzer>
		implements IBooleanAnimatedPartsBlock, IConveyorAttachable, ILadderMultiblock, IExplosionResistantMultiblock, ITactileListener
{
	//Sound Animations
	private static final IISoundAnimation loadingSoundAnimation;
	private static final IISoundAnimation unloadingSoundAnimation;
	private static final IISoundAnimation firingSoundAnimation;

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound soundRotationV, soundRotationH, soundDoorOpen, soundDoorClose;
	//Tactile Animations
	private static final ResLoc animationPlatform, animationOpen, animationFire, animationLoading, animationUnloading, animationPitch, animationYaw;

	static
	{
		//Load tactile animations
		animationOpen = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_door");
		animationPlatform = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_platform");
		animationLoading = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_loading1");
		animationUnloading = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_unloading1");
		animationFire = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_fire1");
		animationPitch = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_tactile_pitch");
		animationYaw = ResLoc.of(IIReference.RES_II, "artillery_howitzer/artillery_howitzer_tactile_yaw");

		//load sound animations
		loadingSoundAnimation = new IISoundAnimation(18);
		unloadingSoundAnimation = new IISoundAnimation(18);
		firingSoundAnimation = new IISoundAnimation(15.96);

		//TODO: 21.04.2023 do it properly
		loadingSoundAnimation
				.withRepeatedSound(0.64, 3.6, IISounds.slidingDoorOpenM)

				.withSound(4.36, IISounds.metalLockerOpen)
				.withRepeatedSound(5.0, 11.28, IISounds.chainM)

				.withSound(8.4, IISounds.artilleryShellPlace)
				.withSound(9, SoundEvents.ENTITY_MINECART_RIDING)
				.withSound(8.92, IISounds.howitzerPlatformStart)

				.withSound(11.52, IISounds.metalLockerClose)

				.withRepeatedSound(11.96, 13.24, IISounds.inserterYawM)
				.withSound(13.96, IISounds.artilleryShellPick)
				.withSound(14, SoundEvents.ENTITY_MINECART_RIDING)
				.withRepeatedSound(14.24, 14.96, IISounds.inserterPitchM)
				.withRepeatedSound(15.12, 15.90, IISounds.inserterYawM)
				.withSound(16.36, IISounds.artilleryShellPlace)
				.withRepeatedSound(15.12, 16.68, IISounds.inserterPitchM)

				.withRepeatedSound(17.0, 18.0, IISounds.slidingDoorCloseM)
				.compile(ArtilleryHowitzer.loadRackTime);

		firingSoundAnimation
				/*.withSound(0.0, IISounds.metalBreadboxOpen)
				.withSound(0.52, IISounds.inserterForward)
				.withSound(1.0, IISounds.inserterForward)
				.withSound(1.32, IISounds.inserterForward)
				.withSound(1.76, IISounds.inserterForward)
				.withSound(2.24, IISounds.inserterForward)
				.withSound(2.28, IISounds.inserterForward)
				.withSound(2.8, IISounds.inserterForward)
				.withSound(3.24, IISounds.inserterForward)
				.withSound(3.72, IISounds.inserterForward)
				.withSound(4.16, IISounds.inserterForward)
				.withSound(4.4, IISounds.metalBreadboxClose)
				.withSound(4.56, IISounds.inserterForward)
				.withSound(4.96, IISounds.artilleryShellPick)
				.withSound(5.04, IISounds.inserterForward)
				.withSound(5.48, IISounds.inserterForward)
				.withSound(5.92, IISounds.inserterForward)
				.withSound(6.36, IISounds.inserterForward)
				.withSound(6.8, IISounds.inserterForward)
				.withSound(7.08, IISounds.metalBreadboxClose)
				.withSound(7.72, IISounds.artilleryShellPut)
				.withSound(7.8, IISounds.inserterForward)
				.withSound(8.24, IISounds.inserterForward)
				.withSound(8.44, IISounds.inserterForward)
				.withSound(8.48, IISounds.inserterForward)
				.withSound(9.0, IISounds.inserterForward)
				//fire!
				.withSound(9.52, IISounds.inserterForward)
				.withSound(10.0, IISounds.inserterForward)
				.withSound(10.36, IISounds.inserterForward)
				.withSound(10.64, IISounds.inserterForward)
				.withSound(10.68, IISounds.inserterForward)
				.withSound(11.2, IISounds.inserterForward)
				.withSound(11.68, IISounds.inserterForward)
				.withSound(12.16, IISounds.inserterForward)
				.withSound(12.24, IISounds.artilleryShellPick)
				.withSound(12.64, IISounds.inserterBackward)
				.withSound(13.08, IISounds.inserterBackward)
				.withSound(13.52, IISounds.inserterBackward)
				.withSound(13.96, IISounds.inserterForward)
				.withSound(14.12, IISounds.metalBreadboxClose)
				.withSound(14.48, IISounds.artilleryShellPut)
				.withSound(14.76, IISounds.inserterBackward)
				.withSound(15.24, IISounds.inserterBackward)
				.withSound(15.6, IISounds.inserterBackward)
				.withSound(15.72, IISounds.metalBreadboxOpen)*/
				.compile(ArtilleryHowitzer.gunFireTime);

		unloadingSoundAnimation
				.withRepeatedSound(0.04, 1.24, IISounds.inserterYawM)

				.withRepeatedSound(0.36, 1, IISounds.slidingDoorCloseM)

				.withSound(1.36, IISounds.artilleryShellPick)
				.withSound(2, SoundEvents.ENTITY_MINECART_RIDING)

				.withRepeatedSound(1.44, 3.8, IISounds.inserterYawM)
				.withSound(4.0, IISounds.artilleryShellPlace)
				.withSound(4.5, SoundEvents.ENTITY_MINECART_RIDING)


				.withRepeatedSound(4.28, 5.8, IISounds.inserterYawM)

				.withSound(5.92, IISounds.metalLockerOpen)
				.withRepeatedSound(6.6, 9.28, IISounds.chainM)
				.withSound(8, IISounds.artilleryShellPick)
				.withSound(12.44, IISounds.metalLockerClose)

				.withRepeatedSound(8.08, 10.5, IISounds.slidingDoorCloseM)
				.compile(ArtilleryHowitzer.loadRackTime);
	}

	//--- Variables ---//

	public ArrayList<HowitzerOrder> orderList = new ArrayList<>();
	//currently performed action
	public ArtilleryHowitzerAnimation animation = ArtilleryHowitzerAnimation.STOP;
	//animation related variables
	public int animationTime = 0, animationTimeMax = 0, shellConveyorTime = 0;
	public boolean isDoorOpened = false, platformPosition = false;

	public int platformTime = 0, doorTime = 0;
	public float turretYaw = 0, turretPitch = 0, plannedYaw = 0, plannedPitch = 0;

	//shells loaded into the rack
	public NonNullList<ItemStack> loadedShells;
	public IItemHandler inventoryHandler, insertionHandler;

	@SideOnly(Side.CLIENT)
	private List<TimedCompoundSound> soundsList;
	private TactileHandler tactileHandler = null;


	public TileEntityArtilleryHowitzer()
	{
		super(MultiblockArtilleryHowitzer.INSTANCE);

		this.energyStorage = new FluxStorageAdvanced(ArtilleryHowitzer.energyCapacity);

		//shell queue: 0-5 in, 5-11 out
		inventory = NonNullList.withSize(12, ItemStack.EMPTY);
		loadedShells = NonNullList.withSize(4, ItemStack.EMPTY);
		inventoryHandler = new IEInventoryHandler(inventory.size(), this, 0, true, true);
		insertionHandler = new IEInventoryHandler(1, this, 0, true, false);
	}

	@Override
	public void onUpdate()
	{
		//handles looped sounds
		if(world.isRemote)
			handleSounds();

		//Handle Tactile AMT on server side
		if(tactileHandler==null)
			tactileHandler = new TactileHandler(multiblock, this);
		tactileHandler.defaultize();


		boolean rs = getRedstoneAtPos(0);
		if(isDoorOpened^rs)
		{
			isDoorOpened = rs;
			if(!world.isRemote)
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, this.getPos()),
						IIPacketHandler.targetPointFromTile(this, 48));
		}

		//operate only if energy is sufficient
		if(energyStorage.getEnergyStored() < ArtilleryHowitzer.energyUsagePassive)
			return;
		energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePassive, false);

		//howitzer door movement
		doorTime = MathHelper.clamp(doorTime+(isDoorOpened?1: -2), 0, ArtilleryHowitzer.doorTime);
		tactileHandler.update(animationOpen, (float)doorTime/ArtilleryHowitzer.doorTime);

		//howitzer platform movement
		platformTime = MathHelper.clamp(platformTime+(platformPosition?1: -1), 0, ArtilleryHowitzer.platformTime);
		tactileHandler.update(animationPlatform, (float)platformTime/ArtilleryHowitzer.platformTime);

		//hide howitzer if door is closed
		if(!isDoorOpened)
			animation = ArtilleryHowitzerAnimation.HIDE;

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
		if(animation==ArtilleryHowitzerAnimation.STOP)
		{
			if(world.isRemote||orderList.isEmpty())
				return;
			HowitzerOrder newOrder = orderList.get(0);
			//only apply when valid

			if(newOrder.animation.isFulfilled(this)) //already fulfilled
				orderList.remove(0);
			else if(newOrder.animation.matchesRequirements(this)) //can be done
			{
				animation = newOrder.animation;
				animationTime = 0;
				animationTimeMax = animation.animationTime;
				plannedPitch = newOrder.pitch;
				plannedYaw = newOrder.yaw;

				forceTileUpdate();
				orderList.remove(0);
			}
			return;
		}


		boolean canContinue = true;
		switch(animation.gunPosition)
		{
			case NEUTRAL: //doesn't need anything
				break;
			case LOADING: //platform lowered, gun yaw,pitch=0
			{
				platformPosition = false;
				plannedYaw = facing.getHorizontalAngle();
				plannedPitch = 0;

				canContinue = platformTime==0;
			}
			break;
			case ON_TARGET: //platform up, gun aimed
			{
				platformPosition = true;
				canContinue = platformTime==ArtilleryHowitzer.platformTime&&isAimed();
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
			else
			{
				animation = ArtilleryHowitzerAnimation.STOP;
				animationTimeMax = 0;
				animationTime = 0;
				forceTileUpdate();
			}

			if(world.isRemote)
				handleAnimationSounds();

			//update tactile animation
			switch(animation)
			{
				case AIM:
					break;
				case FIRE1:
				case FIRE2:
				case FIRE3:
				case FIRE4:
					tactileHandler.update(animationFire, (float)animationTime/animationTimeMax);
					break;
				case LOAD1:
				case LOAD2:
				case LOAD3:
				case LOAD4:
					tactileHandler.update(animationLoading, (float)animationTime/animationTimeMax);
					break;
				case UNLOAD1:
				case UNLOAD2:
				case UNLOAD3:
				case UNLOAD4:
					tactileHandler.update(animationUnloading, (float)animationTime/animationTimeMax);
					break;
			}

			//update animations
			if(animationTime==(int)(animationTimeMax*animation.executeTime))
			{
				switch(animation)
				{
					case AIM:
						break;
					case FIRE1:
					case FIRE2:
					case FIRE3:
					case FIRE4:
						fireGun(animation.ordinal()-ArtilleryHowitzerAnimation.FIRE1.ordinal());
						break;
					case LOAD1:
					case LOAD2:
					case LOAD3:
					case LOAD4:
					{
						int slot = animation.ordinal()-ArtilleryHowitzerAnimation.LOAD1.ordinal();
						loadedShells.set(slot, inventoryHandler.extractItem(5, 1, false));
					}
					break;
					case UNLOAD1:
					case UNLOAD2:
					case UNLOAD3:
					case UNLOAD4:
					{
						int slot = animation.ordinal()-ArtilleryHowitzerAnimation.UNLOAD1.ordinal();
						inventory.set(6, loadedShells.get(slot).copy());
						loadedShells.set(slot, ItemStack.EMPTY);
					}
					break;
				}
			}
		}

		//in case an animation overrides the gun yaw and pitch or of an early return
		animateGunTactiles();
	}

	private void animateGunTactiles()
	{
		tactileHandler.update(animationPitch, turretPitch/105f);
		tactileHandler.update(animationYaw, ((720-turretYaw-facing.getHorizontalAngle()-90)%360)/360f);
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
					.setIgnoredEntities(tactileHandler!=null?tactileHandler.getEntities(): null)
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

		if(!descPacket)
		{
			loadedShells = Utils.readInventory(nbt.getTagList("loaded_shells", 10), loadedShells.size());

			//for compatibility with old howitzer
			if(nbt.hasKey("bullet"))
				loadedShells = NonNullList.from(ItemStack.EMPTY, new ItemStack(nbt.getCompoundTag("bullet")),
						ItemStack.EMPTY, ItemStack.EMPTY, ItemStack.EMPTY);
		}

		turretYaw = nbt.getFloat("turretYaw");
		plannedYaw = nbt.getFloat("plannedYaw");
		turretPitch = nbt.getFloat("turretPitch");
		plannedPitch = nbt.getFloat("plannedPitch");

		isDoorOpened = nbt.getBoolean("isDoorOpened");

		animation = ArtilleryHowitzerAnimation.values()[nbt.getInteger("animation")];
		animationTime = nbt.getInteger("animation_time");
		animationTimeMax = nbt.getInteger("animation_time_max");

		doorTime = nbt.getInteger("door_time");
		platformTime = nbt.getInteger("platform_time");
		platformPosition = nbt.getBoolean("platform_position");

		shellConveyorTime = nbt.getInteger("shell_conveyor_time");

		orderList.clear();
		for(NBTBase order : nbt.getTagList("order_queue", NBT.TAG_COMPOUND))
			if(order instanceof NBTTagCompound)
			{
				NBTTagCompound compound = (NBTTagCompound)order;
				orderList.add(
						new HowitzerOrder(ArtilleryHowitzerAnimation.values()[compound.getInteger("order")],
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

		if(!descPacket)
		{
			nbt.setTag("loaded_shells", Utils.writeInventory(loadedShells));
		}

		nbt.setFloat("turretYaw", turretYaw);
		nbt.setFloat("plannedYaw", plannedYaw);
		nbt.setFloat("turretPitch", turretPitch);
		nbt.setFloat("plannedPitch", plannedPitch);

		nbt.setBoolean("isDoorOpened", isDoorOpened);

		nbt.setInteger("animation", animation.ordinal());
		nbt.setInteger("animation_time", animationTime);
		nbt.setInteger("animation_time_max", animationTimeMax);

		nbt.setInteger("door_time", doorTime);
		nbt.setInteger("platform_time", platformTime);
		nbt.setBoolean("platform_position", platformPosition);

		nbt.setInteger("shell_conveyor_time", shellConveyorTime);


		nbt.setTag("order_queue",
				orderList.stream()
						.map(order -> {
							NBTTagCompound tag = new NBTTagCompound();
							tag.setInteger("order", order.animation.ordinal());
							tag.setFloat("pitch", order.pitch);
							tag.setFloat("yaw", order.yaw);
							return tag;
						})
						.collect(new NBTTagCollector())
		);

	}

	@Override
	public void receiveMessageFromClient(@Nonnull NBTTagCompound message)
	{

	}

	@Override
	public void receiveMessageFromServer(@Nonnull NBTTagCompound message)
	{
		super.receiveMessageFromServer(message);

		if(isFullSyncMessage(message)||isDummy())
			return;

		if(message.hasKey("loaded_shells"))
			loadedShells = Utils.readInventory(message.getTagList("loaded_shells", 10), loadedShells.size());

		if(message.hasKey("turretYaw"))
			turretYaw = message.getFloat("turretYaw");
		if(message.hasKey("plannedYaw"))
			plannedYaw = message.getFloat("plannedYaw");
		if(message.hasKey("turretPitch"))
			turretPitch = message.getFloat("turretPitch");
		if(message.hasKey("plannedPitch"))
			plannedPitch = message.getFloat("plannedPitch");

		if(message.hasKey("isDoorOpened"))
			isDoorOpened = message.getBoolean("isDoorOpened");

		if(message.hasKey("animation"))
			animation = ArtilleryHowitzerAnimation.values()[message.getInteger("animation")];
		if(message.hasKey("animation_time"))
			animationTime = message.getInteger("animation_time");
		if(message.hasKey("animation_time_max"))
			animationTimeMax = message.getInteger("animation_time_max");

		if(message.hasKey("door_time"))
			doorTime = message.getInteger("door_time");
		if(message.hasKey("platform_time"))
			platformTime = message.getInteger("platform_time");
		if(message.hasKey("platform_position"))
			platformPosition = message.getBoolean("platform_position");

		if(message.hasKey("shell_conveyor_time"))
			shellConveyorTime = message.getInteger("shell_conveyor_time");


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

		switch(animation)
		{
			case FIRE1:
			case FIRE2:
			case FIRE3:
			case FIRE4:
				current = firingSoundAnimation;
				break;
			case LOAD1:
			case LOAD2:
			case LOAD3:
			case LOAD4:
				current = loadingSoundAnimation;
				break;
			case UNLOAD1:
			case UNLOAD2:
			case UNLOAD3:
			case UNLOAD4:
				current = unloadingSoundAnimation;
				break;
			default:
				break;
		}

		if(current!=null)
		{
			BlockPos soundPos = getBlockPosForPos(202);
			if(soundsList==null)
				soundsList = new ArrayList<>();
			current.handleSounds(soundsList, soundPos, animationTime, .75f);
		}
	}

	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		boolean hasEnergy = energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive;
		boolean hasActiveEnergy = energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive;

		boolean platformOK = animation==ArtilleryHowitzerAnimation.STOP||platformTime==(platformPosition?ArtilleryHowitzer.platformTime: 0);
		boolean yawOK = turretYaw==MathHelper.wrapDegrees(plannedYaw);
		boolean pitchOK = turretPitch==plannedPitch;

		Vec3d posDoor = new Vec3d(getBlockPosForPos(525));
		soundDoorOpen = playRepeatedSound(soundDoorOpen, IISounds.slidingDoorOpenM, posDoor, .5f, 1f,
				hasEnergy&&isDoorOpened&&doorTime < ArtilleryHowitzer.doorTime
		);
		soundDoorClose = playRepeatedSound(soundDoorClose, IISounds.slidingDoorCloseM, posDoor, .5f, 1f,
				hasEnergy&&!isDoorOpened&&doorTime > 0
		);

		soundRotationH = playRepeatedSound(soundRotationH, IISounds.turntableHeavyForwardM, posDoor, .5f, 1f,
				hasEnergy&&platformOK&&!yawOK
		);
		soundRotationV = playRepeatedSound(soundRotationV, IISounds.electricMotorHeavyForwardM, posDoor, .5f, 1f,
				hasEnergy&&platformOK&&!pitchOK
		);

		/*soundRotationV = playRepeatedSound(soundRotationV, IISounds.howitzerPlatformLower, posDoor, .5f, 1f,
				hasEnergy&&platformOK&&!pitchOK
		);*/

		//TODO: 16.05.2023 platform lowering

	}

	@SideOnly(Side.CLIENT)
	private ConditionCompoundSound playRepeatedSound(ConditionCompoundSound current, MultiSound sound, Vec3d pos, float volume, float pitch, boolean condition)
	{
		if(!condition)
		{
			if(current!=null)
				current.forceStop();
			return null;
		}

		if(current==null)
		{
			current = new ConditionCompoundSound(sound, SoundCategory.BLOCKS, pos, volume, pitch, () -> true);
			ClientUtils.mc().getSoundHandler().playSound(current);
		}
		return current;
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

						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.FIRE1, plannedPitch-vOffset, plannedYaw-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.FIRE2, plannedPitch+vOffset, plannedYaw+hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.FIRE3, plannedPitch-vOffset, plannedYaw-hOffset));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.FIRE4, plannedPitch+vOffset, plannedYaw+hOffset));
					}
					break;
					case "load_all":
					{
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.LOAD1));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.LOAD2));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.LOAD3));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.LOAD4));
					}
					break;
					case "unload_all":
					{
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.UNLOAD1));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.UNLOAD2));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.UNLOAD3));
						orderList.add(new HowitzerOrder(ArtilleryHowitzerAnimation.UNLOAD4));
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
									return new DataTypeFloat(platformTime/(float)ArtilleryHowitzer.platformTime);

								case "get_door_opened":
									return new DataTypeBoolean(isDoorOpened&&doorTime==ArtilleryHowitzer.doorTime);
								case "get_door_closed":
									return new DataTypeBoolean(!isDoorOpened&&doorTime==0);
								case "get_door_opening":
									return new DataTypeBoolean(doorTime!=0&&doorTime!=ArtilleryHowitzer.doorTime);

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
									return new DataTypeString(animation.getName());
								case "get_state_num":
									return new DataTypeInteger(animation.ordinal());
							}
							return null;
						});
						sendData(callback, EnumFacing.UP, 441);
					}
					break;
					//Single Commands
					default:
					{
						ArtilleryHowitzerAnimation anim = ArtilleryHowitzerAnimation.v(command, this);
						if(anim!=null)
						{
							if(anim==ArtilleryHowitzerAnimation.STOP)
								orderList.clear();

							if(anim.matchesRequirements(this))
							{
								animation = anim;
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
		if(part==0)
			isDoorOpened = state;
	}

	@Override
	public void onAnimationChangeServer(boolean state, int part)
	{
		if(part==0)
			isDoorOpened = state;
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), IIPacketHandler.targetPointFromPos(this.getPos(), this.world, 32));
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
	public TactileHandler getTactileHandler()
	{
		return tactileHandler;
	}

	@Nonnull
	@Override
	public World getTactileWorld()
	{
		return world;
	}

	@Nonnull
	@Override
	public BlockPos getTactilePos()
	{
		return this.getPos();
	}

	@Nonnull
	@Override
	public EnumFacing getTactileFacing()
	{
		return facing;
	}

	@Override
	public boolean getIsTactileMirrored()
	{
		return mirrored;
	}

	public enum ArtilleryHowitzerAnimation implements ISerializableEnum
	{
		STOP(false, false, GunPosition.NEUTRAL, t -> true, t -> false, 0, null, 1f), //stops current action
		HIDE(false, false, GunPosition.LOADING, t -> true, t -> t.platformTime==0, 0, null, 1f), //makes howitzer go down

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

		ArtilleryHowitzerAnimation(boolean requiresPlatform, boolean platformUp, GunPosition gunPosition,
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

		public boolean matchesRequirements(TileEntityArtilleryHowitzer te)
		{
			return requirements.test(te);
		}

		public boolean isFulfilled(TileEntityArtilleryHowitzer te)
		{
			return fulfilled.test(te);
		}

		@Nullable
		public static ArtilleryHowitzerAnimation v(String s, TileEntityArtilleryHowitzer te)
		{
			String ss = s.toUpperCase();
			Optional<ArtilleryHowitzerAnimation> found = Arrays.stream(values())
					.filter(e -> e.alias!=null&&e.alias.toLowerCase().equals(s))
					.filter(a -> a.matchesRequirements(te))
					.findFirst();
			return found.orElseGet(() -> Arrays.stream(values())
					.filter(e -> e.name().equals(ss))
					.findFirst()
					.orElse(null));

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
		final ArtilleryHowitzerAnimation animation;
		final float pitch, yaw;

		public HowitzerOrder(ArtilleryHowitzerAnimation animation, float pitch, float yaw)
		{
			this.animation = animation;
			this.pitch = pitch;
			this.yaw = yaw;
		}

		public HowitzerOrder(ArtilleryHowitzerAnimation animation)
		{
			this(animation, 0, 0);
		}
	}
}
