package pl.pabilo8.immersiveintelligence.common.entity.bullets;

import blusunrize.immersiveengineering.common.util.FakePlayerUtil;
import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pl.pabilo8.immersiveintelligence.ImmersiveIntelligence;
import pl.pabilo8.immersiveintelligence.api.MultipleRayTracer;
import pl.pabilo8.immersiveintelligence.api.MultipleRayTracer.MultipleTracerBuilder;
import pl.pabilo8.immersiveintelligence.api.Utils;
import pl.pabilo8.immersiveintelligence.api.bullets.*;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.EnumCoreTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.BulletRegistry.PenMaterialTypes;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.HitEffect;
import pl.pabilo8.immersiveintelligence.api.bullets.PenetrationRegistry.IPenetrationHandler;
import pl.pabilo8.immersiveintelligence.common.IIDamageSources;
import pl.pabilo8.immersiveintelligence.common.network.IIPacketHandler;
import pl.pabilo8.immersiveintelligence.common.network.MessageEntityNBTSync;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Pabilo8 on 30-08-2019.
 * Yes, I stole this one from Flan's Mod too! (Thanks Flan!)
 * Major update on 08-03-2020.
 * Total rewrite on 31-10-2020, almost no old code left
 */
@net.minecraftforge.fml.common.Optional.Interface(iface = "elucent.albedo.lighting.ILightProvider", modid = "albedo")
public class EntityBullet extends Entity implements ILightProvider
{
	public static final int MAX_TICKS = 600;
	public static final float DRAG = 0.98f;
	public static final float GRAVITY = 0.1f;

	//For testing purposes
	public static float DEV_SLOMO = 1f;
	public static boolean DEV_DECAY = true;

	public IBullet bulletCasing;
	public IBulletCore bulletCore;
	public EnumCoreTypes bulletCoreType;
	public IBulletComponent[] components = new IBulletComponent[0];
	public NBTTagCompound[] componentNBT = new NBTTagCompound[0];

	private Entity shooter = null;

	boolean isPainted = false;
	//Set true only for artillery type bullets, forge can't give the mod unlimited tickets for each entity
	boolean shouldLoadChunks = false;
	//Additional motion variables, multiplied by force, that can decrease when the bullet hits an obstacle
	public double baseMotionX = 0, baseMotionY = 0, baseMotionZ = 0;

	public double gravityMotionY = 0;

	/*
	Paint color and fuse is -1 if not existant
	Durability is
	 */
	public int paintColor = -1, fuse = -1, durability = 1;
	/*
	Penetration hardness is the base hardness the bullet core can penetrate
	Base damage is density*initial damage (NOT MASS)
	On hit, they are multiplied by core type bonuses, so a softpoint bullet has less penetration than an AP round, but has more damage vs unarmored targets
	 */
	public float penetrationHardness = 1, force = 1, baseDamage = 0, mass = 0;

	ArrayList<Entity> hitEntities = new ArrayList<>();
	ArrayList<BlockPos> hitPos = new ArrayList<>();

	private boolean wasSynced;
	private Ticket ticket = null;

	public EntityBullet(World worldIn)
	{
		super(worldIn);
		hitEntities.add(this);
		setSize(0.5f, 0.5f);
		wasSynced = !worldIn.isRemote;
	}

	/**
	 * use $link{}
	 */
	public EntityBullet(World worldIn, ItemStack stack, double x, double y, double z, float force, double motionX, double motionY, double motionZ)
	{
		super(worldIn);

		fromStack(stack);

		this.setPosition(x, y, z);
		this.force = force;
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

	private void fromStack(ItemStack stack)
	{
		if(stack.getItem() instanceof IBullet)
		{
			bulletCasing = (IBullet)stack.getItem();
			bulletCore = bulletCasing.getCore(stack);
			bulletCoreType = bulletCasing.getCoreType(stack);
			components = bulletCasing.getComponents(stack);
			componentNBT = bulletCasing.getComponentsNBT(stack);
			paintColor = bulletCasing.getPaintColor(stack);

			refreshBullet();
		}
	}

	private void refreshBullet()
	{
		penetrationHardness = bulletCore.getPenetrationHardness();
		double compMass = 1d+Arrays.stream(components).mapToDouble(IBulletComponent::getDensity).sum();
		compMass += bulletCore.getDensity();
		baseDamage = (float)(bulletCasing.getDamage()*compMass*bulletCore.getDamageModifier());

		shouldLoadChunks = bulletCasing.shouldLoadChunks();

		mass = bulletCasing.getMass(bulletCore, components);

		if(paintColor==-1)
			isPainted = false;

		setBulletSize();

	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if(!world.isRemote&&ticksExisted==1)
		{
			if(this.shooter==null)
				this.shooter = FakePlayerUtil.getFakePlayer(world);

			NBTTagCompound nbt = new NBTTagCompound();
			writeEntityToNBT(nbt);
			IIPacketHandler.INSTANCE.sendToAllAround(new MessageEntityNBTSync(this, nbt), Utils.targetPointFromEntity(this, 32));
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

		if(isDead)
			return;


		if(penetrationHardness==0)
		{
			baseMotionX = 0;
			baseMotionY = 0;
			baseMotionZ = 0;
		}
		else
		{
			// TODO: 21.11.2020 find a way of decreasing force, without making the bullet stop in midair
			//current works, though
			force *= DRAG;
			gravityMotionY -= GRAVITY*this.mass*DEV_SLOMO;
			setMotion();

			MultipleRayTracer tracer = MultipleTracerBuilder.setPos(world, this.getPositionVector(), this.getNextPositionVector())
					.setAABB(this.getEntityBoundingBox().offset(this.getPositionVector().scale(-1)))
					.setFilters(this.hitEntities, this.hitPos)
					.volumetricTrace();

			int i = 0;
			penloop:
			for(RayTraceResult hit : tracer.hits)
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
								float pen = penetrationHardness*bulletCoreType.getPenMod(penType)*force;
								float dmg = baseDamage*bulletCoreType.getDamageMod(penType)/4f;
								float hardness = world.getBlockState(pos).getBlockHardness(world, pos);
								if(hardness < 0)
									hardness = Integer.MAX_VALUE+hardness;

								if(pen > hardness/penetrationHandler.getDensity())
								{
									if(!world.isRemote)
										BulletHelper.dealBlockDamage(world, dmg, pos, penetrationHandler);
									penetrationHardness *= ((hardness*1.5f)/pen);
									force *= 0.85f;
								}
								else if(pen > 0)
								{
									if(!world.isRemote)
									{
										BulletHelper.dealBlockDamage(world, dmg*(hardness/penetrationHandler.getDensity()), pos, penetrationHandler);
										if(fuse==-1)
											performEffect();
									}
									stopAtPoint(hit);
									penetrationHardness = 0;
									break penloop;
								}
								else
								{
									//can't ricochet if penetrates multiple blocks
									if(force > hardness&&penetrationHandler.getPenetrationType().canRicochetOff()&&i==0)
										ricochet(hardness/2f, pos);
									else
									{
										if(!world.isRemote)
										{
											if(fuse==-1)
												performEffect();
										}
										stopAtPoint(hit);

									}
									break penloop;
								}
							}

							break;
						case ENTITY:
							hitEntities.add(hit.entityHit);
							//just to be sure
							if(hit.entityHit!=null)
							{
								Entity e = hit.entityHit;
								int armor = 0;
								int toughness = 1;
								if(e instanceof EntityLivingBase)
								{
									armor = MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR).getAttributeValue());
									toughness += MathHelper.floor(((EntityLivingBase)e).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
								}
								PenMaterialTypes penType = (toughness > 0||armor > 0)?PenMaterialTypes.METAL: PenMaterialTypes.FLESH;
								float pen = penetrationHardness*bulletCoreType.getPenMod(penType)*Math.min(force, 1.15f);
								float dmg = baseDamage*bulletCoreType.getDamageMod(penType);

								//overpenetration
								if(pen > toughness*6f)
								{
									//armor not counted in
									if(!world.isRemote)
									{
										BulletHelper.breakArmour(e, (int)(baseDamage*bulletCoreType.getPenMod(penType)/6f));
										e.hurtResistantTime = 0;
										e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter), dmg);
									}
									penetrationHardness *= ((toughness*4f)/pen);
									force *= 0.85f;
								}
								//penetration
								else if(pen > 0)
								{
									if(!world.isRemote)
									{
										float depth = (pen-(toughness*6f))/pen;

										BulletHelper.breakArmour(e, (int)(baseDamage*bulletCoreType.getPenMod(penType)/8f));
										e.hurtResistantTime = 0;
										e.attackEntityFrom(IIDamageSources.causeBulletDamage(this, this.shooter), Math.max(dmg-(armor*depth), 0));
										penetrationHardness = 0;
										if(fuse==-1)
											performEffect();
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
											performEffect();
									}
									stopAtPoint(hit);
									break penloop;
								}


							}
							break;
					}
				i += 1;

			}
		}

		if(fuse!=-1&&ticksExisted > fuse)
		{
			performEffect();
			return;
		}

		setMotion();
		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		setPosition(posX, posY, posZ);

		if(world.isRemote)
		{
			if(penetrationHardness!=0)
			{
				float motionXZ = MathHelper.sqrt(baseMotionX*baseMotionX+baseMotionZ*baseMotionZ);
				this.rotationYaw = (float)((Math.atan2(baseMotionX, baseMotionZ)*180D)/3.1415927410125732D);
				this.rotationPitch = -(float)((Math.atan2(baseMotionY, motionXZ)*180D)/3.1415927410125732D);

				for(int j = 0; j < components.length; j++)
				{
					IBulletComponent c = components[j];
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

	private void performEffect()
	{
		float str = bulletCasing.getComponentCapacity()*bulletCore.getExplosionModifier()*bulletCoreType.getComponentEffectivenessMod();
		for(int i = 0; i < components.length; i++)
		{
			components[i].onExplosion(str, componentNBT[i], world, this.getPosition(), this);
		}
		setDead();
	}

	public Vec3d getNextPositionVector()
	{
		return getPositionVector().addVector(motionX, motionY, motionZ);
	}

	@SideOnly(Side.CLIENT)
	private void onUpdateClient()
	{
		//TODO: Play flyby sound
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
		}

		if(compound.hasKey("casing"))
			this.bulletCasing = BulletRegistry.INSTANCE.getCasing(compound.getString("casing"));
		if(compound.hasKey("core"))
			this.bulletCore = BulletRegistry.INSTANCE.getCore(compound.getString("core"));
		if(compound.hasKey("core_type"))
			this.bulletCoreType = EnumCoreTypes.v(compound.getString("core_type"));

		if(compound.hasKey("components"))
		{
			ArrayList<IBulletComponent> arrayList = new ArrayList<>();
			NBTTagList components = (NBTTagList)compound.getTag("components");
			for(int i = 0; i < components.tagCount(); i++)
				arrayList.add(BulletRegistry.INSTANCE.getComponent(components.getStringTagAt(i)));
			this.components = arrayList.toArray(new IBulletComponent[0]);
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

		if(world.isRemote)
		{
			wasSynced = true;
			bulletCasing.doPuff(this);
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setString("casing", bulletCasing.getName());
		compound.setString("core", bulletCore.getName());
		compound.setString("core_type", bulletCoreType.getName());

		NBTTagList tagList = new NBTTagList();
		Arrays.stream(components).map(IBulletComponent::getName).map(NBTTagString::new).forEachOrdered(tagList::appendTag);
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

	}

	private void playHitSound(HitEffect effect, World world, BlockPos pos, IPenetrationHandler handler)
	{
		IBlockState state = world.getBlockState(pos);
		SoundEvent event = handler.getSpecialSound(effect);
		if(event==null)
		{
			SoundType type = state.getBlock().getSoundType(state, world, pos, this);
			event = effect==HitEffect.PENETRATION?type.getBreakSound(): type.getStepSound();
		}
		world.getMinecraftServer().getPlayerList().sendToAllNearExcept(null, posX, posY, posZ, 24D, this.world.provider.getDimension(), new SPacketSoundEffect(event, SoundCategory.BLOCKS, posX, posY, posZ, 1f, 1f));
	}

	public Vec3d getBaseMotion()
	{
		return new Vec3d(baseMotionX, baseMotionY, baseMotionZ);
	}

	private void setBulletSize()
	{
		float cal = bulletCasing.getCaliber();
		this.width = cal;
		this.height = cal;
		cal *= 16f;
		setEntityBoundingBox(new AxisAlignedBB(-cal, -cal, -cal, cal, cal, cal));
	}

	@Override
	public Light provideLight()
	{
		if(this.wasSynced)
			for(int j = 0; j < components.length; j++)
			{
				IBulletComponent c = components[j];
				if(c.hasTrail())
				{
					return Light.builder().pos(this).radius(bulletCasing.getComponentCapacity()*16f).color(c.getNBTColour(componentNBT[j]), false).build();
				}
			}
		return null;
	}
}
