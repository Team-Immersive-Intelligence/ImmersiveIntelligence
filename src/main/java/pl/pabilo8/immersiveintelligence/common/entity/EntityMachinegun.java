package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.camera.CameraHandler;
import pl.pabilo8.immersiveintelligence.api.utils.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.common.CommonProxy;
import pl.pabilo8.immersiveintelligence.common.blocks.types.IIBlockTypes_StoneDecoration;
import pl.pabilo8.immersiveintelligence.common.entity.bullets.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBullet;
import pl.pabilo8.immersiveintelligence.common.items.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageMachinegunSync;

import javax.annotation.Nullable;

import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun.clipReloadTime;
import static pl.pabilo8.immersiveintelligence.Config.IIConfig.Weapons.Machinegun.machinegun_scope_max_zoom;

/**
 * Created by Pabilo8 on 01-11-2019.
 */
public class EntityMachinegun extends Entity implements IEntityAdditionalSpawnData
{
	public static MachinegunZoom scope = new MachinegunZoom();
	//Second magazine is an upgrade
	public ItemStack gun, magazine1 = ItemStack.EMPTY, magazine2 = ItemStack.EMPTY;
	public int pickProgress = 0;
	public int bulletDelay = 0, bulletDelayMax = 0, clipReload = 0, setupTime = Machinegun.setupTime, maxSetupTime = Machinegun.setupTime, overheating = 0;
	public float setYaw = 0, recoilYaw = 0, recoilPitch = 0, gunYaw = 0, gunPitch = 0, maxRecoilPitch = Machinegun.recoilHorizontal, maxRecoilYaw = Machinegun.recoilVertical, currentlyLoaded = -1;
	public boolean shoot = false, aiming = false, hasSecondMag = false, mag1Empty = false, mag2Empty = false, hasInfrared = false, loadedFromCrate = false;
	AxisAlignedBB aabb = new AxisAlignedBB(0.15d, 0d, 0.15d, 0.85d, 0.65d, 0.85d);

	public EntityMachinegun(World worldIn)
	{
		super(worldIn);
	}

	public EntityMachinegun(World world, BlockPos pos, float yaw, float pitch, ItemStack stack)
	{
		super(world);
		float height = 0;
		this.gun = stack.copy();
		this.setSize(0.5f, 0.5f);
		if(blusunrize.immersiveengineering.common.util.Utils.isBlockAt(world, pos, CommonProxy.block_stone_decoration, IIBlockTypes_StoneDecoration.SANDBAGS.getMeta()))
		{
			height = -0.25f;
		}
		this.setPositionAndRotation(pos.getX()+0.5f, pos.getY()+1f+height, pos.getZ()+0.5f, yaw, pitch);
		this.setYaw = yaw;

		getConfigFromItem(stack);
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return aabb.offset(getPosition());
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBox(Entity entityIn)
	{
		return getEntityBoundingBox();
	}

	@Override
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	public void setFire(int seconds)
	{
	}

	@Override
	public boolean canPassengerSteer()
	{
		return false;
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		//Do stuff

		if(!world.isRemote&&world.getTotalWorldTime()%10==0)
		{
			if(world.getBlockState(getPosition().offset(EnumFacing.DOWN)).getMaterial().equals(Material.AIR))
			{
				dropItem();
				return;
			}
		}

		if(!world.isRemote&&hasInfrared&&getPassengers().size() > 0&&getPassengers().get(0)!=null&&world.getTotalWorldTime()%20==0)
		{

			if(aiming)
			{
				if(getPassengers().get(0) instanceof EntityLivingBase)
				{
					EntityLivingBase ent = (EntityLivingBase)getPassengers().get(0);
					int energy = 0;
					for(ItemStack stack : ent.getArmorInventoryList())
						if(EnergyHelper.isFluxItem(stack))
						{
							int out = EnergyHelper.extractFlux(stack, Machinegun.infraredScopeEnergyUsage*20, false);
							if(out > 0)
							{
								ent.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Math.round(out/(float)(Machinegun.infraredScopeEnergyUsage)*1.25f), 2, true, false));
								break;
							}
						}
				}
			}
		}

		if(!world.isRemote&&world.getTotalWorldTime()%120==0)
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setBoolean("forClient", true);
			writeEntityToNBT(tag);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageMachinegunSync(this, tag), Utils.targetPointFromEntity(this, 24));
		}

		if(getPassengers().size() > 0&&getPassengers().get(0)!=null)
		{
			if(recoilPitch!=0||recoilYaw!=0)
			{
				recoilPitch /= 2;
				recoilYaw /= 2;

				if(recoilPitch < 0.5f&&recoilPitch > -0.5f)
					recoilPitch = 0;
				if(recoilYaw < 0.5f&&recoilYaw > -0.5f)
					recoilYaw = 0;
			}

			if(getPassengers().get(0) instanceof EntityLivingBase)
			{
				EntityLivingBase psg = (EntityLivingBase)getPassengers().get(0);

				if(setupTime < 1)
				{
					doRotationsWithPlayer(psg);

					if(magazine1.isEmpty())
						mag1Empty = true;
					if(hasSecondMag&&magazine2.isEmpty())
						mag2Empty = true;

					if(!world.isRemote)
					{
						if(shoot)
						{
							if(clipReload==0)
							{
								if(bulletDelay < 1)
								{
									if(!loadedFromCrate)
									{
										if(hasSecondMag&&!mag2Empty)
											mag2Empty = !shoot(2);
										if(!mag1Empty)
											mag1Empty = !shoot(1);
									}
									else
									{

									}

								}
								else
									bulletDelay -= 1;
							}
						}
					}
					if(!shoot&&!loadedFromCrate)
					{
						if(mag1Empty||mag2Empty)
						{
							boolean b1 = false, b2 = false;
							if(currentlyLoaded==-1||currentlyLoaded==2)
								b1 = processEmptyMagazine(mag2Empty, magazine2, psg, EntityEquipmentSlot.OFFHAND, 2);
							if(currentlyLoaded==-1||currentlyLoaded==1)
								b2 = processEmptyMagazine(mag1Empty, magazine1, psg, EntityEquipmentSlot.MAINHAND, 1);

							if(!b1&&!b2)
								currentlyLoaded = -1;
						}
					}


				}
				else
				{
					setupTime -= 1;
				}

			}

			rotationYaw = MathHelper.wrapDegrees(setYaw+gunYaw+recoilYaw);

			rotationPitch = MathHelper.wrapDegrees(gunPitch+recoilPitch);
		}
		else
		{
			//gunPitch=Math.max(gunPitch-1,-25);
		}

		if(world.isRemote&&world.getTotalWorldTime()%2==0)
		{
			double true_angle = Math.toRadians(360f-rotationYaw);
			double true_angle2 = Math.toRadians(-(rotationPitch));

			Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.6875f, true_angle, true_angle2);
			Vec3d gun_height = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.1875f, true_angle, true_angle2+90);

			//ImmersiveEngineering.proxy.spawnRedstoneFX(this.world, posX+0.85*(gun_end.x+gun_height.x), posY+0.34375+0.85*(gun_end.y+gun_height.y), posZ+0.85*(gun_end.z+gun_height.z),0,1f,0,1.5f,0.75f,0.75f,0.75f);
		}

	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(!isDead&&!world.isRemote)
		{
			if(amount > 10||pickProgress > 3)
			{
				dropItem();
			}
			else
				pickProgress += 1;
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			BlockPos pos = getPosition();
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
			Vec3d pos2 = Utils.offsetPosDirection(-1.65f, true_angle, 0);
			Vec3d pos3 = Utils.offsetPosDirection(-0.25f, true_angle2, 0);

			passenger.setPosition(pos.getX()+0.5+pos2.x+pos3.x, pos.getY()-1.15, pos.getZ()+0.5+pos2.z+pos3.z);
		}
	}

	@Override
	public double getMountedYOffset()
	{
		return super.getMountedYOffset();
	}

	boolean processEmptyMagazine(boolean isEmpty, ItemStack magazine, EntityLivingBase entity, EntityEquipmentSlot takeFrom, int setTo)
	{
		if(isEmpty)
		{
			if(!magazine.isEmpty())
			{
				if(clipReload >= clipReloadTime)
				{
					currentlyLoaded = -1;
					clipReload = 0;
					ItemStack mag2 = magazine.copy();
					ItemIIBulletMagazine.makeDefault(mag2);
					if(!world.isRemote)
						blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, entity.getPosition(), mag2);
					setMagazineToSlot(setTo, ItemStack.EMPTY);
					return true;
				}
				else
				{
					currentlyLoaded = setTo;
					clipReload += 1;
					if(!world.isRemote&&clipReload < 2)
					{
						NBTTagCompound tag = new NBTTagCompound();
						writeEntityToNBT(tag);
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageMachinegunSync(this, tag), Utils.targetPointFromEntity(this, 24));
					}
					return true;
				}
			}
			else
			{
				if(clipReload==0)
				{
					if(entity.getItemStackFromSlot(takeFrom).getItem() instanceof ItemIIBulletMagazine)
					{
						if(ItemIIBulletMagazine.hasNoBullets(entity.getItemStackFromSlot(takeFrom)))
						{
							currentlyLoaded = -1;
							return false;
						}
						clipReload = 1;
						currentlyLoaded = setTo;
						return true;
					}
				}
				else if(clipReload < clipReloadTime)
				{
					if(entity.getItemStackFromSlot(takeFrom).getItem() instanceof ItemIIBulletMagazine)
					{
						clipReload += 1;
						currentlyLoaded = setTo;
						return true;
					}
					else
					{
						clipReload = 0;
						currentlyLoaded = -1;
						return false;
					}
				}
				else
				{
					if(entity.getItemStackFromSlot(takeFrom).getItem() instanceof ItemIIBulletMagazine)
					{
						clipReload = 0;
						setMagazineToSlot(setTo, entity.getItemStackFromSlot(takeFrom).copy());
						entity.setItemStackToSlot(takeFrom, ItemStack.EMPTY);
						setEmpty(setTo, false);
						currentlyLoaded = -1;
						if(!world.isRemote)
						{
							NBTTagCompound tag = new NBTTagCompound();
							writeEntityToNBT(tag);
							IIPacketHandler.INSTANCE.sendToAllAround(new MessageMachinegunSync(this, tag), Utils.targetPointFromEntity(this, 24));
						}
						return true;
					}
					else
					{
						clipReload = 0;
						currentlyLoaded = -1;
						return false;
					}

				}
			}
		}
		return false;
	}

	void setMagazineToSlot(int mag, ItemStack stack)
	{
		switch(mag)
		{
			case 1:
			{
				magazine1 = stack;
			}
			break;
			case 2:
			{
				magazine2 = stack;
			}
			break;
		}
	}

	void setEmpty(int mag, boolean is)
	{
		switch(mag)
		{
			case 1:
			{
				mag1Empty = is;
			}
			break;
			case 2:
			{
				mag2Empty = is;
			}
			break;
		}
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		if(!compound.hasKey("clientMessage"))
		{
			if(compound.hasKey("currentlyLoaded"))
				currentlyLoaded = compound.getFloat("currentlyLoaded");
			if(compound.hasKey("magazine1"))
				magazine1 = new ItemStack(compound.getCompoundTag("magazine1"));
			if(compound.hasKey("magazine2"))
				magazine2 = new ItemStack(compound.getCompoundTag("magazine2"));
			if(compound.hasKey("gun"))
			{
				gun = new ItemStack(compound.getCompoundTag("gun"));
				if(!world.isRemote)
					getConfigFromItem(new ItemStack(compound.getCompoundTag("gun")));
			}

			if(compound.hasKey("overheating"))
				overheating = compound.getInteger("overheating");
			if(compound.hasKey("mag1Empty"))
				mag1Empty = compound.getBoolean("mag1Empty");
			if(compound.hasKey("mag2Empty"))
				mag2Empty = compound.getBoolean("mag2Empty");
			if(compound.hasKey("setYaw"))
				setYaw = compound.getFloat("setYaw");
			if(compound.hasKey("gunYaw"))
				gunYaw = compound.getFloat("gunYaw");
			if(compound.hasKey("gunPitch"))
				gunPitch = compound.getFloat("gunPitch");
			if(compound.hasKey("recoilYaw"))
				recoilYaw = compound.getFloat("recoilYaw");
			if(compound.hasKey("recoilPitch"))
				recoilPitch = compound.getFloat("recoilPitch");

			if(compound.hasKey("clipReload"))
				clipReload = compound.getInteger("clipReload");
			if(compound.hasKey("setupTime"))
				setupTime = compound.getInteger("setupTime");
			if(compound.hasKey("maxSetupTime"))
				maxSetupTime = compound.getInteger("maxSetupTime");
		}
		else
		{
			if(compound.hasKey("shoot"))
				shoot = compound.getBoolean("shoot");
			if(compound.hasKey("aiming"))
				aiming = compound.getBoolean("aiming");
			//if(compound.hasKey("reload"))
			//	shoot = compound.getBoolean("shoot");
		}

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setTag("gun", gun.serializeNBT());
		compound.setTag("magazine1", magazine1.serializeNBT());
		compound.setTag("magazine2", magazine2.serializeNBT());

		compound.setBoolean("mag1Empty", mag1Empty);
		compound.setBoolean("mag2Empty", mag2Empty);
		compound.setFloat("currentlyLoaded", currentlyLoaded);

		compound.setInteger("overheating", overheating);
		compound.setFloat("setYaw", setYaw);
		compound.setFloat("gunYaw", gunYaw);
		compound.setFloat("gunPitch", gunPitch);
		compound.setFloat("recoilYaw", recoilYaw);
		compound.setFloat("recoilPitch", recoilPitch);

		compound.setInteger("clipReload", clipReload);
		compound.setInteger("setupTime", setupTime);
		compound.setInteger("maxSetupTime", maxSetupTime);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeItemStack(buffer, gun);
		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		ByteBufUtils.writeTag(buffer, tag);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		gun = ByteBufUtils.readItemStack(additionalData);
		NBTTagCompound tag = ByteBufUtils.readTag(additionalData);
		if(tag!=null)
			readEntityFromNBT(tag);
	}

	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		if(player.isSneaking())
		{
			return false;
		}
		else
		{
			if(!this.world.isRemote)
			{
				player.startRiding(this);
			}

			return true;
		}
	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.INSTANCE.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		shoot = false;
		aiming = false;
		super.removePassenger(passenger);
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public void applyOrientationToEntity(Entity entityToUpdate)
	{
		entityToUpdate.setRenderYawOffset(this.rotationYaw);
		float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw-this.setYaw);
		float f1 = MathHelper.clamp(f, -45.0F, 45.0F);
		entityToUpdate.prevRotationYaw += f1-f;
		entityToUpdate.rotationYaw += f1-f;
		entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
	}

	public void doRotationsWithPlayer(EntityLivingBase entity)
	{
		float true_head_angle;

		true_head_angle = MathHelper.wrapDegrees(entity.rotationYawHead-this.setYaw);

		if(gunYaw < true_head_angle)
			gunYaw += 2;
		else if(gunYaw > true_head_angle)
			gunYaw -= 2;

		if(Math.ceil(gunYaw) <= Math.ceil(true_head_angle)+1&&Math.ceil(gunYaw) >= Math.ceil(true_head_angle)-1)
			gunYaw = true_head_angle;

		if(gunPitch < entity.rotationPitch)
			gunPitch += 1;
		else if(gunPitch > entity.rotationPitch)
			gunPitch -= 1;

		if(Math.ceil(gunPitch) <= Math.ceil(entity.rotationPitch)+1&&Math.ceil(gunPitch) >= Math.ceil(entity.rotationPitch)-1)
			gunPitch = entity.rotationPitch;

		gunYaw = MathHelper.clamp(gunYaw, -45, 45);
		gunPitch = MathHelper.clamp(gunPitch, -20, 20);
	}

	public boolean shoot(int magazine)
	{
		//TODO: Shooting sound

		if(getPassengers().get(0)==null||!(getPassengers().get(0) instanceof EntityLivingBase))
			return false;

		double true_angle = Math.toRadians(360f-rotationYaw);
		double true_angle2 = Math.toRadians(-(rotationPitch));

		bulletDelay = bulletDelayMax;
		recoilYaw += Math.random() > 0.5?maxRecoilYaw*2*Math.random(): -maxRecoilYaw*2*Math.random();
		recoilPitch += maxRecoilPitch*Math.random();

		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("forClient", true);
		tag.setFloat("recoilYaw", recoilYaw);
		tag.setFloat("recoilPitch", recoilPitch);

		Vec3d gun_end = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.95f, true_angle, true_angle2);
		Vec3d gun_height = pl.pabilo8.immersiveintelligence.api.Utils.offsetPosDirection(0.1875f, true_angle, true_angle2+90);

		ItemStack stack = magazine==1?ItemIIBulletMagazine.takeBullet(magazine1): ItemIIBulletMagazine.takeBullet(magazine2);
		if(stack.isEmpty())
		{
			if(magazine==1)
				mag1Empty = true;
			else if(magazine==2)
				mag2Empty = true;
			return false;
		}

		tag.setBoolean("mag1Empty", mag1Empty);
		tag.setBoolean("mag2Empty", mag2Empty);

		EntityBullet a = new EntityBullet(world, posX+0.85*(gun_end.x+gun_height.x), posY+0.34375+0.85*(gun_end.y+gun_height.y), posZ+0.85*(gun_end.z+gun_height.z), (EntityLivingBase)getPassengers().get(0), stack);
		//blocks per tick
		float distance = 6.0f;
		a.motionX = distance*(gun_end.x);
		a.motionY = distance*(gun_end.y);
		a.motionZ = distance*(gun_end.z);
		a.world.spawnEntity(a);

		ItemStack stack2 = ItemIIBullet.getCasing(stack).getStack(1);
		blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), stack2);

		if(!world.isRemote)
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageMachinegunSync(this, tag), Utils.targetPointFromEntity(this, 24));

		return true;
	}

	public void getConfigFromItem(ItemStack stack)
	{
		gun = stack;
		float setup_multiplier = 1;
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("precise_bipod"))
			setup_multiplier = Machinegun.preciseBipodSetupTimeMultiplier;
		else if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("hasty_bipod"))
			setup_multiplier = Machinegun.hastyBipodSetupTimeMultiplier;
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("belt_fed_loader"))
			setup_multiplier *= Machinegun.beltFedLoaderSetupTimeMultiplier;

		setupTime = Math.round(Machinegun.setupTime*setup_multiplier);
		maxSetupTime = setupTime;

		float bullet_delay_multiplier = 1;
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("heavy_barrel"))
			bullet_delay_multiplier = Machinegun.heavyBarrelFireRateMultiplier;
		else if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("water_cooling"))
			bullet_delay_multiplier = Machinegun.waterCoolingFireRateMultiplier;

		bulletDelayMax = Math.round(bullet_delay_multiplier*Machinegun.bulletFireTime);
		bulletDelay = 0;

		float recoil_multiplier_h = 1, recoil_multiplier_w = 1;
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("precise_bipod"))
		{
			recoil_multiplier_w *= Machinegun.preciseBipodRecoilMultiplier;
			recoil_multiplier_h *= Machinegun.preciseBipodRecoilMultiplier;
		}
		else if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("hasty_bipod"))
		{
			recoil_multiplier_w *= Machinegun.hastyBipodRecoilMultiplier;
			recoil_multiplier_h *= Machinegun.hastyBipodRecoilMultiplier;
		}
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("heavy_barrel"))
		{
			recoil_multiplier_w *= Machinegun.recoilHBHorizontal;
			recoil_multiplier_h *= Machinegun.recoilHBVertical;
		}
		if(CommonProxy.item_machinegun.getUpgrades(gun).hasKey("second_magazine"))
		{
			recoil_multiplier_w *= Machinegun.recoilSecondMagazine;
		}

		hasSecondMag = CommonProxy.item_machinegun.getUpgrades(gun).hasKey("second_magazine");

		maxRecoilYaw *= recoil_multiplier_w;
		maxRecoilPitch *= recoil_multiplier_h;

		if(ItemNBTHelper.hasKey(gun, "magazine1"))
			magazine1 = new ItemStack(ItemNBTHelper.getTagCompound(gun, "magazine1"));
		if(ItemNBTHelper.hasKey(gun, "magazine2"))
			magazine2 = new ItemStack(ItemNBTHelper.getTagCompound(gun, "magazine2"));

		if(!hasSecondMag&&!magazine2.isEmpty()&&!world.isRemote)
		{
			blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), stack);
		}

		hasInfrared = CommonProxy.item_machinegun.getUpgrades(gun).hasKey("infrared_scope");
		loadedFromCrate = CommonProxy.item_machinegun.getUpgrades(gun).hasKey("belt_fed_loader");

		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageMachinegunSync(this, tag), Utils.targetPointFromEntity(this, 24));

	}

	void dropItem()
	{
		ItemNBTHelper.setTagCompound(gun, "magazine1", magazine1.serializeNBT());
		ItemNBTHelper.setTagCompound(gun, "magazine2", magazine2.serializeNBT());
		blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), gun);
		setDead();
	}

	public static class MachinegunZoom implements IAdvancedZoomTool
	{

		@Override
		public String getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
		{
			NBTTagCompound nbt = CommonProxy.item_machinegun.getUpgrades(stack);
			return ImmersiveIntelligence.MODID+":textures/gui/item/machinegun/"+(nbt.hasKey("scope")?"scope": "scope_infrared")+".png";
		}

		@Override
		public boolean canZoom(ItemStack stack, EntityPlayer player)
		{
			NBTTagCompound nbt = CommonProxy.item_machinegun.getUpgrades(stack);
			return nbt.hasKey("scope")||nbt.hasKey("infrared_scope");
		}

		@Override
		public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
		{
			return machinegun_scope_max_zoom;
		}
	}
}
