package pl.pabilo8.immersiveintelligence.common.entity.bullet;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.IEntityLightEventConsumer;
import com.elytradev.mirage.lighting.Light;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.common.IIConfigHandler.IIConfig.Weapons;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.bullets.*;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.EnumFuseTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.AmmoRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IISounds;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.messages.MessageEntityNBTSync;
import pl.pabilo8.immersiveintelligence.common.util.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.common.util.raytracer.MultipleRayTracer.MultipleTracerBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Pabilo8 on 30-08-2019.
 * Yes, I stole this one from Flan's Mod too! (Thanks Flan!)
 * Major update on 08-03-2020.
 * Total rewrite on 31-10-2020, almost no old code left
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "com.elytradev.mirage.lighting.IEntityLightEventConsumer", modid = "mirage")
public class EntityBullet extends Entity implements IEntityLightEventConsumer, IEntityAdditionalSpawnData
{
	public static final int MAX_TICKS = 600;
	public static final float DRAG = 0.01f;
	public static final float GRAVITY = 0.15f;

	//For testing purposes
	public static float DEV_SLOMO = 1f;
	public static boolean DEV_DECAY = true;

	public IAmmo bullet;
	public IAmmoCore core;
	public EnumCoreTypes coreType;
	public EnumFuseTypes fuseType = EnumFuseTypes.CONTACT;
	public int fuseParameter = 0;
	public IAmmoComponent[] components = new IAmmoComponent[0];
	public NBTTagCompound[] componentNBT = new NBTTagCompound[0];

	protected Entity shooter = null;

	boolean isPainted = false;
	//Set true only for artillery type bullets, forge can't give the mod unlimited tickets for each entity
	boolean shouldLoadChunks = false;
	//Additional motion variables, multiplied by force, that can decrease when the bullet hits an obstacle
	public double baseMotionX = 0, baseMotionY = 0, baseMotionZ = 0;

	//Whether the bullet has already played its flyby sound
	boolean flybySound = false;

	public double gravityMotionY = 0;

	/*
	Paint color and fuse is -1 if not existant
	 */
	public int paintColor = -1, fuse = -1;
	/*
	Penetration hardness is the base hardness the bullet core can penetrate
	Base damage is density*initial damage (NOT MASS)
	On hit, they are multiplied by core type bonuses, so a softpoint bullet has less penetration than an AP round, but has more damage vs unarmored targets
	 */
	public float penetrationHardness = 1, force = 1, initialForce = 1, baseDamage = 0, mass = 0;

	ArrayList<Entity> hitEntities = new ArrayList<>();
	ArrayList<BlockPos> hitPos = new ArrayList<>();

	protected boolean wasSynced;
	private Ticket ticket = null;

	public EntityBullet(World worldIn)
	{
		super(worldIn);
		hitEntities.add(this);
		setSize(0.5f, 0.5f);
		wasSynced = !worldIn.isRemote;
	}

	/**
	 * This is a general method, use {@link AmmoUtils#createBullet(World, ItemStack, Vec3d, Vec3d, float)} inside your code
	 */
	public EntityBullet(World worldIn, ItemStack stack, double x, double y, double z, float velocityMod, double motionX, double motionY, double motionZ)
	{
		super(worldIn);

		fromStack(stack);

		this.setPosition(x, y, z);
		this.force = bullet.getDefaultVelocity()*velocityMod;
		this.initialForce = bullet.getDefaultVelocity();
		this.baseMotionX = motionX;
		this.baseMotionY = motionY;
		this.baseMotionZ = motionZ;

		setMotion();

		//Should not be called on client
		if(!world.isRemote&&shouldLoadChunks)
		{
			ticket = ForgeChunkManager.requestTicket(ImmersiveIntelligence.INSTANCE, this.getEntityWorld(), Type.ENTITY);
			if(ticket!=null)
				ticket.bindEntity(this);
		}
	}

	public void setShooters(@Nonnull Entity shooter, Entity... others)
	{
		this.shooter = shooter;
		hitEntities.add(shooter);
		if(shooter.isRiding())
		{
			hitEntities.add(shooter.getLowestRidingEntity());
			hitEntities.addAll(shooter.getLowestRidingEntity().getRecursivePassengers());
		}
		hitEntities.addAll(Arrays.asList(others));
	}

	public void setShootPos(BlockPos... others)
	{
		hitPos.addAll(Arrays.asList(others));
	}

	private void setMotion()
	{
		this.motionX = baseMotionX*DEV_SLOMO*force;
		this.motionY = (baseMotionY*force+gravityMotionY)*DEV_SLOMO;
		this.motionZ = baseMotionZ*DEV_SLOMO*force;
	}

	protected void fromStack(ItemStack stack)
	{
		if(stack.getItem() instanceof IAmmo)
		{
			bullet = (IAmmo)stack.getItem();
			core = bullet.getCore(stack);
			coreType = bullet.getCoreType(stack);
			fuseType = bullet.getFuseType(stack);
			fuseParameter = bullet.getFuseParameter(stack);
			components = bullet.getComponents(stack);
			componentNBT = bullet.getComponentsNBT(stack);
			paintColor = bullet.getPaintColor(stack);

			if(fuseType==EnumFuseTypes.TIMED)
				fuse = fuseParameter;

			refreshBullet();
		}
	}

	private void refreshBullet()
	{
		if(core==null||bullet==null||coreType==null)
		{
			setDead();
			return;
		}
		penetrationHardness = fuseType==EnumFuseTypes.PROXIMITY?1: core.getPenetrationHardness();
		double compMass = 1d+Arrays.stream(components).mapToDouble(IAmmoComponent::getDensity).sum();
		compMass += core.getDensity();
		baseDamage = (float)(bullet.getDamage()*compMass*core.getDamageModifier());

		shouldLoadChunks = bullet.shouldLoadChunks();

		mass = bullet.getCoreMass(core, components);

		if(paintColor==-1)
			isPainted = false;

		setBulletSize();

	}

	public void onUpdateSuper()
	{
		super.onUpdate();
	}

	@Override
	public void onUpdate()
	{
		onUpdateSuper();

		if(isDead)
			return;

		if(!world.isRemote&&ticksExisted==1)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, nbt), IIPacketHandler.targetPointFromEntity(this, 32));
		}
		else if(world.isRemote&&!wasSynced)
			return;

		if(world.isRemote)
		{
			prevRotationPitch = this.rotationPitch;
			prevRotationYaw = this.rotationYaw;
		}
		else
		{
			if((!shouldLoadChunks&&ticksExisted > MAX_TICKS&&DEV_DECAY)||(posY < 0))
			{
				setDead();
				return;
			}
		}

		if(penetrationHardness==0)
		{
			baseMotionX = 0;
			baseMotionY = 0;
			baseMotionZ = 0;
			move(MoverType.SELF, 0, -GRAVITY, 0);
		}
		else
		{
			double realDrag = 1d-(DRAG*DEV_SLOMO);
			force *= realDrag;
			gravityMotionY -= GRAVITY*this.mass*DEV_SLOMO;
			gravityMotionY *= realDrag;
			setMotion();
			//IILogger.info(getPositionVector());

			MultipleRayTracer tracer = MultipleTracerBuilder.setPos(world, this.getPositionVector(), this.getNextPositionVector())
					.setAABB(this.getEntityBoundingBox().grow(fuseType==EnumFuseTypes.PROXIMITY?fuseParameter: 0).offset(this.getPositionVector().scale(-1)))
					.setFilters(this.hitEntities, this.hitPos)
					.volumetricTrace();

			int i = 0;
			penloop:
			for(RayTraceResult hit : tracer)
			{
				if(hit!=null)
					switch(hit.typeOfHit)
					{
						case BLOCK:
							BlockPos pos = hit.getBlockPos();
							hitPos.add(pos);
							if(!world.isRemote)
							{
								IPenetrationHandler penetrationHandler = PenetrationRegistry.getPenetrationHandler(world.getBlockState(pos));
								PenMaterialTypes penType = penetrationHandler.getPenetrationType();
								float pen = penetrationHardness*coreType.getPenMod(penType);
								float dmg = baseDamage*coreType.getDamageMod(penType)/4f;
								float hardness = world.getBlockState(pos).getBlockHardness(world, pos);

								if(hardness < 0)
									pen = -1;

								if(pen > hardness/penetrationHandler.getDensity())
								{
									//thanks lgmrszd
									if(!world.getBlockState(pos).getMaterial().isLiquid())
									{
										if(!world.isRemote)
										{
											AmmoUtils.dealBlockDamage(world, getBaseMotion(), dmg, pos, penetrationHandler);
											playHitSound(HitEffect.IMPACT, world, hit.getBlockPos(), penetrationHandler);
										}
									}

									penetrationHardness *= ((hardness*16f)/pen);
									force *= 0.85f;
								}
								else if(pen > 0)
								{
									if(!world.isRemote)
									{
										if(!world.getBlockState(pos).getMaterial().isLiquid())
										{
											AmmoUtils.dealBlockDamage(world, getBaseMotion(), dmg*(hardness/penetrationHandler.getDensity()), pos, penetrationHandler);
											playHitSound(HitEffect.IMPACT, world, hit.getBlockPos(), penetrationHandler);
										}
										if(fuse==-1)
											performEffect(hit);
									}

									stopAtPoint(hit);
									penetrationHardness = 0;
									break penloop;
								}
								else
								{
									//can't ricochet if penetrates multiple blocks
									if(pen!=-1&&force > hardness&&penetrationHandler.getPenetrationType().canRicochetOff()&&i==0)
									{
										ricochet(hardness/2f, pos);
										if(!world.isRemote)
											playHitSound(HitEffect.RICOCHET, world, hit.getBlockPos(), penetrationHandler);
									}
									else
									{
										if(!world.isRemote)
										{
											if(fuse==-1)
												performEffect(hit);
										}
										stopAtPoint(hit);

									}
									break penloop;
								}
							}

							break;
						case ENTITY:
							hitEntities.add(hit.entityHit);
							if(hit.entityHit==this)
								break;

							//just to be sure
							if(hit.entityHit!=null)
							{
								Entity e = hit.entityHit;

								if(e instanceof EntityBullet)
								{
									if(((EntityBullet)e).shooter!=this.shooter)
									{
										//you are already dead
										e.setDead();
										this.setDead();
										//nani?
										((EntityBullet)e).performEffect(new RayTraceResult(e));
										this.performEffect(new RayTraceResult(this));
										break penloop;
									}
								}
								else
								{
									int armor = 0;
									int toughness = 1;
									if(e instanceof EntityLivingBase)
									{
										armor = MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue());
										toughness += MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
									}
									PenMaterialTypes penType = (toughness > 0||armor > 0)?PenMaterialTypes.METAL: PenMaterialTypes.FLESH;
									float pen = penetrationHardness*coreType.getPenMod(penType)*Math.min(force, 1.15f);
									float dmg = baseDamage*coreType.getDamageMod(penType);

									//overpenetration
									if(pen > toughness*6f)
									{
										//armor not counted in
										if(!world.isRemote)
										{
											AmmoUtils.breakArmour(e, (int)(baseDamage*coreType.getPenMod(penType)/6f));
											e.hurtResistantTime = 0;
											if(!e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter, e), dmg))
											{
												performEffect(hit);
											}
										}
										stopAtPoint(hit);
										penetrationHardness *= ((toughness*4f)/pen);
										force *= 0.85f;
									}
									//penetration
									else if(pen > 0)
									{
										if(!world.isRemote)
										{
											float depth = (pen-(toughness*6f))/pen;

											AmmoUtils.breakArmour(e, (int)(baseDamage*coreType.getPenMod(penType)/8f));
											e.hurtResistantTime = 0;
											if(!e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter, e), dmg))
											{
												performEffect(hit);
											}
											penetrationHardness = 0;
											if(fuse==-1)
												performEffect(hit);
										}
										stopAtPoint(hit);
										break penloop;
									}
									//underpenetration + ricochet
									else
									{
										if(force > toughness*1.5f&&penType.canRicochetOff()&&i==0)
										{
											ricochet(toughness/2f, hit.getBlockPos());
											hitEntities.add(hit.entityHit);
										}
										else if(!world.isRemote)
										{
											if(fuse==-1)
												performEffect(hit);
										}
										stopAtPoint(hit);
										break penloop;
									}
								}
							}
							break;
					}
				i += 1;

			}
		}

		if(fuse!=-1&&ticksExisted > fuse)
		{
			performEffect(new RayTraceResult(this));
			return;
		}

		setMotion();
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		setPosition(posX, posY, posZ);

		//Play artillery shell flyby sound
		if(Weapons.artilleryImpactSound&&!flybySound&&ticksExisted > 5&&bullet.shouldLoadChunks())
		{
			if(motionY < 0)
			{
				BlockPos top = world.getTopSolidOrLiquidBlock(new BlockPos(getNextPositionVector())).up();
				IIPacketHandler.playRangedSound(world, new Vec3d(top), IISounds.artilleryImpact, SoundCategory.PLAYERS, 24, 0.75f, 1.3f);
				flybySound = true;
			}
		}

		if(world.isRemote)
		{
			if(penetrationHardness!=0)
			{
				Vec3d normalized = new Vec3d(motionX, motionY, motionZ).normalize();
				float motionXZ = MathHelper.sqrt(normalized.x*normalized.x+normalized.z*normalized.z);
				this.rotationYaw = (float)((Math.atan2(normalized.x, normalized.z)*180D)/3.1415927410125732D);
				this.rotationPitch = -(float)((Math.atan2(normalized.y, motionXZ)*180D)/3.1415927410125732D);

				for(int j = 0; j < components.length; j++)
				{
					IAmmoComponent c = components[j];
					if(c.hasTrail())
						c.spawnParticleTrail(this, componentNBT[j]);
				}
			}
		}
		else
			ForgeChunkManager.forceChunk(this.ticket, this.world.getChunkFromBlockCoords(this.getPosition()).getPos());
	}

	private void stopAtPoint(RayTraceResult hit)
	{
		gravityMotionY = 0;
		penetrationHardness = 0;
		baseMotionX = 0;
		baseMotionY = 0;
		baseMotionZ = 0;
		setPositionAndUpdate(hit.hitVec.x, hit.hitVec.y+height, hit.hitVec.z);
		setMotion();

		if(!world.isRemote)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			nbt.setBoolean("stopped", true);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, nbt), IIPacketHandler.targetPointFromEntity(this, 32));
		}
	}

	private void ricochet(float force, BlockPos currentPos)
	{
		this.force = force;
		baseMotionX *= -1;
		baseMotionY *= -1;
		hitEntities.clear();
		hitEntities.add(this);
		hitPos.clear();
		hitPos.add(currentPos);
	}

	protected void performEffect(RayTraceResult hit)
	{
		setDead();
		setPosition(hit.hitVec.x, hit.hitVec.y, hit.hitVec.z);
		float str = bullet.getComponentMultiplier()*core.getExplosionModifier()*coreType.getComponentEffectivenessMod();
		for(int i = 0; i < components.length; i++)
		{
			if(components[i]!=null&&i < componentNBT.length&&componentNBT[i]!=null)
				components[i].onEffect(str, coreType, componentNBT[i], hit.hitVec, new Vec3d(baseMotionX, baseMotionY, baseMotionZ).normalize(), world
				);
		}
	}

	public Entity getShooter()
	{
		return shooter;
	}

	public Vec3d getNextPositionVector()
	{
		return getPositionVector().addVector(motionX, motionY, motionZ);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox()
	{
		return getEntityBoundingBox();
	}

	@Override
	protected void entityInit()
	{

	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("basemotion_x"))
		{
			this.baseMotionX = compound.getDouble("basemotion_x");
			this.baseMotionY = compound.getDouble("basemotion_y");
			this.baseMotionZ = compound.getDouble("basemotion_z");
			this.force = compound.getFloat("force");
			this.initialForce = compound.getFloat("initalForce");

			float motionXZ = MathHelper.sqrt(baseMotionX*baseMotionX+baseMotionZ*baseMotionZ);
			this.rotationYaw = (float)((Math.atan2(baseMotionX, baseMotionZ)*180D)/3.1415927410125732D);
			this.rotationPitch = -(float)((Math.atan2(baseMotionY, motionXZ)*180D)/3.1415927410125732D);
		}

		if(compound.hasKey("casing"))
			this.bullet = AmmoRegistry.INSTANCE.getBulletItem(compound.getString("casing"));
		if(compound.hasKey("core"))
			this.core = AmmoRegistry.INSTANCE.getCore(compound.getString("core"));
		if(compound.hasKey("core_type"))
			this.coreType = EnumCoreTypes.v(compound.getString("core_type"));
		if(compound.hasKey("fuse_type"))
			this.fuseType = EnumFuseTypes.v(compound.getString("fuse_type"));
		if(compound.hasKey("fuse_parameter"))
			this.fuseParameter = compound.getInteger("fuse_parameter");

		if(compound.hasKey("components"))
		{
			ArrayList<IAmmoComponent> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)compound.getTag("components");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(AmmoRegistry.INSTANCE.getComponent(components.getStringTagAt(i)));
			this.components = arrayList.toArray(new IAmmoComponent[0]);
		}

		if(compound.hasKey("component_nbt"))
		{
			ArrayList<NBTTagCompound> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)compound.getTag("component_nbt");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(components.getCompoundTagAt(i));
			this.componentNBT = arrayList.toArray(new NBTTagCompound[0]);
		}

		if(compound.hasKey("paint_color"))
			this.paintColor = compound.getInteger("paint_color");

		refreshBullet();

		if(compound.hasKey("stopped"))
		{
			gravityMotionY = 0;
			penetrationHardness = 0;
			baseMotionX = 0;
			baseMotionY = 0;
			baseMotionZ = 0;
		}

		if(world.isRemote)
		{
			wasSynced = true;
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("casing", bullet.getName());
		compound.setString("core", core.getName());
		compound.setString("core_type", coreType.getName());
		compound.setString("fuse_type", fuseType.getName());
		compound.setInteger("fuse_parameter", fuseParameter);

		NBTTagList tagList = new NBTTagList();
		Arrays.stream(components).map(IAmmoComponent::getName).map(NBTTagString::new).forEachOrdered(tagList::appendTag);
		if(tagList.tagCount() > 0)
			compound.setTag("components", tagList);

		tagList = new NBTTagList();
		Arrays.stream(componentNBT).forEachOrdered(tagList::appendTag);
		if(tagList.tagCount() > 0)
			compound.setTag("component_nbt", tagList);

		compound.setInteger("paint_color", paintColor);

		compound.setDouble("basemotion_x", this.baseMotionX);
		compound.setDouble("basemotion_y", this.baseMotionY);
		compound.setDouble("basemotion_z", this.baseMotionZ);
		compound.setFloat("force", this.force);
		compound.setFloat("initalForce", this.initialForce);

	}

	private void playHitSound(HitEffect effect, World world, BlockPos pos, IPenetrationHandler handler)
	{
		IBlockState state = world.getBlockState(pos);
		SoundEvent event = handler.getSpecialSound(effect);
		if(event==null)
		{
			SoundType type = state.getBlock().getSoundType(state, world, pos, this);
			event = effect==HitEffect.IMPACT?type.getBreakSound(): type.getStepSound();
		}
		MinecraftServer server = world.getMinecraftServer();
		if(server!=null)
			server.getPlayerList().sendToAllNearExcept(null, posX, posY, posZ, 24D, this.world.provider.getDimension(), new SPacketSoundEffect(event, SoundCategory.BLOCKS, posX, posY, posZ, 0.5f, 1f));
	}

	public Vec3d getBaseMotion()
	{
		return new Vec3d(baseMotionX, baseMotionY, baseMotionZ);
	}

	private void setBulletSize()
	{
		float cal = bullet.getCaliber()/16f;
		this.width = cal;
		this.height = cal;
		cal *= 16f;
		setEntityBoundingBox(new AxisAlignedBB(-cal, -cal, -cal, cal, cal, cal));
	}

	@Override
	@SideOnly(Side.CLIENT)
	@Optional.Method(modid = "mirage")
	public void gatherLights(GatherLightsEvent evt, Entity entity)
	{
		if(this.wasSynced)
			for(int i = 0, componentsLength = components.length; i < componentsLength; i++)
			{
				IAmmoComponent component = components[i];
				if(component.hasTrail())
					evt.add(Light.builder().pos(this)
							.radius(bullet.getComponentMultiplier()*16f)
							.color(component.getNBTColour(componentNBT[i]), false)
							.build());
			}

	}

	@Override
	public boolean canBeCollidedWith()
	{
		return true;
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public void setFire(int seconds)
	{

	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		NBTTagCompound compound = new NBTTagCompound();
		writeEntityToNBT(compound);
		ByteBufUtils.writeTag(buffer, compound);
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		NBTTagCompound compound = ByteBufUtils.readTag(additionalData);
		if(compound!=null)
			readEntityFromNBT(compound);
	}

	@Override
	protected boolean makeFlySound()
	{
		return true;
	}

	//TODO: 20.04.2023 play sound
	@Override
	protected float playFlySound(float delay)
	{
//		this.playSound(IISounds.bulletWind, 2F, (1/this.bullet.getCaliber()));
		return delay+5;
	}
}
