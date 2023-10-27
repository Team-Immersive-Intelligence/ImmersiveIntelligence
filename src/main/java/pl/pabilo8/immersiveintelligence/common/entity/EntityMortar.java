package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.client.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Mortar;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;

/**
 * @author Pabilo8
 * @since 21.01.2021
 */
public class EntityMortar extends Entity implements IEntityAdditionalSpawnData, IEntityZoomProvider
{
	private static final MortarSights SIGHTS = new MortarSights();

	private static final DataParameter<Boolean> dataMarkerGunPitchUp = EntityDataManager.createKey(EntityMortar.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Boolean> dataMarkerGunPitchDown = EntityDataManager.createKey(EntityMortar.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Float> dataMarkerShootingProgress = EntityDataManager.createKey(EntityMortar.class, DataSerializers.FLOAT);

	public boolean fireKeyPress = false, gunPitchUp = false, gunPitchDown = false;
	public int setupTime = 0;
	public float shootingProgress = 0f;

	public EntityMortar(World worldIn)
	{
		super(worldIn);
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(dataMarkerGunPitchUp, gunPitchUp);
		this.dataManager.register(dataMarkerGunPitchDown, gunPitchDown);
		this.dataManager.register(dataMarkerShootingProgress, shootingProgress);
		setSize(1f, 1.5f);
	}

	@Override
	public void onUpdate()
	{
		if(!world.isRemote&&world.getTotalWorldTime()%20==0&&!world.getBlockState(getPosition().down()).isSideSolid(world, getPosition().down(), EnumFacing.UP))
		{
			setDead();
			entityDropItem(this.getPickedResult(null), 0f);
		}

		if(setupTime < Mortar.setupTime)
			setupTime += 1;
		else
		{
			if(world.isRemote)
			{
				Entity pre = ClientUtils.mc().player.getRidingEntity();
				if(pre==this)
				{
					//Handle, send to server, get other from server
					handleClientKeyInput();
					handleClientKeyOutput(true);
				}
				else
				{
					//Get from server
					handleClientKeyOutput(false);
				}
			}
			else
			{
				handleServerKeyInput();
				if(getPassengers().size()==0)
				{
					gunPitchUp = false;
					gunPitchDown = false;
					shootingProgress = 0;
				}
			}

			if(shootingProgress==0)
			{
				if(fireKeyPress&&getPassengers().size() > 0)
				{
					Entity entity = getPassengers().get(0);
					if(entity instanceof EntityLivingBase)
					{
						ItemStack heldItem = ((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND);
						if(heldItem.getItem()==IIContent.itemAmmoMortar)
						{
							shootingProgress = 1;
							fireKeyPress = false;
						}
					}
				}
				else if(gunPitchUp)
					rotationPitch = MathHelper.clamp(rotationPitch+0.5f, -80, -55);
				else if(gunPitchDown)
					rotationPitch = MathHelper.clamp(rotationPitch-0.5f, -80, -55);
			}
			else
			{
				if(getPassengers().size() > 0)
				{
					Entity entity = getPassengers().get(0);
					if(entity instanceof EntityLivingBase)
					{
						ItemStack heldItem = ((EntityLivingBase)entity).getHeldItem(EnumHand.MAIN_HAND);
						if(heldItem.getItem()==IIContent.itemAmmoMortar||shootingProgress > Mortar.shootTime*0.45)
						{
							if(shootingProgress < Mortar.shootTime)
								shootingProgress++;
							else
							{
								shootingProgress = 0;

							}
							if(shootingProgress==Math.round(Mortar.shootTime*0.2f))
							{
								world.playSound(null, posX, posY, posZ, IISounds.mortarLoad, SoundCategory.PLAYERS, 1.25f, 1f);
							}
							if(shootingProgress==Math.round(Mortar.shootTime*0.55f))
							{
								if(!world.isRemote)
								{
									double true_angle = Math.toRadians((MathHelper.wrapDegrees(-rotationYaw+180)));
									double true_angle2 = Math.toRadians(rotationPitch);
									Vec3d gun_end = IIUtils.offsetPosDirection(2f, true_angle, true_angle2);

									world.playSound(null, posX, posY, posZ, IISounds.mortarShot, SoundCategory.PLAYERS, 1.25f, 1f);
									EntityBullet a = AmmoUtils.createBullet(world, heldItem.copy(), getPositionVector().add(gun_end.scale(-1)).addVector(0, 1, 0), gun_end.scale(-1).normalize());
									a.setShooters(this);
									world.spawnEntity(a);
									heldItem.shrink(1);
								}
							}
						}
						else
							shootingProgress = 0;
					}
				}
				else
					shootingProgress = 0;
			}

		}

		super.onUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return amount > 4;
	}

	private void handleClientKeyInput()
	{
		boolean hasChanged;

		boolean u = gunPitchUp, d = gunPitchDown, fk = fireKeyPress;
		gunPitchUp = ClientUtils.mc().gameSettings.keyBindForward.isKeyDown();
		gunPitchDown = ClientUtils.mc().gameSettings.keyBindBack.isKeyDown();
		fireKeyPress = Mouse.isButtonDown(1);
		hasChanged = u^gunPitchUp||d^gunPitchDown||fk^fireKeyPress;

		if(hasChanged)
			IIPacketHandler.sendToServer(new MessageEntityNBTSync(this, updateKeys()));
	}

	@SideOnly(Side.CLIENT)
	private NBTTagCompound updateKeys()
	{
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("gunPitchUp", gunPitchUp);
		compound.setBoolean("gunPitchDown", gunPitchDown);
		compound.setBoolean("fireKeyPress", fireKeyPress);
		return compound;
	}

	public void syncKeyPress(NBTTagCompound tag)
	{
		if(tag.hasKey("gunPitchUp"))
			gunPitchUp = tag.getBoolean("gunPitchUp");
		if(tag.hasKey("gunPitchDown"))
			gunPitchDown = tag.getBoolean("gunPitchDown");
		if(tag.hasKey("fireKeyPress"))
			fireKeyPress = tag.getBoolean("fireKeyPress");
	}

	private void handleClientKeyOutput(boolean user)
	{
		if(user)
		{
			dataManager.get(dataMarkerGunPitchUp);
			dataManager.get(dataMarkerGunPitchDown);
			dataManager.get(dataMarkerShootingProgress);
		}
		else
		{
			gunPitchUp = dataManager.get(dataMarkerGunPitchUp);
			gunPitchDown = dataManager.get(dataMarkerGunPitchDown);
			shootingProgress = dataManager.get(dataMarkerShootingProgress);
		}

	}

	private void handleServerKeyInput()
	{
		dataManager.set(dataMarkerGunPitchUp, gunPitchUp);
		dataManager.set(dataMarkerGunPitchDown, gunPitchDown);
		dataManager.set(dataMarkerShootingProgress, shootingProgress);
	}

	@SideOnly(Side.CLIENT)
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
		if(setupTime==Mortar.setupTime&&player.getRidingEntity()!=this&&this.getPassengers().size()==0)
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
		Vec3d pos = getPositionVector();
		float headYaw = MathHelper.wrapDegrees(this.rotationYaw);
		double true_angle = Math.toRadians((-headYaw) > 180?360f-(-headYaw): (-headYaw));
		double true_angle2 = Math.toRadians((-headYaw-90) > 180?360f-(-headYaw-90): (-headYaw-90));
		Vec3d pos2 = IIUtils.offsetPosDirection(0.125f, true_angle, 0);
		Vec3d pos3 = IIUtils.offsetPosDirection(-0.75f, true_angle2, 0);
		float ff = 1;
		if(shootingProgress > 0)
		{
			float v = shootingProgress/Mortar.shootTime;
			if(v < 0.1)
			{
				//rise up
				ff = 1f-(v/0.1f);
			}
			else if(v < 0.3)
			{
				//unturn
				ff = 0;
			}
			else if(v < 0.4)
			{
				//get down
				ff = (v-0.3f)/0.1f;
			}
		}

		passenger.setPosition(pos.x+pos2.x+pos3.x, pos.y-0.5*ff, pos.z+pos2.z+pos3.z);
		applyOrientationToEntity(passenger);
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		float yy = this.rotationYaw;
		if(setupTime==Mortar.setupTime)
		{
			yy += MathHelper.clamp((setupTime/(Mortar.setupTime*0.2)), 0, 1)*25;
		}
		if(shootingProgress > 0)
		{
			float ff = shootingProgress/Mortar.shootTime;
			if(ff < 0.3)
				yy += MathHelper.clamp(ff/0.1, 0, 1)*65;
			else if(ff < 0.4)
				yy += (1f-MathHelper.clamp((ff-0.3)/0.1, 0, 1))*65;
		}
		entityToUpdate.setRenderYawOffset(yy);

		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw-(this.rotationYaw));
		float f1 = MathHelper.clamp(f, -75.0F, 75.0F);
		entityToUpdate.prevRotationYaw += f1-f;
		entityToUpdate.rotationYaw += f1-f;

		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		setupTime = compound.getInteger("setupTime");
		gunPitchUp = compound.getBoolean("gunPitchUp");
		gunPitchDown = compound.getBoolean("gunPitchDown");
		shootingProgress = compound.getFloat("shootingProgress");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setInteger("setupTime", setupTime);
		compound.setBoolean("gunPitchUp", gunPitchUp);
		compound.setBoolean("gunPitchDown", gunPitchDown);
		compound.setFloat("shootingProgress", shootingProgress);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(IIContent.itemMortar);
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
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

	@Override
	public IAdvancedZoomTool getZoom()
	{
		return SIGHTS;
	}

	private static class MortarSights implements IAdvancedZoomTool
	{
		private static final ResourceLocation SIGHTS_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/mortar.png");

		@Override
		@SideOnly(Side.CLIENT)
		public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
		{
			return SIGHTS_TEXTURE;
		}

		@Override
		public boolean shouldZoom(ItemStack stack, EntityPlayer player)
		{
			Entity ridingEntity = player.getRidingEntity();
			return ridingEntity instanceof EntityMortar&&((EntityMortar)ridingEntity).shootingProgress==0;
		}

		@Override
		public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
		{
			Entity ridingEntity = player.getRidingEntity();
			if(ridingEntity instanceof EntityMortar)
				return new float[]{1f-Math.min(0.75f/(ridingEntity.rotationPitch/-90f), 0.975f)};
			return new float[]{0};
		}
	}
}
