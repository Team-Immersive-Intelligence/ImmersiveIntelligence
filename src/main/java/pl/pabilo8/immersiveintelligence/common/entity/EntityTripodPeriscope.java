package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIUtils;

import static pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Tools.TripodPeriscope.tripodZoomSteps;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class EntityTripodPeriscope extends Entity implements IEntityZoomProvider, IEntityAdditionalSpawnData
{
	public int setupTime = 0;
	public float periscopeYaw = 0, periscopeNextYaw = 0;
	private static final IAdvancedZoomTool ZOOM = new TripodZoom();

	public EntityTripodPeriscope(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		setSize(0.5f, 2f);
	}

	@Override
	public void onUpdate()
	{
		if(!world.isRemote&&world.getTotalWorldTime()%20==0&&!world.getBlockState(getPosition().down()).isSideSolid(world, getPosition().down(), EnumFacing.UP))
		{
			setDead();
			entityDropItem(this.getPickedResult(null), 0f);
		}

		//setupTime = 0;
		if(setupTime < TripodPeriscope.setupTime)
			setupTime += 1;
		else
		{
			if(getPassengers().size() > 0)
			{
				Entity user = getPassengers().get(0);

				aimAt(user.getRotationYawHead());
			}
		}

		super.onUpdate();
	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		super.removePassenger(passenger);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		if(!world.isRemote&&player.isSneaking()&&this.getPassengers().size()==0)
		{
			setDead();
			entityDropItem(this.getPickedResult(null), 0f);
			return true;
		}
		if(setupTime==TripodPeriscope.setupTime&&player.getRidingEntity()!=this&&this.getPassengers().size()==0)
		{
			player.startRiding(this);
			return true;
		}
		else
			return true;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			BlockPos pos = getPosition();
			float headYaw = MathHelper.wrapDegrees(this.periscopeYaw);
			double true_angle = Math.toRadians((-headYaw) > 180?360f-(-headYaw): (-headYaw));
			double true_angle2 = Math.toRadians((-headYaw-90) > 180?360f-(-headYaw-90): (-headYaw-90));
			Vec3d pos2 = IIUtils.offsetPosDirection(-0.5f, true_angle, 0);
			Vec3d pos3 = IIUtils.offsetPosDirection(-0.0625f/2f, true_angle2, 0);

			passenger.setPosition(pos.getX()+0.5+pos2.x+pos3.x, pos.getY(), pos.getZ()+0.5+pos2.z+pos3.z);
		}
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.setRenderYawOffset(this.periscopeYaw);
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		this.setupTime = compound.getInteger("setupTime");
		this.periscopeYaw = compound.getFloat("periscopeYaw");
		this.periscopeNextYaw = compound.getFloat("periscopeNextYaw");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("setupTime", setupTime);
		compound.setFloat("periscopeYaw", periscopeYaw);
		compound.setFloat("periscopeNextYaw", periscopeNextYaw);
	}

	public void aimAt(float yaw)
	{
		periscopeNextYaw = MathHelper.wrapDegrees(yaw);
		float y = MathHelper.wrapDegrees(360+periscopeNextYaw-this.periscopeYaw);
		this.periscopeYaw = MathHelper.wrapDegrees(periscopeYaw+(Math.signum(y)*MathHelper.clamp(Math.abs(y), 0, TripodPeriscope.turnSpeed)));
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(IIContent.itemTripodPeriscope);
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	@Override
	public IAdvancedZoomTool getZoom()
	{
		return ZOOM;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(this.setupTime);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		this.setupTime = additionalData.readInt();
	}

	private static class TripodZoom implements IAdvancedZoomTool
	{
		private static final ResourceLocation SIGHTS_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/binoculars.png");

		@Override
		@SideOnly(Side.CLIENT)
		public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
		{
			return SIGHTS_TEXTURE;
		}

		@Override
		public boolean shouldZoom(ItemStack stack, EntityPlayer player)
		{
			return true;
		}

		@Override
		public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
		{
			return tripodZoomSteps;
		}
	}
}
