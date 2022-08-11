package pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.first;

import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.api.tool.ConveyorHandler.IConveyorAttachable;
import blusunrize.immersiveengineering.client.ClientUtils;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Machines.ArtilleryHowitzer;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletHelper;
import pl.pabilo8.immersiveintelligence.api.data.DataPacket;
import pl.pabilo8.immersiveintelligence.api.data.IDataConnector;
import pl.pabilo8.immersiveintelligence.api.data.types.*;
import pl.pabilo8.immersiveintelligence.api.utils.IBooleanAnimatedPartsBlock;
import pl.pabilo8.immersiveintelligence.api.utils.IIMultiblockInterfaces.IExplosionResistantMultiblock;
import pl.pabilo8.immersiveintelligence.api.utils.IIMultiblockInterfaces.ILadderMultiblock;
import pl.pabilo8.immersiveintelligence.client.fx.ParticleUtils;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.blocks.multiblocks.metal.tileentities.base.TileEntityMultiblockIIGeneric;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ammunition.ItemIIAmmoArtillery;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageBooleanAnimatedPartsSync;
import pl.pabilo8.immersiveintelligence.common.util.IISoundAnimation;

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
		implements IBooleanAnimatedPartsBlock, IConveyorAttachable, ILadderMultiblock, IExplosionResistantMultiblock
{
	//--- AABB ---//
	private static final AxisAlignedBB AABB_FULL = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	private static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0, 0, 0, 0, 0, 0);
	private static final AxisAlignedBB AABB_DOOR = new AxisAlignedBB(0, 0, 0, 1, 0.125, 1);
	private static final AxisAlignedBB AABB_TALLER_FLOOR = new AxisAlignedBB(0, 0, 0, 1, 0.4375, 1);
	private static final AxisAlignedBB AABB_LOWER_FLOOR = new AxisAlignedBB(0, 0, 0, 1, 0.1875, 1);
	private static final AxisAlignedBB AABB_TABLE_TOP = new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);

	private static final AxisAlignedBB[] AABB_LADDER = new AxisAlignedBB[]{
			new AxisAlignedBB(0, 0, 0, 1, 1, 0.9375),
			new AxisAlignedBB(0, 0, 0.0625, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 0.9375, 1, 1),
			new AxisAlignedBB(0.0625, 0, 0, 1, 1, 1)
	};
	private static final AxisAlignedBB[] AABB_DOOR_HOLDERS_RIGHT = new AxisAlignedBB[]{
			new AxisAlignedBB(0, 0, 0, 0.5, 0.625, 1),
			new AxisAlignedBB(0.5, 0, 0, 1, 0.625, 1),
			new AxisAlignedBB(0, 0, 0.5, 1, 0.625, 1),
			new AxisAlignedBB(0, 0, 0, 1, 0.625, 0.5)
	};
	private static final AxisAlignedBB[] AABB_DOOR_HOLDERS_LEFT = new AxisAlignedBB[]{
			new AxisAlignedBB(0.5, 0, 0, 1, 0.625, 1),
			new AxisAlignedBB(0, 0, 0, 0.5, 0.625, 1),
			new AxisAlignedBB(0, 0, 0, 1, 0.625, 0.5),
			new AxisAlignedBB(0, 0, 0.5, 1, 0.625, 1)
	};

	private static IISoundAnimation loadingSoundAnimation, unloadingSoundAnimation, firingSoundAnimation;

	static
	{
		loadingSoundAnimation = new IISoundAnimation(18);
		unloadingSoundAnimation = new IISoundAnimation(18);
		firingSoundAnimation = new IISoundAnimation(15.96);

		loadingSoundAnimation
				.withSound(0.64, IISounds.metal_breadbox_open)
				.withSound(0.84, IISounds.howitzer_door_open)
				.withSound(1.88, IISounds.howitzer_door_open)
				.withSound(2.76, IISounds.howitzer_door_open)
				.withSound(3.68, IISounds.howitzer_door_close)
				.withSound(4.36, IISounds.metal_locker_open)
				.withSound(5.0, IISounds.howitzer_chain)
				.withSound(6.12, IISounds.metal_breadbox_open)
				.withSound(8.24, IISounds.metal_breadbox_close)
				.withSound(8.4, IISounds.howitzer_shell_put)
				.withSound(8.92, IISounds.howitzer_platform_start)
				.withSound(9.04, IISounds.howitzer_chain)
				.withSound(11.28, IISounds.metal_breadbox_open)
				.withSound(11.52, IISounds.metal_locker_close)
				.withSound(11.96, IISounds.inserter_forward)
				.withSound(12.44, IISounds.inserter_forward)
				.withSound(12.8, IISounds.inserter_forward)
				.withSound(13.2, IISounds.inserter_forward)
				.withSound(13.24, IISounds.howitzer_door_open)
				.withSound(13.96, IISounds.howitzer_shell_pick)
				.withSound(14.24, IISounds.inserter_forward)
				.withSound(14.72, IISounds.inserter_forward)
				.withSound(15.12, IISounds.inserter_forward)
				.withSound(15.56, IISounds.inserter_forward)
				.withSound(15.96, IISounds.inserter_forward)
				.withSound(16.36, IISounds.howitzer_shell_put)
				.withSound(16.44, IISounds.inserter_forward)
				.withSound(16.68, IISounds.inserter_forward)
				.compile(ArtilleryHowitzer.loadRackTime);

		firingSoundAnimation
				.withSound(0.0, IISounds.metal_breadbox_open)
				.withSound(0.52, IISounds.inserter_forward)
				.withSound(1.0, IISounds.inserter_forward)
				.withSound(1.32, IISounds.inserter_forward)
				.withSound(1.76, IISounds.inserter_forward)
				.withSound(2.24, IISounds.inserter_forward)
				.withSound(2.28, IISounds.inserter_forward)
				.withSound(2.8, IISounds.inserter_forward)
				.withSound(3.24, IISounds.inserter_forward)
				.withSound(3.72, IISounds.inserter_forward)
				.withSound(4.16, IISounds.inserter_forward)
				.withSound(4.4, IISounds.metal_breadbox_close)
				.withSound(4.56, IISounds.inserter_forward)
				.withSound(4.96, IISounds.howitzer_shell_pick)
				.withSound(5.04, IISounds.inserter_forward)
				.withSound(5.48, IISounds.inserter_forward)
				.withSound(5.92, IISounds.inserter_forward)
				.withSound(6.36, IISounds.inserter_forward)
				.withSound(6.8, IISounds.inserter_forward)
				.withSound(7.08, IISounds.metal_breadbox_close)
				.withSound(7.72, IISounds.howitzer_shell_put)
				.withSound(7.8, IISounds.inserter_forward)
				.withSound(8.24, IISounds.inserter_forward)
				.withSound(8.44, IISounds.inserter_forward)
				.withSound(8.48, IISounds.inserter_forward)
				.withSound(9.0, IISounds.inserter_forward)
				//fire!
				.withSound(9.52, IISounds.inserter_forward)
				.withSound(10.0, IISounds.inserter_forward)
				.withSound(10.36, IISounds.inserter_forward)
				.withSound(10.64, IISounds.inserter_forward)
				.withSound(10.68, IISounds.inserter_forward)
				.withSound(11.2, IISounds.inserter_forward)
				.withSound(11.68, IISounds.inserter_forward)
				.withSound(12.16, IISounds.inserter_forward)
				.withSound(12.24, IISounds.howitzer_shell_pick)
				.withSound(12.64, IISounds.inserter_backward)
				.withSound(13.08, IISounds.inserter_backward)
				.withSound(13.52, IISounds.inserter_backward)
				.withSound(13.96, IISounds.inserter_forward)
				.withSound(14.12, IISounds.metal_breadbox_close)
				.withSound(14.48, IISounds.howitzer_shell_put)
				.withSound(14.76, IISounds.inserter_backward)
				.withSound(15.24, IISounds.inserter_backward)
				.withSound(15.6, IISounds.inserter_backward)
				.withSound(15.72, IISounds.metal_breadbox_open)
				.compile(ArtilleryHowitzer.gunFireTime);

		unloadingSoundAnimation
				.withSound(0.0, IISounds.inserter_forward)
				.withSound(0.04, IISounds.metal_breadbox_open)
				.withSound(0.36, IISounds.howitzer_door_open)
				.withSound(0.48, IISounds.inserter_forward)
				.withSound(0.96, IISounds.inserter_forward)
				.withSound(1.24, IISounds.metal_breadbox_close)
				.withSound(1.36, IISounds.howitzer_shell_pick)
				.withSound(1.44, IISounds.inserter_forward)
				.withSound(1.92, IISounds.inserter_forward)
				.withSound(2.36, IISounds.inserter_forward)
				.withSound(2.84, IISounds.inserter_forward)
				.withSound(3.32, IISounds.inserter_forward)
				.withSound(3.8, IISounds.inserter_forward)
				.withSound(4.0, IISounds.howitzer_shell_put)
				.withSound(4.28, IISounds.inserter_forward)
				.withSound(4.72, IISounds.inserter_forward)
				.withSound(5.16, IISounds.inserter_forward)
				.withSound(5.6, IISounds.inserter_forward)
				.withSound(5.76, IISounds.metal_breadbox_close)
				.withSound(5.92, IISounds.metal_locker_open)
				.withSound(6.6, IISounds.howitzer_chain)
				.withSound(8.08, IISounds.howitzer_door_close)
				.withSound(9.28, IISounds.metal_breadbox_close)
				.withSound(10.32, IISounds.howitzer_door_close)
				.withSound(12.44, IISounds.metal_locker_close)
				.compile(ArtilleryHowitzer.loadRackTime);
	}

	//--- Variables ---//

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


	public TileEntityArtilleryHowitzer()
	{
		super(MultiblockArtilleryHowitzer.instance);

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

		boolean rs = world.isBlockPowered(getBlockPosForPos(getRedstonePos(true)[0]))^redstoneControlInverted;
		if(isDoorOpened^rs)
		{
			isDoorOpened = rs;
			if(!world.isRemote)
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 0, this.getPos()),
						pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromTile(this, 48));
		}

		//operate only if energy is sufficient
		if(energyStorage.getEnergyStored() < ArtilleryHowitzer.energyUsagePassive)
			return;
		energyStorage.extractEnergy(ArtilleryHowitzer.energyUsagePassive, false);

		//howitzer door movement
		doorTime = MathHelper.clamp(doorTime+(isDoorOpened?1: -2), 0, ArtilleryHowitzer.doorTime);
		//howitzer platform movement
		platformTime = MathHelper.clamp(platformTime+(platformPosition?1: -1), 0, ArtilleryHowitzer.platformTime);

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
			if(!inventoryHandler.getStackInSlot(11).isEmpty())
			{
				BlockPos outPos = getBlockPosForPos(327)
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

		//S T O P
		if(animation==ArtilleryHowitzerAnimation.STOP)
			return;

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
	}

	private void fireGun(int i)
	{
		double true_angle = Math.toRadians((-turretYaw) > 180?360f-(-turretYaw): (-turretYaw));
		double true_angle2 = Math.toRadians(-(-90-turretPitch));

		Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(3, true_angle, true_angle2);
		Vec3d gun_dir = gun_end.normalize();
		if(world.isRemote)
		{
			Vec3d gun_end_particle = gun_dir.scale(4.5);
			ParticleUtils.spawnGunfireFX(getGunPosition().x+gun_end_particle.x, getGunPosition().y+gun_end_particle.y, getGunPosition().z+gun_end_particle.z, gun_dir.x, gun_dir.y, gun_dir.z, 8f);
		}
		world.playSound(null, getPos(), IISounds.howitzer_shot, SoundCategory.BLOCKS, 1, 1);

		if(!world.isRemote)
		{
			ItemStack bullet = loadedShells.get(i);
			EntityBullet a = BulletHelper.createBullet(world, bullet, getGunPosition().add(gun_end), gun_dir);
			a.setShootPos(getMultiblockBlocks());
			a.world.spawnEntity(a);
		}

		loadedShells.set(i, IIContent.itemAmmoArtillery.getCasingStack(1));
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
			SoundEvent[] sounds = current.getSounds(animationTime);
			if(sounds!=null)
				for(int i = 0; i < sounds.length; i++)
				{
					Vec3d vv = new Vec3d(getBlockPosForPos(202)).addVector(
							Utils.RAND.nextFloat(),
							Utils.RAND.nextFloat(),
							Utils.RAND.nextFloat()
					);
					world.playSound(ClientUtils.mc().player, vv.x, vv.y, vv.z, sounds[i], SoundCategory.BLOCKS, .75f, 1);
				}

		}
	}

	//for handling repeated sounds
	@SideOnly(Side.CLIENT)
	private void handleSounds()
	{
		boolean hasEnergy = energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive;
		boolean hasActiveEnergy = energyStorage.getEnergyStored() >= ArtilleryHowitzer.energyUsagePassive+ArtilleryHowitzer.energyUsageActive;

		boolean platformOK = animation==ArtilleryHowitzerAnimation.STOP||platformTime==(platformPosition?ArtilleryHowitzer.platformTime: 0);
		boolean yawOK = turretYaw==MathHelper.wrapDegrees(plannedYaw);
		boolean pitchOK = turretPitch==plannedPitch;
		if(platformOK)
		{
			if(!yawOK)
			{
				if(world.getTotalWorldTime()%20==0)
					world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), IISounds.howitzer_rotation_v, SoundCategory.BLOCKS, .5f, 1);
			}
			else if(!pitchOK)
				if(world.getTotalWorldTime()%20==0)
					world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), IISounds.howitzer_rotation_h, SoundCategory.BLOCKS, .5f, 1);
		}
		else if(hasEnergy)
		{
			boolean isStart = platformTime==1, isFinish = platformTime==ArtilleryHowitzer.platformTime-1;
			if(platformPosition?isStart: isFinish)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).down(), IISounds.howitzer_platform_start, SoundCategory.BLOCKS, .5f, 1);
			if(platformPosition?isFinish: isStart)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).down(), IISounds.howitzer_platform_end, SoundCategory.BLOCKS, .5f, 1);

			if(world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525), platformPosition?IISounds.howitzer_platform_raise: IISounds.howitzer_platform_lower, SoundCategory.BLOCKS, .5f, 1);
		}

		if(isDoorOpened&&doorTime < ArtilleryHowitzer.doorTime)
		{
			if(hasActiveEnergy&&world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).up(), IISounds.howitzer_door_open, SoundCategory.BLOCKS, .5f, 1);
		}
		else if(!isDoorOpened&&doorTime > 0)
			if(hasActiveEnergy&&world.getTotalWorldTime()%20==0)
				world.playSound(ClientUtils.mc().player, getBlockPosForPos(525).up(), IISounds.howitzer_door_close, SoundCategory.BLOCKS, .5f, 1);
	}

	@Nonnull
	@Override
	public int[] getEnergyPos()
	{
		return new int[]{449};
	}

	@Nonnull
	@Override
	public int[] getRedstonePos(boolean in)
	{
		return in?new int[]{481}: new int[0];
	}

	@Override
	public boolean isStackValid(int slot, ItemStack stack)
	{
		return stack.getItem() instanceof ItemIIAmmoArtillery;
	}

	@Override
	public int getSlotLimit(int slot)
	{
		return 1;
	}

	@Nonnull
	@Override
	public int[] getDataPos(boolean input)
	{
		//input and output
		return new int[]{441};
	}

	@Override
	public void receiveData(DataPacket packet, int pos)
	{
		IDataConnector conn = pl.pabilo8.immersiveintelligence.api.Utils.findConnectorFacing(getBlockPosForPos(441), world, mirrored?facing.rotateY(): facing.rotateYCCW());

		//Command
		if(animationTime==0&&packet.getPacketVariable('c') instanceof DataTypeString) //cannot interrupt a performed task
		{
			String command = packet.getPacketVariable('c').valueToString();

			ArtilleryHowitzerAnimation anim = ArtilleryHowitzerAnimation.v(command, this);
			if(anim!=null)
			{
				if(anim.matchesRequirements(this))
				{
					if(animation==ArtilleryHowitzerAnimation.STOP) //is not doing any task
					{
						if(packet.getPacketVariable('y') instanceof IDataTypeNumeric)
						{
							plannedYaw = ((IDataTypeNumeric)packet.getPacketVariable('y')).floatValue()%360;
							if(plannedYaw < 0)
								plannedYaw = 360f-plannedYaw;
						}
						if(packet.getPacketVariable('p') instanceof IDataTypeNumeric)
							plannedPitch = Math.min(Math.max(-Math.abs((((IDataTypeNumeric)packet.getPacketVariable('p')).floatValue())%360), -105), 0);
					}

					animation = anim;
					animationTime = 0;
					animationTimeMax = anim.animationTime;
					forceTileUpdate();
				}
			}
			else
			{
				if(conn==null)
					return;

				DataPacket pp = null;
				switch(command)
				{
					case "get_energy":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage(packet, "energy", new DataTypeInteger(energyStorage.getEnergyStored()));
						break;
					case "get_state_progress":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "state_progress", new DataTypeInteger((int)(animationTime/(float)animationTimeMax*100)));
						break;
					case "get_yaw":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "yaw", new DataTypeInteger((int)turretYaw));
						break;
					case "get_pitch":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "pitch", new DataTypeInteger((int)turretPitch));
						break;
					case "get_planned_yaw":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "planned_yaw", new DataTypeInteger((int)plannedYaw));
						break;
					case "get_planned_pitch":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "planned_pitch", new DataTypeInteger((int)plannedPitch));
						break;
					case "get_platform_height":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "platform_height", new DataTypeInteger((int)platformTime));
						break;
					case "get_door_opened":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "door_opened", new DataTypeBoolean(isDoorOpened&&doorTime==ArtilleryHowitzer.doorTime));
						break;
					case "get_door_closed":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "door_closed", new DataTypeBoolean(!isDoorOpened&&doorTime==0));
						break;
					case "get_door_opening":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "door_opening", new DataTypeBoolean(doorTime!=0&&doorTime!=ArtilleryHowitzer.doorTime));
						break;
					case "get_loaded_shell":
					{
						int i = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('i')).value;
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "loaded_shell", new DataTypeItemStack(loadedShells.get(i)));
					}
					break;
					case "get_stored_shell":
					{
						int i = packet.getVarInType(DataTypeInteger.class, packet.getPacketVariable('i')).value;
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "stored_shell", new DataTypeItemStack(inventory.get(MathHelper.clamp(i, 0, 5))));

					}
					break;
					case "get_state":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "state", new DataTypeString(animation.getName()));
						break;
					case "get_state_num":
						pp = pl.pabilo8.immersiveintelligence.api.Utils.getSimpleCallbackMessage
								(packet, "state", new DataTypeInteger(animation.ordinal()));
						break;
				}
				if(pp!=null)
					conn.sendPacket(pp);
			}
		}

		forceTileUpdate();
	}

	@Override
	public List<AxisAlignedBB> getBounds(boolean collision)
	{
		TileEntityArtilleryHowitzer master = master();
		ArrayList<AxisAlignedBB> list = new ArrayList<>();
		switch(pos) //sorry, I wish I could use expressions there, but it's java8 :<
		{
			case 445:
			case 364:
			case 283:
				//ladder side empty blocks
			case 345:
			case 346:
			case 347:
				//ladder side empty blocks
			case 264:
			case 265:
			case 266:
				//ladder side empty blocks
			case 183:
			case 184:
			case 185:
				//ladder side empty blocks
			case 102:
			case 103:
			case 104:
				//loader chamber metal box
			case 419:
			case 418:
			case 417:
				//cannon
			case 293:
			case 284:
			case 275:
			case 291:
			case 282:
			case 273:
			case 111:
			case 112:
			case 113:
			case 120:
			case 122:
			case 129:
			case 130:
			case 131:
				list.add(AABB_EMPTY.offset(getPos()));
				break;
			//base | cable part
			case 30:
			case 31:
			case 32:
			case 41:
			case 39:
			case 48:
			case 49:
			case 50:
				list.add(AABB_LOWER_FLOOR.offset(getPos()));
				break;
			//base | yellow-black stripes/concrete part
			case 21:
			case 22:
			case 23:
			case 29:
			case 33:
			case 38:
			case 42:
			case 47:
			case 51:
			case 57:
			case 58:
			case 59:
				list.add(AABB_TALLER_FLOOR.offset(getPos()));
				break;
			//door
			case 524:
			case 525:
			case 526:
			case 527:
			case 528:
			case 533:
			case 534:
			case 535:
			case 536:
			case 537:
			case 542:
			case 543:
			case 544:
			case 545:
			case 546:
			case 515:
			case 516:
			case 517:
			case 518:
			case 519:
			case 506:
			case 507:
			case 508:
			case 509:
			case 510:
				list.add((master!=null&&master.doorTime > 1?AABB_EMPTY: AABB_DOOR)
						.offset(getPos()));
				break;
			//door holders | right
			case 520:
			case 529:
			case 538:
				list.add(AABB_DOOR_HOLDERS_RIGHT[(mirrored?facing: facing.getOpposite()).getIndex()-2].offset(getPos()));
				break;
			//door holders | left
			case 514:
			case 523:
			case 532:
				list.add(AABB_DOOR_HOLDERS_LEFT[(mirrored?facing: facing.getOpposite()).getIndex()-2].offset(getPos()));
				break;
			//table_top1
			case 560:
			case 551:
			case 541:
			case 540:
				//table_top2
			case 488:
			case 497:
			case 505:
			case 504:
				//table_top3
			case 492:
			case 501:
			case 511:
			case 512:
				//table_top4
			case 548:
			case 547:
			case 555:
			case 564:
				list.add(AABB_TABLE_TOP.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
			//s
			case 465:
			case 425:
			case 384:
			case 303:
			case 222:
			case 141:
			case 344:
			case 263:
			case 182:
			case 101:
				list.add(new AxisAlignedBB(0.25+0.0625, 0, 0.25+0.0625, 0.75-0.0625, 1, 0.75-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				switch(mirrored?facing: facing.getOpposite())
				{
					case NORTH:
					case WEST:
						list.add(new AxisAlignedBB(0.6875, 0, 0, 1, 1, 0.3125).offset(getPos()));
						list.add(new AxisAlignedBB(0, 0, 0.6875, 0.3125, 1, 1).offset(getPos()));
						break;
					case SOUTH:
					case EAST:
						list.add(new AxisAlignedBB(0, 0, 0, 0.3125, 1, 0.3125).offset(getPos()));
						list.add(new AxisAlignedBB(1-0.3125, 0, 1-0.3125, 1, 1, 1).offset(getPos()));
						break;
					default:
						list.add(AABB_EMPTY.offset(getPos()));
						break;
				}
				break;
			//s
			case 461:
			case 380:
			case 299:
			case 218:
			case 137:
			case 429:
			case 348:
			case 267:
			case 186:
			case 105:
				list.add(new AxisAlignedBB(0.25+0.0625, 0, 0.25+0.0625, 0.75-0.0625, 1, 0.75-0.0625).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				switch(mirrored?facing: facing.getOpposite())
				{
					case NORTH:
					case WEST:
						list.add(new AxisAlignedBB(0, 0, 0, 0.3125, 1, 0.3125).offset(getPos()));
						list.add(new AxisAlignedBB(1-0.3125, 0, 1-0.3125, 1, 1, 1).offset(getPos()));
						break;
					case SOUTH:
					case EAST:
						list.add(new AxisAlignedBB(0.6875, 0, 0, 1, 1, 0.3125).offset(getPos()));
						list.add(new AxisAlignedBB(0, 0, 0.6875, 0.3125, 1, 1).offset(getPos()));
						break;
					default:
						list.add(AABB_EMPTY.offset(getPos()));
						break;
				}
				break;
			//ladder
			case 66:
			case 147:
			case 228:
			case 309:
			case 390:
			case 471:
				list.add(AABB_LADDER[facing.getIndex()-2].offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
			case 410:
				list.add(AABB_LADDER[facing.getIndex()-2].offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
			default:
				list.add(AABB_FULL.offset(getPos().getX(), getPos().getY(), getPos().getZ()));
				break;
		}

		return list;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return pos==410&&master()!=null;
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
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageBooleanAnimatedPartsSync(isDoorOpened, 1, getPos()), pl.pabilo8.immersiveintelligence.api.Utils.targetPointFromPos(this.getPos(), this.world, 32));
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
			if(entityItem.getItem().getItem()!=IIContent.itemAmmoArtillery)
				return;

			//insert copied stack to inventory
			ItemStack stack = master.inventoryHandler.insertItem(0, entityItem.getItem().copy(), false);
			if(stack.isEmpty())
				entityItem.setItem(ItemStack.EMPTY);
		}
	}

	private Vec3d getGunPosition()
	{
		BlockPos shoot_pos = getBlockPosForPos(445).offset(EnumFacing.UP, 1);
		return new Vec3d(shoot_pos.getX()+.5, shoot_pos.getY()+1.5, shoot_pos.getZ()+.5);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Nonnull
	public AxisAlignedBB getRenderBoundingBox()
	{
		if(!isDummy())
		{
			BlockPos nullPos = this.getBlockPosForPos(0);
			return new AxisAlignedBB(nullPos, nullPos.offset(facing, structureDimensions[1])
					.offset(mirrored?facing.rotateYCCW(): facing.rotateY(), structureDimensions[2]).up(structureDimensions[0]))
					.grow(5);
		}
		return new AxisAlignedBB(0, 0, 0, 1, 1, 1)
				.offset(getPos());
	}

	@Override
	public boolean isLadder()
	{
		switch(pos)
		{
			case 66:
			case 147:
			case 228:
			case 309:
			case 390:
			case 471:
				return true;
			default:
				return false;
		}
	}

	@Override
	public float getExplosionResistance()
	{
		switch(pos)
		{
			case 524:
			case 525:
			case 526:
			case 527:
			case 528:
			case 533:
			case 534:
			case 535:
			case 536:
			case 537:
			case 542:
			case 543:
			case 544:
			case 545:
			case 546:
			case 515:
			case 516:
			case 517:
			case 518:
			case 519:
			case 506:
			case 507:
			case 508:
			case 509:
			case 510:
			{
				TileEntityArtilleryHowitzer master = master();
				return master!=null&&master.doorTime > 1?2000.0F: IIContent.blockMetalMultiblock0.getBlockResistance();
			}
			default:
				return IIContent.blockMetalMultiblock0.getBlockResistance();
		}
	}

	public enum ArtilleryHowitzerAnimation implements IStringSerializable
	{
		STOP(false, false, GunPosition.NEUTRAL, t -> true, 0, null, 1f), //stops current action
		HIDE(false, false, GunPosition.LOADING, t -> true, 0, null, 1f), //makes howitzer go down
		LOAD1(true, false, GunPosition.LOADING, t -> t.loadedShells.get(0).isEmpty()&&!t.inventory.get(5).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD2(true, false, GunPosition.LOADING, t -> t.loadedShells.get(1).isEmpty()&&!t.inventory.get(5).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD3(true, false, GunPosition.LOADING, t -> t.loadedShells.get(2).isEmpty()&&!t.inventory.get(5).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		LOAD4(true, false, GunPosition.LOADING, t -> t.loadedShells.get(3).isEmpty()&&!t.inventory.get(5).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "LOAD", 1f),
		UNLOAD1(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(0).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD2(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(1).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD3(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(2).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),
		UNLOAD4(true, false, GunPosition.LOADING, t -> !t.loadedShells.get(3).isEmpty(),
				ArtilleryHowitzer.loadRackTime, "UNLOAD", 1f),

		FIRE1(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(0).getItem()==IIContent.itemAmmoArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE2(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(1).getItem()==IIContent.itemAmmoArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE3(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(2).getItem()==IIContent.itemAmmoArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		FIRE4(true, true, GunPosition.ON_TARGET, t -> t.loadedShells.get(3).getItem()==IIContent.itemAmmoArtillery,
				ArtilleryHowitzer.gunFireTime, "FIRE", (float)ArtilleryHowitzer.gunFireMoment),
		AIM(true, true, GunPosition.ON_TARGET, t -> true, 0, null, 1f);

		//whether the platform is required to be in a position
		//the platform position: true - up, false - down
		//whether requires yaw & pitch to be 0
		final boolean requiresPlatform, platformUp;
		final GunPosition gunPosition;
		@Nullable
		final String alias;
		final Predicate<TileEntityArtilleryHowitzer> requirements;
		final int animationTime;
		final float executeTime;

		ArtilleryHowitzerAnimation(boolean requiresPlatform, boolean platformUp, GunPosition gunPosition,
								   Predicate<TileEntityArtilleryHowitzer> requirements,
								   int animationTime, @Nullable String alias, float executeTime)
		{
			this.requiresPlatform = requiresPlatform;
			this.platformUp = platformUp;
			this.gunPosition = gunPosition;
			this.requirements = requirements;
			this.animationTime = animationTime;
			this.alias = alias;
			this.executeTime = executeTime;
		}

		@Override
		@Nonnull
		public String getName()
		{
			return name().toLowerCase();
		}

		public boolean matchesRequirements(TileEntityArtilleryHowitzer te)
		{
			return requirements.test(te);
		}

		@Nullable
		public static ArtilleryHowitzerAnimation v(String s, TileEntityArtilleryHowitzer te)
		{
			String ss = s.toUpperCase();
			Optional<ArtilleryHowitzerAnimation> found = Arrays.stream(values())
					.filter(e -> e.alias!=null&&e.alias.toLowerCase().matches(s))
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
}
