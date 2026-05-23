package pl.pabilo8.immersiveintelligence.common.entity.mounted_weapon;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.entity.vehicle.utils.VehicleControls;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT;
import pl.pabilo8.immersiveintelligence.common.util.easynbt.SyncNBT.SyncEvents;
import pl.pabilo8.immersiveintelligence.common.util.entity.ISyncNBTEntity;
import pl.pabilo8.immersiveintelligence.common.util.gun.GunAimCoordinate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Common class for mounted weapons
 *
 * @author Pabilo8 (pabilo@iiteam.net)
 * @ii-approved 0.3.1
 * @since 15.05.2026
 */
public abstract class EntityMountedWeapon extends Entity implements IEntityAdditionalSpawnData, ISyncNBTEntity<EntityMountedWeapon>
{
	@SyncNBT(events = SyncEvents.ENTITY_VEHICLE_CONTROLS)
	public VehicleControls controls = new VehicleControls();
	@SyncNBT(events = {SyncEvents.ENTITY_CUSTOM1, SyncEvents.ENTITY_CUSTOM2})
	public GunAimCoordinate aim = new GunAimCoordinate();
	@SyncNBT(events = SyncEvents.ENTITY_CUSTOM1)
	public int setupTime = 0, maxSetupTime = 1;
	@SyncNBT(events = SyncEvents.ENTITY_INTERACT)
	private ItemStack originStack = ItemStack.EMPTY;
	private AxisAlignedBB baseAabb;

	public EntityMountedWeapon(World world)
	{
		super(world);
		setSize(1f, 1f);
	}

	@Override
	protected final void entityInit()
	{
		//Do not overwrite, breaks loading order
	}

	@Override
	public final void onUpdate()
	{
		//Just so child classes do not overwrite it
		super.onUpdate();
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		if(setupTime < maxSetupTime)
			setupTime += 1;

		//Check if there is a block supporting the weapon
		if(checkSupportAndDrop())
			return;

		//Update look angle
		this.prevRotationYaw = aim.getYaw(0);
		this.prevRotationPitch = aim.getPitch(0);

		//Check and send controls from client
		if(isBeingRidden())
		{
			aim.update();
			if(world.isRemote)
				handleClientControls();
		}

		//Update displayed angles
		this.rotationYaw = aim.getYaw(0);
		this.rotationPitch = aim.getPitch(0);
	}

	protected boolean checkSupportAndDrop()
	{
		BlockPos checkPos = getSupportCheckPos();
		if(world.isRemote||ticksExisted%20!=0||checkPos==null)
			return false;
		if(!world.getBlockState(checkPos).isSideSolid(world, checkPos, EnumFacing.UP))
		{
			setDead();
			entityDropItem(originStack, 0f);
			return true;
		}
		return false;
	}

	@Nullable
	protected BlockPos getSupportCheckPos()
	{
		return getPosition().down();
	}

	public boolean isSetupComplete()
	{
		return maxSetupTime <= 0||setupTime >= maxSetupTime;
	}

	@SideOnly(Side.CLIENT)
	private void handleClientControls()
	{
		EntityPlayer player = Minecraft.getMinecraft().player;
		if(player.getRidingEntity()!=this)
			return;
		if(controls!=null&&controls.clientUpdate())
			sendServerUpdateForEvent(SyncEvents.ENTITY_VEHICLE_CONTROLS);
	}

	/**
	 * Sets the itemstack from which this weapon is created.
	 *
	 * @param stack the itemstack
	 * @implNote Override to check for ugprades to be applied
	 */
	protected void setOriginStack(ItemStack stack)
	{
		this.originStack = stack;
	}

	public final ItemStack getOriginStack()
	{
		return originStack;
	}

	//--- Collisions ---//


	@Override
	protected void setSize(float width, float height)
	{
		this.width = width;
		this.height = height;
		this.baseAabb = new AxisAlignedBB(-width/2f, 0, -width/2f, width/2f, height, width/2f);
	}

	@Override
	public final boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public final AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public final AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	public final AxisAlignedBB getEntityBoundingBox()
	{
		return baseAabb.offset(getPositionVector());
	}

	//--- Passenger ---//

	@Override
	public boolean canPassengerSteer()
	{
		return false;
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	public final void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			Vec3d pos = getPositionVector().add(getPassengerPosition(passenger));
			passenger.setPosition(pos.x, pos.y, pos.z);
		}
	}

	@Nonnull
	protected abstract Vec3d getPassengerPosition(Entity passenger);

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		else
			updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
		Vec3d pos = getPositionVector().add(getPassengerPosition(passenger));
		passenger.setPosition(pos.x, pos.y, pos.z);
		super.removePassenger(passenger);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		//Pick up weapon
		if(player.isSneaking()&&getPassengers().isEmpty())
		{
			setDead();
			if(!world.isRemote)
				entityDropItem(originStack, 0f);
			return true;
		}
		//Enter as passenger
		else if(isSetupComplete()&&player.getRidingEntity()!=this&&getPassengers().isEmpty())
		{
			player.startRiding(this);
			if(!world.isRemote)
				updateEntityForEvent(SyncEvents.ENTITY_PASSENGER);
			return true;
		}
		return true;
	}

	//--- NBT ---//

	@Override
	public final void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
		ByteBufUtils.writeItemStack(buffer, originStack);
	}

	@Override
	public final void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound tag = ByteBufUtils.readTag(additionalData);
		if(tag!=null)
			readEntityFromNBT(tag);
		setOriginStack(ByteBufUtils.readItemStack(additionalData));
	}

	@Override
	public final void readEntityFromNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.readEntityFromNBT(compound);
	}

	@Override
	public final void writeEntityToNBT(NBTTagCompound compound)
	{
		ISyncNBTEntity.super.writeEntityToNBT(compound);
	}

	//--- Misc ---//

	@Override
	public final boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public final void setFire(int seconds)
	{

	}

	@Override
	public final ItemStack getPickedResult(RayTraceResult target)
	{
		return originStack;
	}
}
