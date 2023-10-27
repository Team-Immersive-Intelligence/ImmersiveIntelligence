package pl.pabilo8.immersiveintelligence.common.entity;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.api.tool.ZoomHandler;
import blusunrize.immersiveengineering.common.util.EnergyHelper;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import blusunrize.immersiveengineering.common.util.Utils;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons.Machinegun;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.MachinegunCoolantHandler;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoUtils;
import pl.pabilo8.immersiveintelligence.api.bullets.IAmmo;
import pl.pabilo8.immersiveintelligence.api.utils.IEntitySpecialRepairable;
import pl.pabilo8.immersiveintelligence.api.utils.camera.IEntityZoomProvider;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedTextOverlay;
import pl.pabilo8.immersiveintelligence.api.utils.tools.IAdvancedZoomTool;
import pl.pabilo8.immersiveintelligence.client.util.CameraHandler;
import pl.pabilo8.immersiveintelligence.common.IIContent;
import pl.pabilo8.immersiveintelligence.common.IIPotions;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.IIUtils;
import pl.pabilo8.immersiveintelligence.common.block.metal_device.tileentity.effect_crate.TileEntityAmmunitionCrate;
import pl.pabilo8.immersiveintelligence.common.entity.bullet.EntityBullet;
import pl.pabilo8.immersiveintelligence.common.item.ammo.ItemIIBulletMagazine;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessagePlayerAimAnimationSync;
import pl.pabilo8.immersiveintelligence.common.util.AdvancedSounds.RangedSound;

import javax.annotation.Nullable;

/**
 * @author Pabilo8
 * @since 01-11-2019
 */
public class EntityMachinegun extends Entity implements IEntityAdditionalSpawnData, IAdvancedTextOverlay, IEntitySpecialRepairable, IEntityZoomProvider
{
	private static final ResourceLocation SIGHTS_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/machinegun/scope.png");
	private static final ResourceLocation IR_SIGHTS_TEXTURE = new ResourceLocation(ImmersiveIntelligence.MODID, "textures/gui/item/machinegun/scope_infrared.png");

	//TODO: switch nbt sending to DataParameters
	private static final DataParameter<NBTTagCompound> dataMarkerFluid = EntityDataManager.createKey(EntityMachinegun.class, DataSerializers.COMPOUND_TAG);
	private static final DataParameter<Integer> dataMarkerFluidCap = EntityDataManager.createKey(EntityMachinegun.class, DataSerializers.VARINT);

	private final MachinegunZoom SCOPE = new MachinegunZoom();
	//Second magazine is an upgrade
	public ItemStack gun = ItemStack.EMPTY, magazine1 = ItemStack.EMPTY, magazine2 = ItemStack.EMPTY;
	public int bulletDelay = 0, bulletDelayMax = 0, clipReload = 0, setupTime = Machinegun.setupTime, maxSetupTime = Machinegun.setupTime, overheating = 0, tankCapacity = 0, bullets1 = 0, bullets2 = 0;
	public float setYaw = 0, recoilYaw = 0, recoilPitch = 0, gunYaw = 0, gunPitch = 0, maxRecoilPitch = Machinegun.recoilHorizontal, maxRecoilYaw = Machinegun.recoilVertical, currentlyLoaded = -1, shieldStrength = 0f, maxShieldStrength = 0f;
	public boolean shoot = false, aiming = false, hasSecondMag = false, mag1Empty = false, mag2Empty = false, hasInfrared = false, loadedFromCrate = false, overheated = false, tripod = false;
	public FluidTank tank = new FluidTank(tankCapacity);

	AxisAlignedBB aabb = new AxisAlignedBB(0.15d, 0d, 0.15d, 0.85d, 0.65d, 0.85d).offset(-0.5, 0, -0.5);
	NonSidedFluidHandler fluidHandler = new NonSidedFluidHandler(this);


	public EntityMachinegun(World worldIn)
	{
		super(worldIn);
	}

	public EntityMachinegun(World world, BlockPos pos, float yaw, float pitch, ItemStack stack)
	{
		super(world);
		float height = 0;
		this.gun = stack.copy();
		getConfigFromItem(this.gun);

		this.setSize(0.5f, tripod?1.625f: 0.5f);
		AxisAlignedBB aabb = world.getBlockState(pos).getBoundingBox(world, pos);
		height -= 1f-(aabb.maxY-aabb.minY);
		this.setPositionAndRotation(pos.getX()+0.5f, pos.getY()+1f+height+(tripod?0.875f: 0f), pos.getZ()+0.5f, yaw, pitch);
		this.setYaw = yaw;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if(!isDead)
		{
			if(source.isProjectile())
			{
				Vec3d v = source.getDamageLocation();
				if(v!=null)
				{
					v = v.subtract(posX, posY, posZ);
					if(getHorizontalFacing()==EnumFacing.getFacingFromVector((float)v.x, (float)v.y, (float)v.z))
					{
						this.shieldStrength -= amount;
						if(shieldStrength > 0)
							return false;
					}
				}
			}
			/*
			if(!world.isRemote&&amount > 10)
			{
				dropItem();
				if(shoot&&(!mag1Empty||!mag2Empty))
					world.createExplosion(source.getTrueSource(), this.posX, this.posY+0.5, this.posZ, 1, true);
			}
			 */
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return gun;
	}

	@Override
	public AxisAlignedBB getEntityBoundingBox()
	{
		return aabb.offset(getPositionVector());
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
			if(getRidingEntity()==null&&world.getBlockState(getPosition().offset(EnumFacing.DOWN, tripod?2: 1)).getMaterial().equals(Material.AIR))
			{
				dropItem();
				return;
			}
			else if(getRidingEntity()!=null)
			{
				this.setYaw = getRidingEntity().getRotationYawHead();
				setEntityBoundingBox(aabb);
				setPositionAndUpdate(posX, posY, posZ);
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

		if(world.getTotalWorldTime()%120==0)
		{
			if(world.isRemote)
				updateTank(true);
			else
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setBoolean("forClient", true);
				writeEntityToNBT(tag);
				IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
			}
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

					if(overheating > Machinegun.maxOverheat)
					{
						overheated = true;
					}
					overheating = Math.max(0, overheating-1);
					if(world.getTotalWorldTime()%2==0&&overheating > 0&&tank.getFluid()!=null&&tank.getFluidAmount() >= Machinegun.waterCoolingFluidUsage)
					{
						overheating = Math.max(0, overheating-MachinegunCoolantHandler.getCoolAmount(tank.getFluid()));
						tank.drain(Machinegun.waterCoolingFluidUsage, true);
					}

					if(overheated&&overheating==0)
						overheated = false;

					if(!world.isRemote)
					{
						if(!overheated)
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
											else if(!mag1Empty)
												mag1Empty = !shoot(1);
										}
										else
										{
											shootFromCrate();
										}

									}
									else
										bulletDelay -= 1;
								}
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
			if(!world.isRemote&&setupTime > 0)
			{
				dropItem();
				return;
			}
			gunPitch = Math.max(gunPitch-1, -25);
		}

	}

	@Override
	public void updatePassenger(Entity passenger)
	{
		if(this.isPassenger(passenger))
		{
			BlockPos pos = getPosition();
			double true_angle = Math.toRadians((-rotationYaw) > 180?360f-(-rotationYaw): (-rotationYaw));
			double true_angle2 = Math.toRadians((-rotationYaw-90) > 180?360f-(-rotationYaw-90): (-rotationYaw-90));
			Vec3d pos2 = IIUtils.offsetPosDirection(-1.65f, true_angle, 0);
			Vec3d pos3 = IIUtils.offsetPosDirection(-0.25f, true_angle2, 0);

			passenger.setPosition(pos.getX()+0.5+pos2.x+pos3.x, pos.getY()-1.15, pos.getZ()+0.5+pos2.z+pos3.z);
		}
	}

	@Override
	public double getMountedYOffset()
	{
		return super.getMountedYOffset();
	}

	@Override
	public boolean shouldRiderSit()
	{
		return false;
	}

	boolean processEmptyMagazine(boolean isEmpty, ItemStack magazine, EntityLivingBase entity, EntityEquipmentSlot takeFrom, int setTo)
	{
		if(isEmpty)
		{
			ItemStack playerMag = entity.getItemStackFromSlot(takeFrom);
			if(!magazine.isEmpty())
			{
				if(clipReload >= Machinegun.clipReloadTime)
				{
					currentlyLoaded = -1;
					clipReload = 0;
					ItemStack mag2 = magazine.copy();
					IIContent.itemBulletMagazine.defaultize(mag2);
					if(!world.isRemote)
						blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, entity.getPosition(), mag2);
					setMagazineToSlot(setTo, ItemStack.EMPTY);
				}
				else
				{
					currentlyLoaded = setTo;
					clipReload += 1;
					if(!world.isRemote&&clipReload < 2)
					{
						NBTTagCompound tag = new NBTTagCompound();
						writeEntityToNBT(tag);
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
					}
					if(!world.isRemote&&clipReload==1)
						world.playSound(null, getPosition(), IISounds.machinegunUnload, SoundCategory.BLOCKS, 1F, 1f);
				}
				return true;
			}
			else
			{
				if(clipReload==0)
				{
					if(playerMag.getItem() instanceof ItemIIBulletMagazine&&playerMag.getMetadata()==0)
					{
						if(IIContent.itemBulletMagazine.hasNoBullets(playerMag))
						{
							currentlyLoaded = -1;
							return false;
						}
						clipReload = 1;
						currentlyLoaded = setTo;
						return true;
					}
				}
				else if(clipReload < Machinegun.clipReloadTime)
				{
					if(playerMag.getItem() instanceof ItemIIBulletMagazine&&playerMag.getMetadata()==0)
					{
						clipReload += 1;
						currentlyLoaded = setTo;
						if(!world.isRemote&&clipReload==Math.round(Machinegun.clipReloadTime*0.35f))
							world.playSound(null, getPosition(), IISounds.machinegunReload, SoundCategory.BLOCKS, 1F, 1f);
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
					if(playerMag.getItem() instanceof ItemIIBulletMagazine&&playerMag.getMetadata()==0)
					{
						clipReload = 0;
						setMagazineToSlot(setTo, playerMag.copy());
						entity.setItemStackToSlot(takeFrom, ItemStack.EMPTY);
						setEmpty(setTo, false);
						currentlyLoaded = -1;
						if(!world.isRemote)
						{
							NBTTagCompound tag = new NBTTagCompound();
							bullets1 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine1);
							bullets2 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine2);
							writeEntityToNBT(tag);
							IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
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
		this.dataManager.register(dataMarkerFluid, new NBTTagCompound());
		this.dataManager.register(dataMarkerFluidCap, 0);
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
			if(compound.hasKey("bullets1"))
				bullets1 = compound.getInteger("bullets1");
			else if(!magazine1.isEmpty())
				bullets1 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine1);
			if(compound.hasKey("bullets2"))
				bullets2 = compound.getInteger("bullets2");
			else if(!magazine2.isEmpty())
				bullets2 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine2);
			if(compound.hasKey("gun"))
			{
				gun = new ItemStack(compound.getCompoundTag("gun"));
				if(!world.isRemote)
					getConfigFromItem(new ItemStack(compound.getCompoundTag("gun")));
			}

			if(compound.hasKey("shieldStrength"))
				shieldStrength = compound.getInteger("shieldStrength");
			if(compound.hasKey("maxShieldStrength"))
				maxShieldStrength = compound.getInteger("maxShieldStrength");
			if(compound.hasKey("overheating"))
				overheating = compound.getInteger("overheating");
			if(compound.hasKey("tankCapacity"))
				tankCapacity = compound.getInteger("tankCapacity");
			if(compound.hasKey("mag1Empty"))
				mag1Empty = compound.getBoolean("mag1Empty");
			if(compound.hasKey("mag2Empty"))
				mag2Empty = compound.getBoolean("mag2Empty");
			if(compound.hasKey("hasSecondMag"))
				hasSecondMag = compound.getBoolean("hasSecondMag");
			if(compound.hasKey("tripod"))
				tripod = compound.getBoolean("tripod");
			if(compound.hasKey("loadedFromCrate"))
				loadedFromCrate = compound.getBoolean("loadedFromCrate");
			if(compound.hasKey("hasInfrared"))
				hasInfrared = compound.getBoolean("hasInfrared");
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
			{
				aiming = compound.getBoolean("aiming");
				if(isBeingRidden())
					IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(getPassengers().get(0), aiming), this.world.provider.getDimension());
			}

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
		compound.setBoolean("hasSecondMag", hasSecondMag);
		compound.setBoolean("hasInfrared", hasInfrared);
		compound.setBoolean("loadedFromCrate", loadedFromCrate);
		compound.setBoolean("tripod", tripod);
		compound.setFloat("currentlyLoaded", currentlyLoaded);

		compound.setFloat("shieldStrength", shieldStrength);
		compound.setFloat("maxShieldStrength", maxShieldStrength);

		compound.setInteger("overheating", overheating);
		compound.setInteger("tankCapacity", tankCapacity);
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
		FluidStack f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.MAIN_HAND));
		if(f==null)
			f = FluidUtil.getFluidContained(player.getHeldItem(EnumHand.OFF_HAND));

		if(f!=null&&this.tankCapacity > 0)
		{
			if(MachinegunCoolantHandler.isValidCoolant(f))
			{
				FluidUtil.interactWithFluidHandler(player, hand, tank);
				if(!world.isRemote)
					updateTank(false);
			}
			return true;
		}
		else if(player.isSneaking()&&player.getHeldItem(hand).isEmpty()&&getPassengers().size()==0)
		{
			if(!world.isRemote)
				dropItem();
			return true;
		}
		else
		{
			if(!world.isRemote)
				player.startRiding(this);
			return true;
		}
	}

	@Override
	protected void removePassenger(Entity passenger)
	{
		if(world.isRemote&&passenger instanceof EntityPlayerSP)
		{
			CameraHandler.setEnabled(false);
			ZoomHandler.isZooming = false;
		}
		shoot = false;
		aiming = false;
		if(!world.isRemote)
			IIPacketHandler.INSTANCE.sendToDimension(new MessagePlayerAimAnimationSync(passenger, false), this.world.provider.getDimension());
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
		float f1 = tripod?MathHelper.clamp(f, -82.5F, 82.5F): MathHelper.clamp(f, -45.0F, 45.0F);
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

		gunYaw = tripod?MathHelper.clamp(gunYaw, -82.5F, 82.5F): MathHelper.clamp(gunYaw, -45.0F, 45.0F);
		gunPitch = MathHelper.clamp(gunPitch, -20, 20);
	}

	public void shootFromStack(ItemStack stack)
	{
		if(stack.isEmpty())
		{
			world.playSound(null, posX, posY, posZ, IISounds.machinegunShotDry, SoundCategory.PLAYERS, 0.25f, 0.9f);
			return;
		}
		NBTTagCompound upgrades = IIContent.itemMachinegun.getUpgrades(gun);

		RangedSound sound = IISounds.machinegunShot;
		if(upgrades.hasKey("heavy_barrel"))
			sound = IISounds.machinegunShotHeavyBarrel;
		else if(upgrades.hasKey("water_cooling"))
			sound = IISounds.machinegunShotWaterCooled;

		IIPacketHandler.playRangedSound(world, getPositionVector(),
				sound, SoundCategory.PLAYERS, 75, 1.5f,
				1f+(float)(Utils.RAND.nextGaussian()*0.02)
		);

		double true_angle = Math.toRadians(360f-rotationYaw);
		double true_angle2 = Math.toRadians(-(rotationPitch));
		Vec3d gun_end = IIUtils.offsetPosDirection(0.95f, true_angle, true_angle2);
		Vec3d gun_height = IIUtils.offsetPosDirection(0.1875f, true_angle, true_angle2+90);

		Vec3d vpos = new Vec3d(posX+0.85*(gun_end.x+gun_height.x), posY+0.34375+0.85*(gun_end.y+gun_height.y), posZ+0.85*(gun_end.z+gun_height.z));
		EntityBullet b = AmmoUtils.createBullet(world, stack, vpos, gun_end);
		b.setShooters(getPassengers().get(0), this);
		world.spawnEntity(b);

		ItemStack stack2 = ((IAmmo)stack.getItem()).getCasingStack(1);
		blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), stack2);
	}

	public boolean shootFromCrate()
	{
		if(getPassengers().get(0)==null||!(getPassengers().get(0) instanceof EntityLivingBase))
			return false;

		blusunrize.immersiveengineering.common.util.Utils.attractEnemies((EntityLivingBase)getPassengers().get(0), 36, null);

		bulletDelay = bulletDelayMax;
		recoilYaw += Math.random() > 0.5?maxRecoilYaw*2*Math.random(): -maxRecoilYaw*2*Math.random();
		recoilPitch += maxRecoilPitch*Math.random();
		if(((EntityLivingBase)getPassengers().get(0)).getActivePotionEffects().stream().anyMatch(potionEffect -> potionEffect.getPotion()==IIPotions.ironWill))
		{
			recoilYaw *= 0.1;
			recoilPitch *= 0.1;
		}

		overheating += 8;

		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("forClient", true);
		tag.setFloat("recoilYaw", recoilYaw);
		tag.setFloat("recoilPitch", recoilPitch);
		tag.setFloat("overheating", overheating);

		BlockPos cratePos = getPosition().offset(EnumFacing.fromAngle(setYaw).getOpposite()).down();
		if(world.getTileEntity(cratePos) instanceof TileEntityAmmunitionCrate)
		{
			TileEntityAmmunitionCrate crate = ((TileEntityAmmunitionCrate)world.getTileEntity(cratePos));
			assert crate!=null;

			if(!crate.open)
				return false;

			if(crate.hasUpgrade(IIContent.UPGRADE_MG_LOADER))
			{
				for(int i = 38; i < 50; i++)
				{
					ItemStack stack = crate.getInventory().get(i);
					if(stack.isEmpty())
						continue;
					shootFromStack(stack);

					crate.insertionHandler.extractItem(i, 1, false);
					if(!world.isRemote)
						IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
					return true;
				}
			}
		}
		return false;
	}

	public boolean shoot(int magazine)
	{
		if(getPassengers().get(0)==null||!(getPassengers().get(0) instanceof EntityLivingBase))
			return false;

		blusunrize.immersiveengineering.common.util.Utils.attractEnemies((EntityLivingBase)getPassengers().get(0), 36, null);

		bulletDelay = bulletDelayMax;
		recoilYaw += Math.random() > 0.5?maxRecoilYaw*2*Math.random(): -maxRecoilYaw*2*Math.random();
		recoilPitch += maxRecoilPitch*Math.random();
		if(((EntityLivingBase)getPassengers().get(0)).getActivePotionEffects().stream().anyMatch(potionEffect -> potionEffect.getPotion()==IIPotions.ironWill))
		{
			recoilYaw *= 0.1;
			recoilPitch *= 0.1;
		}

		overheating += 8;

		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("forClient", true);
		tag.setFloat("recoilYaw", recoilYaw);
		tag.setFloat("recoilPitch", recoilPitch);
		tag.setFloat("overheating", overheating);

		ItemStack stack = IIContent.itemBulletMagazine.takeBullet(magazine==1?magazine1: magazine2, true);

		if(magazine==1)
		{
			mag1Empty = stack.isEmpty();
			if(!mag1Empty)
			{
				bullets1 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine1);
				tag.setInteger("bullets1", bullets1);
			}
		}
		else if(magazine==2)
		{
			mag2Empty = stack.isEmpty();
			if(!mag2Empty)
			{
				bullets2 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine2);
				tag.setInteger("bullets2", bullets2);
			}
		}

		tag.setBoolean("mag1Empty", mag1Empty);
		tag.setBoolean("mag2Empty", mag2Empty);

		if(!world.isRemote)
		{
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
			shootFromStack(stack);
		}

		return true;
	}

	public void getConfigFromItem(ItemStack stack)
	{
		gun = stack;
		float setup_multiplier = 1;
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("precise_bipod"))
			setup_multiplier = Machinegun.preciseBipodSetupTimeMultiplier;
		else if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("hasty_bipod"))
			setup_multiplier = Machinegun.hastyBipodSetupTimeMultiplier;
		else if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("tripod"))
		{
			this.tripod = true;
			setup_multiplier = Machinegun.tripodSetupTimeMultiplier;
		}
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("belt_fed_loader"))
			setup_multiplier *= Machinegun.beltFedLoaderSetupTimeMultiplier;
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("shield"))
		{
			shieldStrength = Machinegun.shieldStrengthInitial;
			maxShieldStrength = Machinegun.shieldStrengthInitial;
			setup_multiplier *= Machinegun.shieldSetupTimeMultiplier;
		}

		setupTime = Math.round(Machinegun.setupTime*setup_multiplier);
		maxSetupTime = setupTime;

		float bullet_delay_multiplier = 1;
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("heavy_barrel"))
			bullet_delay_multiplier = Machinegun.heavyBarrelFireRateMultiplier;
		else if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("water_cooling"))
		{
			tankCapacity = Machinegun.waterCoolingTankCapacity;
		}

		bulletDelayMax = Math.round(bullet_delay_multiplier*Machinegun.bulletFireTime);
		bulletDelay = 0;

		float recoil_multiplier_h = 1, recoil_multiplier_w = 1;
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("precise_bipod"))
		{
			recoil_multiplier_w *= Machinegun.preciseBipodRecoilMultiplier;
			recoil_multiplier_h *= Machinegun.preciseBipodRecoilMultiplier;
		}
		else if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("hasty_bipod"))
		{
			recoil_multiplier_w *= Machinegun.hastyBipodRecoilMultiplier;
			recoil_multiplier_h *= Machinegun.hastyBipodRecoilMultiplier;
		}
		else if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("tripod"))
		{
			recoil_multiplier_w *= Machinegun.tripodRecoilMultiplier;
			recoil_multiplier_h *= Machinegun.tripodRecoilMultiplier;
		}
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("heavy_barrel"))
		{
			recoil_multiplier_w *= Machinegun.recoilHBHorizontal;
			recoil_multiplier_h *= Machinegun.recoilHBVertical;
		}
		if(IIContent.itemMachinegun.getUpgrades(gun).hasKey("second_magazine"))
		{
			recoil_multiplier_w *= Machinegun.recoilSecondMagazine;
			hasSecondMag = true;
		}

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

		if(!magazine1.isEmpty())
			bullets1 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine1);
		if(!magazine2.isEmpty())
			bullets2 = IIContent.itemBulletMagazine.getRemainingBulletCount(magazine2);

		hasInfrared = IIContent.itemMachinegun.getUpgrades(gun).hasKey("infrared_scope");
		loadedFromCrate = IIContent.itemMachinegun.getUpgrades(gun).hasKey("belt_fed_loader");

		tank.setCapacity(tankCapacity);
		IFluidHandlerItem cap = gun.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
		if(cap!=null)
			tank.setFluid(cap.drain(Integer.MAX_VALUE, true));

		updateTank(false);

		NBTTagCompound tag = new NBTTagCompound();
		writeEntityToNBT(tag);
		IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, tag), IIPacketHandler.targetPointFromEntity(this, 24));
	}

	void dropItem()
	{
		if(!isDead)
		{
			ItemNBTHelper.setTagCompound(gun, "magazine1", magazine1.serializeNBT());
			ItemNBTHelper.setTagCompound(gun, "magazine2", magazine2.serializeNBT());
			if(IIContent.itemMachinegun.getCapacity(gun, 0) > 0)
			{
				IFluidHandlerItem cap = gun.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				if(cap!=null)
				{
					cap.drain(Integer.MAX_VALUE, true);
					if(tank.getFluid()!=null)
						cap.fill(tank.getFluid().copy(), true);
				}
			}
			blusunrize.immersiveengineering.common.util.Utils.dropStackAtPos(world, getPosition(), gun);
			setDead();
		}
	}

	protected void updateTank(boolean read)
	{
		if(read)
			readTank(dataManager.get(dataMarkerFluid));
		else
		{
			NBTTagCompound tag = new NBTTagCompound();
			writeTank(tag, false);
			dataManager.set(dataMarkerFluid, tag);
		}
	}

	public void writeTank(NBTTagCompound nbt, boolean toItem)
	{
		boolean write = tank.getFluidAmount() > 0;
		NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
		if(!toItem||write)
			nbt.setTag("tank", tankTag);
	}

	public void readTank(NBTTagCompound nbt)
	{
		if(nbt.hasKey("tank"))
		{
			tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
		tank.setCapacity(tankCapacity);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
	{
		if(tankCapacity > 0&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return true;
		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
	{
		if(tankCapacity > 0&&capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)fluidHandler;
		return super.getCapability(capability, facing);
	}

	@Override
	public String[] getOverlayText(EntityPlayer player, RayTraceResult mop)
	{
		if(blusunrize.immersiveengineering.common.util.Utils.isFluidRelatedItemStack(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			String s;
			if(tank.getFluid()!=null)
				s = tank.getFluid().getLocalizedName()+": "+tank.getFluidAmount()+"mB";
			else
				s = I18n.format(Lib.GUI+"empty");
			return new String[]{s};
		}
		return null;
	}


	@Override
	public boolean canRepair()
	{
		return maxShieldStrength > 0&&shieldStrength < maxShieldStrength;
	}

	@Override
	public boolean repair(int repairPoints)
	{
		shieldStrength = Math.min(shieldStrength += repairPoints, shieldStrength);
		return true;
	}

	@Override
	public int getRepairCost()
	{
		return 2;
	}

	@Override
	public IAdvancedZoomTool getZoom()
	{
		return SCOPE;
	}

	@Override
	public ItemStack getZoomStack()
	{
		return gun;
	}

	private class MachinegunZoom implements IAdvancedZoomTool
	{
		@Override
		public ResourceLocation getZoomOverlayTexture(ItemStack stack, EntityPlayer player)
		{
			NBTTagCompound nbt = IIContent.itemMachinegun.getUpgrades(stack);
			return nbt.hasKey("scope")?SIGHTS_TEXTURE: IR_SIGHTS_TEXTURE;
		}

		@Override
		public boolean shouldZoom(ItemStack stack, EntityPlayer player)
		{
			NBTTagCompound nbt = IIContent.itemMachinegun.getUpgrades(stack);
			return EntityMachinegun.this.aiming&&(nbt.hasKey("scope")||nbt.hasKey("infrared_scope"));
		}

		@Override
		public float[] getZoomSteps(ItemStack stack, EntityPlayer player)
		{
			return Machinegun.machinegunScopeMaxZoom;
		}
	}

	static class NonSidedFluidHandler implements IFluidHandler
	{
		EntityMachinegun machinegun;

		NonSidedFluidHandler(EntityMachinegun machinegun)
		{
			this.machinegun = machinegun;
		}

		@Override
		public int fill(FluidStack resource, boolean doFill)
		{
			if(resource==null)
				return 0;
			if(!MachinegunCoolantHandler.isValidCoolant(resource))
				return 0;

			int i = machinegun.tank.fill(resource, doFill);
			if(i > 0)
			{
				machinegun.updateTank(false);
			}
			return i;
		}

		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain)
		{
			if(resource==null)
				return null;
			return this.drain(resource.amount, doDrain);
		}

		@Override
		public FluidStack drain(int maxDrain, boolean doDrain)
		{
			FluidStack f = machinegun.tank.drain(maxDrain, doDrain);
			if(f!=null&&f.amount > 0)
			{
				machinegun.updateTank(false);
			}
			return f;
		}

		@Override
		public IFluidTankProperties[] getTankProperties()
		{
			return machinegun.tank.getTankProperties();
		}
	}

	@Override
	public double getYOffset()
	{
		return isRiding()?1D: super.getYOffset();
	}
}
